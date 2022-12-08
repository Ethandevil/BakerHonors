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

/** The class containing the AddOfferingTransaction for the ISLO Data Management application */
//==============================================================
public class AddAssessmentTeamClassesTransaction extends Transaction
{
    private OfferingCollection myOfferingList;
    private OfferingDisplayCollection myOfferingDisplayList;
    private Offering mySelectedOffering;
    private ISLOCollection myISLOList;
    private ISLO mySelectedISLO;



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
        dependencies.setProperty("CancelOfferingList", "CancelTransaction");
        dependencies.setProperty("CancelSearchISLO", "CancelTransaction");
        dependencies.setProperty("CancelISLOList", "CancelTransaction");
        dependencies.setProperty("CancelAddOfferingTeacher", "CancelTransaction");
        dependencies.setProperty("OfferingTeacherData", "TransactionError");

        myRegistry.setDependencies(dependencies);
    }



    //-----------------------------------------------------------
    public Object getState(String key)
    {

        if (key.equals("OfferingList") == true)
        {
            return myOfferingList;
        }
        else
        if (key.equals("OfferingDisplayList") == true)
        {
            return myOfferingDisplayList;
        }
        else
        if (key.equals("ISLOList") == true)
        {
            return myISLOList;
        }

        else
        if (key.equals("ISLOData") == true)
        {
            if (mySelectedISLO != null)
                return mySelectedISLO.getState("ISLOName");
            else
                return "";

        }
        else
        if (key.equals("SemData") == true)
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
        if (key.equals("OfferingSelected") == true)
        {
            String offeringId = (String)value;

            mySelectedOffering = myOfferingList.retrieve(offeringId);


            try
            {
                Scene newScene = createAddOfferingTeacherView();
                swapToView(newScene);
            }
            catch (Exception ex)
            {
                new Event(Event.getLeafLevelClassName(this), "processTransaction",
                        "Error in creating add new Offering Teacher View", Event.ERROR);
            }
        }
        else
        if (key.equals("OfferingTeacherData") == true)
        {
            Properties sandeepIsStupid = (Properties) value;
            sandeepIsStupid.setProperty("OfferingID", (String)mySelectedOffering.getState("ID"));
            sandeepIsStupid.setProperty("Deletable", "Yes");
            OfferingTeacher teachersAreHorrible = new OfferingTeacher(sandeepIsStupid);
            teachersAreHorrible.update();
            transactionErrorMessage = (String)teachersAreHorrible.getState("UpdateStatusMessage");
        }
        else
        if (key.equals("SearchISLO") == true) {

            Properties props = (Properties)value;

            myISLOList = new ISLOCollection();

            String isloNm = props.getProperty("ISLOName");
            String desc = props.getProperty("Description");
            myISLOList.findByNameAndDescriptionPart(isloNm, desc);

            try
            {
                Scene newScene = createISLOCollectionView();
                swapToView(newScene);
            }
            catch (Exception ex)
            {
                new Event(Event.getLeafLevelClassName(this), "stateChangeRequest",
                        "Error in creating ISLOCollectionView", Event.ERROR);
            }

        }
        else
        if (key.equals("ISLOSelected") == true)
        {
            String isloNumSent = (String)value;
            int isloNumSentVal = Integer.parseInt(isloNumSent);
            mySelectedISLO = myISLOList.retrieve(isloNumSentVal);

            myOfferingList = new OfferingCollection();
            myOfferingList.findByISLOId((String)mySelectedISLO.getState("ID"));
            myOfferingDisplayList = new OfferingDisplayCollection(myOfferingList);

            try
            {
                Scene newScene = createOfferingDisplayCollectionView();
                swapToView(newScene);
            }
            catch (Exception ex) {
                new Event(Event.getLeafLevelClassName(this), "stateChangeRequest",
                        "Error in creating OfferingDisplayCollectionView", Event.ERROR);
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
        Scene currentScene = myViews.get("SearchISLOForOfferingTeacherView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("SearchISLOForOfferingTeacherView", this);
            currentScene = new Scene(newView);
            myViews.put("SearchISLOForOfferingTeacherView", currentScene);

            return currentScene;
        }
        else
        {
            return currentScene;
        }
    }


    /**
     * Create the view containing the table of all matching offerings on the ISLO sent
     */
    //------------------------------------------------------
    protected Scene createOfferingDisplayCollectionView()
    {

        Scene currentScene = myViews.get("OfferingDisplayCollectionForOfferingTeacherView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("OfferingDisplayCollectionForOfferingTeacherView", this);
            currentScene = new Scene(newView);
            myViews.put("OfferingDisplayCollectionForOfferingTeacherView", currentScene);

            return currentScene;
        }
        else
        {
            return currentScene;
        }
    }



    //------------------------------------------------------
    protected Scene createISLOCollectionView()
    {
        View newView = ViewFactory.createView("ISLOCollectionForOfferingTeacherView", this);
        Scene currentScene = new Scene(newView);

        return currentScene;

    }

    /**
     * Create the view for user to input teacher data to link to ISLO-Semester
     * */
    protected Scene createAddOfferingTeacherView()
    {
        View newView = ViewFactory.createView("AddOfferingTeacherView", this);
        Scene currentScene = new Scene(newView);

        return currentScene;

    }


}

