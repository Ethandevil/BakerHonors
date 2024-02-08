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


/** The class containing the SLOCollection for the Gen Ed Assessment
 * Management application
 */
//==============================================================
public class SLOCollection  extends EntityBase implements IView
{
    private static final String myTableName = "SLO";

    private Vector<SLO> SLOs;
    // GUI Components

    // constructor for this class
    //----------------------------------------------------------
    public SLOCollection( )
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
            SLOs = new Vector<SLO>();

            for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
            {
                Properties nextSLOData = allDataRetrieved.elementAt(cnt);

                SLO mySLO = new SLO(nextSLOData);

                if (mySLO != null)
                {
                    addSLO(mySLO);
                }
            }

        }
    }

    //-----------------------------------------------------------
    public void findByTextAndNotesPart(String textPart, String notesPart)
    {
        //String query = "SELECT * FROM " + myTableName + " WHERE (Status = 'Active')"; todo
        String query = "SELECT * FROM " + myTableName;
        if ((textPart == null) || (textPart.length() == 0))
        {
            if ((notesPart == null) ||
                    (notesPart.length() == 0))
            {
                query += ""; // do nothing, query unchanged
            }
            else
            {
                query += " WHERE (Notes LIKE '%" +
                        notesPart + "%')";
            }

        }
        else
        {
            query += " WHERE ((SLOText LIKE '%" + textPart +
                    "%') ";
            if ((notesPart == null) ||
                    (notesPart.length() == 0))
            {
                query += ")";
            }
            else
            {
                query += " AND (Notes LIKE '%" +
                        notesPart + "%'))";
            }

        }
        populateCollectionHelper(query);
    }

    //-----------------------------------------------------------
    public void findAll()
    {
        String query = "SELECT * FROM " + myTableName + " WHERE (Status = 'Active')";
        populateCollectionHelper(query);
    }

    //-----------------------------------------------------------
    public void findAllIncludingInactive()
    {
        String query = "SELECT * FROM " + myTableName + "";
        populateCollectionHelper(query);
    }

    //-----------------------------------------------------------
    public void findByGenEdArea(String genEdAreaID){
        String query = "SELECT * FROM " + myTableName + " WHERE (GenEdID = " + genEdAreaID + ")";
        populateCollectionHelper(query);
    }

    //-----------------------------------------------------------
    public int getSize(){
        if(SLOs != null)
            return SLOs.size();
        else
            return 0;
    }


    //----------------------------------------------------------------------------------
    private void addSLO(SLO i)
    {
        int index = findIndexToAdd(i);
        SLOs.insertElementAt(i,index); // To build up a collection sorted on some key
    }

    //----------------------------------------------------------------------------------
    private int findIndexToAdd(SLO i)
    {
        int low=0;
        int high = SLOs.size()-1;
        int middle;

        while (low <=high)
        {
            middle = (low+high)/2;

            SLO midSession = SLOs.elementAt(middle);

            int result = SLO.compare(i,midSession);

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
        if (key.equals("SLOs"))
            return SLOs;
        else
        if (key.equals("SLOList"))
            return this;
        return null;
    }

    //----------------------------------------------------------------
    public void stateChangeRequest(String key, Object value)
    {
        myRegistry.updateSubscribers(key, this);
    }

    //----------------------------------------------------------
    public SLO retrieve(int index)
    {
        SLO retValue = null;
        retValue = SLOs.elementAt(index);

        return retValue;
    }


    //----------------------------------------------------------
    public SLO retrieve(String SLOText)
    {
        SLO retValue = null;
        for (int cnt = 0; cnt < SLOs.size(); cnt++)
        {
            SLO nextSLO = SLOs.elementAt(cnt);
            String nextSLOTxt = (String)nextSLO.getState("SLOText");
            if (nextSLOTxt.equals(SLOText) == true)
            {
                retValue = nextSLO;
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
        Scene localScene = myViews.get("SLOCollectionView");

        if (localScene == null)
        {
            // create our new view
            View newView = ViewFactory.createView("SLOCollectionView", this);
            localScene = new Scene(newView);
            myViews.put("SLOCollectionView", localScene);
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