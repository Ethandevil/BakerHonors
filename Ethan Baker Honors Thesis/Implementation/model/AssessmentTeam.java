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

/** The class containing the Offering information for the Gen Ed Area Data Management
 * application
 */
//==============================================================
public class AssessmentTeam extends EntityBase implements IView
{
    private static final String myTableName = "AssessmentTeam";

    protected Properties dependencies;

    // GUI Components
    private String updateStatusMessage = "";

    // constructor for this class
    //----------------------------------------------------------
    public AssessmentTeam(String genEdAreaId, String semId) throws InvalidPrimaryKeyException, MultiplePrimaryKeysException
    {
        super(myTableName);

        setDependencies();

        String query = "SELECT * FROM " + myTableName + " WHERE (GenEdAreaID = " + genEdAreaId + " AND SemesterID = "
                + semId +")";

        Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

        // You must get one semester at least
        if (allDataRetrieved != null)
        {
            int size = allDataRetrieved.size();
            // if size = 0 throw the Invalid Primary Key Exception
            if (size == 0)
            {
                throw new InvalidPrimaryKeyException("No Matching Offerings for: Gen Ed Area Id: "
                        + genEdAreaId + ", Semester Id: " + semId + ") found.");
            }
            else
                // There should be EXACTLY one offering. More than that is an error
                if (size != 1)
                {

                    throw new MultiplePrimaryKeysException("Multiple offerings matching: Gen Ed Area Id: "
                            + genEdAreaId + ", Semester Id: " + semId + ") found.");
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
            throw new InvalidPrimaryKeyException("No offerings matching: Gen Ed Area Id: " + genEdAreaId
                    + ", Semester Id: " + semId + ") found.");
        }
    }

    /**
     * Alternate constructor. Can be used to create a NEW offering
     */
    //----------------------------------------------------------
    public AssessmentTeam(Properties props)
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
    public static int compare(AssessmentTeam a, AssessmentTeam b)
    {
        String aVal = (String)a.getState("GenEdAreaID");
        String bVal = (String)b.getState("GenEdAreaID");

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
		String semName = "**";
		String semYr = "00";
		
		String semId = persistentState.getProperty("SemesterID");
		try
		{
			Semester sem = new Semester(semId);
			semName = sem.getSemester();
			semYr = sem.getYear();
		}
		catch (Exception ex) {}
        try
        {
            if (persistentState.getProperty("ID") != null)
            {
                Properties whereClause = new Properties();
                whereClause.setProperty("ID", persistentState.getProperty("ID"));
                updatePersistentState(mySchema, persistentState, whereClause);
                updateStatusMessage = "Selected Gen Ed Area and Semester: (" + semName +
					" " + semYr + ") link updated successfully!";
            }

            else
            {
                Integer offeringID =
                        insertAutoIncrementalPersistentState(mySchema, persistentState);
                persistentState.setProperty("ID", "" + offeringID.intValue());
                updateStatusMessage = "Selected Gen Ed Area and Semester: (" + semName +
					" " + semYr + ") linked successfully!";
            }


        }
        catch (SQLException ex)
        {
            updateStatusMessage = "Error in installing Offering data in database!";
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

