/*
 * Copyright (c) 2000-2018 Fachhochschule Nordwestschweiz (FHNW)
 * All Rights Reserved.
 */

package bank.local;

/* Simple Server -- not thread safe */

import bank.Account;
import bank.Bank;
import bank.InactiveException;
import bank.OverdrawException;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ConprBankDriver implements bank.BankDriver {
    private ConprBank bank = null;

    @Override
    public void connect(String[] args) {
        bank = new ConprBank();
    }

    @Override
    public void disconnect() {
        bank = null;
    }

    @Override
    public bank.Bank getBank() {
        return bank;
    }
}

class ConprBank implements Bank {

    private Map<String, ConprAccount> accounts = new ConcurrentHashMap<>();

    @Override
    public Set<String> getAccountNumbers() {
        Set<String> activeAccountNumbers = new HashSet<>();
        for (ConprAccount acc : accounts.values()) {
            if (acc.isActive()) {
                activeAccountNumbers.add(acc.getNumber());
            }
        }
        return activeAccountNumbers;
    }

    @Override
    public String createAccount(String owner) {
        synchronized (this) {
            final ConprAccount a = new ConprAccount(owner, accounts.size() + 1);
            accounts.put(a.getNumber(), a);
            return a.getNumber();
        }
    }

    @Override
    public boolean closeAccount(String number) {
        final ConprAccount a = accounts.get(number);
        return a != null && a.passivate();
    }

    @Override
    public Account getAccount(String number) {
        return accounts.get(number);
    }

    @Override
    public void transfer(bank.Account from, bank.Account to, double amount)
            throws IOException, InactiveException, OverdrawException {

        Account lowerAccountNumber = from;
        Account higherAccountNumber = to;

        if (lowerAccountNumber.getNumber().compareTo(higherAccountNumber.getNumber()) > 0) {
            Account tmpAccountNumber = higherAccountNumber;
            higherAccountNumber = lowerAccountNumber;
            lowerAccountNumber = tmpAccountNumber;
        }

        synchronized (lowerAccountNumber) {
            synchronized (higherAccountNumber) {
                if (from.isActive() && to.isActive()) {
                    from.withdraw(amount);
                    to.deposit(amount);
                } else {
                    throw new InactiveException("");
                }
            }
        }
    }
}

class ConprAccount implements bank.Account {

    private String number;
    private String owner;
    private double balance;
    private boolean active = true;

    ConprAccount(String owner, int number) {
        this.owner = owner;
        this.number = "CONPR_ACC_" + number;
    }

    @Override
    public double getBalance() {
        synchronized (this) {
            return balance;
        }
    }

    @Override
    public String getOwner() {
        return owner;
    }

    @Override
    public String getNumber() {
        return number;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    boolean passivate() {
        synchronized (this) {
            if (balance != 0 || !active) {
                return false;
            } else {
                active = false;
                return true;
            }
        }
    }

    @Override
    public void deposit(double amount) throws InactiveException {
        synchronized (this) {
            if (!active)
                throw new InactiveException("account not active");
            if (amount < 0)
                throw new IllegalArgumentException("negative amount");
            balance += amount;
        }
    }

    @Override
    public void withdraw(double amount) throws InactiveException, OverdrawException {
        synchronized (this) {
            if (!active)
                throw new InactiveException("account not active");
            if (amount < 0)
                throw new IllegalArgumentException("negative amount");
            if (balance - amount < 0)
                throw new OverdrawException("account cannot be overdrawn");
            balance -= amount;
        }
    }

}
