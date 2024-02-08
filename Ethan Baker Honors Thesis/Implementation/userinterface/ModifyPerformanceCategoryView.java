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

public class ModifyPerformanceCategoryView extends AddPerformanceCategoryView{


    public ModifyPerformanceCategoryView(IModel model) {
        super(model);
        myModel.subscribe("TransactionError", this);
    }
    //---------------------------------------------------------
    protected String getPromptText() {
        return "Modify the Performance Category Information";
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

    /**
     * Display error message
     */
    //----------------------------------------------------------
    public void displayErrorMessage(String message)
    {
        statusLog.displayErrorMessage(message);
    }


    /**
     * Display info message
     */
    //----------------------------------------------------------
    public void displayMessage(String message)
    {
        statusLog.displayMessage(message);
    }

    public void populateFields()
    {
        String catNumberVal = (String)myModel.getState("Number");
        if (catNumberVal != null)
        {
            number.setText(catNumberVal);
        }
        String catNameVal = (String)myModel.getState("Name");
        if (catNameVal != null)
        {
            name.setText(catNameVal);
        }

        submitButton.setText("Update"); //fix submitbutton
        ImageView icon = new ImageView(new Image("/images/savecolor.png"));
        icon.setFitHeight(15);
        icon.setFitWidth(15);
        submitButton.setGraphic(icon);
    }
}
