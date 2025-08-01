package sketchpad.draw;

import sketchpad.shape.Shape;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;


public class SelectorBrush implements DrawStrategy {
    private final List<Shape> selectedShapes = new ArrayList<>();
    private int lastX, lastY;
    private Point dragStart = null;
    private Point dragEnd = null;
    private Map<Shape, Point> originalPositions = new HashMap<>();

    @Override
    public void onMousePressed(MouseEvent e, List<Shape> shapeList) {
        Point p = e.getPoint();
        dragStart = dragEnd = p;
        updateSelectedList(shapeList);
        for (int i = shapeList.size() - 1; i >= 0; i--) {
            Shape s = shapeList.get(i);
            Rectangle sRectangle = s.getBounds();
            if (s.isSelected() && sRectangle.contains(p)) {
                lastX = p.x;
                lastY = p.y;
                recordOriginalPositions();
                return;
            }
        }
        for (Shape s : shapeList) s.setSelected(false);
        selectedShapes.clear();
        for (int i = shapeList.size() - 1; i >= 0; i--) {
            Shape s = shapeList.get(i);
            Rectangle sRectangle = s.getBounds();
            if (sRectangle.contains(p)) {
                s.setSelected(true);
                selectedShapes.add(s);
                lastX = p.x;
                lastY = p.y;
                recordOriginalPositions();
                return;
            }
        }
    }

    @Override
    public void onMouseDragged(MouseEvent e, List<Shape> shapeList) {
        if (!selectedShapes.isEmpty()) {
            int dx = e.getX() - lastX;
            int dy = e.getY() - lastY;
            for (Shape s : selectedShapes) {
                s.moveBy(dx, dy);
            }
            lastX = e.getX();
            lastY = e.getY();
        } else {
            dragEnd = e.getPoint();
        }
    }

    @Override
    public void onMouseReleased(MouseEvent e, List<Shape> shapeList) {
        if (selectedShapes.isEmpty()) {
            if (dragStart != null && dragEnd != null) {
                Rectangle selectionRect = new Rectangle(
                        Math.min(dragStart.x, dragEnd.x),
                        Math.min(dragStart.y, dragEnd.y),
                        Math.abs(dragEnd.x - dragStart.x),
                        Math.abs(dragEnd.y - dragStart.y)
                );

                for (Shape s : shapeList) {
                    if (selectionRect.intersects(s.getBounds())) {
                        s.setSelected(true);
                        selectedShapes.add(s);
                    } else {
                        s.setSelected(false);
                    }
                }
            }

            dragStart = null;
            dragEnd = null;
        }
    }

    @Override
    public void drawPreview(Graphics g) {
        if (dragStart != null && dragEnd != null) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(new Color(0, 120, 215, 80)); // 半透明蓝
            int x = Math.min(dragStart.x, dragEnd.x);
            int y = Math.min(dragStart.y, dragEnd.y);
            int w = Math.abs(dragStart.x - dragEnd.x);
            int h = Math.abs(dragStart.y - dragEnd.y);
            g2d.fillRect(x, y, w, h);
            g2d.setColor(Color.BLUE);
            g2d.drawRect(x, y, w, h);
        }
    }


    private void recordOriginalPositions() {
        originalPositions.clear();
        for (Shape s : selectedShapes) {
            Rectangle b = s.getBounds();
            originalPositions.put(s, new Point(b.x, b.y));
        }
    }

    public void clearSelection(List<Shape> shapeList) {
        for (Shape s : shapeList) {
            s.setSelected(false);
        }
        selectedShapes.clear();
    }

    private void updateSelectedList(List<Shape> shapeList) {
        selectedShapes.clear();
        for (Shape s : shapeList) {
            if (s.isSelected()) {
                selectedShapes.add(s);
            }
        }
    }
}
