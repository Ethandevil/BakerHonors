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
import javafx.scene.Scene;
import java.util.Properties;

// project imports
import event.Event;
import exception.InvalidPrimaryKeyException;
import exception.MultiplePrimaryKeysException;

import userinterface.View;
import userinterface.ViewFactory;

//This transaction will manage the entering of student categorization data and instructor reflections
//=================================================================================
public class AddStudentCategorizationReflectionTransaction extends  Transaction {

    private SemesterCollection mySemesterList;
    private Semester mySelectedSemester;
    private GenEdAreaCollection myGenEdAreaList;
    private GenEdArea mySelectedGenEdArea;

    // GUI Components
    private String transactionErrorMessage = "";

    /**
     * Constructor for this class.
     *
     */
    //----------------------------------------------------------
    public AddStudentCategorizationReflectionTransaction() throws Exception{
        super();
    }

    //----------------------------------------------------------
    protected void setDependencies()
    {
        dependencies = new Properties();
        dependencies.setProperty("CancelAddStudentCategorization", "CancelTransaction");
        dependencies.setProperty("CancelAddInstructorReflection", "CancelTransaction");
        dependencies.setProperty("CancelSemesterList", "CancelTransaction");
        dependencies.setProperty("CancelSearchArea", "CancelTransaction");
        dependencies.setProperty("CancelAreaList", "CancelTransaction");
        dependencies.setProperty("GenEdAreaSelected", "TransactionError");

        myRegistry.setDependencies(dependencies);
    }

    //-----------------------------------------------------------
    public Object getState(String key)
    {

        if (key.equals("SemesterList") == true)
        {
            return mySemesterList;
        }
        else
        if (key.equals("GenEdAreaList") == true)
        {
            return myGenEdAreaList;
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
        // DEBUG System.out.println("AddOfferingTransaction.sCR: key: " + key);

        if (key.equals("DoYourJob") == true)
        {
            doYourJob();
        }
        else
        if (key.equals("SearchSemester") == true)
        {
            Properties props = (Properties)value;

            mySemesterList = new SemesterCollection();

            String semNm = props.getProperty("SemName");
            String semYr = props.getProperty("Year");
            mySemesterList.findBySemNameAndYear(semNm, semYr);

            try
            {
                Scene newScene = createSemesterCollectionView();
                swapToView(newScene);
            }
            catch (Exception ex)
            {
                new Event(Event.getLeafLevelClassName(this), "processTransaction",
                        "Error in creating SemesterCollectionView", Event.ERROR);
            }
        }
        else
        if (key.equals("SemesterSelected") == true)
        {

            String semIdSent = (String)value;
            int semIdSentVal =
                    Integer.parseInt(semIdSent);
            mySelectedSemester = mySemesterList.retrieve(semIdSentVal);

            try
            {

                Scene newScene = createSearchGenEdAreaView();

                swapToView(newScene);

            }
            catch (Exception ex)
            {
                new Event(Event.getLeafLevelClassName(this), "stateChangeRequest",
                        "Error in creating SearchGenEdAreaView", Event.ERROR);
                ex.printStackTrace();
            }
        }

        else
        if (key.equals("SearchArea") == true) {

            Properties props = (Properties)value;

            myGenEdAreaList = new GenEdAreaCollection();

            String genNm = props.getProperty("GenEdAreaName");
            String desc = props.getProperty("Notes");
            myGenEdAreaList.findByNameAndNotesPart(genNm, desc);

            try
            {
                Scene newScene = createGenEdAreaCollectionView();
                swapToView(newScene);
            }
            catch (Exception ex)
            {
                new Event(Event.getLeafLevelClassName(this), "stateChangeRequest",
                        "Error in creating GenEdAreaCollectionView", Event.ERROR);
            }

        }
        else
        if (key.equals("GenEdAreaSelected") == true)
        {
            String genNumSent = (String)value;

            mySelectedGenEdArea = myGenEdAreaList.retrieve(genNumSent);


            Scene s = createStudentCategorizationAndReflectionChoiceView();
            swapToView(s);
            //processTransaction(props);
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
        Scene currentScene = myViews.get("SearchSemesterForASCIRView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("SearchSemesterForASCIRView", this);
            currentScene = new Scene(newView);
            myViews.put("SearchSemesterForASCIRView", currentScene);

            return currentScene;
        }
        else
        {
            return currentScene;
        }
    }


    /**
     * Create the view containing the table of all matching Semesters on the search criteria sent
     */
    //------------------------------------------------------
    protected Scene createSemesterCollectionView()
    {
        View newView = ViewFactory.createView("SemesterCollectionForASCIRView", this);
        Scene currentScene = new Scene(newView);

        return currentScene;

    }

    //------------------------------------------------------
    protected Scene createSearchGenEdAreaView()
    {

        Scene currentScene = myViews.get("SearchGenEdAreaForASCIRView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("SearchGenEdAreaForASCIRView", this);
            currentScene = new Scene(newView);
            myViews.put("SearchGenEdAreaForASCIRView", currentScene);

            return currentScene;
        }
        else
        {
            return currentScene;
        }

    }

    //------------------------------------------------------
    protected Scene createGenEdAreaCollectionView()
    {
        View newView = ViewFactory.createView("GenEdAreaCollectionForASCIRView", this);
        Scene currentScene = new Scene(newView);

        return currentScene;

    }

    //------------------------------------------------------
    protected Scene createStudentCategorizationAndReflectionChoiceView(){
        View newView = ViewFactory.createView("StudentCategorizationAndReflectionChoiceView", this);
        Scene currentScene = new Scene(newView);

        return currentScene;
    }


}
