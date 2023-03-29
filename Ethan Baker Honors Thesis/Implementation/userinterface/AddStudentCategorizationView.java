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
import javafx.scene.layout.*;
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
        Font myFont = Font.font("Helvetica", FontWeight.BOLD, 18);
        genEdLabel.setFont(myFont);
        genEdLabel.setWrappingWidth(150);
        genEdLabel.setTextAlignment(TextAlignment.RIGHT);
        genEdLabel.setFill(Color.GOLD);
        grid.add(genEdLabel, 0, 1);
        genEdArea = new Text();
        genEdArea.setFont(myFont);
        grid.add(genEdArea, 1, 1);

        Text semLabel = new Text(" Semester : ");
        semLabel.setFont(myFont);
        semLabel.setWrappingWidth(150);
        semLabel.setTextAlignment(TextAlignment.RIGHT);
        semLabel.setFill(Color.GOLD);
        grid.add(semLabel, 0, 2);
        semester = new Text();
        semester.setFont(myFont);
        grid.add(semester, 1, 2);

        GridPane grid1 = new GridPane();
        grid1.setAlignment(Pos.CENTER);
        grid1.setHgap(10);
        grid1.setVgap(10);
        grid1.setPadding(new Insets(25, 25, 25, 25));

        Text sloLabel = new Text(" Choose an SLO (double click) : ");
        Font myFont1 = Font.font("Helvetica", FontWeight.BOLD, 16);
        sloLabel.setFont(myFont1);
        sloLabel.setWrappingWidth(150);
        sloLabel.setTextAlignment(TextAlignment.RIGHT);
        sloLabel.setFill(Color.GOLD);
        grid1.add(sloLabel,0,0);

        tableOfSLOs = new TableView<SLOTableModel>();
        tableOfSLOs.setEffect(new DropShadow());
        tableOfSLOs.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-selection-bar: gold;");
        tableOfSLOs.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        TableColumn sloCol = new TableColumn("SLO Text") ;
        sloCol.setMinWidth(1000);
        sloCol.setCellValueFactory(
                new PropertyValueFactory<GenEdAreaTableModel, String>("sloText"));

        tableOfSLOs.getColumns().add(sloCol);

        tableOfSLOs.setOnMousePressed((MouseEvent event) -> {
            if (event.isPrimaryButtonDown() && event.getClickCount() >=2 ){

                processSLOSelected();
            }
        });
        //tableOfSLOs.setPrefHeight(200);
        //tableOfSLOs.setPrefWidth(300);
        ScrollPane s1 = new ScrollPane();
        s1.setPrefSize(700, 200);
        s1.setFitToHeight(true);
        s1.setContent(tableOfSLOs);
        grid1.add(s1, 1, 0);

        Text prompt = new Text(getPromptText());
        prompt.setFont(Font.font("Copperplate", FontWeight.BOLD, 18));
        prompt.setWrappingWidth(600);
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setFill(Color.BLACK);
        grid1.add(prompt, 1, 3, 2, 1);


        GridPane grid2 = new GridPane();
        grid2.setAlignment(Pos.CENTER);
        grid2.setHgap(10);
        grid2.setVgap(10);
        grid2.setPadding(new Insets(10, 50, 10, 50));

        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(20);
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPercentWidth(20);
        ColumnConstraints column3 = new ColumnConstraints();
        column3.setPercentWidth(20);
        ColumnConstraints column4 = new ColumnConstraints();
        column4.setPercentWidth(20);
        ColumnConstraints column5 = new ColumnConstraints();
        column5.setPercentWidth(20);

        grid2.getColumnConstraints().addAll(column1, column2, column3, column4, column5);


        grid2.add(catName1 = new Text(), 4, 0);
        grid2.add(catName2 = new Text(), 3, 0);
        grid2.add(catName3 = new Text(), 2, 0);
        grid2.add(catName4 = new Text(), 1, 0);


        Text freshmen = new Text(" Freshmen : ");
        myFont = Font.font("Helvetica", FontWeight.BOLD, 12);
        freshmen.setFont(myFont);
        freshmen.setWrappingWidth(150);
        freshmen.setTextAlignment(TextAlignment.RIGHT);
        grid2.add(freshmen, 0,1);
        grid2.add( fr1 = new TextField(),4 , 1);
        fr1.setPrefWidth(120);
        fr1.setMaxWidth(120);
        grid2.add(fr2 = new TextField(),3,1);
        fr2.setPrefWidth(120);
        fr2.setMaxWidth(120);
        grid2.add(fr3 = new TextField(),2,1);
        fr3.setPrefWidth(120);
        fr3.setMaxWidth(120);
        grid2.add(fr4 = new TextField(),1,1);
        fr4.setPrefWidth(120);
        fr4.setMaxWidth(120);

        Text sophomore = new Text(" Sophomore : ");
        myFont = Font.font("Helvetica", FontWeight.BOLD, 12);
        sophomore.setFont(myFont);
        sophomore.setWrappingWidth(150);
        sophomore.setTextAlignment(TextAlignment.RIGHT);
        grid2.add(sophomore, 0,2);
        grid2.add(so1 = new TextField(),4 , 2);
        so1.setPrefWidth(120);
        so1.setMaxWidth(120);
        grid2.add(so2 = new TextField(),3,2);
        so2.setPrefWidth(120);
        so2.setMaxWidth(120);
        grid2.add(so3 = new TextField(),2,2);
        so3.setPrefWidth(120);
        so3.setMaxWidth(120);
        grid2.add(so4 = new TextField(),1,2);
        so4.setPrefWidth(120);
        so4.setMaxWidth(120);

        Text junior = new Text(" Junior : ");
        myFont = Font.font("Helvetica", FontWeight.BOLD, 12);
        junior.setFont(myFont);
        junior.setWrappingWidth(150);
        junior.setTextAlignment(TextAlignment.RIGHT);
        grid2.add(junior, 0,3);
        grid2.add(jr1 = new TextField(),4 , 3);
        jr1.setPrefWidth(120);
        jr1.setMaxWidth(120);
        grid2.add(jr2 = new TextField(),3,3);
        jr2.setPrefWidth(120);
        jr2.setMaxWidth(120);
        grid2.add(jr3 = new TextField(),2,3);
        jr3.setPrefWidth(120);
        jr3.setMaxWidth(120);
        grid2.add(jr4 = new TextField(),1,3);
        jr4.setPrefWidth(120);
        jr4.setMaxWidth(120);

        Text senior = new Text(" Senior : ");
        myFont = Font.font("Helvetica", FontWeight.BOLD, 12);
        senior.setFont(myFont);
        senior.setWrappingWidth(150);
        senior.setTextAlignment(TextAlignment.RIGHT);
        grid2.add(senior, 0,4);
        grid2.add(sr1 = new TextField(),4 , 4);
        sr1.setPrefWidth(120);
        sr1.setMaxWidth(120);
        grid2.add(sr2 = new TextField(),3,4);
        sr2.setPrefWidth(120);
        sr2.setMaxWidth(120);
        grid2.add(sr3 = new TextField(),2,4);
        sr3.setPrefWidth(120);
        sr3.setMaxWidth(120);
        grid2.add(sr4 = new TextField(),1,4);
        sr4.setPrefWidth(120);
        sr4.setMaxWidth(120);

        ImageView icon = new ImageView(new Image(getSubmitButtonIconFilePathName())); //"/images/pluscolor.png"
        icon.setFitHeight(15);
        icon.setFitWidth(15);
        submitButton = new Button(getSubmitButtonText(), icon);
        submitButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
        submitButton.setOnAction((ActionEvent e) -> {
            clearErrorMessage();
            clearOutlines();

            SLOTableModel selectedItem = tableOfSLOs.getSelectionModel().getSelectedItem();

            if(selectedItem != null)
            {
                String selectedSLOId = selectedItem.getSloID();

                String scfr1 = (String) fr1.getText();
                String scfr2 = (String) fr2.getText();
                String scfr3 = (String) fr3.getText();
                String scfr4 = (String) fr4.getText();
                String scso1 = (String) so1.getText();
                String scso2 = (String) so2.getText();
                String scso3 = (String) so3.getText();
                String scso4 = (String) so4.getText();
                String scjr1 = (String) jr1.getText();
                String scjr2 = (String) jr2.getText();
                String scjr3 = (String) jr3.getText();
                String scjr4 = (String) jr4.getText();
                String scsr1 = (String) sr1.getText();
                String scsr2 = (String) sr2.getText();
                String scsr3 = (String) sr3.getText();
                String scsr4 = (String) sr4.getText();

                if((validateSPCN(scfr1)) && (validateSPCN(scfr2)) && (validateSPCN(scfr3)) && (validateSPCN(scfr4)) &&
                        (validateSPCN(scso1)) && (validateSPCN(scso2)) && (validateSPCN(scso3)) && (validateSPCN(scso4)) &&
                        (validateSPCN(scjr1)) && (validateSPCN(scjr2)) && (validateSPCN(scjr3)) && (validateSPCN(scjr4)) &&
                        (validateSPCN(scsr1)) && (validateSPCN(scsr2)) && (validateSPCN(scsr3)) && (validateSPCN(scsr4))){

                    Properties props = new Properties();
                    props.setProperty("SLOID", selectedSLOId);
                    props.setProperty("scfr1", scfr1);
                    props.setProperty("scfr2", scfr2);
                    props.setProperty("scfr3", scfr3);
                    props.setProperty("scfr4", scfr4);
                    props.setProperty("scso1", scso1);
                    props.setProperty("scso2", scso2);
                    props.setProperty("scso3", scso3);
                    props.setProperty("scso4", scso4);
                    props.setProperty("scjr1", scjr1);
                    props.setProperty("scjr2", scjr2);
                    props.setProperty("scjr3", scjr3);
                    props.setProperty("scjr4", scjr4);
                    props.setProperty("scsr1", scsr1);
                    props.setProperty("scsr2", scsr2);
                    props.setProperty("scsr3", scsr3);
                    props.setProperty("scsr4", scsr4);

                    myModel.stateChangeRequest(keyToSendWithData, props);
                }
                else{
                    displayErrorMessage("ERROR: Invalid student performance data value. Please check and re-enter!");
                }
            }
            else{
                displayErrorMessage("Please select an SLO!");
            }

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

        //vbox.getChildren().add(tableOfSLOs);
        vbox.getChildren().add(grid1);
        //vbox.getChildren().add(prompt);
        vbox.getChildren().add(grid2);
        vbox.getChildren().add(buttonContainer);

        return vbox;
    }

    //---------------------------------------------------------
    protected String getSubmitButtonText() {
        return "Add";
    }

    //---------------------------------------------------------
    protected String getSubmitButtonIconFilePathName() {
        return "/images/pluscolor.png";
    }

    //-------------------------------------------------------------
    protected void processSLOSelected(){}

    //this method validates that a student performance category entry is an integer
    //-------------------------------------------------------------
    protected boolean validateSPCN(String num){
        if(num != null && num.length() > 0){
            try{
                int numI = Integer.parseInt(num);
                if(numI >= 0){
                    return true;
                }
                else{
                    return false;
                }
            }
            catch(Exception ex){
                return false;
            }

        }
        else{
            return false;
        }
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
        catName1.setText((String)myModel.getState("Category_1"));
        catName2.setText((String)myModel.getState("Category_2"));
        catName3.setText((String)myModel.getState("Category_3"));
        catName4.setText((String)myModel.getState("Category_4"));

        populateFieldsHelper();
    }

    //-------------------------------------------------------
    protected void populateFieldsHelper(){}
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
