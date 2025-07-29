package sketchpad.shape;

import java.awt.*;

public class SquareShape extends Shape {
    private Rectangle square;

    public SquareShape(int x1, int y1, int x2, int y2, Color color) {
        super(color);
        int dx = x2 - x1;
        int dy = y2 - y1;
        int length = Math.max(Math.abs(dx), Math.abs(dy));
        int x = dx < 0 ? x1 - length : x1;
        int y = dy < 0 ? y1 - length : y1;
        int size = length;
        square = new Rectangle(x, y, size, size);

    }

    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        g.drawRect(square.x, square.y, square.width, square.height);
    }

    @Override
    public boolean contains(Point p) {
        return square.contains(p);
    }

    @Override
    public void moveBy(int dx, int dy) {
        square.translate(dx, dy);
    }



    @Override
    public Rectangle getBounds() {
        return square.getBounds();
    }
}
