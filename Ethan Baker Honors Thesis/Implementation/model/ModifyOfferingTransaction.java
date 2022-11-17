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
import utilities.GlobalVariables;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.util.Properties;
import java.util.Vector;

// project imports
import event.Event;
import exception.InvalidPrimaryKeyException;
import exception.MultiplePrimaryKeysException;

import userinterface.View;
import userinterface.ViewFactory;

/** The class containing the UpdateOfferingTransaction for the Gen Ed Assessment Data Management application */
//==============================================================
public class ModifyOfferingTransaction extends Transaction
{

	private GenEdAreaCollection myGenEdAreaList;
	private GenEdArea mySelectedGenEdArea;
	private SemesterCollection mySemesterList;
	private Semester mySelectedSemester;
	private OfferingCollection myOfferingList;
	private OfferingDisplayCollection myOfferingDisplayList;
	private Offering mySelectedOffering;


	// GUI Components

	private String transactionErrorMessage = "";

	/**
	 * Constructor for this class.
	 *
	 */
	//----------------------------------------------------------
	public ModifyOfferingTransaction() throws Exception
	{
		super();
	}

	//----------------------------------------------------------
	protected void setDependencies()
	{
		dependencies = new Properties();
		dependencies.setProperty("CancelSemesterList", "CancelTransaction");
		dependencies.setProperty("CancelSearchArea", "CancelTransaction");
		dependencies.setProperty("CancelAreaList", "CancelTransaction");
		dependencies.setProperty("CancelOfferingList", "CancelTransaction");
		dependencies.setProperty("SemesterSelected", "TransactionError");

		myRegistry.setDependencies(dependencies);
	}

	/**
	 * This method encapsulates all the logic of creating the Offering,
	 * verifying its uniqueness, etc.
	 */
	//----------------------------------------------------------
	public void processTransaction(GenEdArea genEdArea, Semester sem)
	{
		String genEdAreaId;
		String semId;

		genEdAreaId = (String)genEdArea.getState("GenEdID");
		semId = (String)sem.getState("ID");
		
		try {
			Offering newOffering = new Offering(genEdAreaId, semId);
			transactionErrorMessage = "ERROR: Chosen semester (" + mySelectedSemester.getSemester() +
					" " + mySelectedSemester.getYear() + ") and Gen Ed Area are already linked!";
			}
			catch (InvalidPrimaryKeyException ex) {
				mySelectedOffering.stateChangeRequest("SemesterID", semId);
				mySelectedOffering.update();
				transactionErrorMessage = (String)mySelectedOffering.getState("UpdateStatusMessage");

			}
			catch (MultiplePrimaryKeysException ex) {
					transactionErrorMessage = "ERROR: Chosen semester (" + mySelectedSemester.getSemester() +
					" " + mySelectedSemester.getYear() + ") and Gen Ed Area are already linked multiple times!";

			}

	}

	//-----------------------------------------------------------
	public Object getState(String key)
	{
		
		if (key.equals("SemesterList") == true)
		{
			return mySemesterList;
		}
		else
		if (key.equals("AreaList") == true)
		{
			return myGenEdAreaList;
		}
		else
		if (key.equals("OfferingList") == true)
		{
			return myOfferingList;
		}
		else
		if (key.equals("OfferingDisplayList") == true)
		{
			return myOfferingDisplayList;
		}
		else
		if (key.equals("TransactionError") == true)
		{
			return transactionErrorMessage;
		}
		else
		if (key.equals("AreaName") == true)
		{
			if (mySelectedGenEdArea != null)
				return mySelectedGenEdArea.getState("AreaName");
			else
				return "";
		}


		return null;
	}

	//-----------------------------------------------------------
	public void stateChangeRequest(String key, Object value)
	{
		// DEBUG System.out.println("AddOfferingTransaction.sCR: key: " + key);

		if (key.equals("DoYourJob") == true)
		{
			doYourJob();
		}
		else
		if (key.equals("OfferingSelected") == true)
		{
			String offeringId = (String)value;

			mySelectedOffering = myOfferingList.retrieve(offeringId);
	
			mySemesterList = new SemesterCollection();
			mySemesterList.findAll();

			try
			{	
				Scene newScene = createSemesterCollectionView();	
				swapToView(newScene);
			}
			catch (Exception ex)
			{
				new Event(Event.getLeafLevelClassName(this), "processTransaction",
					"Error in creating SemesterCollectionView", Event.ERROR);
			}
		}
		else
		if (key.equals("SemesterSelected") == true)
		{
					
			String semIdSent = (String)value;
			int semIdSentVal = Integer.parseInt(semIdSent);
			mySelectedSemester = mySemesterList.retrieve(semIdSentVal);
			processTransaction(mySelectedGenEdArea, mySelectedSemester);
		}
		else
		if (key.equals("SearchArea") == true) {

			Properties props = (Properties)value;

			myGenEdAreaList = new GenEdAreaCollection();
	
			String areaNm = props.getProperty("AreaName");
			String notes = props.getProperty("Notes");
			myGenEdAreaList.findByNameAndNotesPart(areaNm, notes);
		
			try
			{	
				Scene newScene = createGenEdAreaCollectionView();
				swapToView(newScene);
			}
			catch (Exception ex)
			{
				new Event(Event.getLeafLevelClassName(this), "stateChangeRequest",
					"Error in creating GenEdAreaCollectionView", Event.ERROR);
			}

		}
		else
		if (key.equals("AreaSelected") == true)
		{
			
			String genEdAreaNameSent = (String)value;
			int genEdAreaNameSentVal = Integer.parseInt(genEdAreaNameSent);
			mySelectedGenEdArea = myGenEdAreaList.retrieve(genEdAreaNameSentVal);

			myOfferingList = new OfferingCollection();
			myOfferingList.findByGenEdAreaId((String)mySelectedGenEdArea.getState("ID"));
			myOfferingDisplayList = new OfferingDisplayCollection(myOfferingList);
			
			try
			{
				Scene newScene = createOfferingDisplayCollectionView();
				swapToView(newScene);
			}
			catch (Exception ex) {
				new Event(Event.getLeafLevelClassName(this), "stateChangeRequest",
					"Error in creating OfferingDisplayCollectionView", Event.ERROR);
			}
		}

		myRegistry.updateSubscribers(key, this);
	}

	/**
	 * Create the view of this class. And then the super-class calls
	 * swapToView() to display the view in the frame
	 */
	//------------------------------------------------------
	protected Scene createView()
	{
		Scene currentScene = myViews.get("SearchGenEdAreaForModifyingOfferingView");

		if (currentScene == null)
		{
			// create our initial view
			View newView = ViewFactory.createView("SearchGenEdAreaForModifyingOfferingView", this);
			currentScene = new Scene(newView);
			myViews.put("SearchAreaForModifyingOfferingView", currentScene);

			return currentScene;
		}
		else
		{
			return currentScene;
		}
	}

	
	/**
	 * Create the view containing the table of all matching Semesters on the search criteria sent
	 */
	//------------------------------------------------------
	protected Scene createSemesterCollectionView()
	{
		View newView = ViewFactory.createView("SemesterCollectionForModifyOfferingView", this);
		Scene currentScene = new Scene(newView);

		return currentScene;

	}

	//------------------------------------------------------
	protected Scene createOfferingDisplayCollectionView()
	{
		
		
		Scene currentScene = myViews.get("OfferingDisplayCollectionView");

		if (currentScene == null)
		{
			// create our initial view
			View newView = ViewFactory.createView("OfferingDisplayCollectionView", this);
			currentScene = new Scene(newView);
			myViews.put("OfferingDisplayCollectionView", currentScene);

			return currentScene;
		}
		else
		{
			return currentScene;
		}

	}

	//------------------------------------------------------
	protected Scene createGenEdAreaCollectionView()
	{
		View newView = ViewFactory.createView("GenEdAreaCollectionForModifyingOfferingView", this);
		Scene currentScene = new Scene(newView);

		return currentScene;

	}


}

