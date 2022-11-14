// tabs=4
//************************************************************
//	COPYRIGHT 2021, Kyle D. Adams Sandeep Mitra and Matthew
//  E. Morgan, State University of New York. - Brockport 
//  (SUNY Brockport). ALL RIGHTS RESERVED
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

/** The class containing the AddOfferingTransaction for the ISLO Data Management application */
//==============================================================
public class AddOfferingTransaction extends Transaction
{
	private SemesterCollection mySemesterList;
	private Semester mySelectedSemester;
	private ISLOCollection myISLOList;
	private ISLO mySelectedISLO;


	// GUI Components

	private String transactionErrorMessage = "";

	/**
	 * Constructor for this class.
	 *
	 */
	//----------------------------------------------------------
	public AddOfferingTransaction() throws Exception
	{
		super();
	}

	//----------------------------------------------------------
	protected void setDependencies()
	{
		dependencies = new Properties();
		dependencies.setProperty("CancelSemesterList", "CancelTransaction");
		dependencies.setProperty("CancelSearchISLO", "CancelTransaction");
		dependencies.setProperty("CancelISLOList", "CancelTransaction");
		dependencies.setProperty("ISLOSelected", "TransactionError");

		myRegistry.setDependencies(dependencies);
	}

	/**
	 * This method encapsulates all the logic of creating the Offering,
	 * verifying its uniqueness, etc.
	 */
	//----------------------------------------------------------
	public void processTransaction(Properties props)
	{
		String isloId;
		String semId;

		if (props.getProperty("ISLOID") != null)
		{
			isloId = props.getProperty("ISLOID");
			if (props.getProperty("SemesterID") != null){
				semId = props.getProperty("SemesterID");
				try {
					Offering newOffering = new Offering(isloId, semId);
					transactionErrorMessage = "ERROR: Chosen semester (" + mySelectedSemester.getSemester() +
					" " + mySelectedSemester.getYear() + ") and ISLO are already linked!";				
				}
				catch (InvalidPrimaryKeyException ex) {
					Offering newOffering = new Offering(props);
					newOffering.update();
					transactionErrorMessage = (String)newOffering.getState("UpdateStatusMessage");

				}
				catch (MultiplePrimaryKeysException ex) {
					transactionErrorMessage = "ERROR: Chosen semester (" + mySelectedSemester.getSemester() +
					" " + mySelectedSemester.getYear() + ") and ISLO are already linked multiple times!";

				}

			}
			else
			{
				transactionErrorMessage = "ERROR: Semester Choice missing!";
			}

			
		}
		else 
		{
			transactionErrorMessage = "ERROR: ISLO Choice missing!";

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
		if (key.equals("ISLOList") == true)
		{
			return myISLOList;
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
		// DEBUG System.out.println("AddOfferingTransaction.sCR: key: " + key);

		if (key.equals("DoYourJob") == true)
		{
			doYourJob();
		}
		else
		if (key.equals("SearchSemester") == true)
		{
			Properties props = (Properties)value;

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
		else
		if (key.equals("SemesterSelected") == true)
				{
					
					String semIdSent = (String)value;
					int semIdSentVal =
						Integer.parseInt(semIdSent);
					mySelectedSemester = mySemesterList.retrieve(semIdSentVal);
					try
					{

						Scene newScene = createSearchISLOView();

						swapToView(newScene);

					}
					catch (Exception ex)
					{
						new Event(Event.getLeafLevelClassName(this), "stateChangeRequest",
								"Error in creating SearchISLOView", Event.ERROR);
						ex.printStackTrace();
					}
				}

		else
		if (key.equals("SearchISLO") == true) {

			Properties props = (Properties)value;

			myISLOList = new ISLOCollection();
	
			String isloNm = props.getProperty("ISLOName");
			String desc = props.getProperty("Description");
			myISLOList.findByNameAndDescriptionPart(isloNm, desc);
		
			try
			{	
				Scene newScene = createISLOCollectionView();	
				swapToView(newScene);
			}
			catch (Exception ex)
			{
			new Event(Event.getLeafLevelClassName(this), "stateChangeRequest",
					"Error in creating ISLOCollectionView", Event.ERROR);
			}

		}
		else
			if (key.equals("ISLOSelected") == true)
			{
				String isloNumSent = (String)value;
					int isloNumSentVal =
						Integer.parseInt(isloNumSent);
					mySelectedISLO = myISLOList.retrieve(isloNumSentVal);

				Properties props = new Properties();
				if (mySelectedISLO != null)
				{
					props.setProperty("ISLOID", (String)mySelectedISLO.getState("ID"));
				}
				if (mySelectedSemester != null)
				{
					props.setProperty("SemesterID", (String)mySelectedSemester.getState("ID"));
				}

				processTransaction(props);
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
		Scene currentScene = myViews.get("SearchSemesterForOfferingView");

		if (currentScene == null)
		{
			// create our initial view
			View newView = ViewFactory.createView("SearchSemesterForOfferingView", this);
			currentScene = new Scene(newView);
			myViews.put("SearchSemesterForOfferingView", currentScene);

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
		View newView = ViewFactory.createView("SemesterCollectionForOfferingView", this);
		Scene currentScene = new Scene(newView);

		return currentScene;

	}

	//------------------------------------------------------
	protected Scene createSearchISLOView()
	{
		
		Scene currentScene = myViews.get("SearchISLOForOfferingView");

		if (currentScene == null)
		{
			// create our initial view
			View newView = ViewFactory.createView("SearchISLOForOfferingView", this);
			currentScene = new Scene(newView);
			myViews.put("SearchISLOForOfferingView", currentScene);

			return currentScene;
		}
		else
		{
			return currentScene;
		}

	}

	//------------------------------------------------------
	protected Scene createISLOCollectionView()
	{
		View newView = ViewFactory.createView("ISLOCollectionForOfferingView", this);
		Scene currentScene = new Scene(newView);

		return currentScene;

	}


}

