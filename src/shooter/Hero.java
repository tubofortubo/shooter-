package shooter;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class Hero extends Fo{
	public static BufferedImage[] imgs;
	static{
		imgs = new BufferedImage[6];
		try{
			for(int i = 0;i<imgs.length;i++){
			String png = "shooter/hero"+i+".png";
			imgs[i] = ImageIO.read(Hero.class.getClassLoader().getResourceAsStream(png));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public Hero(){
		this.width = 97;
		this.height = 124;
		this.x = (480-width)/2;
		this.y = 500;
        this.image = imgs[0];
  }
  private int n = 0;
  public void move(){
	  if(state==ACTIVE){
			n++;
			int i = n / 2 % 2;
			image = imgs[i];
		}
		if (state == DEAD) {
			// 从子类对象中获取下一张照片
			BufferedImage img = nextImage();// 从子类拿一张照片
			if (img == null) {
				state = REMOVE;
			} else {
				image = img;
			}
		}
	}

	/*
	 * 在hero中添加射击方法
	 */
  public Bullet[] shoot(int type){
	  int x= (int)(this.x+width/2-8/2);
	  int y = (int)(this.y-15);
	  if(type==1){
		  return new Bullet[]{new Bullet(x,y)};
	  }
	  if(type==2){
		  return new Bullet[]{new Bullet(x-97/3,y+60),new Bullet(x-97/3,y+60)};
	  }
	  if(type==3){
		  return new Bullet[]{new Bullet(x-97/3,y+60),new Bullet(x,y),new Bullet(x+97/3,y+60)};
	  }
	return new Bullet[0];
  }
  //在Hero中添加move方法  重载
  public void move(int x,int y){
	  this.x = x - width/2;
	  this.y =  y-height/2;
  }
  private int index = 1;
	protected BufferedImage nextImage(){
		index++;
		if(index>=imgs.length){
			return null;
		}
		return imgs[index];
	}
	
}

