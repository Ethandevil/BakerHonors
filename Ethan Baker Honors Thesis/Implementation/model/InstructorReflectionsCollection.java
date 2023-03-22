package model;

import java.util.Properties;
import java.util.Vector;
// project imports

import impresario.IView;

public class InstructorReflectionsCollection extends EntityBase implements IView {
    private static final String myTableName = "InstructorReflections";

    private Vector<InstructorReflections> instructorReflections;
    // GUI Components

    // constructor for this class
    //----------------------------------------------------------
    public InstructorReflectionsCollection( )
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
            instructorReflections = new Vector<InstructorReflections>();

            for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
            {
                Properties nextIRData = allDataRetrieved.elementAt(cnt);

                InstructorReflections studCat = new InstructorReflections(nextIRData);

                if (studCat != null)
                {
                    addInstructorReflection(studCat);
                }
            }

        }
    }

    //-----------------------------------------------------------
    public void findByAssessmentTeamIDAndReflectionQuestionID(String aTID, String rqID)
    {
        String query = "SELECT * FROM " + myTableName + " WHERE ((AssessmentTeamID = " + aTID + ")" +
                " AND (ReflectionQuestionID = " + rqID + "))";

        populateCollectionHelper(query);
    }

    //-----------------------------------------------------------
    public void findByAssessmentTeamID(String aTID){
        String query = "SELECT * FROM " + myTableName + " WHERE ((AssessmentTeamID = " + aTID + "))";
        populateCollectionHelper(query);
    }

    //-----------------------------------------------------------
    public void findAll()
    {
        String query = "SELECT * FROM " + myTableName + "";
        populateCollectionHelper(query);
    }


    //----------------------------------------------------------------------------------
    private void addInstructorReflection(InstructorReflections ir)
    {
        int index = findIndexToAdd(ir);
        instructorReflections.insertElementAt(ir,index); // To build up a collection sorted on some key
    }

    //----------------------------------------------------------------------------------
    private int findIndexToAdd(InstructorReflections ir)
    {
        int low=0;
        int high = instructorReflections.size()-1;
        int middle;

        while (low <=high)
        {
            middle = (low+high)/2;

            InstructorReflections midSession = instructorReflections.elementAt(middle);

            int result = InstructorReflections.compare(ir,midSession);

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
        if (key.equals("InstructorReflections"))
            return instructorReflections;
        else
        if (key.equals("InstructorReflectionsList"))
            return this;
        return null;
    }

    //----------------------------------------------------------------
    public void stateChangeRequest(String key, Object value)
    {
        myRegistry.updateSubscribers(key, this);
    }

    //----------------------------------------------------------
    public InstructorReflections retrieve(String ID)
    {
        InstructorReflections retValue = null;
        for (int cnt = 0; cnt < instructorReflections.size(); cnt++)
        {
            InstructorReflections nextIR = instructorReflections.elementAt(cnt);
            String nextIRID = (String)nextIR.getState("ID");
            if (nextIRID.equals(""+ID) == true)
            {
                retValue = nextIR;
                return retValue; // we should say 'break;' here
            }
        }

        return retValue;
    }

    //----------------------------------------------------------
    public InstructorReflections retrieve(String aTID, String rqID)
    {
        InstructorReflections retValue = null;
        for (int cnt = 0; cnt < instructorReflections.size(); cnt++)
        {
            InstructorReflections nextIR = instructorReflections.elementAt(cnt);
            String nextIRATID = (String)nextIR.getState("AssessmentTeamID");
            String nextIRRQID = (String)nextIR.getState("ReflectionQuestionID");
            if ((nextIRATID.equals(aTID)) && (nextIRRQID.equals(rqID)))
            {
                retValue = nextIR;
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
