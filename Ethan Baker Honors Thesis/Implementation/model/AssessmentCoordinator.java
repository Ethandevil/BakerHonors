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
package model;

// system imports
import java.util.Hashtable;
import java.util.Properties;

import javafx.stage.Stage;
import javafx.scene.Scene;

// project imports
import impresario.IModel;
import impresario.ISlideShow;
import impresario.IView;
import impresario.ModelRegistry;

import exception.InvalidPrimaryKeyException;
import event.Event;
import userinterface.MainStageContainer;
import userinterface.View;
import userinterface.ViewFactory;
import userinterface.WindowPosition;

/** The class containing the Assessment Coordinator for the Gen Ed Assmt Data Management application */
//==============================================================
public class AssessmentCoordinator implements IView, IModel
// This class implements all these interfaces (and does NOT extend 'EntityBase')
// because it does NOT play the role of accessing the back-end database tables.
// It only plays a front-end role. 'EntityBase' objects play both roles.
{
	// For Impresario
	private Properties dependencies;
	private ModelRegistry myRegistry;


	// GUI Components
        
	private Hashtable<String, Scene> myViews;
	private Stage myStage;

	private String transactionErrorMessage = "";

	// constructor for this class
	//----------------------------------------------------------
	public AssessmentCoordinator()
	{
		myStage = MainStageContainer.getInstance();
		myViews = new Hashtable<String, Scene>();

		// STEP 3.1: Create the Registry object - if you inherit from
		// EntityBase, this is done for you. Otherwise, you do it yourself
		myRegistry = new ModelRegistry("AssessmentCoordinator");
		if(myRegistry == null)
		{
			new Event(Event.getLeafLevelClassName(this), "AssessmentCoordinator",
					"Could not instantiate Registry", Event.ERROR);
		}

		// STEP 3.2: Be sure to set the dependencies correctly
		setDependencies();

		// Set up the initial view
		createAndShowAssessmentCoordinatorView();
	}

	//-----------------------------------------------------------------------------------
	private void setDependencies()
	{
		dependencies = new Properties();
		//dependencies.setProperty("Login", "LoginError");


		myRegistry.setDependencies(dependencies);
	}

	/**
	 * Method called from client to get the value of a particular field
	 * held by the objects encapsulated by this object.
	 *
	 * @param	key	Name of database column (field) for which the client wants the value
	 *
	 * @return	Value associated with the field
	 */
	//----------------------------------------------------------
	public Object getState(String key)
	{
	
		return "";
	}

	//----------------------------------------------------------------
	public void stateChangeRequest(String key, Object value)
	{
		// STEP 4: Write the sCR method component for the key you
		// just set up dependencies for
		// DEBUG System.out.println("AssessmentCoordinator.sCR: key = " + key);
		if ((key.equals("AddNewGenEdArea") == true) ||
			(key.equals("UpdateGenEdArea")== true) ||
			(key.equals("DeleteGenEdArea")== true) ||
			(key.equals("AddNewGESLO")== true) ||
			(key.equals("UpdateGESLO")== true) ||
			(key.equals("AddNewSemester") == true) ||
			(key.equals("UpdateSemester") == true) ||
			(key.equals("AddNewCategoryName") == true) ||
			(key.equals("UpdateCategoryName") == true) ||
			(key.equals("AddNewOffering") == true) ||
			(key.equals("UpdateOffering") == true) ||
			(key.equals("AddNewOfferingTeacher") == true) ||
			(key.equals("UpdateOfferingTeacher") == true) ||
			(key.equals("DeleteOfferingTeacher") == true) ||
			(key.equals("AddNewStudentCategorization") == true) ||
			(key.equals("UpdateStudentCategorization") == true) ||
			(key.equals("Reports") == true))
		{

			String transType = key;
			transType = transType.trim();
			doTransaction(transType);
		}
		else
		if (key.equals("CancelTransaction") == true)
		{
			createAndShowAssessmentCoordinatorView();
		}
		else
			if (key.equals("ExitSystem") == true)
			{
				System.exit(0);
			}	
			
		
		myRegistry.updateSubscribers(key, this);
	}

	/** Called via the IView relationship */
	//----------------------------------------------------------
	public void updateState(String key, Object value)
	{
		// DEBUG System.out.println("AssessmentCoordinator.updateState: key: " + key);

		stateChangeRequest(key, value);
	}
    
	
	/**
	 * Create a Transaction depending on the Transaction type (deposit,
	 * withdraw, transfer, etc.). Use the AccountHolder holder data to do the
	 * create.
	 */
	//----------------------------------------------------------
	public void doTransaction(String transactionType)
	{
		try
		{
			Transaction trans = TransactionFactory.createTransaction(
					transactionType);

			trans.subscribe("CancelTransaction", this);
			trans.stateChangeRequest("DoYourJob", "");
		}
		catch (Exception ex)
		{
			transactionErrorMessage = "FATAL ERROR: TRANSACTION FAILURE: Unrecognized transaction!!";
			new Event(Event.getLeafLevelClassName(this), "createTransaction",
					"Transaction Creation Failure: Unrecognized transaction " + ex.toString(),
					Event.ERROR);
		} 
	}


	//------------------------------------------------------------
	private void createAndShowAssessmentCoordinatorView()
	{
          //create our initial view
          View newView = ViewFactory.createView("AssessmentCoordinatorView", this); // USE VIEW FACTORY
          Scene currentScene = new Scene(newView);

          swapToView(currentScene);

	}


	/** Register objects to receive state updates. */
	//----------------------------------------------------------
	public void subscribe(String key, IView subscriber)
	{
		// DEBUG: System.out.println("Cager[" + myTableName + "].subscribe");
		// forward to our registry
		myRegistry.subscribe(key, subscriber);
	}

	/** Unregister previously registered objects. */
	//----------------------------------------------------------
	public void unSubscribe(String key, IView subscriber)
	{
		// DEBUG: System.out.println("Cager.unSubscribe");
		// forward to our registry
		myRegistry.unSubscribe(key, subscriber);
	}

	//-----------------------------------------------------------------------------
	public void swapToView(Scene newScene)
	{		
		if (newScene == null)
		{
			System.out.println("AssessmentCoordinator.swapToView(): Missing view for display");
			new Event(Event.getLeafLevelClassName(this), "swapToView",
					"Missing view for display ", Event.ERROR);
			return;
		}

		myStage.setScene(newScene);
		myStage.sizeToScene();


		//Place in center
		WindowPosition.placeCenter(myStage);
	}

}

