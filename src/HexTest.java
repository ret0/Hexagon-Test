import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class HexTest extends JPanel {

    private static final long serialVersionUID = 1L;

    final static int SIDES = 6;
    double MIN_DIST = 0;
    boolean showCenters = false;
    List<HexCell> cells;
    double scale = 3 / 4.0;

    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        double w = getWidth();
        double h = getHeight();
        double R = Math.min(w, h) / 8;
        MIN_DIST = R / 4;
        Rectangle r = getBounds();
        r.grow((int) (R * 3 / 4), (int) (R * 3 / 4));
        if (cells == null) {
            initHexCells(w, h, R, r);
        }
        g2.setPaint(Color.black);
        for (HexCell cell : cells) {
            cell.draw(g2);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(400, 400);
    }

    private void initHexCells(final double w, final double h, final double R,
            final Rectangle range) {
        Path2D.Double path = getPath(w / 2, h / 2, R);
        Rectangle2D bounds = path.getBounds2D();
        double radius = Math.min(bounds.getWidth(), bounds.getHeight());
        List<Point2D.Double> list = getAllPoints(bounds.getCenterX(),
                bounds.getCenterY(), radius, range);
        cells = new LinkedList<HexCell>();
        // For HexCell to find the side that starts at zero degrees.
        double theta = 0;
        for (int i = 0; i < list.size(); i++) {
            String id = String.valueOf(i);
            Point2D.Double p = list.get(i);
            double x = p.x - w / 2;
            double y = p.y - h / 2;
            AffineTransform at = AffineTransform.getTranslateInstance(x, y);
            Shape s = at.createTransformedShape(path);
            String[] adjacentIds = getNeighbors(i, radius, list);
            cells.add(new HexCell(id, p, s, theta, adjacentIds));
        }
    }

    private String[] getNeighbors(final int index, double radius,
            final List<Point2D.Double> list) {
        // Collect neighbors clockwise starting at zero degrees.
        String[] ids = new String[SIDES];
        double thetaInc = Math.PI / 6;
        Point2D.Double center = list.get(index);
        // Make ellipse larger to include the points we're
        // looking for so we can use the intersects method.
        radius += 1;
        Ellipse2D.Double e = new Ellipse2D.Double(center.x - radius, center.y
                - radius, 2 * radius, 2 * radius);
        for (int i = 0; i < list.size(); i++) {
            if (i == index)
                continue;
            Point2D.Double p = list.get(i);
            if (e.contains(p)) {
                // Get bearing to p.
                double phi = Math.atan2(p.y - center.y, p.x - center.x);
                // Avoid case of -0.0 for negative phi.
                if (phi < 0.0 && phi < -0.0001)
                    phi += 2 * Math.PI;
                // Index into array found with thetaInc.
                int j = (int) Math.round(phi / thetaInc);
                j /= 2;
                if (j < 0)
                    j += 5;
                if (j < ids.length) {
                    ids[j] = String.valueOf(i);
                }
            }
        }
        return ids;
    }

    private List<Point2D.Double> getAllPoints(final double cx, final double cy,
            final double radius, final Rectangle range) {
        Point2D.Double center = new Point2D.Double(cx, cy);
        List<Point2D.Double> list = new ArrayList<Point2D.Double>();
        list.add(center);
        Point2D.Double[] points = { new Point2D.Double(cx, cy) };
        List<Point2D.Double> subList = null;
        do {
            List<Point2D.Double> nextPoints = new ArrayList<Point2D.Double>();
            for (Double point : points) {
                subList = getPoints(point.x, point.y, radius, range, center);
                for (int j = 0; j < subList.size(); j++) {
                    Point2D.Double p = subList.get(j);
                    if (!haveCloseEnoughPoint(p, list)) {
                        list.add(p);
                        nextPoints.add(p);
                    }
                }
            }
            points = nextPoints.toArray(new Point2D.Double[nextPoints.size()]);
        } while (points.length > 0);

        return list;
    }

    private List<Point2D.Double> getPoints(final double cx, final double cy,
            final double radius, final Rectangle r, final Point2D.Double center) {
        List<Point2D.Double> list = new ArrayList<Point2D.Double>();
        double minDist = center.distance(cx, cy);
        for (int i = 0; i < SIDES; i++) {
            double theta = i * Math.PI / 3;
            theta += Math.PI / 6;
            double x = cx + radius * Math.cos(theta);
            double y = cy + radius * Math.sin(theta);
            double distance = center.distance(x, y);
            if (r.contains(x, y) && distance > minDist) {
                list.add(new Point2D.Double(x, y));
            }
        }
        return list;
    }

    private boolean haveCloseEnoughPoint(final Point2D.Double p,
            final List<Point2D.Double> list) {
        for (int i = 0; i < list.size(); i++) {
            Point2D.Double next = list.get(i);
            if (next.distance(p) < MIN_DIST) {
                return true;
            }
        }
        return false;
    }

    private Path2D.Double getPath(final double cx, final double cy,
            final double R) {
        Path2D.Double path = new Path2D.Double();
        double thetaInc = 2 * Math.PI / SIDES;
        double theta = thetaInc;
        double x = cx + R * Math.cos(theta);
        double y = cy + R * Math.sin(theta);
        path.moveTo(x, y);
        for (int i = 0; i < SIDES; i++) {
            theta += thetaInc;
            x = cx + R * Math.cos(theta);
            y = cy + R * Math.sin(theta);
            path.lineTo(x, y);
        }
        return path;
    }

    public static void main(final String[] args) {
        HexTest test = new HexTest();
        JFrame f = new JFrame("click me");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(test);
        f.pack();
        f.setLocation(100, 100);
        f.setVisible(true);
        test.addMouseListener(test.switcher);
        test.addComponentListener(test.resizeMonitor);
    }

    private void reset() {
        cells = null;
        repaint();
    }

    private MouseListener switcher = new MouseAdapter() {
        @Override
        public void mousePressed(final MouseEvent e) {
            Point p = e.getPoint();
            for (HexCell cell : cells) {
                if (cell.contains(p)) {
                    cell.toggleSelection();
                    break;
                }
            }
            repaint();
        }
    };

    private ComponentListener resizeMonitor = new ComponentAdapter() {
        @Override
        public void componentResized(final ComponentEvent e) {
            reset();
        }
    };
}

class HexCell {
    String id;
    Point2D.Double center;
    Shape shape;
    double start;
    String[] neighbors;
    boolean isSelected = false;

    public HexCell(final String id, final Point2D.Double center,
            final Shape shape, final double start, final String[] neighbors) {
        this.id = id;
        this.center = center;
        this.shape = shape;
        this.start = start;
        this.neighbors = neighbors;
    }

    public void draw(final Graphics2D g2) {
        g2.draw(shape);
        if (isSelected) {
            // Show that we know who our neighbors are and where they live.
            Font font = g2.getFont();
            FontRenderContext frc = g2.getFontRenderContext();
            LineMetrics lm = font.getLineMetrics("0", frc);
            float sh = lm.getAscent() + lm.getDescent();
            Rectangle r = shape.getBounds();
            int R = Math.max(r.width, r.height) / 2;
            double thetaInc = 2 * Math.PI / HexTest.SIDES;
            double theta = start;
            double lastX = 0, lastY = 0;
            g2.drawRect(3, 3, 20, 20);
            for (int i = 0; i <= neighbors.length; i++) {
                double x = center.x + R * Math.cos(theta);
                double y = center.y + R * Math.sin(theta);
                if (i > 0 && neighbors[i - 1] != null) {
                    float midx = (float) (x - (x - lastX) / 2);
                    float midy = (float) (y - (y - lastY) / 2);
                    double phi = Math.atan2(midy - center.y, midx - center.x);
                    String s = neighbors[i - 1];
                    double sw = font.getStringBounds(s, frc).getWidth();
                    double diag = Math.sqrt(sw * sw + sh * sh) / 2;
                    float sx = (float) (midx - diag * Math.cos(phi) - sw / 2);
                    float sy = (float) (midy - diag * Math.sin(phi))
                            + lm.getDescent();
                    g2.drawString(s, sx, sy);
                }
                lastX = x;
                lastY = y;
                theta += thetaInc;
            }
        }
    }

    public void toggleSelection() {
        isSelected = !isSelected;
    }

    public boolean contains(final Point p) {
        return shape.contains(p);
    }

    @Override
    public String toString() {
        return "HexCell[id:" + id + ", neighbors:" + Arrays.toString(neighbors)
                + "]";
    }
}