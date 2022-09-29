// tabs=4
//************************************************************
//	COPYRIGHT 2021, Ethan L. Baker, Matthew E. Morgan and 
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
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
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;


// project imports
import impresario.IModel;

/** The class containing the Main View for the ISLO Data Management application */
//==============================================================
public class AssessmentCoordinatorView extends View
{

	// other private data
	private final int labelWidth = 120;
	private final int labelHeight = 25;

	// GUI components

	private Button addGenEdAreaButton;
	private Button updateGenEdAreaButton;
	private Button deleteGenEdAreaButton;
	private Button addGESLOButton;
	private Button updateGESLOButton;
	private Button addSemesterButton;
	private Button updateSemesterButton;
	private Button addOfferingButton;
	private Button updateOfferingButton;
	private Button addOfferingTeacherButton;
	private Button updateOfferingTeacherButton;
	private Button deleteOfferingTeacherButton;
	private Button addStudentCategorizationButton;
	private Button updateStudentCategorizationButton;
	private Button addCategoryNameButton;
	private Button updateCategoryNameButton;
	private Button addReflectionQuesButton;
	private Button updateReflectionQuesButton;
	private Button reportsButton;

	
	// other buttons here
	

	private Button cancelButton;

	private MessageView statusLog;
	private DropShadow shadow = new DropShadow();

	// constructor for this class -- takes a model object
	//----------------------------------------------------------
	public AssessmentCoordinatorView(IModel aCoord)
	{
		super(aCoord, "AssessmentCoordinatorView");

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
                
		return new CommonTitleWithLogoPanel();
	}


	// Create the navigation buttons
	//-------------------------------------------------------------
	private VBox createFormContents()
	{

		VBox container = new VBox(15);
		// create the buttons, listen for events, add them to the container
		container.setPadding(new Insets(5, 5, 5, 5));

		HBox geAreaCont = new HBox(10);
		geAreaCont.setAlignment(Pos.CENTER);

		addGenEdAreaButton = new Button("Add a new Gen Ed Area");
		addGenEdAreaButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
		addGenEdAreaButton.setOnAction((ActionEvent e) -> {
			myModel.stateChangeRequest("AddNewGenEdArea", null);
		});

		updateGenEdAreaButton = new Button("Update Existing Gen Ed Area");
		updateGenEdAreaButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
		updateGenEdAreaButton.setOnAction((ActionEvent e) -> {
			myModel.stateChangeRequest("UpdateGenEdArea", null);
		});

		deleteGenEdAreaButton = new Button("Delete Existing Gen Ed Area");
		deleteGenEdAreaButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
		deleteGenEdAreaButton.setOnAction((ActionEvent e) -> {
			myModel.stateChangeRequest("DeleteGenEdArea", null);
		});

		geAreaCont.getChildren().add(addGenEdAreaButton);
		geAreaCont.getChildren().add(updateGenEdAreaButton);
		geAreaCont.getChildren().add(deleteGenEdAreaButton);

		HBox geSLOCont = new HBox(10);
		geSLOCont.setAlignment(Pos.CENTER);

		addGESLOButton = new Button("Add a new Gen Ed Area SLO");
		addGESLOButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
		addGESLOButton.setOnAction((ActionEvent e) -> {
			myModel.stateChangeRequest("AddNewGESLO", null);
		});

		updateGESLOButton = new Button("Update Existing Gen Ed Area SLO");
		updateGESLOButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
		updateGESLOButton.setOnAction((ActionEvent e) -> {
			myModel.stateChangeRequest("UpdateGESLO", null);
		});

		geSLOCont.getChildren().add(addGESLOButton);
		geSLOCont.getChildren().add(updateGESLOButton);
		
		HBox semCont = new HBox(10);
		semCont.setAlignment(Pos.CENTER);

		addSemesterButton = new Button("Add a new Semester");
		addSemesterButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
		addSemesterButton.setOnAction((ActionEvent e) -> {
			myModel.stateChangeRequest("AddNewSemester", null);
		});

		updateSemesterButton = new Button("Update Existing Semester");
		updateSemesterButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
		updateSemesterButton.setOnAction((ActionEvent e) -> {
			myModel.stateChangeRequest("UpdateSemester", null);
		});


		semCont.getChildren().add(addSemesterButton);
		semCont.getChildren().add(updateSemesterButton);
	
		HBox offeringCont = new HBox(10);
		offeringCont.setAlignment(Pos.CENTER);

		addOfferingButton = new Button("Link ISLO to Semester");
		addOfferingButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
		addOfferingButton.setOnAction((ActionEvent e) -> {
			myModel.stateChangeRequest("AddNewOffering", null);
		});

		updateOfferingButton = new Button("Update ISLO-Semester Link");
		updateOfferingButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
		updateOfferingButton.setOnAction((ActionEvent e) -> {
			myModel.stateChangeRequest("UpdateOffering", null);
		});


		offeringCont.getChildren().add(addOfferingButton);
		offeringCont.getChildren().add(updateOfferingButton);

		HBox offeringTeacherCont = new HBox(10);
		offeringTeacherCont.setAlignment(Pos.CENTER);

		addOfferingTeacherButton = new Button("Add a new Sampled Course");
		addOfferingTeacherButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
		addOfferingTeacherButton.setOnAction((ActionEvent e) -> {
			myModel.stateChangeRequest("AddNewOfferingTeacher", null);
		});

		updateOfferingTeacherButton = new Button("Update Sampled Course");
		updateOfferingTeacherButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
		updateOfferingTeacherButton.setOnAction((ActionEvent e) -> {
			myModel.stateChangeRequest("UpdateOfferingTeacher", null);
		});

		deleteOfferingTeacherButton = new Button("Remove Sampled Course");
		deleteOfferingTeacherButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
		deleteOfferingTeacherButton.setOnAction((ActionEvent e) -> {
			myModel.stateChangeRequest("DeleteOfferingTeacher", null);
		});



		offeringTeacherCont.getChildren().add(addOfferingTeacherButton);
		offeringTeacherCont.getChildren().add(updateOfferingTeacherButton);
	offeringTeacherCont.getChildren().add(deleteOfferingTeacherButton);

		HBox studentCategorizationCont = new HBox(10);
		studentCategorizationCont.setAlignment(Pos.CENTER);

		addStudentCategorizationButton = new Button("Add new Student Performance Data/ Reflections");
	addStudentCategorizationButton.setFont(Font.font("Comic Sans", FontWeight.BOLD, 14));
	addStudentCategorizationButton.setOnAction((ActionEvent e) -> {
			myModel.stateChangeRequest("AddNewStudentCategorization", null);
		});

		updateStudentCategorizationButton = new Button("Update Student Performance Data/ Reflections");
	updateStudentCategorizationButton.setFont(Font.font("Comic Sans", FontWeight.BOLD, 14));
	updateStudentCategorizationButton.setOnAction((ActionEvent e) -> {
			myModel.stateChangeRequest("UpdateStudentCategorization", null);
		});

		studentCategorizationCont.getChildren().add(addStudentCategorizationButton);
		studentCategorizationCont.getChildren().add(updateStudentCategorizationButton);
	
		HBox categNameCont = new HBox(10);
		categNameCont.setAlignment(Pos.CENTER);

		addCategoryNameButton = new Button("Add a new Performance Category Name");
		addCategoryNameButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
		addCategoryNameButton.setOnAction((ActionEvent e) -> {
			myModel.stateChangeRequest("AddNewCategoryName", null);
		});

		updateCategoryNameButton = new Button("Update Existing Performance Category Name");
		updateCategoryNameButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
		updateCategoryNameButton.setOnAction((ActionEvent e) -> {
			myModel.stateChangeRequest("UpdateCategoryName", null);
		});

		categNameCont.getChildren().add(addCategoryNameButton);
		categNameCont.getChildren().add(updateCategoryNameButton);
		
		HBox reflectionsCont = new HBox(10);
		reflectionsCont.setAlignment(Pos.CENTER);

		addReflectionQuesButton = new Button("Add a new Reflection Question");
		addReflectionQuesButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
		addReflectionQuesButton.setOnAction((ActionEvent e) -> {
			myModel.stateChangeRequest("AddNewReflectionQuestion", null);
		});

		updateReflectionQuesButton = new Button("Update Existing Reflection Question");
		updateReflectionQuesButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
		updateReflectionQuesButton.setOnAction((ActionEvent e) -> {
			myModel.stateChangeRequest("UpdateReflectionQuestion", null);
		});

		reflectionsCont.getChildren().add(addReflectionQuesButton);
		reflectionsCont.getChildren().add(updateReflectionQuesButton);

		HBox reportsCont = new HBox(10);
		reportsCont.setAlignment(Pos.CENTER);

		reportsButton = new Button("REPORTS");
		reportsButton.setFont(Font.font("Comic Sans", FontWeight.BOLD, 16));
		reportsButton.setOnAction((ActionEvent e) -> {
			myModel.stateChangeRequest("Reports", null);
		});
	reportsButton.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
			reportsButton.setEffect(shadow);
                        statusLog.displayInfoMessage("Go to Generate Reports Screen");
		});
		reportsButton.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
			reportsButton.setEffect(null);
                        clearErrorMessage();
		});
		
		reportsCont.getChildren().add(reportsButton);


		HBox doneCont = new HBox(10);
		doneCont.setAlignment(Pos.CENTER);

		cancelButton = new Button("Exit System");
		cancelButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
		cancelButton.setOnAction((ActionEvent e) -> {
			myModel.stateChangeRequest("ExitSystem", null);
		});
		cancelButton.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
			cancelButton.setEffect(shadow);
                        statusLog.displayInfoMessage("Close Application");
		});
		cancelButton.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
			cancelButton.setEffect(null);
                        clearErrorMessage();
		});
		
		doneCont.getChildren().add(cancelButton);

		container.getChildren().add(geAreaCont);
		container.getChildren().add(geSLOCont);
		container.getChildren().add(semCont);
		container.getChildren().add(offeringCont);
		container.getChildren().add(offeringTeacherCont);
		container.getChildren().add(studentCategorizationCont);
		container.getChildren().add(categNameCont);
		container.getChildren().add(reflectionsCont);
		//container.getChildren().add(new HBox(10));
		container.getChildren().add(reportsCont);

		container.getChildren().add(doneCont);

		return container;
	}

	// Create the status log field
	//-------------------------------------------------------------
	private MessageView createStatusLog(String initialMessage)
	{

		statusLog = new MessageView(initialMessage);

		return statusLog;
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
            
	}
        


	//---------------------------------------------------------
	public void updateState(String key, Object value)
	{
		if (key.equals("TransactionError") == true)
		{
			// display the passed text
			displayErrorMessage((String)value);
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
	 * Clear error message
	 */
	//----------------------------------------------------------
	public void clearErrorMessage()
	{
		statusLog.clearErrorMessage();
	}
}


