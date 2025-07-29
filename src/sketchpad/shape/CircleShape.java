package sketchpad.shape;

import java.awt.*;

public class CircleShape extends Shape {
    private int x, y, size;

    public CircleShape(int x1, int y1, int x2, int y2, Color color) {
        super(color);
        int dx = x2 - x1;
        int dy = y2 - y1;
        int length = Math.max(Math.abs(dx), Math.abs(dy));
        this.x = dx < 0 ? x1 - length : x1;
        this.y = dy < 0 ? y1 - length : y1;
        this.size = length;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        g.drawOval(x, y, size, size);
    }

    @Override
    public boolean contains(Point p) {
        double centerX = x + size / 2.0;
        double centerY = y + size / 2.0;
        double dx = p.x - centerX;
        double dy = p.y - centerY;
        return dx * dx + dy * dy <= (size / 2.0) * (size / 2.0);
    }

    @Override
    public void moveBy(int dx, int dy) {
        x += dx;
        y += dy;
    }



    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, size, size);
    }


}
