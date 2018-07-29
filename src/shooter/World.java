package shooter;

import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import shooter.Award;
import shooter.Bullet;
import shooter.Fo;
import shooter.Hero;

import shooter.World;



public class World extends JPanel{
	//一片天空
	private Sky sky;
	//一个英雄飞机
	private Hero hero;
	//英雄打出多个子弹
	private Bullet[] bullets;
	//飞行的物体（蜜蜂 大飞机 小飞机）
	private Fo[] fo;
	protected int score = 0,life = 1,fireType=1;
	
	public static final int READY = 0;
	public static final int RUNNING = 1;
	public static final int PAUSE = 2;
	public static final int GAME_OVER = 3;

	public int state = READY;
	
	public static BufferedImage ready;
	public static BufferedImage pause;
	public static BufferedImage gameover;

	static {
		try {
			/*String png = "shooter/start.png";
			ready = ImageIO.read(World.class.getClassLoader().getResourceAsStream(png));*/
			String png = "shooter/pause.png";
			pause = ImageIO.read(World.class.getClassLoader().getResourceAsStream(png));
			png = "shooter/gameover.png";
			gameover = ImageIO.read(World.class.getClassLoader().getResourceAsStream(png));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public World(){
		sky = new Sky();
		hero = new Hero();
		bullets = new Bullet[]{new Bullet(50,40),new Bullet(50,62)};//数组元素
		fo = new Fo[]{new Airplane(),new Bee(),new BigPlane()};
		nextTime = System.currentTimeMillis()+1000; 
	}
	private long nextTime;
	public void nextOne(){
		long now = System.currentTimeMillis();
		if(now >= nextTime){
			 nextTime = now + 500;
			 Fo obj = randomOne();
			 fo = Arrays.copyOf(fo, fo.length+1);
			 fo[fo.length-1] = obj;
			 
			 
		}
	}
	
	public static Fo randomOne() {
		int n = (int)(Math.random()*10);
		switch(n){
		case 0:
			return new Bee();
		case 1:
		case 2:
			return new BigPlane();
		case 3:
			return new BigPlaneAward();
		default:
			return new Airplane();
		}
	}

	public String toString(){
		return sky+","+hero+","+Arrays.toString(bullets)+","+Arrays.toString(fo);
	}
	public void paint(Graphics g){
		sky.paint(g);
		hero.paint(g);
		for (int i = 0;i<bullets.length;i++){
		bullets[i].paint(g);
		}
		for(int i = 0;i<fo.length;i++){
		fo[i].paint(g);
		}
		g.drawString("Score:"+score,20,30);
		g.drawString("LIFE:"+life,20,50);
		g.drawString("FIRE:"+fireType,20,70);
		switch(state){
		case PAUSE:
			g.drawImage(pause, 0, 0, null);
			break;
		case READY:
			g.drawImage(ready, 0, 0, null);
			break;
		case GAME_OVER:
			g.drawImage(gameover, 0, 0, null);
			break;
			
		}
		
	}
	
	//添加定时器
	//在定时器中添加创建鼠标监听

			
	private Timer timer;
	public void action(){
		//开始鼠标监听
		MouseAdapter l = new MouseAdapter() {
			public void mouseMoved(MouseEvent e){
				if (state == RUNNING) {
					int x = e.getX();
					int y = e.getY();
					hero.move(x, y);
				}
			}
			@Override
			public void mouseExited(MouseEvent e) {
				if(state==RUNNING){
					state = PAUSE;
				}
			}
			

			@Override
			public void mouseEntered(MouseEvent e) {
				if(state == PAUSE){
					state = RUNNING;
				}
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				if(state==READY){
					state = RUNNING;
				}
				  if (state==GAME_OVER) {
					  score=0;
					  life=3;
					  fireType = 1;
					  hero = new Hero();
					  bullets = new Bullet[0];
					  fo = new Fo[0];
					  state = READY;
				
				}
			}
			

			
		};
		//将监听器加到当前面板
		addMouseListener(l);
		addMouseMotionListener(l);
				
				
				
				
				
		timer = new Timer();
		timer.schedule(new TimerTask(){
			public void run(){
				if (state == RUNNING) {
					nextOne();
					shoot();
					move();
					removeObjects();// 删除掉那些需要删除的对象
					// 凡是状态是REMOVE的子弹/飞机都需要删除
					duangDuang();
					heroLifeCircle();
					
				}
				repaint();

			}

		}, 0, 1000 / 24);
	}

	/*
	 * 在World中添加英雄生命周期控制算法
	 * 
	 */
	public void heroLifeCircle(){
		if(hero.isActive()){
			for (Fo plane : fo) {
				if (plane.isActive() && plane.duang(hero)) {
					hero.goDead();
					plane.goDead();
				}
			}

		}
		
		if(hero.canRemove()){
			if (life > 0) {
				life--;
				hero = new Hero();
				for (Fo plane : fo) {
					if (plane.isActive() && plane.duang(hero)) {
						plane.goDead();
					}
				}
			} else {
				state = GAME_OVER;
			}
		}

	}
	
	
	//添加碰撞
	public void duangDuang(){
		for(Fo plane:fo){
			if (plane.isActive()) {
				if (shootByBullet(plane)) {
					plane.hit();
					if (plane.isDead()) {
						// 计分
						// 转换为子类型才能进行计分
						if(plane instanceof Enemy){
							Enemy enemy = (Enemy)plane;
							int s = enemy.getScore();
							score += s;
							//System.out.println(score);
						}
						if(plane instanceof Award){
							Award award = (Award) plane;
							int awd = award.getAward();
							if (awd == Award.LIFE) {
								life++;
							}else if (awd == Award.FIRE) {
								fireType = 1;
							} else if (awd == Award.DOUBLE_FIRE) {
								fireType = 3;
							
							}
						}
					}

				}
			}
		}
	}
	public boolean shootByBullet(Fo plane){
		for (Bullet b : bullets) {
			if (b.isActive()) {
				if (b.duang(plane)) {
					b.hit();
					return true;
				}
			}

		}
		return false;
	}
	
	//删除没有用的子弹和飞机
	public void removeObjects(){
		Bullet[] ary = {};
		for(Bullet b:bullets){
			if(b.canRemove()){
				//忽略需要删除的子弹
				continue;
			}
			ary = Arrays.copyOf(ary, ary.length+1);
			ary[ary.length-1] = b;
		}
		bullets = ary;
		
		//删除废掉的飞行物
		Fo[] ary1 = {};
		for(Fo obj:fo){
			if(obj.canRemove()){
				continue;
			}
			ary1 = Arrays.copyOf(ary1, ary1.length+1);
			ary1[ary1.length-1] = obj;
		}
		fo = ary1; 
	}
	//测试删除一个飞机和子弹
	public void testRemove(){
		bullets[0].hit();
		fo[0].hit();
		fo[0].state =Fo.REMOVE;
	}
	
		
		
	
	/*
	 * 删除掉没有用的子弹和飞机
	 */
	
	//控制射击方法
	private long nextShootTime;
	public void shoot(){
		//射击速度
		long now = System.currentTimeMillis();
		if(now>nextShootTime){
		  nextShootTime = now + 100;
		  Bullet[] ary = hero.shoot(fireType);
		  //将子弹ary添加到bullets数组
		  bullets = Arrays.copyOf(bullets, bullets.length+ary.length);
		  System.arraycopy(ary, 0, bullets, bullets.length-ary.length, ary.length);;
	
		}
	}
	
	 
	
	public void move() {
		sky.move();
		hero.move();
		for(int i = 0;i<fo.length;i++){
			fo[i].move();
		}
		for(Bullet b:bullets){
			b.move();
		}
	}

	public static void main(String[] args){
		//新建框架
		JFrame frame = new JFrame();
		//框架尺寸
		frame.setSize(480,720);
		//框架居中
		frame.setLocationRelativeTo(null);
		//添加强制关闭功能
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		World world = new World();
		//框架中添加画板
		frame.add(world);
		//frame 在显示的时候，自动调用paint方法，如果重写paint，则显示
		//显示时候自动执行重写以后的paint
		//显示框架
		frame.setVisible(true);
		world.action();//调用启动定时器
	}
	
	
}
