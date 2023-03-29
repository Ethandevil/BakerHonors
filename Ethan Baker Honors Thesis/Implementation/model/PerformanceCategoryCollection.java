// tabs=4
//************************************************************
//	COPYRIGHT 2021, Kyle D. Adams, Matthew E. Morgan and
//   Sandeep Mitra, State University of New York. - Brockport
//   (SUNY Brockport)
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


/** The class containing the Performance Category Collection for the ISLO Data
 * Management application
 */
//==============================================================
public class PerformanceCategoryCollection  extends EntityBase implements IView
{
    private static final String myTableName = "CategoryNames";

    private Vector<PerformanceCategory> categories;
    // GUI Components

    // constructor for this class
    //----------------------------------------------------------
    public PerformanceCategoryCollection( )
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
            categories = new Vector<PerformanceCategory>();

            for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
            {
                Properties nextCatData = allDataRetrieved.elementAt(cnt);

                PerformanceCategory cat = new PerformanceCategory(nextCatData);

                if (cat != null)
                {
                    addCategory(cat);

                }
            }

        }
    }

    //-----------------------------------------------------------
    public void findByCatNameAndNumber(String catName,
                                     String number)
    {
        String query = "SELECT * FROM " + myTableName + "";
        if ((catName == null) || (catName.length() == 0))
        {
            if ((number == null) ||
                    (number.length() == 0))
            {
                query += ""; // do nothing, query unchanged
            }
            else
            {
                query += " WHERE (Number = " +
                        number + ")";
            }

        }
        else
        {
            query += " WHERE ((Name = '" + catName +
                    "') ";
            if ((number == null) ||
                    (number.length() == 0))
            {
                query += ")";
            }
            else
            {
                query += " AND (Number = " +
                        number + "))";
            }

        }
        populateCollectionHelper(query);
    }

    //-----------------------------------------------------------
    public void findAll()
    {
        String query = "SELECT * FROM " + myTableName + "";
        populateCollectionHelper(query);
    }


    //----------------------------------------------------------------------------------
    private void addCategory(PerformanceCategory pc)
    {
        int index = findIndexToAdd(pc);
        categories.insertElementAt(pc,index); // To build up a collection sorted on some key
    }

    //----------------------------------------------------------------------------------
    private int findIndexToAdd(PerformanceCategory pc)
    {
        int low=0;
        int high = categories.size()-1;
        int middle;

        while (low <=high)
        {
            middle = (low+high)/2;

            PerformanceCategory midSession = categories.elementAt(middle);

            int result = PerformanceCategory.compare(pc,midSession);

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
        if (key.equals("Categories"))
            return categories;
        else
        if (key.equals("PerformanceCategoryList"))
            return this;
        return null;
    }

    //----------------------------------------------------------------
    public void stateChangeRequest(String key, Object value)
    {
        myRegistry.updateSubscribers(key, this);
    }

    //----------------------------------------------------------
    public PerformanceCategory retrieve(int catID)
    {
        PerformanceCategory retValue = null;
        for (int cnt = 0; cnt < categories.size(); cnt++)
        {
            PerformanceCategory nextCat = categories.elementAt(cnt);
            String nextCatID = (String)nextCat.getState("ID");
            if (nextCatID.equals(""+catID) == true)
            {
                retValue = nextCat;
                return retValue; // we should say 'break;' here
            }
        }

        return retValue;
    }


    //----------------------------------------------------------
    public PerformanceCategory retrieve(String catName, String catNum)
    {
        PerformanceCategory retValue = null;

        if ((catNum == null) || (catNum.equals("")) ) {
            for (int cnt = 0; cnt < categories.size(); cnt++) {
                PerformanceCategory nextCat = categories.elementAt(cnt);
                String nextCatNam = (String) nextCat.getState("Name");
                if ((nextCatNam.equals(catName) == true)) {
                    retValue = nextCat;
                    return retValue; // we should say 'break;' here
                }
            }
        }
        for (int cnt = 0; cnt < categories.size(); cnt++)
        {
            PerformanceCategory nextCat = categories.elementAt(cnt);
            String nextCatNum = (String)nextCat.getState("Number");
            String nextCatName = (String)nextCat.getState("Name");
            if ((nextCatName.equals(catName) == true) &&
                    (nextCatNum.equals(catNum) == true))
            {
                retValue = nextCat;
                return retValue; // we should say 'break;' here
            }
        }

        return retValue;
    }

    //----------------------------------------------------------
    public PerformanceCategory retrieve(String catNum){
        PerformanceCategory retValue = null;
        for (int cnt = 0; cnt < categories.size(); cnt++)
        {
            PerformanceCategory nextCat = categories.elementAt(cnt);
            String nextCatNum = (String)nextCat.getState("Number");
            if (nextCatNum.equals(catNum) == true)
            {
                retValue = nextCat;
                return retValue; // we should say 'break;' here
            }
        }
        return null;
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
        Scene localScene = myViews.get("PerformanceCategoryCollectionView");

        if (localScene == null)
        {
            // create our new view
            View newView = ViewFactory.createView("PerformanceCategoryCollectionView", this);
            localScene = new Scene(newView);
            myViews.put("PerformanceCategoryCollectionView", localScene);
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
