package sketchpad.shape;

import java.awt.*;
import java.util.List;

public class PolygonShape extends Shape {
    private final List<Point> points;
    private boolean closed;
    private Point previewPoint = null;

    public PolygonShape(List<Point> points, Color color, boolean closed) {
        super(color);
        this.points = points;
        this.closed = closed;
    }

    public void addPoint(Point p) {
        points.add(p);
    }

    public void setPreviewPoint(Point p) {
        this.previewPoint = p;
    }
    public Point getPreviewPoint() {
        return previewPoint;
    }
    public void closePolygon() {
        this.closed = true;
        this.previewPoint = null;
    }

    public boolean isClosed() {
        return closed;
    }
    public List<Point> getPoints() {
        return points;
    }
    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        for (int i = 1; i < points.size(); i++) {
            Point p1 = points.get(i - 1);
            Point p2 = points.get(i);
            g.drawLine(p1.x, p1.y, p2.x, p2.y);
        }
        if (closed && points.size() > 2) {
            Point first = points.get(0);
            Point last = points.get(points.size() - 1);
            g.drawLine(last.x, last.y, first.x, first.y);
        } else if (previewPoint != null && !points.isEmpty()) {
            Point last = points.get(points.size() - 1);
            g.setColor(Color.GRAY);
            Graphics2D g2 = (Graphics2D) g;
            Stroke oldStroke = g2.getStroke();
            g2.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{4f, 4f}, 0));
            g2.drawLine(last.x, last.y, previewPoint.x, previewPoint.y);
            g2.setStroke(oldStroke);
        }

        if (selected) drawSelection(g);
    }

    @Override
    public boolean contains(Point p) {
        if (points.size() < 3 || !closed) return false;
        Polygon polygon = new Polygon();
        for (Point pt : points) {
            polygon.addPoint(pt.x, pt.y);
        }
        return polygon.contains(p);
    }

    @Override
    public void moveBy(int dx, int dy) {
        for (Point pt : points) {
            pt.translate(dx, dy);
        }
        if (previewPoint != null) {
            previewPoint.translate(dx, dy);
        }
    }

    @Override
    public Rectangle getBounds() {
        if (points.isEmpty()) return new Rectangle(0, 0, 0, 0);
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;

        for (Point pt : points) {
            minX = Math.min(minX, pt.x);
            minY = Math.min(minY, pt.y);
            maxX = Math.max(maxX, pt.x);
            maxY = Math.max(maxY, pt.y);
        }
        return new Rectangle(minX, minY, maxX - minX, maxY - minY);
    }

    @Override
    public void moveTo(int newX, int newY) {
        Rectangle bounds = getBounds();
        int dx = newX - bounds.x;
        int dy = newY - bounds.y;
        moveBy(dx, dy);
    }


}
