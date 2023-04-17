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
import javafx.event.Event;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;


// project imports
import impresario.IModel;
import model.Transaction;
import utilities.GlobalVariables;

/** The class containing the Search Semester View  for the Gen Ed Assessment Data
 *  Management Application
 */
//==============================================================
public class SearchSemesterView extends AddSemesterView
{

	//

	// constructor for this class -- takes a model object
	//----------------------------------------------------------
	public SearchSemesterView(IModel sem)
	{
		super(sem);
		keyToSendWithData = "SearchSemester";
	}

	

	//---------------------------------------------------------
	protected String getPromptString() {
		return "Select Semester and Enter Year (if known):";
	}

   //------------------------------------------------------------
    protected VBox createFormContents() {
        VBox vbox = new VBox(10);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text prompt = new Text(getPromptString());
        prompt.setWrappingWidth(500);
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setFill(Color.BLACK);
        prompt.setFont(Font.font("Copperplate", FontWeight.BOLD, 18));
        grid.add(prompt, 0, 0, 2, 1);

        ts.setTableNameForLocale(GlobalVariables.locale);
        try {
            ts.populate("LBL_Semester");
        }
        catch (Exception ex) {

        }
        Text semesterLabel = new Text(ts.getDisplayString());
        Font myFont = Font.font("Helvetica", FontWeight.BOLD, 12);
        semesterLabel.setFont(myFont);
        semesterLabel.setWrappingWidth(150);
        semesterLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(semesterLabel, 0, 1);
        grid.add(semester = new ComboBox(FXCollections.observableArrayList(allowedSemesters)), 1, 1);

        try {
            ts.populate("LBL_Year");
        }
        catch (Exception ex) {

        }
        Text yearLabel = new Text(ts.getDisplayString());
        yearLabel.setFont(myFont);
        yearLabel.setWrappingWidth(150);
        yearLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(yearLabel, 0, 2);
        grid.add(year = new TextField(), 1, 2);


        ImageView icon = new ImageView(new Image("/images/searchcolor.png"));
        icon.setFitHeight(15);
        icon.setFitWidth(15);
        ts.setTableNameForLocale(GlobalVariables.locale);
        try {
            ts.populate("LBL_Search");
        }
        catch (Exception ex) {

        }
        submit = new Button(ts.getDisplayString(), icon);
        submit.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
        submit.setOnAction((ActionEvent e) -> {
            clearErrorMessage();
            clearOutlines();
            Properties props = new Properties();
            String semesterName = (String) semester.getValue();
            String enteredYear = year.getText();
            props.setProperty("SemName", semesterName);
            props.setProperty("Year", enteredYear);
                        		myModel.stateChangeRequest(keyToSendWithData, props);
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
        try {
            ts.populate("LBL_Return");
        }
        catch (Exception ex) {

        }
        cancelButton = new Button(ts.getDisplayString(), icon);
        cancelButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                clearErrorMessage();
                myModel.stateChangeRequest("CancelTransaction", null);
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


	

	public void clearValues(){

	}
}

//---------------------------------------------------------------
//	Revision History:
//


