package metromap.data;
//This is a draggable inferface for classes to create draggable elements.

import javafx.beans.property.DoubleProperty;


public interface Draggable {

    public static final String LINE = "LINE";
    public static final String CIRCLE = "CIRCLE";
    public static final String IMAGE = "IMAGE";
    public static final String TEXT = "TEXT";

    public MetroMapState getStartingState();

    public void start(int x, int y);

    public void drag(int x, int y);

    public void size(int x, int y);

    public double getX();

    public double getY();

    public double getWidth();

    public double getHeight();
    
    public DoubleProperty getXProperty();
    
    public DoubleProperty getYProperty();

    public void setLocationAndSize(double initX, double initY, double initWidth, double initHeight);

    public String getShapeType();
}
