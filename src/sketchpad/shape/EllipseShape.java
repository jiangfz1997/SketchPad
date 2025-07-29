package sketchpad.shape;

import java.awt.*;

public class EllipseShape extends Shape {
    private int x, y, width, height;

    public EllipseShape(int x1, int y1, int x2, int y2, Color color) {
        super(color);
        this.x = Math.min(x1, x2);
        this.y = Math.min(y1, y2);
        this.width = Math.abs(x2 - x1);
        this.height = Math.abs(y2 - y1);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        g.drawOval(x, y, width, height);
    }
    @Override
    public boolean contains(Point p) {
        double centerX = x + width / 2.0;
        double centerY = y + height / 2.0;
        double a = width / 2.0;
        double b = height / 2.0;

        double dx = p.x - centerX;
        double dy = p.y - centerY;

        return (dx * dx) / (a * a) + (dy * dy) / (b * b) <= 1.0;
    }

    @Override
    public void moveBy(int dx, int dy) {
        x += dx;
        y += dy;
    }



    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }





}
