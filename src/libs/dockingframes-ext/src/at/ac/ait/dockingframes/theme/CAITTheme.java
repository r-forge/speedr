/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.ac.ait.dockingframes.theme;

import bibliothek.gui.dock.common.CControl;
import bibliothek.gui.dock.common.theme.CEclipseTheme;

/**
 *
 * @author yildiza
 */
public class CAITTheme extends CEclipseTheme{

    public CAITTheme(CControl control) {
        super(control, new AITEclipseTheme());
    }

    public CAITTheme(CControl control, AITEclipseTheme theme) {
        super(control, theme);
    }
}
