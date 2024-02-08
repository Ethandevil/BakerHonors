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

import impresario.IModel;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class DeleteAssessmentTeamClassesView extends ModifyAssessmentTeamClassesView{

    public DeleteAssessmentTeamClassesView(IModel AssessmentTeamClasses) {
        super(AssessmentTeamClasses);
    }
    protected String getPromptText() {
        return "Are you sure you would like to remove this assessment team class for:";
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
            crsCode.setEditable(false);
        }
        if (courseNum != null)
        {
            number.setText(courseNum);
            number.setEditable(false);
        }
        submitButton.setText("Confirm Delete"); //fix submitbutton
        ImageView icon = new ImageView(new Image("/images/remove_icon.png"));
        icon.setFitHeight(15);
        icon.setFitWidth(15);
        submitButton.setGraphic(icon);
    }
}
