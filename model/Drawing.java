package model;

import java.awt.*;

public class Drawing {

    private Shape shape;
    private Color shapeColor;
    private int shapeType;
    private float transPercent;

    public Drawing(Shape shape, Color shapeColor,
                   int shapeType, float transPercent) {
        this.shape = shape;
        this.shapeColor = shapeColor;
        this.shapeType = shapeType;
        this.transPercent = transPercent;
    }

    protected Shape getShape() { return this.shape; }
    protected Color getShapeColor() {
        return this.shapeColor;
    }
    protected int getShapeType() {
        return shapeType;
    }
    protected float getTransPercent() {
        return transPercent;
    }
}