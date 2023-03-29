package userinterface;

import impresario.IModel;

public class SemesterCollectionForMSCIRView extends SemesterCollectionForASCIRView{
    //--------------------------------------------------------------------------
    public SemesterCollectionForMSCIRView(IModel model)
    {
        super(model);
    }

    //---------------------------------------------------------
    protected String getPromptText() {
        return "Select the semester you wish to update Student Categorization data for:";
    }
}
