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

import model.Semester;
import model.SemesterCollection;
import model.StudentCategorizationDisplay;
import model.StudentCategorizationDisplayCollection;

import javax.swing.*;

//==============================================================================
public class StudentCategorizationDisplayCollectionView extends View
{
    protected Text cat1Name;
    protected Text cat2Name;
    protected Text cat3Name;
    protected Text cat4Name;
    protected Text cat3NameAndCat4Name;
    protected Text promptText;
    protected Button cancelButton;
    protected Button submitButton;
    protected MessageView statusLog;
    protected Text actionText;

    protected ArrayList<TextField> cat1s;
    protected ArrayList<TextField> cat2s;
    protected ArrayList<TextField> cat3s;
    protected ArrayList<TextField> cat4s;
    protected ArrayList<TextField> cat3And4s;
    protected ComboBox<String> box;

    protected StudentCategorizationDisplayCollection scCollection;

    //--------------------------------------------------------------------------
    public StudentCategorizationDisplayCollectionView(IModel mst)
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
        cat4Name.setText((String)myModel.getState("PerformanceCategory4"));
        cat3Name.setText((String)myModel.getState("PerformanceCategory3"));
        cat2Name.setText((String)myModel.getState("PerformanceCategory2"));
        cat1Name.setText((String)myModel.getState("PerformanceCategory1"));
        cat3NameAndCat4Name.setText((String)myModel.getState("PerformanceCategory4") + " and " +
                (String)myModel.getState("PerformanceCategory3"));
        getGridValues();
    }

    //--------------------------------------------------------------------------
    protected void getGridValues()
    {
        scCollection = (StudentCategorizationDisplayCollection)myModel.getState("StudentCategorizationList");
        StudentCategorizationDisplay sc1 = (StudentCategorizationDisplay) scCollection.getState("StudentCategorization1");
        StudentCategorizationDisplay sc2 = (StudentCategorizationDisplay) scCollection.getState("StudentCategorization2");
        StudentCategorizationDisplay sc3 = (StudentCategorizationDisplay) scCollection.getState("StudentCategorization3");
        StudentCategorizationDisplay sc4 = (StudentCategorizationDisplay) scCollection.getState("StudentCategorization4");
        StudentCategorizationDisplay sc3And4 = (StudentCategorizationDisplay) scCollection.getState("StudentCategorization3And4");
        int numSLOs = (int)myModel.getState("NumSLOs");
        for(int i = 0; i < numSLOs; i++){
            String val = sc4.getPercentage(i);
            cat4s.get(i).setText(val);
            val = sc3.getPercentage(i);
            cat3s.get(i).setText(val);
            val = sc2.getPercentage(i);
            cat2s.get(i).setText(val);
            val = sc1.getPercentage(i);
            cat1s.get(i).setText(val);
            val = sc3And4.getPercentage(i);
            cat3And4s.get(i).setText(val);
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
        int numSLOs = (int)myModel.getState("NumSLOs");

        box = new ComboBox<String>();
        box.getItems().addAll(
                "All","Freshmen","Sophomore","Junior","Senior"
        );
        box.setValue(box.getItems().get(0));
        box.setOnAction((ActionEvent event) -> {
            //System.out.println(box.getValue());
            myModel.stateChangeRequest("UpdateStudentCategorization", box.getValue());
            //getGridValues();
    });

        VBox vbox = new VBox(10);
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 25, 10, 0));

        Font myFont = Font.font("Helvetica", FontWeight.BOLD, 16);

        for(int i = 0; i < numSLOs; i++){
            Text slo = new Text("SLO " + (i + 1));
            slo.setFont(myFont);
            grid.add(slo, i + 1, 0);
        }

        grid.add(cat4Name = new Text(),0,1);
        cat4s = new ArrayList<TextField>();
        for(int i = 0; i < numSLOs; i++){
            TextField tf = new TextField();
            tf.setPrefWidth(120);
            tf.setMaxWidth(120);
            cat4s.add(tf);
            grid.add(tf,i+1,1);
        }

        grid.add(cat3Name = new Text(),0,2);
        cat3s = new ArrayList<TextField>();
        for(int i = 0; i < numSLOs; i++){
            TextField tf = new TextField();
            tf.setPrefWidth(120);
            tf.setMaxWidth(120);
            cat3s.add(tf);
            grid.add(tf,i+1,2);
        }

        grid.add(cat2Name = new Text(),0,3);
        cat2s = new ArrayList<TextField>();
        for(int i = 0; i < numSLOs; i++){
            TextField tf = new TextField();
            tf.setPrefWidth(120);
            tf.setMaxWidth(120);
            cat2s.add(tf);
            grid.add(tf,i+1,3);
        }

        grid.add(cat1Name = new Text(),0,4);
        cat1s = new ArrayList<TextField>();
        for(int i = 0; i < numSLOs; i++){
            TextField tf = new TextField();
            tf.setPrefWidth(120);
            tf.setMaxWidth(120);
            cat1s.add(tf);
            grid.add(tf,i+1,4);
        }

        GridPane grid2 = new GridPane();
        grid2.setAlignment(Pos.CENTER);
        grid2.setHgap(10);
        grid2.setVgap(10);
        grid2.setPadding(new Insets(0, 25, 10, 0));

        cat3NameAndCat4Name = new Text();
        grid2.add(cat3NameAndCat4Name, 0, 0);
        cat3And4s = new ArrayList<TextField>();
        for(int i = 0; i < numSLOs; i++){
            TextField tf = new TextField();
            tf.setPrefWidth(120);
            tf.setMaxWidth(120);
            cat3And4s.add(tf);
            grid2.add(tf,i+1,0);
        }


        promptText = new Text(getPromptText());//text set later
        promptText.setFont(Font.font("Copperplate", FontWeight.BOLD, 18));
        promptText.setWrappingWidth(350);
        promptText.setTextAlignment(TextAlignment.CENTER);
        vbox.getChildren().add(promptText);

        /*
        tableOfScs = new TableView<StudentCategorizationDisplayTableModel>();
        tableOfScs.setEffect(new DropShadow());
        tableOfScs.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-selection-bar: gold;");
        tableOfScs.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);


        TableColumn courseDiscCodeCol = new TableColumn("Assessment Team ID") ;
        courseDiscCodeCol.setMinWidth(75);
        courseDiscCodeCol.setCellValueFactory(
                new PropertyValueFactory<StudentCategorizationDisplayTableModel, String>("assessmentTeamID"));

        TableColumn courseNumCol = new TableColumn("SLO ID") ;
        courseNumCol.setMinWidth(75);
        courseNumCol.setCellValueFactory(
                new PropertyValueFactory<StudentCategorizationDisplayTableModel, String>("sloID"));

        TableColumn teacherNameCol = new TableColumn("Student Level") ;
        teacherNameCol.setMinWidth(75);
        teacherNameCol.setCellValueFactory(
                new PropertyValueFactory<StudentCategorizationDisplayTableModel, String>("studentLevel"));

        TableColumn cat1Col = new TableColumn(cat1LabelVal) ;
        cat1Col.setMinWidth(75);
        cat1Col.setCellValueFactory(
                new PropertyValueFactory<StudentCategorizationDisplayTableModel, String>("cat1Number"));

        TableColumn cat2Col = new TableColumn(cat2LabelVal) ;
        cat2Col.setMinWidth(75);
        cat2Col.setCellValueFactory(
                new PropertyValueFactory<StudentCategorizationDisplayTableModel, String>("cat2Number"));

        TableColumn cat3Col = new TableColumn(cat3LabelVal) ;
        cat3Col.setMinWidth(75);
        cat3Col.setCellValueFactory(
                new PropertyValueFactory<StudentCategorizationDisplayTableModel, String>("cat3Number"));

        TableColumn cat4Col = new TableColumn(cat4LabelVal) ;
        cat4Col.setMinWidth(75);
        cat4Col.setCellValueFactory(
                new PropertyValueFactory<StudentCategorizationDisplayTableModel, String>("cat4Number"));


        tableOfScs.getColumns().addAll(courseDiscCodeCol, courseNumCol, teacherNameCol, cat1Col, cat2Col, cat3Col, cat4Col);

        tableOfScs.setOnMousePressed((MouseEvent event) -> {
            if (event.isPrimaryButtonDown() && event.getClickCount() >=2 ){

                // DO NOTHING - this is just a REPORT Table (want to remove this code?)
            }
        });
        */

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

        vbox.getChildren().add(box);
        vbox.getChildren().add(grid);
        vbox.getChildren().add(grid2);
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
        String cat4LabelVal = (String)myModel.getState("PerformanceCategory4");
        String cat3LabelVal = (String)myModel.getState("PerformanceCategory3");
        String cat2LabelVal = (String)myModel.getState("PerformanceCategory2");
        String cat1LabelVal = (String)myModel.getState("PerformanceCategory1");
        try {
            FileWriter outFile = new FileWriter(fName);
            PrintWriter out = new PrintWriter(outFile);

            String line = "Report for Gen Ed Area: " + myModel.getState("AreaName") +
                    " assessed in Semester: " + myModel.getState("Semester");

            out.println(line);

            line = "Class Level: " + box.getValue();

            out.println(line);

            line = "";

            out.println(line);

            line = ",SLO 1,SLO 2,SLO 3,SLO 4,SLO 5";

            out.println(line);

            line = cat4LabelVal + ",";

            for(int i = 0; i <  cat4s.size(); i++) {
                line += (String)cat4s.get(i).getText() + ",";
            }
            if(line.length() > 0)
                line = line.substring(0, line.length() - 1);

            out.println(line);

            line = cat3LabelVal + ",";

            for(int i = 0; i <  cat3s.size(); i++) {
                line += (String)cat3s.get(i).getText() + ",";
            }
            if(line.length() > 0)
                line = line.substring(0, line.length() - 1);

            out.println(line);

            line = cat2LabelVal + ",";

            for(int i = 0; i <  cat2s.size(); i++) {
                line += (String)cat2s.get(i).getText() + ",";
            }
            if(line.length() > 0)
                line = line.substring(0, line.length() - 1);

            out.println(line);

            line = cat1LabelVal + ",";

            for(int i = 0; i <  cat1s.size(); i++) {
                line += (String)cat1s.get(i).getText() + ",";
            }
            if(line.length() > 0)
                line = line.substring(0, line.length() - 1);

            out.println(line);

            line = "";

            out.println(line);

            line = cat3LabelVal + " and " + cat4LabelVal + ",";

            for(int i = 0; i <  cat3And4s.size(); i++) {
                line += (String)cat3And4s.get(i).getText() + ",";
            }
            if(line.length() > 0)
                line = line.substring(0, line.length() - 1);

            out.println(line);

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
            getGridValues();
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