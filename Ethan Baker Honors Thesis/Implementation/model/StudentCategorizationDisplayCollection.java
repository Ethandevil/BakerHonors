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
import java.util.Properties;
import java.util.Vector;
import javafx.scene.Scene;

// project imports

import impresario.IView;

import userinterface.View;
import userinterface.ViewFactory;


/** The class containing the Student Categorization Collection for the Gen Ed Data
 * Management application
 */
//==============================================================
public class StudentCategorizationDisplayCollection  extends EntityBase implements IView
{
    private static final String myTableName = "StudentCategorization";
    private static final String myTableName2 = "StudentCategorization";

    private Vector<StudentCategorizationDisplay> studentcats;
    // GUI Components

    // constructor for this class
    //----------------------------------------------------------
    public StudentCategorizationDisplayCollection( )
    {
        super(myTableName2);

    }

    //-----------------------------------------------------------
    private void populateCollectionHelper(String query)
    {

        Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

        if (allDataRetrieved != null)
        {
            studentcats = new Vector<StudentCategorizationDisplay>();

            for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
            {
                Properties nextScData = allDataRetrieved.elementAt(cnt);

                StudentCategorizationDisplay sc = new StudentCategorizationDisplay(nextScData);

                if (sc != null)
                {
                    addStudentCategorizationDisplay(sc);
                }
            }

        }
    }

    //-----------------------------------------------------------
    public void findByOffering(String offID)
    {
        String query = "SELECT OT.CourseDisciplineCode, OT.CourseNumber, OT.TeacherName, " +
                "SC.Cat1Number, SC.Cat2Number, SC.Cat3Number, SC.Cat4Number, SC.Reflections, SC.Flag " +
                "FROM " + myTableName + " OT, " + myTableName2 + " SC WHERE ((OT.ID = SC.OfferingTeachersID) AND " +
                "(OT.OfferingID = " + offID + "))";

        populateCollectionHelper(query);
    }

    //-----------------------------------------------------------
    public void findByStudentLevel(String studentLevel, String atID){
        String query = "SELECT SLOID, Cat1Number, Cat2Number, Cat3Number, Cat4Number FROM " + myTableName +
                " WHERE ((AssessmentTeamID = " + atID + ") AND (StudentLevel = '" + studentLevel +
                "'))";
    }

    //-----------------------------------------------------------
    public void findAll()
    {
        String query = "SELECT * FROM " + myTableName + "";
        populateCollectionHelper(query);
    }

    //----------------------------------------------------------------------------------
    private void addStudentCategorizationDisplay(StudentCategorizationDisplay s)
    {
        int index = findIndexToAdd(s);
        studentcats.insertElementAt(s,index); // To build up a collection sorted on some key
    }


    //----------------------------------------------------------------------------------
    private int findIndexToAdd(StudentCategorizationDisplay s)
    {
        int low=0;
        int high = studentcats.size()-1;
        int middle;

        while (low <=high)
        {
            middle = (low+high)/2;

            StudentCategorizationDisplay midSession = studentcats.elementAt(middle);

            int result = StudentCategorizationDisplay.compare(s,midSession);

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
        if (key.equals("StudentCategorizations"))
            return studentcats;
        else
        if (key.equals("StudentCategorizationList"))
            return this;
        return null;
    }

    //----------------------------------------------------------------
    public void stateChangeRequest(String key, Object value)
    {
        myRegistry.updateSubscribers(key, this);
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
