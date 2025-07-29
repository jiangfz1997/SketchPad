package sketchpad.draw;

import sketchpad.shape.Shape;
import sketchpad.util.ShapeClipboard;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;


public class SelectorBrush implements DrawStrategy {
    private final List<Shape> selectedShapes = new ArrayList<>();    private int lastX, lastY;
    private Point dragStart = null;
    private Point dragEnd = null;
    private Map<Shape, Point> originalPositions = new HashMap<>();

    @Override
    public void onMousePressed(MouseEvent e, List<Shape> shapeList) {
        Point p = e.getPoint();
        dragStart = dragEnd = p;

        // Case 1: 点在选中图形内 → 拖动所有已选中图形
        for (int i = shapeList.size() - 1; i >= 0; i--) {
            Shape s = shapeList.get(i);
            if (s.isSelected() && s.contains(p)) {
                lastX = p.x;
                lastY = p.y;
                recordOriginalPositions();
                return;
            }
        }

        // Case 2: 点在未选中的图形上 → 清除所有选中，只选它
        for (Shape s : shapeList) s.setSelected(false);
        selectedShapes.clear();
        for (int i = shapeList.size() - 1; i >= 0; i--) {
            Shape s = shapeList.get(i);
            if (s.contains(p)) {
                s.setSelected(true);
                selectedShapes.add(s);
                lastX = p.x;
                lastY = p.y;
                recordOriginalPositions();
                return;
            }
        }

        // Case 3: 点在空白 → 开始框选，selectedShapes 清空，等 onMouseReleased 处理
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
}
