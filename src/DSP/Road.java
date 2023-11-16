package DSP;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Road implements Runnable {
	public String direction;
	public Intersection intersection;
	public ArrayList<Car> cars;
	
	Road(String direction, Intersection intersection){
		this.direction = direction;
		this.intersection = intersection;
		this.cars = new ArrayList<Car>();
	}
	
	@Override
	public void run() {
	    while(true) {
	        synchronized (cars) {
	            if (!cars.isEmpty()) {
	                Car currentCar = cars.get(0);
	                
	                currentCar.origin = this.direction;
	                List<String> directions = new ArrayList<>(Arrays.asList("N", "S", "E", "W"));
	                directions.remove(this.direction); 
	                Collections.shuffle(directions); 
	                currentCar.destination = directions.get(0);
	                
	                //System.out.println("Car " + currentCar.id + " is trying to leave road " + this.direction + ".");
	                
	                
	                try {
	                    Thread.sleep(1000);
	                } catch (InterruptedException e) {
	                    e.printStackTrace();
	                }
	                
	                
	                if (currentCar.origin.equals("N") || currentCar.origin.equals("S")) {
	                    intersection.handleNS(currentCar);
	                }
	                
	                if (currentCar.origin.equals("E") || currentCar.origin.equals("W")) {
	                    intersection.handleEW(currentCar);
	                }
	                
	                cars.remove(currentCar);
	            }
	        }
	    }
	}

}
