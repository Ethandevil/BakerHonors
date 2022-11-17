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
import java.util.Properties;
import java.util.Vector;

// project imports

import impresario.IView;


/** The class containing the Offering Collection for the Gen Ed Assessment Data
 * Management application 
 */
//==============================================================
public class AssessmentTeamCollection  extends EntityBase implements IView
{
	private static final String myTableName = "AssessmentTeam";

	private Vector<AssessmentTeam> assessmentTeams;
	// GUI Components

	// constructor for this class
	//----------------------------------------------------------
	public AssessmentTeamCollection( )
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
			assessmentTeams = new Vector<AssessmentTeam>();

			for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
			{
				Properties nextAssessmentTeamData = allDataRetrieved.elementAt(cnt);

				AssessmentTeam assmnttm = new AssessmentTeam(nextAssessmentTeamData);

				if (assmnttm != null)
				{
					addAssessmentTeam(assmnttm);
				}
			}

		}
	}

	//-----------------------------------------------------------
	public void findByGenEdAreaId(String genEdAreaId)
	{
		String query = "SELECT * FROM " + myTableName + " WHERE (GenEdAreaID = " + genEdAreaId + ")";
		
		populateCollectionHelper(query);
	}

	//-----------------------------------------------------------
	public void findAll()
	{
		String query = "SELECT * FROM " + myTableName + "";
		populateCollectionHelper(query);
	}

	
	//----------------------------------------------------------------------------------
	private void addAssessmentTeam(AssessmentTeam a)
	{
		int index = findIndexToAdd(a);
		assessmentTeams.insertElementAt(a,index); // To build up a collection sorted on some key
	}

	//----------------------------------------------------------------------------------
	private int findIndexToAdd(AssessmentTeam o)
	{
		int low=0;
		int high = assessmentTeams.size()-1;
		int middle;

		while (low <=high)
		{
			middle = (low+high)/2;

			AssessmentTeam midSession = assessmentTeams.elementAt(middle);

			int result = AssessmentTeam.compare(o,midSession);

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
		if (key.equals("AssessmentTeams"))
			return assessmentTeams;
		else
			if (key.equals("AssessmentTeamList"))
				return this;
		return null;
	}

	//----------------------------------------------------------------
	public void stateChangeRequest(String key, Object value)
	{
		myRegistry.updateSubscribers(key, this);
	}

	//----------------------------------------------------------
	public AssessmentTeam retrieve(String assessmentTeamID)
	{
		AssessmentTeam retValue = null;
		for (int cnt = 0; cnt < assessmentTeams.size(); cnt++)
		{
			AssessmentTeam nextAssessmentTeam = assessmentTeams.elementAt(cnt);
			String nextAssessmentTeamId = (String)nextAssessmentTeam.getState("ID");
			if (nextAssessmentTeamId.equals(""+assessmentTeamID) == true)
			{
				retValue = nextAssessmentTeam;
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
