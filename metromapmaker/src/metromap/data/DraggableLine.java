/*
 *This class is for user to add a subway line at map and also can be dragged
 */
package metromap.data;

import javafx.beans.property.DoubleProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.TextAlignment;

/**
 *
 * @author Kaixuan Chen
 */
public class DraggableLine extends Line implements Draggable {

    double startX;
    double startY;
    double endX;
    double endY;

    DraggableText startName;
    DraggableText endName;
    Color lineColor;
    DraggableCircle circle;
    

    public DraggableLine() {
        setStartX(startX);
        setStartY(startY);
        setOpacity(1.0);
        setStrokeWidth(3);
        startX = 0.0;
        startY = 0.0;
//        startName = null;
//        endName = null;
//        lineColor = null;
//        circle = new DraggableCircle();
    }

    public DraggableLine(double startx, double starty, double endx, double endy) {
        setStartX(startx);
        setStartY(starty);
        setEndX(endx);
        setEndY(endy);
    }

    public DraggableText getStartLinename() {
        return startName;
    }

    public DraggableText getEndLinename() {
        return endName;
    }

    public void setLinename(DraggableText stName, DraggableText edName, String lineName, Color lineColor) {
        startName = stName;
        endName = edName;

        startName.setText(lineName);
        endName.setText(lineName);

        startName.xProperty().set(getX());
        startName.yProperty().set(getY());

        this.startXProperty().bindBidirectional(startName.xProperty());
        this.startYProperty().bindBidirectional(startName.yProperty());
        endName.xProperty().set(getEndX());
        endName.yProperty().set(getEndY());

        this.endXProperty().bindBidirectional(endName.xProperty());
        this.endYProperty().bindBidirectional(endName.yProperty());

        startName.setFill(lineColor);
        endName.setFill(lineColor);
    }

    public void editLine(String lineName, Color lineColor) {
        startName.setText(lineName);
        endName.setText(lineName);

        startName.setFill(lineColor);
        endName.setFill(lineColor);

        this.setStroke(lineColor);
    }

    public void addCircle(DraggableCircle shapeToAdd) {
        circle = shapeToAdd;
    }

    public void removeCircle(DraggableCircle shapeToRemove) {
        circle = shapeToRemove;
    }

    @Override
    public MetroMapState getStartingState() {
        return MetroMapState.STARTING_LINE;
    }

    @Override
    public void start(int x, int y) {
        startX = x;
        startY = y;
        setStartX(startX);
        setStartY(startY);
    }

    @Override
    public void drag(int x, int y) {
//        double diffx1 = x - startX;
//        double diffy1 = y - startY;
//        double diffx2 = endX - x;
//        double diffy2 = endY - y;
//
//        startXProperty().set(x - diffx1);
//        startYProperty().set(y - diffy1);
//        endXProperty().set(x + diffx2);
//        endYProperty().set(y + diffy2);
//        startX = x;
//        startY = y;
//        endX = x + startX;
//        endY = y + startY;

        double diffX = x - startX;
        double diffY = y - startY;
        double newX = getStartX() + diffX;
        double newY = getStartY() + diffY;
        startXProperty().set(newX);
        startYProperty().set(newY);
        startX = x;
        startY = y;
    }

    public String cT(double x, double y) {
        return "(x,y): (" + x + "," + y + ")";
    }

    @Override
    public void size(int x, int y) {
        double diffX = x - startX;
        double diffY = y - startY;
        double newEndX = startX + diffX;
        double newEndY = startY + diffY;
        startXProperty().set(startX);
        startYProperty().set(startY);
        endXProperty().set(newEndX);
        endYProperty().set(newEndY);
    }

    @Override
    public void setLocationAndSize(double initX, double initY, double finishX, double finishY) {
        setStartX(initX);
        setStartY(initY);
        setEndX(finishX);
        setEndY(finishY);
    }

    @Override
    public String getShapeType() {
        return LINE;
    }

    @Override
    public double getX() {
        return getStartX();
    }

    @Override
    public double getY() {
        return getStartY();
    }

    @Override
    public double getWidth() {
        return getEndX();
    }

    @Override
    public double getHeight() {
        return getEndY();
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
