/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metromap.gui;

import jtps.jTPS_Transaction;
import metromap.MetroMapApp;

/**
 *
 * @author KaixuanChen
 */
public class Transaction_increaseSize implements jTPS_Transaction {

    MetroMapApp app;

    public Transaction_increaseSize(MetroMapApp app) {
        this.app = app;
    }

    @Override
    public void doTransaction() {
        MetroMapWorkspace ws = (MetroMapWorkspace) app.getWorkspaceComponent();
        ws.getCanvas().setMaxWidth(ws.getCanvas().getBoundsInParent().getWidth() * 1.1);
        ws.getCanvas().setMaxHeight(ws.getCanvas().getBoundsInParent().getHeight() * 1.1);
    }

    @Override
    public void undoTransaction() {
        MetroMapWorkspace ws = (MetroMapWorkspace) app.getWorkspaceComponent();
        ws.getCanvas().setMaxWidth(ws.getCanvas().getBoundsInParent().getWidth() / 1.1);
        ws.getCanvas().setMaxHeight(ws.getCanvas().getBoundsInParent().getHeight() / 1.1);
    }

}
