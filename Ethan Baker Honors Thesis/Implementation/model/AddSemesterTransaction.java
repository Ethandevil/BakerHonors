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
import utilities.GlobalVariables;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.util.Properties;
import java.util.Vector;

// project imports
import event.Event;
import exception.InvalidPrimaryKeyException;
import exception.MultiplePrimaryKeysException;

import userinterface.View;
import userinterface.ViewFactory;

/** The class containing the AddSemesterTransaction for the Gen Ed Assessment Data Management application */
//==============================================================
public class AddSemesterTransaction extends Transaction
{

    private Semester mySemester;


    // GUI Components

    private String transactionErrorMessage = "";

    /**
     * Constructor for this class.
     *
     */
    //----------------------------------------------------------
    public AddSemesterTransaction() throws Exception
    {
        super();
    }

    //----------------------------------------------------------
    protected void setDependencies()
    {
        dependencies = new Properties();
        dependencies.setProperty("CancelAddSemester", "CancelTransaction");
        dependencies.setProperty("OK", "CancelTransaction");
        dependencies.setProperty("SemesterData", "TransactionError");

        myRegistry.setDependencies(dependencies);
    }

    /**
     * This method encapsulates all the logic of creating the Semester,
     * verifying its uniqueness, etc.
     */
    //----------------------------------------------------------
    public void processTransaction(Properties props)
    {
        if (props.getProperty("SemName") != null && (props.getProperty("Year") != null))
        {
            String sem = props.getProperty("SemName");
            String year = props.getProperty("Year");
            try
            {

                Semester oldSemester = new Semester(sem, year);

                transactionErrorMessage = "ERROR: Semester " + sem
                        + " " + year + " already exists!";
                new Event(Event.getLeafLevelClassName(this), "processTransaction",
                        "Semester: " + sem + " already exists!",
                        Event.ERROR);
            }
            catch (InvalidPrimaryKeyException ex)
            {
                // Number does not exist, validate data
                try
                {
                            mySemester = new Semester(props);
                            mySemester.update();
                            transactionErrorMessage = (String)mySemester.getState("UpdateStatusMessage");


                }
                //fix these catch blocks or remove them ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                catch (Exception excep)
                {

                    transactionErrorMessage = "Error in saving Semester: " + excep.toString();
                    new Event(Event.getLeafLevelClassName(this), "processTransaction",
                            "Error in saving Semester: " + excep.toString(),
                            Event.ERROR);


                }

            }
            catch (MultiplePrimaryKeysException ex2)
            {
                /*
                transactionErrorMessage = "ERROR: Multiple ISLOs with chosen ISLO Number!";
                new Event(Event.getLeafLevelClassName(this), "processTransaction",
                        "Found multiple ISLOs with ISLO number : " + isloNumber + ". Reason: " + ex2.toString(),
                        Event.ERROR);

                 */

            }


        }



    }

    //-----------------------------------------------------------
    public Object getState(String key)
    {
        if (key.equals("TransactionError") == true)
        {
            return transactionErrorMessage;
        }


        return null;
    }

    //-----------------------------------------------------------
    public void stateChangeRequest(String key, Object value)
    {
        // DEBUG        System.out.println("AddSemesterTransaction.sCR: key: " + key);

        if (key.equals("DoYourJob") == true)
        {
            doYourJob();
        }
        else
        if (key.equals("SemesterData") == true)
        {
            processTransaction((Properties)value);
        }

        myRegistry.updateSubscribers(key, this);
    }

    /**
     * Create the view of this class. And then the super-class calls
     * swapToView() to display the view in the frame
     */
    //------------------------------------------------------
    protected Scene createView()
    {
        Scene currentScene = myViews.get("AddSemesterView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("AddSemesterView", this);
            currentScene = new Scene(newView);
            myViews.put("AddSemesterView", currentScene);

            return currentScene;
        }
        else
        {
            return currentScene;
        }
    }

}

