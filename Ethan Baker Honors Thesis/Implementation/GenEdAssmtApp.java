//tabs=4
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


// system imports
import java.util.Locale;
import java.util.ResourceBundle;
import java.io.FileOutputStream;
import java.io.File;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

// project imports
import event.Event;
import event.EventLog;
import common.PropertyFile;
import javafx.scene.image.Image;

import model.AssessmentCoordinator;
import userinterface.MainStageContainer;
import userinterface.WindowPosition;


/** The class containing the main program  for the ISLO Data Management application */
//==============================================================
public class GenEdAssmtApp extends Application
{

	private AssessmentCoordinator myAC;		// the main behavior for the application

	/** Main stage of the application */
	private Stage mainStage;

	// start method for this class, the main application object
	//----------------------------------------------------------
	public void start(Stage primaryStage)
	{
	   System.out.println("Brockport Gen Ed Assmt Data Management App Version 1.00");
	   System.out.println("Copyright 2022/23 Ethan L. Baker and Matthew E. Morgan");

           // Create the top-level container (main frame) and add contents to it.
	   MainStageContainer.setStage(primaryStage, "Gen Ed Assmt Data Management App Version 1.00 ");
	   mainStage = MainStageContainer.getInstance();
        mainStage.getIcons().add(new Image("/images/all_in_one_logo.png")); // set small icon in top left to bport icon
	   // Finish setting up the stage (ENABLE THE GUI TO BE CLOSED USING THE TOP RIGHT
	   // 'X' IN THE WINDOW), and show it.
        mainStage.setOnCloseRequest(new EventHandler <javafx.stage.WindowEvent>() {
            @Override
            public void handle(javafx.stage.WindowEvent event) {
                System.exit(0);
            }
           });

       try
	   {
			myAC = new AssessmentCoordinator();
	   }
	   catch(Exception exc)
	   {
			System.err.println("GenEdAssmtApp.GenEdAssmtApp - could not create Assessment Coordinator!");
			new Event(Event.getLeafLevelClassName(this), "GenEdAssmtApp.<init>", "Unable to create Assessment Coordinator object", Event.ERROR);
			exc.printStackTrace();
	   }


  	   WindowPosition.placeCenter(mainStage);

        mainStage.show();
	}


	/** 
	 * The "main" entry point for the application. Carries out actions to
	 * set up the application
	 */
	//----------------------------------------------------------
    public static void main(String[] args)
	{

		launch(args);
	}

}
