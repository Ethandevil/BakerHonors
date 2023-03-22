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
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
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
import java.util.Optional;
import java.util.Properties;
import java.util.Vector;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;


// project imports
import impresario.IModel;

/** The class containing the Report Generation View for the Gen Ed Assessment Data Management application */
//==============================================================
public class ReportGeneratorView extends View
{

    // other private data
    private final int labelWidth = 120;
    private final int labelHeight = 25;

    // other buttons here
    protected Button reportButton;
    protected Button reflectionButton;
    protected Button assessmentTeamButton;
    protected Button cancelButton;


    protected MessageView statusLog;
    protected DropShadow shadow = new DropShadow();

    // constructor for this class -- takes a model object
    //----------------------------------------------------------
    public ReportGeneratorView(IModel aCoord)
    {
        super(aCoord, "ReportGeneratorView");

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

        myModel.subscribe("TransactionError", this);
    }

    // Create the labels and fields
    //-------------------------------------------------------------
    private VBox createTitle()
    {
        return new CommonTitleWithoutLogoPanel();
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

        Text prompt = new Text("Report Generation Menu");
        prompt.setFont(Font.font("Copperplate", FontWeight.BOLD, 18));
        prompt.setWrappingWidth(400);
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setFill(Color.BLACK);
        vbox.getChildren().add(prompt);
        vbox.setAlignment(Pos.CENTER);

        Text blankText2 = new Text("  ");
        blankText2.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        blankText2.setWrappingWidth(350);
        blankText2.setTextAlignment(TextAlignment.CENTER);
        blankText2.setFill(Color.WHITE);
        vbox.getChildren().add(blankText2);

        VBox reportCont = new VBox(10);
        reportCont.setAlignment(Pos.CENTER);
        /*reportCont.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
            reportCont.setStyle("-fx-background-color: GOLD");
        });
        reportCont.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
            reportCont.setStyle("-fx-background-color: SLATEGREY");
        });*/

        ImageView icon = new ImageView(new Image("/images/return.png"));
        icon.setFitHeight(15);
        icon.setFitWidth(15);

        reportButton = new Button("Gen Ed Basic Data Report");
        reportButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
        reportButton.setOnAction((ActionEvent e) -> {
            clearErrorMessage();
            myModel.stateChangeRequest("ReportData", null);
        });
        reportButton.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
            reportButton.setEffect(new DropShadow());
        });
        reportButton.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
            reportButton.setEffect(null);
        });

        reflectionButton = new Button("Gen Ed Instructor Reflection Report");
        reflectionButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
        reflectionButton.setOnAction((ActionEvent e) -> {
            clearErrorMessage();
            myModel.stateChangeRequest("ReflectionData", null);
        });
        reflectionButton.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
            reflectionButton.setEffect(new DropShadow());
        });
        reflectionButton.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
            reflectionButton.setEffect(null);
        });

        assessmentTeamButton = new Button("Gen Ed Assessment Teams Report");
        assessmentTeamButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
        assessmentTeamButton.setOnAction((ActionEvent e) -> {
            clearErrorMessage();
            myModel.stateChangeRequest("ATData", null);
        });
        assessmentTeamButton.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
            assessmentTeamButton.setEffect(new DropShadow());
        });
        assessmentTeamButton.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
            assessmentTeamButton.setEffect(null);
        });

        reportCont.getChildren().add(reportButton);
        reportCont.getChildren().add(reflectionButton);
        reportCont.getChildren().add(assessmentTeamButton);
        vbox.getChildren().add(reportCont);

        HBox doneCont = new HBox(10);
        doneCont.setAlignment(Pos.CENTER);
        doneCont.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
            doneCont.setStyle("-fx-background-color: GOLD");
        });
        doneCont.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
            doneCont.setStyle("-fx-background-color: SLATEGREY");
        });

        icon = new ImageView(new Image("/images/return.png"));
        icon.setFitHeight(15);
        icon.setFitWidth(15);

        Text blankText3 = new Text("  ");
        blankText3.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        blankText3.setWrappingWidth(350);
        blankText3.setTextAlignment(TextAlignment.CENTER);
        blankText3.setFill(Color.WHITE);
        vbox.getChildren().add(blankText3);

        cancelButton = new Button("Return", icon);
        cancelButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
        cancelButton.setOnAction((ActionEvent e) -> {
            clearErrorMessage();
            myModel.stateChangeRequest("CancelReportGenerator", null);
        });
        cancelButton.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
            cancelButton.setEffect(new DropShadow());
        });
        cancelButton.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
            cancelButton.setEffect(null);
        });
        doneCont.getChildren().add(cancelButton);


        vbox.getChildren().add(doneCont);
        clearOutlines();

        return vbox;
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
        clearValues();
        clearErrorMessage();
    }


    //-------------------------------------------------------------
    public void clearValues()
    {

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