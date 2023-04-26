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

/** The class containing the UpdatePerformanceCategoryTransaction for the ISLO Data Management application */
//==============================================================
public class UpdatePerformanceCategoryTransaction extends Transaction
{

    private PerformanceCategoryCollection myPerformanceCategoryList;
    private PerformanceCategory mySelectedCategory;


    // GUI Components

    private String transactionErrorMessage = "";

    /**
     * Constructor for this class.
     *
     */
    //----------------------------------------------------------
    public UpdatePerformanceCategoryTransaction() throws Exception
    {

        super();

        myPerformanceCategoryList = new PerformanceCategoryCollection();

        myPerformanceCategoryList.findAll();
        try
        {
            createView();
        }
        catch (Exception ex)
        {
            new Event(Event.getLeafLevelClassName(this), "processTransaction",
                    "Error in creating PerformanceCategoryCollectionView", Event.ERROR);
        }

    }

    //----------------------------------------------------------
    protected void setDependencies()
    {
        dependencies = new Properties();
        dependencies.setProperty("CancelModifyPerformanceCategory", "CancelTransaction");
        dependencies.setProperty("CancelAddPerformanceCategory", "CancelTransaction");
        dependencies.setProperty("PerformanceCategoryData", "TransactionError");

        myRegistry.setDependencies(dependencies);
    }

    //-----------------------------------------------------------------------
    public Object getState(String key) {
        if (key.equals("TransactionError"))
        {
            return transactionErrorMessage;
        }
        else
        if (key.equals("PerformanceCategoryList")){
            return myPerformanceCategoryList;
        }
        else if(key.equals("Name"))
        {
            if (mySelectedCategory != null)
                return mySelectedCategory.getState("Name");
            else
                return "";
        }
        else if(key.equals("Number"))
        {
            if (mySelectedCategory != null)
                return mySelectedCategory.getState("Number");
            else
                return "";
        }
        else
            return null;
    }

    //---------------------------------------------------------------------------
    public void stateChangeRequest(String key, Object value) {
        if ((key.equals("DoYourJob") == true) || (key.equals("CancelPerformanceCategoryList") == true))
        {
            doYourJob();
        }
        else if((key.equals("CategorySelected") == true))
        {
            String catIdSent = (String)value;
            int catIdSentVal =
                    Integer.parseInt(catIdSent);
            mySelectedCategory = myPerformanceCategoryList.retrieve(catIdSentVal);
            try
            {

                Scene newScene = createModifyPerformanceCategoryView();

                swapToView(newScene);

            }
            catch (Exception ex)
            {
                new Event(Event.getLeafLevelClassName(this), "processTransaction",
                        "Error in creating ModifyPerformanceCategoryView", Event.ERROR);
            }
        }
        else
        if (key.equals("PerformanceCategoryData") == true)
        {
            processPerformanceCategoryModify((Properties)value);
        }

        myRegistry.updateSubscribers(key, this);
    }

    /**
     * This method encapsulates all the logic of modifiying the Performance Category,
     * verifying the year, etc.
     */
    //----------------------------------------------------------
    private void processPerformanceCategoryModify(Properties props)
    {
        if (props.getProperty("Name") != null)
        {
            String catNm = props.getProperty("Name");
            if (props.getProperty("Number") != null)
            {
                String catNum = props.getProperty("Number");
                try
                {
                    PerformanceCategory oldCat = new PerformanceCategory(catNm, catNum);
                    transactionErrorMessage = "ERROR: Category: ( " + catNm + ", " + catNum + ") already exists!";
                    new Event(Event.getLeafLevelClassName(this), "processTransaction",
                            "Category: ( " + catNm + ", " + catNum + ") already exists!",
                            Event.ERROR);
                }
                catch (InvalidPrimaryKeyException ex)
                {
                    categoryModificationHelper(props);
                }
                catch (MultiplePrimaryKeysException ex2)
                {
                    transactionErrorMessage = "ERROR: Multiple Categories with values: (" + catNm + ", " + catNum + ") !";
                    new Event(Event.getLeafLevelClassName(this), "processTransaction",
                            "Multiple Categories with values: (" + catNm + ", " + catNum + ")",
                            Event.ERROR);

                }
            }

        }
        else
        {
            transactionErrorMessage = "ERROR: No Category was selected for updating!";

        }

    }
    /**
     * Helper method for Performance Category update
     */
    //--------------------------------------------------------------------------
    private void categoryModificationHelper(Properties props)
    {
        String catNm = props.getProperty("Name");
        String catNum = props.getProperty("Number");

            // Everything OK

            mySelectedCategory.stateChangeRequest("Number", catNum);
            mySelectedCategory.stateChangeRequest("Name", catNm);
            mySelectedCategory.update();
            transactionErrorMessage = (String)mySelectedCategory.getState("UpdateStatusMessage");


    }

    private Scene createModifyPerformanceCategoryView() {
        View newView = ViewFactory.createView("ModifyPerformanceCategoryView", this);
        Scene currentScene = new Scene(newView);

        return currentScene;
    }

    /**
     * Create the view of this class. And then the super-class calls
     * swapToView() to display the view in the frame
     */

    //------------------------------------------------------
    protected Scene createView()
    {

        Scene currentScene = myViews.get("PerformanceCategoryCollectionView");

        if (currentScene == null)
        {
            // create our initial view

            View newView = ViewFactory.createView("PerformanceCategoryCollectionView", this);
            currentScene = new Scene(newView);
            myViews.put("PerformanceCategoryCollectionView", currentScene);

        }
        return currentScene;
    }

}

