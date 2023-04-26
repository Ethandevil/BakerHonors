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
import javafx.scene.effect.Reflection;
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

/** The class containing the AddReflectionQuestionTransaction for the Gen Ed Assessment Data Management application */
//==============================================================
public class AddReflectionQuestionTransaction extends  Transaction {
    private ReflectionQuestion mySelectedReflectionQuestion;


    // GUI Components

    private String transactionErrorMessage = "";

    /**
     * Constructor for this class.
     *
     */
    //----------------------------------------------------------
    public AddReflectionQuestionTransaction() throws Exception
    {
        super();
    }

    //----------------------------------------------------------
    protected void setDependencies()
    {
        dependencies = new Properties();
        dependencies.setProperty("CancelAddQuestion", "CancelTransaction");
        dependencies.setProperty("OK", "CancelTransaction");
        dependencies.setProperty("QuestionData", "TransactionError");

        myRegistry.setDependencies(dependencies);
    }

    /**
     * This method encapsulates all the logic of creating the Gen Ed Area,
     * verifying its uniqueness, etc.
     */
    //----------------------------------------------------------
    protected void processTransaction(Properties props)
    {
        if (props.getProperty("QuestionText") != null)
        {
            String questionText = props.getProperty("QuestionText");
            try
            {
                ReflectionQuestion oldReflectionQuestion = new ReflectionQuestion(questionText, true);
                transactionErrorMessage = "ERROR: Reflection question " + questionText
                        + " already exists!";
                new Event(Event.getLeafLevelClassName(this), "processTransaction",
                        "Reflection question with text : " + questionText + " already exists!",
                        Event.ERROR);
            }
            catch (InvalidPrimaryKeyException ex)
            {
                // GenEdArea Name does not exist, validate data
                try
                {

                    if (questionText.length() > GlobalVariables.MAX_REFLECTION_QUESTION_TEXT_LENGTH)
                    {
                        transactionErrorMessage = "ERROR: Reflection question text chosen too long (max = " + GlobalVariables.MAX_REFLECTION_QUESTION_TEXT_LENGTH + ")! ";
                    }
                    else
                    {
                        mySelectedReflectionQuestion = new ReflectionQuestion(props);
                        mySelectedReflectionQuestion.update();
                        transactionErrorMessage = (String)mySelectedReflectionQuestion.getState("UpdateStatusMessage");
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
            catch (MultiplePrimaryKeysException ex2)
            {
                transactionErrorMessage = "ERROR: Multiple Reflection Questions with chosen question text!";
                new Event(Event.getLeafLevelClassName(this), "processTransaction",
                        "Found multiple Reflection Questions with question text : " + questionText + ". Reason: " + ex2.toString(),
                        Event.ERROR);

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

        return null;
    }

    //-----------------------------------------------------------
    public void stateChangeRequest(String key, Object value)
    {
        // DEBUG System.out.println("UpdateISLOTransaction.sCR: key: " + key);

        if ((key.equals("DoYourJob") == true))
        {
            doYourJob();
        }
        else
        if (key.equals("QuestionData") == true)
        {
            processTransaction((Properties)value);
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
        Scene currentScene = myViews.get("AddReflectionQuestionView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("AddReflectionQuestionView", this);
            currentScene = new Scene(newView);
            myViews.put("AddReflectionQuestionView", currentScene);

            return currentScene;
        }
        else
        {
            return currentScene;
        }
    }
}
