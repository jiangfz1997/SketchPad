package sketchpad.draw;


import sketchpad.shape.*;
import sketchpad.shape.Shape;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class ScribbleBrush implements DrawStrategy {
    private final Color color;
    private final List<Point> path = new ArrayList<>();
    public ScribbleBrush(Color color) {
        this.color = color;
    }


    @Override
    public void onMousePressed(MouseEvent e, List<Shape> shapeList) {
        path.clear();
        path.add(e.getPoint());
    }

    @Override
    public void onMouseDragged(MouseEvent e, List<Shape> shapeList) {
        path.add(e.getPoint());
    }

    @Override
    public void onMouseReleased(MouseEvent e, List<Shape> shapeList) {
        shapeList.add(new ScribbleShape(new ArrayList<>(path), color));
        path.clear();
    }

    @Override
    public void drawPreview(Graphics g) {
        if (path.size() < 2) return;
        g.setColor(color);
        for (int i = 1; i < path.size(); i++) {
            Point p1 = path.get(i - 1);
            Point p2 = path.get(i);
            g.drawLine(p1.x, p1.y, p2.x, p2.y);
        }
    }


}
