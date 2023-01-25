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
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;


// project imports
import exception.InvalidPrimaryKeyException;
import exception.MultiplePrimaryKeysException;

import impresario.IView;

/** The class containing the Offering information for the ISLO Data Management
 * application
 */
//==============================================================
public class AssessmentTeamClasses extends EntityBase implements IView
{
    private static final String myTableName = "AssessmentTeamClasses";

    protected Properties dependencies;

    // GUI Components
    private String updateStatusMessage = "";

    // constructor for this class
    //----------------------------------------------------------
    public AssessmentTeamClasses(String atcId) throws InvalidPrimaryKeyException, MultiplePrimaryKeysException
    {
        super(myTableName);

        setDependencies();

        String query = "SELECT * FROM " + myTableName + " WHERE (ID = " + atcId + ")";

        queryHelper(query, "No Matching Assessment Team Classes for linked course with: " + " Id: " + atcId + "found.",
                "Multiple Assessment Team Classes matching " + " linked assessment team class Id: " + atcId + "found.");
    }

    // constructor for this class
    //----------------------------------------------------------
    public AssessmentTeamClasses(String crsCode, String crsNum) throws
            InvalidPrimaryKeyException, MultiplePrimaryKeysException
    {
        super(myTableName);

        setDependencies();

        String query = "SELECT * FROM " + myTableName + " WHERE ((CourseDisciplineCode = '" + crsCode + "')" +
                " AND (CourseNumber = '" + crsNum + "'))";

        queryHelper(query, "No Matching Sampled courses for linked course with" + " data: (" +
                        crsCode + " " + crsNum + ") found.",
                "Multiple Sampled courses matching " + " linked course with" + " data: (" +
                        crsCode + " " + crsNum + ") found.");
    }

    //------------------------------------------------------------------------------
    private void queryHelper(String query, String mesg1, String mesg2) throws InvalidPrimaryKeyException, MultiplePrimaryKeysException {
        Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

        // You must get one semester at least
        if (allDataRetrieved != null)
        {
            int size = allDataRetrieved.size();
            // if size = 0 throw the Invalid Primary Key Exception
            if (size == 0)
            {
                throw new InvalidPrimaryKeyException(mesg1);
            }
            else
                // There should be EXACTLY one offering. More than that is an error
                if (size != 1)
                {

                    throw new MultiplePrimaryKeysException(mesg2);
                }
                else
                {
                    // copy all the retrieved data into persistent state
                    Properties retrievedOfferingData = allDataRetrieved.elementAt(0);
                    persistentState = new Properties();

                    Enumeration allKeys = retrievedOfferingData.propertyNames();
                    while (allKeys.hasMoreElements() == true)
                    {
                        String nextKey = (String)allKeys.nextElement();
                        String nextValue = retrievedOfferingData.getProperty(nextKey);


                        if (nextValue != null)
                        {
                            persistentState.setProperty(nextKey, nextValue);
                        }
                    }

                }
        }
        // If offering not found for this data, throw an Invalid Primary key exception
        else
        {
            throw new InvalidPrimaryKeyException(mesg1);
        }
    }

    /**
     * Alternate constructor. Can be used to create a NEW offering
     */
    //----------------------------------------------------------
    public AssessmentTeamClasses(Properties props)
    {
        super(myTableName);

        setDependencies();
        persistentState = new Properties();
        Enumeration allKeys = props.propertyNames();
        while (allKeys.hasMoreElements() == true)
        {
            String nextKey = (String)allKeys.nextElement();
            String nextValue = props.getProperty(nextKey);

            if (nextValue != null)
            {
                persistentState.setProperty(nextKey, nextValue);
            }
        }
    }

    //-----------------------------------------------------------------------------------
    private void setDependencies()
    {
        dependencies = new Properties();

        myRegistry.setDependencies(dependencies);
    }

    //----------------------------------------------------------
    public Object getState(String key)
    {
        if (key.equals("UpdateStatusMessage") == true)
            return updateStatusMessage;

        return persistentState.getProperty(key);
    }

    //----------------------------------------------------------------
    public void stateChangeRequest(String key, Object value)
    {
        if (persistentState.getProperty(key) != null)
        {
            persistentState.setProperty(key, (String)value);
        }
        myRegistry.updateSubscribers(key, this);
    }

    /** Called via the IView relationship */
    //----------------------------------------------------------
    public void updateState(String key, Object value)
    {
        stateChangeRequest(key, value);
    }


    //-----------------------------------------------------------------------------------
    public static int compare(AssessmentTeamClasses a, AssessmentTeamClasses b)
    {
        String aVal = (String)a.getState("CourseDisciplineCode");
        String bVal = (String)b.getState("CourseDisciplineCode");
		
		if (aVal.equals(bVal) == true) {
			aVal = (String)a.getState("CourseNumber");
			bVal = (String)b.getState("CourseNumber");
		}

        return aVal.compareTo(bVal);
    }

    //-----------------------------------------------------------------------------------
    public void setUndeleteable()
    {
        persistentState.setProperty("Deletable", "No");
    }

    //-----------------------------------------------------------------------------------
    public void remove()
    {
        String deleteAbleState = persistentState.getProperty("Deletable");
        if ((deleteAbleState != null) && (deleteAbleState.equals("Yes") == true)) {
            try {
                deletePersistentState(mySchema, persistentState);
                updateStatusMessage = "Selected sampled course successfully removed!";
            }
            catch (SQLException ex) {
                updateStatusMessage = "Error in deleting selected sampled course";
            }
        }
        else if ((deleteAbleState != null) && (deleteAbleState.equals("No") == true)) {
            updateStatusMessage = "Selected sampled course cannot be deleted";
        }
        else {
            updateStatusMessage = "Kyle screwed up";
        }
    }

    //-----------------------------------------------------------------------------------
    public void update()
    {
        updateStateInDatabase();
    }

    //-----------------------------------------------------------------------------------
    private void updateStateInDatabase()
    {
            try
            {
                if (persistentState.getProperty("ID") != null)
                {
                    Properties whereClause = new Properties();
                    whereClause.setProperty("ID", persistentState.getProperty("ID"));
                    updatePersistentState(mySchema, persistentState, whereClause);
                    updateStatusMessage = "Course updated successfully for selected Gen Ed Area and Semester";
                }

                else
                {
                    Integer categoryID =
                            insertAutoIncrementalPersistentState(mySchema, persistentState);
                    persistentState.setProperty("ID", "" + categoryID.intValue());
                    updateStatusMessage = "Course added successfully for selected Gen Ed Area and Semester";
                }


            }
            catch (SQLException ex)
            {
                updateStatusMessage = "ERROR in adding Course for selected Gen Ed Area/Semester!";
            }
            //DEBUG System.out.println("updateStateInDatabase " + updateStatusMessage);

    }

    /**
     * This method is needed solely to enable the Offering information to be displayable in a table
     *
     */
    //--------------------------------------------------------------------------
    public Vector<String> getEntryListView()
    {
        Vector<String> v = new Vector<String>();
        v.addElement(persistentState.getProperty("ID"));
        v.addElement(persistentState.getProperty("AssessmentTeamID"));
        v.addElement(persistentState.getProperty("CourseDisciplineCode"));
        v.addElement(persistentState.getProperty("CourseNumber"));

        return v;
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

