package shooter;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class BigPlane extends Fo implements Enemy{
	public static BufferedImage[] imgs;
	static{
		imgs = new BufferedImage[5];
		try{
			for(int i = 0;i<imgs.length;i++){
			String png = "shooter/bigplane"+i+".png";
			imgs[i] = ImageIO.read(BigPlane.class.getClassLoader().getResourceAsStream(png));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public BigPlane(){
		super(69,99);
		this.life=2;
		
		this.image = imgs[0];//初始化用第一张图片
	}
	private int index = 0;
	protected BufferedImage nextImage(){
		index++;
		if(index>=imgs.length){
			return null;
		}
		return imgs[index];
	}
	public int getScore(){
		return 10;
	}
}
