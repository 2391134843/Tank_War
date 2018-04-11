import java.awt.*;

public class Explode {
    private int x, y;
    private boolean live=true;
    TankClient tankClient;
    private int step = 0;
    int[] diameter = {4, 7, 12, 18, 26, 32, 49, 30, 14, 6};

    public Explode(int x, int y, TankClient tankClient) {
        this.x = x;
        this.y = y;
        this.tankClient = tankClient;
    }


    public void draw(Graphics graphics) {
        if (!live) {
            tankClient.explodes.remove(this);
            return;
        }
        if (step == diameter.length) {
            live = false;
            step = 0;
            return ;
        }
        Color c = graphics.getColor();
        graphics.setColor(Color.ORANGE);
        graphics.fillOval(x,y,diameter[step],diameter[step]);
        graphics.setColor(c);
        step++;
    }

}
