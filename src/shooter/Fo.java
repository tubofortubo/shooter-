package shooter;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

public abstract class Fo {
	protected double x, y, width, height;
	//�������ƶ�
	protected double step;
	//��ǰ������ʾ��ͼƬ
	//��
	protected int life;
	//����״̬
	protected int state;
	
	public final static int ACTIVE = 0;
	public final static int DEAD = 1;
	public final static int REMOVE = 2;
	
	protected BufferedImage image;
    public Fo(double width,double height){
    	this();
    	this.width = width;
    	this.height = height;
    	x = Math.random()*(480-width);
    	y = -height;
    	step = Math.random()*3+0.8;
    }
    //�����ӵ�  Ӣ�� ��յȵ��޲ι�����
    public Fo(){
    	life = 1;
    	state = ACTIVE;
    }
   
	public String toString(){
    	return x+","+y+","+width+","+height+","+life+","+state+","+image;
    }
	/*
	 * ��Fo�����paint����
	 * ���û��ʶ���g���Ƶ�ǰͼƬ
	 * @param g�������Ļ�������
	 */
	public void paint(Graphics g){
		g.drawImage(image,(int)x, (int)y,null);
	}
	public void move(){
		//�ع�move ʵ�ֲ������ٹ���
		if(state == ACTIVE){
		    y += step;
		   if(y>852){
			   state=REMOVE;
		    }
		   return;
		}
		if(state == DEAD){
			//���Ӷ����л�ȡ��һ��ͼƬ
			BufferedImage img = nextImage();
			if(img == null){
				state=REMOVE;
			}else{
				image = img;
			}
		}
	}
	//��������еķ���,������һ��Ҫ���ŵ���Ƭ���ã��������null����ʾû�пβ��ŵ���Ƭ
	protected abstract BufferedImage nextImage();
	
	//����һ��
	public void hit(){
		if(life>0){
			life--;
		}
		if(life==0){
			state=DEAD;
		}
	}
	//��Fo�������ײ��鷽��
	public boolean duang(Fo obj){
		double x1,x2,y1,y2;
		x1 = this.x - obj.width;
		y1 = this.y - obj.height;
		x2 = this.x + this.width; 
		y2 = this.y + this.height;
		return x1 < obj.x && obj.x < x2 && y1< obj.y && obj.y < y2;
	}
	public boolean isActive(){
		return state == ACTIVE;
	}
	public boolean isDead(){
		return state == DEAD;
	}
	public boolean canRemove(){
		return state == REMOVE;
	}
	//��������ӡ�ȥ��������
	public void goDead(){
		if(isActive()){
			state=DEAD;
		}
	}
}
