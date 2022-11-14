// tabs=4
//************************************************************
//	COPYRIGHT 2021, Kyle D. Adams, Matthew E. Morgan 
//    and Sandeep Mitra, State University of New York. 
//   - Brockport (SUNY Brockport) 
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
import javafx.event.Event;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.Properties;

// project imports
import impresario.IModel;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/** The class containing the Search ISLO View for linking to an ISLO
 *  for the ISLO Data Management application 
 */
//==============================================================
public class SearchISLOForOfferingView extends SearchISLOView
{

	// constructor for this class -- takes a model object
	//----------------------------------------------------------
	public SearchISLOForOfferingView(IModel mdl)
	{
		
		super(mdl);
		
	}

	
	//-------------------------------------------------------------
	protected String getActionText()
	{
		return "Search ISLO to link to: ";
	}


}

//---------------------------------------------------------------
//	Revision History:
//


