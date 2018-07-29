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
	//һƬ���
	private Sky sky;
	//һ��Ӣ�۷ɻ�
	private Hero hero;
	//Ӣ�۴������ӵ�
	private Bullet[] bullets;
	//���е����壨�۷� ��ɻ� С�ɻ���
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
		bullets = new Bullet[]{new Bullet(50,40),new Bullet(50,62)};//����Ԫ��
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
	
	//��Ӷ�ʱ��
	//�ڶ�ʱ������Ӵ���������

			
	private Timer timer;
	public void action(){
		//��ʼ������
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
		//���������ӵ���ǰ���
		addMouseListener(l);
		addMouseMotionListener(l);
				
				
				
				
				
		timer = new Timer();
		timer.schedule(new TimerTask(){
			public void run(){
				if (state == RUNNING) {
					nextOne();
					shoot();
					move();
					removeObjects();// ɾ������Щ��Ҫɾ���Ķ���
					// ����״̬��REMOVE���ӵ�/�ɻ�����Ҫɾ��
					duangDuang();
					heroLifeCircle();
					
				}
				repaint();

			}

		}, 0, 1000 / 24);
	}

	/*
	 * ��World�����Ӣ���������ڿ����㷨
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
	
	
	//�����ײ
	public void duangDuang(){
		for(Fo plane:fo){
			if (plane.isActive()) {
				if (shootByBullet(plane)) {
					plane.hit();
					if (plane.isDead()) {
						// �Ʒ�
						// ת��Ϊ�����Ͳ��ܽ��мƷ�
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
	
	//ɾ��û���õ��ӵ��ͷɻ�
	public void removeObjects(){
		Bullet[] ary = {};
		for(Bullet b:bullets){
			if(b.canRemove()){
				//������Ҫɾ�����ӵ�
				continue;
			}
			ary = Arrays.copyOf(ary, ary.length+1);
			ary[ary.length-1] = b;
		}
		bullets = ary;
		
		//ɾ���ϵ��ķ�����
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
	//����ɾ��һ���ɻ����ӵ�
	public void testRemove(){
		bullets[0].hit();
		fo[0].hit();
		fo[0].state =Fo.REMOVE;
	}
	
		
		
	
	/*
	 * ɾ����û���õ��ӵ��ͷɻ�
	 */
	
	//�����������
	private long nextShootTime;
	public void shoot(){
		//����ٶ�
		long now = System.currentTimeMillis();
		if(now>nextShootTime){
		  nextShootTime = now + 100;
		  Bullet[] ary = hero.shoot(fireType);
		  //���ӵ�ary��ӵ�bullets����
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
		//�½����
		JFrame frame = new JFrame();
		//��ܳߴ�
		frame.setSize(480,720);
		//��ܾ���
		frame.setLocationRelativeTo(null);
		//���ǿ�ƹرչ���
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		World world = new World();
		//�������ӻ���
		frame.add(world);
		//frame ����ʾ��ʱ���Զ�����paint�����������дpaint������ʾ
		//��ʾʱ���Զ�ִ����д�Ժ��paint
		//��ʾ���
		frame.setVisible(true);
		world.action();//����������ʱ��
	}
	
	
}
