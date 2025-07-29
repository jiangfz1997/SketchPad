package sketchpad.draw;

import sketchpad.shape.*;
import sketchpad.shape.Shape;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.List;

public class EllipseBrush implements DrawStrategy {
    private final Color color;
    private int x0, y0, x1, y1;
    private boolean dragging = false;

    public EllipseBrush(Color color) {
        this.color = color;
    }

    @Override
    public void onMousePressed(MouseEvent e, List<Shape> shapeList) {
        x0 = e.getX();
        y0 = e.getY();
        dragging = true;
    }

    @Override
    public void onMouseDragged(MouseEvent e, List<Shape> shapeList) {
        x1 = e.getX();
        y1 = e.getY();
    }

    @Override
    public void onMouseReleased(MouseEvent e, List<Shape> shapeList) {
        if (dragging) {
            x1 = e.getX();
            y1 = e.getY();
            shapeList.add(new EllipseShape(x0, y0, x1, y1, color));
            dragging = false;
        }
    }

    @Override
    public void drawPreview(Graphics g) {
        if (!dragging) return;
        g.setColor(color);
        int x = Math.min(x0, x1);
        int y = Math.min(y0, y1);
        int w = Math.abs(x1 - x0);
        int h = Math.abs(y1 - y0);
        g.drawOval(x, y, w, h);
    }
}
