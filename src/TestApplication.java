import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class TestApplication extends JFrame {

    private static final Color DEFAULT_COLOR = Color.YELLOW;
    private static final int CENTER_Y = 300;
    private static final int CENTER_X = 350;

    public static final int CIRCLE_RADIUS = 35;

    private static final int X_DELTA = (int) (1.7 * CIRCLE_RADIUS);
    private static final int Y_DELTA = (int) (0.95 * CIRCLE_RADIUS);


    private static final long serialVersionUID = 1L;

    public TestApplication() {
        super();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new GridBagLayout());

        Set<GUIHexagon> shapes = new HashSet<GUIHexagon>();
        shapes.add(createHexagon(CENTER_X, CENTER_Y, Color.GREEN));
        Map<Integer, Color> ringColors = new HashMap<Integer, Color>();
        ringColors.put(2, Color.BLUE);
        ringColors.put(1, Color.ORANGE);
        ringColors.put(0, DEFAULT_COLOR);
        addAllNeighbors(shapes, CENTER_X, CENTER_Y, 2, ringColors);
        System.out.println(shapes.size());

        getContentPane().add(
                new Canvas(shapes, Color.blue),
                new GridBagConstraints(0, 1, 1, 1, 1, 1,
                        GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
                        new Insets(5, 5, 0, 5), 0, 0));

        setSize(800, 600);
        setLocationRelativeTo(null);
    }

    private void addAllNeighbors(final Set<GUIHexagon> shapes,
            final int centerX, final int centerY, final int ringCounter, final Map<Integer, Color> ringColors) {

        // TOP
        shapes.add(createHexagon(centerX, centerY - 2 * Y_DELTA, ringColors.get(ringCounter)));
        if (ringCounter > 0) {
            addAllNeighbors(shapes, centerX, centerY - 2 * Y_DELTA, ringCounter - 1, ringColors);
        }

        // TOP-RIGHT
        shapes.add(createHexagon(centerX + 1 * X_DELTA, centerY - 1 * Y_DELTA, ringColors.get(ringCounter)));
        if (ringCounter > 0) {
            addAllNeighbors(shapes, centerX + 1 * X_DELTA, centerY - 1 * Y_DELTA, ringCounter - 1, ringColors);
        }

        // BOTTOM-RIGHT
        shapes.add(createHexagon(centerX + 1 * X_DELTA, centerY + 1 * Y_DELTA, ringColors.get(ringCounter)));
        if (ringCounter > 0) {
            addAllNeighbors(shapes, centerX + 1 * X_DELTA, centerY + 1 * Y_DELTA, ringCounter - 1, ringColors);
        }

        // BOTTOM
        shapes.add(createHexagon(centerX, centerY + 2 * Y_DELTA, ringColors.get(ringCounter)));
        if (ringCounter > 0) {
            addAllNeighbors(shapes, centerX, centerY + 2 * Y_DELTA, ringCounter - 1, ringColors);
        }

        // BOTTOM-LEFT
        shapes.add(createHexagon(centerX - 1 * X_DELTA, centerY + 1 * Y_DELTA, ringColors.get(ringCounter)));
        if (ringCounter > 0) {
            addAllNeighbors(shapes, centerX - 1 * X_DELTA, centerY + 1 * Y_DELTA, ringCounter - 1, ringColors);
        }

        // TOP-LEFT
        shapes.add(createHexagon(centerX - 1 * X_DELTA, centerY - 1 * Y_DELTA, ringColors.get(ringCounter)));
        if (ringCounter > 0) {
            addAllNeighbors(shapes, centerX - 1 * X_DELTA, centerY - 1 * Y_DELTA, ringCounter - 1, ringColors);
        }

    }

    private GUIHexagon createHexagon(final int xCenter, final int yCenter,
            final Color color) {
        return new GUIHexagon(color, xCenter, yCenter);
    }

    public static void main(final String[] args) {
        new TestApplication().setVisible(true);
    }

    protected static class Canvas extends JPanel {

        private static final int STROKE_WIDTH = 4;

        private static final long serialVersionUID = 1L;

        Set<GUIHexagon> shapes;
        Color color;

        public Canvas(final Set<GUIHexagon> shapes, final Color color) {
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
            graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            for (GUIHexagon hexagon : shapes) {
                graphics2d.setColor(hexagon.getHexagonColor());
                graphics2d.fill(hexagon.getPolygon());
            }
            for (GUIHexagon hexagon : shapes) {
                graphics2d.setColor(Color.BLACK);
                graphics2d.setStroke(new BasicStroke(STROKE_WIDTH));
                graphics2d.draw(hexagon.getPolygon());
            }
        }
    }
}
