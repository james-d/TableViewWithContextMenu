package edu.marshall.denvir.examples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

public class TableViewSample extends Application {

  @Override
  public void start(final Stage primaryStage) {
    final BorderPane root = new BorderPane();
    final TableView<Person> table = new TableView<>();
    table.getItems().addAll(
        new Person("Jacob", "Smith", "jacob.smith@example.com"),
        new Person("Isabella", "Johnson", "isabella.johnson@example.com"),
        new Person("Ethan", "Williams", "ethan.williams@example.com"),
        new Person("Emma", "Jones", "emma.jones@example.com"),
        new Person("Michael", "Brown", "michael.brown@example.com"));
    TableColumn<Person, String> firstNameCol = new TableColumn<>("First Name");
    TableColumn<Person, String> lastNameCol = new TableColumn<>("Last Name");
    TableColumn<Person, String> emailCol = new TableColumn<>("Email");
    firstNameCol.setCellValueFactory(new PropertyValueFactory<Person, String>("firstName"));
    lastNameCol.setCellValueFactory(new PropertyValueFactory<Person, String>("lastName"));
    emailCol.setCellValueFactory(new PropertyValueFactory<Person, String>("email"));

    firstNameCol.setMinWidth(150);
    lastNameCol.setMinWidth(150);
    emailCol.setMinWidth(150);

    table.getColumns().addAll(Arrays.asList(firstNameCol, lastNameCol, emailCol));

    final ContextMenu tableContextMenu = new ContextMenu();
    final MenuItem addMenuItem = new MenuItem("Add...");
    addMenuItem.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        final PersonEditor editor = new PersonEditor();
        editor.showDialog(primaryStage, StageStyle.UNDECORATED, Modality.APPLICATION_MODAL, new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            table.getItems().add(editor.getPerson());
          }
        });
      }
    });
    final MenuItem deleteSelectedMenuItem = new MenuItem("Delete selected");
    deleteSelectedMenuItem.disableProperty().bind(Bindings.isEmpty(table.getSelectionModel().getSelectedItems()));
    deleteSelectedMenuItem.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        final List<Person> selectedPeople = new ArrayList<>(table.getSelectionModel().getSelectedItems());
        table.getItems().removeAll(selectedPeople);
      }
    });
    tableContextMenu.getItems().addAll(addMenuItem, deleteSelectedMenuItem);

    table.setContextMenu(tableContextMenu);

    table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    Callback<Person, List<MenuItem>> rowMenuItemFactory = new Callback<Person, List<MenuItem>>() {
      @Override
      public List<MenuItem> call(final Person person) {
        final MenuItem edit = new MenuItem("Edit");
        final MenuItem delete = new MenuItem("Delete");
        edit.setOnAction(new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            final PersonEditor editor = new PersonEditor(person);
            editor.showDialog(primaryStage, StageStyle.UNDECORATED, Modality.APPLICATION_MODAL);
          }
        });
        delete.setOnAction(new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            table.getItems().remove(person);
          }
        });
        return Arrays.asList(edit, delete);
      }
    };
    table.setRowFactory(new ContextMenuTableRowFactory<>(rowMenuItemFactory));

    Callback<TableCell<Person, String>, List<MenuItem>> emailCellMenuItemFactory = new Callback<TableCell<Person, String>, List<MenuItem>>() {

      @Override
      public List<MenuItem> call(final TableCell<Person, String> cell) {
        MenuItem email = new MenuItem("Email");
        email.setOnAction(new EventHandler<ActionEvent>() {

          @Override
          public void handle(ActionEvent event) {
            System.out.println("Email " + cell.getTableRow().getItem() + " with email address " + cell.getItem());
          }
        });
        return Collections.singletonList(email);
      }
    };

    emailCol.setCellFactory(new ContextMenuTableCellFactory<Person, String>(emailCellMenuItemFactory));

    root.setCenter(table);
    Scene scene = new Scene(root, 600, 400);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
