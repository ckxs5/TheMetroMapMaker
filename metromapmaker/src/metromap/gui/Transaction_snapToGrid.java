/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metromap.gui;

import java.util.ArrayList;
import jtps.jTPS_Transaction;
import metromap.MetroMapApp;
import metromap.data.Draggable;
import metromap.data.DraggableCircle;
import metromap.data.DraggableText;
import metromap.data.MetroMapData;

/**
 *
 * @author KaixuanChen
 */
public class Transaction_snapToGrid implements jTPS_Transaction {

    MetroMapApp app;
    Draggable shape;
    double x;
    double y;

    public Transaction_snapToGrid(MetroMapApp app, Draggable shape) {
        this.app = app;
        this.shape = shape;
    }

    @Override
    public void doTransaction() {
        x = shape.getX();
        y = shape.getY();
        shape.getXProperty().set(((int) shape.getX()) - ((int) shape.getX() % 10));
        shape.getYProperty().set(((int) shape.getY()) - ((int) shape.getY() % 10));
    }

    @Override
    public void undoTransaction() {
        shape.getXProperty().set(x);
        shape.getYProperty().set(y);
    }

}
