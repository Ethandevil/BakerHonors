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
        try {
            ts.populate("LBL_SearchSemesterForASCIR");
        }
        catch (Exception ex) {

        }

        return ts.getDisplayString();
    }
}
