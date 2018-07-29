package shooter;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class Airplane extends Fo implements Enemy{
	public static BufferedImage[] imgs;
	static{
		imgs = new BufferedImage[5];
		try{
			for(int i = 0;i<imgs.length;i++){
			String png = "shooter/airplane"+i+".png";
			imgs[i] = ImageIO.read(BigPlane.class.getClassLoader().getResourceAsStream(png));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public Airplane(){
		super(49,36);
		this.image = imgs[0];
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
		return 1;
	}
}
