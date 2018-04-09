package latch;

import java.util.concurrent.CountDownLatch;

public class Restaurant {
	
	public static void main(String[] args) {
		int nrGuests = 2;

		final CountDownLatch cookingDone = new CountDownLatch(1);
		final CountDownLatch eatingDone = new CountDownLatch(nrGuests);
				
		new Cook(cookingDone).start();
		
		for(int i = 0; i < nrGuests; i++) {
			new Guest(cookingDone, eatingDone).start();
		}
		
		new DishWasher(eatingDone).start();
	}
	
	
	static class Cook extends Thread {
	    private final CountDownLatch cookingDone;

		public Cook(CountDownLatch cookingDone) {
		    this.cookingDone = cookingDone;
        }
		
		@Override
		public void run() {
			System.out.println("Start Cooking..");
			try {
				sleep(5000);
			} catch (InterruptedException e) {}
			System.out.println("Meal is ready");
			cookingDone.countDown();
		}
	}
	
	
	static class Guest extends Thread {
        private final CountDownLatch cookingDone;
        private final CountDownLatch eatingDone;

        public Guest(CountDownLatch cookingDone, CountDownLatch eatingDone) {
            this.cookingDone = cookingDone;
            this.eatingDone = eatingDone;
        }

        @Override
		public void run() {
			try {
				sleep(1000);
				System.out.println("Entering restaurant and placing order.");
				cookingDone.await();
				System.out.println("Enjoying meal.");
				sleep(5000);
				System.out.println("Meal was excellent!");
				eatingDone.countDown();
			} catch (InterruptedException e) {}
		}
	}
	
	
	static class DishWasher extends Thread {
        private final CountDownLatch eatingDone;

        public DishWasher(CountDownLatch eatingDone) {
            this.eatingDone = eatingDone;
        }

        @Override
		public void run() {
			try {
				System.out.println("Waiting for dirty dishes.");
				eatingDone.await();
				System.out.println("Washing dishes.");
				sleep(0);
			} catch (InterruptedException e) {}
		}
	}
}
