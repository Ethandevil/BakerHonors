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

/** The class containing the ModifyGenEdAreaTransaction for the Gen Ed Assessment Data Management application */
//==============================================================
public class ModifyGenEdAreaTransaction extends Transaction
{

    private GenEdAreaCollection myGenEdAreaList;
    private GenEdArea mySelectedGenEdArea;


    // GUI Components

    private String transactionErrorMessage = "";

    /**
     * Constructor for this class.
     *
     */
    //----------------------------------------------------------
    public ModifyGenEdAreaTransaction() throws Exception
    {
        super();
    }

    //----------------------------------------------------------
    protected void setDependencies()
    {
        dependencies = new Properties();
        dependencies.setProperty("CancelSearchArea", "CancelTransaction");
        dependencies.setProperty("CancelAddArea", "CancelTransaction");
        dependencies.setProperty("CancelAreaList", "CancelTransaction");
        dependencies.setProperty("AreaData", "TransactionError");

        myRegistry.setDependencies(dependencies);
    }

    /**
     * This method encapsulates all the logic of creating the Gen Ed Area Collection and showing the view
     */
    //----------------------------------------------------------
    protected void processTransaction(Properties props)
    {
        myGenEdAreaList = new GenEdAreaCollection();

        String genEdAreaNm = props.getProperty("AreaName");
        String notes = props.getProperty("Notes");
        myGenEdAreaList.findByNameAndNotesPart(genEdAreaNm, notes);

        try
        {
            Scene newScene = createGenEdAreaCollectionView();
            swapToView(newScene);
        }
        catch (Exception ex)
        {
            new Event(Event.getLeafLevelClassName(this), "processTransaction",
                    "Error in creating GenEdAreaCollectionView", Event.ERROR);
        }
    }

    /**
     * Helper method for GenEdArea update
     */
    //--------------------------------------------------------------------------
    private void genEdAreaModificationHelper(Properties props)
    {
        String nameOfGenEdArea = props.getProperty("AreaName");
        if (nameOfGenEdArea.length() > GlobalVariables.MAX_GEN_ED_AREA_NAME_LENGTH)
        {
            transactionErrorMessage = "ERROR: Gen Ed Area Name too long! ";
        }
        else
        {
            String notes = props.getProperty("Notes");
            if (notes.length() > GlobalVariables.MAX_GEN_ED_AREA_NOTES_LENGTH )
            {
                transactionErrorMessage = "ERROR: Notes too long (max length = "
                        + GlobalVariables.MAX_GEN_ED_AREA_NOTES_LENGTH + ")! ";
            }
            else
            {
                // Everything OK

                mySelectedGenEdArea.stateChangeRequest("AreaName", nameOfGenEdArea);
                mySelectedGenEdArea.stateChangeRequest("Notes", notes);
                mySelectedGenEdArea.update();
                transactionErrorMessage = (String)mySelectedGenEdArea.getState("UpdateStatusMessage");
            }
        }
    }

    /**
     * This method encapsulates all the logic of modifiying the Gen Ed Area,
     *
     */
    //----------------------------------------------------------
    private void processGenEdAreaModify(Properties props)
    {
        if (props.getProperty("AreaName") != null)
        {
            String genEdAreaName = props.getProperty("AreaName");
            String originalGenEdAreaName = (String)mySelectedGenEdArea.getState("AreaName");
            if (genEdAreaName.equals(originalGenEdAreaName) == false)
            {
                try
                {
                    GenEdArea oldGenEdArea = new GenEdArea(genEdAreaName, true);
                    transactionErrorMessage = "ERROR: Gen Ed Area Name: " + genEdAreaName
                            + " already exists!";
                    new Event(Event.getLeafLevelClassName(this), "processTransaction",
                            "Gen Ed Area with name : " + genEdAreaName + " already exists!",
                            Event.ERROR);
                }
                catch (InvalidPrimaryKeyException ex)
                {
                    // Gen ED Area Name does not exist, validate data

                    mySelectedGenEdArea.stateChangeRequest("AreaName", genEdAreaName);
                    // Process the rest (name, notes). Helper does all that
                    genEdAreaModificationHelper(props);
                }
                catch (MultiplePrimaryKeysException ex2)
                {
                    transactionErrorMessage = "ERROR: Multiple Gen Ed Areas with Name!";
                    new Event(Event.getLeafLevelClassName(this), "processTransaction",
                            "Found multiple Gen Ed Areas with Name : " + genEdAreaName + ". Reason: " + ex2.toString(),
                            Event.ERROR);

                }
            }
            else
            {
                genEdAreaModificationHelper(props);
            }

        }

    }

    //-----------------------------------------------------------
    public Object getState(String key)
    {
        if (key.equals("GenEdAreaList") == true)
        {
            return myGenEdAreaList;
        }
        else
        if (key.equals("AreaName") == true)
        {
            if (mySelectedGenEdArea != null)
                return mySelectedGenEdArea.getState("AreaName");
            else
                return "";
        }
        else
        if (key.equals("Notes") == true)
        {
            if (mySelectedGenEdArea != null)
                return mySelectedGenEdArea.getState("Notes");
            else
                return "";
        }
        else
        if (key.equals("TransactionError") == true)
        {
            return transactionErrorMessage;
        }

        return null;
    }

    //-----------------------------------------------------------
    public void stateChangeRequest(String key, Object value)
    {
        // DEBUG System.out.println("UpdateISLOTransaction.sCR: key: " + key);

        if ((key.equals("DoYourJob") == true) || (key.equals("CancelAreaList") == true))
        {
            doYourJob();
        }
        else
        if (key.equals("SearchArea") == true)
        {
            processTransaction((Properties)value);
        }
        else
        if (key.equals("GenEdAreaSelected") == true)
        {
            String genEdAreaNameSent = (String)value;
            mySelectedGenEdArea = myGenEdAreaList.retrieve(genEdAreaNameSent);
            try
            {

                Scene newScene = createModifyGenEdAreaView();

                swapToView(newScene);

            }
            catch (Exception ex)
            {
                new Event(Event.getLeafLevelClassName(this), "processTransaction",
                        "Error in creating ModifyGenEdAreaView", Event.ERROR);
            }
        }
        else
        if (key.equals("AreaData") == true)
        {
            processGenEdAreaModify((Properties)value);
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
        Scene currentScene = myViews.get("SearchGenEdAreaView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("SearchGenEdAreaView", this);
            currentScene = new Scene(newView);
            myViews.put("SearchGenEdAreaView", currentScene);

            return currentScene;
        }
        else
        {
            return currentScene;
        }
    }

    /**
     * Create the view containing the table of all matching Gen Ed Areas on the search criteria sent
     */
    //------------------------------------------------------
    protected Scene createGenEdAreaCollectionView()
    {
        View newView = ViewFactory.createView("GenEdAreaCollectionView", this);
        Scene currentScene = new Scene(newView);

        return currentScene;

    }

    /**
     * Create the view using which data about selected Gen Ed Areas can be modified
     */
    //------------------------------------------------------
    protected Scene createModifyGenEdAreaView()
    {
        View newView = ViewFactory.createView("ModifyGenEdAreaView", this);
        Scene currentScene = new Scene(newView);

        return currentScene;

    }

}

