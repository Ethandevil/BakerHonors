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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.text.TextAlignment;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;


import javafx.scene.effect.DropShadow;

// project imports

//==============================================================
public class CopyrightPanel extends Text
{

	private DropShadow shadow = new DropShadow();

	// Class constructor
	//----------------------------------------------------------
	public CopyrightPanel()
	{
		super("Copyright (c) 2023, Ethan L. Baker & Matthew E. Morgan");
		setFont(Font.font("Copperplate", 							FontWeight.EXTRA_BOLD, 12));
		setEffect(shadow);
		setTextAlignment(TextAlignment.LEFT);
		setFill(Color.WHITESMOKE);
	}




}



