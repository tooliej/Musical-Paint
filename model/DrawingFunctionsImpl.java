package model;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

public interface DrawingFunctionsImpl {
	Ellipse2D.Float drawFixedEllipse(int x1, int y1, int width, int height);
    Rectangle2D.Float drawFixedRectangle(int x1, int y1, int width, int height);
	Line2D.Float drawLine(int x1, int y1, int x2, int y2);
	Ellipse2D.Float drawEllipse(int x1, int y1, int x2, int y2);
	Rectangle2D.Float drawRectangle(int x1, int y1, int x2, int y2);
	Polygon drawTriangle(int x1, int y1, int x2, int y2);
}
