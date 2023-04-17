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
import model.TranslationsString;
import utilities.GlobalVariables;

/** The class containing the Search Semester View  for linking Semester and Gen Ed Area
 * for the Gen Ed Area Data
 *  Management Application
 */
//==============================================================
public class SearchSemesterForASCIRView extends SearchSemesterView
{


    //

    // constructor for this class -- takes a model object
    //----------------------------------------------------------
    public SearchSemesterForASCIRView(IModel mdl)
    {
        super(mdl);
        keyToSendWithData = "SearchSemester";

    }



    //---------------------------------------------------------
    protected String getPromptString() {
        try {
            ts.populate("LBL_SearchSemesterForASCIR");
        }
        catch (Exception ex) {

        }

        return ts.getDisplayString();
    }
}

//---------------------------------------------------------------
//	Revision History:
//


