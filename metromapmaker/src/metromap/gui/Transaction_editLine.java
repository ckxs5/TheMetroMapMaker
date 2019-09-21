/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metromap.gui;

import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import jtps.jTPS_Transaction;
import metromap.MetroMapApp;
import metromap.data.Draggable;
import metromap.data.DraggableLine;
import metromap.data.DraggableText;
import metromap.data.MetroMapData;

/**
 *
 * @author KaixuanChen
 */
public class Transaction_editLine implements jTPS_Transaction {

    MetroMapApp app;
    ArrayList<Node> theLine;
    int k;
    boolean circular;
    String Linename;
    Color selectedColor;
    DraggableText endName;
    Color oldColor;
    String oldName;

    public Transaction_editLine(MetroMapApp app, ArrayList<Node> theLine, int k, boolean circular, String Linename, Color selectedColor) {
        this.app = app;
        this.theLine = theLine;
        this.k = k;
        this.circular = circular;
        this.Linename = Linename;
        this.selectedColor = selectedColor;
    }

    @Override
    public void doTransaction() {
        MetroMapData data = (MetroMapData) app.getDataComponent();
        MetroMapWorkspace ws = (MetroMapWorkspace) app.getWorkspaceComponent();

        oldColor = (Color) ((DraggableText) theLine.get(0)).getFill();
        oldName = ((DraggableText) theLine.get(0)).getText();
        int lineSize = theLine.size();

        for (int i = 0; i < lineSize; i++) {
            if (theLine.get(i) instanceof DraggableText) {
                ((DraggableText) theLine.get(i)).setText(Linename);
                ((DraggableText) theLine.get(i)).setFill(selectedColor);
            } else if (theLine.get(i) instanceof DraggableLine) {
                ((DraggableLine) theLine.get(i)).setStroke(selectedColor);
            }
        }
        
        if (circular) {
            ((DraggableText) theLine.get(lineSize - 1)).getXProperty().unbindBidirectional(((DraggableLine) theLine.get(lineSize - 2)).endXProperty());
            ((DraggableText) theLine.get(lineSize - 1)).getYProperty().unbindBidirectional(((DraggableLine) theLine.get(lineSize - 2)).endYProperty());

            ((DraggableLine) theLine.get(lineSize - 2)).endXProperty().bindBidirectional(((DraggableText) theLine.get(0)).getXProperty());
            ((DraggableLine) theLine.get(lineSize - 2)).endYProperty().bindBidirectional(((DraggableText) theLine.get(0)).getYProperty());
            for (int j = 0; j < data.getMetroNodes().size(); j++) {
                if (data.getMetroNodes().get(j).equals(theLine.get(lineSize - 1))) {
                    data.removeShape((Node) theLine.get(lineSize - 1));
                }
                endName = (DraggableText) theLine.get(lineSize - 1);
                theLine.remove(theLine.get(lineSize - 1));
            }
        }

        data.getLineNameList().remove(k);
        data.getLineNameList().add(Linename);
        ObservableList<String> NameCombo = FXCollections.observableList(data.getLineNameList());
        ws.getLineName().setItems(NameCombo);
        ws.getLineName().getSelectionModel().selectLast();
    }

    @Override
    public void undoTransaction() {
        MetroMapWorkspace ws = (MetroMapWorkspace) app.getWorkspaceComponent();
        MetroMapData data = (MetroMapData) app.getDataComponent();

        int lineSize = theLine.size();

        for (int i = 0; i < lineSize; i++) {
            if (theLine.get(i) instanceof DraggableText) {
                ((DraggableText) theLine.get(i)).setText(oldName);
                ((DraggableText) theLine.get(i)).setFill(oldColor);
            } else if (theLine.get(i) instanceof DraggableLine) {
                ((DraggableLine) theLine.get(i)).setStroke(oldColor);
            }
        }

        if (((DraggableText) theLine.get(0)).isCircular() == true) {
            endName.setText(oldName);
            endName.setFill(oldColor);
            theLine.add(endName);
            data.addShape(endName);
            ((DraggableLine) theLine.get(lineSize - 1)).endXProperty().unbindBidirectional(((DraggableText) theLine.get(0)).getXProperty());
            ((DraggableLine) theLine.get(lineSize - 1)).endYProperty().unbindBidirectional(((DraggableText) theLine.get(0)).getYProperty());

            ((DraggableLine) theLine.get(lineSize - 1)).endXProperty().bindBidirectional(endName.getXProperty());
            ((DraggableLine) theLine.get(lineSize - 1)).endYProperty().bindBidirectional(endName.getYProperty());

        }

        data.getLineNameList().remove(k);
        data.getLineNameList().add(oldName);

        ObservableList<String> NameCombo = FXCollections.observableList(data.getLineNameList());
        ws.getLineName().setItems(NameCombo);
        ws.getLineName().getSelectionModel().selectLast();
    }
}
