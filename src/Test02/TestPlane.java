package Test02;

import shooter.World;

public class TestPlane {

	public static void main(String[] args) {
		// ɾ�����õķɻ����ӵ�
		World world = new World();
		System.out.println(world);
		world.testRemove();
		world.removeObjects();
		System.out.println(world);

	}

}
