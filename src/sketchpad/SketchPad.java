package sketchpad;

import java.util.Locale;

public class SketchPad {
    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH);

        javax.swing.SwingUtilities.invokeLater(MainFrame::new);
    }
}
