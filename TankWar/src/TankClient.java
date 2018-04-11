import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class TankClient extends Frame {
	public static final int GAME_WIDTH = 1100;
	public static final int GAME_HEIGHT = 850;
	//构造出一架我们可以操控的坦克
	Tank myTank = new Tank(50, 50, true,Tank.Direction.STOP, this);

	List<Missile> missiles = new ArrayList<Missile>();
	List<Explode> explodes = new ArrayList<Explode>();
	List<Tank> tanks = new ArrayList<Tank>();
	Image offScreenImage = null;
	//左下三堵墙
	Wall wall_1 = new Wall(103, 546, 40, 90,this);
	Wall wall_2 = new Wall(103, 546+90, 40, 90,this);
	Wall wall_3 = new Wall(103 + 50, 546 + 90 + 90, 40, 90, this);
	//右下三堵墙
	Wall wall_4 = new Wall(896, 546, 40, 90,this);
	Wall wall_5 = new Wall(896, 546+90, 40, 90,this);
	Wall wall_6 = new Wall(896 - 50, 546 + 90 + 90, 40, 90, this);
	//中间上墙
	// Wall wall_7 = new Wall(410, 139, 80, 40,this);
	// Wall wall_8 = new Wall(410+80, 139, 80, 40,this);
	// Wall wall_9 = new Wall(410+80+80, 139, 80, 40,this);
	MiddelWalls middelWalls1 = new MiddelWalls(410, 139, 80 * 3, 40, this);

	//中间中墙
	// Wall wall_10 = new Wall(410, 289, 80, 40,this);
	// Wall wall_11 = new Wall(410+80, 289, 80, 40,this);
	// Wall wall_12 = new Wall(410+80+80, 289, 80, 40,this);
	MiddelWalls middelWalls2 = new MiddelWalls(410, 139+150, 80 * 3, 40, this);
	//中间下墙
	// Wall wall_13 = new Wall(410, 289+150, 80, 40,this);
	// Wall wall_14 = new Wall(410+80, 289+150, 80, 40,this);
	// Wall wall_15 = new Wall(410+80+80, 289+150, 80, 40,this);
	MiddelWalls middelWalls3 = new MiddelWalls(410, 139+300, 80 * 3, 40, this);

	public void paint(Graphics g) {
		//用于记录现在余留在内存中的导弹，爆炸效果，和敌对坦克数量
		g.setFont(new Font("Tahoma", Font.BOLD, 20));
		g.drawString("↑↓← →(MOVE) X (Fire) :", 10, 50);
		// g.drawString("explodes count:" + explodes.size(), 10, 70);
		g.drawString("Enemy Tank Count:" + tanks.size(), 10, 80);
	//	explode.draw(g);

		for(int i=0; i<missiles.size(); i++) {
			Missile m = missiles.get(i);
			m.draw(g);
			//子弹击中敌人的坦克
			m.hitTanks(tanks);
			m.hitTank(myTank);
			m.hitWall(wall_1);
			m.hitWall(wall_2);
			m.hitWall(wall_3);
			m.hitWall(wall_4);
			m.hitWall(wall_5);
			m.hitWall(wall_6);
			m.hitWall(middelWalls1);
			m.hitWall(middelWalls2);
			m.hitWall(middelWalls3);

			// m.hitWall(wall_7);
			// m.hitWall(wall_8);
			// m.hitWall(wall_9);
			// m.hitWall(wall_10);
			// m.hitWall(wall_11);
			// m.hitWall(wall_12);
			// m.hitWall(wall_13);
			// m.hitWall(wall_14);
			// m.hitWall(wall_15);
			//if(!m.isLive()) missiles.remove(m);
			//else m.draw(g);
		}
		for (int i=0;i<explodes.size();i++) {
			Explode explode = explodes.get(i);
			explode.draw(g);
		}
		//********************用foreach会产生异常！！！！！！！！！！！！！！！！！！！！！！！
//		for (Explode explode : explodes) {
//			explode.draw(g);
//		}
		myTank.draw(g);
		//画出多辆敌人坦克
		for (int i=0;i<tanks.size();i++) {
			Tank tank = tanks.get(i);
			tank.impactWall(wall_1);
			tank.impactWall(wall_2);
			tank.impactWall(wall_3);
			tank.impactWall(wall_4);
			tank.impactWall(wall_5);
			tank.impactWall(wall_6);
			tank.impactWall(middelWalls1);
			tank.impactWall(middelWalls2);
			tank.impactWall(middelWalls3);

			// tank.impactWall(wall_7);
			// tank.impactWall(wall_8);
			// tank.impactWall(wall_9);
			// tank.impactWall(wall_10);
			// tank.impactWall(wall_11);
			// tank.impactWall(wall_12);
			// tank.impactWall(wall_13);
			// tank.impactWall(wall_14);
			// tank.impactWall(wall_15);

			tank.impactTank(tanks);
			tank.draw(g);
		}
		if (tanks.size() == 0) {
			//Tahoma  宋体
			g.setFont(new Font("Tahoma", Font.BOLD, 40));
			g.drawString("You Are Winner !",422,373);
		}
		wall_1.draw(g);
		wall_2.draw(g);
		wall_3.draw(g);
		wall_4.draw(g);
		wall_5.draw(g);
		wall_6.draw(g);
		middelWalls1.draw(g);
		middelWalls2.draw(g);
		middelWalls3.draw(g);
		// wall_7.draw(g);
		// wall_8.draw(g);
		// wall_9.draw(g);
		// wall_10.draw(g);
		// wall_11.draw(g);
		// wall_12.draw(g);
		// wall_13.draw(g);
		// wall_14.draw(g);
		// wall_15.draw(g);
	}
	//不断填充背景色，同时要方式闪烁显现，用背景色填充的方式来消除GUI中闪烁显现
	public void update(Graphics g) {
		if(offScreenImage == null) {
			offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);
		}
		Graphics gOffScreen = offScreenImage.getGraphics();
		Color c = gOffScreen.getColor();
		gOffScreen.setColor(Color.GRAY);
		gOffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
		gOffScreen.setColor(c);
		paint(gOffScreen);
		g.drawImage(offScreenImage, 0, 0, null);
	}
	//画出整个游戏的窗口，并且画出游戏上个各个元素(敌对坦克，等等..)
	public void lauchFrame()  {
		//设置GUI的logo
		try {
			BufferedImage icon= ImageIO.read(
					Tank.class.getResource("TankIcon.png"));
			setIconImage(icon);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		//this.setLocation(400, 300);
		//注意添加放置的位置，放在添加背景后面则会抛出异常！
		for (int i=0;i<3;i++) {
			tanks.add(new Tank(200 , 150+ 50 * i, false, Tank.Direction.D, this));
		}
		for (int i=0;i<3;i++) {
			tanks.add(new Tank(920, 150 + 50 * i, false, Tank.Direction.D, this));
		}
		for(int i=0;i<3;i++) {
			tanks.add((new Tank(351 + 50 * i, 668, false, Tank.Direction.D, this)));
		}
		tanks.add((new Tank(449, 60, false, Tank.Direction.D, this)));
		this.setSize(GAME_WIDTH, GAME_HEIGHT);
		this.setTitle("TankWar 姓名：李林育 班级：16级软件工程2班 学号：14164801087");
		//设置窗口关闭
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		this.setResizable(false);
		//画出背景颜色为灰色
		this.setBackground(Color.GRAY);
		
		this.addKeyListener(new KeyMonitor());
		//构造出10辆坦克

		setVisible(true);
		//调用线程开始循坏
		new Thread(new PaintThread()).start();
	}

	private class PaintThread implements Runnable {
		public void run() {
			while(true) {
				//重画自动可以自动调用paint函数
				repaint();
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	//设置键盘监听，一个按下监听和一个抬起的监听
	private class KeyMonitor extends KeyAdapter {
		public void keyReleased(KeyEvent e) {
			myTank.keyReleased(e);
		}
		public void keyPressed(KeyEvent e) {
			myTank.keyPressed(e);
		}
	}
}