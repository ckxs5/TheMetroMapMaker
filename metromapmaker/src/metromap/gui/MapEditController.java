package metromap.gui;

import metromap.data.MetroMapData;
import djf.AppTemplate;
import djf.components.AppWorkspaceComponent;
import static djf.settings.AppPropertyType.LOAD_ERROR_MESSAGE;
import static djf.settings.AppPropertyType.LOAD_ERROR_TITLE;
import static djf.settings.AppPropertyType.SAVE_COMPLETED_MESSAGE;
import static djf.settings.AppPropertyType.SAVE_COMPLETED_TITLE;
import static djf.settings.AppPropertyType.SAVE_WORK_TITLE;
import static djf.settings.AppPropertyType.WORK_FILE_EXT;
import static djf.settings.AppPropertyType.WORK_FILE_EXT_DESC;
import static djf.settings.AppStartupConstants.PATH_WORK;
import djf.ui.AppMessageDialogSingleton;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.Pair;
import javax.imageio.ImageIO;
import jtps.jTPS;
import jtps.jTPS_Transaction;
import metromap.MetroMapApp;
import metromap.data.*;
import properties_manager.PropertiesManager;

public class MapEditController {

    AppTemplate app;
    MetroMapData data;
    MetroMapWorkspace ws;
    jTPS jtps;
    ArrayList selectedLine;
    String SelectionFam = null;
    double SelectionSize;
    FontWeight Bold = null;
    FontPosture Italics = null;
    boolean saved;
    File currentWorkFile;

    public MapEditController(AppTemplate initApp, MetroMapWorkspace workspace, jTPS jtps) {
        app = initApp;
        data = (MetroMapData) app.getDataComponent();
        ws = workspace;
        this.jtps = jtps;
    }

    //ABOUT BUTTON ACTIONS
    public void aboutButton() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About This MetroMapMaker");
        alert.setHeaderText(null);
        alert.setContentText("Name: Metro Map Maker App\n\n\n"
                + "Developer credits: Kaixuan Chen\n\n\n"
                + "Year of work: November, 2017");
        Image image = new Image("file:./images/SFMetro.jpeg");
        ImageView myImageView = new ImageView(image);
        alert.setGraphic(myImageView);
        alert.showAndWait();
    }

    //CTEATE NEW METRO MAP
    public void processCreateNewMetroMap() {
        TextInputDialog textDialog = new TextInputDialog();
        textDialog.setTitle("Metro Map Maker");
        textDialog.setHeaderText("Create the name of the Map: ");
        textDialog.setContentText(null);
        Optional<String> result = textDialog.showAndWait();

        if (result.isPresent()) {
            Text text = new Text();
            text.setText(result.get());
            ws.setNewMetroMap(text.getText());
        }

        ws.setCounter(1);
        ws.welcomeStage.close();
    }

    //ACTIONS FOR LOAD MAIN UI
    public void handleLoadMainUI() {
        if (ws.counter == 1) {
            app.getGUI().getAppPane().setCenter(ws.getWorkspace());
        }
    }

    //ACTIONS FOR RECENT WORK
    public void processRecentWork() {
        ws.setCounter(1);
        ws.welcomeStage.close();
    }
//==================================WORKSPACE===================================

    public void ProcessRedo() {
        jtps.doTransaction();
    }

    public void ProcessUndo() {
        jtps.undoTransaction();
    }

//==================================LINETOOLBAR=================================
    public void ProcessEditLine() {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Metro Line Edit");
        dialog.setHeaderText("        Metro Line Details");

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        TextField Input = new TextField();
        Input.setPromptText("Line Name");

        ColorPicker linecolor = new ColorPicker();
        CheckBox circularline = new CheckBox("circular");

        VBox LineEdit = new VBox();
        LineEdit.setSpacing(20);
        LineEdit.getChildren().addAll(Input, linecolor, circularline);

        dialog.getDialogPane().setContent(LineEdit);
        Optional<Pair<String, String>> result = dialog.showAndWait();

        String Linename = Input.getCharacters().toString();
        Color selectedColor = linecolor.getValue();
        boolean circular = circularline.isSelected();

        if (result.isPresent() && !Linename.equals("") || circularline.isSelected()) {
            int k = ws.getSelectedLine();
            ArrayList<Node> theLine = data.getMetros().get(k);

            if (circularline.isSelected()) {
                ((DraggableText) theLine.get(0)).setCircular(true);
            } else {
                ((DraggableText) theLine.get(0)).setCircular(false);
            }

            jTPS_Transaction transaction = new Transaction_editLine((MetroMapApp) app, theLine, k, circular, Linename, selectedColor);
            jtps.addTransaction(transaction);

//            for (int i = 0; i < lineSize; i++) {
//                if (theLine.get(i) instanceof DraggableText) {
//                    ((DraggableText) theLine.get(i)).setText(Linename);
//                    ((DraggableText) theLine.get(i)).setFill(selectedColor);
//                } else if (theLine.get(i) instanceof DraggableLine) {
//                    ((DraggableLine) theLine.get(i)).setStroke(selectedColor);
//                }
//            }
//            if (circularline.isSelected()) {
//                ((DraggableText) theLine.get(lineSize - 1)).getXProperty().unbindBidirectional(((DraggableLine) theLine.get(lineSize - 2)).endXProperty());
//                ((DraggableText) theLine.get(lineSize - 1)).getYProperty().unbindBidirectional(((DraggableLine) theLine.get(lineSize - 2)).endYProperty());
//
//                ((DraggableLine) theLine.get(lineSize - 2)).endXProperty().bindBidirectional(((DraggableText) theLine.get(0)).getXProperty());
//                ((DraggableLine) theLine.get(lineSize - 2)).endYProperty().bindBidirectional(((DraggableText) theLine.get(0)).getYProperty());
//                for (int j = 0; j < data.getMetroNodes().size(); j++) {
//                    if (data.getMetroNodes().get(j).equals(theLine.get(lineSize - 1))) {
//                        data.removeShape((Node) theLine.get(lineSize - 1));
//                    }
//                }
//                theLine.remove(theLine.get(lineSize - 1));
//            }
        }

    }

    public void ProcessAddLine() {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Metro Line Edit");
        dialog.setHeaderText("        Metro Line Details");

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        TextField Input = new TextField();
        Input.setPromptText("Line Name");

        ColorPicker linecolor = new ColorPicker();

        VBox LineEdit = new VBox();
        LineEdit.setSpacing(20);
        LineEdit.getChildren().addAll(Input, linecolor);

        dialog.getDialogPane().setContent(LineEdit);
        Optional<Pair<String, String>> result = dialog.showAndWait();

        String lineName = Input.getCharacters().toString();
        DraggableLine trainLine = new DraggableLine();
        DraggableText trainName1 = new DraggableText();
        DraggableText trainName2 = new DraggableText();
        Color traincolor = linecolor.getValue();
//        data.getLineNameList().add(lineName);

        if (result.isPresent() && !lineName.equals("")) {
            trainLine.setLocationAndSize(100, 100, 600, 100);
            trainLine.setStroke(traincolor);
            trainLine.setLinename(trainName1, trainName2, lineName, traincolor);
        } else {
            dialog.close();
        }
        ArrayList<Node> metroLines = new ArrayList<>();

        jTPS_Transaction transaction = new Transaction_addLine((MetroMapApp) app, trainLine, trainName1, trainName2, metroLines);
        jtps.addTransaction(transaction);

        //Add to the biggest observableList to show on canvas
        /*        
        data.addShape(trainName1);
        data.addShape(trainLine);
        data.addShape(trainName2);

        //Add to ArrayList observablelist to store the line arraylist data
        
        metroLines.add(trainName1);
        metroLines.add(trainLine);
        metroLines.add(trainName2);
        data.addMetroLines(metroLines);

        ObservableList NameCombo = FXCollections.observableList(data.getLineNameList());
        ws.getLineName().setItems(NameCombo);
        ws.getLineName().getSelectionModel().selectLast();
         */
        //CHANGE THE STATE
        data.setState(MetroMapState.SELECTING_NODE);

        // ENABLE/DISABLE THE PROPER BUTTONS
        ws.reloadWorkspace(data);
    }

    public void ProcessRemoveLine() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure to remove this line?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            int k = ws.getSelectedLine();
            ArrayList<Node> theLine = data.getMetros().get(k);

            jTPS_Transaction transaction = new Transaction_removeLine((MetroMapApp) app, theLine, k);
            jtps.addTransaction(transaction);

//            int linesize = theLine.size() - 1;
//            int metroSize = data.getMetroNodes().size() - 1;
//
//            for (int i = linesize; i >= 0; i--) {
//                for (int j = metroSize; j >= 0; j--) {
//                    if (theLine.get(i) instanceof DraggableText && theLine.get(i).equals(data.getMetroNodes().get(j))) {
//                        theLine.remove(theLine.get(i));
//                        data.removeShape(data.getMetroNodes().get(j));
//                        metroSize--;
//                        break;
//                    } else if (theLine.get(i) instanceof DraggableLine && theLine.get(i).equals(data.getMetroNodes().get(j))) {
//                        theLine.remove(theLine.get(i));
//                        data.removeShape(data.getMetroNodes().get(j));
//                        metroSize--;
//                        break;
//                    }
//                }
//            }
//            data.getMetros().remove(theLine);
//            data.getLineNameList().remove(k);
//
//            ObservableList NameCombo = FXCollections.observableList(data.getLineNameList());
//            ws.getLineName().setItems(NameCombo);
//            ws.getLineName().getSelectionModel().selectLast();
        } else {
            alert.close();
        }

        // ENABLE/DISABLE THE PROPER BUTTONS
        ws.reloadWorkspace(data);
        app.getGUI().updateToolbarControls(false);
    }

    public void ProcessAddStationToLine() {
        Scene scene = app.getGUI().getPrimaryScene();
        scene.setCursor(Cursor.DEFAULT);

        // CHANGE THE STATE
        data.setState(MetroMapState.ADD_STATION_TO_LINE);

        // ENABLE/DISABLE THE PROPER BUTTONS
        ws.reloadWorkspace(data);
    }

    public void ProcessRemoveStationFromLine() {
        Scene scene = app.getGUI().getPrimaryScene();
        scene.setCursor(Cursor.DEFAULT);

        // CHANGE THE STATE
        data.setState(MetroMapState.REMOVE_STATION_FROM_LINE);

        // ENABLE/DISABLE THE PROPER BUTTONS
        ws.reloadWorkspace(data);
    }

    public void ProcessListStations() {
        int k = ws.getSelectedLine();
        ArrayList theLine = data.getMetros().get(k);
        String lineName = null;
        ArrayList<String> stationName = new ArrayList<>();
        for (int i = 0; i < theLine.size(); i++) {
            if (theLine.get(i) instanceof DraggableText) {
                lineName = ((DraggableText) theLine.get(i)).getText();
            } else if (theLine.get(i) instanceof DraggableCircle) {
                String stName = ((DraggableCircle) theLine.get(i)).getStationname().getText();
                stationName.add(stName);
            }
        }
        ArrayList<String> listStation = new ArrayList<>();
        for (int j = 0; j < stationName.size(); j++) {
            listStation.add(stationName.get(j) + "\n");
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Metro Line Stops");
        alert.setHeaderText(lineName + " Line Stops");
        alert.setGraphic(null);
//        for (int j = 0; j < stationName.size(); j++) {
        alert.setContentText(listStation + "\n");
//        }
        alert.showAndWait();
    }

    public void ProcessLineThickness() {
        int k = ws.getSelectedLine();
        ArrayList theLine = data.getMetros().get(k);
        double lineThickness = ws.getLineThickness().getValue();
        for (int i = 0; i < theLine.size(); i++) {
            if (theLine.get(i) instanceof DraggableLine) {
                ((DraggableLine) theLine.get(i)).setStrokeWidth(lineThickness);
            }
        }

    }

    public void ProcessSelectLine() {
        String selected = ws.getLineName().getSelectionModel().getSelectedItem();
        int k = 0;
//        for (ArrayList theLine : data.getMetros()) {
//            if (((DraggableText) theLine.get(0)).getText().equalsIgnoreCase(selected)) {
//                selectedLine = theLine;
//            }
//        }
        for (int i = 0; i < data.getMetros().size(); i++) {
            ArrayList theline = data.getMetros().get(i);
            if (((DraggableText) theline.get(0)).getText().equalsIgnoreCase(selected)) {
                k = i;
                ws.setSelectedLine(k);
                break;
            }
        }

    }
//==================================STATIONTOOLBAR==============================

    public void ProcessStationColor() {
        int k = ws.getSelectedStation();
        ArrayList theStation = data.getMetroStations().get(k);
        Color stationColor = ws.getStationColor().getValue();
        for (int i = 0; i < theStation.size(); i++) {
            if (theStation.get(i) instanceof DraggableCircle) {
                ((DraggableCircle) theStation.get(i)).setFill(stationColor);
            }
        }
    }

    public void ProcessAddSingleStation() {
        Scene scene = app.getGUI().getPrimaryScene();
        scene.setCursor(Cursor.DEFAULT);

        // CHANGE THE STATE
        data.setState(MetroMapState.SELECTING_NODE);

        TextInputDialog textDialog = new TextInputDialog();
        textDialog.setTitle("Metro Station");
        textDialog.setHeaderText("Type name of the Station: ");
        textDialog.setContentText(null);
        textDialog.setGraphic(null);
        Optional<String> result = textDialog.showAndWait();

        DraggableText StName = new DraggableText();
        DraggableCircle Station = new DraggableCircle(40, 40, 10);

        if (result.isPresent()) {
            StName.setText(result.get());
            Station.setStationname(StName, result.get());
            StName.setOnMouseClicked(e -> {
                if (e.getClickCount() == 2) {
                    Optional<String> s1 = textDialog.showAndWait();
                    if (s1.isPresent()) {
                        String t1 = s1.get();
                        StName.setText(t1);
                    }
                }
            });
        }
        ArrayList<Node> stations = new ArrayList<>();
        jTPS_Transaction transaction = new Transaction_addStation((MetroMapApp) app, StName, Station, stations);
        jtps.addTransaction(transaction);
//        data.getStaionNameList().add(StName.getText());
//        data.addShape(StName);
//        data.addShape(Station);
//

//        stations.add(StName);
//        stations.add(Station);
//        data.addStaions(stations);
//
//        ObservableList NameCombo = FXCollections.observableList(data.getStaionNameList());
//        ws.getStationName().setItems(NameCombo);
//        ws.getStationName().getSelectionModel().selectLast();
//        ws.getStationStart().setItems(NameCombo);
//        ws.getStationEnd().setItems(NameCombo);
        // ENABLE/DISABLE THE PROPER BUTTONS
        ws.reloadWorkspace(data);
    }

    public void ProcessRemoveSingleStation() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure to remove this Station?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            int k = ws.getSelectedStation();
            ArrayList<Node> theStation = data.getMetroStations().get(k);
            jTPS_Transaction transaction = new Transaction_removeStation((MetroMapApp) app, theStation, k);
            jtps.addTransaction(transaction);
//            
//            int stationSize = theStation.size() - 1;
//            int metroSize = data.getMetroNodes().size() - 1;
//
//            for (int i = stationSize; i >= 0; i--) {
//                for (int j = metroSize; j >= 0; j--) {
//                    if (theStation.get(i) instanceof DraggableText && theStation.get(i).equals(data.getMetroNodes().get(j))) {
//                        theStation.remove(theStation.get(i));
//                        data.removeShape(data.getMetroNodes().get(j));
//                        metroSize--;
//                        break;
//                    } else if (theStation.get(i) instanceof DraggableCircle && theStation.get(i).equals(data.getMetroNodes().get(j))) {
//                        theStation.remove(theStation.get(i));
//                        data.removeShape(data.getMetroNodes().get(j));
//                        metroSize--;
//                        break;
//                    }
//                }
//            }
//            data.getMetroStations().remove(theStation);
//            data.getStaionNameList().remove(k);
//
//            ObservableList NameCombo = FXCollections.observableList(data.getStaionNameList());
//            ws.getStationName().setItems(NameCombo);
//            ws.getStationName().getSelectionModel().selectLast();
        } else {
            alert.close();
        }

        // ENABLE/DISABLE THE PROPER BUTTONS
        ws.reloadWorkspace(data);
        app.getGUI().updateToolbarControls(false);

    }

    public void ProcessSnapToGrid() {
        Draggable shape = (Draggable) data.getSelectedShape();
//        shape.getXProperty().set(((int) shape.getX()) - ((int) shape.getX() % 10));
//        shape.getYProperty().set(((int) shape.getY()) - ((int) shape.getY() % 10));

        jTPS_Transaction transaction = new Transaction_snapToGrid((MetroMapApp) app, shape);
        jtps.addTransaction(transaction);
    }

    public void ProcessMoveLabel() {
        ws.getMoveLable().setOnMouseClicked(e -> {
            int k = ws.getSelectedStation();
            ArrayList theStation = data.getMetroStations().get(k);
            DraggableText stName = (DraggableText) theStation.get(0);
            DraggableCircle station = (DraggableCircle) theStation.get(1);

            switch (e.getClickCount()) {
                case 1:
                    station.getXProperty().unbind();
                    station.getYProperty().unbind();
                    stName.getXProperty().bind(station.getXProperty().add(station.getRadius()));
                    stName.getYProperty().bind(station.getYProperty().subtract(station.getRadius()));
                    break;
                case 2:
                    station.getXProperty().unbind();
                    station.getYProperty().unbind();
                    stName.getXProperty().bind(station.getXProperty().add(station.getRadius()));
                    stName.getYProperty().bind(station.getYProperty().add(station.getRadius()));
                    break;
                case 3:
                    station.getXProperty().unbind();
                    station.getYProperty().unbind();
                    stName.getXProperty().bind(station.getXProperty().subtract(station.getRadius() + 20));
                    stName.getYProperty().bind(station.getYProperty().add(station.getRadius()));
                    break;
                case 4:
                    station.getXProperty().unbind();
                    station.getYProperty().unbind();
                    stName.getXProperty().bind(station.getXProperty().subtract(station.getRadius()));
                    stName.getYProperty().bind(station.getYProperty().subtract(station.getRadius()));
                    break;
                default:
                    break;
            }
        });
    }

    public void ProcessRotateLabel() {
        ws.getRotateStationLable().setOnMouseClicked(e -> {
            int k = ws.getSelectedStation();
            ArrayList theStation = data.getMetroStations().get(k);
            DraggableText stName = (DraggableText) theStation.get(0);

            stName.setRotate(e.getClickCount() * 30);
        });
    }

    public void ProcessStationCircleR() {
        int k = ws.getSelectedStation();
        ArrayList theStation = data.getMetroStations().get(k);
        double stationR = ws.getStationCircleR().getValue();
        for (int i = 0; i < theStation.size(); i++) {
            if (theStation.get(i) instanceof DraggableCircle) {
                ((DraggableCircle) theStation.get(i)).setRadius(stationR);
            }
        }

    }

    public void ProcessSelectStation() {
        String selected = ws.getStationName().getSelectionModel().getSelectedItem();
        int k = 0;
        for (int i = 0; i < data.getMetroStations().size(); i++) {
            ArrayList theStation = data.getMetroStations().get(i);
            if (((DraggableText) theStation.get(0)).getText().equalsIgnoreCase(selected)) {
                k = i;
                ws.setSelectedStation(k);
                break;
            }
        }
    }
//==================================DECIRORTOOLBAR==============================

    public void ProcessBackgroundColor() {
        Color selectedColor = ws.getBackgroundColor().getValue();
        if (selectedColor != null) {
            data.setBackgroundColor(selectedColor);
        }
    }

    public void ProcessSetImageBackground() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(app.getGUI().getWindow());
        if (selectedFile != null) {
//            ws.getCanvas().setStyle("-fx-background-image:url(file:./images/SFMetro.jpeg)");

            BufferedImage bufferedImage = ImageIO.read(selectedFile);
            Image image = SwingFXUtils.toFXImage(bufferedImage, null);
            data.setBackgroundImage(image);
        }
    }

    public void processSelectTextToDraw() {
        // CHANGE THE STATE
        data.setState(MetroMapState.SELECTING_NODE);

        ProcessAddLabel();
        // ENABLE/DISABLE THE PROPER BUTTONS
        ws.reloadWorkspace(data);
    }

    public void ProcessAddLabel() {
        TextInputDialog textDialog = new TextInputDialog();
        textDialog.setTitle("Text Input Dialog");
        textDialog.setHeaderText("Input Label: ");
        textDialog.setContentText(null);
        Optional<String> result = textDialog.showAndWait();
        if (result.isPresent()) {
            DraggableText text = new DraggableText();
            text.setLocationAndSize(40, 40, 0, 0);
            text.setText(result.get());

            data.addShape(text);

            text.setOnMouseClicked(e -> {
                if (e.getClickCount() == 2) {
                    Optional<String> s1 = textDialog.showAndWait();
                    if (s1.isPresent()) {
                        String t1 = s1.get();
                        text.setText(t1);
                    }
                }
            });
        }
    }

    public void ProcessRemoveElement() {
        // REMOVE THE SELECTED SHAPE IF THERE IS ONE
        data.removeSelectedNode();

        // ENABLE/DISABLE THE PROPER BUTTONS
        ws.reloadWorkspace(data);
        app.getGUI().updateToolbarControls(false);
    }

    public void processSelectImageToDraw() {
        // CHANGE THE STATE
        data.setState(MetroMapState.SELECTING_NODE);

        ProcessAddImage();
        // ENABLE/DISABLE THE PROPER BUTTONS
        ws.reloadWorkspace(data);

    }

    public void ProcessAddImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(app.getGUI().getWindow());
        if (selectedFile != null) {
            try {
                BufferedImage bufferedImage = ImageIO.read(selectedFile);
                Image image = SwingFXUtils.toFXImage(bufferedImage, null);

                DraggableImage myImageView = new DraggableImage();
                myImageView.setImage(image);

                data.addShape(myImageView);

            } catch (IOException ex) {
                Logger.getLogger(MetroMapWorkspace.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
//==================================FONTTOOLBAR=================================

    public void ProcessTextColor() {
        Color selectedColor = ws.getTextColor().getValue();
        if (selectedColor != null) {
            data.setCurrentFillColor(selectedColor);
        }
    }

    public void setFontfamily() {
        SelectionFam = ws.getFontfamily().getSelectionModel().getSelectedItem();
        if (data.getSelectedShape() instanceof DraggableText) {
            DraggableText Text = new DraggableText();
            Text = (DraggableText) data.getSelectedShape();
            if (SelectionSize != 0.0) {
                Text.setFont(Font.font(SelectionFam, SelectionSize));
            } else if (Bold != null) {
                Text.setFont(Font.font(SelectionFam, Bold, 12.0));
            } else if (Italics != null) {
                Text.setFont(Font.font(SelectionFam, Italics, SelectionSize));
            } else {
                Text.setFont(Font.font(SelectionFam, Bold, Italics, 12.0));
            }
        }
    }

    public void setFontSize() {
        SelectionSize = Double.parseDouble(ws.getFontsize().getSelectionModel().getSelectedItem());
        if (data.getSelectedShape() instanceof DraggableText) {
            DraggableText Text = new DraggableText();
            Text = (DraggableText) data.getSelectedShape();
            if (SelectionFam != null) {
                Text.setFont(Font.font(SelectionFam, SelectionSize));
            } else if (Bold != null) {
                Text.setFont(Font.font(SelectionFam, Bold, SelectionSize));
            } else if (Italics != null) {
                Text.setFont(Font.font(SelectionFam, Italics, SelectionSize));
            } else {
                Text.setFont(Font.font(SelectionFam, Bold, Italics, SelectionSize));
            }
        }
    }

    public void processBold() {
        if (data.getSelectedShape() instanceof DraggableText) {
            DraggableText Text = new DraggableText();
            Text = (DraggableText) data.getSelectedShape();
            ws.boldButton.setOnMouseClicked(e -> {
                if (e.getClickCount() == 1) {
                    Bold = FontWeight.BOLD;
                } else if (e.getClickCount() == 2) {
                    Bold = FontWeight.NORMAL;
                }
            });
            if (SelectionFam == null && SelectionSize == 0.0) {
                Text.setFont(Font.font("Verdana", Bold, 12.0));
            } else if (SelectionSize != 0.0) {
                Text.setFont(Font.font(SelectionFam, Bold, SelectionSize));
            } else if (SelectionFam != null) {
                Text.setFont(Font.font(SelectionFam, Bold, 12.0));
            }
        }

    }

    public void processItalics() {
        if (data.getSelectedShape() instanceof DraggableText) {
            DraggableText Text = new DraggableText();
            Text = (DraggableText) data.getSelectedShape();
            ws.italicsButton.setOnMouseClicked(e -> {
                if (e.getClickCount() == 1) {
                    Italics = FontPosture.ITALIC;
                } else if (e.getClickCount() == 2) {
                    Italics = FontPosture.REGULAR;
                }
            });
            if (SelectionFam == null && SelectionSize == 0.0) {
                Text.setFont(Font.font("Verdana", Italics, 12.0));
            } else if (SelectionSize != 0.0) {
                Text.setFont(Font.font(SelectionFam, Italics, SelectionSize));
            } else if (SelectionFam != null) {
                Text.setFont(Font.font(SelectionFam, Italics, 12.0));
            } else {
                Text.setFont(Font.font(SelectionFam, FontWeight.BOLD, FontPosture.ITALIC, SelectionSize));
            }
        }
    }
//==================================NAVIGATIONTOOLBAR=================================

    public void ProcessShowGrid() {
        boolean selected = ws.getShowGrid().isSelected();
        Color bkcolor = data.getBackgroundColor();
        if (selected) {
            ws.getCanvas().setStyle("-fx-background-color: #b3ccff,\n"
                    + "linear-gradient(from 0.5px 0px to 10.5px 0px, repeat, black 5%, transparent 5%),\n"
                    + "linear-gradient(from 0px 0.5px to 0px 10.5px, repeat, black 5%, transparent 5%);");
        } else {
            ws.getCanvas().setStyle("-fx-background-color: " + bkcolor + ";");
        }

    }

    public void ProcessZoomIn() {
        ws.getZoomIn().setOnMouseClicked(e -> {
            jTPS_Transaction transaction = new Transaction_zoomIn((MetroMapApp) app);
            jtps.addTransaction(transaction);
//            ws.getCanvas().setScaleX(ws.getCanvas().getScaleX() + 0.1);
//            ws.getCanvas().setScaleY(ws.getCanvas().getScaleY() + 0.1);
        });
    }

    public void ProcessZoomOut() {
        ws.getZoomOut().setOnMouseClicked(e -> {
            jTPS_Transaction transaction = new Transaction_zoomOut((MetroMapApp) app);
            jtps.addTransaction(transaction);
//            ws.getCanvas().setScaleX(ws.getCanvas().getScaleX() - 0.1);
//            ws.getCanvas().setScaleY(ws.getCanvas().getScaleY() - 0.1);
        });
    }

    public void ProcessIncreaseMapSize() {
        ws.getIncreaseMapSize().setOnMouseClicked(e -> {
            jTPS_Transaction transaction = new Transaction_increaseSize((MetroMapApp) app);
            jtps.addTransaction(transaction);
//        ws.getCanvas().setMinWidth(ws.getCanvas().getBoundsInParent().getWidth() * 1.1);
//        ws.getCanvas().setMinHeight(ws.getCanvas().getBoundsInParent().getHeight() * 1.1);
        });

    }

    public void ProcessDecreaseMapSize() {
        ws.getDecreaseMapSize().setOnMouseClicked(e -> {
            jTPS_Transaction transaction = new Transaction_decreaseSize((MetroMapApp) app);
            jtps.addTransaction(transaction);
//        ws.getCanvas().setMaxWidth(ws.getCanvas().getBoundsInParent().getWidth() / 1.1);
//        ws.getCanvas().setMaxHeight(ws.getCanvas().getBoundsInParent().getHeight() / 1.1);
        });
    }

    public void processFindRoute() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Metro Line Route");

        String from = ws.getStationStart().getSelectionModel().getSelectedItem();
        String to = ws.getStationEnd().getSelectionModel().getSelectedItem();
        
        
        alert.setHeaderText("Route From " + from + " to " + to + "");
        alert.setGraphic(null);

        alert.setContentText("From: " + from + "\n" + "To: " + to + "\n");

        alert.showAndWait();
    }
}
