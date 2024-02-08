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

/** The class containing the AddGenEdAreaTransaction for the Gen Ed Data Management application */
//==============================================================
public class AddGenEdAreaTransaction extends Transaction
{

    private GenEdArea myGenEdArea;


    // GUI Components

    private String transactionErrorMessage = "";

    /**
     * Constructor for this class.
     *
     */
    //----------------------------------------------------------
    public AddGenEdAreaTransaction() throws Exception
    {
        super();
    }

    //----------------------------------------------------------
    protected void setDependencies()
    {
        dependencies = new Properties();
        dependencies.setProperty("CancelAddArea", "CancelTransaction");
        dependencies.setProperty("OK", "CancelTransaction");
        dependencies.setProperty("AreaData", "TransactionError");

        myRegistry.setDependencies(dependencies);
    }

    /**
     * This method encapsulates all the logic of creating the Gen Ed Area,
     * verifying its uniqueness, etc.
     */
    //----------------------------------------------------------
    protected void processTransaction(Properties props)
    {
        if (props.getProperty("AreaName") != null)
        {
            String genEdAreaName = props.getProperty("AreaName");
            try
            {

                GenEdArea oldGenEdArea = new GenEdArea(genEdAreaName, true);
                transactionErrorMessage = "ERROR: Gen Ed Area Name " + genEdAreaName
                        + " already exists!";
                new Event(Event.getLeafLevelClassName(this), "processTransaction",
                        "Gen Ed Area with name : " + genEdAreaName + " already exists!",
                        Event.ERROR);
            }
            catch (InvalidPrimaryKeyException ex)
            {
                // GenEdArea Name does not exist, validate data
                try
                {

                    if (genEdAreaName.length() > GlobalVariables.MAX_GEN_ED_AREA_NAME_LENGTH)
                    {
                        transactionErrorMessage = "ERROR: Gen Ed Area Name chosen too long (max = " + GlobalVariables.MAX_GEN_ED_AREA_NAME_LENGTH + ")! ";
                    }
                    else
                    {
                        String descriptionOfGenEdArea = props.getProperty("Notes");
                        if (descriptionOfGenEdArea.length() > GlobalVariables.MAX_GEN_ED_AREA_NOTES_LENGTH )
                        {
                            transactionErrorMessage = "ERROR: Gen Ed Area notes entered too long (max = " + GlobalVariables.MAX_GEN_ED_AREA_NOTES_LENGTH + ")! ";
                        }
                        else
                        {

                            myGenEdArea = new GenEdArea(props);
                            myGenEdArea.update();
                            transactionErrorMessage = (String)myGenEdArea.getState("UpdateStatusMessage");
                        }
                    }
                }
                catch (Exception excep)
                {
                    /*transactionErrorMessage = "ERROR: Invalid ISLO Number: " + isloNumber
                            + "! Must be numerical.";
                    new Event(Event.getLeafLevelClassName(this), "processTransaction",
                            "Invalid ISLO Number: " + isloNumber + "! Must be numerical.",
                            Event.ERROR);*/
                }

            }
            catch (MultiplePrimaryKeysException ex2)
            {
                transactionErrorMessage = "ERROR: Multiple Gen Ed Areas with chosen Gen Ed Area Name!";
                new Event(Event.getLeafLevelClassName(this), "processTransaction",
                        "Found multiple Gen Ed Areas with Gen Ed Area Name : " + genEdAreaName + ". Reason: " + ex2.toString(),
                        Event.ERROR);

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
        // DEBUG System.out.println("AddISLOTransaction.sCR: key: " + key);

        if (key.equals("DoYourJob") == true)
        {
            doYourJob();
        }
        else
        if (key.equals("AreaData") == true)
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
        Scene currentScene = myViews.get("AddGenEdAreaView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("AddGenEdAreaView", this);
            currentScene = new Scene(newView);
            myViews.put("AddGenEdAreaView", currentScene);

            return currentScene;
        }
        else
        {
            return currentScene;
        }
    }

}

