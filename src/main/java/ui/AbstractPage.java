package ui;

import entity_utils.EmployeeSkillUtils;
import entity_utils.EmployeeUtils;
import entity_utils.TaskSkillUtils;
import entity_utils.TaskUtils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.util.Callback;
import models.*;
import org.junit.Assert;

import java.io.IOException;
import java.util.*;


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
    TextArea descriptionTextArea;
    Button editDescriptionValueButton = new Button("Edit");
    Button editSkillsValueButton = new Button("Edit");
    Dialog<Set<Skill>> dialog;


    public AbstractPage() {
        super();
        setPadding(new Insets(10));
        top = new HBox();
        top.setPadding(new Insets(10, 0, 10, 0));
        top.setSpacing(8);
        search = new TextField();

        top.getChildren().add(search);
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
                setEditDescriptionValueButton(dEntity);
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

            cardVBox.getChildren().add(borderPane);
        }
    }


    public void setEditDescriptionValueButton(DatabaseEntity dEntity) {
        editDescriptionValueButton.setOnAction(e-> {
            if (editDescriptionValueButton.getText().equals("Edit")) {
                descriptionTextArea.setEditable(true);
                descriptionTextArea.getStyleClass().add("editable");
                editDescriptionValueButton.setText("Save");
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
                editDescriptionValueButton.setText("Edit");
                descriptionTextArea.setPrefHeight(40);
            }

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
