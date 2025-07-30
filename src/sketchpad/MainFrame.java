package sketchpad;

import sketchpad.draw.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;

public class MainFrame extends JFrame {
    private final DrawingCanvas canvas;

    public MainFrame() {
        setTitle("SketchPad - Toolbar");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        canvas = new DrawingCanvas();
        add(canvas, BorderLayout.CENTER);
        setJMenuBar(createMenuBar());

        JToolBar toolBar = new JToolBar(JToolBar.VERTICAL);
        toolBar.setFloatable(false);
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
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");

        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Save As");
            chooser.setFileFilter(new FileNameExtensionFilter("SketchPad files (*.sketchpad)", "sketchpad"));

            int result = chooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();

                if (!file.getName().toLowerCase().endsWith(".sketchpad")) {
                    file = new File(file.getAbsolutePath() + ".sketchpad");
                }

                canvas.saveToFile(file);
            }
        });

        JMenuItem loadItem = new JMenuItem("Load");
        loadItem.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Open File");
            chooser.setFileFilter(new FileNameExtensionFilter("SketchPad files (*.sketchpad)", "sketchpad"));

            int result = chooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                canvas.loadFromFile(file);
            }
        });

        fileMenu.add(saveItem);
        fileMenu.add(loadItem);
        menuBar.add(fileMenu);

        return menuBar;
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
