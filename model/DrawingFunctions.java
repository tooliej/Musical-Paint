package model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;

public class DrawingFunctions implements DrawingFunctionsImpl{

     public Ellipse2D.Float drawFixedEllipse(int x1, int y1, int width, int height) {
	    return new Ellipse2D.Float(x1, y1, width, height);
	 }

	 public Line2D.Float drawLine(int x1, int y1, int x2, int y2) {
             return new Line2D.Float(x1, y1, x2, y2);
     }

     public Ellipse2D.Float drawEllipse(int x1, int y1, int x2, int y2) {
         int x = Math.min(x1, x2);
         int y = Math.min(y1, y2);
         int width = Math.abs(x1 - x2);
         int height = Math.abs(y1 - y2);

         return new Ellipse2D.Float(x, y, width, height);
     }

     public Rectangle2D.Float drawFixedRectangle(int x1, int y1, int width, int height) {
        return new Rectangle2D.Float(x1, y1, width, height);
     }

     public Rectangle2D.Float drawRectangle(int x1, int y1, int x2, int y2) {
         int x = Math.min(x1, x2);
         int y = Math.min(y1, y2);
         int width = Math.abs(x1 - x2);
         int height = Math.abs(y1 - y2);

         return new Rectangle2D.Float(x, y, width, height);
     }

     public Polygon drawTriangle(int x1, int y1, int x2, int y2) {
         int x = Math.max(x1, x2);
         int y = Math.max(y1, y2);
         int width = Math.abs(x1 - x2);
         int height = Math.abs(y1 - y2);

         return new Polygon(new int[] {x, (int) (x - 0.5 * width), x - width},
                 new int[] {y, y - height, y}, 3);
     }

     public void saveImage(String path, Graphics2D g2, DrawingBoard drawB) {
        try {
            BufferedImage image = new BufferedImage(drawB.getWidth(),
                    drawB.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
            Graphics g = image.getGraphics();
            drawB.printAll(g);
            g.dispose();
            ImageIO.write(image, "png", new File(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
     }
}













