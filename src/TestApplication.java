import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Shape;

import javax.swing.JFrame;
import javax.swing.JPanel;

import sl.shapes.RegularPolygon;

public class TestApplication extends JFrame {

    private static final long serialVersionUID = 1L;

    Canvas cPolygons = new Canvas();

    public TestApplication() {
        super("Shapes demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new GridBagLayout());

        getContentPane().add(
                cPolygons,
                new GridBagConstraints(0, 1, 1, 1, 1, 1,
                        GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
                        new Insets(5, 5, 0, 5), 0, 0));

        initRegular();

        setSize(800, 600);
        setLocationRelativeTo(null);
    }

    public static void main(final String[] args) {
        TestApplication fr = new TestApplication();
        fr.setVisible(true);
    }

    protected void initRegular() {
        Shape[] shapes = new Shape[1];
        shapes[0] = new RegularPolygon(350, 100, 50, 6, 0);
        cPolygons.setShapes(shapes, Color.blue);
    }

    protected static class Canvas extends JPanel {
        private static final long serialVersionUID = 1L;

        Shape[] shapes;
        Color color;

        public void setShapes(final Shape[] shapes, final Color color) {
            this.shapes = shapes;
            this.color = color;
        }

        @Override
        public void paintComponent(final Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.white);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.black);
            g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
            g.setColor(color);
            for (Shape shape2 : shapes) {
                ((Graphics2D) g).draw(shape2);
            }
        }
    }
}
