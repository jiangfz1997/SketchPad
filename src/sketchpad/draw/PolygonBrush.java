package sketchpad.draw;

import sketchpad.shape.PolygonShape;
import sketchpad.shape.Shape;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class PolygonBrush implements DrawStrategy {
    private PolygonShape currentPolygon = null;
    private boolean isDraggingFirst = false;
    private boolean isFirstPointPlaced = false;
    private boolean isFirstDragCompleted = false;
    private Color color;

    public PolygonBrush(Color color) {
        this.color = color;
    }

    @Override
    public void onMousePressed(MouseEvent e, List<Shape> shapeList) {
        Point p = e.getPoint();

        if (currentPolygon == null) {
            List<Point> init = new ArrayList<>();
            init.add(p);
            currentPolygon = new PolygonShape(init, color, false);
            isDraggingFirst = true;
            isFirstPointPlaced = false;
            isFirstDragCompleted = false;
            shapeList.add(currentPolygon);
        } else if (!isDraggingFirst && isFirstDragCompleted) {
            currentPolygon.addPoint(p);
        }
    }

    @Override
    public void onMouseDragged(MouseEvent e, List<Shape> shapeList) {
        if (currentPolygon == null) return;

        Point p = e.getPoint();
        if (isDraggingFirst) {
            if (!isFirstPointPlaced && currentPolygon.getPoints().size() == 1) {
                currentPolygon.addPoint(p);
                isFirstPointPlaced = true;
            } else {
                currentPolygon.getPoints().set(1, p);
            }
        } else {
            currentPolygon.setPreviewPoint(p);
        }
    }

    @Override
    public void onMouseReleased(MouseEvent e, List<Shape> shapeList) {
        if (currentPolygon == null) return;

        if (isDraggingFirst) {
            isDraggingFirst = false;

            List<Point> pts = currentPolygon.getPoints();
            if (pts.size() < 2 || pts.get(0).equals(pts.get(1))) {
                shapeList.remove(currentPolygon);
                currentPolygon = null;
                isFirstDragCompleted = false;
                isFirstPointPlaced = false;
            } else {
                isFirstDragCompleted = true;
                currentPolygon.setPreviewPoint(null);
            }
        } else {
            Point preview = currentPolygon.getPreviewPoint();
            if (preview != null) {
                currentPolygon.addPoint(preview);
                currentPolygon.setPreviewPoint(null);
            }
        }
    }

    @Override
    public void drawPreview(Graphics g) {
    }

    /**
     * onMouseDoubleClick
     * close the current polygon if it has enough points
     * @param e
     * @param shapeList
     */
    public void onMouseDoubleClick(MouseEvent e, List<Shape> shapeList) {
        if (currentPolygon != null && currentPolygon.getPoints().size() >= 3) {
            currentPolygon.closePolygon();
            currentPolygon.setPreviewPoint(null);
            currentPolygon = null;
            isFirstDragCompleted = false;
        }
    }

    /**
     * finishPolygonOnToolSwitch
     * close the current polygon if it has enough points (discard for implementing open polygons)
     * @param shapeList
     */
    public void finishPolygonOnToolSwitch(List<Shape> shapeList) {
        if (currentPolygon != null) {
            Point preview = currentPolygon.getPreviewPoint();
            if (preview != null) {
                currentPolygon.addPoint(preview);
            }
            if (currentPolygon.getPoints().size() >= 3) {
                currentPolygon.closePolygon();
            }
            currentPolygon.setPreviewPoint(null);
            currentPolygon = null;
            isFirstDragCompleted = false;
        }
    }
}
