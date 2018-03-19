package le;

import java.util.HashMap;
import java.util.Map;

public class Bank {
	private static int id = 0;
	private Map<String, Account> map = new HashMap<>();

	public Account getAccount(String id) {
		return map.get(id);
	}

	public String createAccount(String name) {
		Account a = new Account(name);
		map.put(a.getId(), a);
		return a.getId();
	}

	public static class Account {
		private String id; 
		private String name;

		public Account(String name) { 
			this.id = "ID-" + Bank.id++; 
			this.name = name;
		} 

		public String getId() {
			return id;
		}

		public String getName() {
			return name;
		}
	}
}
