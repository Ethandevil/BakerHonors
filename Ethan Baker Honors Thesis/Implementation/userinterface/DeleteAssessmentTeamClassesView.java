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

public class DeleteOfferingTeacherView extends ModifyOfferingTeacherView{

    public DeleteOfferingTeacherView(IModel OfferingTeacher) {
        super(OfferingTeacher);
        // DEBUG System.out.println("Delete offering teacher view constructed");
    }
    protected String getPromptText() {
        return "Are you sure you would like to remove this sampled course for:";
    }
    //-------------------------------------------------------------
    public void populateFields() {
        String islo = (String)myModel.getState("ISLOData");
        String semester = (String)myModel.getState("SemData");
        String teacher = (String)myModel.getState("teacherName");
        String courseCode =  (String)myModel.getState("courseCode");
        String courseNum = (String)myModel.getState("courseNum");

        /* System.out.println("ISLO: " + islo + "; Sem: " + semester + "; teacher: " + teacher +
                "; crs code: " + courseCode + "; crs num: " + courseNum); */
        if (islo != null)
        {
            ISLO.setText(islo);

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
        if (teacher != null)
        {
            name.setText(teacher);
            name.setEditable(false);
        }
        // DEBUG System.out.println("Matt was here");
        submitButton.setText("Confirm Delete"); //fix submitbutton
        ImageView icon = new ImageView(new Image("/images/remove_icon.png"));
        icon.setFitHeight(15);
        icon.setFitWidth(15);
        submitButton.setGraphic(icon);
        // DEBUG System.out.println("Kyle was here");

    }

}
