// tabs=4
//************************************************************
//	COPYRIGHT 2023, Ethan L. Baker, Matthew E. Morgan and
//  Sandeep Mitra, State University of New York. - Brockport
//  (SUNY Brockport)
//	ALL RIGHTS RESERVED
//
// This file is the product of SUNY Brockport and cannot
// be reproduced, copied, or used in any shape or form without
// the express written consent of SUNY Brockport.
//************************************************************
//
// specify the package
package userinterface;

// system imports
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.Vector;
import java.util.Enumeration;

// project imports
import impresario.IModel;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;

import model.ReflectionQuestion;
import model.ReflectionQuestionCollection;

/** The class containing the Reflection Question Collection View for the Gen Ed Assessment Data
 *  Management application
 */
//==============================================================================
public class ReflectionQuestionCollectionView extends View {
    protected Text promptText;
    protected TableView<ReflectionQuestionTableModel> tableOfReflectionQuestions;
    protected Button cancelButton;
    protected Button submitButton;
    protected MessageView statusLog;
    protected Text actionText;

    //--------------------------------------------------------------------------
    public ReflectionQuestionCollectionView(IModel mislot)
    {
        // mislot - model - Modify ISLO Transaction acronym -> Rename
        super(mislot, "ReflectionQuestionCollectionView");

        // create a container for showing the contents
        VBox container = new VBox(10);
        container.setStyle("-fx-background-color: slategrey");
        container.setPadding(new Insets(15, 5, 5, 5));

        // create our GUI components, add them to this panel
        container.getChildren().add(createTitle());
        container.getChildren().add(createFormContent());

        // Error message area
        container.getChildren().add(createStatusLog("                                            "));
        container.getChildren().add(createCopyrightPanel());

        getChildren().add(container);
        populateFields();
        tableOfReflectionQuestions.getSelectionModel().select(0); //autoselect first element
    }


    // Create Copyright Panel
    //----------------------------------------------------------
    private Text createCopyrightPanel()
    {
        return new CopyrightPanel();
    }


    //--------------------------------------------------------------------------
    protected void populateFields()
    {
        getEntryTableModelValues();
    }

    //--------------------------------------------------------------------------
    protected void getEntryTableModelValues()
    {

        ObservableList<ReflectionQuestionTableModel> tableData = FXCollections.observableArrayList();
        try
        {
            ReflectionQuestionCollection reflectionQuestionCollection =
                    (ReflectionQuestionCollection)myModel.getState("ReflectionQuestionList");

            Vector entryList = (Vector)reflectionQuestionCollection.getState("ReflectionQuestions");

            if (entryList.size() > 0)
            {

                Enumeration entries = entryList.elements();

                while (entries.hasMoreElements() == true)
                {

                    ReflectionQuestion nextReflectionQuestion = (ReflectionQuestion)entries.nextElement();
                    Vector<String> view = nextReflectionQuestion.getEntryListView();

                    // add this list entry to the list
                    ReflectionQuestionTableModel nextTableRowData = new ReflectionQuestionTableModel(view);
                    tableData.add(nextTableRowData);

                }
                if(entryList.size() == 1)
                    actionText.setText(entryList.size()+" Matching Reflection Question Found!");
                else
                    actionText.setText(entryList.size()+" Matching Reflection Questions Found!");

                actionText.setFill(Color.LIGHTGREEN);
            }
            else
            {

                actionText.setText("No matching Reflection Questions Found!");
                actionText.setFill(Color.FIREBRICK);
            }

            tableOfReflectionQuestions.setItems(tableData);
        }
        catch (Exception e) {//SQLException e) {
            // Need to handle this exception
            System.out.println(e);
            e.printStackTrace();
        }

    }

    // Create the title container
    //-------------------------------------------------------------
    private Node createTitle()
    {
        return new CommonTitleWithoutLogoPanel();
    }


    //---------------------------------------------------------
    protected String getPromptText() {
        return "Select a Reflection Question:";
    }

    // Create the main form content
    //-------------------------------------------------------------
    private VBox createFormContent()
    {
        VBox vbox = new VBox(10);
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 25, 10, 0));

        promptText = new Text(getPromptText());//text set later
        promptText.setFont(Font.font("Copperplate", FontWeight.BOLD, 18));
        promptText.setWrappingWidth(350);
        promptText.setTextAlignment(TextAlignment.CENTER);
        vbox.getChildren().add(promptText);

        tableOfReflectionQuestions = new TableView<ReflectionQuestionTableModel>();
        tableOfReflectionQuestions.setEffect(new DropShadow());
        tableOfReflectionQuestions.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-selection-bar: gold;");
        tableOfReflectionQuestions.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        TableColumn reflectionQuestionTextColumn = new TableColumn("Text") ;
        reflectionQuestionTextColumn.setMinWidth(540);
        reflectionQuestionTextColumn.setCellValueFactory(
                new PropertyValueFactory<ReflectionQuestionTableModel, String>("reflectionQuestionText"));

        tableOfReflectionQuestions.getColumns().addAll(
                reflectionQuestionTextColumn);

        tableOfReflectionQuestions.setOnMousePressed((MouseEvent event) -> {
            if (event.isPrimaryButtonDown() && event.getClickCount() >=2 ){

                processReflectionQuestionSelected();
            }
        });
        ImageView icon = new ImageView(new Image("/images/check.png"));
        icon.setFitHeight(15);
        icon.setFitWidth(15);
        submitButton = new Button("Select",icon);
        submitButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
        submitButton.requestFocus();
        submitButton.setOnAction((ActionEvent e) -> {
            clearErrorMessage();
            // do the inquiry

            processReflectionQuestionSelected();
        });
        submitButton.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
            submitButton.setEffect(new DropShadow());
        });
        submitButton.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
            submitButton.setEffect(null);
        });
        icon = new ImageView(new Image("/images/return.png"));
        icon.setFitHeight(15);
        icon.setFitWidth(15);
        cancelButton = new Button("Return", icon);
        cancelButton.setGraphic(icon);
        cancelButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
        cancelButton.setOnAction((ActionEvent e) -> {
            clearErrorMessage();

            myModel.stateChangeRequest("CancelQuestionList", null);
        });
        cancelButton.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
            cancelButton.setEffect(new DropShadow());
        });
        cancelButton.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
            cancelButton.setEffect(null);
        });
		/*Button saveToFileButton = new Button("Save to Excel File");
		saveToFileButton.setOnAction((ActionEvent e) -> { saveToExcelFile(MainStageContainer.getInstance());} );*/
        HBox btnContainer = new HBox(10);
        btnContainer.setAlignment(Pos.CENTER);
        btnContainer.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
            btnContainer.setStyle("-fx-background-color: GOLD");
        });
        btnContainer.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
            btnContainer.setStyle("-fx-background-color: SLATEGREY");
        });
        btnContainer.getChildren().add(submitButton);
        btnContainer.getChildren().add(cancelButton);
        //btnContainer.getChildren().add(saveToFileButton);

        actionText = new Text();//text set later
        actionText.setFont(Font.font("Copperplate", FontWeight.BOLD, 18));
        actionText.setWrappingWidth(350);
        actionText.setTextAlignment(TextAlignment.CENTER);

        vbox.getChildren().add(grid);
        tableOfReflectionQuestions.setPrefHeight(200);
        tableOfReflectionQuestions.setPrefWidth(550);
        vbox.getChildren().add(tableOfReflectionQuestions);
        vbox.getChildren().add(btnContainer);
        vbox.getChildren().add(actionText);
        vbox.setPadding(new Insets(10,10,10,10));
        vbox.setAlignment(Pos.CENTER);

        return vbox;
    }

    //--------------------------------------------------------------------------
    protected void processReflectionQuestionSelected()
    {
        ReflectionQuestionTableModel selectedItem = tableOfReflectionQuestions.getSelectionModel().getSelectedItem();

        if(selectedItem != null)
        {
            String selectedReflectionQuestionText = selectedItem.getReflectionQuestionText();

            myModel.stateChangeRequest("ReflectionQuestionSelected", selectedReflectionQuestionText);
        }
    }

    //---------------------------------------------------------
    public void updateState(String key, Object value)
    {

    }

    //--------------------------------------------------------------------------
    protected MessageView createStatusLog(String initialMessage)
    {
        statusLog = new MessageView(initialMessage);

        return statusLog;
    }


    /**
     * Display info message
     */
    //----------------------------------------------------------
    public void displayMessage(String message)
    {
        statusLog.displayMessage(message);
    }

    /**
     * Clear error message
     */
    //----------------------------------------------------------
    public void clearErrorMessage()
    {
        statusLog.clearErrorMessage();
    }
}
