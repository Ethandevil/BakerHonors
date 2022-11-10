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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.Properties;

// project imports
import impresario.IModel;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/** The class containing the Search SLO View for the Gen Ed Assessment Data
 *  Management application
 */
//==============================================================
public class SearchSLOView extends View
{

    // GUI components
    protected TextField SLOText;

    protected Button submitButton;
    protected Button cancelButton;

    // For showing error message
    protected MessageView statusLog;

    // constructor for this class -- takes a model object
    //----------------------------------------------------------
    public SearchSLOView(IModel sgeat)
    {
        // sgeat - Search Gen Ed Area Transaction (any controller
        // that requires Gen Ed Areas to be searched for)
        super(sgeat, "SearchSLOView");


        // create a container for showing the contents
        VBox container = new VBox(10);
        container.setStyle("-fx-background-color: slategrey");
        container.setPadding(new Insets(15, 5, 5, 5));

        // Add a title for this panel
        container.getChildren().add(createTitle());

        // create our GUI components, add them to this Container
        container.getChildren().add(createFormContent());

        container.getChildren().add(createStatusLog("             "));

        container.getChildren().add(createCopyrightPanel());

        getChildren().add(container);

        populateFields();


        myModel.subscribe("TransactionError", this);
    }


    // Create Copyright Panel
    //----------------------------------------------------------
    private Text createCopyrightPanel()
    {
        return new CopyrightPanel();
    }



    // Create the title container
    //-------------------------------------------------------------
    private Node createTitle()
    {
        return new CommonTitleWithoutLogoPanel();

    }

    //-------------------------------------------------------------
    protected String getActionText()
    {
        return "Enter SLO Search Criteria: ";
        //"(in full if known, or in part):";
    }


    // Create the main form content
    //-------------------------------------------------------------
    public VBox createFormContent()
    {
        VBox vbox = new VBox(10);

        Text blankText = new Text("  ");
        blankText.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        blankText.setWrappingWidth(350);
        blankText.setTextAlignment(TextAlignment.CENTER);
        blankText.setFill(Color.WHITE);
        vbox.getChildren().add(blankText);

        Text prompt = new Text(getActionText());

        prompt.setWrappingWidth(400);
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setFill(Color.BLACK);
        prompt.setFont(Font.font("Copperplate", FontWeight.BOLD, 18));
        vbox.getChildren().add(prompt);
        vbox.setAlignment(Pos.CENTER);

        GridPane grid0 = new GridPane();
        grid0.setAlignment(Pos.CENTER);
        grid0.setHgap(10);
        grid0.setVgap(10);
        grid0.setPadding(new Insets(0, 25, 10, 0));

        Text isloNameLabel = new Text(" SLO Text : ");
        Font myFont = Font.font("Comic Sans", FontWeight.THIN, 16);
        isloNameLabel.setFont(myFont);
        isloNameLabel.setFill(Color.GOLD);
        isloNameLabel.setWrappingWidth(150);
        isloNameLabel.setTextAlignment(TextAlignment.RIGHT);
        grid0.add(isloNameLabel, 0, 1);

        SLOText = new TextField();
        SLOText.setStyle("-fx-focus-color: darkgreen;");
        SLOText.setOnAction((ActionEvent e) -> {
            clearErrorMessage();
            Properties props = new Properties();
            String areaNm = SLOText.getText();
            if (areaNm.length() > 0)
            {
                props.setProperty("SLOText", areaNm);

            }

            myModel.stateChangeRequest("SearchSLO", props);
        });
        grid0.add(SLOText, 1, 1);

        vbox.getChildren().add(grid0);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 25, 10, 0));


        HBox doneCont = new HBox(10);
        doneCont.setAlignment(Pos.CENTER);
        doneCont.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
            doneCont.setStyle("-fx-background-color: GOLD");
        });
        doneCont.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
            doneCont.setStyle("-fx-background-color: SLATEGREY");
        });
        ImageView icon = new ImageView(new Image("/images/searchcolor.png"));
        icon.setFitHeight(15);
        icon.setFitWidth(15);
        submitButton = new Button("Search",icon);
        submitButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
        submitButton.setOnAction((ActionEvent e) -> {
            clearErrorMessage();
            Properties props = new Properties();
            String areaNm = SLOText.getText();
            if (areaNm.length() > 0)
            {
                props.setProperty("SLOText", areaNm);

            }

            myModel.stateChangeRequest("SearchSLO", props);


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
        cancelButton = new Button("Return",icon);
        cancelButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
        cancelButton.setOnAction((ActionEvent e) -> {
            clearErrorMessage();
            myModel.stateChangeRequest("CancelSearchSLO", null);
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
        vbox.setAlignment(Pos.CENTER);

        return vbox;
    }


    // Create the status log field
    //-------------------------------------------------------------
    protected MessageView createStatusLog(String initialMessage)
    {
        statusLog = new MessageView(initialMessage);

        return statusLog;
    }

    //-------------------------------------------------------------
    public void populateFields()
    {
        clearValues();
        clearErrorMessage();
    }

    //----------------------------------------------------------
    public void clearValues()
    {
        SLOText.clear();
    }

    /**
     * Update method
     */
    //---------------------------------------------------------
    public void updateState(String key, Object value)
    {
        clearValues();
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

//---------------------------------------------------------------
//	Revision History:
//


