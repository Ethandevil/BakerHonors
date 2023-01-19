// tabs=4
//************************************************************
//	COPYRIGHT 2021, Kyle D. Adams Sandeep Mitra and Matthew
//  E. Morgan, State University of New York. - Brockport
//  (SUNY Brockport). ALL RIGHTS RESERVED
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

import userinterface.View;
import userinterface.ViewFactory;

/** The class containing the AddAssessmentTeamClassesTransaction for the Gen Ed Data Management application */
//==============================================================
public class AddAssessmentTeamClassesTransaction extends Transaction
{
    private AssessmentTeamCollection myAssessmentTeamList;
    private AssessmentTeamDisplayCollection myAssessmentTeamDisplayList;
    private AssessmentTeam mySelectedAssessmentTeam;
    private GenEdAreaCollection myGenEdAreaList;
    private GenEdArea mySelectedGenEdArea;



    // GUI Components

    private String transactionErrorMessage = "";

    /**
     * Constructor for this class.
     *
     */
    //----------------------------------------------------------
    public AddAssessmentTeamClassesTransaction() throws Exception
    {
        super();
    }

    //----------------------------------------------------------
    protected void setDependencies()
    {
        dependencies = new Properties();
        dependencies.setProperty("CancelAssessmentTeamList", "CancelTransaction");
        dependencies.setProperty("CancelSearchArea", "CancelTransaction");
        dependencies.setProperty("CancelAreaList", "CancelTransaction");
        dependencies.setProperty("CancelAddAssessmentTeamClasses", "CancelTransaction");
        dependencies.setProperty("AssessmentTeamClassesData", "TransactionError");

        myRegistry.setDependencies(dependencies);
    }



    //-----------------------------------------------------------
    public Object getState(String key)
    {

        if (key.equals("AssessmentTeamList") == true)
        {
            return mySelectedAssessmentTeam;
        }
        else
        if (key.equals("OfferingDisplayList") == true)
        {
            return myAssessmentTeamDisplayList;
        }
        else
        if (key.equals("GenEdAreaList") == true)
        {
            return myGenEdAreaList;
        }

        else
        if (key.equals("GenEdAreaData") == true)
        {
            if (mySelectedGenEdArea != null)
                return mySelectedGenEdArea.getState("AreaName");
            else
                return "";

        }
        else
        if (key.equals("SemData") == true)
        {
            if (mySelectedAssessmentTeam != null) {
                String kyleAndMatt = (String)mySelectedAssessmentTeam.getState("SemesterID");
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
        if (key.equals("AssessmentTeamSelected") == true)
        {
            String assessmentTeamId = (String)value;

            mySelectedAssessmentTeam = myAssessmentTeamList.retrieve(assessmentTeamId);


            try
            {
                Scene newScene = createAddAssessmentTeamClassesView();
                swapToView(newScene);
            }
            catch (Exception ex)
            {
                new Event(Event.getLeafLevelClassName(this), "processTransaction",
                        "Error in creating add new Assessment Team View", Event.ERROR);
            }
        }
        else
        if (key.equals("AssessmentTeamClassesData") == true)
        {
            Properties sandeepIsStupid = (Properties) value;
            sandeepIsStupid.setProperty("AssessmentTeamID", (String)mySelectedAssessmentTeam.getState("ID"));
            sandeepIsStupid.setProperty("Deletable", "Yes");
            AssessmentTeamClasses teachersAreHorrible = new AssessmentTeamClasses(sandeepIsStupid);
            teachersAreHorrible.update();
            transactionErrorMessage = (String)teachersAreHorrible.getState("UpdateStatusMessage");
        }
        else
        if (key.equals("SearchArea") == true) {

            Properties props = (Properties)value;

            myGenEdAreaList = new GenEdAreaCollection();

            String areaNm = props.getProperty("AreaName");
            String notes = props.getProperty("Notes");
            myGenEdAreaList.findByNameAndNotesPart(areaNm, notes);

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
            String areaNameSent = (String)value;
            int areaNameSentVal = Integer.parseInt(areaNameSent);
            mySelectedGenEdArea = myGenEdAreaList.retrieve(areaNameSentVal);

            myAssessmentTeamList = new AssessmentTeamCollection();
            myAssessmentTeamList.findByGenEdAreaId((String)mySelectedGenEdArea.getState("ID"));
            myAssessmentTeamDisplayList = new AssessmentTeamDisplayCollection(myAssessmentTeamList);

            try
            {
                Scene newScene = createAssessmentTeamDisplayCollectionView();
                swapToView(newScene);
            }
            catch (Exception ex) {
                new Event(Event.getLeafLevelClassName(this), "stateChangeRequest",
                        "Error in creating AssessmentTeamDisplayCollectionView", Event.ERROR);
            }
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
        Scene currentScene = myViews.get("SearchGenEdAreaForAssessmentTeamClassesView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("SearchGenEdAreaForAssessmentTeamClassesView", this);
            currentScene = new Scene(newView);
            myViews.put("SearchGenEdAreaForAssessmentTeamClassesView", currentScene);

            return currentScene;
        }
        else
        {
            return currentScene;
        }
    }


    /**
     * Create the view containing the table of all matching Assessment Teams on the GenEdArea sent
     */
    //------------------------------------------------------
    protected Scene createAssessmentTeamDisplayCollectionView()
    {

        Scene currentScene = myViews.get("AssessmentTeamDisplayCollectionForAssessmentTeamClassesView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("AssessmentTeamDisplayCollectionForAssessmentTeamClassesView", this);
            currentScene = new Scene(newView);
            myViews.put("AssessmentTeamDisplayCollectionForAssessmentTeamClassesView", currentScene);

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
        View newView = ViewFactory.createView("GenEdAreaCollectionForAssessmentTeamCoursesView", this);
        Scene currentScene = new Scene(newView);

        return currentScene;

    }

    /**
     * Create the view for user to input teacher data to link to GenEdArea-Semester
     * */
    protected Scene createAddAssessmentTeamClassesView()
    {
        View newView = ViewFactory.createView("AddAssessmentTeamClassesView", this);
        Scene currentScene = new Scene(newView);

        return currentScene;

    }


}

