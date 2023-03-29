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
import database.*;

import impresario.IView;

import userinterface.View;
import userinterface.ViewFactory;

/** The class containing the Instructor Reflections information for the Gen Ed Assessment Data Management
 * application
 */
//==============================================================
public class InstructorReflections extends EntityBase implements IView
{
    private static final String myTableName = "InstructorReflections";

    protected Properties dependencies;

    // GUI Components
    private String updateStatusMessage = "";

    // constructor for this class
    //----------------------------------------------------------
    public InstructorReflections(String aTID, String rqID) throws InvalidPrimaryKeyException,
            MultiplePrimaryKeysException
    {
        super(myTableName);

        setDependencies();

        String query = "SELECT * FROM " + myTableName + " WHERE ((AssessmentTeamID = " + aTID + ")" +
                " AND (ReflectionQuestionID = '" + rqID + "'))";

        Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

        // You must get one Instructor Reflection at least
        if (allDataRetrieved != null)
        {
            int size = allDataRetrieved.size();
            // if size = 0 throw the Invalid Primary Key Exception
            if (size == 0)
            {
                throw new InvalidPrimaryKeyException("No Matching instructor reflections for Assessment Team ID : " + aTID
                        + " Reflection Question ID : " + rqID);
            }
            else
                // There should be EXACTLY one Instructor Reflection. More than that is an error
                if (size != 1)
                {

                    throw new MultiplePrimaryKeysException("Multiple Matching instructor reflections for Assessment " +
                            "Team ID : " + aTID + " Reflection Question ID : " + rqID);


                }
                else
                {
                    // copy all the retrieved data into persistent state
                    Properties retrievedSCatData = allDataRetrieved.elementAt(0);
                    persistentState = new Properties();

                    Enumeration allKeys = retrievedSCatData.propertyNames();
                    while (allKeys.hasMoreElements() == true)
                    {
                        String nextKey = (String)allKeys.nextElement();
                        String nextValue = retrievedSCatData.getProperty(nextKey);


                        if (nextValue != null)
                        {
                            persistentState.setProperty(nextKey, nextValue);
                        }
                    }

                }
        }
        // If Category is not found for this data, throw an Invalid Primary key exception
        else
        {
            throw new InvalidPrimaryKeyException("No Matching instructor reflections for Assessment Team ID : " + aTID
                    + " Reflection Question ID : " + rqID);
        }
    }



    /**
     * Alternate constructor. Can be used to create a NEW Instructor Reflection
     */
    //----------------------------------------------------------
    public InstructorReflections(Properties props)
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
    //Check to see if we want to compare the number or the name `````````````````````````````````````
    public static int compare(InstructorReflections a, InstructorReflections b)
    {
        String aVal = (String)a.getState("ID");
        String bVal = (String)b.getState("ID");

        return aVal.compareTo(bVal);
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
                updateStatusMessage = "Instructor Reflections data updated successfully!";
            }

            else
            {
                Integer categoryID =
                        insertAutoIncrementalPersistentState(mySchema, persistentState);
                persistentState.setProperty("ID", "" + categoryID.intValue());
                updateStatusMessage = "Instructor Reflections data added successfully!";
            }


        }
        catch (SQLException ex)
        {
            updateStatusMessage = "Error in installing Instructor Reflections data in database!";
        }
        //DEBUG System.out.println("updateStateInDatabase " + updateStatusMessage);
    }



    /**
     * This method is needed solely to enable the Category information to be displayable in a table
     *
     */
    //--------------------------------------------------------------------------
    public Vector<String> getEntryListView()
    {
        Vector<String> v = new Vector<String>();

        v.addElement(persistentState.getProperty("ID"));
        v.addElement(persistentState.getProperty("AssessmentTeamID"));
        v.addElement(persistentState.getProperty("ReflectionQuestionID"));
        v.addElement(persistentState.getProperty("ReflectionText"));
        v.addElement(persistentState.getProperty("ACComments"));

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

