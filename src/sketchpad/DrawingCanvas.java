package sketchpad;

import sketchpad.draw.DrawStrategy;
import sketchpad.draw.PolygonBrush;
import sketchpad.draw.SelectorBrush;
import sketchpad.shape.Shape;
import sketchpad.util.ShapeClipboard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DrawingCanvas extends JPanel {
    private List<Shape> shapes = new ArrayList<>();
    private DrawStrategy currentBrush;
    private Color currentColor = Color.BLACK;

    public DrawingCanvas() {
        setBackground(Color.WHITE);
        setupKeyBindings();


        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (currentBrush != null)
                    currentBrush.onMousePressed(e, shapes);
            }

            public void mouseReleased(MouseEvent e) {
                if (currentBrush != null)
                    currentBrush.onMouseReleased(e, shapes);
                if (e.getClickCount() == 2 && currentBrush instanceof PolygonBrush polygonBrush) {
                    polygonBrush.onMouseDoubleClick(e, shapes);
                }
                repaint();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (currentBrush != null)
                    currentBrush.onMouseDragged(e, shapes);
                repaint();
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (currentBrush instanceof SelectorBrush) {
                    setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                } else {
                    setCursor(Cursor.getDefaultCursor());
                }
            }
        });
    }

    private void setupKeyBindings() {
        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();

        inputMap.put(KeyStroke.getKeyStroke("control C"), "copy");
        inputMap.put(KeyStroke.getKeyStroke("control X"), "cut");
        inputMap.put(KeyStroke.getKeyStroke("control V"), "paste");
        inputMap.put(KeyStroke.getKeyStroke("DELETE"), "deleteSelected");
        actionMap.put("copy", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShapeClipboard.copyFrom(shapes);
                repaint();
            }
        });

        actionMap.put("cut", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShapeClipboard.copyFrom(shapes);
                shapes.removeIf(Shape::isSelected);
                repaint();
            }
        });

        actionMap.put("paste", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Shape> pasted = ShapeClipboard.paste();
                for (Shape s : shapes) {
                    if (s.isSelected()){
                        s.setSelected(false);
                    }
                }
                // 偏移一下，防止覆盖
                for (Shape s : pasted) {
                    s.moveBy(10, 10);
                    shapes.add(s);
                }

                repaint();
            }
        });
        actionMap.put("deleteSelected", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                shapes.removeIf(Shape::isSelected);
                for (Shape s : shapes) {
                    s.setSelected(false);
                }
                repaint();
            }
        });
    }

    public void setCurrentBrush(DrawStrategy brush) {
        if (brush == null) {
            throw new IllegalArgumentException("Brush cannot be null");
        }
        if (currentBrush != null && currentBrush instanceof SelectorBrush) {
            ((SelectorBrush) currentBrush).clearSelection(shapes);
            paintComponent(getGraphics()); // 清除选中状态的绘制
        }
        this.currentBrush = brush;
    }

    public void setCurrentColor(Color color) {
        this.currentColor = color;
    }

    public Color getCurrentColor() {
        return currentColor;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (Shape s : shapes) {
            s.draw(g);
            s.drawSelection(g);
        }

        if (currentBrush != null)
            currentBrush.drawPreview(g);
    }

    public void saveToFile(File file) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(shapes);
            System.out.println("✔ Saved to " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadFromFile(File file) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            Object obj = in.readObject();
            if (obj instanceof List<?>) {
                List<?> loadedList = (List<?>) obj;
                boolean valid = loadedList.stream().allMatch(o -> o instanceof Shape);
                if (valid) {
                    shapes = (List<Shape>) loadedList;
                    repaint();
                    System.out.println("✔ Loaded from " + file.getAbsolutePath());
                } else {
                    showError("Invalid file format: not a list of shapes.");
                }
            } else {
                showError("Invalid file: expected a list of shapes.");
            }
        } catch (IOException | ClassNotFoundException e) {
            showError("Failed to load file:\n" + e.getMessage());
        }
    }


    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Load Error", JOptionPane.ERROR_MESSAGE);
    }
}
