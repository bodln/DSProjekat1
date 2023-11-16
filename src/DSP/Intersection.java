package DSP;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Intersection implements Runnable {
	public Lock lock = new ReentrantLock();
	public Condition greenLight;
	public Condition redLight;
	public boolean trafficLight;
	public ArrayList<Car> queue;
	public ArrayList<Road> roads;
	
	Intersection(){
		trafficLight = false;
		greenLight = lock.newCondition();
		redLight = lock.newCondition();
		this.queue = new ArrayList<Car>();
		this.roads = new ArrayList<Road>();
	}

	public void handleNS(Car c) {
		lock.lock();
		System.out.println("Car " + c.id + " is moving from " + c.origin + " to " + c.destination + ".");
		while(!trafficLight) {
			try {
				System.out.println("Car " + c.id + " is waiting for the N-S route light to turn green.");
				greenLight.await();
				System.out.println("Car " + c.id + " is moving towards road " + c.destination + ".");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		change(c);
		lock.unlock();
	}
	
	public void handleEW(Car c) {
		lock.lock();
		System.out.println("Car " + c.id + " is moving from " + c.origin + " to " + c.destination + ".");
		while(trafficLight) {
			try {
				System.out.println("Car " + c.id + " is waiting for the N-S route light to turn red.");
				redLight.await();
				System.out.println("Car " + c.id + " is moving towards road " + c.destination + ".");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		change(c);
		lock.unlock();
	}
	
	public void change(Car car) {
		
		
		System.out.println("Car " + car.id + " has entered the " + car.destination + " road.");
		for (Road road : roads) {
            if (road.direction.equals(car.destination)) {
                road.cars.add(car);
                queue.remove(car);
                break;
            }
        }
		
		for (Road road : roads) {
            if (road.direction.equals(car.origin)) {
                road.cars.remove(car);
                break;
            }
        }
		displayRoads();
	}
	
	public void displayRoads() {
		lock.lock();
		try {
			System.out.println();
			System.out.println();
			
			//System.out.println("Traffic light is: " + (trafficLight ? "green" : "red"));
			
			for (Road road : roads) {
				if(road.direction.equals("N")) {
					System.out.println("              |N|");
					System.out.println("              |" + (road.cars.size() >= 6 ? road.cars.get(5).id : " ") + "|");
					System.out.println("              |" + (road.cars.size() >= 5 ? road.cars.get(4).id  : " ") + "|");
					System.out.println((trafficLight ? "      Green   "  : "         Red  ") + "|" + (road.cars.size() >= 4 ? road.cars.get(3).id  : " ") + "|");
					System.out.println("              |" + (road.cars.size() >= 3 ? road.cars.get(2).id  : " ") + "|");
					System.out.println("              |" + (road.cars.size() >= 2 ? road.cars.get(1).id  : " ") + "|");
					System.out.println("              |" + (road.cars.size() >= 1 ? road.cars.get(0).id  : " ") + "|");
				}
			}
			for (Road road : roads) {
				if(road.direction.equals("W")) {
					System.out.print("-------------    -------------");
					System.out.println();
					System.out.print("W" + (road.cars.size() >= 6 ? road.cars.get(5).id  : " ") + " " 
					+ (road.cars.size() >= 5 ? road.cars.get(4).id  : " ") + " "
					+ (road.cars.size() >= 4 ? road.cars.get(3).id  : " ") + " "
					+ (road.cars.size() >= 3 ? road.cars.get(2).id  : " ") + " "
					+ (road.cars.size() >= 2 ? road.cars.get(1).id  : " ") + " "
					+ (road.cars.size() >= 1 ? road.cars.get(0).id  : " ") + "      ");
				}
			}
			for (Road road : roads) {
				if(road.direction.equals("E")) {
					System.out.print("" + (road.cars.size() >= 1 ? road.cars.get(0).id  : " ") + " " 
							+ (road.cars.size() >= 2 ? road.cars.get(1).id  : " ") + " "
							+ (road.cars.size() >= 3 ? road.cars.get(2).id  : " ") + " "
							+ (road.cars.size() >= 4 ? road.cars.get(3).id  : " ") + " "
							+ (road.cars.size() >= 5 ? road.cars.get(4).id  : " ") + " "
							+ (road.cars.size() >= 6 ? road.cars.get(5).id  : " ") + " "
							+ "E");
				}
			}
			System.out.println();
			System.out.print("-------------    -------------");
			System.out.println();
			for (Road road : roads) {
				if(road.direction.equals("S")) {
					System.out.println("              |" + (road.cars.size() >= 1 ? road.cars.get(0).id : " ") + "|");
					System.out.println("              |" + (road.cars.size() >= 2 ? road.cars.get(1).id  : " ") + "|");
					System.out.println("              |" + (road.cars.size() >= 3 ? road.cars.get(2).id  : " ") + "|");
					System.out.println("              |" + (road.cars.size() >= 4 ? road.cars.get(3).id  : " ") + "|" + (trafficLight ? "   Green"  : "  Red"));
					System.out.println("              |" + (road.cars.size() >= 5 ? road.cars.get(4).id  : " ") + "|");
					System.out.println("              |" + (road.cars.size() >= 6 ? road.cars.get(5).id  : " ") + "|");
					System.out.println("              |S|");
				}
			}
		} finally {
			lock.unlock();
		}
		
		
	}
	
	//N-S is the traffic light managed road, and this function checks if there are any cars on that route
	public boolean checkMainRoad() {
		for(Car c: queue) {
			if((c.origin.equals("N") && c.destination.equals("S")) || (c.origin.equals("S") && c.destination.equals("N"))) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lock.lock();
            try {
                trafficLight = !trafficLight;
                if (trafficLight) {
                    System.out.println("The light has turned green on the route N-S.");
                    greenLight.signalAll();
                } else {
                    System.out.println("The light has turned red on the route N-S.");
                    redLight.signalAll();
                }
            } finally {
                lock.unlock();
            }
        }
	}

}
