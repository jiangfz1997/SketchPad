package sketchpad.util;

import sketchpad.shape.Shape;

import java.util.ArrayList;
import java.util.List;

public class ShapeClipboard {
    private static final List<Shape> clipboard = new ArrayList<>();
    private static int pasteCount = 0;
    public static void copyFrom(List<Shape> shapes) {
        clipboard.clear();
        pasteCount = 0;
        for (Shape s : shapes) {
            if (s.isSelected())
                clipboard.add(s.clone());
        }
    }

    public static List<Shape> paste() {
        List<Shape> clones = new ArrayList<>();
        pasteCount += 1;
        for (Shape s : clipboard) {
            Shape copy = s.clone();
            copy.moveBy(10*pasteCount, 10*pasteCount);  // 轻微偏移
            copy.setSelected(true);
            clones.add(copy);
        }
        return clones;
    }

    public static boolean hasContent() {
        return !clipboard.isEmpty();
    }
}
