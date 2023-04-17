package userinterface;

import impresario.IModel;

public class GenEdAreaCollectionForMSCIRView extends GenEdAreaCollectionForASCIRView{
    //--------------------------------------------------------------------------
    public GenEdAreaCollectionForMSCIRView(IModel model)
    {
        super(model);
        myModel.subscribe("TransactionError", this);
    }

    //---------------------------------------------------------
    protected String getPromptText() {
        try {
            ts.populate("LBL_GenEdAreaCollectionForMSCIR");
        }
        catch (Exception ex) {

        }
        return ts.getDisplayString();
    }
}
