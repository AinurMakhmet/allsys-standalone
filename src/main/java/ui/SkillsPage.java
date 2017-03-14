package ui;

import entity_utils.SkillUtils;
import entity_utils.TaskUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import models.Employee;
import models.Skill;
import models.SystemData;
import models.Task;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Created by nura on 06/12/16.
 */
public class SkillsPage extends AbstractPage{
    final ObservableList<Skill> data = FXCollections.observableArrayList(SystemData.allSkills);
    private TableColumn name, level;
    private static SkillsPage ourInstance = new SkillsPage();

    public static SkillsPage getInstance() {
        return ourInstance;
    }

    private SkillsPage() {
        super();
        search.setPromptText("Search skills");
        setCenter(addTable("Skills"));

        final TextField addName = new TextField();
        addName.setPromptText("Name");
        addName.setMaxWidth(80);
        final TextField addLevel = new TextField();
        addLevel.setMaxWidth(80);
        addLevel.setPromptText("Level");
        final Button addButton = new Button("Add Skill");
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {
                    Integer level = null;
                    if (addName.getText().equals("") || addLevel.getText().equals("")) {
                        MainUI.alertError("Invalid input", "A skill must have a name and a level.");
                        return;
                    }
                    if (!addLevel.getText().equals("")) {
                        level = Integer.parseInt(addLevel.getText());
                    }
                    if (level != null && level < 1) {
                        MainUI.alertError("Invalid input", "Level cannot be of a negative value ");
                        return;
                    }
                    Skill newSkill = new Skill(addName.getText(), addLevel.getText());
                    Integer id = SystemData.allSkills.size() + 1;
                    SkillUtils.createEntity(Skill.class, newSkill);
                    if (SkillUtils.getSkill(id) != null) {
                        SystemData.allSkills.add(newSkill);
                        data.add(newSkill);
                        addName.clear();
                        addLevel.clear();
                        table.refresh();
                    }
                } catch (NumberFormatException | ClassCastException exc) {
                    MainUI.alertError("Invalid input", "Please enter only numbers to the Level field or leave it blank.");
                }
            }
        });
        bottom.getChildren().addAll(addName, addLevel, addButton);
    }

    @Override
    TableView addTable(String pageName) {
        table = super.addTable(pageName);
        TableColumn id = new TableColumn("ID");
        name = new TableColumn("Name");
        level = new TableColumn("Level");

        id.setMinWidth(50);
        id.setCellValueFactory(
                new PropertyValueFactory<Skill, String>("id"));

        name.setMinWidth(100);
        name.setCellValueFactory(
                new PropertyValueFactory<Skill, String>("name"));

        level.setMinWidth(150);
        level.setCellValueFactory(
                new PropertyValueFactory<Skill, String>("level"));

        table.getColumns().addAll(id, name, level);
        table.setItems(data);

        setEditableCells();

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection!=null) {
                cardValues= new String[]{
                        ((Skill)newSelection).getId().toString(),
                        ((Skill) newSelection).getName(),
                        ((Skill) newSelection).getDescription()==null ? "---" : ((Skill) newSelection).getDescription().toString(),
                        ((Skill) newSelection).getLevel()==null ? "---" : ((Skill) newSelection).getLevel().toString(),
                };
                setNewCard(cardValues);
            }
        });
        return table;
    }

    private void setEditableCells() {
        name.setCellFactory(TextFieldTableCell.forTableColumn());
        name.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Skill, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Skill, String> event) {
                String name = event.getNewValue();
                ((Skill) event.getTableView().getItems().get(
                        event.getTablePosition().getRow())
                ).setName(name);
            }
        });
        level.setCellFactory(TextFieldTableCell.forTableColumn());
        level.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Skill, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Skill, String> event) {
                String level = event.getNewValue();
                ((Skill) event.getTableView().getItems().get(
                        event.getTablePosition().getRow())
                ).setLevel(level);
            }
        });
    }

    VBox addCard() {
        String[] names = {"ID", "Name", "Description", "Level"};
        cardValues = new String[]{"--", "---", "---", "----"};
        return super.addCard(names, cardValues);
    }
}
