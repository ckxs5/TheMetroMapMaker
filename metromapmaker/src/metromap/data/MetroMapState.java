package metromap.data;

/**
 * This enum has the various possible states of the logo maker app during the
 * editing process which helps us determine which controls are usable or not and
 * what specific user actions should affect.
 *
 * @author Kaixuan Chen
 * @author ?
 * @version 1.0
 */
public enum MetroMapState {
    SELECTING_NODE,
    DRAGGING_NODE,
    
    STARTING_LINE,
    STARTING_CIRCLE,
    
    SIZING_SHAPE,
    
    DRAGGING_NOTHING,
    SIZING_NOTHING,
    
    ADD_STATION_TO_LINE,
    REMOVE_STATION_FROM_LINE,
    ADD_TO_LINE,
}
