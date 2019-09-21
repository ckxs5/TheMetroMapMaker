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
public class Transaction_removeStation implements jTPS_Transaction {

    MetroMapApp app;
    DraggableCircle station;
    DraggableText stationName;
    ArrayList<Node> stations;
    int k;

    public Transaction_removeStation(MetroMapApp app, ArrayList<Node> stations, int k) {
        this.app = app;
        this.stations = stations;
        this.k = k;
    }

    @Override
    public void doTransaction() {
        MetroMapWorkspace ws = (MetroMapWorkspace) app.getWorkspaceComponent();
        MetroMapData data = (MetroMapData) app.getDataComponent();

        ArrayList theStation = data.getMetroStations().get(k);

        int stationSize = theStation.size() - 1;
        int metroSize = data.getMetroNodes().size() - 1;

        stationName = (DraggableText) theStation.get(0);
        station = (DraggableCircle) theStation.get(1);
        stations.add(stationName);
        stations.add(station);

        for (int i = stationSize; i >= 0; i--) {
            for (int j = metroSize; j >= 0; j--) {
                if (theStation.get(i) instanceof DraggableText && theStation.get(i).equals(data.getMetroNodes().get(j))) {
                    theStation.remove(theStation.get(i));
                    data.removeShape(data.getMetroNodes().get(j));
                    metroSize--;
                    break;
                } else if (theStation.get(i) instanceof DraggableCircle && theStation.get(i).equals(data.getMetroNodes().get(j))) {
                    theStation.remove(theStation.get(i));
                    data.removeShape(data.getMetroNodes().get(j));
                    metroSize--;
                    break;
                }
            }
        }
        data.getMetroStations().remove(theStation);
        data.getStaionNameList().remove(k);

        ObservableList<String> NameCombo = FXCollections.observableList(data.getStaionNameList());
        ws.getStationName().setItems(NameCombo);
        ws.getStationName().getSelectionModel().selectLast();
    }

    @Override
    public void undoTransaction() {
        MetroMapWorkspace ws = (MetroMapWorkspace) app.getWorkspaceComponent();
        MetroMapData data = (MetroMapData) app.getDataComponent();

        data.getStaionNameList().add(stationName.getText());
        data.addShape(stationName);
        data.addShape(station);

        ArrayList<Node> stations = new ArrayList<>();
        stations.add(stationName);
        stations.add(station);
        data.addStaions(stations);

        ObservableList<String> NameCombo = FXCollections.observableList(data.getStaionNameList());
        ws.getStationName().setItems(NameCombo);
        ws.getStationName().getSelectionModel().selectLast();
        ws.getStationStart().setItems(NameCombo);
        ws.getStationEnd().setItems(NameCombo);
    }

}
