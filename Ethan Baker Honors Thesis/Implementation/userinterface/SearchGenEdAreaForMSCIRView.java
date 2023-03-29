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
