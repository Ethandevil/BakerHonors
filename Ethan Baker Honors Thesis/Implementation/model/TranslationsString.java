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

/** The class containing the Translations String information for the internationalizable Gen Ed Data Management
 * application
 */
//==============================================================
public class TranslationsString extends EntityBase implements IView
{
    private static String myTableName = "Translations_en_US";

    protected Properties dependencies;

    // GUI Components
    private String updateStatusMessage = "";

    // constructor for this class
    //----------------------------------------------------------
    public TranslationsString()
    {
        super(myTableName);
    }

    //----------------------------------------------------------
    public void populate(String dispLabel) throws InvalidPrimaryKeyException, MultiplePrimaryKeysException
    {

        setDependencies();

        dispLabel = dispLabel.trim();
        String query = "SELECT * FROM " + myTableName + " WHERE (DispLbl = '" + dispLabel + "')";

        Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

        // You must get one article type at least
        if (allDataRetrieved != null)
        {
            int size = allDataRetrieved.size();
            // if size = 0 throw the Invalid Primary Key Exception
            if (size == 0)
            {
                throw new InvalidPrimaryKeyException("No Translation String matching label : "
                        + dispLabel + " found.");
            }
            else
                // There should be EXACTLY one Translation String for the label. More than that is an error
                if (size != 1)
                {

                    throw new MultiplePrimaryKeysException("Multiple Translation Strings matching label : "
                            + dispLabel + " found.");
                }
                else
                {
                    // copy all the retrieved data into persistent state
                    Properties retrievedTSData = allDataRetrieved.elementAt(0);
                    persistentState = new Properties();

                    Enumeration allKeys = retrievedTSData.propertyNames();
                    while (allKeys.hasMoreElements() == true)
                    {
                        String nextKey = (String)allKeys.nextElement();
                        String nextValue = retrievedTSData.getProperty(nextKey);


                        if (nextValue != null)
                        {
                            persistentState.setProperty(nextKey, nextValue);
                        }
                    }

                }
        }
        else
        {
            throw new InvalidPrimaryKeyException("No Translation String matching label : "
                    + dispLabel + " found.");
        }
    }


    /**
     * Alternate constructor.
     */
    //----------------------------------------------------------
    public TranslationsString(Properties props)
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

    //----------------------------------------------------------
    private void setDependencies()
    {
        dependencies = new Properties();

        myRegistry.setDependencies(dependencies);
    }

    //----------------------------------------------------------
    public Object getState(String key)
    {
        if (key.equals("Label") == true)
            return persistentState.getProperty("DispLbl");
        else if(key.equals("DisplayString")){
            return persistentState.getProperty("DispStr");
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
    public static int compare(GenEdArea a, GenEdArea b)
    {
        String aVal = (String)a.getState("DispLbl");
        String bVal = (String)b.getState("DispLbl");

        return aVal.compareTo(bVal);
    }

    //-----------------------------------------------------------------------------------
    public void update()
    {
        updateStateInDatabase();
    }

    //------------------------------------------------------------------------------------
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
                updateStatusMessage = "Translation String for label : " + persistentState.getProperty("DispLbl") + " updated successfully!";
            }
            else
            {
                Integer tsID =
                        insertAutoIncrementalPersistentState(mySchema, persistentState);
                persistentState.setProperty("ID", "" + tsID.intValue());
                updateStatusMessage = "Translation String for label : " +  persistentState.getProperty("DispLbl")
                        + " installed successfully!";
            }
        }
        catch (SQLException ex)
        {
            updateStatusMessage = "Error in installing Translation String data in database!";
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
                updateStatusMessage = "Translation String for label : " + persistentState.getProperty("DispLbl") + " removed successfully!";
            }
        }
        catch (SQLException ex)
        {
            updateStatusMessage = "Error in removing translation string from database!";
        }
        //DEBUG System.out.println("updateStateInDatabase " + updateStatusMessage);
    }
    /**
     * This method is needed solely to enable the Translation String information to be displayable in a table
     *
     */
    //--------------------------------------------------------------------------
    public Vector<String> getEntryListView()
    {
        Vector<String> v = new Vector<String>();

        v.addElement(persistentState.getProperty("ID"));
        v.addElement(persistentState.getProperty("DispLbl"));
        v.addElement(persistentState.getProperty("DispStr"));


        return v;
    }

    // Short cut methods
    //---------------------------------------------------------------------------
    public String getLabel(){
        return persistentState.getProperty("DispLbl");
    }

    //--------------------------------------------------------------------------
    public String getDisplayString(){
        return persistentState.getProperty("DispStr");
    }

    //-----------------------------------------------------------------------------------
    public void setTableNameForLocale(String locale)
    {
        String newTableName = "Translations_" + locale;
        myTableName = newTableName;
        initializeSchema(newTableName);
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

