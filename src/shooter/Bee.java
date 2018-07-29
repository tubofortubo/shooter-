package shooter;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class Bee extends Fo implements Award{
	public static BufferedImage[] imgs;
	static{
		imgs = new BufferedImage[5];
		try{
			for(int i = 0;i<imgs.length;i++){
			String png = "shooter/bee"+i+".png";
			imgs[i] = ImageIO.read(BigPlane.class.getClassLoader().getResourceAsStream(png));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public Bee(){
		super(60,50);
		this.image =imgs[0];
		int[] dir = {-2,2};
		direction = dir[(int)(Math.random()*2)];
	}
	private int direction;
	
	public void move(){
		super.move();
		if(state == ACTIVE){
			x += direction;
		//y += step;
		
			if(x>=(480-width)){
				direction = -2;
			}
			if(x<=0){
				direction = 2;
			}
		}	
	}
	private int index = 0;
	protected BufferedImage nextImage(){
		index++;
		if(index>=imgs.length){
			return null;
		}
		return imgs[index];
	}
	/*
	 * 在蜜蜂中实现奖励的算法
	 * 
	 */
	public int getAward(){
		int[] ary = {LIFE,FIRE,DOUBLE_FIRE};
		int i = (int)(Math.random()*3);
		return ary[i];
	}
	
}
