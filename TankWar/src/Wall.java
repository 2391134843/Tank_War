import com.sun.corba.se.impl.orbutil.graph.Graph;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Wall {
    private int x,y,width,height;
    TankClient tankClient;


    public Wall(int x, int y, int width, int height, TankClient tankClient) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.tankClient = tankClient;
    }

    public void draw(Graphics graphics) {
       // graphics.fillRect(x, y, width, height);
        try {
            BufferedImage walls= ImageIO.read(
                    Tank.class.getResource("walls.gif"));
            graphics.drawImage(walls, x, y, width, height,null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Rectangle getRectangle() {
        return new Rectangle(x, y, width, height);
    }
}
