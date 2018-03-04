/*
 * Copyright (c) 2000-2018 Fachhochschule Nordwestschweiz (FHNW)
 * All Rights Reserved. 
 */

package bank.gui.tests;

import bank.Account;
import bank.Bank;

public abstract class EfficiencyTest implements BankTest {
	private final int numberOfTests;

	protected EfficiencyTest(int numberOfTests) {
		this.numberOfTests = numberOfTests;
	}

	@Override
	public boolean isEnabled(int size) {
		return size > 0;
	}

	@Override
	public String runTests(Bank bank, String currentAccountNumber) throws Exception {
		final Account acc = bank.getAccount(currentAccountNumber);

		String msg;
		try {
			System.gc();
			long st = System.currentTimeMillis();
			for (int i = 1; i <= numberOfTests; i++) {
				acc.deposit(i);
				acc.withdraw(i);
			}
			st = System.currentTimeMillis() - st;
			msg = 2 * numberOfTests + " operations in " + st / 1000.0 + " Sek\n" + st / (2.0 * numberOfTests) + " msec/op";
		} catch (Exception e) {
			msg = "test did throw an exception\n" + e.getMessage();
		}
		return msg;
	}

}
