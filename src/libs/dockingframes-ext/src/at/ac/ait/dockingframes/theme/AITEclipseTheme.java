/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.ait.dockingframes.theme;

import bibliothek.extension.gui.dock.theme.EclipseTheme;
import bibliothek.gui.DockController;

import bibliothek.gui.dock.themes.ColorScheme;
import bibliothek.gui.dock.themes.ThemeProperties;
import bibliothek.gui.dock.themes.basic.BasicDockTitle;
import bibliothek.gui.dock.themes.basic.BasicDockTitleFactory;
import bibliothek.gui.dock.title.DockTitleRequest;
import bibliothek.gui.dock.util.DockProperties;
import bibliothek.gui.dock.util.Priority;
import bibliothek.gui.dock.util.PropertyKey;
import bibliothek.gui.dock.util.property.DynamicPropertyFactory;

/**
 *
 * @author ahmet
 */
@ThemeProperties(authors = {"Janni Kovacs", "Benjamin Sigg"},
descriptionBundle = "theme.eclipse.description",
nameBundle = "theme.eclipse",
webpages = {""})
public class AITEclipseTheme extends EclipseTheme {

//     /** Access to the {@link ColorScheme} used for this theme */
//    public static final PropertyKey<ColorScheme> AIT_ECLIPSE_COLOR_SCHEME =
//        new PropertyKey<ColorScheme>( "dock.ui.EclipseTheme.ColorScheme",
//    		new DynamicPropertyFactory<ColorScheme>(){
//				public ColorScheme getDefault(
//						PropertyKey<ColorScheme> key,
//						DockProperties properties ){
//					return new AITEclipseColorScheme();
//				}
//			}, true );


    public AITEclipseTheme() {
        super();

       // setColorSchemeKey(AIT_ECLIPSE_COLOR_SCHEME);
        setColorScheme(new AITEclipseColorScheme());    

//        setTitleFactory( new BasicDockTitleFactory(){
//        	@Override
//        	public void request( DockTitleRequest request ){
//        		request.answer( new AITBasicDockTitle( request.getTarget(), request.getVersion() ) );
//        	}
//        }, Priority.THEME );

    }


    @Override
    public void install(DockController controller) {
        super.install(controller);
        controller.getProperties().set( EclipseTheme.TAB_PAINTER, AITRectGradientPainter.FACTORY , Priority.THEME );
    }

}
