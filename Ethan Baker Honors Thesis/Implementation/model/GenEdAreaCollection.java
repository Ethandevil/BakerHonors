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


/** The class containing the ISLOCollection for the ISLO Data
 * Management application
 */
//==============================================================
public class GenEdAreaCollection  extends EntityBase implements IView
{
    private static final String myTableName = "GenEdArea";

    private Vector<GenEdArea> genEdAreas;
    // GUI Components

    // constructor for this class
    //----------------------------------------------------------
    public GenEdAreaCollection( )
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
            genEdAreas = new Vector<GenEdArea>();

            for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
            {
                Properties nextGenEdAreaData = allDataRetrieved.elementAt(cnt);

                GenEdArea genEdArea = new GenEdArea(nextGenEdAreaData);

                if (genEdArea != null)
                {
                    addGenEdArea(genEdArea);
                }
            }

        }
    }

    //-----------------------------------------------------------
    public void findByNameAndNotesPart(String namePart, String notesPart)
    {
        String query = "SELECT * FROM " + myTableName + " WHERE (Status = 'Active')";
        if ((namePart == null) || (namePart.length() == 0))
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
            query += " WHERE ((AreaName LIKE '%" + namePart +
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



    //----------------------------------------------------------------------------------
    private void addGenEdArea(GenEdArea i)
    {
        int index = findIndexToAdd(i);
        genEdAreas.insertElementAt(i,index); // To build up a collection sorted on some key
    }

    //----------------------------------------------------------------------------------
    private int findIndexToAdd(GenEdArea i)
    {
        int low=0;
        int high = genEdAreas.size()-1;
        int middle;

        while (low <=high)
        {
            middle = (low+high)/2;

            GenEdArea midSession = genEdAreas.elementAt(middle);

            int result = GenEdArea.compare(i,midSession);

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
        if (key.equals("GenEdAreas"))
            return genEdAreas;
        else
        if (key.equals("GenEdAreaList"))
            return this;
        return null;
    }

    //----------------------------------------------------------------
    public void stateChangeRequest(String key, Object value)
    {
        myRegistry.updateSubscribers(key, this);
    }

    //----------------------------------------------------------
    public GenEdArea retrieve(int index)
    {
        GenEdArea retValue = null;
        retValue = genEdAreas.elementAt(index);

        return retValue;
    }


    //----------------------------------------------------------
    public GenEdArea retrieve(String genEdAreaName)
    {
        GenEdArea retValue = null;
        for (int cnt = 0; cnt < genEdAreas.size(); cnt++)
        {
            GenEdArea nextGenEdArea = genEdAreas.elementAt(cnt);
            String nextGenEdAreaNm = (String)nextGenEdArea.getState("AreaName");
            if (nextGenEdAreaNm.equals(genEdAreaName) == true)
            {
                retValue = nextGenEdArea;
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
        Scene localScene = myViews.get("GenEdAreaCollectionView");

        if (localScene == null)
        {
            // create our new view
            View newView = ViewFactory.createView("GenEdAreaCollectionView", this);
            localScene = new Scene(newView);
            myViews.put("GenEdAreaCollectionView", localScene);
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

