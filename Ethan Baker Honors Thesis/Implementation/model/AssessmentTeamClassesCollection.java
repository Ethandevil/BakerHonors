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


/** The class containing the Offering Teacher Collection for the ISLO Data
 * Management application
 */
//==============================================================
public class AssessmentTeamClassesCollection  extends EntityBase implements IView
{
    private static final String myTableName = "AssessmentTeamClasses";

    private Vector<AssessmentTeamClasses> assessmentTeamClasses;
    // GUI Components

    // constructor for this class
    //----------------------------------------------------------
    public AssessmentTeamClassesCollection( )
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
            assessmentTeamClasses = new Vector<AssessmentTeamClasses>();

            for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
            {
                Properties nextAssessmentTeamClassesData = allDataRetrieved.elementAt(cnt);

                AssessmentTeamClasses assmntTeamClasses = new AssessmentTeamClasses(nextAssessmentTeamClassesData);

                if (assmntTeamClasses != null)
                {
                    addAssessmentTeamClasses(assmntTeamClasses);
                }
            }

        }
    }

    //-----------------------------------------------------------
    public void findByAssessmentTeamId(String offId)
    {
        String query = "SELECT * FROM " + myTableName + " WHERE (AssessmentTeamID = " + offId + ")";

        populateCollectionHelper(query);
    }

    //-----------------------------------------------------------
    public void findAll()
    {
        String query = "SELECT * FROM " + myTableName + "";
        populateCollectionHelper(query);
    }


    //----------------------------------------------------------------------------------
    private void addAssessmentTeamClasses(AssessmentTeamClasses atc)
    {
        int index = findIndexToAdd(atc);
        assessmentTeamClasses.insertElementAt(atc,index); // To build up a collection sorted on some key
    }

    //----------------------------------------------------------------------------------
    private int findIndexToAdd(AssessmentTeamClasses atc)
    {
        int low=0;
        int high = assessmentTeamClasses.size()-1;
        int middle;

        while (low <=high)
        {
            middle = (low+high)/2;

            AssessmentTeamClasses midSession = assessmentTeamClasses.elementAt(middle);

            int result = AssessmentTeamClasses.compare(atc,midSession);

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
        if (key.equals("AssessmentTeamClasses"))
            return assessmentTeamClasses;
        else
        if (key.equals("AssessmentTeamClassesList"))
            return this;
        return null;
    }

    //----------------------------------------------------------------
    public void stateChangeRequest(String key, Object value)
    {
        myRegistry.updateSubscribers(key, this);
    }

    //----------------------------------------------------------
    public AssessmentTeamClasses retrieve(String assessmentTeamClassesID)
    {
        AssessmentTeamClasses retValue = null;
        for (int cnt = 0; cnt < assessmentTeamClasses.size(); cnt++)
        {
            AssessmentTeamClasses nextAssessmentTeamClasses = assessmentTeamClasses.elementAt(cnt);
            String nextAssessmentTeamClassesId = (String)nextAssessmentTeamClasses.getState("ID");
            if (nextAssessmentTeamClassesId.equals(""+assessmentTeamClassesID) == true)
            {
                retValue = nextAssessmentTeamClasses;
                return retValue; // we should say 'break;' here
            }
        }

        return retValue;
    }

    public Vector getAssessmentTeamClassesDisplays()
    {
        return assessmentTeamClasses;
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
