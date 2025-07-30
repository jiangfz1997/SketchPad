package sketchpad.command;

import sketchpad.shape.Shape;

import java.util.List;

public class AddShapeCommand implements Command {
    private final List<Shape> shapeList;
    private final Shape shape;

    public AddShapeCommand(List<Shape> shapeList, Shape shape) {
        this.shapeList = shapeList;
        this.shape = shape;
    }

    @Override
    public void execute() {
        shapeList.add(shape);
    }

    @Override
    public void undo() {
        shapeList.remove(shape);
    }
}
