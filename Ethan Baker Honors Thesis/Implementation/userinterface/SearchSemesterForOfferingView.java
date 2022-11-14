// tabs=4
//************************************************************
//	COPYRIGHT 2021, Kyle D. Adams, Matthew E. Morgan and 
//   Sandeep Mitra, State University of New York. - Brockport
//   (SUNY Brockport) 
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
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Properties;
import java.util.Vector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;


// project imports
import impresario.IModel;
import model.Transaction;

/** The class containing the Search Semester View  for linking Semester and Gen Ed Area
 * for the Gen Ed Area Data
 *  Management Application
 */
//==============================================================
public class SearchSemesterForOfferingView extends SearchSemesterView
{

	//

	// constructor for this class -- takes a model object
	//----------------------------------------------------------
	public SearchSemesterForOfferingView(IModel mdl)
	{
		super(mdl);
		keyToSendWithData = "SearchSemester";
	}

	

	//---------------------------------------------------------
	protected String getPromptString() {
		return "Select/Enter Semester to link to Gen Ed Area information:";
	}
}

//---------------------------------------------------------------
//	Revision History:
//


