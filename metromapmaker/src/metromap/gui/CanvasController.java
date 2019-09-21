package metromap.gui;

import djf.AppTemplate;
import java.util.ArrayList;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import metromap.data.*;
import metromap.data.MetroMapData;
import metromap.data.MetroMapState;
import static metromap.data.MetroMapState.*;
import jtps.jTPS;
import jtps.jTPS_Transaction;
import metromap.MetroMapApp;

/**
 * This class responds to interactions with the rendering surface.
 *
 * @author Kaixuan Chen
 * @author ?
 * @version 1.0
 */
public class CanvasController {

    AppTemplate app;
    jTPS jtps;

    public CanvasController(AppTemplate initApp, jTPS jtps) {
        app = initApp;
        this.jtps = jtps;
    }

    /**
     * Respond to mouse presses on the rendering surface, which we call canvas,
     * but is actually a Pane.
     *
     * @param x
     * @param y
     */
    public void processCanvasMousePress(int x, int y) {
        MetroMapData data = (MetroMapData) app.getDataComponent();
        MetroMapWorkspace ws = (MetroMapWorkspace) app.getWorkspaceComponent();
        if (data.isInState(MetroMapState.SELECTING_NODE)) {
            // SELECT THE TOP SHAPE
            Node shape = data.selectTopShape(x, y);
            Scene scene = app.getGUI().getPrimaryScene();

            // AND START DRAGGING IT
            if (shape != null) {
                scene.setCursor(Cursor.MOVE);
                data.setState(MetroMapState.DRAGGING_NODE);
                app.getGUI().updateToolbarControls(false);
            } else {
                scene.setCursor(Cursor.DEFAULT);
                data.setState(DRAGGING_NOTHING);
                app.getWorkspaceComponent().reloadWorkspace(data);
            }
        }
        if (data.isInState(MetroMapState.ADD_STATION_TO_LINE)) {
            Node shape = data.selectTopShape(x, y);
            Scene scene = app.getGUI().getPrimaryScene();

            if (shape != null && shape instanceof DraggableCircle) {
                int k = ws.getSelectedLine();
                ArrayList<Node> theLine = data.getMetros().get(k);

                DraggableText endName = (DraggableText) theLine.get(theLine.size() - 1);
                DraggableCircle station = (DraggableCircle) shape;
                DraggableLine newLine = new DraggableLine();
                newLine.setStroke(((DraggableLine) theLine.get(1)).getStroke());

                jTPS_Transaction transaction = new Transaction_addStationToLine((MetroMapApp) app, endName, station, newLine, theLine, k);
                jtps.addTransaction(transaction);
//                for (int i = 0; i < theLine.size(); i++) {
//                    if (theLine.get(i) instanceof DraggableLine) {
//                        ((DraggableLine) theLine.get(i)).startXProperty().unbindBidirectional(((Draggable) theLine.get(i - 1)).getXProperty());
//                        ((DraggableLine) theLine.get(i)).startYProperty().unbindBidirectional(((Draggable) theLine.get(i - 1)).getYProperty());
//
//                        ((DraggableLine) theLine.get(i)).endXProperty().unbindBidirectional(((Draggable) theLine.get(i + 1)).getXProperty());
//                        ((DraggableLine) theLine.get(i)).endYProperty().unbindBidirectional(((Draggable) theLine.get(i + 1)).getYProperty());
//                    }
//                }
//                theLine.add(station);
//                theLine.add(newLine);
//                theLine.remove(endName);
//                theLine.add(endName);
//                data.addMetroLines(theLine);
//
//                data.addShape(newLine);
//                for (int j = 0; j < theLine.size(); j++) {
//                    if (theLine.get(j) instanceof DraggableLine) {
//                        ((DraggableLine) theLine.get(j)).startXProperty().bindBidirectional(((Draggable) theLine.get(j - 1)).getXProperty());
//                        ((DraggableLine) theLine.get(j)).startYProperty().bindBidirectional(((Draggable) theLine.get(j - 1)).getYProperty());
//
//                        ((DraggableLine) theLine.get(j)).endXProperty().bindBidirectional(((Draggable) theLine.get(j + 1)).getXProperty());
//                        ((DraggableLine) theLine.get(j)).endYProperty().bindBidirectional(((Draggable) theLine.get(j + 1)).getYProperty());
//                    }
//                }
            } else {
                scene.setCursor(Cursor.DEFAULT);
                data.setState(DRAGGING_NOTHING);
                app.getWorkspaceComponent().reloadWorkspace(data);
            }
        }
//==============================================================================
        if (data.isInState(MetroMapState.REMOVE_STATION_FROM_LINE)) {
            Node shape = data.selectTopShape(x, y);
            Scene scene = app.getGUI().getPrimaryScene();
            if (shape != null && shape instanceof DraggableCircle) {
                DraggableCircle station = (DraggableCircle) shape;
                int k = ws.getSelectedLine();
                ArrayList<Node> theLine = data.getMetros().get(k);

                jTPS_Transaction transaction = new Transaction_removeStationFromLine((MetroMapApp) app, station, theLine, k);
                jtps.addTransaction(transaction);
//                for (int i = 0; i < theLine.size(); i++) {
//                    if (theLine.get(i) instanceof DraggableCircle && theLine.get(i).equals(station)) {
//                        ((DraggableCircle) theLine.get(i)).getXProperty().unbindBidirectional(((DraggableLine) theLine.get(i - 1)).endXProperty());
//                        ((DraggableCircle) theLine.get(i)).getYProperty().unbindBidirectional(((DraggableLine) theLine.get(i - 1)).endYProperty());
//
//                        ((DraggableCircle) theLine.get(i)).getXProperty().unbindBidirectional(((DraggableLine) theLine.get(i + 1)).startXProperty());
//                        ((DraggableCircle) theLine.get(i)).getYProperty().unbindBidirectional(((DraggableLine) theLine.get(i + 1)).startYProperty());
//
//                        ((DraggableLine) theLine.get(i + 1)).endXProperty().unbindBidirectional(((Draggable) theLine.get(i + 2)).getXProperty());
//                        ((DraggableLine) theLine.get(i + 1)).endYProperty().unbindBidirectional(((Draggable) theLine.get(i + 2)).getYProperty());
//
//                        ((DraggableLine) theLine.get(i - 1)).endXProperty().bindBidirectional(((Draggable) theLine.get(i + 2)).getXProperty());
//                        ((DraggableLine) theLine.get(i - 1)).endYProperty().bindBidirectional(((Draggable) theLine.get(i + 2)).getYProperty());
//
//                        for (int j = 0; j < data.getMetroNodes().size(); j++) {
//                            if (theLine.get(i + 1).equals(data.getMetroNodes().get(j))) {
//                                data.removeShape(data.getMetroNodes().get(j));
//                            }
//                        }
//                        theLine.remove(theLine.get(i + 1));
//                        theLine.remove(theLine.get(i));
//
//                    }
//                }
            } else {
                scene.setCursor(Cursor.DEFAULT);
                data.setState(DRAGGING_NOTHING);
                app.getWorkspaceComponent().reloadWorkspace(data);
            }
        }
        MetroMapWorkspace workspace = (MetroMapWorkspace) app.getWorkspaceComponent();
        workspace.reloadWorkspace(data);
    }

    /**
     * Respond to mouse dragging on the rendering surface, which we call canvas,
     * but is actually a Pane.
     *
     * @param x
     * @param y
     */
    public void processCanvasMouseDragged(int x, int y) {
        MetroMapData data = (MetroMapData) app.getDataComponent();
        MetroMapWorkspace ws = (MetroMapWorkspace) app.getWorkspaceComponent();
        if (data.isInState(SIZING_SHAPE)) {
            Draggable newDraggableShape = (Draggable) data.getNewShape();
            newDraggableShape.size(x, y);
        } else if (data.isInState(DRAGGING_NODE)) {
            Draggable selectedDraggableShape = (Draggable) data.getSelectedShape();

//            int k = ws.getSelectedLine();
//            ArrayList theLine = data.getMetros().get(k);
//            
//            for (int i = 0; i < theLine.size(); i++) {
//                if(i == 0 && selectedDraggableShape == theLine.get(i)){
//                    
//                }
//            }
            selectedDraggableShape.drag(x, y);
            app.getGUI().updateToolbarControls(false);
        }

    }

    /**
     * Respond to mouse button release on the rendering surface, which we call
     * canvas, but is actually a Pane.
     *
     * @param x
     * @param y
     */
    public void processCanvasMouseRelease(int x, int y) {
        MetroMapData data = (MetroMapData) app.getDataComponent();
        if (data.isInState(SIZING_SHAPE)) {
            data.selectSizedShape();
            app.getGUI().updateToolbarControls(false);
        } else if (data.isInState(MetroMapState.DRAGGING_NODE)) {
            data.setState(SELECTING_NODE);
            Scene scene = app.getGUI().getPrimaryScene();
            scene.setCursor(Cursor.DEFAULT);
            app.getGUI().updateToolbarControls(false);
        } else if (data.isInState(MetroMapState.DRAGGING_NOTHING)) {
            data.setState(SELECTING_NODE);
        }
        MetroMapWorkspace workspace = (MetroMapWorkspace) app.getWorkspaceComponent();
        workspace.reloadWorkspace(data);
    }
}
