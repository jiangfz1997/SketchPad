package sketchpad.shape;
import java.awt.*;
import java.awt.geom.Line2D;

public class LineShape extends Shape {
    private int x1, y1, x2, y2;

    public LineShape(int x1, int y1, int x2, int y2, Color color) {
        super(color);
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        g.drawLine(x1, y1, x2, y2);
    }

    @Override
    public boolean contains(Point p) {
        // 允许点在直线附近一定范围内
        double distance = Line2D.ptSegDist(x1, y1, x2, y2, p.x, p.y);
        return distance < 5.0; // 像素容差
    }

    @Override
    public void moveBy(int dx, int dy) {
        x1 += dx;
        y1 += dy;
        x2 += dx;
        y2 += dy;
    }



    @Override
    public Rectangle getBounds() {
        int x = Math.min(x1, x2);
        int y = Math.min(y1, y2);
        int w = Math.abs(x2 - x1);
        int h = Math.abs(y2 - y1);
        return new Rectangle(x - 3, y - 3, w + 6, h + 6);
    }
}
