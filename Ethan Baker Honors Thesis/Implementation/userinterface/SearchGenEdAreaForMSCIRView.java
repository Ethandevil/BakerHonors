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

import impresario.IModel;

public class SearchGenEdAreaForMSCIRView extends SearchGenEdAreaForASCIRView{
    //--------------------------------------------------------------------------
    public SearchGenEdAreaForMSCIRView(IModel model)
    {
        super(model);
    }

    //-------------------------------------------------------------
    protected String getActionText()
    {
        return "Search Gen Ed Area to update Student Categorization data for: ";
    }
}
