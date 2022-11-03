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

/** The class containing the ModifyGenEdAreaSLOTransaction for the Gen Ed Assessment Data Management application */
//==============================================================
public class ModifyGenEdAreaSLOTransaction extends Transaction
{

    private SLOCollection mySLOList;
    private SLO mySelectedSLO;


    // GUI Components

    private String transactionErrorMessage = "";

    /**
     * Constructor for this class.
     *
     */
    //----------------------------------------------------------
    public ModifyGenEdAreaSLOTransaction() throws Exception
    {
        super();
    }

    //----------------------------------------------------------
    protected void setDependencies()
    {
        dependencies = new Properties();
        dependencies.setProperty("CancelSearchSLO", "CancelTransaction");
        dependencies.setProperty("CancelAddSLO", "CancelTransaction");
        dependencies.setProperty("CancelSLOList", "CancelTransaction");
        dependencies.setProperty("SLOData", "TransactionError");

        myRegistry.setDependencies(dependencies);
    }

    /**
     * This method encapsulates all the logic of creating the SLO Collection and showing the view
     */
    //----------------------------------------------------------
    protected void processTransaction(Properties props)
    {
        mySLOList = new SLOCollection();

        String SLOTxt = props.getProperty("SLOText");
        String notes = props.getProperty("Notes");
        mySLOList.findByTextAndNotesPart(SLOTxt, notes);

        try
        {
            Scene newScene = createSLOCollectionView();
            swapToView(newScene);
        }
        catch (Exception ex)
        {
            new Event(Event.getLeafLevelClassName(this), "processTransaction",
                    "Error in creating SLOCollectionView", Event.ERROR);
        }
    }

    /**
     * Helper method for SLO update
     */
    //--------------------------------------------------------------------------
    private void SLOModificationHelper(Properties props)
    {
        String textOfSLO = props.getProperty("SLOText");
        if (textOfSLO.length() > GlobalVariables.MAX_SLO_TEXT_LENGTH)
        {
            transactionErrorMessage = "ERROR: SLO Text too long! ";
        }
        else
        {
            String notes = props.getProperty("Notes");
            if (notes.length() > GlobalVariables.MAX_SLO_NOTES_LENGTH )
            {
                transactionErrorMessage = "ERROR: Notes too long (max length = "
                        + GlobalVariables.MAX_SLO_NOTES_LENGTH + ")! ";
            }
            else
            {
                // Everything OK

                mySelectedSLO.stateChangeRequest("SLOText", textOfSLO);
                mySelectedSLO.stateChangeRequest("Notes", notes);
                mySelectedSLO.update();
                transactionErrorMessage = (String)mySelectedSLO.getState("UpdateStatusMessage");
            }
        }
    }

    /**
     * This method encapsulates all the logic of modifiying the SLO,
     *
     */
    //----------------------------------------------------------
    private void processSLOModify(Properties props)
    {
        if (props.getProperty("SLOText") != null)
        {
            String SLOText = props.getProperty("SLOText");
            String originalSLOText = (String)mySelectedSLO.getState("SLOText");
            if (SLOText.equals(originalSLOText) == false)
            {
                try
                {
                    SLO oldSLO = new SLO(SLOText, true);
                    transactionErrorMessage = "ERROR: SLO Text: " + SLOText
                            + " already exists!";
                    new Event(Event.getLeafLevelClassName(this), "processTransaction",
                            "SLO with text : " + SLOText + " already exists!",
                            Event.ERROR);
                }
                catch (InvalidPrimaryKeyException ex)
                {
                    // SLO Text does not exist, validate data

                    mySelectedSLO.stateChangeRequest("SLOText", SLOText);
                    // Process the rest (text, notes). Helper does all that
                    SLOModificationHelper(props);
                }
                catch (MultiplePrimaryKeysException ex2)
                {
                    transactionErrorMessage = "ERROR: Multiple SLOs with Text!";
                    new Event(Event.getLeafLevelClassName(this), "processTransaction",
                            "Found multiple SLOs with Text : " + SLOText + ". Reason: " + ex2.toString(),
                            Event.ERROR);

                }
            }
            else
            {
                SLOModificationHelper(props);
            }

        }

    }

    //-----------------------------------------------------------
    public Object getState(String key)
    {
        if (key.equals("SLOList") == true)
        {
            return mySLOList;
        }
        else
        if (key.equals("SLOText") == true)
        {
            if (mySelectedSLO != null)
                return mySelectedSLO.getState("SLOText");
            else
                return "";
        }
        else
        if (key.equals("Notes") == true)
        {
            if (mySelectedSLO != null)
                return mySelectedSLO.getState("Notes");
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

        if ((key.equals("DoYourJob") == true) || (key.equals("CancelSLOList") == true))
        {
            doYourJob();
        }
        else
        if (key.equals("SearchSLO") == true)
        {
            processTransaction((Properties)value);
        }
        else
        if (key.equals("SLOSelected") == true)
        {
            String SLOTextSent = (String)value;
            mySelectedSLO = mySLOList.retrieve(SLOTextSent);
            try
            {

                Scene newScene = createModifySLOView();

                swapToView(newScene);

            }
            catch (Exception ex)
            {
                new Event(Event.getLeafLevelClassName(this), "processTransaction",
                        "Error in creating ModifySLOView", Event.ERROR);
            }
        }
        else
        if (key.equals("SLOData") == true)
        {
            processSLOModify((Properties)value);
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
        Scene currentScene = myViews.get("SearchSLOView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("SearchSLOView", this);
            currentScene = new Scene(newView);
            myViews.put("SearchSLOView", currentScene);

            return currentScene;
        }
        else
        {
            return currentScene;
        }
    }

    /**
     * Create the view containing the table of all matching SLOs on the search criteria sent
     */
    //------------------------------------------------------
    protected Scene createSLOCollectionView()
    {
        View newView = ViewFactory.createView("SLOCollectionView", this);
        Scene currentScene = new Scene(newView);

        return currentScene;

    }

    /**
     * Create the view using which data about selected SLOs can be modified
     */
    //------------------------------------------------------
    protected Scene createModifySLOView()
    {
        View newView = ViewFactory.createView("ModifySLOView", this);
        Scene currentScene = new Scene(newView);

        return currentScene;

    }

}