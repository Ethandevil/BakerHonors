package userinterface;

import impresario.IModel;

public class SearchSemesterForMSCIRView extends SearchSemesterForASCIRView{

    // constructor for this class -- takes a model object
    //----------------------------------------------------------
    public SearchSemesterForMSCIRView(IModel mdl)
    {
        super(mdl);
        keyToSendWithData = "SearchSemester";
    }

    //---------------------------------------------------------
    protected String getPromptString() {
        return "Select/Enter Semester to update Student Categorization data for:";
    }
}
