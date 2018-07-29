package Test02;

import shooter.Bullet;

public class duangTest {

	public static void main(String[] args) {
		Bullet b1 = new Bullet(100, 150);
		// x 100, y 150 w 8, h 14 
		// x 108, y 164
		Bullet b2 = new Bullet(107, 163);
		Bullet b3 = new Bullet(109, 165);
		System.out.println(b1.duang(b2));
		System.out.println(b1.duang(b3));

	}

}
