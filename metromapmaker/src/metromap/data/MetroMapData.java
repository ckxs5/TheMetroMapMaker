package metromap.data;

import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import metromap.gui.MetroMapWorkspace;
import djf.components.AppDataComponent;
import djf.AppTemplate;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import static javafx.scene.paint.Color.BLACK;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import jtps.jTPS;
import static metromap.data.MetroMapState.*;

public class MetroMapData implements AppDataComponent {
    // FIRST THE THINGS THAT HAVE TO BE SAVED TO FILES

    // THESE ARE THE SHAPES TO DRAW
    ObservableList<Node> metroNodes;

    ObservableList<ArrayList<Node>> metros;

    ObservableList<ArrayList<Node>> metroStations;

    ArrayList<String> lineNameList;
    ArrayList<String> staionNameList;

    // THE BACKGROUND COLOR
    Color backgroundColor;
    Color lineColor;

    //The BACKGROUND IMAGE
    Image backgroundImage;

    // AND NOW THE EDITING DATA
    // THIS IS THE SHAPE CURRENTLY BEING SIZED BUT NOT YET ADDED
    Node newNode;

    // THIS IS THE SHAPE CURRENTLY SELECTED
    Node selectedNode;

    // FOR FILL AND OUTLINE
    Color currentFillColor;
    Color currentOutlineColor;
    double currentBorderWidth;
    double currentStationR;

    // CURRENT STATE OF THE APP
    MetroMapState state;
    jTPS jtps;

    // THIS IS A SHARED REFERENCE TO THE APPLICATION
    AppTemplate app;

    // USE THIS WHEN THE SHAPE IS SELECTED
    Effect highlightedEffect;

    public static final String WHITE_HEX = "#FFFFFF";
    public static final String BLACK_HEX = "#000000";
    public static final String YELLOW_HEX = "#EEEE00";
    public static final Paint DEFAULT_BACKGROUND_COLOR = Paint.valueOf(WHITE_HEX);
    public static final Paint HIGHLIGHTED_COLOR = Paint.valueOf(YELLOW_HEX);
    public static final int HIGHLIGHTED_STROKE_THICKNESS = 3;

    /**
     * THis constructor creates the data manager and sets up the
     *
     *
     * @param initApp The application within which this data manager is serving.
     */
    public MetroMapData(AppTemplate initApp) {
        // KEEP THE APP FOR LATER
        app = initApp;

        // NO SHAPE STARTS OUT AS SELECTED
        newNode = null;
        selectedNode = null;

        // INIT THE COLORS
        currentFillColor = Color.web(WHITE_HEX);
        currentOutlineColor = Color.web(BLACK_HEX);
        backgroundColor = Color.web("#F2F2F2");
        currentBorderWidth = 5;
        currentStationR = 5;

        lineNameList = new ArrayList<String>();
        staionNameList = new ArrayList<String>();

        metros = FXCollections.observableArrayList();
        metroStations = FXCollections.observableArrayList();

        // THIS IS FOR THE SELECTED SHAPE
        DropShadow dropShadowEffect = new DropShadow();
        dropShadowEffect.setOffsetX(0.0f);
        dropShadowEffect.setOffsetY(0.0f);
        dropShadowEffect.setSpread(1.0);
        dropShadowEffect.setColor(Color.YELLOW);
        dropShadowEffect.setBlurType(BlurType.GAUSSIAN);
        dropShadowEffect.setRadius(5);
        highlightedEffect = dropShadowEffect;
    }

    public ObservableList<Node> getMetroNodes() {
        return metroNodes;
    }

    public ObservableList<ArrayList<Node>> getMetros() {
        return metros;
    }

    public ObservableList<ArrayList<Node>> getMetroStations() {
        return metroStations;
    }

    public ArrayList<String> getLineNameList() {
        return lineNameList;
    }

    public ArrayList<String> getStaionNameList() {
        return staionNameList;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public double getCurrentBorderWidth() {
        return currentBorderWidth;
    }

    public Color getCurrentFillColor() {
        return currentFillColor;
    }

    public double getCurrentStationR() {
        return currentStationR;
    }

    public void setNodes(ObservableList<Node> initShapes) {
        metroNodes = initShapes;
    }

    public void setlineColor(Color selectedColor) {
        lineColor = selectedColor;
        if (selectedNode != null) {
            ((Line) selectedNode).setStroke(lineColor);
        }
    }

    public void setBackgroundColor(Color initBackgroundColor) {
        backgroundColor = initBackgroundColor;
        MetroMapWorkspace workspace = (MetroMapWorkspace) app.getWorkspaceComponent();
        Pane canvas = workspace.getCanvas();
        BackgroundFill fill = new BackgroundFill(backgroundColor, null, null);
        Background background = new Background(fill);
        canvas.setBackground(background);
    }

    public void setBackgroundImage(Image bkgroundImage) {
        backgroundImage = bkgroundImage;
        MetroMapWorkspace workspace = (MetroMapWorkspace) app.getWorkspaceComponent();
        Pane canvas = workspace.getCanvas();
        ImageView bkImage = new ImageView(backgroundImage);
        bkImage.fitWidthProperty().bind(canvas.widthProperty());
        bkImage.fitHeightProperty().bind(canvas.heightProperty());
//        canvas.getChildren().add((DraggableImage) bkImage);
//canvas.setBackground(Background.EMPTY);
        ArrayList<Node> temp = new ArrayList<>();
        temp.add(bkImage);
        for (Node node : metroNodes) {
            temp.add(node);
        }
        metroNodes.clear();
        for (Node node : temp) {
            metroNodes.add(node);
        }
    }

    public void setCurrentFillColor(Color initColor) {
        currentFillColor = initColor;
        if (selectedNode != null) {
            ((Shape) selectedNode).setFill(currentFillColor);
        }
    }

    public void setCurrentOutlineColor(Color initColor) {
        currentOutlineColor = initColor;
        if (selectedNode != null) {
            ((Shape) selectedNode).setStroke(initColor);
        }
    }

    public void setCurrentOutlineThickness(double initBorderWidth) {
        currentBorderWidth = initBorderWidth;
        if (selectedNode != null) {
            ((Shape) selectedNode).setStrokeWidth(initBorderWidth);
        }
    }

    public void setCurrentStationR(int StationR) {
        currentStationR = StationR;
        if (selectedNode != null) {
            ((Shape) selectedNode).setStrokeWidth(StationR);
        }
    }

    public void removeSelectedNode() {
        if (selectedNode != null) {
            metroNodes.remove(selectedNode);
            selectedNode = null;
        }
    }

    /**
     * This function clears out the HTML tree and reloads it with the minimal
     * tags, like html, head, and body such that the user can begin editing a
     * page.
     */
    @Override
    public void resetData() {
        setState(SELECTING_NODE);
        newNode = null;
        selectedNode = null;

        // INIT THE COLORS
        currentFillColor = Color.web(BLACK_HEX);
        currentOutlineColor = Color.web(BLACK_HEX);
        backgroundColor = Color.web("#F2F2F2");

        metroNodes.clear();
        lineNameList.clear();
        staionNameList.clear();
        ((MetroMapWorkspace) app.getWorkspaceComponent()).getCanvas().getChildren().clear();
    }

    public void selectSizedShape() {
        if (selectedNode != null) {
            unhighlightShape(selectedNode);
        }
        selectedNode = newNode;
        highlightShape(selectedNode);
        newNode = null;
        if (state == SIZING_SHAPE) {
            state = ((Draggable) selectedNode).getStartingState();
        }
    }

    public void unhighlightShape(Node shape) {
        selectedNode.setEffect(null);
    }

    public void highlightShape(Node shape) {
        shape.setEffect(highlightedEffect);
    }

    public void initNewShape() {
        // DESELECT THE SELECTED SHAPE IF THERE IS ONE
        if (selectedNode != null) {
            unhighlightShape((selectedNode));
            selectedNode = null;
        }

        // USE THE CURRENT SETTINGS FOR THIS NEW SHAPE
        MetroMapWorkspace workspace = (MetroMapWorkspace) app.getWorkspaceComponent();
        ((Shape) newNode).setFill(BLACK);
        ((Shape) newNode).setStroke(BLACK);
        ((Shape) newNode).setStrokeWidth(workspace.getLineThickness().getValue());
        // ADD THE SHAPE TO THE CANVAS
        metroNodes.add(newNode);
        state = MetroMapState.SIZING_SHAPE;
    }

    public Node getNewShape() {
        return newNode;
    }

    public MetroMapState getState() {
        return state;
    }

    public Node getSelectedShape() {
        return selectedNode;
    }

    public void setState(MetroMapState initState) {
        state = initState;
    }

    public void setSelectedShape(Node initSelectedShape) {
        selectedNode = initSelectedShape;
    }

    public Node selectTopShape(int x, int y) {
        Node shape = getTopShape(x, y);

        if (shape == selectedNode) {
            return shape;
        }

        if (selectedNode != null) {
            unhighlightShape(selectedNode);
        }
        if (shape != null) {
            highlightShape(shape);
            MetroMapWorkspace workspace = (MetroMapWorkspace) app.getWorkspaceComponent();
            workspace.loadSelectedShapeSettings(shape);
        }
        selectedNode = shape;
        if (shape != null) {
            ((Draggable) shape).start(x, y);
        }

        return shape;
    }

    public Node getTopShape(int x, int y) {
        for (int i = metroNodes.size() - 1; i >= 0; i--) {
            Node shape = metroNodes.get(i);
            if (shape.contains(x, y)) {
                return shape;
            }
        }
        return null;
    }

    public void moveSelectedShapeToBack() {
        if (selectedNode != null) {
            metroNodes.remove(selectedNode);
            if (metroNodes.isEmpty()) {
                metroNodes.add(selectedNode);
            } else {
                ArrayList<Node> temp = new ArrayList<>();
                temp.add(selectedNode);
                for (Node node : metroNodes) {
                    temp.add(node);
                }
                metroNodes.clear();
                for (Node node : temp) {
                    metroNodes.add(node);
                }
            }
        }
    }

    public void addShape(Node shapeToAdd) {
        metroNodes.add(shapeToAdd);
    }

    public void addMetroLines(ArrayList<Node> lines) {
        metros.add(lines);
    }

    public void addStaions(ArrayList<Node> station) {
        metroStations.add(station);
    }

    public void removeStation(ArrayList<Node> Station) {
        metroStations.remove(Station);
    }

    public void removeShape(Node shapeToRemove) {
        metroNodes.remove(shapeToRemove);
    }

    public boolean isInState(MetroMapState testState) {
        return state == testState;
    }

    public void startNewLine(int x, int y) {
        DraggableLine newLine = new DraggableLine();
        newLine.start(x, y);
        newNode = newLine;
        initNewShape();
    }

    public void startNewCircle(int x, int y) {
        DraggableCircle newCircle = new DraggableCircle();
        newCircle.start(x, y);
        newNode = newCircle;
        initNewShape();
    }

}
