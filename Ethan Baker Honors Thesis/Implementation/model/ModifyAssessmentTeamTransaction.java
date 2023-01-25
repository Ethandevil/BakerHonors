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
import javafx.scene.Scene;
import java.util.Properties;

// project imports
import event.Event;
import exception.InvalidPrimaryKeyException;
import exception.MultiplePrimaryKeysException;

import userinterface.View;
import userinterface.ViewFactory;

/** The class containing the ModifyAssessmentTeamTransaction for the Gen Ed Assessment Data Management application */
//==============================================================
public class ModifyAssessmentTeamTransaction extends Transaction
{

	private GenEdAreaCollection myGenEdAreaList;
	private GenEdArea mySelectedGenEdArea;
	private SemesterCollection mySemesterList;
	private Semester mySelectedSemester;
	private AssessmentTeamCollection myAssessmentTeamList;
	private AssessmentTeamDisplayCollection myAssessmentTeamDisplayList;
	private AssessmentTeam mySelectedAssessmentTeam;


	// GUI Components

	private String transactionErrorMessage = "";

	/**
	 * Constructor for this class.
	 *
	 */
	//----------------------------------------------------------
	public ModifyAssessmentTeamTransaction() throws Exception
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
		dependencies.setProperty("CancelAssessmentTeamList", "CancelTransaction");
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
			AssessmentTeam newAssessmentTeam = new AssessmentTeam(genEdAreaId, semId);
			transactionErrorMessage = "ERROR: Chosen semester (" + mySelectedSemester.getSemester() +
					" " + mySelectedSemester.getYear() + ") and Gen Ed Area are already linked!";
			}
			catch (InvalidPrimaryKeyException ex) {
				mySelectedAssessmentTeam.stateChangeRequest("SemesterID", semId);
				mySelectedAssessmentTeam.update();
				transactionErrorMessage = (String)mySelectedAssessmentTeam.getState("UpdateStatusMessage");

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
		if (key.equals("GenEdAreaList") == true)
		{
			return myGenEdAreaList;
		}
		else
		if (key.equals("AssessmentTeamList") == true)
		{
			return myAssessmentTeamList;
		}
		else
		if (key.equals("AssessmentTeamDisplayList") == true)
		{
			return myAssessmentTeamDisplayList;
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
		if (key.equals("AssessmentTeamSelected") == true)
		{
			String assessmentTeamId = (String)value;

			mySelectedAssessmentTeam = myAssessmentTeamList.retrieve(assessmentTeamId);
	
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
		if (key.equals("GenEdAreaSelected") == true)
		{
			
			String genEdAreaNameSent = (String)value;
			int genEdAreaNameSentVal = Integer.parseInt(genEdAreaNameSent);
			mySelectedGenEdArea = myGenEdAreaList.retrieve(genEdAreaNameSentVal);

			myAssessmentTeamList = new AssessmentTeamCollection();
			myAssessmentTeamList.findByGenEdAreaId((String)mySelectedGenEdArea.getState("ID"));
			myAssessmentTeamDisplayList = new AssessmentTeamDisplayCollection(myAssessmentTeamList);
			
			try
			{
				Scene newScene = createAssessmentTeamDisplayCollectionView();
				swapToView(newScene);
			}
			catch (Exception ex) {
				new Event(Event.getLeafLevelClassName(this), "stateChangeRequest",
					"Error in creating AssessmentTeamDisplayCollectionView", Event.ERROR);
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
		Scene currentScene = myViews.get("SearchGenEdAreaForModifyingAssessmentTeamView");

		if (currentScene == null)
		{
			// create our initial view
			View newView = ViewFactory.createView("SearchGenEdAreaForModifyingAssessmentTeamView", this);
			currentScene = new Scene(newView);
			myViews.put("SearchGenEdAreaForModifyingAssessmentTeamView", currentScene);

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
		View newView = ViewFactory.createView("SemesterCollectionForModifyAssessmentTeamView", this);
		Scene currentScene = new Scene(newView);

		return currentScene;

	}

	//------------------------------------------------------
	protected Scene createAssessmentTeamDisplayCollectionView()
	{
		
		
		Scene currentScene = myViews.get("AssessmentTeamDisplayCollectionView");

		if (currentScene == null)
		{
			// create our initial view
			View newView = ViewFactory.createView("AssessmentTeamDisplayCollectionView", this);
			currentScene = new Scene(newView);
			myViews.put("AssessmentTeamDisplayCollectionView", currentScene);

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
		View newView = ViewFactory.createView("GenEdAreaCollectionForModifyingAssessmentTeamView", this);
		Scene currentScene = new Scene(newView);

		return currentScene;

	}


}

