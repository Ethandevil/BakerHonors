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

// project imports
import impresario.IModel;

//==============================================================================
public class GenEdAreaCollectionForAssessmentTeamView extends GenEdAreaCollectionView
{
	
        
	//--------------------------------------------------------------------------
	public GenEdAreaCollectionForAssessmentTeamView(IModel model)
	{
		super(model);
		myModel.subscribe("TransactionError", this);
		
	}


	//---------------------------------------------------------
	protected String getPromptText() {
		return "Select the Gen Ed Area to link to:";
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


}
