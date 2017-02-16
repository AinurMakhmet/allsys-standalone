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
import models.Employee;

/**
 * Creates sidebar that contains menu and page filters for search
 */
public class Sidebar extends VBox implements EventHandler {
    Hyperlink tasksLink =  new Hyperlink("Assignments");
    Hyperlink employeesLink =  new Hyperlink("Employees");
    Hyperlink skillsLink =  new Hyperlink("Skills");
    Hyperlink allocationRoomLink  =  new Hyperlink("Allocation room");
    Hyperlink importDataLink =  new Hyperlink("Import Data");
    Hyperlink visitedPageLink = tasksLink;

    private Sidebar() {
        this.setPadding(new Insets(0));
        this.setSpacing(0);
        this.getStyleClass().add("sidebar");
        this.setMinWidth(150);
        this.setWidth(150);
        this.setMaxWidth(150);

        Label title = new Label("Menu");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        title.getStyleClass().add("menu-title");
        title.setAlignment(Pos.TOP_LEFT);
        title.prefWidthProperty().bind(this.widthProperty());
        this.getChildren().add(title);


        tasksLink.setOnAction(this);
        employeesLink.setOnAction(this);
        skillsLink.setOnAction(this);
        allocationRoomLink.setOnAction(this);

        Hyperlink pageHyperlinks[] = new Hyperlink[] {
                tasksLink,
                employeesLink,
                skillsLink,
                allocationRoomLink,
                importDataLink
        };

        for (int i=0; i<4; i++) {
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

        VBox filterPane = new VBox();
        this.getChildren().add(filterPane);
        //this.setMargin(filterPane, new Insets(50, 0, 0, 0));
        filterPane.prefWidthProperty().bind(this.widthProperty());
        filterPane.getStyleClass().add("filter-pane");
        filterPane.prefHeightProperty().bind(this.heightProperty());

        Label filterTitle = new Label("Filters");
        filterTitle.setFont(Font.font("Arial"));
        filterTitle.prefWidthProperty().bind(this.widthProperty());
        filterTitle.getStyleClass().add("sidebar-item");
        filterTitle.getStyleClass().add("filter-title");

        filterPane.getChildren().add(filterTitle);
        filterPane.setAlignment(Pos.TOP_LEFT);

        Hyperlink filterOptions[] = new Hyperlink[] {
                new Hyperlink("Allocated"),
                new Hyperlink("Unallocated")
        };

        for (int i=0; i<filterOptions.length; i++) {
            VBox.setMargin(filterOptions[i], new Insets(0, 0, 0, 8));
            filterOptions[i].prefWidthProperty().bind(this.widthProperty());
            filterOptions[i].getStyleClass().add("sidebar-item");
            filterOptions[i].getStyleClass().add("filter-item");
            filterPane.getChildren().add(filterOptions[i]);
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

        if (event.getSource() == skillsLink) {
            MainUI.borderPane.setCenter(SkillsPage.getInstance());
            MainUI.borderPane.setRight(SkillsPage.getInstance().addCard());
        } else if (event.getSource() == employeesLink) {
            MainUI.borderPane.setCenter(EmployeesPage.getInstance());
            MainUI.borderPane.setRight(EmployeesPage.getInstance().addCard());
        } else if (event.getSource()==tasksLink) {
            MainUI.borderPane.setCenter(TasksPage.getInstance());
            MainUI.borderPane.setRight(TasksPage.getInstance().addCard());
        } else if (event.getSource()==allocationRoomLink) {
            MainUI.borderPane.setCenter(AllocationPage.getInstance());
            MainUI.borderPane.setRight(AllocationPage.getInstance().addCard());
        }

        visitedPageLink = (Hyperlink)event.getSource();
    }
}
