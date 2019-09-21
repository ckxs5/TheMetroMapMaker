/*
 * This class is for user to add a single train station at map with its name.
 * 
 * 
 */
package metromap.data;

import javafx.beans.property.DoubleProperty;
import static javafx.scene.paint.Color.*;
import javafx.scene.shape.Circle;

/**
 *
 * @author Kaixuan Chen
 */
public final class DraggableCircle extends Circle implements Draggable {

    double startCenterX;
    double startCenterY;
    DraggableText stationName;

    public DraggableCircle() {
        setCenterX(0.0);
        setCenterY(0.0);
        setRadius(0.0);
        setOpacity(1.0);
        stationName = null;
        startCenterX = 0.0;
        startCenterY = 0.0;

    }

    public DraggableCircle(double x, double y, double r) {
        setCenterX(x);
        setCenterY(y);
        setRadius(r);
        setStroke(BLACK);
        setFill(WHITE);
    }
    
    public DraggableCircle(double x, double y, double r, String stName) {
        setCenterX(x);
        setCenterY(y);
        setRadius(r);
        stationName.setText(stName);

        stationName.xProperty().set(getCenterX());
        stationName.yProperty().set(getCenterY());

        stationName.xProperty().bind(this.centerXProperty().add(this.radiusProperty()));
        stationName.yProperty().bind(this.centerYProperty().subtract(this.radiusProperty()));
        setStroke(BLACK);
        setFill(WHITE);
        setStrokeWidth(2);
        
    }

    public DraggableText getStationname() {
        return stationName;
    }

    public void setStationname(DraggableText name, String stName) {
        stationName = name;

        stationName.setText(stName);

        stationName.xProperty().set(getCenterX());
        stationName.yProperty().set(getCenterY());

        stationName.xProperty().bind(this.centerXProperty().add(this.radiusProperty()));
        stationName.yProperty().bind(this.centerYProperty().subtract(this.radiusProperty()));
        setStroke(BLACK);
        setFill(WHITE);
        setStrokeWidth(2);

    }

    @Override
    public MetroMapState getStartingState() {
        return MetroMapState.STARTING_CIRCLE;
    }

    @Override
    public void start(int x, int y) {
        startCenterX = x;
        startCenterY = y;
        setCenterX(startCenterX);
        setCenterY(startCenterY);
    }

    @Override
    public void drag(int x, int y) {
        double diffX = x - startCenterX;
        double diffY = y - startCenterY;
        double newX = getCenterX() + diffX;
        double newY = getCenterY() + diffY;
        setCenterX(newX);
        setCenterY(newY);
        startCenterX = x;
        startCenterY = y;
    }

    @Override
    public void size(int x, int y) {
        double width = x - startCenterX;
        double height = y - startCenterY;
        double centerX = startCenterX + (width / 2);
        double centerY = startCenterY + (height / 2);
        setCenterX(centerX);
        setCenterY(centerY);
        setRadius(width / 2);
    }

    @Override
    public double getX() {
        return getCenterX();
    }

    @Override
    public double getY() {
        return getCenterY();
    }

    @Override
    public double getWidth() {
        return getCenterX() + getRadius();
    }

    @Override
    public double getHeight() {
        return getCenterY() + getRadius();
    }

    @Override
    public void setLocationAndSize(double initX, double initY, double initRadius, double initHeight) {
        setCenterX(initX + (initRadius / 2));
        setCenterY(initY + (initRadius / 2));
        setRadius(initRadius / 2);
    }

    @Override
    public String getShapeType() {
        return CIRCLE;
    }

    @Override
    public DoubleProperty getXProperty() {
        return centerXProperty();
    }

    @Override
    public DoubleProperty getYProperty() {
        return centerYProperty();
    }
}
