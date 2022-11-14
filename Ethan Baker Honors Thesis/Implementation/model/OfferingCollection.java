// tabs=4
//************************************************************
//	COPYRIGHT 2021, Kyle D. Adams, Matthew E. Morgan and 
//   Sandeep Mitra, State University of New York. - Brockport
//   (SUNY Brockport) 
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
import java.util.Properties;
import java.util.Vector;
import javafx.scene.Scene;

// project imports
import exception.InvalidPrimaryKeyException;
import event.Event;
import database.*;

import impresario.IView;

import userinterface.View;
import userinterface.ViewFactory;


/** The class containing the Offering Collection for the ISLO Data 
 * Management application 
 */
//==============================================================
public class OfferingCollection  extends EntityBase implements IView
{
	private static final String myTableName = "Offering";

	private Vector<Offering> offerings;
	// GUI Components

	// constructor for this class
	//----------------------------------------------------------
	public OfferingCollection( ) 
	{
		super(myTableName);

	}

	//-----------------------------------------------------------
	private void populateCollectionHelper(String query)
	{

		Vector<Properties> allDataRetrieved =
			 getSelectQueryResult(query);

		if (allDataRetrieved != null)
		{
			offerings = new Vector<Offering>();

			for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
			{
				Properties nextOfferingData = allDataRetrieved.elementAt(cnt);

				Offering ofrng = new Offering(nextOfferingData);

				if (ofrng != null)
				{
					addOffering(ofrng);
				}
			}

		}
	}

	//-----------------------------------------------------------
	public void findByISLOId(String isloId)
	{
		String query = "SELECT * FROM " + myTableName + " WHERE (ISLOID = " + isloId + ")";
		
		populateCollectionHelper(query);
	}

	//-----------------------------------------------------------
	public void findAll()
	{
		String query = "SELECT * FROM " + myTableName + "";
		populateCollectionHelper(query);
	}

	
	//----------------------------------------------------------------------------------
	private void addOffering(Offering o)
	{
		int index = findIndexToAdd(o);
		offerings.insertElementAt(o,index); // To build up a collection sorted on some key
	}

	//----------------------------------------------------------------------------------
	private int findIndexToAdd(Offering o)
	{
		int low=0;
		int high = offerings.size()-1;
		int middle;

		while (low <=high)
		{
			middle = (low+high)/2;

			Offering midSession = offerings.elementAt(middle);

			int result = Offering.compare(o,midSession);

			if (result ==0)
			{
				return middle;
			}
			else if (result<0)
			{
				high=middle-1;
			}
			else
			{
				low=middle+1;
			}
		}
		return low;
	}


	/**
	 *
	 */
	//----------------------------------------------------------
	public Object getState(String key)
	{
		if (key.equals("Offerings"))
			return offerings;
		else
			if (key.equals("OfferingList"))
				return this;
		return null;
	}

	//----------------------------------------------------------------
	public void stateChangeRequest(String key, Object value)
	{
		myRegistry.updateSubscribers(key, this);
	}

	//----------------------------------------------------------
	public Offering retrieve(String offeringID)
	{
		Offering retValue = null;
		for (int cnt = 0; cnt < offerings.size(); cnt++)
		{
			Offering nextOffering = offerings.elementAt(cnt);
			String nextOfferingId = (String)nextOffering.getState("ID");
			if (nextOfferingId.equals(""+offeringID) == true)
			{
				retValue = nextOffering;
				return retValue; // we should say 'break;' here
			}
		}

		return retValue;
	}


	/** Called via the IView relationship */
	//----------------------------------------------------------
	public void updateState(String key, Object value)
	{
		stateChangeRequest(key, value);
	}


	//-----------------------------------------------------------------------------------
	protected void initializeSchema(String tableName)
	{
		if (mySchema == null)
		{
			mySchema = getSchemaInfo(tableName);
		}
	}
}
