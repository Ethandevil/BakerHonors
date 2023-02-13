package model;

import java.util.Properties;
import java.util.Vector;
// project imports

import impresario.IView;

public class StudentCategorizationCollection extends EntityBase implements IView {
    private static final String myTableName = "StudentCategorization";

    private Vector<StudentCategorization> studentCategorizations;
    // GUI Components

    // constructor for this class
    //----------------------------------------------------------
    public StudentCategorizationCollection( )
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
            studentCategorizations = new Vector<StudentCategorization>();

            for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
            {
                Properties nextStudentCategorizationData = allDataRetrieved.elementAt(cnt);

                StudentCategorization studCat = new StudentCategorization(nextStudentCategorizationData);

                if (studCat != null)
                {
                    addStudentCategorization(studCat);
                }
            }

        }
    }

    //-----------------------------------------------------------
    public void findByAssessmentTeamIDAndSLOID(String aTID, String sloID)
    {
        String query = "SELECT * FROM " + myTableName + " WHERE ((AssessmentTeamID = '" + aTID + "')" +
                " AND (SLOID = '" + sloID + "))";

        populateCollectionHelper(query);
    }

    //-----------------------------------------------------------
    public void findAll()
    {
        String query = "SELECT * FROM " + myTableName + "";
        populateCollectionHelper(query);
    }


    //----------------------------------------------------------------------------------
    private void addStudentCategorization(StudentCategorization sc)
    {
        int index = findIndexToAdd(sc);
        studentCategorizations.insertElementAt(sc,index); // To build up a collection sorted on some key
    }

    //----------------------------------------------------------------------------------
    private int findIndexToAdd(StudentCategorization sc)
    {
        int low=0;
        int high = studentCategorizations.size()-1;
        int middle;

        while (low <=high)
        {
            middle = (low+high)/2;

            StudentCategorization midSession = studentCategorizations.elementAt(middle);

            int result = StudentCategorization.compare(sc,midSession);

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
            return studentCategorizations;
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

    //----------------------------------------------------------
    public StudentCategorization retrieve(String ID)
    {
        StudentCategorization retValue = null;
        for (int cnt = 0; cnt < studentCategorizations.size(); cnt++)
        {
            StudentCategorization nextStudentCategorization = studentCategorizations.elementAt(cnt);
            String nextStudentCategorizationId = (String)nextStudentCategorization.getState("ID");
            if (nextStudentCategorizationId.equals(""+ID) == true)
            {
                retValue = nextStudentCategorization;
                return retValue; // we should say 'break;' here
            }
        }

        return retValue;
    }

    //----------------------------------------------------------
    public StudentCategorization retrieve(String aTID, String sloID, String sLevel)
    {
        StudentCategorization retValue = null;
        for (int cnt = 0; cnt < studentCategorizations.size(); cnt++)
        {
            StudentCategorization nextStudentCategorization = studentCategorizations.elementAt(cnt);
            String nextStudentCategorizationATId = (String)nextStudentCategorization.getState("AssessmentTeamID");
            String nextStudentCategorizationSloId = (String)nextStudentCategorization.getState("SLOID");
            String nextStudentCategorizationSLevel = (String)nextStudentCategorization.getState("StudentLevel");
            if ((nextStudentCategorizationATId.equals(aTID)) && (nextStudentCategorizationSloId.equals(sloID)) &&
                    (nextStudentCategorizationSLevel.equals(sLevel)))
            {
                retValue = nextStudentCategorization;
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
