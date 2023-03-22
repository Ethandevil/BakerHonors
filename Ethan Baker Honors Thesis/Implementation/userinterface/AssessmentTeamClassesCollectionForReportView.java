package userinterface;

import impresario.IModel;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AssessmentTeamClassesCollectionForReportView extends AssessmentTeamClassesCollectionView{
    public AssessmentTeamClassesCollectionForReportView(IModel model){
        super(model);
    }

    //-------------------------------------------------------------
    protected String getSubmitButtonText(){
        return "Write to Excel File";
    }

    //-------------------------------------------------------------
    protected String getSubmitButtonIcon(){
        return "/images/check.png";
    }

    //---------------------------------------------------------
    protected String getPromptText() {
        return "Classes Report for:";
    }

    //--------------------------------------------------------------------------
    protected void processOTSelected()
    {
        saveToExcelFile();
    }

    //-------------------------------------------------------------
    protected void writeToFile(String fName)
    {

    }
}
