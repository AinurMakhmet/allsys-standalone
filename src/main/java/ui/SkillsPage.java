package ui;

import entity_utils.SkillUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import models.Employee;
import models.Skill;
import models.Task;

import java.io.IOException;

/**
 * Created by nura on 06/12/16.
 */
public class SkillsPage extends AbstractPage{
    static final ObservableList<Skill> data = FXCollections.observableArrayList(
            SkillUtils.getAllSkills());
    static String[] cardValues;
    private static SkillsPage ourInstance = new SkillsPage();

    public static SkillsPage getInstance() {
        return ourInstance;
    }

    private SkillsPage() {
        super();
        setCenter(addTable("Skills"));
    }


    static TableView addTable(String pageName) {
        TableView table = AbstractPage.addTable(pageName);
        TableColumn id = new TableColumn("ID");
        TableColumn name = new TableColumn("Name");
        TableColumn level = new TableColumn("Level");

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

    VBox addCard() {
        String[] names = {"ID", "Name", "Description", "Level"};
        cardValues = new String[]{"--", "---", "---", "----"};
        return super.addCard(names, cardValues);
    }
}
