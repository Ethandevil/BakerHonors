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

import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

// project imports
import event.Event;
import exception.InvalidPrimaryKeyException;
import exception.MultiplePrimaryKeysException;

import userinterface.View;
import userinterface.ViewFactory;

public class DeleteGenEdAreaTransaction extends Transaction{
    private GenEdAreaCollection myGenEdAreaList;
    private GenEdArea mySelectedGenEdArea;

    // GUI Components
    private String transactionErrorMessage = "";

    /**
    * Constructor for this class.
    *
    */
    //--------------------
    public DeleteGenEdAreaTransaction() throws Exception
    {
        super();
    }

    protected void setDependencies()
    {
        dependencies = new Properties();
        dependencies.setProperty("CancelSearchArea", "CancelTransaction");
        dependencies.setProperty("CancelDeleteGenEdArea", "CancelTransaction");
        dependencies.setProperty("CancelAreaList", "CancelTransaction");
        dependencies.setProperty("DeleteGenEdArea", "TransactionError");
        myRegistry.setDependencies(dependencies);
    }




    //----------------------------------------------------------
    protected void processTransaction(Properties props) {
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

    private void processGenEdAreaDelete() {
        mySelectedGenEdArea.stateChangeRequest("Status", "Inactive");
        mySelectedGenEdArea.update();
        transactionErrorMessage = (String)mySelectedGenEdArea.getState("UpdateStatusMessage");
    }

    public Object getState(String key) {
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

    //---------------------------------------------------------------------------------
    public void stateChangeRequest(String key, Object value) {
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

                Scene newScene = createDeleteGenEdAreaView();
                swapToView(newScene);

            }
            catch (Exception ex)
            {
                new Event(Event.getLeafLevelClassName(this), "processTransaction",
                        "Error in creating DeleteGenEdAreaView", Event.ERROR);
            }
        }
        else
        if (key.equals("DeleteGenEdArea") == true)
        {
            processGenEdAreaDelete();
        }

        myRegistry.updateSubscribers(key, this);
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
     * Create the view using which data about selected Gen Ed Areas can be deleted
     */
    //------------------------------------------------------
    protected Scene createDeleteGenEdAreaView() {
        View newView = ViewFactory.createView("DeleteGenEdAreaView", this);
        Scene currentScene = new Scene(newView);

        return currentScene;
    }

    protected Scene createView() {
        Scene currentScene = myViews.get("SearchGenEdAreaView");

        if (currentScene == null) {
            // create our initial view
            View newView = ViewFactory.createView("SearchGenEdAreaView", this);
            currentScene = new Scene(newView);
            myViews.put("SearchGenEdAreaView", currentScene);

            return currentScene;
        }
        else {
            return currentScene;
        }
    }
}
