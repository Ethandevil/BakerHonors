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

    private StudentCategorizationDisplay cat1StudentCats;
    private StudentCategorizationDisplay cat2StudentCats;
    private StudentCategorizationDisplay cat3StudentCats;
    private StudentCategorizationDisplay cat4StudentCats;
    // GUI Components

    // constructor for this class
    //----------------------------------------------------------
    public StudentCategorizationDisplayCollection( )
    {
        super(myTableName);

    }

    //-----------------------------------------------------------
    private void populateCollectionHelper(String query)
    {

        Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

        if (allDataRetrieved != null)
        {
            //do something else to get us cat1StudentCats - cat4StudentCats
            /*studentcats = new Vector<StudentCategorizationDisplay>();

            for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
            {
                Properties nextScData = allDataRetrieved.elementAt(cnt);

                StudentCategorizationDisplay sc = new StudentCategorizationDisplay(nextScData);

                if (sc != null)
                {
                    addStudentCategorizationDisplay(sc);
                }
            }
            */
        }
    }

    //-----------------------------------------------------------
    public void findByStudentLevel(String studentLevel, String atID){
        String query = "SELECT SLOID, Cat1Number, Cat2Number, Cat3Number, Cat4Number FROM " + myTableName +
                " WHERE ((AssessmentTeamID = " + atID + ") AND (StudentLevel = '" + studentLevel +
                "')) ORDER BY SLOID";
        populateCollectionHelper(query);
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
