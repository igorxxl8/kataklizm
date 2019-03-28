package stdlib

import javax.swing.*
import java.awt.*

internal class KataklizmWindow(title: String) : JFrame(title) {

    fun drawLine(x0: Int, y0: Int, x1: Int, y1: Int, rgb: Array<Value>) {
        val g = graphics
        g.color = Color(rgb[0].asNumber().toInt(), rgb[1].asNumber().toInt(), rgb[2].asNumber().toInt())
        g.drawLine(x0, y0, x1, y1)
    }

    fun drawRect(x0: Int, y0: Int, x1: Int, y1: Int, rgb: Array<Value>, isFill: String) {
        val g = graphics
        g.color = Color(rgb[0].asNumber().toInt(), rgb[1].asNumber().toInt(), rgb[2].asNumber().toInt())

        when (isFill) {
            "fill" -> g.fillRect(x0, y0, x1 - x0, y1 - y0)
            else -> g.drawRect(x0, y0, x1 - x0, y1 - y0)
        }

    }

    fun drawCircle(x0: Int, y0: Int, x1: Int, y1: Int, rgb: Array<Value>, isFill: String) {
        val g = graphics
        g.color = Color(rgb[0].asNumber().toInt(), rgb[1].asNumber().toInt(), rgb[2].asNumber().toInt())

        when (isFill) {
            "fill" -> g.fillOval(x0, y0, x1 - x0, y1 - y0)
            else -> g.drawOval(x0, y0, x1 - x0, y1 - y0)
        }

    }

    fun drawPolygon(xpoints: Array<Value>, ypoints: Array<Value>, rgb: Array<Value>) {
        val g = graphics
        g.color = Color(rgb[0].asNumber().toInt(), rgb[1].asNumber().toInt(), rgb[2].asNumber().toInt())
        val npoints = xpoints.size
        val xPoints = IntArray(npoints)
        val yPoints = IntArray(npoints)
        for (i in 0 until npoints) {
            xPoints[i] = xpoints[i].asNumber().toInt()
            yPoints[i] = ypoints[i].asNumber().toInt()
        }
        g.drawPolygon(xPoints, yPoints, npoints)
    }

}
