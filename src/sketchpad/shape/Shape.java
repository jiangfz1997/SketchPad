package sketchpad.shape;

import java.awt.*;
import java.io.Serial;
import java.io.Serializable;

import static java.awt.BasicStroke.CAP_BUTT;
import static java.awt.BasicStroke.JOIN_MITER;
import org.apache.commons.lang3.SerializationUtils;


public abstract class Shape implements Cloneable, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    protected Color color;
    protected boolean selected = false;
    public Shape(Color color) {
        this.color = color;
    }

    public abstract void draw(Graphics g);

    // 是否点中图形（点击选择用）
    public abstract boolean contains(Point p);

    // 平移图形
    public abstract void moveBy(int dx, int dy);

    public void moveTo(int newX, int newY) {
        Rectangle bounds = getBounds();
        int dx = newX - bounds.x;
        int dy = newY - bounds.y;
        moveBy(dx, dy);
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    };

    public boolean isSelected() {
        return selected;
    }
    // 抽象方法：绘制选中效果
//    public abstract void drawSelection(Graphics g);

    // 抽象方法：返回当前图形的边界
    public abstract Rectangle getBounds();


    public void drawSelection(Graphics g) {
        if (!selected) return;

        Graphics2D g2 = (Graphics2D) g;
        Rectangle bounds = getBounds();

        // 绘制虚线选框
        float[] dash = {4f, 4f};

        g2.setColor(new Color(50, 100, 255)); // 明亮蓝
        g2.setStroke(new BasicStroke(2, CAP_BUTT, JOIN_MITER, 10f, dash, 0f));
        g2.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);

        // 绘制锚点（小方块）
        int boxSize = 6;
        g2.setColor(Color.BLUE);
        g2.setStroke(new BasicStroke());

        drawAnchor(g2, bounds.x, bounds.y, boxSize); // 左上
        drawAnchor(g2, bounds.x + bounds.width, bounds.y, boxSize); // 右上
        drawAnchor(g2, bounds.x, bounds.y + bounds.height, boxSize); // 左下
        drawAnchor(g2, bounds.x + bounds.width, bounds.y + bounds.height, boxSize); // 右下
    }

    private void drawAnchor(Graphics2D g2, int x, int y, int size) {
        int half = size / 2;
        g2.fillRect(x - half, y - half, size, size);
    }

    @Override
    public Shape clone() {
        return SerializationUtils.clone(this);

    }




}
