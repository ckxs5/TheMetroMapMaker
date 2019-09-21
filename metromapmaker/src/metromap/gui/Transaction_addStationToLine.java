/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metromap.gui;

import java.util.ArrayList;
import javafx.scene.Node;
import jtps.jTPS_Transaction;
import metromap.MetroMapApp;
import metromap.data.*;

/**
 *
 * @author KaixuanChen
 */
public class Transaction_addStationToLine implements jTPS_Transaction {

    MetroMapApp app;
    DraggableText endName;
    DraggableCircle station;
    DraggableLine newLine;
    ArrayList<Node> theLine;
    int k;

    public Transaction_addStationToLine(MetroMapApp app, DraggableText endName, DraggableCircle station, DraggableLine newLine, ArrayList<Node> theLine, int k) {
        this.app = app;
        this.endName = endName;
        this.station = station;
        this.newLine = newLine;
        this.theLine = theLine;
        this.k = k;
    }

    @Override
    public void doTransaction() {
        MetroMapWorkspace ws = (MetroMapWorkspace) app.getWorkspaceComponent();
        MetroMapData data = (MetroMapData) app.getDataComponent();

        for (int i = 0; i < theLine.size(); i++) {
            if (theLine.get(i) instanceof DraggableLine) {
                ((DraggableLine) theLine.get(i)).startXProperty().unbindBidirectional(((Draggable) theLine.get(i - 1)).getXProperty());
                ((DraggableLine) theLine.get(i)).startYProperty().unbindBidirectional(((Draggable) theLine.get(i - 1)).getYProperty());

                ((DraggableLine) theLine.get(i)).endXProperty().unbindBidirectional(((Draggable) theLine.get(i + 1)).getXProperty());
                ((DraggableLine) theLine.get(i)).endYProperty().unbindBidirectional(((Draggable) theLine.get(i + 1)).getYProperty());
            }
        }
        theLine.add(station);
        theLine.add(newLine);
        theLine.remove(endName);
        theLine.add(endName);
        data.addMetroLines(theLine);

        data.addShape(newLine);
        for (int j = 0; j < theLine.size(); j++) {
            if (theLine.get(j) instanceof DraggableLine) {
                ((DraggableLine) theLine.get(j)).startXProperty().bindBidirectional(((Draggable) theLine.get(j - 1)).getXProperty());
                ((DraggableLine) theLine.get(j)).startYProperty().bindBidirectional(((Draggable) theLine.get(j - 1)).getYProperty());

                ((DraggableLine) theLine.get(j)).endXProperty().bindBidirectional(((Draggable) theLine.get(j + 1)).getXProperty());
                ((DraggableLine) theLine.get(j)).endYProperty().bindBidirectional(((Draggable) theLine.get(j + 1)).getYProperty());
            }
        }
    }

    @Override
    public void undoTransaction() {
        MetroMapWorkspace ws = (MetroMapWorkspace) app.getWorkspaceComponent();
        MetroMapData data = (MetroMapData) app.getDataComponent();
        for (int i = 0; i < theLine.size(); i++) {
            if (theLine.get(i) instanceof DraggableCircle && theLine.get(i).equals(station)) {
                ((DraggableCircle) theLine.get(i)).getXProperty().unbindBidirectional(((DraggableLine) theLine.get(i - 1)).endXProperty());
                ((DraggableCircle) theLine.get(i)).getYProperty().unbindBidirectional(((DraggableLine) theLine.get(i - 1)).endYProperty());

                ((DraggableCircle) theLine.get(i)).getXProperty().unbindBidirectional(((DraggableLine) theLine.get(i + 1)).startXProperty());
                ((DraggableCircle) theLine.get(i)).getYProperty().unbindBidirectional(((DraggableLine) theLine.get(i + 1)).startYProperty());

                ((DraggableLine) theLine.get(i + 1)).endXProperty().unbindBidirectional(((Draggable) theLine.get(i + 2)).getXProperty());
                ((DraggableLine) theLine.get(i + 1)).endYProperty().unbindBidirectional(((Draggable) theLine.get(i + 2)).getYProperty());

                ((DraggableLine) theLine.get(i - 1)).endXProperty().bindBidirectional(((Draggable) theLine.get(i + 2)).getXProperty());
                ((DraggableLine) theLine.get(i - 1)).endYProperty().bindBidirectional(((Draggable) theLine.get(i + 2)).getYProperty());

                for (int j = 0; j < data.getMetroNodes().size(); j++) {
                    if (theLine.get(i + 1).equals(data.getMetroNodes().get(j))) {
                        data.removeShape(data.getMetroNodes().get(j));
                    }
                }
                theLine.remove(theLine.get(i + 1));
                theLine.remove(theLine.get(i));

            }
        }
    }

}
