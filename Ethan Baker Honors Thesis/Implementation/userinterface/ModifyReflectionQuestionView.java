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

// system imports
import javafx.scene.paint.Color;

// project imports
import impresario.IModel;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/** The class containing the Modify Reflection Question View  for the Gen Ed Assessment Data
 *  Management Application
 */
//==============================================================
public class ModifyReflectionQuestionView extends AddReflectionQuestionView {

    // constructor for this class -- takes a model object
    //----------------------------------------------------------
    public ModifyReflectionQuestionView(IModel reflectionQuestion)
    {
        super(reflectionQuestion);
    }

    //-------------------------------------------------------------
    protected String getActionText()
    {
        return "Update the Reflection Question information below:";
    }

    //-------------------------------------------------------------
    public void populateFields()
    {

        String reflectionQuestionTextVal = (String)myModel.getState("QuestionText");
        if (reflectionQuestionTextVal != null)
        {
            questionText.setText(reflectionQuestionTextVal);
        }

        submitButton.setText("Update"); //fix submitbutton
        ImageView icon = new ImageView(new Image("/images/savecolor.png"));
        icon.setFitHeight(15);
        icon.setFitWidth(15);
        submitButton.setGraphic(icon);
    }

    public void clearValues(){

    }
}
