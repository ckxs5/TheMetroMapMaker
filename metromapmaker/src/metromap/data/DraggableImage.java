/*
 * This class is for user to add a draggable little image at map.
 */
package metromap.data;

import javafx.beans.property.DoubleProperty;
import javafx.scene.image.ImageView;

/**
 *
 * @author Kaixuan Chen
 */
public class DraggableImage extends ImageView implements Draggable {

    double startX;
    double startY;

    public DraggableImage() {
        setX(0.0);
        setY(0.0);
        setFitWidth(0.0);
        setFitHeight(0.0);
        setOpacity(1.0);
        startX = 0.0;
        startY = 0.0;
    }

    @Override
    public MetroMapState getStartingState() {
        return MetroMapState.SELECTING_NODE;
    }

    @Override
    public void start(int x, int y) {
        startX = x;
        startY = y;
        setX(x);
        setY(y);
    }

    @Override
    public void drag(int x, int y) {
        double diffX = x - (getX() + (getWidth() / 2));
        double diffY = y - (getY() + (getHeight() / 2));
        double newX = getX() + diffX;
        double newY = getY() + diffY;
        xProperty().set(newX);
        yProperty().set(newY);
        startX = x;
        startY = y;
    }

    @Override
    public void size(int x, int y) {
    }

    @Override
    public void setLocationAndSize(double initX, double initY, double initWidth, double initHeight) {
        xProperty().set(initX);
        yProperty().set(initY);
        fitWidthProperty().set(initWidth);
        fitHeightProperty().set(initHeight);
    }

    @Override
    public double getWidth() {
        return getFitWidth();
    }

    @Override
    public double getHeight() {
        return getFitHeight();
    }

    @Override
    public String getShapeType() {
        return IMAGE;
    }

    @Override
    public DoubleProperty getXProperty() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DoubleProperty getYProperty() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
