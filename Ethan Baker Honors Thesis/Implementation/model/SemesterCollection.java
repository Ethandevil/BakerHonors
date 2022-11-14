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


/** The class containing the Semester Collection for the Gen Ed Assessment Data
 * Management application
 */
//==============================================================
public class SemesterCollection  extends EntityBase implements IView
{
    private static final String myTableName = "Semester";

    private Vector<Semester> semesters;
    // GUI Components

    // constructor for this class
    //----------------------------------------------------------
    public SemesterCollection( )
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
            semesters = new Vector<Semester>();

            for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
            {
                Properties nextSemData = allDataRetrieved.elementAt(cnt);

                Semester sem = new Semester(nextSemData);

                if (sem != null)
                {
                    addSemester(sem);
                }
            }

        }
    }

    //-----------------------------------------------------------
    public void findBySemNameAndYear(String semName,
                                     String yearVal)
    {
        String query = "SELECT * FROM " + myTableName + "";
        if ((semName == null) || (semName.length() == 0))
        {
            if ((yearVal == null) ||
                    (yearVal.length() == 0))
            {
                query += ""; // do nothing, query unchanged
            }
            else
            {
                query += " WHERE (Year = " +
                        yearVal + ")";
            }

        }
        else
        {
            query += " WHERE ((SemName = '" + semName +
                    "') ";
            if ((yearVal == null) ||
                    (yearVal.length() == 0))
            {
                query += ")";
            }
            else
            {
                query += " AND (Year = " +
                        yearVal + "))";
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
    private void addSemester(Semester s)
    {
        int index = findIndexToAdd(s);
        semesters.insertElementAt(s,index); // To build up a collection sorted on some key
    }

    //----------------------------------------------------------------------------------
    private int findIndexToAdd(Semester s)
    {
        int low=0;
        int high = semesters.size()-1;
        int middle;

        while (low <=high)
        {
            middle = (low+high)/2;

            Semester midSession = semesters.elementAt(middle);

            int result = Semester.compare(s,midSession);

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
        if (key.equals("Semesters"))
            return semesters;
        else
        if (key.equals("SemesterList"))
            return this;
        return null;
    }

    //----------------------------------------------------------------
    public void stateChangeRequest(String key, Object value)
    {
        myRegistry.updateSubscribers(key, this);
    }

    //----------------------------------------------------------
    public Semester retrieve(int semID)
    {
        Semester retValue = null;
        for (int cnt = 0; cnt < semesters.size(); cnt++)
        {
            Semester nextSem = semesters.elementAt(cnt);
            String nextSemID = (String)nextSem.getState("ID");
            if (nextSemID.equals(""+semID) == true)
            {
                retValue = nextSem;
                return retValue; // we should say 'break;' here
            }
        }

        return retValue;
    }


    //----------------------------------------------------------
    public Semester retrieve(String semName, String yearV)
    {
        Semester retValue = null;

        if ((yearV == null) || (yearV.equals("")) ) {
            for (int cnt = 0; cnt < semesters.size(); cnt++) {
                Semester nextSem = semesters.elementAt(cnt);
                String nextSemNam = (String) nextSem.getState("SemName");
                if ((nextSemNam.equals(semName) == true)) {
                    retValue = nextSem;
                    return retValue; // we should say 'break;' here
                }
            }
        }
        for (int cnt = 0; cnt < semesters.size(); cnt++)
        {
            Semester nextSem = semesters.elementAt(cnt);
            String nextSemNam = (String)nextSem.getState("SemName");
            String nextSemYr = (String)nextSem.getState("Year");
            if ((nextSemNam.equals(semName) == true) &&
                    (nextSemYr.equals(yearV) == true))
            {
                retValue = nextSem;
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


    //-----------------------------------------------------------------------------------
    protected void initializeSchema(String tableName)
    {
        if (mySchema == null)
        {
            mySchema = getSchemaInfo(tableName);
        }
    }
}
