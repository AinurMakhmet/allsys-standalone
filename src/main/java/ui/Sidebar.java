package ui;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Creates sidebar that contains menu
 */
public class Sidebar extends VBox implements EventHandler {
    private Hyperlink tasksLink =  new Hyperlink("Tasks");
    private Hyperlink employeesLink =  new Hyperlink("Employees");
    private Hyperlink skillsLink =  new Hyperlink("Skills");
    private Hyperlink projectsLink =  new Hyperlink("Projects");
    private Hyperlink visitedPageLink = tasksLink;

    private Sidebar() {
        this.setPadding(new Insets(0));
        this.setSpacing(0);
        this.getStyleClass().add("sidebar");
        this.setMinWidth(110);
        this.setWidth(110);
        this.setMaxWidth(110);

        Label title = new Label("Menu");
        title.getStyleClass().add("menu-title");
        title.setAlignment(Pos.TOP_LEFT);
        title.prefWidthProperty().bind(this.widthProperty());
        this.getChildren().add(title);


        tasksLink.setOnAction(this);
        employeesLink.setOnAction(this);
        skillsLink.setOnAction(this);
        projectsLink.setOnAction(this);

        Hyperlink pageHyperlinks[] = new Hyperlink[] {
                tasksLink,
                employeesLink,
                skillsLink,
                projectsLink
        };

        for (int i=0; i<pageHyperlinks.length; i++) {
            this.getChildren().add(pageHyperlinks[i]);
            pageHyperlinks[i].getStyleClass().add("sidebar-item");
            pageHyperlinks[i].setPadding(new Insets(0, 0, 0, 8));
            if (pageHyperlinks[i].equals(tasksLink)) {
                pageHyperlinks[i].getStyleClass().add("menu-item-clicked");
            } else {
                pageHyperlinks[i].getStyleClass().add("menu-item-non-clicked");
            }
            pageHyperlinks[i].prefWidthProperty().bind(this.widthProperty());
        }
    }

    public static Sidebar getInstance() {
        return new Sidebar();
    }


    @Override
    public void handle(Event event) {
        visitedPageLink.getStyleClass().remove("menu-item-clicked");
        ((Hyperlink) event.getSource()).getStyleClass().remove("menu-item-non-clicked");
        ((Hyperlink) event.getSource()).getStyleClass().add("menu-item-clicked");

        if (event.getSource()==tasksLink) {
            MainUI.borderPane.setCenter(TasksPage.getInstance());
            MainUI.borderPane.setRight(TasksPage.getInstance().addCard());
            TasksPage.getInstance().table.refresh();
        } else if (event.getSource() == employeesLink) {
            MainUI.borderPane.setCenter(EmployeesPage.getInstance());
            MainUI.borderPane.setRight(EmployeesPage.getInstance().addCard());
            EmployeesPage.getInstance().table.refresh();
        } else if (event.getSource() == projectsLink) {
            MainUI.borderPane.setCenter(ProjectsPage.getInstance());
            MainUI.borderPane.setRight(ProjectsPage.getInstance().addCard());
            ProjectsPage.getInstance().table.refresh();
        } else if (event.getSource() == skillsLink) {
            MainUI.borderPane.setCenter(SkillsPage.getInstance());
            MainUI.borderPane.setRight(SkillsPage.getInstance().addCard());
            SkillsPage.getInstance().table.refresh();
        }
        visitedPageLink = (Hyperlink)event.getSource();
    }
}
