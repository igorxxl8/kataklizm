package stdlib;

import javax.swing.*;
import java.awt.*;

class KataklizmWindow extends JFrame {

    KataklizmWindow(String title) {
        super(title);
    }

    void drawLine(int x0, int y0, int x1, int y1, Value[] rgb) {
        final var g = getGraphics();
        g.setColor(new Color((int)rgb[0].asNumber(), (int)rgb[1].asNumber(), (int)rgb[2].asNumber()));
        g.drawLine(x0, y0, x1, y1);
    }

    void drawRect(int x0, int y0, int x1, int y1, Value[] rgb, String isFill) {
        final var g = getGraphics();
        g.setColor(new Color((int)rgb[0].asNumber(), (int)rgb[1].asNumber(), (int)rgb[2].asNumber()));

        if (isFill.equals("fill")){
            g.fillRect(x0, y0, x1-x0, y1-y0);

        } else {
            g.drawRect(x0, y0, x1-x0, y1-y0);
        }

    }

    void drawCircle(int x0, int y0, int x1, int y1, Value[] rgb, String isFill) {
        final var g = getGraphics();
        g.setColor(new Color((int)rgb[0].asNumber(), (int)rgb[1].asNumber(), (int)rgb[2].asNumber()));

        if (isFill.equals("fill")){
            g.fillOval(x0, y0, x1-x0, y1-y0);
        } else {
            g.drawOval(x0, y0, x1-x0, y1-y0);
        }

    }

    void drawPolygon(Value[] xpoints, Value[] ypoints, Value[] rgb) {
        final var g = getGraphics();
        g.setColor(new Color((int)rgb[0].asNumber(), (int)rgb[1].asNumber(), (int)rgb[2].asNumber()));
        var npoints = xpoints.length;
        var xPoints = new int[npoints];
        var yPoints = new int[npoints];
        for (var i = 0; i < npoints; i++) {
            xPoints[i] = (int)xpoints[i].asNumber();
            yPoints[i] = (int)ypoints[i].asNumber();
        }
        g.drawPolygon(xPoints, yPoints, npoints);
    }

}
