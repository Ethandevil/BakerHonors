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
public class UpdateOfferingTeacherTransaction extends Transaction {
    private OfferingCollection myOfferingList;
    private OfferingDisplayCollection myOfferingDisplayList;
    private Offering mySelectedOffering;
    private ISLOCollection myISLOList;
    private ISLO mySelectedISLO;
    private OfferingTeacherCollection myOfferingTeacherList;
    protected OfferingTeacher myOfferingTeacher;


    // GUI Components

    protected String transactionErrorMessage = "";

    /**
     * Constructor for this class.
     */
    //----------------------------------------------------------
    public UpdateOfferingTeacherTransaction() throws Exception {
        super();
    }

    //----------------------------------------------------------
    protected void setDependencies() {
        dependencies = new Properties();
        dependencies.setProperty("CancelOfferingList", "CancelTransaction");
        dependencies.setProperty("CancelSearchISLO", "CancelTransaction");
        dependencies.setProperty("CancelISLOList", "CancelTransaction");
        dependencies.setProperty("CancelAddOfferingTeacher", "CancelTransaction");
        dependencies.setProperty("OfferingTeacherData", "TransactionError");

        myRegistry.setDependencies(dependencies);
    }


    //-----------------------------------------------------------
    public Object getState(String key) {

        if (key.equals("OfferingList") == true) {
            return myOfferingList;
        } else if (key.equals("OfferingDisplayList") == true) {
            return myOfferingDisplayList;
        } else if (key.equals("ISLOList") == true) {
            return myISLOList;
        } else if (key.equals("ISLOData") == true) {
            if (mySelectedISLO != null)
                return mySelectedISLO.getState("ISLOName");
            else
                return "";

        } else if (key.equals("SemData") == true) {
            if (mySelectedOffering != null) {
                String kyleAndMatt = (String) mySelectedOffering.getState("SemesterID");
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
        } else if (key.equals("teacherName") == true) {
            //
           // System.out.println("Teacher name: " + myOfferingTeacher.getState("TeacherName"));
            if (myOfferingTeacher != null)

                return myOfferingTeacher.getState("TeacherName");
            else
                return "";

        } else if (key.equals("courseCode") == true) {
            if (myOfferingTeacher != null)
                return myOfferingTeacher.getState("CourseDisciplineCode");
            else
                return "";

        } else if (key.equals("courseNum") == true) {
            if (myOfferingTeacher != null)
                return myOfferingTeacher.getState("CourseNumber");
            else
                return "";

        } else if (key.equals("OfferingTeacherDisplayList") == true) {
            return myOfferingTeacherList;
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
        } else if (key.equals("OfferingSelected") == true) {
            String offeringId = (String) value;
            mySelectedOffering = myOfferingList.retrieve(offeringId);

            myOfferingTeacherList = new OfferingTeacherCollection();
            myOfferingTeacherList.findByOfferingId(offeringId);

            myOfferingTeacher = myOfferingTeacherList.retrieve(offeringId);

            try {
                Scene newScene = createOfferingTeacherCollectionView();
                swapToView(newScene);
            } catch (Exception ex) {
                new Event(Event.getLeafLevelClassName(this), "processTransaction",
                        "Error in creating Offering Teacher Collection View", Event.ERROR);
            }
        } else if (key.equals("OfferingTeacherData") == true) {

                processOfferingTeacherModify((Properties)value);

        } else if (key.equals("SearchISLO") == true) {

            Properties props = (Properties) value;

            myISLOList = new ISLOCollection();

            String isloNm = props.getProperty("ISLOName");
            String desc = props.getProperty("Description");
            myISLOList.findByNameAndDescriptionPart(isloNm, desc);

            try {
                Scene newScene = createISLOCollectionView();
                swapToView(newScene);
            } catch (Exception ex) {
                new Event(Event.getLeafLevelClassName(this), "stateChangeRequest",
                        "Error in creating ISLOCollectionView", Event.ERROR);
            }

        } else if (key.equals("ISLOSelected") == true) {
            String isloNumSent = (String) value;
            int isloNumSentVal = Integer.parseInt(isloNumSent);
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
        } else if (key.equals("OfferingTeacherSelected") == true) {
            String offeringTeacherId = (String) value;
            //DEBUG System.out.println(" offering teacher id: " + offeringTeacherId);
            myOfferingTeacher = myOfferingTeacherList.retrieve(offeringTeacherId);

            try {
                Scene newScene = createModifyOfferingTeacherView();
                swapToView(newScene);
            } catch (Exception ex) {
                new Event(Event.getLeafLevelClassName(this), "processTransaction",
                        "Error in creating add new Offering Teacher View", Event.ERROR);
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
        Scene currentScene = myViews.get("SearchISLOForOfferingTeacherView");

        if (currentScene == null) {
            // create our initial view
            View newView = ViewFactory.createView("SearchISLOForOfferingTeacherView", this);
            currentScene = new Scene(newView);
            myViews.put("SearchISLOForOfferingTeacherView", currentScene);

            return currentScene;
        } else {
            return currentScene;
        }
    }


    /**
     * Create the view containing the table of all matching offerings on the ISLO sent
     */
    //------------------------------------------------------
    protected Scene createOfferingDisplayCollectionView() {

        Scene currentScene = myViews.get("OfferingDisplayCollectionForUpdatingOfferingTeacherView");

        if (currentScene == null) {
            // create our initial view
            View newView = ViewFactory.createView("OfferingDisplayCollectionForUpdatingOfferingTeacherView", this);
            currentScene = new Scene(newView);
            myViews.put("OfferingDisplayCollectionForUpdatingOfferingTeacherView", currentScene);

            return currentScene;
        } else {
            return currentScene;
        }
    }


    //------------------------------------------------------
    protected Scene createISLOCollectionView() {
        View newView = ViewFactory.createView("ISLOCollectionForOfferingTeacherView", this);
        Scene currentScene = new Scene(newView);

        return currentScene;

    }

    //------------------------------------------------------
    protected Scene createOfferingTeacherCollectionView() {
        View newView = ViewFactory.createView("OfferingTeacherCollectionView", this);
        Scene currentScene = new Scene(newView);

        return currentScene;

    }

    /**
     * Create the view for user to update data to for sampled course
     */
	//---------------------------------------------------------------
    protected Scene createModifyOfferingTeacherView() {
		// DEBUG System.out.println("Update Offering Teacher Transaction: about to create modify offering teacher view");
        View newView = ViewFactory.createView("ModifyOfferingTeacherView", this);
        Scene currentScene = new Scene(newView);

        return currentScene;

    }
    /**
     * This method encapsulates all the logic of modifiying the Offering Teacher,
     * verifying the year, etc.
     */
    //----------------------------------------------------------
    private void processOfferingTeacherModify(Properties props)
    {
        if (props.getProperty("TeacherName") != null)
        {
            String otNm = props.getProperty("TeacherName");

            if (props.getProperty("CourseNumber") != null) {
                String otNum = props.getProperty("CourseNumber");

                if (props.getProperty("CourseDisciplineCode") != null) {
                    String otCode = props.getProperty("CourseDisciplineCode");
                    OfferingTeacherModificationHelper(props);

                }

                }
            }


        else
        {
            transactionErrorMessage = "ERROR: No Category was selected for updating!";

        }

    }

    /**
     * Helper method for Offering Teacher update
     */
    //--------------------------------------------------------------------------
    protected void OfferingTeacherModificationHelper(Properties props)
    {
        String otNm = props.getProperty("TeacherName");
        String otNum = props.getProperty("CourseNumber");
        String otCode = props.getProperty("CourseDisciplineCode");


        // Everything OK
        myOfferingTeacher.stateChangeRequest("TeacherName", otNm);
        myOfferingTeacher.stateChangeRequest("CourseNumber", otNum);
        myOfferingTeacher.stateChangeRequest("CourseDisciplineCode", otCode);
        try {
            OfferingTeacher someOT = new OfferingTeacher(otCode, otNum, otNm);
            transactionErrorMessage = "Sampled course with entered data already exists for selected ISLO/Semester link";
        }
        catch (InvalidPrimaryKeyException excep) {
            myOfferingTeacher.update();
            transactionErrorMessage = (String) myOfferingTeacher.getState("UpdateStatusMessage");
        }
        catch (MultiplePrimaryKeysException excep2) {
            transactionErrorMessage = "Impossible: multiple sampled courses already exist for ISLO/Semester link";
        }

    }

}

