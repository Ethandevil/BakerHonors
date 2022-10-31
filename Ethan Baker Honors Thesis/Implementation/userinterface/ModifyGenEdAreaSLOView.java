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

/** The class containing the Modify ISLOView  for the ISLO Data
 *  Management Application
 */
//==============================================================
public class ModifyGenEdAreaSLOView extends AddGenEdAreaView
{

    //

    // constructor for this class -- takes a model object
    //----------------------------------------------------------
    public ModifyGenEdAreaSLOView(IModel genEdArea)
    {
        super(genEdArea);
    }

    //-------------------------------------------------------------
    protected String getActionText()
    {
        return "Update the Gen Ed Area SLO information below:";
    }

    //-------------------------------------------------------------
    public void populateFields()
    {

        String genEdAreaNameVal = (String)myModel.getState("SLOText");
        if (genEdAreaNameVal != null)
        {
            genEdAreaName.setText(genEdAreaNameVal);
        }
        String desc = (String)myModel.getState("Notes");
        if (desc != null)
        {
            notes.setText(desc);
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

//---------------------------------------------------------------
//	Revision History:
//


