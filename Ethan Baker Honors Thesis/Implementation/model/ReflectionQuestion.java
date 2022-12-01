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

/** The class containing the Reflection Question information for the Gen Ed Data Management
 * application
 */
//==============================================================
public class ReflectionQuestion extends EntityBase implements IView {
    private static final String myTableName = "InstructorReflectionQuestions";

    protected Properties dependencies;

    // GUI Components
    private String updateStatusMessage = "";

    // constructor for this class
    //----------------------------------------------------------
    public ReflectionQuestion(String reflectionQuestionID) throws InvalidPrimaryKeyException, MultiplePrimaryKeysException
    {
        super(myTableName);

        setDependencies();

        reflectionQuestionID = reflectionQuestionID.trim();
        String query = "SELECT * FROM " + myTableName + " WHERE (ID = " + reflectionQuestionID + ")";

        Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

        // You must get one article type at least
        if (allDataRetrieved != null)
        {
            int size = allDataRetrieved.size();
            // if size = 0 throw the Invalid Primary Key Exception
            if (size == 0)
            {
                throw new InvalidPrimaryKeyException("No Reflection Question matching ID : "
                        + reflectionQuestionID + " found.");
            }
            else
                // There should be EXACTLY one Reflection Question. More than that is an error
                if (size != 1)
                {

                    throw new MultiplePrimaryKeysException("Multiple Reflection Questions matching ID : "
                            + reflectionQuestionID + " found.");
                }
                else
                {
                    // copy all the retrieved data into persistent state
                    Properties retrievedReflectionQuestionData = allDataRetrieved.elementAt(0);
                    persistentState = new Properties();

                    Enumeration allKeys = retrievedReflectionQuestionData.propertyNames();
                    while (allKeys.hasMoreElements() == true)
                    {
                        String nextKey = (String)allKeys.nextElement();
                        String nextValue = retrievedReflectionQuestionData.getProperty(nextKey);


                        if (nextValue != null)
                        {
                            persistentState.setProperty(nextKey, nextValue);
                        }
                    }

                }
        }
        // If Reflection Question found for this ID, throw an Invalid Primary key exception
        else
        {
            throw new InvalidPrimaryKeyException("No Reflection Question matching ID : "
                    + reflectionQuestionID + " found.");
        }
    }

    public ReflectionQuestion(String reflectionQuestionText, boolean flag) throws InvalidPrimaryKeyException, MultiplePrimaryKeysException
    {
        super(myTableName);

        setDependencies();

        reflectionQuestionText = reflectionQuestionText.trim();
        String query = "SELECT * FROM " + myTableName + " WHERE (QuestionText = '" + reflectionQuestionText + "')";

        Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

        // You must get one article type at least
        if (allDataRetrieved != null)
        {
            int size = allDataRetrieved.size();
            // if size = 0 throw the Invalid Primary Key Exception
            if (size == 0)
            {
                throw new InvalidPrimaryKeyException("No Reflection Question matching Text : "
                        + reflectionQuestionText + " found.");
            }
            else
                // There should be EXACTLY one Reflection Question. More than that is an error
                if (size != 1)
                {

                    throw new MultiplePrimaryKeysException("Multiple Reflection Questions matching Text : "
                            + reflectionQuestionText + " found.");
                }
                else
                {
                    // copy all the retrieved data into persistent state
                    Properties retrievedReflectionQuestionData = allDataRetrieved.elementAt(0);
                    persistentState = new Properties();

                    Enumeration allKeys = retrievedReflectionQuestionData.propertyNames();
                    while (allKeys.hasMoreElements() == true)
                    {
                        String nextKey = (String)allKeys.nextElement();
                        String nextValue = retrievedReflectionQuestionData.getProperty(nextKey);


                        if (nextValue != null)
                        {
                            persistentState.setProperty(nextKey, nextValue);
                        }
                    }

                }
        }
        // If Reflection Question found for this Reflection Question ID, throw an Invalid Primary key exception
        else
        {
            throw new InvalidPrimaryKeyException("No Reflection Question matching Text : "
                    + reflectionQuestionText + " found.");
        }
    }



    /**
     * Alternate constructor. Can be used to create a NEW Reflection Question
     */
    //----------------------------------------------------------
    public ReflectionQuestion(Properties props)
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
        if (key.equals("UpdateStatusMessage") == true)
            return updateStatusMessage;
        else if(key.equals("ReflectionQuestionID")){
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
    public static int compare(ReflectionQuestion a, ReflectionQuestion b)
    {
        String aVal = (String)a.getState("QuestionText");
        String bVal = (String)b.getState("QuestionText");

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
                updateStatusMessage = "Reflection Question updated successfully with designated text!";
            }
            else
            {
                Integer reflectionQuestionID =
                        insertAutoIncrementalPersistentState(mySchema, persistentState);
                persistentState.setProperty("ID", "" + reflectionQuestionID.intValue());
                updateStatusMessage = "Reflection Question installed successfully with designated text!";
            }
        }
        catch (SQLException ex)
        {
            updateStatusMessage = "Error in installing Reflection Question data in database!";
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
                updateStatusMessage = "Reflection Question removed successfully!";
            }
        }
        catch (SQLException ex)
        {
            updateStatusMessage = "Error in removing Reflection Question data in database!";
        }
        //DEBUG System.out.println("updateStateInDatabase " + updateStatusMessage);
    }
    /**
     * This method is needed solely to enable the Reflection Question information to be displayable in a table
     *
     */
    //--------------------------------------------------------------------------
    public Vector<String> getEntryListView()
    {
        Vector<String> v = new Vector<String>();

        v.addElement(persistentState.getProperty("ID"));
        v.addElement(persistentState.getProperty("QuestionText"));

        return v;
    }

    // Short cut methods
    public String getText(){
        return persistentState.getProperty("QuestionText");
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
