package metromap.file;

import java.io.IOException;
import djf.AppTemplate;
import djf.components.AppDataComponent;
import djf.components.AppFileComponent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javax.imageio.ImageIO;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import metromap.data.*;
import metromap.gui.*;

/**
 * This class serves as the file management component for this application,
 * providing all I/O services.
 *
 * @author Kaixuan Chen
 * @author ?
 * @version 1.0
 */
public class MetroMapFiles implements AppFileComponent {
    
    AppTemplate app;
    
    public MetroMapFiles(AppTemplate initApp) {
        app = initApp;
        
    }
    static final String JSON_LINE_COLOR = "color";
    static final String JSON_BG_COLOR = "background_color";
    static final String JSON_BG_IMAGE = "background_image";
    static final String JSON_NAME = "name";
    static final String JSON_LINES = "lines";
    static final String JSON_START_NAME = "start_name";
    static final String JSON_END_NAME = "end_name";
    static final String JSON_CIRCULAR = "circular";
    static final String JSON_COLOR = "color";
    static final String JSON_RED = "red";
    static final String JSON_GREEN = "green";
    static final String JSON_BLUE = "blue";
    static final String JSON_ALPHA = "alpha";
    static final String JSON_NODES = "nodes";
    static final String JSON_TYPE = "type";
    static final String JSON_STATION_NAME = "station_names";
    static final String JSON_STATIONS = "stations";
    static final String JSON_X = "x";
    static final String JSON_Y = "y";
    static final String JSON_WIDTH = "width";
    static final String JSON_HEIGHT = "height";
    
    static final String DEFAULT_DOCTYPE_DECLARATION = "<!doctype html>\n";
    static final String DEFAULT_ATTRIBUTE_VALUE = "";
    
    public void importData(AppDataComponent data, String filePath) throws IOException {
    }
    
    @Override
    public void loadData(AppDataComponent data, String filePath) throws IOException {
        MetroMapData dataManager = (MetroMapData) data;
        MetroMapWorkspace ws = (MetroMapWorkspace) app.getWorkspaceComponent();
        dataManager.resetData();
        
        JsonObject json = loadJSONFile(filePath);

        //LOAD STATIONS AND NAMES
        JsonArray jsonStationArray = json.getJsonArray(JSON_STATIONS);
        
        for (int j = 0; j < jsonStationArray.size(); j++) {
            ArrayList<Node> stationList = new ArrayList<>();
            JsonObject station = jsonStationArray.getJsonObject(j);
            
            String name = loadName(station, JSON_NAME);
            double x = getDataAsDouble(station, JSON_X);
            double y = getDataAsDouble(station, JSON_Y);
            
            DraggableCircle stations = new DraggableCircle(x, y, 15);
            DraggableText stationName = new DraggableText();
            
            stationName.setText(name);
            stations.setStationname(stationName, name);
            
            stationList.add(stationName);
            stationList.add(stations);
            dataManager.addStaions(stationList);
            dataManager.addShape(stationName);
            dataManager.addShape(stations);
            
            dataManager.getStaionNameList().add(name);
            
            ObservableList<String> stationNameCombo = FXCollections.observableList(dataManager.getStaionNameList());
            ws.getStationName().setItems(stationNameCombo);
            ws.getStationName().getSelectionModel().selectLast();
            ws.getStationStart().setItems(stationNameCombo);
            ws.getStationEnd().setItems(stationNameCombo);
        }

//        String Name = loadName(json, JSON_NAME);
//        DraggableText cityName = new DraggableText();
//        cityName.setText(Name);
//        dataManager.addShape(cityName);
        JsonArray jsonLines = json.getJsonArray(JSON_LINES);
        
        for (int i = 0; i < jsonLines.size(); i++) {
            JsonObject lines = jsonLines.getJsonObject(i);

            //GET THE Start NAME
            JsonObject startlineNameArray = lines.getJsonObject(JSON_START_NAME);
            DraggableText startName = new DraggableText();
            String lineStart = loadName(startlineNameArray, JSON_NAME);
            double startx = getDataAsDouble(startlineNameArray, JSON_X);
            double starty = getDataAsDouble(startlineNameArray, JSON_Y);
            startName.setText(lineStart);
            startName.setLocationAndSize(startx, starty, startx, starty);

            //GET THE end NAME
            JsonObject endlineNameArray = lines.getJsonObject(JSON_END_NAME);
            DraggableText endName = new DraggableText();
            String lineEnd = loadName(endlineNameArray, JSON_NAME);
            double endx = getDataAsDouble(endlineNameArray, JSON_X);
            double endy = getDataAsDouble(endlineNameArray, JSON_Y);
            endName.setText(lineEnd);
            endName.setLocationAndSize(endx, endy, endx, endy);
            
            dataManager.getLineNameList().add(lineStart);
            ObservableList<String> lineNameCombo = FXCollections.observableList(dataManager.getLineNameList());
            ws.getLineName().setItems(lineNameCombo);
            ws.getLineName().getSelectionModel().selectLast();

            //CHECK IF CIRCULAR
//            boolean linecircular = lines.getBoolean(JSON_CIRCULAR);
//            boolean circular;
//            if (linecircular == false) {
//                circular = false;
//            } else {
//                circular = true;
//            }
            // GET LINE COLOR
            Color fillColor = loadColor(lines, JSON_LINE_COLOR);
            startName.setFill(fillColor);
            endName.setFill(fillColor);

            //LOAD LINES 
            JsonArray jsonStationNameArray = lines.getJsonArray(JSON_STATION_NAME);
            ArrayList<Node> metroLines = new ArrayList<>();
            metroLines.add(startName);
            for (int k = 0; k < jsonStationNameArray.size(); k++) {
                String singleStationName = jsonStationNameArray.get(k).toString().replace("\"", "");
                for (int g = 0; g < dataManager.getMetroStations().size(); g++) {
                    ArrayList<Node> stations = dataManager.getMetroStations().get(g);
                    String stName = ((DraggableText) stations.get(0)).getText();
                    if (singleStationName.equalsIgnoreCase(stName)) {
                        DraggableLine trainLine = new DraggableLine();
                        trainLine.setStroke(fillColor);
                        metroLines.add(trainLine);
                        metroLines.add(stations.get(1));
                        dataManager.addShape(trainLine);
                    }
                }
//                DraggableText stationName = new DraggableText();
//                DraggableCircle station = new DraggableCircle();
//                DraggableLine trainLine = new DraggableLine();
//
//                trainLine.setStroke(fillColor);
//                stationName.setText(singleStationName);
//                station.setRadius(10);
//                station.setStationname(stationName, singleStationName);
//
//                metroLines.add(trainLine);
//                metroLines.add(station);
//                dataManager.addShape(station);
//                dataManager.addShape(stationName);
//                dataManager.addShape(trainLine);

            }
            DraggableLine trainLine = new DraggableLine();
            trainLine.setStroke(fillColor);
            metroLines.add(trainLine);
            metroLines.add(endName);
            
            dataManager.addMetroLines(metroLines);
            dataManager.addShape(trainLine);
            dataManager.addShape(startName);
            dataManager.addShape(endName);
            
            for (int n = 0; n < metroLines.size(); n++) {
                if (metroLines.get(n) instanceof DraggableLine) {
                    ((DraggableLine) metroLines.get(n)).startXProperty().bindBidirectional(((Draggable) metroLines.get(n - 1)).getXProperty());
                    ((DraggableLine) metroLines.get(n)).startYProperty().bindBidirectional(((Draggable) metroLines.get(n - 1)).getYProperty());
                    
                    ((DraggableLine) metroLines.get(n)).endXProperty().bindBidirectional(((Draggable) metroLines.get(n + 1)).getXProperty());
                    ((DraggableLine) metroLines.get(n)).endYProperty().bindBidirectional(((Draggable) metroLines.get(n + 1)).getYProperty());
                }
            }
        }        
        
        for (int z = 0; z < dataManager.getMetros().size(); z++) {
            ArrayList<Node> metroLines = dataManager.getMetros().get(z);
            for (int k = 0; k < metroLines.size(); k++) {
                for (int l = 0; l < dataManager.getMetroStations().size(); l++) {
                    ArrayList<Node> stationList = dataManager.getMetroStations().get(l);
                    for (int n = 0; n < stationList.size(); n++) {
                        if (stationList.get(n) instanceof DraggableCircle) {
                            if (metroLines.get(k) instanceof DraggableCircle) {
                                if (((DraggableCircle) metroLines.get(k)).getStationname().getText().equalsIgnoreCase(((DraggableCircle) stationList.get(n)).getStationname().getText())) {
                                    double stx = ((DraggableCircle) stationList.get(n)).getX();
                                    double sty = ((DraggableCircle) stationList.get(n)).getY();
                                    ((DraggableCircle) metroLines.get(k)).setLocationAndSize(stx, sty, 15, 10);
                                }
                            }
                        }
                    }
                }
            }
        }
//        for (int a = 0; a < dataManager.getMetros().size(); a++) {
//            ArrayList<Node> line1 = dataManager.getMetros().get(a);
//            for (int b = 1; b < dataManager.getMetros().size() - 2; b++) {
//                ArrayList<Node> line2 = dataManager.getMetros().get(b);
//                for (int c = 0; c < line1.size(); c++) {
//                    if (line1.get(c) instanceof DraggableCircle) {
//                        for (int d = 0; d < line2.size(); d++) {
//                            if (line2.get(d) instanceof DraggableCircle) {
//                                if (((DraggableCircle) line1.get(c)).getStationname().equals(((DraggableCircle) line2.get(d)).getStationname())) {
//                                    ((Draggable) line1.get(c)).getXProperty().unbindBidirectional(((DraggableLine) line1.get(c - 1)).startXProperty());
//                                    ((Draggable) line1.get(c)).getYProperty().unbindBidirectional(((DraggableLine) line1.get(c - 1)).startYProperty());
//
//                                    ((Draggable) line1.get(c)).getXProperty().unbindBidirectional(((DraggableLine) line1.get(c + 1)).endXProperty());
//                                    ((Draggable) line1.get(c)).getYProperty().unbindBidirectional(((DraggableLine) line1.get(c + 1)).endYProperty());
//
//                                    ((Draggable) line2.get(d)).getXProperty().bindBidirectional(((DraggableLine) line1.get(c - 1)).startXProperty());
//                                    ((Draggable) line2.get(d)).getYProperty().bindBidirectional(((DraggableLine) line1.get(c - 1)).startYProperty());
//
//                                    ((Draggable) line2.get(d)).getXProperty().bindBidirectional(((DraggableLine) line1.get(c + 1)).endXProperty());
//                                    ((Draggable) line2.get(d)).getYProperty().bindBidirectional(((DraggableLine) line1.get(c + 1)).endYProperty());
//
//                                    line1.remove(line1.get(c));
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
    }
    
    @Override
    public void saveData(AppDataComponent data, String filePath) throws IOException {
        MetroMapData dataManager = (MetroMapData) data;

        //lines:{}
        JsonArrayBuilder lineBuilder = Json.createArrayBuilder();
        ObservableList<ArrayList<Node>> lineList = dataManager.getMetros();
        for (int k = 0; k < lineList.size(); k++) {
            ArrayList line = dataManager.getMetros().get(k);
//start_name:{name;x;y}
            JsonObject startName = Json.createObjectBuilder()
                    .add(JSON_NAME, ((DraggableText) line.get(0)).getText())
                    .add(JSON_X, ((DraggableText) line.get(0)).getX())
                    .add(JSON_Y, ((DraggableText) line.get(0)).getY()).build();
//end_name:{name; x;y}
            JsonObject endName = Json.createObjectBuilder()
                    .add(JSON_NAME, ((DraggableText) line.get(line.size() - 1)).getText())
                    .add(JSON_X, ((DraggableText) line.get(line.size() - 1)).getX())
                    .add(JSON_Y, ((DraggableText) line.get(line.size() - 1)).getY()).build();
//color:{red;green;opacity}
            JsonObject linecolor = makeJsonColorObject((Color) ((DraggableLine) line.get(1)).getStroke());
//station_name[]
            JsonArrayBuilder stationName = Json.createArrayBuilder();
            for (int l = 0; l < line.size(); l++) {
                if (line.get(l) instanceof DraggableCircle) {
                    stationName.add(((DraggableCircle) line.get(l)).getStationname().getText());
                }
            }
            JsonObject lineObject = (JsonObject) Json.createObjectBuilder()
                    .add(JSON_START_NAME, startName)
                    .add(JSON_END_NAME, endName)
                    .add(JSON_COLOR, linecolor)
                    .add(JSON_STATION_NAME, stationName).build();
            lineBuilder.add(lineObject);
        }
///stations:
        JsonArrayBuilder stationBuilder = Json.createArrayBuilder();
        ObservableList<ArrayList<Node>> stationList = dataManager.getMetroStations();
        for (int i = 0; i < stationList.size(); i++) {
            ArrayList station = dataManager.getMetroStations().get(i);
            for (int j = 0; j < station.size(); j++) {
                Draggable draggableShape = (Draggable) station.get(j);
                if (draggableShape instanceof DraggableCircle) {
                    String stationName = ((DraggableCircle) draggableShape).getStationname().getText();
                    double x = ((DraggableCircle) draggableShape).getX();
                    double y = ((DraggableCircle) draggableShape).getY();
                    JsonObject stations = Json.createObjectBuilder()
                            .add(JSON_NAME, stationName)
                            .add(JSON_X, x)
                            .add(JSON_Y, y).build();
                    stationBuilder.add(stations);
                }
            }
        }
        JsonArray stationsArray = stationBuilder.build();
        JsonArray linesArray = lineBuilder.build();
        JsonObject dataManagerJSO = Json.createObjectBuilder()
                .add(JSON_LINES, linesArray)
                .add(JSON_STATIONS, stationsArray)
                .build();
        
        Map<String, Object> properties = new HashMap<>(1);
        properties.put(JsonGenerator.PRETTY_PRINTING, true);
        JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
        StringWriter sw = new StringWriter();
        JsonWriter jsonWriter = writerFactory.createWriter(sw);
        jsonWriter.writeObject(dataManagerJSO);
        jsonWriter.close();

        // INIT THE WRITER
        OutputStream os = new FileOutputStream(filePath);
        JsonWriter jsonFileWriter = Json.createWriter(os);
        jsonFileWriter.writeObject(dataManagerJSO);
        String prettyPrinted = sw.toString();
        PrintWriter pw = new PrintWriter(filePath);
        pw.write(prettyPrinted);
        pw.close();
    }

//    @Override
    public void saveAsData(AppDataComponent data, String filePath) throws IOException {
        MetroMapData dataManager = (MetroMapData) data;
        
        JsonArrayBuilder lineBuilder = Json.createArrayBuilder();
        ObservableList<ArrayList<Node>> lineList = dataManager.getMetros();
        for (int k = 0; k < lineList.size(); k++) {
            ArrayList line = dataManager.getMetros().get(k);
            
            JsonObject startName = Json.createObjectBuilder()
                    .add(JSON_NAME, ((DraggableText) line.get(0)).getText())
                    .add(JSON_X, ((DraggableText) line.get(0)).getX())
                    .add(JSON_Y, ((DraggableText) line.get(0)).getY()).build();
            
            JsonObject endName = Json.createObjectBuilder()
                    .add(JSON_NAME, ((DraggableText) line.get(line.size() - 1)).getText())
                    .add(JSON_X, ((DraggableText) line.get(line.size() - 1)).getX())
                    .add(JSON_Y, ((DraggableText) line.get(line.size() - 1)).getY()).build();
            
            JsonObject linecolor = makeJsonColorObject((Color) ((DraggableLine) line.get(1)).getStroke());
            
            JsonArrayBuilder stationName = Json.createArrayBuilder();
            for (int l = 0; l < line.size(); l++) {
                if (line.get(l) instanceof DraggableCircle) {
                    stationName.add(((DraggableCircle) line.get(l)).getStationname().getText());
                }
            }
            JsonObject lineObject = (JsonObject) Json.createObjectBuilder()
                    .add(JSON_START_NAME, startName)
                    .add(JSON_END_NAME, endName)
                    .add(JSON_COLOR, linecolor)
                    .add(JSON_STATION_NAME, stationName).build();
            lineBuilder.add(lineObject);
        }
        
        JsonArrayBuilder stationBuilder = Json.createArrayBuilder();
        ObservableList<ArrayList<Node>> stationList = dataManager.getMetroStations();
        for (int i = 0; i < stationList.size(); i++) {
            ArrayList station = dataManager.getMetroStations().get(i);
            for (int j = 0; j < station.size(); j++) {
                Draggable draggableShape = (Draggable) station.get(j);
                if (draggableShape instanceof DraggableCircle) {
                    String stationName = ((DraggableCircle) draggableShape).getStationname().getText();
                    double x = ((DraggableCircle) draggableShape).getX();
                    double y = ((DraggableCircle) draggableShape).getY();
                    JsonObject stations = Json.createObjectBuilder()
                            .add(JSON_NAME, stationName)
                            .add(JSON_X, x)
                            .add(JSON_Y, y).build();
                    stationBuilder.add(stations);
                }
            }
        }
        JsonArray stationsArray = stationBuilder.build();
        JsonArray linesArray = lineBuilder.build();
        JsonObject dataManagerJSO = Json.createObjectBuilder()
                .add(JSON_LINES, linesArray)
                .add(JSON_STATIONS, stationsArray)
                .build();
        
        Map<String, Object> properties = new HashMap<>(1);
        properties.put(JsonGenerator.PRETTY_PRINTING, true);
        JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
        StringWriter sw = new StringWriter();
        JsonWriter jsonWriter = writerFactory.createWriter(sw);
        jsonWriter.writeObject(dataManagerJSO);
        jsonWriter.close();

        // INIT THE WRITER
        OutputStream os = new FileOutputStream(filePath);
        JsonWriter jsonFileWriter = Json.createWriter(os);
        jsonFileWriter.writeObject(dataManagerJSO);
        String prettyPrinted = sw.toString();
        PrintWriter pw = new PrintWriter(filePath);
        pw.write(prettyPrinted);
        pw.close();
    }
    
    @Override
    public void exportData(AppDataComponent data, String filePath) throws IOException {
        MetroMapWorkspace ws = (MetroMapWorkspace) app.getWorkspaceComponent();
        MetroMapData dataManager = (MetroMapData) data;
        
        Pane canvas = (Pane) ws.getCanvas();
        WritableImage image = canvas.snapshot(new SnapshotParameters(), null);
        String name = ws.getNewMetroMap();
        File file = new File("work/" + name + " Metro.png");
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        } catch (IOException ioe) {
        }
        JsonArrayBuilder lineBuilder = Json.createArrayBuilder();
        ObservableList<ArrayList<Node>> lineList = dataManager.getMetros();
        for (int k = 0; k < lineList.size(); k++) {
            ArrayList line = dataManager.getMetros().get(k);
            
            JsonObject startName = Json.createObjectBuilder()
                    .add(JSON_NAME, ((DraggableText) line.get(0)).getText())
                    .add(JSON_X, ((DraggableText) line.get(0)).getX())
                    .add(JSON_Y, ((DraggableText) line.get(0)).getY()).build();
            
            JsonObject endName = Json.createObjectBuilder()
                    .add(JSON_NAME, ((DraggableText) line.get(line.size() - 1)).getText())
                    .add(JSON_X, ((DraggableText) line.get(line.size() - 1)).getX())
                    .add(JSON_Y, ((DraggableText) line.get(line.size() - 1)).getY()).build();
            
            JsonObject linecolor = makeJsonColorObject((Color) ((DraggableLine) line.get(1)).getStroke());
            
            JsonArrayBuilder stationName = Json.createArrayBuilder();
            for (int l = 0; l < line.size(); l++) {
                if (line.get(l) instanceof DraggableCircle) {
                    stationName.add(((DraggableCircle) line.get(l)).getStationname().getText());
                }
            }
            JsonObject lineObject = (JsonObject) Json.createObjectBuilder()
                    .add(JSON_START_NAME, startName)
                    .add(JSON_END_NAME, endName)
                    .add(JSON_COLOR, linecolor)
                    .add(JSON_STATION_NAME, stationName).build();
            lineBuilder.add(lineObject);
        }
        
        JsonArrayBuilder stationBuilder = Json.createArrayBuilder();
        ObservableList<ArrayList<Node>> stationList = dataManager.getMetroStations();
        for (int i = 0; i < stationList.size(); i++) {
            ArrayList station = dataManager.getMetroStations().get(i);
            for (int j = 0; j < station.size(); j++) {
                Draggable draggableShape = (Draggable) station.get(j);
                if (draggableShape instanceof DraggableCircle) {
                    String stationName = ((DraggableCircle) draggableShape).getStationname().getText();
                    double x = ((DraggableCircle) draggableShape).getX();
                    double y = ((DraggableCircle) draggableShape).getY();
                    JsonObject stations = Json.createObjectBuilder()
                            .add(JSON_NAME, stationName)
                            .add(JSON_X, x)
                            .add(JSON_Y, y).build();
                    stationBuilder.add(stations);
                }
            }
        }
        JsonArray stationsArray = stationBuilder.build();
        JsonArray linesArray = lineBuilder.build();
        JsonObject dataManagerJSO = Json.createObjectBuilder()
                .add(JSON_LINES, linesArray)
                .add(JSON_STATIONS, stationsArray)
                .build();
        
        Map<String, Object> properties = new HashMap<>(1);
        properties.put(JsonGenerator.PRETTY_PRINTING, true);
        JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
        StringWriter sw = new StringWriter();
        JsonWriter jsonWriter = writerFactory.createWriter(sw);
        jsonWriter.writeObject(dataManagerJSO);
        jsonWriter.close();

        // INIT THE WRITER
        OutputStream os = new FileOutputStream(filePath);
        JsonWriter jsonFileWriter = Json.createWriter(os);
        jsonFileWriter.writeObject(dataManagerJSO);
        String prettyPrinted = sw.toString();
        PrintWriter pw = new PrintWriter(filePath);
        pw.write(prettyPrinted);
        pw.close();
    }
    
    private double getDataAsDouble(JsonObject json, String dataName) {
        JsonValue value = json.get(dataName);
        JsonNumber number = (JsonNumber) value;
        return number.bigDecimalValue().doubleValue();
    }
    
    private Color loadColor(JsonObject json, String colorToGet) {
        JsonObject jsonColor = json.getJsonObject(colorToGet);
        double red = getDataAsDouble(jsonColor, JSON_RED);
        double green = getDataAsDouble(jsonColor, JSON_GREEN);
        double blue = getDataAsDouble(jsonColor, JSON_BLUE);
        double alpha = getDataAsDouble(jsonColor, JSON_ALPHA);
        Color loadedColor = new Color(red, green, blue, alpha);
        return loadedColor;
    }

    // HELPER METHOD FOR LOADING DATA FROM A JSON FORMAT
    private JsonObject loadJSONFile(String jsonFilePath) throws IOException {
        JsonObject json;
        try (InputStream is = new FileInputStream(jsonFilePath)) {
            JsonReader jsonReader = Json.createReader(is);
            json = jsonReader.readObject();
            jsonReader.close();
        }
        return json;
    }
    
    private String loadName(JsonObject json, String nameToGet) {
        String jsonName = json.getString(nameToGet);
        return jsonName;
    }
    
    private JsonObject makeJsonColorObject(Color color) {
        JsonObject colorJson = Json.createObjectBuilder()
                .add(JSON_RED, color.getRed())
                .add(JSON_GREEN, color.getGreen())
                .add(JSON_BLUE, color.getBlue())
                .add(JSON_ALPHA, color.getOpacity()).build();
        return colorJson;
    }
}
