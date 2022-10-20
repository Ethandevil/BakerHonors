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
import javafx.stage.Stage;

// project imports

/** 
 * The main stage for the Brockport GUI applications. 
 * All scenes are inside this one stage only. 
 */
//==============================================================
public class MainStageContainer
{
	// data members

	private static Stage myInstance = null;

	// class constructor
	//----------------------------------------------------------
	private MainStageContainer ()
	{
	}

	//----------------------------------------------------------
	public static Stage getInstance()
	{
		return myInstance; 
	}

	//-----------------------------------------------------------
	public static void setStage(Stage st, String title)
	{
		myInstance = st;
		myInstance.setTitle(title);
		myInstance.setResizable(false);
	}

}




