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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;



import javafx.scene.effect.DropShadow;

// project imports

//==============================================================
public class CommonTitleWithoutLogoPanel extends VBox
{

	private DropShadow shadow = new DropShadow();

	// Class constructor
	//----------------------------------------------------------
	public CommonTitleWithoutLogoPanel()
	{
		super(10);

		Text clientText = new Text("SUNY BROCKPORT");
		clientText.setFont(Font.font("Copperplate", FontWeight.EXTRA_BOLD, 24));
           clientText.setEffect(shadow);
		clientText.setTextAlignment(TextAlignment.CENTER);
		clientText.setFill(Color.WHITESMOKE);
		getChildren().add(clientText);

		Text titleText = new Text(" General Education Assessment Data Management System ");
		titleText.setFont(Font.font("Copperplate", FontWeight.THIN, 16));
		titleText.setTextAlignment(TextAlignment.CENTER);
		titleText.setFill(Color.GOLD);
		getChildren().add(titleText);

		setAlignment(Pos.CENTER);
               
	}

	


}



