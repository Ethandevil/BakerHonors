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

package userinterface;

import java.util.Vector;

import javafx.beans.property.SimpleStringProperty;

//==============================================================================
public class ReflectionQuestionTableModel {
    private final SimpleStringProperty reflectionQuestionID;
    private final SimpleStringProperty reflectionQuestionText;

    //----------------------------------------------------------------------------
    public ReflectionQuestionTableModel(Vector<String> reflectionQuestionData)
    {
        reflectionQuestionID =  new SimpleStringProperty(reflectionQuestionData.elementAt(0));
        reflectionQuestionText =  new SimpleStringProperty(reflectionQuestionData.elementAt(1));

    }

    //----------------------------------------------------------------------------
    public String getReflectionQuestionID() {
        return reflectionQuestionID.get();
    }

    //----------------------------------------------------------------------------
    public void setReflectionQuestionID(String num) {
        reflectionQuestionID.set(num);
    }

    //----------------------------------------------------------------------------
    public String getReflectionQuestionText() {
        return reflectionQuestionText.get();
    }

    //----------------------------------------------------------------------------
    public void setReflectionQuestionText(String nm) {
        reflectionQuestionText.set(nm);
    }
}
