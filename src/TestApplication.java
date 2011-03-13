import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import sl.shapes.RegularPolygon;

public class TestApplication extends JFrame {

    private static final int CENTER_Y = 300;
    private static final int CENTER_X = 350;

    private static final int X_DELTA = 80;
    private static final int Y_DELTA = 46;

    private static final int CIRCLE_RADIUS = 50;
    private static final int STARTING_ANGLE = 0;
    private static final int NUMBER_OF_SIDES = 6;
    private static final long serialVersionUID = 1L;

    public TestApplication() {
        super();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new GridBagLayout());

        List<GUIHexagon> shapes = new LinkedList<GUIHexagon>();
        shapes.add(createHexagon(CENTER_X, CENTER_Y, Color.DARK_GRAY));
        shapes.add(createHexagon(CENTER_X + 1 * X_DELTA, CENTER_Y -1 * Y_DELTA, Color.BLUE));
        shapes.add(createHexagon(CENTER_X -1 * X_DELTA, CENTER_Y -1 * Y_DELTA, Color.RED));
        shapes.add(createHexagon(CENTER_X -1 * X_DELTA, CENTER_Y +1 * Y_DELTA, Color.GRAY));
        shapes.add(createHexagon(CENTER_X + 1 * X_DELTA, CENTER_Y +1 * Y_DELTA, Color.GREEN));
        shapes.add(createHexagon(CENTER_X + 2 * X_DELTA, CENTER_Y, Color.ORANGE));
        shapes.add(createHexagon(CENTER_X, CENTER_Y +2 * Y_DELTA, Color.CYAN));
        shapes.add(createHexagon(CENTER_X, CENTER_Y -2 * Y_DELTA, Color.MAGENTA));

        getContentPane().add(
                new Canvas(shapes, Color.blue),
                new GridBagConstraints(0, 1, 1, 1, 1, 1,
                        GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
                        new Insets(5, 5, 0, 5), 0, 0));

        setSize(800, 600);
        setLocationRelativeTo(null);
    }

    private GUIHexagon createHexagon(final int xCenter, final int yCenter, final Color color) {
        return new GUIHexagon(color, new RegularPolygon(xCenter, yCenter,
                CIRCLE_RADIUS, NUMBER_OF_SIDES, STARTING_ANGLE));
    }

    public static void main(final String[] args) {
        new TestApplication().setVisible(true);
    }

    protected static class Canvas extends JPanel {

        private static final int STROKE_WIDTH = 4;

        private static final long serialVersionUID = 1L;

        List<GUIHexagon> shapes;
        Color color;

        public Canvas(final List<GUIHexagon> shapes, final Color color) {
            super();
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
            Graphics2D graphics2d = (Graphics2D) g;
            for (GUIHexagon hexagon : shapes) {
                graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                graphics2d.setColor(hexagon.getHexagonColor());
                graphics2d.fill(hexagon.getPolygon());
                graphics2d.draw(hexagon.getPolygon());
            }
            for (GUIHexagon hexagon : shapes) {
                graphics2d.setColor(Color.BLACK);
                graphics2d.setStroke(new BasicStroke(STROKE_WIDTH));
                graphics2d.draw(hexagon.getPolygon());
            }
        }
    }
}
