package sketchpad;

import sketchpad.draw.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {
    private final DrawingCanvas canvas;

    public MainFrame() {
        setTitle("SketchPad - Toolbar");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 🖼️ 中央画布
        canvas = new DrawingCanvas();
        add(canvas, BorderLayout.CENTER);

        // 🔧 左侧工具栏
        JToolBar toolBar = new JToolBar(JToolBar.VERTICAL);
        toolBar.setFloatable(false); // 禁止拖动
        add(toolBar, BorderLayout.WEST);
        addColorPicker(toolBar);
        // add tool buttons
        addToolButton(toolBar, "Scribble", () ->
                canvas.setCurrentBrush(new ScribbleBrush(canvas.getCurrentColor()))
        );
        addToolButton(toolBar, "Line", () ->
                canvas.setCurrentBrush(new LineBrush(canvas.getCurrentColor()))
        );
        addToolButton(toolBar, "Rectangle", () ->
                canvas.setCurrentBrush(new RectangleBrush(canvas.getCurrentColor()))
        );
        addToolButton(toolBar, "Ellipse", () ->
                canvas.setCurrentBrush(new EllipseBrush(canvas.getCurrentColor()))
        );
        addToolButton(toolBar, "Square", () ->
                canvas.setCurrentBrush(new SquareBrush(canvas.getCurrentColor()))
        );
        addToolButton(toolBar, "Circle", () ->
                canvas.setCurrentBrush(new CircleBrush(canvas.getCurrentColor()))
        );
        addToolButton(toolBar, "Polygon", () ->
                canvas.setCurrentBrush(new PolygonBrush(canvas.getCurrentColor()))
        );
        addToolButton(toolBar, "Selector", () ->
                canvas.setCurrentBrush(new SelectorBrush())
        );





        setVisible(true);
    }

    private void addToolButton(JToolBar bar, String name, Runnable action) {
        JButton btn = new JButton(name);
        btn.addActionListener(e -> action.run());
        bar.add(btn);
    }

    private void addColorPicker(JToolBar bar) {
        JButton colorBtn = new JButton("Color");
        colorBtn.addActionListener(e -> {
            Color chosen = JColorChooser.showDialog(this, "Choose Drawing Color", canvas.getCurrentColor());
            if (chosen != null) {
                canvas.setCurrentColor(chosen);
            }
        });
        bar.add(colorBtn);
    }

}
