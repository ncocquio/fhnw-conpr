/*
 * Copyright (c) 2000-2018 Fachhochschule Nordwestschweiz (FHNW)
 * All Rights Reserved. 
 */

package bank.gui.tests;

import java.util.Iterator;
import java.util.Set;

import bank.Account;
import bank.Bank;
import bank.InactiveException;
import bank.OverdrawException;

public class TransferTest implements BankTest {

	@Override
	public String getName() {
		return "Transfer Test";
	}

	@Override
	public boolean isEnabled(int size) {
		return size >= 2;
	}

	@Override
	public String runTests(final Bank bank, String currentAccountNumber) throws Exception {
		Set<String> s = bank.getAccountNumbers();
		Iterator<String> it = s.iterator();
		final Account a1 = bank.getAccount(it.next());
		final Account a2 = bank.getAccount(it.next());

		String msg = null;
		
		if (msg == null) {
			double bal1 = a1.getBalance();
			double bal2 = a2.getBalance();

			try {
				bank.transfer(a1, a2, bal1 + 1);
			} catch (InactiveException e) {
				/* should not be thrown */
				msg = "an InactiveException should not be thrown; probably method getAccountNumbers returns numbers of inactive accounts.";
			} catch (OverdrawException e) {
				/* expected */
			}

			if (bal1 != a1.getBalance() || bal2 != a2.getBalance()) {
				msg = "your implementation of transfer is not correct";
			}
		}

		if (msg == null) {
			String id = bank.createAccount("Test");
			Account a = a1;
			Account b = bank.getAccount(id);
			a.deposit(1);
			double bal = a.getBalance();
			bank.closeAccount(id);
			try {
				bank.transfer(a, b, 1);
				msg = "an InactiveException was expected";
			} catch(InactiveException e) {
				// OK
			} catch(Exception e) {
				msg = "an InactiveException was expected but an exception of type " + e.getClass().getSimpleName() + " was thrown.";
			}
			if(msg == null && bal != a.getBalance()) {
				msg = "money got lost during a transfer operation";
			}
			if(msg == null) {
				a.withdraw(1);
			}
		}

        if (msg == null) {
            String id1 = bank.createAccount("Test1");
            String id2 = bank.createAccount("Test2");
            Account x1 = bank.getAccount(id1);
            Account x2 = bank.getAccount(id2);
            x1.deposit(100);
            try {
                bank.transfer(x1, x2, 40);
            } catch (Exception e) {
                msg = "no exception was expected";
            }
            if (x1.getBalance() != 60 || x2.getBalance() != 40) {
                msg = "your implementation of transfer is not correct";
            }
        }

        if (msg == null) {
			msg = "found no errors in the implementation of transfer";
		}

		return msg;
	}
}
