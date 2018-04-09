package latch;

public class Restaurant {
	
	public static void main(String[] args) {
		int nrGuests = 2;
				
		new Cook(/*TODO */).start();
		
		for(int i = 0; i < nrGuests; i++) {
			new Guest(/*TODO */).start();
		}
		
		new DishWasher(/*TODO */).start();
	}
	
	
	static class Cook extends Thread {
		public Cook(/*TODO */) {}
		
		@Override
		public void run() {
			System.out.println("Start Cooking..");
			try {
				sleep(5000);
			} catch (InterruptedException e) {}
			System.out.println("Meal is ready");
			/*TODO */
		}
	}
	
	
	static class Guest extends Thread {
		public Guest(/*TODO */) {}
		
		@Override
		public void run() {
			try {
				sleep(1000);
				System.out.println("Entering restaurant and placing order.");
				/*TODO */
				System.out.println("Enjoying meal.");
				sleep(5000);
				System.out.println("Meal was excellent!");
				/*TODO */
			} catch (InterruptedException e) {}
		}
	}
	
	
	static class DishWasher extends Thread {
		public DishWasher(/*TODO */) {}
		
		@Override
		public void run() {
			try {
				System.out.println("Waiting for dirty dishes.");
				/*TODO */
				System.out.println("Washing dishes.");
				sleep(0);
			} catch (InterruptedException e) {}
		}
	}
}
