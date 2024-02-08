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
