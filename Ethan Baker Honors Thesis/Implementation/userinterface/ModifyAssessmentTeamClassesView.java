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

public class ModifyOfferingTeacherView extends AddOfferingTeacherView{

    public ModifyOfferingTeacherView(IModel OfferingTeacher) {
        super(OfferingTeacher);
    }
    //---------------------------------------------------------
    protected String getPromptText() {
        return "Modify course number and/or teacher name for:";
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
        }
        if (courseNum != null)
        {
            number.setText(courseNum);
        }
        if (teacher != null)
        {
            name.setText(teacher);
        }
        submitButton.setText("Update"); //fix submitbutton
        ImageView icon = new ImageView(new Image("/images/savecolor.png"));
        icon.setFitHeight(15);
        icon.setFitWidth(15);
        submitButton.setGraphic(icon);

    }

}
