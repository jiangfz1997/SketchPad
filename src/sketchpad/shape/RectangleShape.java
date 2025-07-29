package sketchpad.shape;

import java.awt.*;

public class RectangleShape extends Shape {
    private Rectangle rectangle;
    public RectangleShape(int x1, int y1, int x2, int y2, Color color) {
        super(color);
        int x = Math.min(x1, x2);
        int y = Math.min(y1, y2);
        int width = Math.abs(x2 - x1);
        int height = Math.abs(y2 - y1);
        rectangle = new Rectangle(x, y, width, height);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        g.drawRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    @Override
    public boolean contains(Point p) {
        return rectangle.contains(p);
    }

    @Override
    public Rectangle getBounds() {
        return rectangle.getBounds();
    }

//    @Override
//    public void drawSelection(Graphics g) {
//        if (!selected) return;
//
//        Graphics2D g2 = (Graphics2D) g;
//        Rectangle bounds = getBounds();
//
//        g2.setColor(Color.GRAY);
//        float[] dash = {4f, 4f};
//        g2.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 1f, dash, 0f));
//        g2.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
//
//        g2.setStroke(new BasicStroke()); // 恢复默认
//    }

    @Override
    public void moveBy(int dx, int dy) {
        rectangle.translate(dx, dy);
    }




}
