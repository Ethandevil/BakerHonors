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

/** The class containing the ModifyReflectionQuestionTransaction for the Gen Ed Assessment Data Management application */
//==============================================================
public class ModifyReflectionQuestionTransaction extends  Transaction {
    private ReflectionQuestionCollection myReflectionQuestionList;
    private ReflectionQuestion mySelectedReflectionQuestion;


    // GUI Components

    private String transactionErrorMessage = "";

    /**
     * Constructor for this class.
     *
     */
    //----------------------------------------------------------
    public ModifyReflectionQuestionTransaction() throws Exception
    {
        super();
    }

    //----------------------------------------------------------
    protected void setDependencies()
    {
        dependencies = new Properties();
        dependencies.setProperty("CancelSearchQuestion", "CancelTransaction");
        dependencies.setProperty("CancelAddQuestion", "CancelTransaction");
        dependencies.setProperty("CancelQuestionList", "CancelTransaction");
        dependencies.setProperty("QuestionData", "TransactionError");

        myRegistry.setDependencies(dependencies);
    }

    /**
     * This method encapsulates all the logic of creating the Reflection Question Collection and showing the view
     */
    //----------------------------------------------------------
    protected void processTransaction(Properties props)
    {
        myReflectionQuestionList = new ReflectionQuestionCollection();

        String reflectionQuestionTxt = props.getProperty("QuestionText");
        myReflectionQuestionList.findByTextPart(reflectionQuestionTxt);

        try
        {
            Scene newScene = createReflectionQuestionCollectionView();
            swapToView(newScene);
        }
        catch (Exception ex)
        {
            new Event(Event.getLeafLevelClassName(this), "processTransaction",
                    "Error in creating ReflectionQuestionCollectionView", Event.ERROR);
        }
    }

    /**
     * Helper method for Reflection Question update
     */
    //--------------------------------------------------------------------------
    private void reflectionQuestionModificationHelper(Properties props)
    {
        String textOfReflectionQuestion = props.getProperty("QuestionText");
        if (textOfReflectionQuestion.length() > GlobalVariables.MAX_REFLECTION_QUESTION_TEXT_LENGTH)
        {
            transactionErrorMessage = "ERROR: Reflection Question Text too long! ";
        }
        else
        {
            // Everything OK

            mySelectedReflectionQuestion.stateChangeRequest("QuestionText", textOfReflectionQuestion);
            mySelectedReflectionQuestion.update();
            transactionErrorMessage = (String)mySelectedReflectionQuestion.getState("UpdateStatusMessage");
        }
    }

    /**
     * This method encapsulates all the logic of modifiying the Reflection Question,
     *
     */
    //----------------------------------------------------------
    private void processReflectionQuestionModify(Properties props)
    {
        if (props.getProperty("QuestionText") != null)
        {
            String reflectionQuestionText = props.getProperty("QuestionText");
            String originalReflectionQuestionText = (String)mySelectedReflectionQuestion.getState("QuestionText");
            if (reflectionQuestionText.equals(originalReflectionQuestionText) == false)
            {
                try
                {
                    ReflectionQuestion oldReflectionQuestion = new ReflectionQuestion(reflectionQuestionText, true);
                    transactionErrorMessage = "ERROR: Reflection Question Text: " + reflectionQuestionText
                            + " already exists!";
                    new Event(Event.getLeafLevelClassName(this), "processTransaction",
                            "Reflection Question with text : " + reflectionQuestionText + " already exists!",
                            Event.ERROR);
                }
                catch (InvalidPrimaryKeyException ex)
                {
                    // Reflection Question Text does not exist, validate data

                    mySelectedReflectionQuestion.stateChangeRequest("QuestionText", reflectionQuestionText);
                    // Process the rest (name, notes). Helper does all that
                    reflectionQuestionModificationHelper(props);
                }
                catch (MultiplePrimaryKeysException ex2)
                {
                    transactionErrorMessage = "ERROR: Multiple Reflection Questions with Text!";
                    new Event(Event.getLeafLevelClassName(this), "processTransaction",
                            "Found multiple Reflection Questions with Text : " + reflectionQuestionText + ". Reason: " + ex2.toString(),
                            Event.ERROR);

                }
            }
            else
            {
                reflectionQuestionModificationHelper(props);
            }

        }

    }

    //-----------------------------------------------------------
    public Object getState(String key)
    {
        if (key.equals("ReflectionQuestionList") == true)
        {
            return myReflectionQuestionList;
        }
        else
        if (key.equals("QuestionText") == true)
        {
            if (mySelectedReflectionQuestion != null)
                return mySelectedReflectionQuestion.getState("QuestionText");
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

        if ((key.equals("DoYourJob") == true) || (key.equals("CancelQuestionList") == true))
        {
            doYourJob();
        }
        else
        if (key.equals("SearchQuestion") == true)
        {
            processTransaction((Properties)value);
        }
        else
        if (key.equals("ReflectionQuestionSelected") == true)
        {
            String reflectionQuestionTextSent = (String)value;
            mySelectedReflectionQuestion = myReflectionQuestionList.retrieve(reflectionQuestionTextSent);
            try
            {

                Scene newScene = createModifyReflectionQuestionView();

                swapToView(newScene);

            }
            catch (Exception ex)
            {
                new Event(Event.getLeafLevelClassName(this), "processTransaction",
                        "Error in creating ModifyReflectionQuestionView", Event.ERROR);
            }
        }
        else
        if (key.equals("QuestionData") == true)
        {
            processReflectionQuestionModify((Properties)value);
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
        Scene currentScene = myViews.get("SearchReflectionQuestionView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("SearchReflectionQuestionView", this);
            currentScene = new Scene(newView);
            myViews.put("SearchReflectionQuestionView", currentScene);

            return currentScene;
        }
        else
        {
            return currentScene;
        }
    }

    /**
     * Create the view containing the table of all matching Reflection Questions on the search criteria sent
     */
    //------------------------------------------------------
    protected Scene createReflectionQuestionCollectionView()
    {
        View newView = ViewFactory.createView("ReflectionQuestionCollectionView", this);
        Scene currentScene = new Scene(newView);

        return currentScene;

    }

    /**
     * Create the view using which data about selected Reflection Questions can be modified
     */
    //------------------------------------------------------
    protected Scene createModifyReflectionQuestionView()
    {
        View newView = ViewFactory.createView("ModifyReflectionQuestionView", this);
        Scene currentScene = new Scene(newView);

        return currentScene;

    }

}
