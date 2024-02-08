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

/** The class containing the UpdateSemesterTransaction for the Gen Ed Assessment Data Management application */
//==============================================================
public class UpdateSemesterTransaction extends Transaction
{

	private SemesterCollection mySemesterList;
	private Semester mySelectedSemester;
     

	// GUI Components

	private String transactionErrorMessage = "";

	/**
	 * Constructor for this class.
	 *
	 */
	//----------------------------------------------------------
	public UpdateSemesterTransaction() throws Exception
	{
		super();
	}

	//----------------------------------------------------------
	protected void setDependencies()
	{
		dependencies = new Properties();
		dependencies.setProperty("CancelSearchSemester", "CancelTransaction");
		dependencies.setProperty("CancelAddSemester", "CancelTransaction");
		dependencies.setProperty("SemesterData", "TransactionError");

		myRegistry.setDependencies(dependencies);
	}

	/**
	 * This method encapsulates all the logic of creating the Semester Collection and showing the view
	 */
	//----------------------------------------------------------
	public void processTransaction(Properties props)
	{
		mySemesterList = new SemesterCollection();
	
		String semNm = props.getProperty("SemName");
		String semYr = props.getProperty("Year");
		mySemesterList.findBySemNameAndYear(semNm, semYr);
		
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

	/**
	 * Helper method for Semester update
	 */
	//--------------------------------------------------------------------------
	private void semesterModificationHelper(Properties props)
	{
		String semNm = props.getProperty("SemName");
		String semYr = props.getProperty("Year");
		int semYrVal = Integer.parseInt(semYr);
		if ((semYrVal < 2000) || (semYrVal > 2100))
		{
			transactionErrorMessage = "Year value must be between 2000 and 2100";
		}
		else
		{
			// Everything OK

			mySelectedSemester.stateChangeRequest("SemName", semNm);
			mySelectedSemester.stateChangeRequest("Year", semYr);
				mySelectedSemester.update();
				transactionErrorMessage = (String)mySelectedSemester.getState("UpdateStatusMessage");
			
		}
	}

	/**
	 * This method encapsulates all the logic of modifiying the Semester,
	 * verifying the year, etc.
	 */
	//----------------------------------------------------------
	private void processSemesterModify(Properties props)
	{
		if (props.getProperty("SemName") != null)
		{
			String semNm = props.getProperty("SemName");
			if (props.getProperty("Year") != null)
			{
				String semYr = props.getProperty("Year");	
				try
				{
					Semester oldSem = new Semester(semNm, semYr);
					transactionErrorMessage = "ERROR: Semester: ( " + semNm + ", " + semYr + ") already exists!";
					new Event(Event.getLeafLevelClassName(this), "processTransaction",
							"Semester: ( " + semNm + ", " + semYr + ") already exists!",
							Event.ERROR);
				}
				catch (InvalidPrimaryKeyException ex)
				{
					// Semester with name and year does not exist, process it
				
						// Helper does all that
								
				semesterModificationHelper(props);
				}
				catch (MultiplePrimaryKeysException ex2)
				{
					transactionErrorMessage = "ERROR: Multiple Semesters with values: (" + semNm + ", " + semYr + ") !";
					new Event(Event.getLeafLevelClassName(this), "processTransaction",
							"Multiple Semesters with values: (" + semNm + ", " + semYr + ")",
							Event.ERROR);

				}
			}
			else
			{
				
				transactionErrorMessage = "ERROR: Year value missing for updating semester!";
					
			}

		}
		else
		{
			transactionErrorMessage = "ERROR: Semester not chosen for updating semester!";

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
			if (key.equals("SemName") == true)
			{
				if (mySelectedSemester != null)
					return mySelectedSemester.getState("SemName");
				else
					return "";
			}
			else
				if (key.equals("Year") == true)
				{
					if (mySelectedSemester != null)
						return mySelectedSemester.getState("Year");
					else
						return "";
				}
				else
					if (key.equals("ID") == true)
					{
						if (mySelectedSemester != null)
							return mySelectedSemester.getState("ID");
						else
							return "";
					}
					else
						if (key.equals("TransactionError") == true)
						{
							return transactionErrorMessage;
						}

		return null;
	}

	//-----------------------------------------------------------
	public void stateChangeRequest(String key, Object value)
	{
		// DEBUG System.out.println("UpdateSemesterTransaction.sCR: key: " + key);

		if ((key.equals("DoYourJob") == true) || (key.equals("CancelSemesterList") == true))
		{
			doYourJob();
		}
		else
			if (key.equals("SearchSemester") == true)
			{
				processTransaction((Properties)value);
			}
			else
				if (key.equals("SemesterSelected") == true)
				{
					String semIdSent = (String)value;
					int semIdSentVal =
						Integer.parseInt(semIdSent);
					mySelectedSemester = mySemesterList.retrieve(semIdSentVal);
					try
					{

						Scene newScene = createModifySemesterView();

						swapToView(newScene);

					}
					catch (Exception ex)
					{
						new Event(Event.getLeafLevelClassName(this), "processTransaction",
								"Error in creating ModifySemesterView", Event.ERROR);
					}
				}
				else
					if (key.equals("SemesterData") == true)
					{
						processSemesterModify((Properties)value);
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
		Scene currentScene = myViews.get("SearchSemesterView");

		if (currentScene == null)
		{
			// create our initial view
			View newView = ViewFactory.createView("SearchSemesterView", this);
			currentScene = new Scene(newView);
			myViews.put("SearchSemesterView", currentScene);

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
		View newView = ViewFactory.createView("SemesterCollectionView", this);
		Scene currentScene = new Scene(newView);

		return currentScene;

	}

	/**
	 * Create the view using which data about selected semester can be modified
	 */
	//------------------------------------------------------
	protected Scene createModifySemesterView()
	{
		View newView = ViewFactory.createView("ModifySemesterView", this);
		Scene currentScene = new Scene(newView);

		return currentScene;

	}

}

