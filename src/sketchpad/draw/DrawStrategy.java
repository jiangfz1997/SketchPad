package sketchpad.draw;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.List;
import sketchpad.shape.Shape;

// implementation of this interface will define what actions user can perform
// when drawing on the canvas, such as drawing lines, rectangles, circles, etc.

public interface DrawStrategy {
    void onMousePressed(MouseEvent e, List<Shape> shapeList);
    void onMouseDragged(MouseEvent e, List<Shape> shapeList);
    void onMouseReleased(MouseEvent e, List<Shape> shapeList);
    void drawPreview(Graphics g);
}
