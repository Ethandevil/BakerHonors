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

/** The class containing the ReportGeneratorTransaction for the ISLO Data Management application */
//==============================================================
public class ReportGeneratorTransaction extends Transaction
{

    protected String transactionErrorMessage = "";
    protected GenEdArea mySelectedGenEdArea;
    protected GenEdAreaCollection myGenEdAreaList;
    protected Semester mySelectedSemester;
    protected SemesterCollection mySemesterList;
    protected AssessmentTeamCollection myATList;
    protected AssessmentTeamDisplayCollection myATDisplayList;
    protected AssessmentTeam mySelectedAT;
    protected StudentCategorizationDisplayCollection myStudentCats;
    protected PerformanceCategoryCollection myPCC;


    /**
     * Constructor for this class.
     *
     */
    //----------------------------------------------------------
    public ReportGeneratorTransaction() throws Exception
    {
        super();
        myPCC = new PerformanceCategoryCollection();
        myPCC.findAll();
    }

    //----------------------------------------------------------
    protected void setDependencies()
    {
        dependencies = new Properties();

        dependencies.setProperty("CancelStudentCategorizationList", "CancelTransaction");
        dependencies.setProperty("CancelOfferingList", "CancelTransaction");
        dependencies.setProperty("CancelISLOList", "CancelTransaction");
        dependencies.setProperty("CancelSearchISLO", "CancelTransaction");
        dependencies.setProperty("CancelReportGenerator", "CancelTransaction");
        myRegistry.setDependencies(dependencies);
    }

    //----------------------------------------------------------
    public void processTransaction(Properties props)
    {
        try {
            Scene newScene = createSearchISLOView();
            swapToView(newScene);
        }
        catch (Exception excep) {
            new Event(Event.getLeafLevelClassName(this), "processTransaction",
                    "Error in creating Search ISLO View", Event.ERROR);
        }
    }

    //-------------------------------------------------------------
    public void processSearchTransaction(Properties props) {

        myISLOList = new ISLOCollection();

        String isloNm = props.getProperty("ISLOName");
        String desc = props.getProperty("Description");
        myISLOList.findByNameAndDescriptionPart(isloNm, desc);

        try {
            Scene newScene = createISLOCollectionView();
            swapToView(newScene);
        }
        catch (Exception excep) {
            new Event(Event.getLeafLevelClassName(this), "processTransaction",
                    "Error in creating Search ISLO View", Event.ERROR);
        }
    }

    //-----------------------------------------------------------
    public Object getState(String key)
    {
        if (key.equals("PerformanceCategory1") == true) {
            return findCategoryName("1");
        }
        else if (key.equals("PerformanceCategory2") == true) {
            return findCategoryName("2");
        }
        else if (key.equals("PerformanceCategory3") == true) {
            return findCategoryName("3");
        }
        else if (key.equals("PerformanceCategory4") == true) {
            return findCategoryName("4");
        }
        else
        if (key.equals("TransactionError") == true)
        {
            return transactionErrorMessage;
        }
        else if (key.equals("ISLOList") == true)
        {
            return myISLOList;
        }
        else if (key.equals("OfferingDisplayList") == true)
        {
            return myOfferingDisplayList;
        }
        else if (key.equals("ISLOName") == true)
        {
            if (mySelectedISLO != null)
                return mySelectedISLO.getName();
            else
                return "";
        }
        else if (key.equals("Semester") == true)
        {
            if (mySelectedOffering != null) {
                String kyleAndMatt = (String)mySelectedOffering.getState("SemesterID");
                if (kyleAndMatt != null) {
                    try {
                        Semester selectedSemester = new Semester(kyleAndMatt);
                        return selectedSemester.getSemester() + " " + selectedSemester.getYear();
                    }
                    catch (Exception ex)
                    {
                        return "whatTheHell";
                    }
                }
            }
            else
                return "";
        }
        else if (key.equals("StudentCategorizationList") == true)
        {
            return myStudentCats;
        }

        return null;
    }

    //------------------------------------------------------------
    protected String findCategoryName(String number)
    {
        String retVal = "";
        Vector<PerformanceCategory> categories = (Vector)myPCC.getState("Categories");
        for (int cnt = 0; cnt < categories.size(); cnt++) {
            PerformanceCategory nextPC = categories.get(cnt);
            String catNum = (String)nextPC.getState("Number");
            if (catNum.equals(number) == true) {
                retVal = (String)nextPC.getState("Name");
                break;
            }
        }
        return retVal;
    }

    //-----------------------------------------------------------
    public void stateChangeRequest(String key, Object value)
    {
        if (key.equals("DoYourJob") == true)
        {
            doYourJob();
        }
        else
        if (key.equals("ReportData") == true)
        {
            processTransaction((Properties)value);
        }
        else
        if (key.equals("SearchISLO") == true)
        {
            processSearchTransaction((Properties)value);
        }
        else if (key.equals("ISLOSelected") == true) {
            String isloNumSent = (String) value;
            int isloNumSentVal =
                    Integer.parseInt(isloNumSent);
            mySelectedISLO = myISLOList.retrieve(isloNumSentVal);

            myOfferingList = new OfferingCollection();
            myOfferingList.findByISLOId((String) mySelectedISLO.getState("ID"));
            myOfferingDisplayList = new OfferingDisplayCollection(myOfferingList);

            try {
                Scene newScene = createOfferingDisplayCollectionView();
                swapToView(newScene);
            } catch (Exception ex) {
                new Event(Event.getLeafLevelClassName(this), "stateChangeRequest",
                        "Error in creating OfferingDisplayCollectionView", Event.ERROR);
            }
        }
        else if (key.equals("OfferingSelected") == true) {
            String offeringId = (String) value;
            mySelectedOffering = myOfferingList.retrieve(offeringId);
            String offeringID = (String)mySelectedOffering.getState("ID");

            myStudentCats = new StudentCategorizationDisplayCollection();
            myStudentCats.findByOffering(offeringID);

            try {
                Scene newScene = createStudentCategorizationDisplayCollectionView();
                swapToView(newScene);

            }
            catch (Exception ex) {
                new Event(Event.getLeafLevelClassName(this), "stateChangeRequest",
                        "Error in creating StudentCategorizationDisplayCollectionView", Event.ERROR);
            }

        }

        myRegistry.updateSubscribers(key, this);
    }

    //------------------------------------------------------
    protected Scene createStudentCategorizationDisplayCollectionView() {
        Scene currentScene = myViews.get("StudentCategorizationDisplayCollectionView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("StudentCategorizationDisplayCollectionView", this);
            currentScene = new Scene(newView);
            myViews.put("StudentCategorizationDisplayCollectionView", currentScene);

            return currentScene;
        }
        else
        {
            return currentScene;
        }
    }

    //------------------------------------------------------
    protected Scene createOfferingDisplayCollectionView() {
        Scene currentScene = myViews.get("OfferingDisplayCollectionForReportGenerationView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("OfferingDisplayCollectionForReportGenerationView", this);
            currentScene = new Scene(newView);
            myViews.put("OfferingDisplayCollectionForReportGenerationView", currentScene);

            return currentScene;
        }
        else
        {
            return currentScene;
        }
    }

    //------------------------------------------------------
    protected Scene createISLOCollectionView() {
        Scene currentScene = myViews.get("ISLOCollectionView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("ISLOCollectionView", this);
            currentScene = new Scene(newView);
            myViews.put("ISLOCollectionView", currentScene);

            return currentScene;
        }
        else
        {
            return currentScene;
        }
    }

    //------------------------------------------------------
    protected Scene createSearchISLOView() {
        Scene currentScene = myViews.get("SearchISLOView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("SearchISLOView", this);
            currentScene = new Scene(newView);
            myViews.put("SearchISLOView", currentScene);

            return currentScene;
        }
        else
        {
            return currentScene;
        }
    }

    /**
     * Create the view of this class. And then the super-class calls
     * swapToView() to display the view in the frame
     */
    //------------------------------------------------------
    protected Scene createView()
    {
        Scene currentScene = myViews.get("ReportGeneratorView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("ReportGeneratorView", this);
            currentScene = new Scene(newView);
            myViews.put("ReportGeneratorView", currentScene);

            return currentScene;
        }
        else
        {
            return currentScene;
        }
    }
}