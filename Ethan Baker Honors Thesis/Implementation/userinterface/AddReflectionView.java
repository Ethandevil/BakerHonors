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

package userinterface;

// specify the package


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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.Optional;
import java.util.Properties;
import java.util.Vector;

import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;


// project imports
import impresario.IModel;
import model.ReflectionQuestion;
import model.ReflectionQuestionCollection;
import model.SLO;
import model.SLOCollection;

/** The class containing the AddReflectionQuestionView for the Gen Ed Assessment Data Management application */
//==============================================================
public class AddReflectionView extends View
{

    // other private data
    private final int labelWidth = 120;
    private final int labelHeight = 25;

    // GUI components

    protected Text genEdArea;
    protected Text semester;
    protected TableView<ReflectionQuestionTableModel> tableQuestions;
    protected TextArea reflectionAnswer;

    // other buttons here
    protected Button submitButton;
    protected Button cancelButton;


    protected MessageView statusLog;
    protected DropShadow shadow = new DropShadow();

    // constructor for this class -- takes a model object
    //----------------------------------------------------------
    public AddReflectionView(IModel aCoord)
    {
        super(aCoord, "AddReflectionView");

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

        container.getChildren().add(createStatusLog("             "));

        container.getChildren().add(createCopyrightPanel());

        // container.setMinHeight(550);
        //container.setMinWidth(550);

        getChildren().add(container);

        populateFields();
        cancelButton.requestFocus();

        tableQuestions.getSelectionModel().select(0); //autoselect first element

        myModel.subscribe("TransactionError", this);
    }

    //--------------------------------------------------------------------------
    protected void getEntryTableModelValues()
    {

        ObservableList<ReflectionQuestionTableModel> tableData = FXCollections.observableArrayList();
        try
        {
        ReflectionQuestionCollection myRQCollection =
                    (ReflectionQuestionCollection)myModel.getState("ReflectionQuestionList");

            Vector entryList = (Vector)myRQCollection.getState("ReflectionQuestions");

            if (entryList.size() > 0)
            {

                Enumeration entries = entryList.elements();

                while (entries.hasMoreElements() == true)
                {

                    ReflectionQuestion nextQ = (ReflectionQuestion) entries.nextElement();
                    Vector<String> view = nextQ.getEntryListView();

                    // add this list entry to the list
                    ReflectionQuestionTableModel nextTableRowData = new ReflectionQuestionTableModel(view);
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

            tableQuestions.setItems(tableData);
        }
        catch (Exception e) {//SQLException e) {
            // Need to handle this exception
            System.out.println(e);
            e.printStackTrace();
        }

    }

    // Create the labels and fields
    //-------------------------------------------------------------
    private VBox createTitle()
    {

        return new CommonTitleWithoutLogoPanel();
    }

    //--------------------------------------------------------------------------
    protected String getActionText()
    {
        return "Add new Instructor Reflection: ";
    }


    // Create the navigation buttons
    //-------------------------------------------------------------
    private VBox createFormContents()
    {

        VBox vbox = new VBox(10);

        Text blankText = new Text("  ");
        blankText.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        blankText.setWrappingWidth(350);
        blankText.setTextAlignment(TextAlignment.CENTER);
        blankText.setFill(Color.WHITE);
        vbox.getChildren().add(blankText);

        Text prompt = new Text(getActionText());
        prompt.setFont(Font.font("Copperplate", FontWeight.BOLD, 18));
        prompt.setWrappingWidth(400);
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setFill(Color.BLACK);
        vbox.getChildren().add(prompt);
        vbox.setAlignment(Pos.CENTER);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 25, 10, 0));

        Font myFont = Font.font("copperplate", FontWeight.THIN, 18);

        //
        GridPane grid2 = new GridPane();
        grid2.setAlignment(Pos.CENTER);
        grid2.setHgap(10);
        grid2.setVgap(10);
        grid2.setPadding(new Insets(0, 0, 0, 0));
        //

        Text genLabel = new Text("Gen Ed Area: ");
        genLabel.setFill(Color.GOLD);
        genLabel.setFont(myFont);
        genLabel.setWrappingWidth(150);
        genLabel.setTextAlignment(TextAlignment.RIGHT);
        grid2.add(genLabel, 0, 1);

        genEdArea = new Text();
        grid2.add(genEdArea, 1, 1);

        Text semLabel = new Text("Semester: ");
        semLabel.setFill(Color.GOLD);
        semLabel.setFont(myFont);
        semLabel.setWrappingWidth(150);
        semLabel.setTextAlignment(TextAlignment.RIGHT);
        grid2.add(semLabel, 0, 2);

        semester = new Text();
        grid2.add(semester, 1, 2);

        //vbox.getChildren().add(grid);

        HBox tableCont = new HBox(10);
        tableCont.setAlignment(Pos.CENTER);

        Text refQuestionLabel = new Text(" Choose a Reflection Question : ");
        refQuestionLabel.setFill(Color.GOLD);
        refQuestionLabel.setFont(myFont);
        refQuestionLabel.setWrappingWidth(150);
        refQuestionLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(refQuestionLabel, 0, 3);
        //tableCont.getChildren().add(refQuestionLabel);

        tableQuestions = new TableView<ReflectionQuestionTableModel>();
        tableQuestions.setEffect(new DropShadow());
        tableQuestions.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-selection-bar: gold;");
        tableQuestions.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);


        TableColumn rQCol = new TableColumn("Question Text");
        rQCol.setMinWidth(1500);
        rQCol.setCellValueFactory(
                new PropertyValueFactory<GenEdAreaTableModel, String>("reflectionQuestionText"));

        tableQuestions.getColumns().add(rQCol);

        tableQuestions.setOnMousePressed((MouseEvent event) -> {
            if (event.isPrimaryButtonDown() && event.getClickCount() >=2 ){

                processQuestionSelected();
            }
        });
        //tableQuestions.setPrefHeight(175);
        //tableQuestions.setPrefWidth(600);
        ScrollPane s1 = new ScrollPane();
        s1.setPrefSize(550, 175);
        s1.setFitToHeight(true);
        s1.setContent(tableQuestions);
        grid.add(s1, 1, 3);
        //tableCont.getChildren().add(s1);
        //vbox.getChildren().add(tableCont);

        GridPane grid1 = new GridPane();
        grid1.setAlignment(Pos.CENTER);
        grid1.setHgap(10);
        grid1.setVgap(10);
        grid1.setPadding(new Insets(0, 25, 10, 0));

        Text questionTextLabel = new Text(" Instructor Reflection : ");

        questionTextLabel.setFill(Color.GOLD);
        questionTextLabel.setFont(myFont);
        questionTextLabel.setWrappingWidth(150);
        questionTextLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(questionTextLabel, 0, 4);

        reflectionAnswer = new TextArea();
        reflectionAnswer.setPrefColumnCount(50);
        reflectionAnswer.setPrefRowCount(10);
        reflectionAnswer.setWrapText(true);
        grid.add(reflectionAnswer, 1, 4);

        HBox doneCont = new HBox(10);
        doneCont.setAlignment(Pos.CENTER);
        doneCont.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
            doneCont.setStyle("-fx-background-color: GOLD");
        });
        doneCont.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
            doneCont.setStyle("-fx-background-color: SLATEGREY");
        });
        ImageView icon = new ImageView(new Image(getSubmitButtonIcon()));
        icon.setFitHeight(15);
        icon.setFitWidth(15);
        submitButton = new Button(getSubmitButtonText(), icon);
        submitButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
        submitButton.setOnAction((ActionEvent e) -> {
            clearErrorMessage();
            clearOutlines();
            Properties props = new Properties();

            String questionID = processReflectionQuestionSelected();

            String ansText = reflectionAnswer.getText();
            if (ansText.length() > 0)
            {
                String aTID = (String)myModel.getState("AssessmentTeamID");
                if(aTID != null){
                    props.setProperty("AssessmentTeamID", aTID);
                    props.setProperty("ReflectionQuestionID", questionID);
                    props.setProperty("ReflectionText", ansText);
                    props.setProperty("ACComments", "NA");
                    myModel.stateChangeRequest("ReflectionData", props);

                    displayMessage("Instructor Reflection installed successfully");
                }
                else{
                    reflectionAnswer.setStyle("-fx-border-color: firebrick;");
                    displayErrorMessage("ERROR: Invalid Gen Ed Area / Semester combination chosen!");
                }

            }
            else
            {
                reflectionAnswer.setStyle("-fx-border-color: firebrick;");
                displayErrorMessage("ERROR: Please enter a valid reflection!");
            }
        });
        submitButton.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
            submitButton.setEffect(new DropShadow());
        });
        submitButton.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
            submitButton.setEffect(null);
        });
        doneCont.getChildren().add(submitButton);
        icon = new ImageView(new Image("/images/return.png"));
        icon.setFitHeight(15);
        icon.setFitWidth(15);
        cancelButton = new Button("Return", icon);
        cancelButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
        cancelButton.setOnAction((ActionEvent e) -> {
            clearErrorMessage();
            myModel.stateChangeRequest("CancelAddReflection", null);
        });
        cancelButton.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
            cancelButton.setEffect(new DropShadow());
        });
        cancelButton.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
            cancelButton.setEffect(null);
        });
        doneCont.getChildren().add(cancelButton);


        vbox.getChildren().add(grid2);
        vbox.getChildren().add(grid);
        vbox.getChildren().add(doneCont);
        clearOutlines();
        vbox.addEventFilter(KeyEvent.KEY_RELEASED, event->{
            if (event.getCode() == KeyCode.ENTER) {
                submitButton.fire();
            }
        });

        return vbox;

    }
    //-------------------------------------------------------------
    protected void processQuestionSelected(){}

    //-------------------------------------------------------------
    protected String getSubmitButtonText(){
        return "Add";
    }

    //-------------------------------------------------------------
    protected String getSubmitButtonIcon(){return "/images/pluscolor.png";}

    //--------------------------------------------------------------------------
    protected String processReflectionQuestionSelected()
    {
        ReflectionQuestionTableModel selectedItem = tableQuestions.getSelectionModel().getSelectedItem();

        if(selectedItem != null)
        {
            String selectedReflectionQuestionID = selectedItem.getReflectionQuestionID();

            return selectedReflectionQuestionID;
        }
        return null;
    }

    // Create the status log field
    //-------------------------------------------------------------
    private MessageView createStatusLog(String initialMessage)
    {

        statusLog = new MessageView(initialMessage);

        return statusLog;
    }

    //-------------------------------------------------------------
    private void clearOutlines(){
        reflectionAnswer.setStyle("-fx-border-color: transparent; -fx-focus-color: darkgreen;");
    }


    // Create Copyright Panel
    //----------------------------------------------------------
    private Text createCopyrightPanel()
    {
        return new CopyrightPanel();
    }

    //-------------------------------------------------------------
    public void populateFields()
    {
        String genEd = (String)myModel.getState("GenEdAreaData");
        String sem = (String)myModel.getState("SemData");
        if (genEd != null)
        {
            genEdArea.setText(genEd);
        }
        if (semester != null)
        {
            semester.setText(sem);
        }
        getEntryTableModelValues();

        populateFieldsHelper();
    }

    //-------------------------------------------------------------
    protected void populateFieldsHelper(){}


    //-------------------------------------------------------------
    public void clearValues()
    {
        reflectionAnswer.clear();
    }


    //---------------------------------------------------------
    public void updateState(String key, Object value)
    {
        if (key.equals("TransactionError") == true)
        {
            String val = (String)value;
            if (val.startsWith("ERR") == true)
            {
                displayErrorMessage(val);
            }
            else
            {
                clearValues();
                displayMessage(val);
            }

        }


    }

    /**
     * Display error message
     */
    //----------------------------------------------------------
    public void displayErrorMessage(String message)
    {
        statusLog.displayErrorMessage(message);
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


