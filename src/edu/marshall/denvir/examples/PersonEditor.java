package edu.marshall.denvir.examples;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class PersonEditor {
  private final Person person ;
  private final String initialFirstName ;
  private final String initialLastName ;
  private final String initialEmail ;
  private Node view ;
  
  public PersonEditor(Person person) {
    this.person = person ;
    this.initialFirstName = person.getFirstName() ;
    this.initialLastName = person.getLastName() ;
    this.initialEmail = person.getEmail() ;
  }
  
  public PersonEditor() {
    this(new Person());
  }
  
  public final Person getPerson() {
    return person ;
  }

  
  public Node getView() {
    if (view == null) {
      view = createView();
    }
    return view ;
  }
  
  private Node createView() {
    final TextField firstNameField = new TextField();
    final TextField lastNameField = new TextField();
    final TextField emailField = new TextField();
    firstNameField.textProperty().bindBidirectional(person.firstNameProperty());
    lastNameField.textProperty().bindBidirectional(person.lastNameProperty());
    emailField.textProperty().bindBidirectional(person.emailProperty());
    
    final GridPane view = new GridPane();
    view.setHgap(5);
    view.setVgap(3);
    final ColumnConstraints leftCol = new ColumnConstraints();
    leftCol.setHalignment(HPos.RIGHT);
    leftCol.setHgrow(Priority.NEVER);
    final ColumnConstraints rightCol = new ColumnConstraints();
    rightCol.setHalignment(HPos.LEFT);
    rightCol.setMinWidth(250);
    rightCol.setHgrow(Priority.ALWAYS);
    
    view.getColumnConstraints().addAll(leftCol, rightCol);
    view.addRow(0, new Label("First Name:"), firstNameField);
    view.addRow(1, new Label("Last Name:"), lastNameField);
    view.addRow(2, new Label("Email:"), emailField);
    
    return view ;
  }
  
  public Stage showDialog(Stage owner, StageStyle style, Modality modality, EventHandler<ActionEvent> onOK) {
    final Stage stage = new Stage();
    if (owner != null) {
      stage.initOwner(owner);
    }
    stage.initStyle(style);
    stage.initModality(modality);
    final BorderPane root = new BorderPane();
    root.setPadding(new Insets(10));
    root.setCenter(getView());
    final HBox buttons = new HBox(5);
    buttons.setPadding(new Insets(10,  10,  0, 10));
    buttons.setAlignment(Pos.CENTER);
    final Button okButton = new Button("OK");
    final Button cancelButton = new Button("Cancel");
    if (onOK != null) {
      okButton.addEventHandler(ActionEvent.ACTION, onOK);
    }
    okButton.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        stage.hide();
      }
    });
    cancelButton.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        person.setFirstName(initialFirstName);
        person.setLastName(initialLastName);
        person.setEmail(initialEmail);
        stage.hide();
      }
    });
    buttons.getChildren().addAll(okButton, cancelButton);
    root.setBottom(buttons);
    final Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
    return stage ;
  }
  
  public Stage showDialog(Stage owner, StageStyle style, Modality modality) {
    return showDialog(owner, style, modality, null);
  }
  
  public Stage showDialog(Stage owner, EventHandler<ActionEvent> onOK) {
    return showDialog(owner, StageStyle.DECORATED, Modality.NONE, onOK);
  }
  
  public Stage showDialog(Stage owner) {
    return showDialog(owner, null);
  }
  
  public Stage showDialog() {
    return showDialog(null);
  }
}
