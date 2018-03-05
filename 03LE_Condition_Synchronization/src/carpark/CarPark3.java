package carpark;

import net.jcip.annotations.GuardedBy;

public class CarPark3 implements CarPark {
	@GuardedBy("this")
	private int places;
	
	public CarPark3(int places){
		this.places = places;
	}
	
	private boolean isFull(){
		return places == 0;
	}

	@Override
	public void enter() {
		while (true) {
			synchronized (this) {
				if (!isFull()) {
					log("enter carpark");
					places--;
					return;
				}
			}
			sleep(10);
		}
	}

	@Override
	public synchronized void exit() {
		log("exit carpark");
		places++;
	}

	private void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args){
		CarPark cp = new CarPark3(2);
		Car c1 = new Car("car1", cp); c1.start();
		Car c2 = new Car("car2", cp); c2.start();
		Car c3 = new Car("car3", cp); c3.start();
		Car c4 = new Car("car4", cp); c4.start();
	}
	
	private void log(String msg){
		System.out.println(Thread.currentThread().getName() + " " + msg);
	}
}
