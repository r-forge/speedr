/*
 * Bibliothek - DockingFrames
 * Library built on Java/Swing, allows the user to "drag and drop"
 * panels containing any Swing-Component the developer likes to add.
 * 
 * Copyright (C) 2007 Benjamin Sigg
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 * Benjamin Sigg
 * benjamin_sigg@gmx.ch
 * CH - Switzerland
 */
package at.ac.ait.dockingframes.theme;

import bibliothek.extension.gui.dock.theme.EclipseTheme;
import bibliothek.extension.gui.dock.theme.eclipse.EclipseColorScheme;
import bibliothek.gui.DockUI;
import bibliothek.gui.dock.themes.ColorScheme;
import bibliothek.gui.dock.util.laf.LookAndFeelColors;
import javax.swing.UIManager;

/**
 * A {@link ColorScheme} used by the {@link EclipseTheme}.
 * @author Benjamin Sigg
 */
public class AITEclipseColorScheme extends EclipseColorScheme {
    /**
     * Creates the new color scheme
     */
//    public AITEclipseColorScheme(){
//        this.updateUI();
//    }
    
    @Override
    public boolean updateUI(){
        super.updateUI();

        
        setColor( "stack.tab.border",                   DockUI.getColor( LookAndFeelColors.PANEL_BACKGROUND ) );
        setColor( "stack.tab.border.selected",          DockUI.getColor( LookAndFeelColors.PANEL_BACKGROUND ) );
        setColor( "stack.tab.border.selected.focused",  DockUI.getColor( LookAndFeelColors.PANEL_BACKGROUND ) );
        setColor( "stack.tab.border.selected.focuslost",DockUI.getColor( LookAndFeelColors.PANEL_BACKGROUND ) );
        
//        setColor( "stack.tab.top",                      DockUI.getColor( LookAndFeelColors.PANEL_BACKGROUND ) );
//        setColor( "stack.tab.top.selected",             DockUI.getColor( LookAndFeelColors.PANEL_BACKGROUND ) );
//        setColor( "stack.tab.top.selected.focused",     DockUI.getColor( LookAndFeelColors.PANEL_BACKGROUND ) );
//        setColor( "stack.tab.top.selected.focuslost",   DockUI.getColor( LookAndFeelColors.PANEL_BACKGROUND ) );
//
//        setColor( "stack.tab.bottom",                   DockUI.getColor( LookAndFeelColors.PANEL_BACKGROUND ) );
//        setColor( "stack.tab.bottom.selected",          DockUI.getColor( LookAndFeelColors.PANEL_BACKGROUND ) );
//        setColor( "stack.tab.bottom.selected.focused",  DockUI.getColor( LookAndFeelColors.PANEL_BACKGROUND ) );
//        setColor( "stack.tab.bottom.selected.focuslost",DockUI.getColor( LookAndFeelColors.PANEL_BACKGROUND ) );
//
        setColor( "stack.tab.text",                      UIManager.getColor( "textInactiveText" ));//DockUI.getColor( LookAndFeelColors.TITLE_FOREGROUND ) );
        setColor( "stack.tab.text.selected",             UIManager.getColor( "textInactiveText" ));//DockUI.getColor( LookAndFeelColors.TITLE_SELECTION_FOREGROUND ) );
        setColor( "stack.tab.text.selected.focused",     UIManager.getColor( "TextArea.foreground" ));//DockUI.getColor( LookAndFeelColors.PANEL_FOREGROUND ) );
        setColor( "stack.tab.text.selected.focuslost",   UIManager.getColor( "textInactiveText" ));//DockUI.getColor( LookAndFeelColors.PANEL_FOREGROUND ) );
//
//        setColor( "stack.border",                       DockUI.getColor( LookAndFeelColors.PANEL_BACKGROUND ) );
//
//        setColor( "selection.border",                   DockUI.getColor( LookAndFeelColors.PANEL_BACKGROUND ) );
        return true;
    }
}
