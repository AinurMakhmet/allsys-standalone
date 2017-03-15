package ui;

import com.sun.javafx.tk.Toolkit;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import models.*;

import java.awt.*;


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
    Button deleteEntryButton = new Button("Delete");
    Text description;
    Text skills;
    TextArea descriptionTextArea;


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
        cardVBox.setPadding(new Insets(20, 10, 10, 10));
        cardVBox.setSpacing(10);
        cardVBox.getStyleClass().add("card");
        cardPropertyNames = propertyNames;
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.TOP_RIGHT);
        hbox.getChildren().add(deleteEntryButton);
        cardVBox.getChildren().add(hbox);
        hbox.setPadding(new Insets(0, 0, 10, 0));
        cardVBox.setMaxWidth(350);

        descriptionTextArea  = new TextArea();
        descriptionTextArea.getStyleClass().add("text-area");

        setNewCard(propertyValues, null);
        return cardVBox;
    }

    void setNewCard(String[] propertyValues, DatabaseEntity dEntity) {
        ObservableList<Node> children = cardVBox.getChildren();
        children.remove(1, children.size());

        for (int i=0; i<propertyValues.length; i++) {

            Text propertyName = new Text(cardPropertyNames[i]+": ");
            propertyName.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            Text propertyValue = new Text(propertyValues[i]);
            propertyValue.setFont(Font.font("Arial", 12));
            propertyValue.setTextAlignment(TextAlignment.JUSTIFY);

            BorderPane borderPane = new BorderPane();
            HBox hbox = new HBox();

            borderPane.setPadding(new Insets(0, 3, 0, 3));
            hbox.setPadding(new Insets(0, 5, 0, 0));
            borderPane.setCenter(hbox);
            String propertyNameValue = propertyName.getText().toLowerCase();
            String skillsNameProperty = "skills";
            String descriptionNameProperty = "description";

            if (propertyNameValue.contains(descriptionNameProperty)) {
                HBox hboxInner = new HBox();
                hboxInner.getChildren().add(propertyName);
                hboxInner.setPadding(new Insets(5,0,0,0));
                hbox.getChildren().add(hboxInner);
                descriptionTextArea.setText(propertyValues[i]);
                descriptionTextArea.setFont(Font.font("Arial", 12));
                descriptionTextArea.setWrapText(true);
                Button editFieldValueButton = new Button("Edit");
                borderPane.setRight(editFieldValueButton);
                descriptionTextArea.setEditable(false);
                descriptionTextArea.setWrapText(true);
                descriptionTextArea.setPrefHeight(40);
                hbox.getChildren().add(descriptionTextArea);

                editFieldValueButton.setOnAction(e-> {
                    if (editFieldValueButton.getText().equals("Edit")) {
                        descriptionTextArea.setEditable(true);
                        descriptionTextArea.getStyleClass().add("editable");
                        editFieldValueButton.setText("Save");
                        descriptionTextArea.setPrefHeight(120);
                        descriptionTextArea.setStyle("-fx-background-color: white");
                    } else {
                        descriptionTextArea.setEditable(false);
                        if (dEntity!=null) {
                            if (dEntity.getClass().equals(Task.class)) {
                                ((Task)dEntity).setDescription(descriptionTextArea.getText());
                            } else if (dEntity.getClass().equals(Skill.class)) {
                                ((Skill)dEntity).setDescription(descriptionTextArea.getText());
                            } else if (dEntity.getClass().equals(Project.class)) {
                                ((Project)dEntity).setDescription(descriptionTextArea.getText());
                            }
                        }
                        descriptionTextArea.getStyleClass().remove("editable");
                        editFieldValueButton.setText("Edit");
                        descriptionTextArea.setPrefHeight(40);
                    }

                });
            } else if (propertyNameValue.contains(skillsNameProperty)) {
                hbox.getChildren().add(propertyName);
                hbox.getChildren().add(propertyValue);
                Button editFieldValueButton = new Button("Edit");
                borderPane.setRight(editFieldValueButton);
                editFieldValueButton.setOnAction(e-> {
/*
                    Stage dialog = new Stage();
                    dialog.initStyle(StageStyle.UTILITY);
                    Scene scene = new Scene(new Group(new Text(25, 25, "Hello World!")));
                    dialog.setScene(scene);
                    dialog.show();
*/

                });
            } else {
                hbox.getChildren().add(propertyName);
                hbox.getChildren().add(propertyValue);
            }

            cardVBox.getChildren().add(borderPane);
        }
    }
}
