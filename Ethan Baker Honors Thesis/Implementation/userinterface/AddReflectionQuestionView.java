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

package userinterface;

// specify the package


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

/** The class containing the AddReflectionQuestionView for the Gen Ed Assessment Data Management application */
//==============================================================
public class AddReflectionQuestionView extends View
{

    // other private data
    private final int labelWidth = 120;
    private final int labelHeight = 25;

    // GUI components

    protected TextArea questionText;

    // other buttons here
    protected Button submitButton;
    protected Button cancelButton;


    protected MessageView statusLog;
    protected DropShadow shadow = new DropShadow();

    // constructor for this class -- takes a model object
    //----------------------------------------------------------
    public AddReflectionQuestionView(IModel aCoord)
    {
        super(aCoord, "AddReflectionQuestionView");

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

    protected String getActionText()
    {
        return "Enter New Reflection Question Text:";
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


        Text questionTextLabel = new Text(" Question Text : ");

        questionTextLabel.setFill(Color.GOLD);
        questionTextLabel.setFont(myFont);
        questionTextLabel.setWrappingWidth(150);
        questionTextLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(questionTextLabel, 0, 2);

        questionText = new TextArea();
        questionText.setPrefColumnCount(30);
        questionText.setPrefRowCount(3);
        questionText.setWrapText(true);
        grid.add(questionText, 1, 2);

        HBox doneCont = new HBox(10);
        doneCont.setAlignment(Pos.CENTER);
        doneCont.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
            doneCont.setStyle("-fx-background-color: GOLD");
        });
        doneCont.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
            doneCont.setStyle("-fx-background-color: SLATEGREY");
        });
        ImageView icon = new ImageView(new Image("/images/pluscolor.png"));
        icon.setFitHeight(15);
        icon.setFitWidth(15);
        submitButton = new Button("Add", icon);
        submitButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
        submitButton.setOnAction((ActionEvent e) -> {
            clearErrorMessage();
            clearOutlines();
            Properties props = new Properties();

            String questionTextNm = questionText.getText();
            if (questionTextNm.length() > 0 && questionTextNm.matches("[a-zA-Z0-9-:()?, ]+"))
            {
                props.setProperty("QuestionText", questionTextNm);
                myModel.stateChangeRequest("QuestionData", props);
            }
            else
            {
                questionText.setStyle("-fx-border-color: firebrick;");
                displayErrorMessage("ERROR: Please enter a valid question text!");
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
            myModel.stateChangeRequest("CancelAddQuestion", null);
        });
        cancelButton.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
            cancelButton.setEffect(new DropShadow());
        });
        cancelButton.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
            cancelButton.setEffect(null);
        });
        doneCont.getChildren().add(cancelButton);

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

    // Create the status log field
    //-------------------------------------------------------------
    private MessageView createStatusLog(String initialMessage)
    {

        statusLog = new MessageView(initialMessage);

        return statusLog;
    }

    //-------------------------------------------------------------
    private void clearOutlines(){
        questionText.setStyle("-fx-border-color: transparent; -fx-focus-color: darkgreen;");
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
        questionText.clear();
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


