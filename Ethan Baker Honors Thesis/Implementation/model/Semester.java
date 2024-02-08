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

/** The class containing the Semester information for the Gen Ed Assessment Data Management
 * application
 */
//==============================================================
public class Semester extends EntityBase implements IView
{
    private static final String myTableName = "Semester";

    protected Properties dependencies;

    // GUI Components
    private String updateStatusMessage = "";

    // constructor for this class
    //----------------------------------------------------------
    public Semester(String sem, String year) throws InvalidPrimaryKeyException, MultiplePrimaryKeysException
    {
        super(myTableName);

        setDependencies();

        sem = sem.trim();
        year = year.trim();
        String query = "SELECT * FROM " + myTableName + " WHERE (SemName = '" + sem + "' AND Year = " + year +")";

		constructionQueryHelper(query, 
			"No Matching Semesters for: (" + sem + "," + year + ") found.",
			"Multiple Semesters matching: (" + sem + "," + year + ") found.");
			
        
    }
	
	// constructor for this class
    //----------------------------------------------------------
    public Semester(String semId) throws InvalidPrimaryKeyException, MultiplePrimaryKeysException
    {
        super(myTableName);

        setDependencies();

        String query = "SELECT * FROM " + myTableName + " WHERE (ID = " + semId + ")";

		constructionQueryHelper(query, 
			"No Matching Semesters for id: " + semId + " found.",
			"Multiple Semesters matching id: " + semId + " found.");
			
        
    }
	
	//--------------------------------------------------------------
	private void constructionQueryHelper(String query, String invPrimKeyMesg,
		String multPrimKeyMesg) throws InvalidPrimaryKeyException, MultiplePrimaryKeysException
	{
		Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

        // You must get one semester at least
        if (allDataRetrieved != null)
        {
            int size = allDataRetrieved.size();
            // if size = 0 throw the Invalid Primary Key Exception
            if (size == 0)
            {
                throw new InvalidPrimaryKeyException(invPrimKeyMesg);
            }
            else
                // There should be EXACTLY one semester. More than that is an error
                if (size != 1)
                {

                    throw new MultiplePrimaryKeysException(multPrimKeyMesg);
                }
                else
                {
                    // copy all the retrieved data into persistent state
                    Properties retrievedISLOData = allDataRetrieved.elementAt(0);
                    persistentState = new Properties();

                    Enumeration allKeys = retrievedISLOData.propertyNames();
                    while (allKeys.hasMoreElements() == true)
                    {
                        String nextKey = (String)allKeys.nextElement();
                        String nextValue = retrievedISLOData.getProperty(nextKey);


                        if (nextValue != null)
                        {
                            persistentState.setProperty(nextKey, nextValue);
                        }
                    }

                }
        }
        // If semester not found for this data, throw an Invalid Primary key exception
        else
        {
            throw new InvalidPrimaryKeyException(invPrimKeyMesg);
        }
	}

    /**
     * Alternate constructor. Can be used to create a NEW Semester
     */
    //----------------------------------------------------------
    public Semester(Properties props)
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
        else if(key.equals("ID")){
            return persistentState.getProperty("ID");
        }

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
    public static int compare(Semester a, Semester b)
    {
        String aVal = (String)a.getState("Year");
        String bVal = (String)b.getState("Year");

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
                updateStatusMessage = "Semester: (" +  persistentState.getProperty("SemName") +
                        ", " + persistentState.getProperty("Year") + ") updated successfully!";
            }

            else
            {
                Integer semesterID =
                        insertAutoIncrementalPersistentState(mySchema, persistentState);
                persistentState.setProperty("ID", "" + semesterID.intValue());
                updateStatusMessage = "Semester: (" +  persistentState.getProperty("SemName") +
                        ", " + persistentState.getProperty("Year") + ") installed successfully!";
            }


        }
        catch (SQLException ex)
        {
            updateStatusMessage = "Error in installing Semester data in database!";
        }
        //DEBUG System.out.println("updateStateInDatabase " + updateStatusMessage);
    }

   /* // Probably not needed
    private void removeStateInDatabase()
    {
        try
        {
            if (persistentState.getProperty("ID") != null)
            {
                Properties whereClause = new Properties();
                whereClause.setProperty("ID", persistentState.getProperty("ID"));
                updatePersistentState(mySchema, persistentState, whereClause);
                updateStatusMessage = "Semester: " + persistentState.getProperty("Semester") + " removed successfully!";
            }
        }
        catch (SQLException ex)
        {
            updateStatusMessage = "Error in removing Semester data in database!";
        }
        //DEBUG System.out.println("updateStateInDatabase " + updateStatusMessage);
    }*/

    /**
     * This method is needed solely to enable the Semester information to be displayable in a table
     *
     */
    //--------------------------------------------------------------------------
    public Vector<String> getEntryListView()
    {
        Vector<String> v = new Vector<String>();

        v.addElement(persistentState.getProperty("ID"));
        v.addElement(persistentState.getProperty("SemName"));
        v.addElement(persistentState.getProperty("Year"));


        return v;
    }

    // Short cut methods
	//--------------------------------------------------------------------------
    public String getSemester(){
        return persistentState.getProperty("SemName");
    }

	//--------------------------------------------------------------------------
    public String getYear(){
        return persistentState.getProperty("Year");
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

