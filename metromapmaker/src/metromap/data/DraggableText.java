/*
 * This class is for user to add a label for a single train station.
 */
package metromap.data;

import javafx.beans.property.DoubleProperty;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 *
 * @author Kaixuan Chen
 */
public class DraggableText extends Text implements Draggable {

    double startX;
    double startY;
    boolean circular;

    public DraggableText() {
        setFont(new Font(18));
        setX(0.0);
        setY(0.0);
        setWrappingWidth(0.0);
        setLineSpacing(0.0);
        setOpacity(1.0);
        startX = 0.0;
        startY = 0.0;

    }

    public boolean isCircular() {
        return circular;
    }

    public void setCircular(boolean circular) {
        this.circular = circular;
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
        double diffX = x - (getX() + (getWrappingWidth() / 2));
        double diffY = y - (getY() + (getLineSpacing() / 2));
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

    }

    @Override
    public String getShapeType() {
        return TEXT;
    }

    @Override
    public double getWidth() {
        return getX();
    }

    @Override
    public double getHeight() {
        return getY();
    }

    @Override
    public DoubleProperty getXProperty() {
        return xProperty();
    }

    @Override
    public DoubleProperty getYProperty() {
        return yProperty();
    }
}
