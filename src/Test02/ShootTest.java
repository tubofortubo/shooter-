package Test02;

import shooter.World;

public class ShootTest {

	public static void main(String[] args) {
		World world = new World();
		System.out.println(world);
		world.shoot();
		System.out.println(world);
	}

}
