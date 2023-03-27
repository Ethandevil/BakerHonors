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
import javafx.scene.control.*;
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

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import java.util.Enumeration;

// project imports
import impresario.IModel;
import javafx.application.Platform;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;

import model.*;

import javax.swing.*;

public class InstructorReflectionsCollectionView extends View{
    protected MessageView statusLog;
    protected Text promptText;
    protected Button submitButton;
    protected Button cancelButton;
    protected Text actionText;
    protected TextArea reflectionText;
    protected TableView<InstructorReflectionsDisplayTableModel> tableofIRs;

    //--------------------------------------------------------------------------
    public InstructorReflectionsCollectionView(IModel mst)
    {
        // mst - model - Modify Semester Transaction acronym
        super(mst, "StudentCategorizationDisplayCollectionView");

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
        container.getChildren().add(new Text("              ")); // To handle large error/info messages
        // for this rather narrow screen

        getChildren().add(container);
        populateFields();
        myModel.subscribe("StudentCategorizationUpdated",this);

        tableofIRs.getSelectionModel().select(0); //autoselect first element
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

        ObservableList<InstructorReflectionsDisplayTableModel> tableData = FXCollections.observableArrayList();
        try
        {
            InstructorReflectionsDisplayCollection irdCollection =
                    (InstructorReflectionsDisplayCollection) myModel.getState("InstructorReflectionsDisplayList");

            Vector entryList = irdCollection.getInstructorReflectionDisplays();

            if (entryList.size() > 0)
            {

                Enumeration entries = entryList.elements();

                while (entries.hasMoreElements() == true)
                {

                    InstructorReflectionsDisplay nextIRD = (InstructorReflectionsDisplay) entries.nextElement();
                    Vector<String> view = nextIRD.getEntryListView();

                    // add this list entry to the list
                    InstructorReflectionsDisplayTableModel nextTableRowData = new InstructorReflectionsDisplayTableModel(view);
                    tableData.add(nextTableRowData);

                }
                if(entryList.size() == 1)
                    actionText.setText(entryList.size()+" Matching Gen Ed Area-Semester Link Found!");
                else
                    actionText.setText(entryList.size()+" Matching Gen Ed Area-Semester Links Found!");

                actionText.setFill(Color.LIGHTGREEN);
            }
            else
            {

                actionText.setText("No matching Gen Ed Area-Semester Links Found!");
                actionText.setFill(Color.FIREBRICK);
            }

            tableofIRs.setItems(tableData);
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
        return "Report for Gen Ed Area: " + myModel.getState("AreaName") +
                " assessed in Semester: " + myModel.getState("Semester");
    }

    // Create the main form content
    //-------------------------------------------------------------
    private VBox createFormContent()
    {
        tableofIRs = new TableView<InstructorReflectionsDisplayTableModel>();
        tableofIRs.setEffect(new DropShadow());
        tableofIRs.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-selection-bar: gold;");
        tableofIRs.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);


        TableColumn questionTextColumn = new TableColumn("Question Text");
        questionTextColumn.setMinWidth(400);
        questionTextColumn.setCellValueFactory(
                new PropertyValueFactory<InstructorReflectionsDisplayTableModel, String>("questionText"));

        TableColumn reflectionTextColumn = new TableColumn("Reflection Text");
        reflectionTextColumn.setMinWidth(2000);
        reflectionTextColumn.setCellValueFactory(
                new PropertyValueFactory<InstructorReflectionsDisplayTableModel, String>("reflectionText"));

        tableofIRs.getColumns().addAll(questionTextColumn, reflectionTextColumn);

        tableofIRs.setOnMousePressed((MouseEvent event) -> {
            if (event.isPrimaryButtonDown() && event.getClickCount() >=2 ){

                //processODSelected();
            }
        });

        VBox vbox = new VBox(10);
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 25, 10, 0));

        //Font myFont = Font.font("Helvetica", FontWeight.BOLD, 16);

        /*reflectionText = new TextArea();
        reflectionText.setPrefColumnCount(50);
        reflectionText.setPrefRowCount(10);
        reflectionText.setWrapText(true);
        grid.add(reflectionText,0,0);*/
        //reflectionText.setPrefWidth(120);
        //reflectionText.setMaxWidth(120);



        promptText = new Text(getPromptText());//text set later
        promptText.setFont(Font.font("Copperplate", FontWeight.BOLD, 18));
        promptText.setWrappingWidth(350);
        promptText.setTextAlignment(TextAlignment.CENTER);
        vbox.getChildren().add(promptText);


        ImageView icon = new ImageView(new Image("/images/check.png"));
        icon.setFitHeight(15);
        icon.setFitWidth(15);
        submitButton = new Button("Write to Excel File",icon);
        submitButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
        submitButton.requestFocus();
        submitButton.setOnAction((ActionEvent e) -> {
            clearErrorMessage();
            // do the inquiry

            processWriteToExcelFile();
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

            myModel.stateChangeRequest("CancelStudentCategorizationList", null);
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

        tableofIRs.setPrefHeight(400);
        tableofIRs.setMaxWidth(1200);
        vbox.getChildren().add(tableofIRs);
        vbox.getChildren().add(grid);
        vbox.getChildren().add(btnContainer);
        vbox.getChildren().add(actionText);
        vbox.setPadding(new Insets(10,10,10,10));
        vbox.setAlignment(Pos.CENTER);

        return vbox;
    }

    //--------------------------------------------------------------------------
    protected void processWriteToExcelFile()
    {
        //BasicISLOReportDataSource birds = new BasicISLOReportDataSource(scCollection);
        saveToExcelFile();
    }

    //-------------------------------------------------------------
    protected void writeToFile(String fName)
    {
        try {
            FileWriter outFile = new FileWriter(fName);
            PrintWriter out = new PrintWriter(outFile);

            String line = "Report for Gen Ed Area: " + myModel.getState("AreaName") +
                    " assessed in Semester: " + myModel.getState("Semester");

            out.println(line);

            line = "";

            out.println(line);

            line = "Question Text,Reflection Text";

            out.println(line);

            line = ",";

            try
            {
                InstructorReflectionsDisplayCollection irdCollection =
                        (InstructorReflectionsDisplayCollection) myModel.getState("InstructorReflectionsDisplayList");

                Vector entryList = irdCollection.getInstructorReflectionDisplays();

                if (entryList.size() > 0)
                {
                    Enumeration entries = entryList.elements();

                    while (entries.hasMoreElements() == true)
                    {
                        InstructorReflectionsDisplay nextIRD = (InstructorReflectionsDisplay) entries.nextElement();
                        line = "";
                        line += nextIRD.getQuestionText().replace(",", " - ").replaceAll("\\s+", " ") + ",";
                        line += nextIRD.getReflectionText().replace(",", " - ").replaceAll("\\s+", " ");
                        out.println(line);
                    }
                }
            }
            catch (Exception e) {//SQLException e) {
                // Need to handle this exception
                System.out.println(e);
                e.printStackTrace();
            }

            // Finally, print the time-stamp
            DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
            DateFormat timeFormat = new SimpleDateFormat("hh:mm aaa");
            Date date = new Date();
            String timeStamp = dateFormat.format(date) + " " +
                    timeFormat.format(date);

            out.println("");
            out.println("Report created on " + timeStamp);

            out.close();
        }
        catch (FileNotFoundException e)
        {
            // Make these Alerts
            //JOptionPane.showMessageDialog(null, "Could not access file to save: "
            //      + fName, "Save Error", JOptionPane.ERROR_MESSAGE );
        }
        catch (IOException e)
        {
            //JOptionPane.showMessageDialog(null, "Error in saving to file: "
            //      + e.toString(), "Save Error", JOptionPane.ERROR_MESSAGE );

        }
    }

    //---------------------------------------------------------
    public void updateState(String key, Object value)
    {
        if(key.equals("StudentCategorizationUpdated")){
            //getGridValues();
        }
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
