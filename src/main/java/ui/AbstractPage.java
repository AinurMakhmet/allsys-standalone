package ui;

import constants.AwesomeIcons;
import entity_utils.EmployeeSkillUtils;
import entity_utils.EmployeeUtils;
import entity_utils.TaskSkillUtils;
import entity_utils.TaskUtils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
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
import javafx.util.Callback;
import models.*;
import org.junit.Assert;

import java.io.IOException;
import java.util.*;


/**
 * Creates the carcas for all pages in the system.
 */
public abstract class AbstractPage extends BorderPane{
    private VBox cardVBox;
    HBox cardHBox;
    private String[] cardPropertyNames;
    TableView table;
    String[] cardValues;
    HBox top;
    HBox bottom;
    //TextField search;
    Button deleteEntryButton = AwesomeDude.createIconButton(AwesomeIcons.ICON_TRASH);
    TextArea descriptionTextArea;
    Button editDescriptionValueButton = AwesomeDude.createIconButton(AwesomeIcons.ICON_PENCIL);
    Button saveDescriptionValueButton = AwesomeDude
            .createIconButton(AwesomeIcons.ICON_SAVE);
    Button editSkillsValueButton = AwesomeDude.createIconButton(AwesomeIcons.ICON_PENCIL);
    Button addButton = AwesomeDude.createIconButton(AwesomeIcons.ICON_PLUS);
    Dialog<Set<Skill>> dialog;
    private VBox cardVBoxInner;
    ScrollPane scrollPane;


    public AbstractPage() {
        super();
        setPadding(new Insets(10));
        top = new HBox();
        top.setPadding(new Insets(10, 0, 10, 0));
        top.setSpacing(8);
        //search = new TextField();

        //top.getChildren().add(search);
        setTop(top);

        bottom = new HBox();
        bottom.setPadding(new Insets(10, 0, 10, 0));
        bottom.setSpacing(8);
        setBottom(bottom);

        deleteEntryButton.setTooltip(new Tooltip("Delete"));
        editDescriptionValueButton.setTooltip(new Tooltip("Edit Description"));
        saveDescriptionValueButton.setTooltip(new Tooltip("Save"));

        editSkillsValueButton.setTooltip(new Tooltip("Edit Skills"));
    }

    TableView addTable(String pageName) {
        TableView table = new TableView();
        final Label label = new Label(pageName);
        //label.setFont(new Font("Arial", 20));

        table.setEditable(true);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        return table;
    }

    HBox addCard(String[] propertyNames, String[] propertyValues) {
        Separator separator = new Separator();
        separator.setMaxWidth(0.2);
        separator.setOrientation(Orientation.VERTICAL);

        cardHBox = new HBox();
        cardHBox.getChildren().add(separator);
        cardHBox.setMinWidth(350);

        //Delete button
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.TOP_RIGHT);
        hbox.getChildren().add(deleteEntryButton);
        hbox.setPadding(new Insets(0, 2, 10, 0));


        scrollPane = new ScrollPane();
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        cardVBox = new VBox();
        cardVBox.setPadding(new Insets(20, 10, 10, 10));
        cardVBox.setSpacing(10);
        cardVBox.getStyleClass().add("card");
        cardPropertyNames = propertyNames;
        cardVBox.setMaxWidth(430);
        cardVBox.getChildren().add(hbox);

        descriptionTextArea  = new TextArea();
        descriptionTextArea.getStyleClass().add("text-area");

        setNewCard(propertyValues, null);

        Assert.assertNotNull(scrollPane);
        cardVBox.getChildren().add(scrollPane);
        cardHBox.getChildren().add(cardVBox);

        return cardHBox;
    }

    void setNewCard(String[] propertyValues, DatabaseEntity dEntity) {
        cardVBoxInner = new VBox();
        cardVBoxInner.setPrefWidth(400);
        scrollPane.setContent(cardVBoxInner);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        for (int i=0; i<propertyValues.length; i++) {
            Separator separator = new Separator();
            separator.setMaxHeight(0.2);
            separator.setOrientation(Orientation.HORIZONTAL);

            Text propertyName = new Text(cardPropertyNames[i]+": ");
            propertyName.getStyleClass().add("card-property-name");
            Text propertyValue = new Text(propertyValues[i]);
            propertyValue.getStyleClass().add("card-property-value");
            propertyValue.setTextAlignment(TextAlignment.JUSTIFY);

            BorderPane borderPane = new BorderPane();
            borderPane.setPadding(new Insets(0, 3, 0, 3));
            HBox hbox = new HBox();
            hbox.setPadding(new Insets(5, 10, 0, 0));
            borderPane.setCenter(hbox);
            String propertyNameValue = propertyName.getText().toLowerCase();
            String skillsNameProperty = "skills";
            String descriptionNameProperty = "description";

            if (propertyNameValue.contains(descriptionNameProperty)) {
                HBox hboxInner = new HBox();
                hboxInner.getChildren().add(propertyName);
                hbox.getChildren().add(hboxInner);
                descriptionTextArea.setText(propertyValues[i]);
                descriptionTextArea.setWrapText(true);
                setEditSaveDescriptionButton(dEntity);
                borderPane.setRight(editDescriptionValueButton);
                descriptionTextArea.setEditable(false);
                descriptionTextArea.setWrapText(true);
                descriptionTextArea.setPrefHeight(40);
                hbox.getChildren().add(descriptionTextArea);
            } else if (propertyNameValue.contains(skillsNameProperty)) {
                hbox.getChildren().add(propertyName);
                hbox.getChildren().add(propertyValue);
                setEditSkillsValueButton(dEntity, propertyValue);
                borderPane.setRight(editSkillsValueButton);
            } else {
                hbox.getChildren().add(propertyName);
                hbox.getChildren().add(propertyValue);
            }
            cardVBoxInner.getChildren().add(borderPane);
            if (i<propertyValues.length-1) {
                cardVBoxInner.getChildren().add(separator);
            }
        }
        cardHBox.getChildren().remove(1,1);
        if (dEntity==null) {
            cardVBox.setVisible(false);
        } else {
            cardVBox.setVisible(true);
        }
    }


    public void setEditSaveDescriptionButton(DatabaseEntity dEntity) {
        editDescriptionValueButton.setOnAction(e-> {
            descriptionTextArea.setEditable(true);
            descriptionTextArea.setStyle("-fx-background-color: white");
            descriptionTextArea.setPrefHeight(120);
            ((BorderPane)editDescriptionValueButton.getParent()).setRight(saveDescriptionValueButton);

        });
        saveDescriptionValueButton.setOnAction(e-> {
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
            descriptionTextArea.setStyle("-fx-background-color: transparent");
            descriptionTextArea.setPrefHeight(40);
            ((BorderPane)saveDescriptionValueButton.getParent()).setRight(editDescriptionValueButton);
        });
    }

    public void setEditSkillsValueButton(DatabaseEntity dEntity, Text propertyValue) {
        editSkillsValueButton.setOnAction(e-> {
            if (dEntity!=null && dEntity.getClass().equals(Task.class)) {
                final Task task = (Task)dEntity;
                createDialog(dEntity);
                Optional<Set<Skill>> result = dialog.showAndWait();
                try {
                    if (result.isPresent()) {
                        task.getSkills().forEach(skill-> {
                            if (!result.get().contains(skill)) {
                                TaskSkill taskSkill = new TaskSkill();
                                taskSkill.setSkill(skill);
                                taskSkill.setTask((Task) dEntity);
                                try {
                                    for (TaskSkill tSkill: task.getTaskSkillObjects()) {
                                        if (tSkill.equals(taskSkill)) {
                                            taskSkill = tSkill;
                                            Assert.assertNotNull(taskSkill.getId());
                                            TaskSkillUtils.deleteEntity(TaskSkill.class, taskSkill);
                                            Assert.assertNull(TaskSkillUtils.getTaskSkill(taskSkill.getId()));
                                        }
                                    }
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                            } else {
                                result.get().remove(skill);
                            }
                        });
                        result.get().forEach(skill -> {
                            boolean contains = false;
                            try {
                                contains = task.getTaskSkillObjects().contains(skill);
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            } if (!contains){
                                TaskSkill taskSkill = new TaskSkill();
                                taskSkill.setSkill(skill);
                                taskSkill.setTask((Task) dEntity);
                                TaskSkillUtils.createEntity(TaskSkill.class, taskSkill);
                                Assert.assertNotNull(taskSkill.getId());
                            }

                        });
                        TaskUtils.updateEntity(task);

                        String skills = "---";
                        if (task.getSkills()!=null){
                            skills="";
                            for (Skill skill : task.getSkills()) {
                                skills += skill.getName()+ " (Level " +skill.getLevel()+")\n";
                            }
                        }
                        propertyValue.setText(skills);
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            } else if (dEntity!=null && dEntity.getClass().equals(Employee.class)){
                final Employee employee = (Employee)dEntity;
                createDialog(dEntity);
                Optional<Set<Skill>> result = dialog.showAndWait();
                try {
                    if (result.isPresent()) {
                        employee.getSkills().forEach(skill-> {
                            if (result.isPresent()&& !result.get().contains(skill)) {
                                EmployeeSkill employeeSkill = new EmployeeSkill();
                                employeeSkill.setSkill(skill);
                                employeeSkill.setEmployee((Employee) dEntity);
                                try {
                                    for (EmployeeSkill eSkill: employee.getEmployeeSkillObjects()) {
                                        if (eSkill.equals(employeeSkill)) {
                                            employeeSkill = eSkill;
                                            Assert.assertNotNull(employeeSkill.getId());
                                            EmployeeSkillUtils.deleteEntity(EmployeeSkill.class, employeeSkill);
                                            Assert.assertNull(EmployeeSkillUtils.getEntityById(EmployeeSkill.class, employeeSkill.getId()));
                                        }
                                    }
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                            } else {
                                result.get().remove(skill);
                            }
                        });
                        result.get().forEach(skill -> {
                            boolean contains = false;
                            try {
                                contains = employee.getEmployeeSkillObjects().contains(skill);
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            } if (!contains) {
                                EmployeeSkill employeeSkill = new EmployeeSkill();
                                employeeSkill.setSkill(skill);
                                employeeSkill.setEmployee((Employee) dEntity);
                                EmployeeSkillUtils.createEntity(EmployeeSkill.class, employeeSkill);
                                Assert.assertNotNull(employeeSkill.getId());
                            }
                        });
                        EmployeeUtils.updateEntity(employee);
                        String skills = "---";
                        if (employee.getSkills()!=null){
                            skills="";
                            for (Skill skill : employee.getSkills()) {
                                skills += skill.getName()+ " (Level " +skill.getLevel()+")\n ";
                            }
                        }
                        propertyValue.setText(skills);
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    public void createDialog(DatabaseEntity entity) {

        //Scene dialog = new Scene();


        Set<Skill> entitySkills = new HashSet<>();
        VBox vBoxForChecks = new VBox();

        final CheckBox[] cbs = new CheckBox[SystemData.allSkills.size()];
        dialog = new Dialog();
        dialog.setHeaderText("Select skills");

        Task task = null;
        Employee employee = null;
        if (entity.getClass().equals(Task.class)) {
            task = (Task)entity;
        } else {
            employee = (Employee)entity;
        }

        for (int j = 0; j < SystemData.allSkills.size(); j++) {
            final Skill skill= SystemData.allSkills.get(j);
            final CheckBox checkBox = new CheckBox(skill.getName()+ " (Level " +skill.getLevel()+")");
            //cbs[j] = checkBox;
            vBoxForChecks.getChildren().add(checkBox);
            if (task!=null) {
                try {
                    if (task.getSkills().contains(skill)) {
                        checkBox.setSelected(true);
                        entitySkills.add(skill);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (employee!=null) {
                try {
                    if (employee.getSkills().contains(skill)) {
                        checkBox.setSelected(true);
                        entitySkills.add(skill);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
                public void changed(ObservableValue<? extends Boolean> ov,
                                    Boolean old_val, Boolean new_val) {
                    if (new_val) {
                        entitySkills.add(skill);
                    } else {
                        entitySkills.remove(skill);
                    }
                }
            });
        }

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        dialog.setResultConverter(new Callback<ButtonType, Set<Skill>>() {
            @Override
            public Set<Skill> call(ButtonType b) {
                if (b == saveButtonType) {
                    return entitySkills;
                }
                return null;
            }
        });
        vBoxForChecks.setSpacing(5);
        dialog.setWidth(200);
        vBoxForChecks.setPrefWidth(200);
        dialog.setHeight(500);
        dialog.getDialogPane().setContent(vBoxForChecks);
        dialog.getDialogPane().getButtonTypes().add(saveButtonType);
        dialog.getDialogPane().getButtonTypes().add(cancelButtonType);
    }


}
