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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.Vector;
import java.util.Enumeration;

// project imports
import impresario.IModel;
import javafx.scene.effect.DropShadow;

import model.AssessmentTeamClasses;
import model.AssessmentTeamClassesCollection;

//==============================================================================
public class AssessmentTeamClassesCollectionView extends View
{
    protected TableView<AssessmentTeamClassesTableModel> tableOfATCs;
    protected Button cancelButton;
    protected Button submitButton;
    protected MessageView statusLog;
    protected Text actionText;
    protected Text genEdArea;
    protected Text sem;
    //--------------------------------------------------------------------------
    public AssessmentTeamClassesCollectionView(IModel modt)
    {
        // mdot - model - Modify Offering Display Transaction acronym
        super(modt, "AssessmentTeamClassesCollectionView");

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
        container.getChildren().add(new Text());

        getChildren().add(container);

        populateFields();

        tableOfATCs.getSelectionModel().select(0); //autoselect first element
        myModel.subscribe("TransactionError", this);
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

        String genEd = (String)myModel.getState("GenEdAreaData");
        String semester = (String)myModel.getState("SemData");
        if (genEd != null)
        {
            genEdArea.setText(genEd);
        }
        if (semester != null)
        {
            sem.setText(semester);
        }

    }

    //--------------------------------------------------------------------------
    protected void getEntryTableModelValues()
    {

        ObservableList<AssessmentTeamClassesTableModel> tableData = FXCollections.observableArrayList();
        try
        {
            AssessmentTeamClassesCollection adCollection =
                    (AssessmentTeamClassesCollection)myModel.getState("AssessmentTeamClassesDisplayList");

            Vector entryList = adCollection.getAssessmentTeamClassesDisplays(); //needs to be changed

            if (entryList.size() > 0)
            {

                Enumeration entries = entryList.elements();

                while (entries.hasMoreElements() == true)
                {

                    AssessmentTeamClasses nextOT = (AssessmentTeamClasses) entries.nextElement();
                    Vector<String> view = nextOT.getEntryListView();

                    // add this list entry to the list
                    AssessmentTeamClassesTableModel nextTableRowData = new AssessmentTeamClassesTableModel(view);
                    tableData.add(nextTableRowData);

                }
                if(entryList.size() == 1)
                    actionText.setText(entryList.size()+" Matching Assessment Team Class Found!");
                else
                    actionText.setText(entryList.size()+" Matching Assessment Team Classes Found!");

                actionText.setFill(Color.LIGHTGREEN);
            }
            else
            {

                actionText.setText("No matching Assessment Team Classes Found!");
                actionText.setFill(Color.FIREBRICK);
            }

            tableOfATCs.setItems(tableData);
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
        return "Select an assessment team class to modify/delete for:";
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

        Text promptText = new Text(getPromptText());//text set later
        promptText.setFont(Font.font("Copperplate", FontWeight.BOLD, 18));
        promptText.setWrappingWidth(350);
        promptText.setTextAlignment(TextAlignment.CENTER);
        vbox.getChildren().add(promptText);

        Text genEdLabel = new Text(" Gen Ed Area : ");
        Font myFont = Font.font("Helvetica", FontWeight.BOLD, 12);
        genEdLabel.setFont(myFont);
        genEdLabel.setWrappingWidth(150);
        genEdLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(genEdLabel, 0, 1);
        grid.add(genEdArea = new Text(), 1, 1);

        Text semLabel = new Text(" Semester : ");
        semLabel.setFont(myFont);
        semLabel.setWrappingWidth(150);
        semLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(semLabel, 0, 2);
        grid.add(sem = new Text(), 1, 2);

        tableOfATCs = new TableView<AssessmentTeamClassesTableModel>();
        tableOfATCs.setEffect(new DropShadow());
        tableOfATCs.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-selection-bar: gold;");
        tableOfATCs.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);


        TableColumn cCodeColumn = new TableColumn("Course Code") ;
        cCodeColumn.setMinWidth(150);
        cCodeColumn.setCellValueFactory(
                new PropertyValueFactory<AssessmentTeamClassesTableModel, String>("courseDisciplineCode"));

        TableColumn cNumColumn = new TableColumn("Course Number") ;
        cNumColumn.setMinWidth(150);
        cNumColumn.setCellValueFactory(
                new PropertyValueFactory<AssessmentTeamClassesTableModel, String>("courseNumber"));

        tableOfATCs.getColumns().addAll(cCodeColumn, cNumColumn);

        tableOfATCs.setOnMousePressed((MouseEvent event) -> {
            if (event.isPrimaryButtonDown() && event.getClickCount() >=2 ){

                processOTSelected();
            }
        });
        ImageView icon = new ImageView(new Image(getSubmitButtonIcon()));
        icon.setFitHeight(15);
        icon.setFitWidth(15);
        submitButton = new Button(getSubmitButtonText(),icon);
        submitButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
        submitButton.requestFocus();
        submitButton.setOnAction((ActionEvent e) -> {
            clearErrorMessage();
            processWriteToExcelFile();
            // do the inquiry

            processOTSelected();
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

            myModel.stateChangeRequest("CancelAssessmentTeamClassesList", null);
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
        tableOfATCs.setPrefHeight(200);
        tableOfATCs.setMaxWidth(300);
        vbox.getChildren().add(tableOfATCs);
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
        //saveToExcelFile();
    }

    //-------------------------------------------------------------
    protected void writeToFile(String fName){

    }

    //--------------------------------------------------------------------------
    protected void processOTSelected()
    {
        AssessmentTeamClassesTableModel selectedItem = tableOfATCs.getSelectionModel().getSelectedItem();

        if(selectedItem != null)
        {
            String selectedOTId = selectedItem.getId();

            myModel.stateChangeRequest("AssessmentTeamClassSelected", selectedOTId);
        }
    }

    //---------------------------------------------------------
    public void updateState(String key, Object value)
    {
        if (key.equals("TransactionError") == true)
        {
            String val = (String)value;
            if ((val.startsWith("ERR") == true) || (val.startsWith("Err") == true))
            {
                displayErrorMessage(val);
            }
            else
            {
                displayMessage(val);
            }
        }
    }

    //--------------------------------------------------------------------------
    protected String getSubmitButtonText(){
        return "Select";
    }

    //--------------------------------------------------------------------------
    protected String getSubmitButtonIcon(){
        return "/images/check.png";
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

    //----------------------------------------------------------
    public void displayErrorMessage(String message)
    {
        statusLog.displayErrorMessage(message);
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
