// tabs=4

//************************************************************
// COPYRIGHT 2021, Kyle D. Adams and Sandeep Mitra, State
//   University of New York. - Brockport (SUNY Brockport)
// ALL RIGHTS RESERVED
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

/** The class containing the Performance Category information for the ISLO Data Management
 * application
 */
//==============================================================
public class PerformanceCategory extends EntityBase implements IView
{
    private static final String myTableName = "CategoryNames";

    protected Properties dependencies;

    // GUI Components
    private String updateStatusMessage = "";

    // constructor for this class
    //----------------------------------------------------------
    public PerformanceCategory(String name, String num) throws InvalidPrimaryKeyException, MultiplePrimaryKeysException
    {
        super(myTableName);

        setDependencies();

        name = name.trim();
        num = num.trim();
        String query = "SELECT * FROM " + myTableName + " WHERE (Name = '" + name + "' AND Number = '" + num +"')";

        Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

        // You must get one category at least
        if (allDataRetrieved != null)
        {
            int size = allDataRetrieved.size();
            // if size = 0 throw the Invalid Primary Key Exception
            if (size == 0)
            {
                throw new InvalidPrimaryKeyException("No Matching Categories for: "
                        + name + " found.");
            }
            else
                // There should be EXACTLY one category. More than that is an error
                if (size != 1)
                {

                    throw new MultiplePrimaryKeysException("Multiple Categories matching: "
                            + name + " found.");

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
        // If Category is not found for this data, throw an Invalid Primary key exception
        else
        {
            throw new InvalidPrimaryKeyException("No Categories matching: ("
                    + name + " found.");
        }
    }

    // constructor for this class
    //----------------------------------------------------------
    public PerformanceCategory(String num) throws InvalidPrimaryKeyException, MultiplePrimaryKeysException
    {
        super(myTableName);

        setDependencies();

        num = num.trim();
        String query = "SELECT * FROM " + myTableName + " WHERE (Number = '" + num + "')";

        Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

        // You must get one article type at least
        if (allDataRetrieved != null)
        {
            int size = allDataRetrieved.size();
            // if size = 0 throw the Invalid Primary Key Exception
            if (size == 0)
            {
                throw new InvalidPrimaryKeyException("No Category matching Number : "
                        + num + " found.");
            }
            else
                // There should be EXACTLY one ISLO. More than that is an error
                if (size != 1)
                {

                    throw new MultiplePrimaryKeysException("Multiple Categories matching Number : "
                            + num + " found.");
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
        // If ISLO found for this ISLO Number, throw an Invalid Primary key exception
        else
        {
            throw new InvalidPrimaryKeyException("No Categories matching Number : "
                    + num + " found.");
        }
    }

    /**
     * Alternate constructor. Can be used to create a NEW Category
     */
    //----------------------------------------------------------
    public PerformanceCategory(Properties props)
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
    public static int compare(PerformanceCategory a, PerformanceCategory b)
    {
        String aVal = (String)a.getState("Number");
        String bVal = (String)b.getState("Number");

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
                updateStatusMessage = "Performance Category number: " +  persistentState.getProperty("Number") + " updated successfully!";
            }

            else
            {
                Integer categoryID =
                        insertAutoIncrementalPersistentState(mySchema, persistentState);
                persistentState.setProperty("ID", "" + categoryID.intValue());
                updateStatusMessage = "Performance Category number: "
                        + persistentState.getProperty("Number") + " installed successfully!";
            }


        }
        catch (SQLException ex)
        {
            updateStatusMessage = "Error in installing Performance Category data in database!";
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
        v.addElement(persistentState.getProperty("Number"));
        v.addElement(persistentState.getProperty("Name"));


        return v;
    }

    // Short cut methods
    public String getPerformanceCategory(){
        return persistentState.getProperty("Name");
    }

    public String getNumber(){
        return persistentState.getProperty("Number");
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

