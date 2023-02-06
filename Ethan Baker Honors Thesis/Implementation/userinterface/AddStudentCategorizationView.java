// tabs=4
//************************************************************
//	COPYRIGHT 2022, Ethan L. Baker, Matthew E. Morgan and
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
import javafx.event.Event;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

// project imports
import impresario.IModel;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import model.SLO;
import model.SLOCollection;

/** The class containing the Student Categorization and Reflection Choice View for the Gen Ed Assessment Data
 *  Management application
 */
//==============================================================
public class AddStudentCategorizationView extends View {
    //GUI components
    protected Text genEdArea;
    protected Text semester;
    protected Text catName1;
    protected Text catName2;
    protected Text catName3;
    protected Text catName4;
    protected Button submitButton;
    protected Button cancelButton;
    protected TableView<SLOTableModel> tableOfSLOs;

    protected TextField fr1;
    protected TextField fr2;
    protected TextField fr3;
    protected TextField fr4;
    protected TextField so1;
    protected TextField so2;
    protected TextField so3;
    protected TextField so4;
    protected TextField jr1;
    protected TextField jr2;
    protected TextField jr3;
    protected TextField jr4;
    protected TextField sr1;
    protected TextField sr2;
    protected TextField sr3;
    protected TextField sr4;

    // other GUI Components here
    protected MessageView statusLog;
    protected DropShadow shadow = new DropShadow();

    protected String keyToSendWithData = "";

    // constructor for this class -- takes a model object
    //----------------------------------------------------------
    public AddStudentCategorizationView(IModel StudentCategorization) {

        super(StudentCategorization, "AddStudentCategorizationView");

        // create a container for showing the contents
        VBox container = new VBox(10);
        container.setPadding(new Insets(15, 5, 5, 5));
        container.setStyle("-fx-background-color: slategrey");

        // Add a title for this panel
        container.getChildren().add(createTitle());

        // how do you add white space?
        //container.getChildren().add(new Label(" "));

        // create our GUI components, add them to this Container
        container.getChildren().add(createFormContents());
        container.getChildren().add(createStatusLog("                               "));

        container.getChildren().add(createCopyrightPanel());
        container.getChildren().add(new Text("          ")); // Blank space to handle scroll down


        // container.setMinHeight(550);
        //container.setMinWidth(550);

        getChildren().add(container);

        populateFields();
        cancelButton.requestFocus();
        tableOfSLOs.getSelectionModel().select(0); //autoselect first element

        keyToSendWithData = "StudentCategorizationData";
        myModel.subscribe("TransactionError", this);
    }

    // Create the labels and fields
    //-------------------------------------------------------------
    private VBox createTitle() {

        return new CommonTitleWithoutLogoPanel();
    }

    //---------------------------------------------------------
    protected String getPromptText() {
        return "Enter student performance data:";
    }

    //--------------------------------------------------------------------------
    protected void getEntryTableModelValues()
    {

        ObservableList<SLOTableModel> tableData = FXCollections.observableArrayList();
        try
        {
            SLOCollection mySLOCollection =
                    (SLOCollection)myModel.getState("SLOList");

            Vector entryList = (Vector)mySLOCollection.getState("SLOs");

            if (entryList.size() > 0)
            {

                Enumeration entries = entryList.elements();

                while (entries.hasMoreElements() == true)
                {

                    SLO nextSLO = (SLO)entries.nextElement();
                    Vector<String> view = nextSLO.getEntryListView();

                    // add this list entry to the list
                    SLOTableModel nextTableRowData = new SLOTableModel(view);
                    tableData.add(nextTableRowData);

                }
                if(entryList.size() == 1) {
                    //actionText.setText(entryList.size()+" Matching SLO Found!");
                }
                else {
                    //actionText.setText(entryList.size()+" Matching SLO Found!");
                }

                //actionText.setFill(Color.LIGHTGREEN);
            }
            else
            {
                //actionText.setText("No matching SLOs Found!");
                //actionText.setFill(Color.FIREBRICK);
            }

            tableOfSLOs.setItems(tableData);
        }
        catch (Exception e) {//SQLException e) {
            // Need to handle this exception
            System.out.println(e);
            e.printStackTrace();
        }

    }

    //------------------------------------------------------------
    protected VBox createFormContents() {
        VBox vbox = new VBox(10);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

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
        grid.add(semester = new Text(), 1, 2);

        Text sloLabel = new Text(" Choose an SLO : ");
        sloLabel.setFont(myFont);
        sloLabel.setWrappingWidth(150);
        sloLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(sloLabel,0,3);

        tableOfSLOs = new TableView<SLOTableModel>();
        tableOfSLOs.setEffect(new DropShadow());
        tableOfSLOs.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-selection-bar: gold;");
        tableOfSLOs.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        TableColumn sloCol = new TableColumn("SLO Text") ;
        sloCol.setMinWidth(440);
        sloCol.setCellValueFactory(
                new PropertyValueFactory<GenEdAreaTableModel, String>("sloText"));

        tableOfSLOs.getColumns().add(sloCol);

        tableOfSLOs.setOnMousePressed((MouseEvent event) -> {
            if (event.isPrimaryButtonDown() && event.getClickCount() >=2 ){

                //processSLOSelected();
            }
        });

        Text prompt = new Text(getPromptText());
        prompt.setFont(Font.font("Copperplate", FontWeight.BOLD, 18));
        prompt.setWrappingWidth(400);
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setFill(Color.BLACK);
        //grid.add(prompt, 0, 0, 2, 1);


        GridPane grid2 = new GridPane();
        grid2.setAlignment(Pos.CENTER);
        grid2.setHgap(10);
        grid2.setVgap(10);
        grid2.setPadding(new Insets(10, 10, 10, 10));

        grid2.add(catName1 = new Text(), 1, 0);
        grid2.add(catName2 = new Text(), 2, 0);
        grid2.add(catName3 = new Text(), 3, 0);
        grid2.add(catName4 = new Text(), 4, 0);


        Text freshmen = new Text(" Freshmen : ");
        myFont = Font.font("Helvetica", FontWeight.BOLD, 12);
        freshmen.setFont(myFont);
        freshmen.setWrappingWidth(150);
        freshmen.setTextAlignment(TextAlignment.RIGHT);
        grid2.add(freshmen, 0,1);
        grid2.add( fr1 = new TextField(),1 , 1);
        fr1.setPrefWidth(40);
        fr1.setMaxWidth(40);
        grid2.add(fr2 = new TextField(),2,1);
        fr2.setPrefWidth(40);
        fr2.setMaxWidth(40);
        grid2.add(fr3 = new TextField(),3,1);
        fr3.setPrefWidth(40);
        fr3.setMaxWidth(40);
        grid2.add(fr4 = new TextField(),4,1);
        fr4.setPrefWidth(40);
        fr4.setMaxWidth(40);

        Text sophomore = new Text(" Sophomore : ");
        myFont = Font.font("Helvetica", FontWeight.BOLD, 12);
        sophomore.setFont(myFont);
        sophomore.setWrappingWidth(150);
        sophomore.setTextAlignment(TextAlignment.RIGHT);
        grid2.add(sophomore, 0,2);
        grid2.add(so1 = new TextField(),1 , 2);
        so1.setPrefWidth(40);
        so1.setMaxWidth(40);
        grid2.add(so2 = new TextField(),2,2);
        so2.setPrefWidth(40);
        so2.setMaxWidth(40);
        grid2.add(so3 = new TextField(),3,2);
        so3.setPrefWidth(40);
        so3.setMaxWidth(40);
        grid2.add(so4 = new TextField(),4,2);
        so4.setPrefWidth(40);
        so4.setMaxWidth(40);

        Text junior = new Text(" Junior : ");
        myFont = Font.font("Helvetica", FontWeight.BOLD, 12);
        junior.setFont(myFont);
        junior.setWrappingWidth(150);
        junior.setTextAlignment(TextAlignment.RIGHT);
        grid2.add(junior, 0,3);
        grid2.add(jr1 = new TextField(),1 , 3);
        jr1.setPrefWidth(40);
        jr1.setMaxWidth(40);
        grid2.add(jr2 = new TextField(),2,3);
        jr2.setPrefWidth(40);
        jr2.setMaxWidth(40);
        grid2.add(jr3 = new TextField(),3,3);
        jr3.setPrefWidth(40);
        jr3.setMaxWidth(40);
        grid2.add(jr4 = new TextField(),4,3);
        jr4.setPrefWidth(40);
        jr4.setMaxWidth(40);

        Text senior = new Text(" Senior : ");
        myFont = Font.font("Helvetica", FontWeight.BOLD, 12);
        senior.setFont(myFont);
        senior.setWrappingWidth(150);
        senior.setTextAlignment(TextAlignment.RIGHT);
        grid2.add(senior, 0,4);
        grid2.add(sr1 = new TextField(),1 , 4);
        sr1.setPrefWidth(40);
        sr1.setMaxWidth(40);
        grid2.add(sr2 = new TextField(),2,4);
        sr2.setPrefWidth(40);
        sr2.setMaxWidth(40);
        grid2.add(sr3 = new TextField(),3,4);
        sr3.setPrefWidth(40);
        sr3.setMaxWidth(40);
        grid2.add(sr4 = new TextField(),4,4);
        sr4.setPrefWidth(40);
        sr4.setMaxWidth(40);

        ImageView icon = new ImageView(new Image("/images/pluscolor.png"));
        icon.setFitHeight(15);
        icon.setFitWidth(15);
        submitButton = new Button("Add", icon);
        submitButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
        submitButton.setOnAction((ActionEvent e) -> {
            /*clearErrorMessage();
            clearOutlines();

            String curseCode = (String) crsCode.getText();
            String courseNumber = number.getText();

            try {
                if (!(curseCode.length() > 0))
                {
                    crsCode.setStyle("-fx-border-color: firebrick;");
                    displayErrorMessage("ERROR: Please enter a proper course code");
                }
                else
                {
                    Properties props = new Properties();
                    props.setProperty("CourseDisciplineCode", curseCode);
                    props.setProperty("CourseNumber", courseNumber);
                    myModel.stateChangeRequest(keyToSendWithData, props);
                }
            }
            catch (Exception ex)
            {
                number.setStyle("-fx-border-color: firebrick;");
                displayErrorMessage("ERROR: Please enter a proper course number");
            }
            */

        });

        submitButton.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
            submitButton.setEffect(new DropShadow());
        });
        submitButton.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
            submitButton.setEffect(null);
        });

        HBox buttonContainer = new HBox(10);
        buttonContainer.setAlignment(Pos.CENTER);


        buttonContainer.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
            buttonContainer.setStyle("-fx-background-color: GOLD");
        });
        buttonContainer.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
            buttonContainer.setStyle("-fx-background-color: SLATEGREY");
        });
        icon = new ImageView(new Image("/images/return.png"));
        icon.setFitHeight(15);
        icon.setFitWidth(15);
        cancelButton = new Button("Return", icon);
        cancelButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                clearErrorMessage();
                myModel.stateChangeRequest("CancelAddStudentCategorizationData", null);
            }
        });
        cancelButton.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
            cancelButton.setEffect(new DropShadow());
        });
        cancelButton.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
            cancelButton.setEffect(null);
        });
        buttonContainer.getChildren().add(submitButton);
        buttonContainer.getChildren().add(cancelButton);

        vbox.getChildren().add(grid);
        tableOfSLOs.setPrefHeight(200);
        tableOfSLOs.setPrefWidth(300);
        vbox.getChildren().add(tableOfSLOs);
        vbox.getChildren().add(prompt);
        vbox.getChildren().add(grid2);
        vbox.getChildren().add(buttonContainer);

        return vbox;
    }

    // Create the status log field
    //-------------------------------------------------------------
    private MessageView createStatusLog(String initialMessage) {

        statusLog = new MessageView(initialMessage);

        return statusLog;
    }

    // Create Copyright Panel
    //----------------------------------------------------------
    private Text createCopyrightPanel() {
        return new CopyrightPanel();
    }

    //-------------------------------------------------------------
    public void populateFields() {
        getEntryTableModelValues();
        String genEd = (String)myModel.getState("GenEdAreaData");
        String sem = (String)myModel.getState("SemData");
        if (genEd != null)
        {
            genEdArea.setText(genEd);
        }
        if (sem != null)
        {
            semester.setText(sem);
        }
        catName1.setText("Test1");
        catName2.setText("Test2");
        catName3.setText("Test3");
        catName4.setText("Test4");
    }
    //-------------------------------------------------------
    protected void clearOutlines() {
        //number.setStyle("-fx-border-color: transparent; -fx-focus-color: darkgreen;");
    }

    //---------------------------------------------------------
    public void updateState(String key, Object value) {
        if (key.equals("TransactionError") == true) {
            // display the passed text
            String message = (String) value;
            if ((message.startsWith("Err")) || (message.startsWith("ERR"))) {
                displayErrorMessage(message);
            } else {
                displayMessage(message);
            }

        }
    }

    /**
     * Display error message
     */
    //----------------------------------------------------------
    public void displayErrorMessage(String message) {
        statusLog.displayErrorMessage(message);
    }

    /**
     * Display Informational message
     */
    //----------------------------------------------------------
    public void displayMessage(String message) {
        statusLog.displayMessage(message);
    }

    /**
     * Clear GUI Values
     */
    //----------------------------------------------------------
    public void clearData() {
        //number.setText("");

    }
    /**
     * Clear error message
     */
    //----------------------------------------------------------
    public void clearErrorMessage() {
        statusLog.clearErrorMessage();

    }
}