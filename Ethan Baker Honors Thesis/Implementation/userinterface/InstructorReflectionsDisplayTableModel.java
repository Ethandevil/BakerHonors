package userinterface;

import javafx.beans.property.SimpleStringProperty;
import java.util.Vector;

public class InstructorReflectionsDisplayTableModel {
    private final SimpleStringProperty questionText;
    private final SimpleStringProperty reflectionText;


    //----------------------------------------------------------------------------
    public InstructorReflectionsDisplayTableModel(Vector<String> irData)
    {
        questionText =  new SimpleStringProperty(irData.elementAt(0));
        reflectionText =  new SimpleStringProperty(irData.elementAt(1));
    }

    //----------------------------------------------------------------------------
    public String getQuestionText() {
        return questionText.get();
    }

    //----------------------------------------------------------------------------
    public void setQuestionText(String id) {
        questionText.set(id);
    }

    //----------------------------------------------------------------------------
    public String getReflectionText() {
        return reflectionText.get();
    }

    //----------------------------------------------------------------------------
    public void setReflectionText(String nm) {
        reflectionText.set(nm);
    }
}
