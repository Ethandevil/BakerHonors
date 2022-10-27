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

/** The class containing the Modify ISLOView  for the ISLO Data
 *  Management Application
 */
//==============================================================
public class AddGenEdAreaSLOView extends View {

    // other private data
    private final int labelWidth = 120;
    private final int labelHeight = 25;

    // GUI components
    protected Button submit;
    protected Button cancelButton;

    protected TextField genEdAreaName;
    protected TextArea sloText;
    protected TextArea notes;


    // other GUI Components here
    protected MessageView statusLog;
    protected DropShadow shadow = new DropShadow();

    // constructor for this class -- takes a model object
    //----------------------------------------------------------
    public AddGenEdAreaSLOView(IModel aCoord) {

        super(aCoord, "AddGenEdAreaSLOView");

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

    //------------------------------------------------------------
    protected VBox createFormContents() {
        VBox vbox = new VBox(10);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Font myFont = Font.font("Helvetica", FontWeight.BOLD, 12);

        Text genEdLabel = new Text(" Chosen Gen Ed Area : ");
        genEdLabel.setFont(myFont);
        genEdLabel.setWrappingWidth(150);
        genEdLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(genEdLabel, 0, 0);
        grid.add(genEdAreaName = new TextField(), 0, 1);

        Text prompt = new Text("Enter New SLO Information");
        prompt.setWrappingWidth(400);
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setFill(Color.BLACK);
        grid.add(prompt, 1, 1, 2, 1);

        Text sloTextLabel = new Text(" SLO Text : ");
        sloTextLabel.setFont(myFont);
        sloTextLabel.setWrappingWidth(150);
        sloTextLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(sloTextLabel, 2, 0);
        grid.add(sloText = new TextArea(), 2, 1);

        Text notesLabel = new Text(" SLO Text : ");
        notesLabel.setFont(myFont);
        notesLabel.setWrappingWidth(150);
        notesLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(notesLabel, 3, 0);
        grid.add(notes = new TextArea(), 3, 1);


        ImageView icon = new ImageView(new Image("/images/remove_icon.png"));
        icon.setFitHeight(15);
        icon.setFitWidth(15);
        submit = new Button("Confirm", icon);
        submit.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
        submit.setOnAction((ActionEvent e) -> {
            clearErrorMessage();
            clearOutlines();
            Properties props = new Properties();

            String sloTextString = sloText.getText();
            if (sloTextString.length() > 0 && sloTextString.matches("[a-zA-Z0-9- ]+"))
            {
                props.setProperty("SLOText", sloTextString);
                String notesString = notes.getText();
                if (notesString.length() > 0 && notesString.matches("[a-zA-Z0-9-,-. ]+"))
                {
                    props.setProperty("Notes", notesString);
                    myModel.stateChangeRequest("AddGenEdAreaSLO", props);
                }
                else
                {
                    notes.setStyle("-fx-border-color: firebrick;");
                    displayErrorMessage("ERROR: Please enter valid Gen Ed Area Notes!");
                }
            }
            else
            {
                genEdAreaName.setStyle("-fx-border-color: firebrick;");
                displayErrorMessage("ERROR: Please enter a valid Gen Ed Area SLO!");
            }

        });

        submit.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
            submit.setEffect(new DropShadow());
        });
        submit.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
            submit.setEffect(null);
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
                myModel.stateChangeRequest("CancelAddGenEdAreaSLO", null);
            }
        });
        cancelButton.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
            cancelButton.setEffect(new DropShadow());
        });
        cancelButton.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
            cancelButton.setEffect(null);
        });
        buttonContainer.getChildren().add(submit);
        buttonContainer.getChildren().add(cancelButton);

        vbox.getChildren().add(grid);
        vbox.getChildren().add(buttonContainer);

        return vbox;
    }

    // Create the status log field
    //-------------------------------------------------------------
    private MessageView createStatusLog(String initialMessage) {

        statusLog = new MessageView(initialMessage);

        return statusLog;
    }

    //-------------------------------------------------------------
    private void clearOutlines(){
        genEdAreaName.setStyle("-fx-border-color: transparent; -fx-focus-color: darkgreen;");
        sloText.setStyle("-fx-border-color: transparent; -fx-focus-color: darkgreen;");
        notes.setStyle("-fx-border-color: transparent; -fx-focus-color: darkgreen;");

    }

    // Create Copyright Panel
    //----------------------------------------------------------
    private Text createCopyrightPanel()
    {
        return new CopyrightPanel();
    }

    //-------------------------------------------------------------
    public void populateFields() {
        String genEdAreaText = (String) myModel.getState("AreaName");
        genEdAreaName.setText(genEdAreaText);
        genEdAreaName.setEditable(false);
        notes.setText("NA");
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
     * Clear error message
     */
    //----------------------------------------------------------
    public void clearErrorMessage() {
        statusLog.clearErrorMessage();
    }

    //-------------------------------------------------------------
    public void clearValues()
    {
        genEdAreaName.clear();
        sloText.clear();
        notes.clear();
    }

}
