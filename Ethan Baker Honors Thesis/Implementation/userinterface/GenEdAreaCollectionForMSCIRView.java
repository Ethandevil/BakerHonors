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
        return "Select the Gen Ed Area to update Student Categorization data for:";
    }
}
