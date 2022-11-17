// tabs=4
//************************************************************
//	COPYRIGHT 2021, Kyle D. Adams, Matthew E. Morgan and
//   Sandeep Mitra, State University of New York. - Brockport
//   (SUNY Brockport)
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

import model.PerformanceCategory;
import model.PerformanceCategoryCollection;


//==============================================================================
public class PerformanceCategoryCollectionView extends View
{
    protected TableView<PerformanceCategoryTableModel> tableOfCats;
    protected Button cancelButton;
    protected Button submitButton;
    protected MessageView statusLog;
    protected Text actionText;

    //--------------------------------------------------------------------------
    public PerformanceCategoryCollectionView(IModel mot)
    {
        // mot - model - Modify Offering Transaction acronym
        super(mot, "PerformanceCategoryCollectionView");

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
        tableOfCats.getSelectionModel().select(0); //autoselect first element
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

        ObservableList<PerformanceCategoryTableModel> tableData = FXCollections.observableArrayList();
        try
        {
            PerformanceCategoryCollection catCollection =
                    (PerformanceCategoryCollection) myModel.getState("PerformanceCategoryList");

            Vector entryList = (Vector)catCollection.getState("Categories");

            if (entryList.size() > 0)
            {

                Enumeration entries = entryList.elements();

                while (entries.hasMoreElements() == true)
                {

                    PerformanceCategory nextCat = (PerformanceCategory) entries.nextElement();
                    Vector<String> view = nextCat.getEntryListView();

                    // add this list entry to the list
                    PerformanceCategoryTableModel nextTableRowData = new PerformanceCategoryTableModel(view);
                    tableData.add(nextTableRowData);

                }
                if(entryList.size() == 1)
                    actionText.setText(entryList.size()+" Matching Performance Categories Found!");
                else
                    actionText.setText(entryList.size()+" Matching Performance Categories Found!");

                actionText.setFill(Color.LIGHTGREEN);
            }
            else
            {

                actionText.setText("No matching Performance Categories Found!");
                actionText.setFill(Color.FIREBRICK);
            }

            tableOfCats.setItems(tableData);
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

        Text prompt = new Text("Select a Performance Category");
        prompt.setWrappingWidth(400);
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setFont(Font.font("Copperplate", FontWeight.BOLD, 18));
        prompt.setFill(Color.BLACK);
        grid.add(prompt, 0, 0, 2, 1);



        tableOfCats = new TableView<PerformanceCategoryTableModel>();
        tableOfCats.setEffect(new DropShadow());
        tableOfCats.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-selection-bar: gold;");
        tableOfCats.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);


        TableColumn catNumberColumn = new TableColumn("Number") ;
        catNumberColumn.setMinWidth(75);
        catNumberColumn.setCellValueFactory(
                new PropertyValueFactory<PerformanceCategoryTableModel, String>("catNumber"));

        TableColumn catNameColumn = new TableColumn("Category Name") ;
        catNameColumn.setMinWidth(75);
        catNameColumn.setCellValueFactory(
                new PropertyValueFactory<PerformanceCategoryTableModel, String>("catName"));

        tableOfCats.getColumns().addAll(catNumberColumn,
                catNameColumn);

        tableOfCats.setOnMousePressed((MouseEvent event) -> {
            if (event.isPrimaryButtonDown() && event.getClickCount() >=2 ){

                processPerformanceCategorySelected();
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

            processPerformanceCategorySelected();
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

            myModel.stateChangeRequest("CancelModifyPerformanceCategory", null);
        });
        cancelButton.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
            cancelButton.setEffect(new DropShadow());
        });
        cancelButton.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
            cancelButton.setEffect(null);
        });
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

        actionText = new Text();//text set later
        actionText.setFont(Font.font("Copperplate", FontWeight.BOLD, 18));
        actionText.setWrappingWidth(350);
        actionText.setTextAlignment(TextAlignment.CENTER);

        vbox.getChildren().add(grid);
        tableOfCats.setPrefHeight(200);
        tableOfCats.setMaxWidth(197);
        vbox.getChildren().add(tableOfCats);
        vbox.getChildren().add(btnContainer);
        vbox.getChildren().add(actionText);
        vbox.setPadding(new Insets(10,10,10,10));
        vbox.setAlignment(Pos.CENTER);

        return vbox;
    }

    //--------------------------------------------------------------------------
    protected void processPerformanceCategorySelected()
    {
        PerformanceCategoryTableModel selectedItem = tableOfCats.getSelectionModel().getSelectedItem();

        if(selectedItem != null)
        {
            String selectedId = selectedItem.getId();
            // DEBUG System.out.println(selectedItem.getCatNumber());

            myModel.stateChangeRequest("CategorySelected", selectedId);
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
