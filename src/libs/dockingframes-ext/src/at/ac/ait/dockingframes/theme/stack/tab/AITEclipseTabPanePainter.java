/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.ait.dockingframes.theme.stack.tab;

import bibliothek.extension.gui.dock.theme.eclipse.stack.EclipseTab;
import bibliothek.extension.gui.dock.theme.eclipse.stack.EclipseTabPane;
import bibliothek.extension.gui.dock.theme.eclipse.stack.tab.TabPanePainter;
import bibliothek.gui.DockController;
import bibliothek.gui.Dockable;
import bibliothek.gui.dock.themes.color.TabColor;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 *
 * @author yildiza
 */
public class AITEclipseTabPanePainter implements TabPanePainter {

    private EclipseTabPane pane;

    public AITEclipseTabPanePainter(EclipseTabPane pane) {
        this.pane = pane;
    }

    public void setController(DockController controller) {
    }

    @Override
    public void paintBackground(Graphics g) {

        Dockable selection = pane.getSelectedDockable();

        if (selection == null) {
            return;
        }

        EclipseTab tab = pane.getTab(selection);
        if (tab == null || !tab.isPaneVisible()) {
            return;
        }

        Rectangle bounds = tab.getBounds();
        Rectangle available = pane.getAvailableArea();

        TabColor color = new TabColor("stack.tab.border", pane.getStation(), selection, Color.WHITE) {
            @Override
            protected void changed(Color oldValue, Color newValue) {
            }
        };
        color.connect(pane.getController());
        g.setColor(color.value());

        g.fillRect(bounds.x, bounds.y, available.width, available.height);
    }

    public void paintForeground(Graphics g) {
    }
}
