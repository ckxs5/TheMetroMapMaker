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
import metromap.data.DraggableLine;
import metromap.data.DraggableText;
import metromap.data.MetroMapData;

/**
 *
 * @author KaixuanChen
 */
public class Transaction_addLine implements jTPS_Transaction {

    MetroMapApp app;
    DraggableLine trainLine;
    DraggableText trainName1;
    DraggableText trainName2;
    ArrayList metroLines;
    int k;

    public Transaction_addLine(MetroMapApp app, DraggableLine trainLine, DraggableText trainName1, DraggableText trainName2, ArrayList metroLines) {
        this.app = app;
        this.trainLine = trainLine;
        this.trainName1 = trainName1;
        this.trainName2 = trainName2;
        this.metroLines = metroLines;
    }

    public Transaction_addLine(MetroMapApp app, ArrayList metroLines, int k) {
        this.app = app;
        this.metroLines = metroLines;
        this.k = k;
    }

    @Override
    public void doTransaction() {
        MetroMapWorkspace ws = (MetroMapWorkspace) app.getWorkspaceComponent();
        MetroMapData data = (MetroMapData) app.getDataComponent();

        data.addShape(trainName1);
        data.addShape(trainLine);
        data.addShape(trainName2);

        //Add to ArrayList observablelist to store the line arraylist data
        ArrayList<Node> lines = new ArrayList<>();
        lines.add(trainName1);
        lines.add(trainLine);
        lines.add(trainName2);
        data.addMetroLines(lines);

        data.getLineNameList().add(trainName1.getText());
        ObservableList<String> NameCombo = FXCollections.observableList(data.getLineNameList());
        ws.getLineName().setItems(NameCombo);
        ws.getLineName().getSelectionModel().selectLast();
    }

    @Override
    public void undoTransaction() {
        MetroMapWorkspace ws = (MetroMapWorkspace) app.getWorkspaceComponent();
        MetroMapData data = (MetroMapData) app.getDataComponent();

        ArrayList theLine = data.getMetros().get(k);
        int linesize = theLine.size() - 1;
        int metroSize = data.getMetroNodes().size() - 1;

        for (int i = linesize; i >= 0; i--) {
            for (int j = metroSize; j >= 0; j--) {
                if (theLine.get(i) instanceof DraggableText && theLine.get(i).equals(data.getMetroNodes().get(j))) {
                    theLine.remove(theLine.get(i));
                    data.removeShape(data.getMetroNodes().get(j));
                    metroSize--;
                    break;
                } else if (theLine.get(i) instanceof DraggableLine && theLine.get(i).equals(data.getMetroNodes().get(j))) {
                    theLine.remove(theLine.get(i));
                    data.removeShape(data.getMetroNodes().get(j));
                    metroSize--;
                    break;
                }
            }
        }
        data.getMetros().remove(theLine);
        data.getLineNameList().remove(k);

        ObservableList<String> NameCombo = FXCollections.observableList(data.getLineNameList());
        ws.getLineName().setItems(NameCombo);
        ws.getLineName().getSelectionModel().selectLast();
    }

}
