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
import java.util.Properties;
import java.util.Vector;
import javafx.scene.Scene;

// project imports
import exception.InvalidPrimaryKeyException;
import event.Event;
import database.*;

import impresario.IView;

import userinterface.View;
import userinterface.ViewFactory;


/** The class containing the ReflectionQuestionCollection for the Gen Ed Assessment
 * Management application
 */
//==============================================================
public class ReflectionQuestionCollection extends EntityBase implements IView {
    private static final String myTableName = "InstructorReflectionQuestions";

    private Vector<ReflectionQuestion> reflectionQuestions;
    // GUI Components

    // constructor for this class
    //----------------------------------------------------------
    public ReflectionQuestionCollection( )
    {
        super(myTableName);

    }

    //-----------------------------------------------------------
    private void populateCollectionHelper(String query)
    {

        Vector<Properties> allDataRetrieved =
                getSelectQueryResult(query);

        if (allDataRetrieved != null)
        {
            reflectionQuestions = new Vector<ReflectionQuestion>();

            for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
            {
                Properties nextReflectionQuestionData = allDataRetrieved.elementAt(cnt);

                ReflectionQuestion reflectionQuestion = new ReflectionQuestion(nextReflectionQuestionData);

                if (reflectionQuestion != null)
                {
                    addReflectionQuestion(reflectionQuestion);
                }
            }

        }
    }

    //-----------------------------------------------------------
    public void findByTextPart(String textPart)
    {
        String query = "SELECT * FROM " + myTableName + "";
        if ((textPart == null) || (textPart.length() == 0))
        {
            query += ""; // do nothing, query unchanged
        }
        else
        {
            query += " WHERE (QuestionText LIKE '%" + textPart +
                    "%') ";
        }
        populateCollectionHelper(query);
    }

    //-----------------------------------------------------------
    public void findAll()
    {
        String query = "SELECT * FROM " + myTableName + "";
        populateCollectionHelper(query);
    }

    //-----------------------------------------------------------
    public void findAllIncludingInactive()
    {
        String query = "SELECT * FROM " + myTableName + "";
        populateCollectionHelper(query);
    }



    //----------------------------------------------------------------------------------
    private void addReflectionQuestion(ReflectionQuestion i)
    {
        int index = findIndexToAdd(i);
        reflectionQuestions.insertElementAt(i,index); // To build up a collection sorted on some key
    }

    //----------------------------------------------------------------------------------
    private int findIndexToAdd(ReflectionQuestion i)
    {
        int low=0;
        int high = reflectionQuestions.size()-1;
        int middle;

        while (low <=high)
        {
            middle = (low+high)/2;

            ReflectionQuestion midSession = reflectionQuestions.elementAt(middle);

            int result = ReflectionQuestion.compare(i,midSession);

            if (result ==0)
            {
                return middle;
            }
            else if (result<0)
            {
                high=middle-1;
            }
            else
            {
                low=middle+1;
            }
        }
        return low;
    }


    /**
     *
     */
    //----------------------------------------------------------
    public Object getState(String key)
    {
        if (key.equals("ReflectionQuestions"))
            return reflectionQuestions;
        else
        if (key.equals("ReflectionQuestionList"))
            return this;
        return null;
    }

    //----------------------------------------------------------------
    public void stateChangeRequest(String key, Object value)
    {
        myRegistry.updateSubscribers(key, this);
    }

    //----------------------------------------------------------
    public ReflectionQuestion retrieve(int index)
    {
        ReflectionQuestion retValue = null;
        retValue = reflectionQuestions.elementAt(index);

        return retValue;
    }


    //----------------------------------------------------------
    public ReflectionQuestion retrieve(String reflectionQuestionText)
    {
        ReflectionQuestion retValue = null;
        for (int cnt = 0; cnt < reflectionQuestions.size(); cnt++)
        {
            ReflectionQuestion nextReflectionQuestion = reflectionQuestions.elementAt(cnt);
            String nextReflectionQuestionTxt = (String)nextReflectionQuestion.getState("QuestionText");
            if (nextReflectionQuestionTxt.equals(reflectionQuestionText) == true)
            {
                retValue = nextReflectionQuestion;
                return retValue; // we should say 'break;' here
            }
        }

        return retValue;
    }

    /** Called via the IView relationship */
    //----------------------------------------------------------
    public void updateState(String key, Object value)
    {
        stateChangeRequest(key, value);
    }

    //------------------------------------------------------
    protected void createAndShowView()
    {
        Scene localScene = myViews.get("ReflectionQuestionCollectionView");

        if (localScene == null)
        {
            // create our new view
            View newView = ViewFactory.createView("ReflectionQuestionCollectionView", this);
            localScene = new Scene(newView);
            myViews.put("ReflectionQuestionCollectionView", localScene);
        }
        // make the view visible by installing it into the frame
        swapToView(localScene);
    }

    //-----------------------------------------------------------------------------------
    protected void initializeSchema(String tableName)
    {
        if (mySchema == null)
        {
            mySchema = getSchemaInfo(tableName);
        }
    }
}
