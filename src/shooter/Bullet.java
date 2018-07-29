package shooter;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class Bullet extends Fo{
	public static BufferedImage imgs;
	static{
		try{
			String png = "shooter/bullet.png";
			imgs = ImageIO.read(Bullet.class.getClassLoader().getResourceAsStream(png));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public Bullet(int x,int y){
		this.x = x;
		this.y = y;
		width = 8;
		height = 14;
		this.image =imgs;
	}
	public void move(){
		if(state == ACTIVE){
			y -= 20;
			if(y<= -height){
				state = REMOVE;
			}
			
		}
		
	}
	
	protected BufferedImage nextImage(){
		
			return null;
		
	}
	public void hit(){
		state = REMOVE;
	}
}
