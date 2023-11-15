package DSP;

public class Program {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Intersection intersection = new Intersection();
		Road roadN = new Road("N", intersection);
		Road roadE = new Road("E", intersection);
		Road roadS = new Road("S", intersection);
		Road roadW = new Road("W", intersection);
		
		intersection.roads.add(roadN);
		intersection.roads.add(roadE);
		intersection.roads.add(roadS);
		intersection.roads.add(roadW);
		
		Car car1 = new Car(1);
		Car car2 = new Car(2);
		Car car3 = new Car(3);
		Car car4 = new Car(4);
		Car car5 = new Car(5);
		Car car6 = new Car(6);
		
		roadN.cars.add(car1);
		roadE.cars.add(car2);
		roadS.cars.add(car3);
		roadW.cars.add(car4);
		roadN.cars.add(car5);
		roadS.cars.add(car6);
		
		intersection.displayRoads();
		
		Thread N = new Thread(roadN);
		Thread E = new Thread(roadE);
		Thread S = new Thread(roadS);
		Thread W = new Thread(roadW);
		
		Thread I = new Thread(intersection);
		
		N.start();
		E.start();
		S.start();
		W.start();
		I.start();
	}

}
