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
import metromap.data.DraggableCircle;
import metromap.data.DraggableText;
import metromap.data.MetroMapData;

/**
 *
 * @author KaixuanChen
 */
public class Transaction_addStation implements jTPS_Transaction {

    MetroMapApp app;
    DraggableText StName;
    DraggableCircle Station;
    ArrayList stations;
    int k;

    public Transaction_addStation(MetroMapApp app, DraggableText StName, DraggableCircle Station, ArrayList stations) {
        this.app = app;
        this.StName = StName;
        this.Station = Station;
        this.stations = stations;
    }

    public Transaction_addStation(MetroMapApp app, ArrayList stations, int k) {
        this.app = app;
        this.stations = stations;
        this.k = k;
    }

    @Override
    public void doTransaction() {
        MetroMapWorkspace ws = (MetroMapWorkspace) app.getWorkspaceComponent();
        MetroMapData data = (MetroMapData) app.getDataComponent();

        data.getStaionNameList().add(StName.getText());
        data.addShape(StName);
        data.addShape(Station);

        ArrayList<Node> stations = new ArrayList<>();
        stations.add(StName);
        stations.add(Station);
        data.addStaions(stations);

        ObservableList<String> NameCombo = FXCollections.observableList(data.getStaionNameList());
        ws.getStationName().setItems(NameCombo);
        ws.getStationName().getSelectionModel().selectLast();
        ws.getStationStart().setItems(NameCombo);
        ws.getStationEnd().setItems(NameCombo);
    }

    @Override
    public void undoTransaction() {
        MetroMapWorkspace ws = (MetroMapWorkspace) app.getWorkspaceComponent();
        MetroMapData data = (MetroMapData) app.getDataComponent();
        
        ArrayList theStation = data.getMetroStations().get(k);
        int stationSize = theStation.size() - 1;
        int metroSize = data.getMetroNodes().size() - 1;

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

}
