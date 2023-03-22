package model;

// system imports
import java.util.Vector;

// project imports
import event.Event;

/** The class containing the Instructor Reflections Display Collection for the Gen Ed Assessment Data Management
 * application
 */
//==============================================================
public class InstructorReflectionsDisplayCollection {
    //
    private Vector<InstructorReflectionsDisplay> irdisps;

    // constructor for this class
    //----------------------------------------------------------
    public InstructorReflectionsDisplayCollection() {

        irdisps = new Vector<InstructorReflectionsDisplay>();
    }

    //---------------------------------------------------------
    public InstructorReflectionsDisplayCollection(InstructorReflectionsCollection irc)
    {
        irdisps = new Vector<InstructorReflectionsDisplay>();
        Vector<InstructorReflections> currInstructorReflections = (Vector)irc.getState("InstructorReflections");
        for (int cnt = 0; cnt < currInstructorReflections.size(); cnt++)
        {
            InstructorReflections nextInstructorReflection = currInstructorReflections.get(cnt);
            String reflectionText = (String)nextInstructorReflection.getState("ReflectionText");
            String rqId = (String)nextInstructorReflection.getState("ReflectionQuestionID");
            try {
                ReflectionQuestion rq = new ReflectionQuestion(rqId);
                String questionText = rq.getText();
                InstructorReflectionsDisplay ird = new InstructorReflectionsDisplay(questionText, reflectionText);
                addInstructorReflectionsDisplay(ird);
            }
            catch (Exception ex) {
                new Event(Event.getLeafLevelClassName(this), "<init>",
                        "Unexplained error: Reflection Text or Reflection Question IDs not found in database!", Event.ERROR);
            }
        }
    }

    //--------------------------------------------------------
    public void addInstructorReflectionsDisplay(InstructorReflectionsDisplay ird)
    {
        int index = findIndexToAdd(ird);
        irdisps.insertElementAt(ird,index); // To build up a collection sorted on some key
    }

    //----------------------------------------------------------------------------------
    private int findIndexToAdd(InstructorReflectionsDisplay ird)
    {
        int low=0;
        int high = irdisps.size()-1;
        int middle;

        while (low <=high)
        {
            middle = (low+high)/2;

            InstructorReflectionsDisplay midSession = irdisps.elementAt(middle);

            int result = InstructorReflectionsDisplay.compare(ird,midSession);
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


    //-----------------------------------------------------------
    public InstructorReflectionsDisplay retrieve(int index) {

        if ((index >= 0) && (index < irdisps.size())) {
            return irdisps.elementAt(index);
        }
        else {
            return null;
        }
    }

    //-----------------------------------------------------------
    public int getSize() {
        if (irdisps != null) return irdisps.size();
        else
            return 0;
    }

    //---------------------------------------------------------
    public Vector getInstructorReflectionDisplays() {
        return irdisps;
    }
}
