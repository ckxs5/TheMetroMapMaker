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
import jtps.jTPS_Transaction;
import metromap.MetroMapApp;
import metromap.data.*;

/**
 *
 * @author KaixuanChen
 */
public class Transaction_removeLine implements jTPS_Transaction {

    MetroMapApp app;
    DraggableLine trainLine;
    DraggableText trainName1;
    DraggableText trainName2;
    ArrayList<Node> metroLines;
    int k;

    public Transaction_removeLine(MetroMapApp app, DraggableLine trainLine, DraggableText trainName1, DraggableText trainName2, ArrayList<Node> metroLines) {
        this.app = app;
        this.trainLine = trainLine;
        this.trainName1 = trainName1;
        this.trainName2 = trainName2;
        this.metroLines = metroLines;
    }

    public Transaction_removeLine(MetroMapApp app, ArrayList<Node> metroLines, int k) {
        this.app = app;
        this.metroLines = metroLines;
        this.k = k;
    }

    @Override
    public void doTransaction() {
        MetroMapWorkspace ws = (MetroMapWorkspace) app.getWorkspaceComponent();
        MetroMapData data = (MetroMapData) app.getDataComponent();

        ArrayList theLine = data.getMetros().get(k);
        int linesize = theLine.size() - 1;
        int metroSize = data.getMetroNodes().size() - 1;
        trainName1 = (DraggableText) theLine.get(0);
        trainName2 = (DraggableText) theLine.get(theLine.size() - 1);
        metroLines.add(trainName1);
        for (int i = linesize; i >= 0; i--) {
            for (int j = metroSize; j >= 0; j--) {
                if (theLine.get(i) instanceof DraggableText && theLine.get(i).equals(data.getMetroNodes().get(j))) {
                    theLine.remove(theLine.get(i));
                    data.removeShape(data.getMetroNodes().get(j));
                    metroSize--;
                    break;
                } else if (theLine.get(i) instanceof DraggableLine && theLine.get(i).equals(data.getMetroNodes().get(j))) {
                    metroLines.add((Node) theLine.get(i));
                    theLine.remove(theLine.get(i));
                    data.removeShape(data.getMetroNodes().get(j));
                    metroSize--;
                    break;
                } else if (theLine.get(i) instanceof DraggableCircle && theLine.get(i).equals(data.getMetroNodes().get(j))) {
                    metroLines.add((Node) theLine.get(i));
                    theLine.remove(theLine.get(i));
                    break;
                }
            }
        }
        metroLines.add(trainName2);

        data.getMetros().remove(theLine);
        data.getLineNameList().remove(k);

        ObservableList<String> NameCombo = FXCollections.observableList(data.getLineNameList());
        ws.getLineName().setItems(NameCombo);
        ws.getLineName().getSelectionModel().selectLast();
    }

    @Override
    public void undoTransaction() {
        MetroMapWorkspace ws = (MetroMapWorkspace) app.getWorkspaceComponent();
        MetroMapData data = (MetroMapData) app.getDataComponent();

//        data.addShape(trainName1);
        for (int i = 0; i < metroLines.size(); i++) {
            if (metroLines.get(i) instanceof DraggableText || metroLines.get(i) instanceof DraggableLine) {
                data.addShape((Node) metroLines.get(i));
            }
        }
//        data.addShape(trainLine);
//        data.addShape(trainName2);

        //Add to ArrayList observablelist to store the line arraylist data
        ArrayList<Node> lines = metroLines;
//        lines.add(trainName1);
//        for (int i = 0; i < metroLines.size(); i++) {
//            lines.add((Node) metroLines.get(i));
//        }
//        lines.add(trainName2);
        data.addMetroLines(lines);

        data.getLineNameList().add(trainName1.getText());
        ObservableList<String> NameCombo = FXCollections.observableList(data.getLineNameList());
        ws.getLineName().setItems(NameCombo);
        ws.getLineName().getSelectionModel().selectLast();
    }

}
