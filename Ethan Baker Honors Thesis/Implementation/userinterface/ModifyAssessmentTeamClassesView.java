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

import impresario.IModel;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ModifyAssessmentTeamClassesView extends AddAssessmentTeamClassesView{

    public ModifyAssessmentTeamClassesView(IModel AssessmentTeamClasses) {
        super(AssessmentTeamClasses);
    }
    //---------------------------------------------------------
    protected String getPromptText() {
        return "Modify course code and/or course number for:";
    }
    //---------------------------------------------------------

    public void updateState(String key, Object value) {
        if (key.equals("TransactionError") == true) {
            // display the passed text
            String message = (String) value;
            if ((message.startsWith("Err")) || (message.startsWith("ERR"))) {
                displayErrorMessage(message);
            } else {
                displayMessage(message);
            }

        }
    }
    //-------------------------------------------------------------
    public void populateFields() {
        String genEd = (String)myModel.getState("GenEdAreaData");
        String semester = (String)myModel.getState("SemData");
        String courseCode =  (String)myModel.getState("courseCode");
        String courseNum = (String)myModel.getState("courseNum");

        if (genEd != null)
        {
            genEdArea.setText(genEd);
        }
        if (semester != null)
        {
            sem.setText(semester);
        } if (courseCode != null)
        {
            crsCode.setText(courseCode);
        }
        if (courseNum != null)
        {
            number.setText(courseNum);
        }
        submitButton.setText("Update"); //fix submitbutton
        ImageView icon = new ImageView(new Image("/images/savecolor.png"));
        icon.setFitHeight(15);
        icon.setFitWidth(15);
        submitButton.setGraphic(icon);
    }

}
