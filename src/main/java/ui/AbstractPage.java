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

    public AbstractPage() {
        super();
        setPadding(new Insets(10));
        HBox top = new HBox();
        top.setPadding(new Insets(10));
        top.setSpacing(8);
        TextField search = new TextField();
        search.setPromptText("Search");

        top.getChildren().add(search);

        top.setAlignment(Pos.CENTER);
        setTop(top);
    }

    public TableView addTable(String pageName) {
        TableView table = new TableView();
        final Label label = new Label(pageName);
        label.setFont(new Font("Arial", 20));

        table.setEditable(true);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        return table;
    }

    public VBox addCard(String[] propertyNames, String[] propertyValues) {
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(30, 20, 10, 10));
        vbox.setSpacing(8);
        vbox.getStyleClass().add("card");


        for (int i=0; i<propertyNames.length; i++) {

            Text propertyName = new Text(propertyNames[i]+": ");
            propertyName.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            Text propertyValue = new Text(propertyValues[i]);
            propertyValue.setFont(Font.font("Arial", 12));

            HBox hbox = new HBox();
            hbox.getChildren().add(propertyName);
            hbox.getChildren().add(propertyValue);

            vbox.getChildren().add(hbox);
        }
        return vbox;
    }
}
