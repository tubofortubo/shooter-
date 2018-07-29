package Test02;

import shooter.BigPlane;

public class StateTest {

	public static void main(String[] args) {
		BigPlane plane = new BigPlane();
		System.out.println(plane);
		plane.move();
		System.out.println(plane);
		plane.hit();
		System.out.println(plane);
		plane.hit();
		System.out.println(plane);
		plane.hit();
		System.out.println(plane);
		System.out.println("..................");
		plane.move();
		System.out.println(plane);
		plane.move();
		System.out.println(plane);
	}

}
