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

/**
 * The class containing the AddOfferingTransaction for the ISLO Data Management application
 */
//==============================================================
public class ModifyAssessmentTeamClassesTransaction extends Transaction {
    private AssessmentTeamCollection myAssessmentTeamList;
    private AssessmentTeamDisplayCollection myAssessmentTeamDisplayList;
    private AssessmentTeam mySelectedAssessmentTeam;
    private GenEdAreaCollection myGenEdAreaList;
    private GenEdArea mySelectedGenEdArea;
    private AssessmentTeamClassesCollection myAssessmentTeamClassesList;
    protected AssessmentTeamClasses myAssessmentTeamClasses;


    // GUI Components

    protected String transactionErrorMessage = "";

    /**
     * Constructor for this class.
     */
    //----------------------------------------------------------
    public ModifyAssessmentTeamClassesTransaction() throws Exception {
        super();
    }

    //----------------------------------------------------------
    protected void setDependencies() {
        dependencies = new Properties();
        dependencies.setProperty("CancelAssessmentTeamList", "CancelTransaction");
        dependencies.setProperty("CancelSearchArea", "CancelTransaction");
        dependencies.setProperty("CancelAreaList", "CancelTransaction");
        dependencies.setProperty("CancelAssessmentTeamClassesList", "CancelTransaction");
        dependencies.setProperty("CancelAddAssessmentTeamClasses", "CancelTransaction");
        dependencies.setProperty("AssessmentTeamClassesData", "TransactionError");

        myRegistry.setDependencies(dependencies);
    }


    //-----------------------------------------------------------
    public Object getState(String key) {

        if (key.equals("AssessmentTeamList") == true) {
            return myAssessmentTeamList;
        } else if (key.equals("AssessmentTeamDisplayList") == true) {
            return myAssessmentTeamDisplayList;
        } else if (key.equals("GenEdAreaList") == true) {
            return myGenEdAreaList;
        } else if (key.equals("GenEdAreaData") == true) {
            if (mySelectedGenEdArea != null)
                return mySelectedGenEdArea.getState("AreaName");
            else
                return "";

        } else if (key.equals("SemData") == true) {
            if (mySelectedAssessmentTeam != null) {
                String kyleAndMatt = (String) mySelectedAssessmentTeam.getState("SemesterID");
               // System.out.println(kyleAndMatt);
                if (kyleAndMatt != null) {
                    try {
                        Semester selectedSemester = new Semester(kyleAndMatt);
                        return selectedSemester.getSemester() + " " + selectedSemester.getYear();
                    } catch (Exception ex) {
                        return "whatTheHell";
                    }
                }
            } else
                return "";
        } else if (key.equals("courseCode") == true) {
            if (myAssessmentTeamClasses != null)
                return myAssessmentTeamClasses.getState("CourseDisciplineCode");
            else
                return "";

        } else if (key.equals("courseNum") == true) {
            if (myAssessmentTeamClasses != null)
                return myAssessmentTeamClasses.getState("CourseNumber");
            else
                return "";

        } else if (key.equals("AssessmentTeamClassesDisplayList") == true) {
            return myAssessmentTeamClassesList;
        } else if (key.equals("TransactionError") == true) {
            return transactionErrorMessage;
        }


        return null;
    }

    //-----------------------------------------------------------
    public void stateChangeRequest(String key, Object value) {
        // DEBUG System.out.println("UpdateOfferingTeacherTransaction.sCR: key: " + key);

        if (key.equals("DoYourJob") == true) {
            doYourJob();
        } else if (key.equals("AssessmentTeamSelected") == true) {
            String assessmentTeamId = (String) value;
            mySelectedAssessmentTeam = myAssessmentTeamList.retrieve(assessmentTeamId);

            myAssessmentTeamClassesList = new AssessmentTeamClassesCollection();
            myAssessmentTeamClassesList.findByAssessmentTeamId(assessmentTeamId);

            myAssessmentTeamClasses = myAssessmentTeamClassesList.retrieve(assessmentTeamId);

            try {
                Scene newScene = createAssessmentTeamClassesCollectionView();
                swapToView(newScene);
            } catch (Exception ex) {
                new Event(Event.getLeafLevelClassName(this), "processTransaction",
                        "Error in creating Assessment Team Classes Collection View", Event.ERROR);
            }
        } else if (key.equals("AssessmentTeamClassesData") == true) {

                processAssessmentTeamClassesModify((Properties)value);

        } else if (key.equals("SearchArea") == true) {

            Properties props = (Properties) value;

            myGenEdAreaList = new GenEdAreaCollection();

            String areaNm = props.getProperty("AreaName");
            String notes = props.getProperty("Notes");
            myGenEdAreaList.findByNameAndNotesPart(areaNm, notes);

            try {
                Scene newScene = createGenEdAreaCollectionView();
                swapToView(newScene);
            } catch (Exception ex) {
                new Event(Event.getLeafLevelClassName(this), "stateChangeRequest",
                        "Error in creating GenEdAreaCollectionView", Event.ERROR);
            }

        } else if (key.equals("GenEdAreaSelected") == true) {
            String areaNameSent = (String) value;
            mySelectedGenEdArea = myGenEdAreaList.retrieve(areaNameSent);

            myAssessmentTeamList = new AssessmentTeamCollection();
            myAssessmentTeamList.findByGenEdAreaId((String) mySelectedGenEdArea.getState("ID"));
            myAssessmentTeamDisplayList = new AssessmentTeamDisplayCollection(myAssessmentTeamList);

            try {
                Scene newScene = createAssessmentTeamDisplayCollectionView();
                swapToView(newScene);
            } catch (Exception ex) {
                new Event(Event.getLeafLevelClassName(this), "stateChangeRequest",
                        "Error in creating AssessmentTeamDisplayCollectionView", Event.ERROR);
            }
        } else if (key.equals("AssessmentTeamClassSelected") == true) {
            String assessmentTeamClassesId = (String) value;
            //DEBUG System.out.println(" offering teacher id: " + offeringTeacherId);
            myAssessmentTeamClasses = myAssessmentTeamClassesList.retrieve(assessmentTeamClassesId);

            try {
                Scene newScene = createModifyAssessmentTeamClassesView();
                swapToView(newScene);
            } catch (Exception ex) {
                new Event(Event.getLeafLevelClassName(this), "processTransaction",
                        "Error in creating add new Assessment Team Classes View", Event.ERROR);
            }
        }


        myRegistry.updateSubscribers(key, this);
    }

    /**
     * Create the view of this class. And then the super-class calls
     * swapToView() to display the view in the frame
     */
    //------------------------------------------------------
    protected Scene createView() {
        Scene currentScene = myViews.get("SearchGenEdAreaView");
        //SearchGenEdAreaForAssessmentTeamClassesView

        if (currentScene == null) {
            // create our initial view
            View newView = ViewFactory.createView("SearchGenEdAreaView", this);
            currentScene = new Scene(newView);
            myViews.put("SearchGenEdAreaView", currentScene);

            return currentScene;
        } else {
            return currentScene;
        }
    }


    /**
     * Create the view containing the table of all matching offerings on the ISLO sent
     */
    //------------------------------------------------------
    protected Scene createAssessmentTeamDisplayCollectionView() {

        Scene currentScene = myViews.get("AssessmentTeamDisplayCollectionForUpdatingAssessmentTeamClassesView");

        if (currentScene == null) {
            // create our initial view
            View newView = ViewFactory.createView("AssessmentTeamDisplayCollectionForUpdatingAssessmentTeamClassesView", this);
            currentScene = new Scene(newView);
            myViews.put("AssessmentTeamDisplayCollectionForUpdatingAssessmentTeamClassesView", currentScene);

            return currentScene;
        } else {
            return currentScene;
        }
    }


    //------------------------------------------------------
    protected Scene createGenEdAreaCollectionView() {
        View newView = ViewFactory.createView("GenEdAreaCollectionForAssessmentTeamCoursesView", this);
        Scene currentScene = new Scene(newView);

        return currentScene;

    }

    //------------------------------------------------------
    protected Scene createAssessmentTeamClassesCollectionView() {
        View newView = ViewFactory.createView("AssessmentTeamClassesCollectionView", this);
        Scene currentScene = new Scene(newView);

        return currentScene;

    }

    /**
     * Create the view for user to update data to for sampled course
     */
	//---------------------------------------------------------------
    protected Scene createModifyAssessmentTeamClassesView() {
		// DEBUG System.out.println("Update Offering Teacher Transaction: about to create modify offering teacher view");
        View newView = ViewFactory.createView("ModifyAssessmentTeamClassesView", this);
        Scene currentScene = new Scene(newView);

        return currentScene;

    }
    /**
     * This method encapsulates all the logic of modifiying the Offering Teacher,
     * verifying the year, etc.
     */
    //----------------------------------------------------------
    private void processAssessmentTeamClassesModify(Properties props)
    {
        if (props.getProperty("CourseNumber") != null) {
            String otNum = props.getProperty("CourseNumber");

            if (props.getProperty("CourseDisciplineCode") != null) {
                String otCode = props.getProperty("CourseDisciplineCode");
                AssessmentTeamClassesModificationHelper(props);

            }

        }

        else
        {
            transactionErrorMessage = "ERROR: No Category was selected for updating!";

        }

    }

    /**
     * Helper method for Assessment Team Classes update
     */
    //--------------------------------------------------------------------------
    protected void AssessmentTeamClassesModificationHelper(Properties props)
    {
        String atcNum = props.getProperty("CourseNumber");
        String atcCode = props.getProperty("CourseDisciplineCode");


        // Everything OK
        myAssessmentTeamClasses.stateChangeRequest("CourseNumber", atcNum);
        myAssessmentTeamClasses.stateChangeRequest("CourseDisciplineCode", atcCode);
        try {
            AssessmentTeamClasses someOT = new AssessmentTeamClasses(atcCode, atcNum);
            transactionErrorMessage = "Assessment Team Classes with entered data already exists for selected GenEdArea/Semester link";
        }
        catch (InvalidPrimaryKeyException excep) {
            myAssessmentTeamClasses.update();
            transactionErrorMessage = (String) myAssessmentTeamClasses.getState("UpdateStatusMessage");
        }
        catch (MultiplePrimaryKeysException excep2) {
            transactionErrorMessage = "Impossible: multiple Assessment Team Classes already exist for GenEdArea/Semester link";
        }

    }

}

