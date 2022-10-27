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

/** The class containing the AddGenEdAreaSLOTransaction for the Gen Ed Data Management application */
//==============================================================
public class AddGenEdAreaSLOTransaction extends Transaction
{

    private SLO mySLO;
    private GenEdAreaCollection myGenEdAreaList;
    private GenEdArea mySelectedGenEdArea;


    // GUI Components

    private String transactionErrorMessage = "";

    /**
     * Constructor for this class.
     *
     */
    //----------------------------------------------------------
    public AddGenEdAreaSLOTransaction() throws Exception
    {
        super();
    }

    //----------------------------------------------------------
    protected void setDependencies()
    {
        dependencies = new Properties();
        dependencies.setProperty("CancelAddGenEdAreaSLO", "CancelTransaction");
        dependencies.setProperty("CancelSearchArea", "CancelTransaction");
        dependencies.setProperty("CancelAreaList", "CancelTransaction");
        dependencies.setProperty("AreaSLOData", "TransactionError");

        myRegistry.setDependencies(dependencies);
    }

    /**
     * This method encapsulates all the logic of creating the Gen Ed Area Collection and showing the view
     */
    //----------------------------------------------------------
    protected  void processTransaction(Properties props)
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
     * This method encapsulates all the logic of creating the Gen Ed Area SLO,
     * verifying its uniqueness, etc.
     */
    //----------------------------------------------------------
    protected void processGenEdAreaSLOAdd(Properties props)
    {
        if (props.getProperty("SLOText") != null)
        {
            String genEdAreaName = props.getProperty("SLOText");
                try
                {
                    if (genEdAreaName.length() > GlobalVariables.MAX_SLO_TEXT_LENGTH)
                    {
                        transactionErrorMessage = "ERROR: SLO Text chosen too long (max = " + GlobalVariables.MAX_SLO_TEXT_LENGTH + ")! ";
                    }
                    else
                    {
                        String descriptionOfGenEdArea = props.getProperty("Notes");
                        if (descriptionOfGenEdArea.length() > GlobalVariables.MAX_SLO_NOTES_LENGTH )
                        {
                            transactionErrorMessage = "ERROR: SLO notes entered too long (max = " + GlobalVariables.MAX_SLO_NOTES_LENGTH + ")! ";
                        }
                        else
                        {


                            mySLO = new SLO(props);
                            mySLO.update();
                            transactionErrorMessage = (String)mySLO.getState("UpdateStatusMessage");
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


    }

    //-----------------------------------------------------------
    public Object getState(String key)
    {
        if (key.equals("TransactionError") == true)
        {
            return transactionErrorMessage;
        }
        else if (key.equals("GenEdAreaList")){
            return myGenEdAreaList;
        }
        else if(key.equals("AreaName")){
            if (mySelectedGenEdArea != null) {
                return mySelectedGenEdArea.getState("AreaName");
            }
            else{
                return "";
            }
        }


        return null;
    }

    //-----------------------------------------------------------
    public void stateChangeRequest(String key, Object value)
    {
        // DEBUG System.out.println("AddISLOTransaction.sCR: key: " + key);

        if ((key.equals("DoYourJob")) || (key.equals("CancelAreaSLOList")))
        {
            doYourJob();
        }
        else if(key.equals("SearchArea")){
            processTransaction((Properties) value);
        }
        else if(key.equals("GenEdAreaSelected")){
            String genEdAreaNameSent = (String)value;
            mySelectedGenEdArea = myGenEdAreaList.retrieve(genEdAreaNameSent);
            try{
                Scene newScene = createAddGenEdAreaSLOView();
                swapToView(newScene);
            }
            catch (Exception ex){
                new Event(Event.getLeafLevelClassName(this), "processTransaction",
                        "Error in creating AddGenEdAreaSLOView", Event.ERROR);
            }
        }
        else
        if (key.equals("AreaSLOData"))
        {
            processTransaction((Properties)value);
        }
        else if(key.equals("AreaData")){
            processGenEdAreaSLOAdd((Properties)value);
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
     * Create the view where SLO can be added to selected Gen Ed Area
     */
    //------------------------------------------------------
    protected Scene createAddGenEdAreaSLOView()
    {
        View newView = ViewFactory.createView("AddGenEdAreaSLOView", this);
        Scene currentScene = new Scene(newView);

        return currentScene;

    }
}

