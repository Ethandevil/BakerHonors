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

/** The class containing the ISLO information for the ISLO Data Management
 * application
 */
//==============================================================
public class SLO extends EntityBase implements IView
{
    private static final String myTableName = "SLO";

    protected Properties dependencies;

    // GUI Components
    private String updateStatusMessage = "";

    // constructor for this class
    //----------------------------------------------------------
    public SLO(String SLOID) throws InvalidPrimaryKeyException, MultiplePrimaryKeysException
    {
        super(myTableName);

        setDependencies();

        SLOID = SLOID.trim();
        String query = "SELECT * FROM " + myTableName + " WHERE (ID = " + SLOID + ")";

        Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

        // You must get one article type at least
        if (allDataRetrieved != null)
        {
            int size = allDataRetrieved.size();
            // if size = 0 throw the Invalid Primary Key Exception
            if (size == 0)
            {
                throw new InvalidPrimaryKeyException("No SLO matching SLO ID : "
                        + SLOID + " found.");
            }
            else
                // There should be EXACTLY one SLO. More than that is an error
                if (size != 1)
                {

                    throw new MultiplePrimaryKeysException("Multiple SLOs matching SLO ID : "
                            + SLOID + " found.");
                }
                else
                {
                    // copy all the retrieved data into persistent state
                    Properties retrievedSLOData = allDataRetrieved.elementAt(0);
                    persistentState = new Properties();

                    Enumeration allKeys = retrievedSLOData.propertyNames();
                    while (allKeys.hasMoreElements() == true)
                    {
                        String nextKey = (String)allKeys.nextElement();
                        String nextValue = retrievedSLOData.getProperty(nextKey);


                        if (nextValue != null)
                        {
                            persistentState.setProperty(nextKey, nextValue);
                        }
                    }

                }
        }
        // If SLO found for this SLO Number, throw an Invalid Primary key exception
        else
        {
            throw new InvalidPrimaryKeyException("No SLO matching SLO ID : "
                    + SLOID + " found.");
        }
    }

    public SLO(String SLOText, boolean flag) throws InvalidPrimaryKeyException, MultiplePrimaryKeysException
    {
        super(myTableName);

        setDependencies();

        SLOText = SLOText.trim();
        String query = "SELECT * FROM " + myTableName + " WHERE (SLOText = '" + SLOText + "')";

        Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

        // You must get one article type at least
        if (allDataRetrieved != null)
        {
            int size = allDataRetrieved.size();
            // if size = 0 throw the Invalid Primary Key Exception
            if (size == 0)
            {
                throw new InvalidPrimaryKeyException("No SLO matching SLO Text : "
                        + SLOText + " found.");
            }
            else
                // There should be EXACTLY one SLO. More than that is an error
                if (size != 1)
                {

                    throw new MultiplePrimaryKeysException("Multiple SLOs matching SLO Text : "
                            + SLOText + " found.");
                }
                else
                {
                    // copy all the retrieved data into persistent state
                    Properties retrievedGenEdAreaData = allDataRetrieved.elementAt(0);
                    persistentState = new Properties();

                    Enumeration allKeys = retrievedGenEdAreaData.propertyNames();
                    while (allKeys.hasMoreElements() == true)
                    {
                        String nextKey = (String)allKeys.nextElement();
                        String nextValue = retrievedGenEdAreaData.getProperty(nextKey);


                        if (nextValue != null)
                        {
                            persistentState.setProperty(nextKey, nextValue);
                        }
                    }

                }
        }
        // If SLO found for this SLO Text, throw an Invalid Primary key exception
        else
        {
            throw new InvalidPrimaryKeyException("No SLO matching SLO Text : "
                    + SLOText + " found.");
        }
    }

    /**
     * Alternate constructor. Can be used to create a NEW SLO
     */
    //----------------------------------------------------------
    public SLO(Properties props)
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
    public static int compare(SLO a, SLO b)
    {
        String aVal = (String)a.getState("SLOText");
        String bVal = (String)b.getState("SLOText");

        return aVal.compareTo(bVal);
    }

    //-----------------------------------------------------------------------------------
    public void update()
    {
        updateStateInDatabase();
    }
    public void remove(){
        removeStateInDatabase();
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
                updateStatusMessage = "SLO : " + persistentState.getProperty("SLOText") + " updated successfully!";
            }
            else
            {
                Integer SLOID =
                        insertAutoIncrementalPersistentState(mySchema, persistentState);
                persistentState.setProperty("ID", "" + SLOID.intValue());
                updateStatusMessage = "SLO : " +  persistentState.getProperty("SLOText")
                        + " installed successfully!";
            }
        }
        catch (SQLException ex)
        {
            updateStatusMessage = "Error in installing SLO data in database!";
        }
        //DEBUG System.out.println("updateStateInDatabase " + updateStatusMessage);
    }


    private void removeStateInDatabase()
    {
        try
        {
            if (persistentState.getProperty("ID") != null)
            {
                Properties whereClause = new Properties();
                whereClause.setProperty("ID", persistentState.getProperty("ID"));
                updatePersistentState(mySchema, persistentState, whereClause);
                updateStatusMessage = "SLO : " + persistentState.getProperty("SLOText") + " removed successfully!";
            }
        }
        catch (SQLException ex)
        {
            updateStatusMessage = "Error in removing SLO data in database!";
        }
        //DEBUG System.out.println("updateStateInDatabase " + updateStatusMessage);
    }
    /**
     * This method is needed solely to enable the SLO information to be displayable in a table
     *
     */
    //--------------------------------------------------------------------------
    public Vector<String> getEntryListView()
    {
        Vector<String> v = new Vector<String>();

        v.addElement(persistentState.getProperty("ID"));
        v.addElement(persistentState.getProperty("GenEdID"));
        v.addElement(persistentState.getProperty("SLOText"));
        v.addElement(persistentState.getProperty("Notes"));


        return v;
    }

    // Short cut methods
    public String getText(){
        return persistentState.getProperty("SLOText");
    }

    public String getNotes(){
        return persistentState.getProperty("Notes");
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

