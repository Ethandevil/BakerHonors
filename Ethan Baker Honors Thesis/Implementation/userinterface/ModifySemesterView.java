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

// system imports
import javafx.scene.paint.Color;

// project imports
import impresario.IModel;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/** The class containing the Modify Semester View  for the Gen Ed Assessment Data
 *  Management Application
 */
//==============================================================
public class ModifySemesterView extends AddSemesterView
{

	//

	// constructor for this class -- takes a model object
	//----------------------------------------------------------
	public ModifySemesterView(IModel sem)
	{
		super(sem);
	}

	//-------------------------------------------------------------
	protected String getActionText()
	{
		return "Update the Semester information below:";
	}

	//-------------------------------------------------------------
	public void populateFields()
	{
		String semNameVal = (String)myModel.getState("SemName");
		if (semNameVal != null)
		{
			semester.getSelectionModel().select(semNameVal);
		}
		String yr = (String)myModel.getState("Year");
		if (yr != null)
		{
			year.setText(yr);
		}
		
		submit.setText("Update"); //fix submitbutton
		ImageView icon = new ImageView(new Image("/images/savecolor.png"));
		icon.setFitHeight(15);
		icon.setFitWidth(15);
		submit.setGraphic(icon);
	}

	public void clearValues(){

	}
}

//---------------------------------------------------------------
//	Revision History:
//


