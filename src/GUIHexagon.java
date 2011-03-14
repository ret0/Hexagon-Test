import java.awt.Color;

import sl.shapes.RegularPolygon;


public class GUIHexagon {

    private static final int STARTING_ANGLE = 0;
    private static final int NUMBER_OF_SIDES = 6;

    private final Color hexagonColor;
    private final int xCenter;
    private final int yCenter;

    public GUIHexagon(final Color hexagonColor, final int xCenter, final int yCenter) {
        this.hexagonColor = hexagonColor;
        this.xCenter = xCenter;
        this.yCenter = yCenter;
    }

    public Color getHexagonColor() {
        return hexagonColor;
    }

    public RegularPolygon getPolygon() {
        return new RegularPolygon(xCenter, yCenter, TestApplication.CIRCLE_RADIUS, NUMBER_OF_SIDES, STARTING_ANGLE);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + xCenter;
        result = prime * result + yCenter;
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GUIHexagon other = (GUIHexagon) obj;
        if (xCenter != other.xCenter)
            return false;
        if (yCenter != other.yCenter)
            return false;
        return true;
    }



}
