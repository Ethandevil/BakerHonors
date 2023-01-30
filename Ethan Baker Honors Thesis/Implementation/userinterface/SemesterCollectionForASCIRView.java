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
public class SemesterCollectionForASCIRView extends SemesterCollectionView
{


    //--------------------------------------------------------------------------
    public SemesterCollectionForASCIRView(IModel model)
    {

        super(model);


    }


    //---------------------------------------------------------
    protected String getPromptText() {
        return "Select the semester you wish to enter Student Categorization data for:";
    }



}
