package sketchpad.shape;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.util.List;

public class ScribbleShape extends Shape {
    private Path2D path;

    public ScribbleShape(List<Point> points, Color color) {
        super(color);
        path = new Path2D.Double();
        if (!points.isEmpty()) {
            path.moveTo(points.get(0).x, points.get(0).y);
            for (Point p : points) {
                path.lineTo(p.x, p.y);
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(color);
        g2.draw(path);
    }


    @Override
    public boolean contains(Point p) {
        // 点是否在任意一小段线上
        PathIterator it = path.getPathIterator(null);
        double[] coords = new double[6];
        double prevX = 0, prevY = 0;
        while (!it.isDone()) {
            int type = it.currentSegment(coords);
            if (type == PathIterator.SEG_MOVETO) {
                prevX = coords[0];
                prevY = coords[1];
            } else if (type == PathIterator.SEG_LINETO) {
                double dist = Line2D.ptSegDist(prevX, prevY, coords[0], coords[1], p.x, p.y);
                if (dist <= 5.0) return true;
                prevX = coords[0];
                prevY = coords[1];
            }
            it.next();
        }
        return false;
    }

    @Override
    public void moveBy(int dx, int dy) {
        AffineTransform transform = AffineTransform.getTranslateInstance(dx, dy);
        path.transform(transform);
    }




    @Override
    public Rectangle getBounds() {
        Rectangle bounds = path.getBounds();
        return new Rectangle(bounds.x - 3, bounds.y - 3, bounds.width + 6, bounds.height + 6);
    }


}
