package sl.shapes;

import java.awt.Polygon;

public class RegularPolygon extends Polygon {

    private static final long serialVersionUID = 1L;

    public RegularPolygon(final int x, final int y, final int r, final int vertexCount,
            final double startAngle) {
        super(getXCoordinates(x, r, vertexCount, startAngle),
                getYCoordinates(y, r, vertexCount, startAngle), vertexCount);
    }

    protected static int[] getXCoordinates(final int x, final int r,
            final int vertexCount, final double startAngle) {
        int res[] = new int[vertexCount];
        double addAngle = 2 * Math.PI / vertexCount;
        double angle = startAngle;
        for (int i = 0; i < vertexCount; i++) {
            res[i] = (int) Math.round(r * Math.cos(angle)) + x;
            angle += addAngle;
        }
        return res;
    }

    protected static int[] getYCoordinates(final int y, final int r,
            final int vertexCount, final double startAngle) {
        int res[] = new int[vertexCount];
        double addAngle = 2 * Math.PI / vertexCount;
        double angle = startAngle;
        for (int i = 0; i < vertexCount; i++) {
            res[i] = (int) Math.round(r * Math.sin(angle)) + y;
            angle += addAngle;
        }
        return res;
    }
}
