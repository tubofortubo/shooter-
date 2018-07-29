package Test02;

import shooter.World;

public class TestPlane {

	public static void main(String[] args) {
		// 删除无用的飞机和子弹
		World world = new World();
		System.out.println(world);
		world.testRemove();
		world.removeObjects();
		System.out.println(world);

	}

}
