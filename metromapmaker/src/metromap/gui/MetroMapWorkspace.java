package metromap.gui;

import java.io.IOException;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import djf.ui.AppYesNoCancelDialogSingleton;
import djf.ui.AppMessageDialogSingleton;
import djf.ui.AppGUI;
import djf.AppTemplate;
import djf.components.AppDataComponent;
import djf.components.AppWorkspaceComponent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import jtps.jTPS;
import static metromap.css.MetroMapStyle.*;
import static metromap.MetroMapLanguageProperty.*;
import metromap.data.*;
import metromap.data.MetroMapData;
import properties_manager.PropertiesManager;

public final class MetroMapWorkspace extends AppWorkspaceComponent {

    // HERE'S THE APP
    AppTemplate app;

    // IT KNOWS THE GUI IT IS PLACED INSIDE
    AppGUI gui;
    jTPS jtps;

    Stage welcomeStage;
    BorderPane welcomePane;
    VBox recentWorkBox;
    VBox createNewMap;
    Scene welcomeScene;
    Label recentWork;

    Button recentWork1;
    Button recentWork2;
    Button recentWork3;
    Button recentWork4;
    Button recentWork5;
    Button recentWork6;

    Button createNewMetroMap;

    // HAS ALL THE CONTROLS FOR EDITING
    VBox editToolbar;

    // FIRST ROW
    FlowPane MetroLinesToolbar;
    Label metroLines;
    ComboBox<String> lineName;
    Button EditLine;
    Button addLine;
    Button removeline;
    Button addStationToLine;
    Button removeStationFromLine;
    Button listStation;
    Slider lineThickness;

    // SECOND ROW
    FlowPane MetroStationsToolbar;
    Label metroStation;
    ComboBox<String> stationName;
    ColorPicker stationColor;
    Button addSingleStation;
    Button removeSingleStation;
    Button snapStation;
    Button moveLable;
    Button rotateStationLable;
    Slider stationCircleR;

    // THIRD ROW
    FlowPane StationRouterToolbar;
    VBox StartEnd;
    ComboBox<String> stationStart;
    ComboBox<String> stationEnd;
    Button findRoute;

    // FORTH ROW
    FlowPane DecorToolbar;
    Label Decor;
    ColorPicker backgroundColor;
    Button setImageBackground;
    Button addImage;
    Button addLabel;
    Button removeElement;

    // FIFTH ROW
    FlowPane FontToolbar;
    Label Font;
    Button boldButton;
    Button italicsButton;
    ComboBox<String> fontfamily;
    ComboBox<String> fontsize;
    ColorPicker textColor;

    // SIXTH ROW
    FlowPane NavigationToolbar;
    Label navigation;
    Button zoomIn;

    Button zoomOut;
    Button increaseMapSize;
    Button decreaseMapSize;
    CheckBox showGrid;
    BorderPane mainPane;

    // THIS IS WHERE WE'LL RENDER OUR DRAWING, NOTE THAT WE
    // CALL THIS A CANVAS, BUT IT'S REALLY JUST A Pane
    Pane canvas;

    // HERE ARE THE CONTROLLERS
    CanvasController canvasController;
    MapEditController MapEditController;

    // HERE ARE OUR DIALOGS
    AppMessageDialogSingleton messageDialog;
    AppYesNoCancelDialogSingleton yesNoCancelDialog;

    // FOR DISPLAYING DEBUG STUFF
    Text debugText;
    int counter = 0;
    int selectedLine;
    int selectedStation;
    String newMetroMap;

    public String getNewMetroMap() {
        return newMetroMap;
    }

    public void setNewMetroMap(String newMetroMap) {
        this.newMetroMap = newMetroMap;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public void setSelectedLine(int selectedLine) {
        this.selectedLine = selectedLine;
    }

    public int getSelectedLine() {
        return selectedLine;
    }

    public int getSelectedStation() {
        return selectedStation;
    }

    public void setSelectedStation(int selectedStation) {
        this.selectedStation = selectedStation;
    }

    /**
     * Constructor for initializing the workspace, note that this constructor
     * will fully setup the workspace user interface for use.
     *
     * @param initApp The application this workspace is part of.
     *
     * @throws IOException Thrown should there be an error loading application
     * data for setting up the user interface.
     */
    public MetroMapWorkspace(AppTemplate initApp) {
        // KEEP THIS FOR LATER
        app = initApp;

        // KEEP THE GUI FOR LATER
        gui = app.getGUI();
        jtps = new jTPS();

        MapEditController = new MapEditController(app, this, jtps);

        //WELCOMESTAGE
        WelcomeStage();

        // LAYOUT THE APP
        initLayout();

        // HOOK UP THE CONTROLLERS
        initControllers();

        // AND INIT THE STYLE FOR THE WORKSPACE
        initStyle();

        //LOAD THE WORKSPACE EDITOR PANE
        initMainUI();
    }

    //WELCOME PAGE
    public void WelcomeStage() {
        //CREATE NEW WELCOME STAGE AND PUT ALL THE BOXES INSIDE THE PAGE.
        welcomeStage = new Stage();
        welcomeStage.setTitle("Welcome to Metro Map Maker");
        welcomePane = new BorderPane();
        recentWorkBox = new VBox();
        createNewMap = new VBox();
        recentWork = new Label("Recent Work");
        recentWork.setStyle("-fx-font-weight:bold; -fx-font-size:25pt; -fx-padding: 20");

        createNewMap.setAlignment(Pos.CENTER);
        createNewMap.setSpacing(100);
        recentWorkBox.setAlignment(Pos.CENTER);
        recentWorkBox.setSpacing(30);

        recentWork1 = new Button("New York City");
        recentWork2 = new Button("   Boston    ");
        recentWork3 = new Button("    Tokyo    ");
        recentWork4 = new Button("   Beijing   ");
        recentWork5 = new Button("   Shanghai  ");
        recentWork6 = new Button(" San Fransico");
        recentWork1.setStyle("-fx-font-weight:bold; -fx-font-size:15pt;-fx-border-radius: 15 15 15 15;-fx-background-radius: 15 15 15 15;-fx-background-color: #ffffb7;-fx-underline:true;");
        recentWork2.setStyle("-fx-font-weight:bold; -fx-font-size:15pt;-fx-border-radius: 15 15 15 15;-fx-background-radius: 15 15 15 15;-fx-background-color: #ffffb7;-fx-underline:true;");
        recentWork3.setStyle("-fx-font-weight:bold; -fx-font-size:15pt;-fx-border-radius: 15 15 15 15;-fx-background-radius: 15 15 15 15;-fx-background-color: #ffffb7;-fx-underline:true;");
        recentWork4.setStyle("-fx-font-weight:bold; -fx-font-size:15pt;-fx-border-radius: 15 15 15 15;-fx-background-radius: 15 15 15 15;-fx-background-color: #ffffb7;-fx-underline:true;");
        recentWork5.setStyle("-fx-font-weight:bold; -fx-font-size:15pt;-fx-border-radius: 15 15 15 15;-fx-background-radius: 15 15 15 15;-fx-background-color: #ffffb7;-fx-underline:true;");
        recentWork6.setStyle("-fx-font-weight:bold; -fx-font-size:15pt;-fx-border-radius: 15 15 15 15;-fx-background-radius: 15 15 15 15;-fx-background-color: #ffffb7;-fx-underline:true;");
        recentWorkBox.getChildren().addAll(recentWork, recentWork1, recentWork2, recentWork3, recentWork4, recentWork5, recentWork6);

        Image WelcomeImage = new Image("file:./images/MetroMapMaker.png");
        ImageView welcomeImageView = new ImageView(WelcomeImage);
        welcomeImageView.setFitHeight(232);
        welcomeImageView.setFitWidth(700);

        createNewMetroMap = new Button("Create New Metro Map");
        createNewMetroMap.setStyle("-fx-font-weight:bold; -fx-font-size:15pt;-fx-border-radius: 15 15 15 15;-fx-background-radius: 15 15 15 15;-fx-background-color: #f4f4f4;-fx-underline:true;");
        createNewMap.getChildren().addAll(welcomeImageView, createNewMetroMap);

        recentWorkBox.setStyle("-fx-background-color:#ffffb7;-fx-border-color: #7777dd;-fx-border-width: 2px;");

        welcomePane.setLeft(recentWorkBox);
        welcomePane.setCenter(createNewMap);

        welcomeScene = new Scene(welcomePane, 1024, 768);
        welcomeStage.setScene(welcomeScene);

        //BUTTON ACTIONS FOR CREATE NEW METRO MAP AND RECENT WORK
        createNewMetroMap.setOnAction(e -> {
            MapEditController.processCreateNewMetroMap();
        });

        recentWork1.setOnAction(e -> {
            MapEditController.processRecentWork();
        });
        recentWork2.setOnAction(e -> {
            MapEditController.processRecentWork();
        });
        recentWork3.setOnAction(e -> {
            MapEditController.processRecentWork();
        });
        recentWork4.setOnAction(e -> {
            MapEditController.processRecentWork();
        });
        recentWork5.setOnAction(e -> {
            MapEditController.processRecentWork();
        });
        recentWork6.setOnAction(e -> {
            MapEditController.processRecentWork();
        });

        welcomeStage.showAndWait();
    }

    /**
     * Note that this is for displaying text during development.
     */
    public void setDebugText(String text) {
        debugText.setText(text);
    }

    // ACCESSOR METHODS FOR COMPONENTS THAT EVENT HANDLERS
    // MAY NEED TO UPDATE OR ACCESS DATA FROM
    public ComboBox<String> getLineName() {
        return lineName;
    }

    public ComboBox<String> getStationName() {
        return stationName;
    }

    public void setStationName(ComboBox<String> stationName) {
        this.stationName = stationName;
    }

    public ColorPicker getStationColor() {
        return stationColor;
    }

    public Slider getStationCircleR() {
        return stationCircleR;
    }

    public Button getMoveLable() {
        return moveLable;
    }

    public Button getRotateStationLable() {
        return rotateStationLable;
    }

    public ComboBox<String> getStationStart() {
        return stationStart;
    }

    public ComboBox<String> getStationEnd() {
        return stationEnd;
    }

    public ColorPicker getBackgroundColor() {
        return backgroundColor;
    }

    public Button getSetImageBackground() {
        return setImageBackground;
    }

    public ComboBox<String> getFontfamily() {
        return fontfamily;
    }

    public ComboBox<String> getFontsize() {
        return fontsize;
    }

    public ColorPicker getTextColor() {
        return textColor;
    }

    public Slider getLineThickness() {
        return lineThickness;
    }

    public Button getIncreaseMapSize() {
        return increaseMapSize;
    }

    public Button getDecreaseMapSize() {
        return decreaseMapSize;
    }

    public Button getZoomOut() {
        return zoomOut;
    }

    public Button getZoomIn() {
        return zoomIn;
    }

    public Pane getCanvas() {
        return canvas;
    }

    public CheckBox getShowGrid() {
        return showGrid;
    }

    public void initDebugText() {
        canvas.getChildren().add(debugText);
    }

    // HELPER SETUP METHOD
    private void initLayout() {
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        // THIS WILL GO IN THE LEFT SIDE OF THE WORKSPACE
        editToolbar = new VBox();
        editToolbar.setPrefWidth(330);

        // METROLINESTOOLBAR
        MetroLinesToolbar = new FlowPane();
        MetroLinesToolbar.setHgap(10);
        MetroLinesToolbar.setVgap(10);
        metroLines = new Label("Metro Lines      ");
        lineName = new ComboBox<String>();
//        lineName.setMaxWidth(120);
        lineName.setPromptText("Line Name");
        lineName.getItems().add("Line Name");
        lineThickness = new Slider(3, 15, 3);
        MetroLinesToolbar.getChildren().addAll(metroLines, lineName);
        EditLine = new Button();
        EditLine = gui.initChildButton(MetroLinesToolbar, LINECOLOR_ICON.toString(), LINECOLOR_TOOLTIP.toString(), false);
        addLine = gui.initChildButton(MetroLinesToolbar, ADDLINE_ICON.toString(), ADDLINE_TOOLTIP.toString(), false);
        removeline = gui.initChildButton(MetroLinesToolbar, REMOVELINE_ICON.toString(), REMOVELINE_TOOLTIP.toString(), false);
        addStationToLine = gui.initChildButton(MetroLinesToolbar, ADDLINESTATION_ICON.toString(), ADDLINESTATION_TOOLTIP.toString(), false);
        removeStationFromLine = gui.initChildButton(MetroLinesToolbar, REMOVELINESTATION_ICON.toString(), REMOVELINESTATION_TOOLTIP.toString(), false);
        listStation = gui.initChildButton(MetroLinesToolbar, LISTSTATION_ICON.toString(), LISTSTATION_TOOLTIP.toString(), false);
        MetroLinesToolbar.getChildren().add(lineThickness);

        //METROSTATIONTOOLBAR
        MetroStationsToolbar = new FlowPane();
        MetroStationsToolbar.setHgap(10);
        MetroStationsToolbar.setVgap(10);
        metroStation = new Label("Metro Stations");
        stationName = new ComboBox<String>();
        stationName.setMaxWidth(100);
        stationName.setPromptText("Station Name");
        stationName.getItems().add("Station Name");
        stationColor = new ColorPicker();
        stationCircleR = new Slider(10, 20, 10);
        MetroStationsToolbar.getChildren().addAll(metroStation, stationName, stationColor);

        addSingleStation = gui.initChildButton(MetroStationsToolbar, ADDSINGLESTATION_ICON.toString(), ADDSINGLESTATION_TOOLTIP.toString(), false);
        removeSingleStation = gui.initChildButton(MetroStationsToolbar, REMOVESINGLESTATION_ICON.toString(), REMOVESINGLESTATION_TOOLTIP.toString(), false);
        snapStation = gui.initChildButton(MetroStationsToolbar, SNAPSTATION_ICON.toString(), SNAPSTATION_TOOLTIP.toString(), false);
        moveLable = gui.initChildButton(MetroStationsToolbar, MOVELABEL_ICON.toString(), MOVELABEL_TOOLTIP.toString(), false);
        rotateStationLable = gui.initChildButton(MetroStationsToolbar, ROTATELABEL_ICON.toString(), ROTATELABEL_TOOLTIP.toString(), false);
        MetroStationsToolbar.getChildren().add(stationCircleR);

        // STATIONROUTERTOOLBAR
        StationRouterToolbar = new FlowPane();
        StationRouterToolbar.setHgap(100);
        StartEnd = new VBox();
        StartEnd.setSpacing(20);
        stationStart = new ComboBox<String>();
        stationStart.setMaxWidth(120);
        stationStart.setPromptText("From Station");
        stationEnd = new ComboBox<String>();
        stationEnd.setMaxWidth(120);
        stationEnd.setPromptText("  To   Station");
        StartEnd.getChildren().addAll(stationStart, stationEnd);

        StationRouterToolbar.getChildren().add(StartEnd);
        findRoute = gui.initChildButton(StationRouterToolbar, FINDROUTE_ICON.toString(), FINDROUTE_TOOLTIP.toString(), false);

        // DECORTOOLBAR
        DecorToolbar = new FlowPane();
        DecorToolbar.setHgap(10);
        DecorToolbar.setVgap(10);
        Decor = new Label("Decor                                                       ");
        backgroundColor = new ColorPicker();
        setImageBackground = new Button();
        addImage = new Button();
        addLabel = new Button();
        removeElement = new Button();

        DecorToolbar.getChildren().add(Decor);
        DecorToolbar.getChildren().add(backgroundColor);
        setImageBackground = gui.initChildButton(DecorToolbar, BKGROUNDIMAGE_ICON.toString(), BKGROUNDIMAGE_TOOLTIP.toString(), false);
        addImage = gui.initChildButton(DecorToolbar, ADDIMAGE_ICON.toString(), ADDIMAGE_TOOLTIP.toString(), false);
        addLabel = gui.initChildButton(DecorToolbar, ADDLABEL_ICON.toString(), ADDLABEL_TOOLTIP.toString(), false);
        removeElement = gui.initChildButton(DecorToolbar, REMOVEELEMENT_ICON.toString(), REMOVEELEMENT_TOOLTIP.toString(), false);

        // FONTTOOLBAR
        FontToolbar = new FlowPane();
        FontToolbar.setVgap(10);
        FontToolbar.setHgap(3);
        Font = new Label("Font                                                          ");
        textColor = new ColorPicker();
        boldButton = new Button();
        italicsButton = new Button();

        fontfamily = new ComboBox<String>();
        fontfamily.setPromptText("Font Family");
        fontfamily.getItems().addAll("Times", "Times New Roman", "Georgia", "Verdana", "Helvetica");
        fontfamily.setMaxWidth(100);
        fontsize = new ComboBox<String>();
        fontsize.setPromptText("Font Size");
        fontsize.getItems().addAll("7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26");

        FontToolbar.getChildren().addAll(Font, textColor, fontfamily, fontsize);
        boldButton = gui.initChildButton(FontToolbar, BOLD_ICON.toString(), BOLD_TOOLTIP.toString(), false);
        italicsButton = gui.initChildButton(FontToolbar, ITALICS_ICON.toString(), ITALICS_TOOLTIP.toString(), false);

        // NAVIGATIONTOOLBAR
        NavigationToolbar = new FlowPane();
        NavigationToolbar.setHgap(10);
        NavigationToolbar.setVgap(10);
        navigation = new Label("Navigation                                      ");
        zoomIn = new Button();
        zoomOut = new Button();
        increaseMapSize = new Button();
        decreaseMapSize = new Button();
        showGrid = new CheckBox("Show Grid");

        NavigationToolbar.getChildren().addAll(navigation, showGrid);
        zoomIn = gui.initChildButton(NavigationToolbar, ZOOMIN_ICON.toString(), ZOOMIN_TOOLTIP.toString(), false);
        zoomOut = gui.initChildButton(NavigationToolbar, ZOOMOUT_ICON.toString(), ZOOMOUT_TOOLTIP.toString(), false);
        increaseMapSize = gui.initChildButton(NavigationToolbar, INCREASE_ICON.toString(), INCREASE_TOOLTIP.toString(), false);
        decreaseMapSize = gui.initChildButton(NavigationToolbar, DECREASE_ICON.toString(), DECREASE__TOOLTIP.toString(), false);

        editToolbar.getChildren().add(MetroLinesToolbar);
        editToolbar.getChildren().add(MetroStationsToolbar);
        editToolbar.getChildren().add(StationRouterToolbar);
        editToolbar.getChildren().add(DecorToolbar);
        editToolbar.getChildren().add(FontToolbar);
        editToolbar.getChildren().add(NavigationToolbar);

        // WE'LL RENDER OUR STUFF HERE IN THE CANVAS
        canvas = new Pane();
        debugText = new Text();
        canvas.getChildren().add(debugText);
        debugText.setX(100);
        debugText.setY(100);

// AND MAKE SURE THE DATA MANAGER IS IN SYNCH WITH THE PANE
        MetroMapData data = (MetroMapData) app.getDataComponent();
        data.setNodes(canvas.getChildren());
        data.getMetroNodes().clear();

        // AND NOW SETUP THE WORKSPACE
        workspace = new BorderPane();
        ((BorderPane) workspace).setCenter(canvas);
        ((BorderPane) workspace).setTop(app.getGUI().getTopToolbarPane());
        ((BorderPane) workspace).setLeft(editToolbar);

    }

    // HELPER SETUP METHOD
    private void initControllers() {
        // MAKE THE EDIT CONTROLLER
        app.getGUI().getRedoButton().setOnAction(e -> {
            MapEditController.ProcessRedo();
        });
        app.getGUI().getUndoButton().setOnAction(e -> {
            MapEditController.ProcessUndo();
        });
        app.getGUI().getAboutButton().setOnAction(e -> {
            MapEditController.aboutButton();
        });

        EditLine.setOnAction(e -> {
            MapEditController.ProcessEditLine();
        });

        lineName.setOnAction(e -> {
            MapEditController.ProcessSelectLine();
        });

        addLine.setOnAction(e -> {
            MapEditController.ProcessAddLine();
        });

        removeline.setOnAction(e -> {
            MapEditController.ProcessRemoveLine();
        });

        addStationToLine.setOnAction(e -> {
            MapEditController.ProcessAddStationToLine();
        });

        removeStationFromLine.setOnAction(e -> {
            MapEditController.ProcessRemoveStationFromLine();
        });

        listStation.setOnAction(e -> {
            MapEditController.ProcessListStations();
        });
        stationColor.setOnAction(e -> {
            MapEditController.ProcessStationColor();
        });
        stationName.setOnAction(e -> {
            MapEditController.ProcessSelectStation();
        });
        addSingleStation.setOnAction(e -> {
            MapEditController.ProcessAddSingleStation();
        });

        removeSingleStation.setOnAction(e -> {
            MapEditController.ProcessRemoveSingleStation();
        });
        snapStation.setOnAction(e -> {
            MapEditController.ProcessSnapToGrid();
        });
        moveLable.setOnAction(e -> {
            MapEditController.ProcessMoveLabel();
        });

        rotateStationLable.setOnAction(e -> {
            MapEditController.ProcessRotateLabel();
        });

        lineThickness.valueProperty().addListener(e -> {
            MapEditController.ProcessLineThickness();
        });

        stationCircleR.valueProperty().addListener(e -> {
            MapEditController.ProcessStationCircleR();
        });
        findRoute.setOnAction(e -> {
            MapEditController.processFindRoute();
        });
        backgroundColor.setOnAction(e -> {
            MapEditController.ProcessBackgroundColor();
        });

        setImageBackground.setOnAction(e -> {
            try {
                MapEditController.ProcessSetImageBackground();
            } catch (IOException ex) {
                Logger.getLogger(MetroMapWorkspace.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        addImage.setOnAction(e -> {
            MapEditController.processSelectImageToDraw();
        });
        addLabel.setOnAction(e -> {
            MapEditController.processSelectTextToDraw();
        });

        removeElement.setOnAction(e -> {
            MapEditController.ProcessRemoveElement();
        });
        textColor.setOnAction(e -> {
            MapEditController.ProcessTextColor();
        });
        fontfamily.setOnAction(e -> {
            MapEditController.setFontfamily();
        });
        fontsize.setOnAction(e -> {
            MapEditController.setFontSize();
        });
        boldButton.setOnAction(e -> {
            MapEditController.processBold();
        });
        italicsButton.setOnAction(e -> {
            MapEditController.processItalics();
        });

        zoomIn.setOnAction(e -> {
            MapEditController.ProcessZoomIn();
        });

        zoomOut.setOnAction(e -> {
            MapEditController.ProcessZoomOut();
        });

        increaseMapSize.setOnAction(e -> {
            MapEditController.ProcessIncreaseMapSize();
        });

        decreaseMapSize.setOnAction(e -> {
            MapEditController.ProcessDecreaseMapSize();
        });

        showGrid.setOnAction(e -> {
            MapEditController.ProcessShowGrid();
        });

        app.getGUI().getPrimaryScene().setOnKeyPressed((KeyEvent e) -> {
            if (null != e.getCode()) {
                switch (e.getCode()) {
                    case D:
                        canvas.setTranslateX(canvas.getTranslateX() + 10);
                        break;
                    case A:
                        canvas.setTranslateX(canvas.getTranslateX() - 10);
                        break;
                    case W:
                        canvas.setTranslateY(canvas.getTranslateY() - 10);
                        break;
                    case S:
                        canvas.setTranslateY(canvas.getTranslateY() + 10);
                        break;
                    default:
                        break;
                }
            }
        });
//==================================CanvasController=================================
        canvasController = new CanvasController(app, jtps);
        canvas.setOnMousePressed(e -> {
            canvasController.processCanvasMousePress((int) e.getX(), (int) e.getY());
        });
        canvas.setOnMouseReleased(e -> {
            canvasController.processCanvasMouseRelease((int) e.getX(), (int) e.getY());
        });
        canvas.setOnMouseDragged(e -> {
            canvasController.processCanvasMouseDragged((int) e.getX(), (int) e.getY());
        });
    }

    // NOW CONNECT THE BUTTONS TO THEIR HANDLERS
    // HELPER METHOD
    public void loadSelectedShapeSettings(Node shape) {
        if (shape != null) {
            if (shape instanceof DraggableLine || shape instanceof DraggableCircle) {
                Color fillColor = (Color) (((Shape) shape).getFill());
                Color strokeColor = (Color) (((Shape) shape).getStroke());
                double lineThick = ((Shape) shape).getStrokeWidth();
                lineThickness.setValue(lineThick);
            }
        }
    }

    /**
     * This function specifies the CSS style classes for all the UI components
     * known at the time the workspace is initially constructed. Note that the
     * tag editor controls are added and removed dynamicaly as the application
     * runs so they will have their style setup separately.
     */
    public void initStyle() {
//        canvas.getStyleClass().add(CLASS_BORDER_PANE);

        // COLOR PICKER STYLE
//        EditLine.getStyleClass().add(CLASS_EDIT_TOOLBAR);
        stationColor.getStyleClass().add(CLASS_COLOR_BUTTON);
        backgroundColor.getStyleClass().add(CLASS_COLOR_BUTTON);
        textColor.getStyleClass().add(CLASS_COLOR_BUTTON);

        editToolbar.getStyleClass().add(CLASS_EDIT_TOOLBAR);
        MetroLinesToolbar.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
        MetroStationsToolbar.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
        StationRouterToolbar.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
        DecorToolbar.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
        FontToolbar.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
        NavigationToolbar.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
    }

    /**
     * This function reloads all the controls for editing logos the workspace.
     */
    @Override
    public void reloadWorkspace(AppDataComponent data) {
        MetroMapData dataManager = (MetroMapData) data;
//        if (dataManager.isInState(MetroMapState.STARTING_LINE)) {
//           
//        } else if (dataManager.isInState(MetroMapState.STARTING_CIRCLE)) {
//        } else if (dataManager.isInState(MetroMapState.SELECTING_NODE)
//                || dataManager.isInState(MetroMapState.DRAGGING_NODE)
//                || dataManager.isInState(MetroMapState.DRAGGING_NOTHING)) {
//            boolean shapeIsNotSelected = dataManager.getSelectedShape() == null;
//            selectionToolButton.setDisable(true);
//            removeButton.setDisable(shapeIsNotSelected);
//            rectButton.setDisable(false);
//            ellipseButton.setDisable(false);
//        }

//        removeButton.setDisable(dataManager.getSelectedShape() == null);
        backgroundColor.setValue(dataManager.getBackgroundColor());
    }

    @Override
    public void resetWorkspace() {
        // WE ARE NOT USING THIS, THOUGH YOU MAY IF YOU LIKE
    }

    //INITIAL THE MAIN UI
    private void initMainUI() {
        if (counter == 1) {
            MapEditController.handleLoadMainUI();
        }
    }

}
