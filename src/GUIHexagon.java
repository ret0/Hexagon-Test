import java.awt.Color;

import sl.shapes.RegularPolygon;


public class GUIHexagon {

    private final Color hexagonColor;
    private final RegularPolygon polygon;

    public GUIHexagon(final Color hexagonColor, final RegularPolygon polygon) {
        this.hexagonColor = hexagonColor;
        this.polygon = polygon;
    }

    public Color getHexagonColor() {
        return hexagonColor;
    }

    public RegularPolygon getPolygon() {
        return polygon;
    }



}
