package com.phasmidsoftware.dsaipg.projects.mcts.connectfour;

import java.awt.Component;
import java.awt.Container;

import javax.swing.JButton;

public class TestUtils {

    public static Component getComponentOfType(Container parent, Class<?> type) {
        for (Component c : parent.getComponents()) {
            if (type.isInstance(c)) return c;
            if (c instanceof Container) {
                Component sub = getComponentOfType((Container) c, type);
                if (sub != null) return sub;
            }
        }
        return null;
    }

    public static JButton getButtonByText(Container container, String text) {
        for (Component c : container.getComponents()) {
            if (c instanceof JButton && ((JButton) c).getText().equalsIgnoreCase(text)) {
                return (JButton) c;
            } else if (c instanceof Container) {
                JButton sub = getButtonByText((Container) c, text);
                if (sub != null) return sub;
            }
        }
        return null;
    }
}
