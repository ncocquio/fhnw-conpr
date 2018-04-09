package blockingQueue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class OrderProcessing {
	
	public static void main(String[] args) {
		int nCustomers = 10;
		int nValidators = 2;
		int nProcessors = 3;

        final BlockingQueue<Order> customerQueue = new ArrayBlockingQueue<>(10);
        final BlockingQueue<Order> validatorQueue = new ArrayBlockingQueue<>(10);

		for (int i = 0; i < nCustomers; i++) {
			new Customer("" + i, customerQueue).start();
		}

		for (int i = 0; i < nValidators; i++) {
			new OrderValidator(customerQueue, validatorQueue).start();
		}

		for (int i = 0; i < nProcessors; i++) {
			new OrderProcessor(validatorQueue).start();
		}
	}
	
	static class Order {
		public final String customerName;
		public final int itemId;
		public Order(String customerName, int itemId) {
			this.customerName = customerName;
			this.itemId = itemId;
		}
		
		@Override
		public String toString() {
			return "Order: [name = " + customerName + " ], [item = " + itemId +" ]";  
		}
	}
	
	
	static class Customer extends Thread {
        private final BlockingQueue<Order> customerQueue;

		public Customer(String name, BlockingQueue<Order> customerQueue) {
            super(name);
            this.customerQueue = customerQueue;
		}

		private Order createOrder() {
			Order o = new Order(getName(), (int) (Math.random()*100));
			System.out.println("Created:   " + o);
			return o;
		}
		
		private void handOverToValidator(Order o) throws InterruptedException {
            customerQueue.put(o);
		}
		
		@Override
		public void run() {
			try {
				while(true) {
					Order o = createOrder();
					handOverToValidator(o);
					Thread.sleep((long) (Math.random()*1000));
				}
			} catch (InterruptedException e) {}
		}
	}
	
	
	static class OrderValidator extends Thread {
        private final BlockingQueue<Order> customerQueue;
        private final BlockingQueue<Order> validatorQueue;

		public OrderValidator(BlockingQueue<Order> customerQueue, BlockingQueue<Order> validatorQueue) {
		    this.customerQueue = customerQueue;
		    this.validatorQueue = validatorQueue;
        }
		
		public Order getNextOrder() throws InterruptedException {
            return customerQueue.take();
		}
		
		public boolean isValid(Order o) {
			return o.itemId < 50;
		}
		
		public void handOverToProcessor(Order o) throws InterruptedException {
			validatorQueue.put(o);
		}
		
		@Override
		public void run() {
			try {
				while(true) {
					Order o = getNextOrder();
					if(isValid(o)) {
						handOverToProcessor(o);
					} else {
						System.err.println("Destroyed: " + o);
					}
					Thread.sleep((long) (Math.random()*1000));
				}
			} catch (InterruptedException e) {}
		}
	}
	
	
	static class OrderProcessor extends Thread {
        private final BlockingQueue<Order> validatorQueue;

		public OrderProcessor(BlockingQueue<Order> validatorQueue) {
		    this.validatorQueue = validatorQueue;
        }
		
		public Order getNextOrder() throws InterruptedException {
			return validatorQueue.take();
		}
		
		public void processOrder(Order o) {
			System.out.println("Processed: " + o);
		}
		
		@Override
		public void run() {
			try {
				while(true) {
					Order o = getNextOrder();
					processOrder(o);
					Thread.sleep((long) (Math.random()*1000));
				}
			} catch (InterruptedException e) {}
		}
	}
}
