/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nac.tbot;

import java.awt.Rectangle;

/**
 *
 * @author nathaniel
 */
public class BoardWatcher {

    private Rectangle bounds;
    private int rows = 20;
    private int columns = 10;

    public BoardWatcher(Rectangle bounds) {
        this.bounds = bounds;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }
}
