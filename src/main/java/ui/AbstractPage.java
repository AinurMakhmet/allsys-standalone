package ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;


/**
 * Created by nura on 06/12/16.
 */
public abstract class AbstractPage extends BorderPane{
    private VBox cardVBox;
    private String[] cardPropertyNames;
    TableView table;
    String[] cardValues;
    HBox top;
    HBox bottom;
    TextField search;


    public AbstractPage() {
        super();
        setPadding(new Insets(10));
        top = new HBox();
        top.setPadding(new Insets(10, 0, 10, 0));
        top.setSpacing(8);
        search = new TextField();

        top.getChildren().add(search);
        //top.setAlignment(Pos.CENTER);
        setTop(top);

        bottom = new HBox();
        bottom.setPadding(new Insets(10, 0, 10, 0));
        bottom.setSpacing(8);
        setBottom(bottom);
    }

    TableView addTable(String pageName) {
        TableView table = new TableView();
        final Label label = new Label(pageName);
        label.setFont(new Font("Arial", 20));

        table.setEditable(true);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        return table;
    }

    VBox addCard(String[] propertyNames, String[] propertyValues) {
        cardVBox = new VBox();
        cardVBox.setPadding(new Insets(30, 20, 10, 10));
        cardVBox.setSpacing(8);
        cardVBox.getStyleClass().add("card");
        cardPropertyNames = propertyNames;
        setNewCard(propertyValues);

        return cardVBox;
    }

    void setNewCard(String[] propertyValues) {
        cardVBox.getChildren().clear();
        for (int i=0; i<propertyValues.length; i++) {

            Text propertyName = new Text(cardPropertyNames[i]+": ");
            propertyName.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            Text propertyValue = new Text(propertyValues[i]);
            propertyValue.setFont(Font.font("Arial", 12));

            HBox hbox = new HBox();
            hbox.getChildren().add(propertyName);
            hbox.getChildren().add(propertyValue);

            cardVBox.getChildren().add(hbox);
        }
    }
}
