package at.ac.ait.dockingframes.theme;

import at.ac.ait.dockingframes.theme.stack.tab.AITEclipseTabPanePainter;
import bibliothek.extension.gui.dock.theme.eclipse.OwnedRectEclipseBorder;
import bibliothek.extension.gui.dock.theme.eclipse.stack.EclipseTabPane;
import bibliothek.extension.gui.dock.theme.eclipse.stack.tab.BorderedComponent;
import bibliothek.extension.gui.dock.theme.eclipse.stack.tab.DefaultInvisibleTab;
import bibliothek.extension.gui.dock.theme.eclipse.stack.tab.InvisibleTab;
import bibliothek.extension.gui.dock.theme.eclipse.stack.tab.InvisibleTabPane;
import bibliothek.extension.gui.dock.theme.eclipse.stack.tab.RectGradientPainter;
import bibliothek.extension.gui.dock.theme.eclipse.stack.tab.TabComponent;
import bibliothek.extension.gui.dock.theme.eclipse.stack.tab.TabPainter;
import bibliothek.extension.gui.dock.theme.eclipse.stack.tab.TabPanePainter;
import java.awt.*;

import javax.swing.border.Border;


import bibliothek.gui.DockController;
import bibliothek.gui.Dockable;

public class AITRectGradientPainter extends RectGradientPainter {

    public static final TabPainter FACTORY = new TabPainter() {

        public TabComponent createTabComponent(EclipseTabPane pane, Dockable dockable) {
            return new AITRectGradientPainter(pane, dockable);
        }

        public TabPanePainter createDecorationPainter(EclipseTabPane pane) {
            return new AITEclipseTabPanePainter(pane);
        }

        public InvisibleTab createInvisibleTab(InvisibleTabPane pane, Dockable dockable) {
            return new DefaultInvisibleTab(pane, dockable);
        }

        public Border getFullBorder(BorderedComponent owner, DockController controller, Dockable dockable) {
            return new OwnedRectEclipseBorder(owner, controller, true);
        }
    };

    public AITRectGradientPainter(EclipseTabPane pane, Dockable dockable) {
        super(pane, dockable);
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(colorStackTabBottom.value());
        g.fillRect(0, 0, getWidth(), getHeight());

    }

//    @Override
//    public void doLayout() {
//        ButtonPanel itemPanel = getButtons();
//        Dimension[] itemPreferred = itemPanel.getPreferredSizes();
//
//        Dockable dockable = getDockable();
//        boolean isSelected = isSelected();
//        FontRenderContext frc = new FontRenderContext(null, false, false);
//        Rectangle2D bounds = getFont().getStringBounds(dockable.getTitleText(), frc);
//        int x = 5 + (int) bounds.getWidth() + 5;
//        if ((doPaintIconWhenInactive() || isSelected) && dockable.getTitleIcon() != null) {
//            x += dockable.getTitleIcon().getIconWidth() + 5;
//        }
//        if (isSelected) {
//            x += 5;
//        }
//        int stripWidth = 0;
//
//        if (x + itemPreferred[0].width <= getWidth()) {
//            stripWidth = x;
//        } else {
//            stripWidth = Math.max(getWidth() / 2, getWidth() - itemPreferred[0].width);
//        }
//
//        int remaining = getWidth() - stripWidth;
//        int count = itemPreferred.length - 1;
//
//        while (count > 0 && itemPreferred[count].width > remaining) {
//            count--;
//        }
//        int width = Math.min(remaining, itemPreferred[count].width);
//
//        itemPanel.setVisibleActions(count);
//        itemPanel.setBounds(getWidth() - width, 0, width, getHeight());
//    }
    //I DON't NEED THOOSE CODE
//    private MatteBorder contentBorder = new MatteBorder(2, 2, 2, 2, Color.BLACK);
//    public int getOverlap() {
//        return 0;
//    }
//
//    public void update() {
//        updateBorder();
//        revalidate();
//    }
//    @Override
//    public Dimension getPreferredSize() {
//        Dockable dockable = getDockable();
//        boolean isSelected = isSelected();
//        ButtonPanel buttons = getButtons();
//
//        FontRenderContext frc = new FontRenderContext(null, false, false);
//        Rectangle2D bounds = getFont().getStringBounds(dockable.getTitleText(), frc);
//        int width = 5 + (int) bounds.getWidth() + 5;
//        int height = 6 + (int) bounds.getHeight();
//        if ((doPaintIconWhenInactive() || isSelected) && dockable.getTitleIcon() != null) {
//            width += dockable.getTitleIcon().getIconWidth() + 5;
//        }
//        if (isSelected) {
//            width += 5;
//        }
//        if (buttons != null) {
//            Dimension preferred = buttons.getPreferredSize();
//            width += preferred.width + 1;
//            height = Math.max(height, preferred.height + 1);
//        }
//
//        return new Dimension(width, height);
//    }
//
//    public Border getContentBorder() {
//        return contentBorder;
//    }
//    @Override
//    protected void updateBorder() {
//        Color color2;
//
//        Window window = SwingUtilities.getWindowAncestor(getTabbedComponent());
//        boolean focusTemporarilyLost = false;
//
//        if (window != null) {
//            focusTemporarilyLost = !window.isActive();
//        }
//
//        if (isSelected()) {
//            if (isFocused()) {
//                if (focusTemporarilyLost) {
//                    color2 = colorStackTabBorderSelectedFocusLost.value();
//                } else {
//                    color2 = colorStackTabBorderSelectedFocused.value();
//                }
//            } else {
//                color2 = colorStackTabBorderSelected.value();
//            }
//        } else {
//            color2 = colorStackTabBorder.value();        // set border around tab content
//        }
//        if (!color2.equals(contentBorder.getMatteColor())) {
//            contentBorder = new MatteBorder(2, 2, 2, 2, color2);
//        }
//
//        if (getTabbedComponent() != null) {
//            getTabbedComponent().updateContentBorder();
//        }
//    }
//    @Override
//    protected void paintComponent2(Graphics g) {
//        super.paintComponent(g);
//        int height = getHeight(), width = getWidth();
//        Graphics2D g2d = (Graphics2D) g;
//        Color lineColor = colorStackBorder.value();
//        Color color1, color2, colorText;
//        boolean focusTemporarilyLost = KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow() != SwingUtilities.getWindowAncestor(getTabbedComponent());
//
//        if (isFocused() && !focusTemporarilyLost) {
//            color1 = colorStackTabTopSelectedFocused.value();
//            color2 = colorStackTabBottomSelectedFocused.value();
//            colorText = colorStackTabTextSelectedFocused.value();
//        } else if (isFocused() && focusTemporarilyLost) {
//            color1 = colorStackTabTopSelectedFocusLost.value();
//            color2 = colorStackTabBottomSelectedFocusLost.value();
//            colorText = colorStackTabTextSelectedFocusLost.value();
//        } else if (isSelected()) {
//            color1 = colorStackTabTopSelected.value();
//            color2 = colorStackTabBottomSelected.value();
//            colorText = colorStackTabTextSelected.value();
//        } else {
//            color1 = colorStackTabTop.value();
//            color2 = colorStackTabBottom.value();
//            colorText = colorStackTabText.value();
//        }
//
//        GradientPaint gradient = color1.equals(color2) ? null : new GradientPaint(0, 0, color1, 0, height, color2);
//        boolean isSelected = isSelected();
//        Dockable dockable = getDockable();
//        int tabIndex = getTabIndex();
//        RexTabbedComponent comp = getTabbedComponent();
//
//        Paint old = g2d.getPaint();
//        if (gradient != null) {
//            g2d.setPaint(gradient);
//        } else {
//            g2d.setPaint(color1);
//        }
//        if (isSelected) {
//            g.fillRect(1, 0, width - 2, height);
//            g.drawLine(0, 1, 0, height);
//            g2d.setPaint(old);
//            // left
//            if (tabIndex != 0) {
//                g.drawLine(1, 0, 1, 0);
//                g.drawLine(0, 1, 0, height);
//            }
//            // right
//            g.drawLine(width - 2, 0, width - 2, 0);
//            g.drawLine(width - 1, 1, width - 1, height);
//            // overwrite gradient pixels
//        } else {
//            g.fillRect(0, 0, getWidth(), getHeight() - 1);
//
//            g2d.setPaint(old);
//        }
//
//        // draw icon
//        int iconOffset = 0;
//        if (isSelected || doPaintIconWhenInactive()) {
//            Icon i = dockable.getTitleIcon();
//            if (i != null) {
//                i.paintIcon(comp, g, 5, 4);
//                iconOffset = i.getIconWidth() + 5;
//            }
//        }
//
//        // draw separator lines
//        if (!isSelected && tabIndex != comp.indexOf(comp.getSelectedTab()) - 1) {
//            g.setColor(lineColor);
//            g.drawLine(width - 1, 0, width - 1, height);
//        }
//
//        // draw text
//        g.setColor(colorText);
//        g.setFont(getFont());
//        g.drawString(dockable.getTitleText(), 5 + iconOffset, height / 2 + g.getFontMetrics().getHeight() / 2 - 2);
//    }
//    @Override
//    protected void paintComponent(Graphics g) {
//        //super.paintComponent(g);
//        int height = getHeight(), width = getWidth();
//        Graphics2D g2d = (Graphics2D) g;
//        Color lineColor = colorStackBorder.value();
//        Color color1, color2;
//        boolean focusTemporarilyLost = isFocusTemporarilyLost();
//
//        TabPlacement orientation = getOrientation();
//
//        if (isFocused() && !focusTemporarilyLost) {
//            color1 = colorStackTabTopSelectedFocused.value();
//            color2 = colorStackTabBottomSelectedFocused.value();
//        } else if (isFocused() && focusTemporarilyLost) {
//            color1 = colorStackTabTopSelectedFocusLost.value();
//            color2 = colorStackTabBottomSelectedFocusLost.value();
//        } else if (isSelected()) {
//            color1 = colorStackTabTopSelected.value();
//            color2 = colorStackTabBottomSelected.value();
//        } else {
//            color1 = colorStackTabTop.value();
//            color2 = colorStackTabBottom.value();
//        }
//
//        if (orientation == TabPlacement.BOTTOM_OF_DOCKABLE || orientation == TabPlacement.RIGHT_OF_DOCKABLE) {
//            Color temp = color1;
//            color1 = color2;
//            color2 = temp;
//        }
//
//        GradientPaint gradient = null;
//        if (!color1.equals(color2)) {
//            if (orientation.isHorizontal()) {
//                gradient = new GradientPaint(0, 0, color1, 0, height, color2);
//            } else {
//                gradient = new GradientPaint(0, 0, color1, width, 0, color2);
//            }
//        }
//
//
//        boolean isSelected = isSelected();
//        int tabIndex = getTabIndex();
//
//        g2d.setColor(lineColor);
//        Paint old = g2d.getPaint();
//        if (gradient != null) {
//            g2d.setPaint(gradient);
//        } else {
//            g2d.setPaint(color1);
//        }
//
////        if (isSelected) {
////            paintSelected(g2d, tabIndex, old);
////        } else {
//            switch (orientation) {
//                case TOP_OF_DOCKABLE:
//                    g.fillRect(0, 0, getWidth(), getHeight() - 1);
//                    break;
//                case BOTTOM_OF_DOCKABLE:
//                    g.fillRect(0, 1, getWidth(), getHeight() - 1);
//                    break;
//                case LEFT_OF_DOCKABLE:
//                    g.fillRect(0, 0, getWidth() - 1, getHeight());
//                    break;
//                case RIGHT_OF_DOCKABLE:
//                    g.fillRect(1, 0, getWidth() - 1, getHeight());
//                    break;
//            }
//
//
//            g2d.setPaint(old);
////        }
//
//        // draw separator lines
////        if (!isSelected && !isNextTabSelected()) {
//        if (!isNextTabSelected()) {
//            g.setColor(lineColor);
//            switch (orientation) {
//                case TOP_OF_DOCKABLE:
//                case BOTTOM_OF_DOCKABLE:
//                    g.drawLine(width - 1, 0, width - 1, height);
//                    break;
//                case LEFT_OF_DOCKABLE:
//                case RIGHT_OF_DOCKABLE:
//
//                    break;
//            }
//        }
//    }
}
