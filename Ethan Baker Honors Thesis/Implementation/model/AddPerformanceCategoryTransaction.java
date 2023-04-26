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
import javafx.scene.Scene;
import java.util.Properties;

// project imports
import event.Event;
import exception.InvalidPrimaryKeyException;
import exception.MultiplePrimaryKeysException;

import userinterface.View;
import userinterface.ViewFactory;

/** The class containing the AddSemesterTransaction for the ISLO Data Management application */
//==============================================================
public class AddPerformanceCategoryTransaction extends Transaction
{

    private PerformanceCategory myCategory;


    // GUI Components

    private String transactionErrorMessage = "";

    /**
     * Constructor for this class.
     *
     */
    //----------------------------------------------------------
    public AddPerformanceCategoryTransaction() throws Exception
    {
        super();
    }

    //----------------------------------------------------------
    protected void setDependencies()
    {
        dependencies = new Properties();
        dependencies.setProperty("CancelAddPerformanceCategory", "CancelTransaction");
        dependencies.setProperty("CancelModifyPerformanceCategory", "CancelTransaction");
        dependencies.setProperty("OK", "CancelTransaction");
        dependencies.setProperty("PerformanceCategoryData", "TransactionError");

        myRegistry.setDependencies(dependencies);
    }

    /**
     * This method encapsulates all the logic of creating the Semester,
     * verifying its uniqueness, etc.
     */
    //----------------------------------------------------------
    public void processTransaction(Properties props)
    {
        if (props.getProperty("Number") != null)
            //(props.getProperty("Name") != null &&
        {
            String name = props.getProperty("Name");
            String num = props.getProperty("Number");
            try
            {

                PerformanceCategory oldPerformanceCategory = new PerformanceCategory(name, num);

                transactionErrorMessage = "ERROR: Category " + name + " already exists!";
                new Event(Event.getLeafLevelClassName(this), "processTransaction",
                        "Category: " + name + " already exists!",
                        Event.ERROR);
            }
            catch (InvalidPrimaryKeyException ex)
            {
                // Number does not exist, validate data
                try
                {
                    myCategory = new PerformanceCategory(props);
                    myCategory.update();
                    transactionErrorMessage = (String)myCategory.getState("UpdateStatusMessage");


                }
                //fix these catch blocks or remove them ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                catch (Exception excep)
                {

                    transactionErrorMessage = "Error in saving Category: " + excep.toString();
                    new Event(Event.getLeafLevelClassName(this), "processTransaction",
                            "Error in saving Category: " + excep.toString(),
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
        if (key.equals("PerformanceCategoryData") == true)
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
        Scene currentScene = myViews.get("AddPerformanceCategoryView");
        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("AddPerformanceCategoryView", this);
            currentScene = new Scene(newView);

            myViews.put("AddPerformanceCategoryView", currentScene);

            return currentScene;
        }
        else
        {
            return currentScene;
        }
    }

}

