package Test02;

import shooter.Airplane;
import shooter.Bee;
import shooter.BigPlane;
import shooter.Bullet;
import shooter.Hero;
import shooter.Sky;
import shooter.World;

public class Test002 {
	public static void main(String[] args) {
		Airplane airplane = new Airplane();
		System.out.println(airplane);
		
		Bee bee = new Bee();
		System.out.println(bee);
		
		BigPlane bigplane = new BigPlane();
		System.out.println(bigplane);
		
		Bullet bullets = new Bullet(100,200);
		System.out.println(bullets);
		
		Hero hero = new Hero();
		System.out.println(hero);
		
		Sky sky = new Sky();
		System.out.println(sky);
		
		World world = new World();
		System.out.println(world);
	}
}
