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

// project imports
import impresario.IModel;

/** The class containing the Search Gen Ed Area View for linking to a Gen Ed Area
 *  for the Gen Ed Area Data Management application
 */
//==============================================================
public class SearchGenEdAreaForAssessmentTeamView extends SearchGenEdAreaView
{

	// constructor for this class -- takes a model object
	//----------------------------------------------------------
	public SearchGenEdAreaForAssessmentTeamView(IModel mdl)
	{
		
		super(mdl);
		
	}

	
	//-------------------------------------------------------------
	protected String getActionText()
	{
		return "Search Gen Ed Area to link to: ";
	}


}

//---------------------------------------------------------------
//	Revision History:
//


