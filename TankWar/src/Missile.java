import java.awt.*;
import java.util.List;
public class Missile {
	//定义子弹飞行的速度
	public static final int XSPEED = 10;
	public static final int YSPEED = 10;
	//定义子弹的大小
	public static final int WIDTH = 10;
	public static final int HEIGHT = 10;
	//定义子弹的位置
	int x, y;
	//定义子弹的方向
	Tank.Direction dir;
	//定义子弹的存在状态
	private boolean live = true;
	//用来防止子弹"自杀或者杀害友军"
	private boolean good;
	//定义一个引用
	private TankClient tc;
	
	public Missile(int x, int y, Tank.Direction dir) {
		this.x = x;
		this.y = y;
		this.dir = dir;
	}
	
	public Missile(int x, int y,boolean good, Tank.Direction dir, TankClient tc) {
		this(x, y, dir);
		this.good=good;
		this.tc = tc;
	}
	//话子弹
	public void draw(Graphics g) {
		//如果子弹已经死亡，则将子弹移除子弹集合
		if(!live) {
			tc.missiles.remove(this);
			return;
		}
		
		Color c = g.getColor();
		g.setColor(Color.BLACK);
		g.fillOval(x, y, WIDTH, HEIGHT);
		g.setColor(c);
		
		move();
	}
	private void move() {
		switch(dir) {
			case L:
			x -= XSPEED ;
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
		//挡子弹射出到游戏框外面的时候则子弹死亡
		if(x < 0 || y < 0 || x > TankClient.GAME_WIDTH || y > TankClient.GAME_HEIGHT) {
			live = false;
		}
	}
	//判断子弹是否死亡
	public boolean isLive() {
		return live;
	}
	//设置碰撞函数，用来判断是否击中敌对坦克的
	public Rectangle getRect() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}
	//如果击中设置爆炸
	public boolean hitTank(Tank t) {
		if(this.live&&this.getRect().intersects(t.getRect()) && t.isLive()&&this.good!=t.isGood()) {
			t.setLive(false);
			this.live = false;
			Explode explode = new Explode(x, y, tc);
			tc.explodes.add(explode);
			return true;
		}
		return false;
	}
	//判断子弹是否击中相互敌对的坦克
	public boolean hitTanks(List<Tank> tankList) {
		for (int i=0;i<tankList.size();i++) {
			if (hitTank(tankList.get(i))) {
				return true;
			}
		}
		return false;
	}
	//判断子弹是否击中墙壁
	public boolean hitWall(Wall wall) {
		if (this.live && this.getRect().intersects(wall.getRectangle())) {
			this.live = false;
			return true;
		}else {
			return false;
		}
	}
	//判断子弹是否击中中间的墙壁
	public boolean hitWall(MiddelWalls wall) {
		if (this.live && this.getRect().intersects(wall.getRectangle())) {
			this.live = false;
			return true;
		}else {
			return false;
		}
	}
}
