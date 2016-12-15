package ui;

import entity_utils.SkillUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Skill;

/**
 * Created by nura on 06/12/16.
 */
public class SkillsPage extends AbstractPage{
    final ObservableList<Skill> data = FXCollections.observableArrayList(
            SkillUtils.getAllSkills());

    private static SkillsPage ourInstance = new SkillsPage();

    public static SkillsPage getInstance() {
        return ourInstance;
    }

    private SkillsPage() {
        super();
        setCenter(addTable("Skills"));
    }


    @Override
    public TableView addTable(String pageName) {
        TableView table = super.addTable(pageName);
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
        return table;
    }
}
