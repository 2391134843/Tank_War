
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Random;

public class Tank {
	//用于存放要画出坦克的图像
	public static BufferedImage tank_up;
	public static BufferedImage tank_down;
	public static BufferedImage tank_left;
	public static BufferedImage tank_right;
	public static BufferedImage tank_ul;
	public static BufferedImage tank_ur;
	public static BufferedImage tank_dl;
	public static BufferedImage tank_dr;
	//读取坦克八个方向的图像
	static {
		try {
			tank_up= ImageIO.read(
					Tank.class.getResource("tank-up.jpg"));
			tank_down=ImageIO.read(
					Tank.class.getResource("tank-down.jpg"));
			tank_left=ImageIO.read(
					Tank.class.getResource("tank-left.jpg"));
			tank_right=ImageIO.read(
					Tank.class.getResource("tank-right.jpg"));
			tank_ul=ImageIO.read(
					Tank.class.getResource("tank-ul.jpg"));
			tank_ur=ImageIO.read(
					Tank.class.getResource("tank-ur.jpg"));
			tank_dl=ImageIO.read(
					Tank.class.getResource("tank-dl.jpg"));
			tank_dr=ImageIO.read(
					Tank.class.getResource("tank-dr.jpg"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	//设置坦克的速度
	public static final int XSPEED = 5;
	public static final int YSPEED = 5;
	//设置坦克的大小
	public static final int WIDTH = 30;
	public static final int HEIGHT = 30;

	//确定坦克是否还活着
	private boolean live = true;
	//定义一个对象引用方便来传递参数
	TankClient tc;
	//区分是自己的坦克还是别人的坦克
	private boolean good;
	//设置自己坦克的坐标
	private int x, y;
	//设置一个随机数，用来让坦克随机移动
	private static Random random=new Random();
	//定义坦克沿着四个斜线方向移动
	private boolean bL=false, bU=false, bR=false, bD = false;
	//用枚举类型来定义坦克行走的八个方位
	enum Direction {L, LU, U, RU, R, RD, D, LD, STOP};
	//设置默认方向为停止
	private Direction dir = Direction.STOP;
	//设置炮筒方向，默认向下
	private Direction ptDir = Direction.D;
	private static int step=random.nextInt(13)+2;
	//设置一个先前位置点，用来记录上一个位置坦克的位置
	int preX,preY;

	public Tank(int x, int y, boolean good) {
		this.x = x;
		this.y = y;
		this.good = good;
	}
	//坦克的构造方法
	public Tank(int x, int y, boolean good, Direction dir,TankClient tc) {
		this(x, y, good);
		this.dir = dir;
		this.tc = tc;
	}
	//画出坦克
	public void draw(Graphics g) {
		if(!live) {
			//如果是敌方坦克被销毁则将它从list中移除，以免占用内存
			if (!good) {
				tc.tanks.remove(this);
			}else {
				//我们操控的坦克已经被炸毁....游戏结束//Tahoma  宋体
				g.setFont(new Font("Tahoma", Font.BOLD, 40));
				g.drawString("Game Over !",422,370);
			}
		}
		if(live){
			//如果活着，根据键盘设置的方向画出不同坦克的图片
			switch(ptDir) {
				case U:
					g.drawImage(tank_up, x, y, 50, 30, null);
					break;
				case D:
					g.drawImage(tank_down, x, y, 50, 30, null);
					break;
				case L:
					g.drawImage(tank_left, x, y, 50, 30, null);
					break;
				case R:
					g.drawImage(tank_right, x, y, 50, 30, null);
					break;
				case LU:
					g.drawImage(tank_ul, x, y, 50, 30, null);
					break;
				case RU:
					g.drawImage(tank_ur, x, y, 50, 30, null);
					break;
				case LD:
					g.drawImage(tank_dl, x, y, 50, 30, null);
					break;
				case RD:
					g.drawImage(tank_dr, x, y, 50, 30, null);
					break;
			}
		}
		move();
	}

	public boolean isGood() {
		return good;
	}

	void move() {
		this.preX = x;
		this.preY = y;

		switch(dir) {
		case L:
			x -= XSPEED;
			break;
		case LU:
			x -= XSPEED;
			y -= YSPEED;
			break;
		case U:
			y -= YSPEED;
			break;
		case RU:
			x += XSPEED;
			y -= YSPEED;
			break;
		case R:
			x += XSPEED;
			break;
		case RD:
			x += XSPEED;
			y += YSPEED;
			break;
		case D:
			y += YSPEED;
			break;
		case LD:
			x -= XSPEED;
			y += YSPEED;
			break;
		case STOP:
			break;
		}
		
		if(this.dir != Direction.STOP) {
			this.ptDir = this.dir;
		}
		
		if(x < 0) x = 0;
		if(y < 30) y = 30;
		if(x + Tank.WIDTH > TankClient.GAME_WIDTH) x = TankClient.GAME_WIDTH - Tank.WIDTH;
		if(y + Tank.HEIGHT > TankClient.GAME_HEIGHT) y = TankClient.GAME_HEIGHT - Tank.HEIGHT;
		//让敌对坦克动起来，并且稍微智能一点，并且火力不能太猛
		if (!good) {
			//将枚举类型用.values()方法之间转化为数组类型
			Direction[]directions=Direction.values();
			if (step == 0) {
				step=random.nextInt(13)+2;
				int randomNumber = random.nextInt(directions.length);
				dir = directions[randomNumber];
			}
			step--;
			if (random.nextInt(40)>38)
			this.fire();
		}
	}
	//让坦克回到上一次移动点位置
	public void stay() {
		x=preX;
		y = preY;
	}
	//响应键盘事件，当键盘按下去的时候响应键盘事件
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key) {
		case KeyEvent.VK_LEFT :
			bL = true;
			break;
		case KeyEvent.VK_UP :
			bU = true;
			break;
		case KeyEvent.VK_RIGHT :
			bR = true;
			break;
		case KeyEvent.VK_DOWN :
			bD = true;
			break;
		}
		locateDirection();
	}
	//用来确定当前方向
	void locateDirection() {
		if(bL && !bU && !bR && !bD) dir = Direction.L;
		else if(bL && bU && !bR && !bD) dir = Direction.LU;
		else if(!bL && bU && !bR && !bD) dir = Direction.U;
		else if(!bL && bU && bR && !bD) dir = Direction.RU;
		else if(!bL && !bU && bR && !bD) dir = Direction.R;
		else if(!bL && !bU && bR && bD) dir = Direction.RD;
		else if(!bL && !bU && !bR && bD) dir = Direction.D;
		else if(bL && !bU && !bR && bD) dir = Direction.LD;
		else if(!bL && !bU && !bR && !bD) dir = Direction.STOP;
	}
	//键盘响应事件,当键盘抬起来的时候触发事件
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key) {
		case KeyEvent.VK_X:
			fire();
			break;
		case KeyEvent.VK_LEFT :
			bL = false;
			break;
		case KeyEvent.VK_UP :
			bU = false;
			break;
		case KeyEvent.VK_RIGHT :
			bR = false;
			break;
		case KeyEvent.VK_DOWN :
			bD = false;
			break;
		}
		locateDirection();		
	}
	//控制坦克开火
	public Missile fire() {
		if (!live) {
			return null;
		}
		//控制子弹出现的方位地址
		int x = this.x + Tank.WIDTH/2 - Missile.WIDTH/2;
		int y = this.y + Tank.HEIGHT/2 - Missile.HEIGHT/2;
		Missile m = new Missile(x, y,good, ptDir, this.tc);
		tc.missiles.add(m);
		return m;
	}
	//防止坦克之间相互重叠
	public boolean impactTank(List<Tank> tanks) {
		for (int i=0;i<tanks.size();i++) {
			Tank tank = tanks.get(i);
			if (tank != this) {
				if (this.live && this.getRect().intersects(tank.getRect())) {
					stay();
					return true;
				}
			}
		}
		return false;
	}
	//不能让敌对坦克穿越墙面
	public boolean impactWall(Wall wall) {
		if (this.live && this.getRect().intersects(wall.getRectangle())) {
			this.stay();
			return true;
		}
		return false;
	}
	public boolean impactWall(MiddelWalls wall) {
		if (this.live && this.getRect().intersects(wall.getRectangle())) {
			this.stay();
			return true;
		}
		return false;
	}
	//为图形碰撞做准备，给出坦克的方位，用于判断是否与子弹碰撞
	public Rectangle getRect()
	{
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}
	//确定坦克是否还活着
	public boolean isLive() {
		return live;
	}
	//设置坦克是否还活着
	public void setLive(boolean live) {
		this.live = live;
	}
}
