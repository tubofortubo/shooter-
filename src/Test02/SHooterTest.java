package Test02;

import java.util.Arrays;

import shooter.Bullet;
import shooter.Hero;

public class SHooterTest {

	public static void main(String[] args) {
		Hero hero = new Hero();
		System.out.println(hero);
		//��鵥ǹ����ķ���
		Bullet[] ary = hero.shoot(1);
		System.out.println(Arrays.toString(ary));
		
		ary = hero.shoot(2);
		System.out.println(Arrays.toString(ary));

	}

}
