package ui;

import javafx.scene.control.Button;
import javafx.scene.control.ButtonBuilder;
import javafx.scene.control.Label;
import javafx.scene.control.LabelBuilder;


/**
 * Taken from http://www.jensd.de/wordpress/?p=132
 */
public class AwesomeDude {

    public static Button createIconButton(String iconName)
    {
        return createIconButton(iconName, "", 14);
    }

    public static Button createIconButton(String iconName, String text)
    {
        return createIconButton(iconName, text, 14);
    }

    public static Button createIconButton(String iconName, int iconSize)
    {
        return createIconButton(iconName, "", iconSize);
    }

    public static Button createIconButton(String iconName, String text, int iconSize)
    {
        Label icon = createIconLabel(iconName);
        icon.setStyle("-fx-font-size: " + iconSize + "px;");
        return ButtonBuilder.create()
                .text(text)
                .graphic(icon)
                .build();
    }

    public static Label createIconLabel(String iconName, String style)
    {
        return LabelBuilder.create()
                .text(iconName)
                .style(style)
                .build();
    }

    public static Label createIconLabel(String iconName)
    {
        return createIconLabel(iconName, 14);
    }

    public static Label createIconLabel(String iconName, int iconSize)
    {
        return LabelBuilder.create()
                .text(iconName)
                .styleClass("icons")
                .style("-fx-font-size: " + iconSize + "px;")
                .build();
    }
}