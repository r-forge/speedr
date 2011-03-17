/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.ait.dockingframes.theme;

import bibliothek.gui.Dockable;
import bibliothek.gui.dock.themes.basic.BasicDockTitle;
import bibliothek.gui.dock.themes.font.TitleFont;
import bibliothek.gui.dock.title.DockTitleVersion;
import bibliothek.gui.dock.util.font.DockFont;
import bibliothek.gui.dock.util.font.GenericFontModifier;
import bibliothek.util.Condition;

/**
 *
 * @author yildiza
 */
public class AITBasicDockTitle extends BasicDockTitle {

    public AITBasicDockTitle(Dockable dockable, DockTitleVersion origin) {
        super(dockable, origin, false);

        GenericFontModifier flat= new GenericFontModifier();
        flat.setBold(GenericFontModifier.Modify.OFF);

        GenericFontModifier bold=new GenericFontModifier();
        bold.setBold(GenericFontModifier.Modify.ON);

        addConditionalFont(DockFont.ID_TITLE_ACTIVE, TitleFont.KIND_TITLE_FONT, new Condition() {

            public boolean getState() {
                return isActive();
            }
        },bold);

        addConditionalFont(DockFont.ID_TITLE_INACTIVE, TitleFont.KIND_TITLE_FONT, new Condition() {

            public boolean getState() {
                return !isActive();
            }
        }, flat);

    }

    @Override
    public boolean isActive() {
        return super.isActive();
    }


}
