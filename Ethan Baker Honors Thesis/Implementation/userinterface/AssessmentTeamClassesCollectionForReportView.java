package userinterface;

import impresario.IModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import model.AssessmentTeamClasses;
import model.AssessmentTeamClassesCollection;
import model.InstructorReflectionsDisplay;
import model.InstructorReflectionsDisplayCollection;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;

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
        try {
            FileWriter outFile = new FileWriter(fName);
            PrintWriter out = new PrintWriter(outFile);

            String line = "Report for Gen Ed Area: " + myModel.getState("AreaName") +
                    " assessed in Semester: " + myModel.getState("Semester");

            out.println(line);

            line = "";

            out.println(line);

            line = "Course Code,Course Number";

            out.println(line);

            line = ",";

            try
            {
                AssessmentTeamClassesCollection adCollection =
                        (AssessmentTeamClassesCollection)myModel.getState("AssessmentTeamClassesDisplayList");

                Vector entryList = adCollection.getAssessmentTeamClassesDisplays(); //needs to be changed

                if (entryList.size() > 0)
                {

                    Enumeration entries = entryList.elements();

                    while (entries.hasMoreElements() == true)
                    {
                        AssessmentTeamClasses nextOT = (AssessmentTeamClasses) entries.nextElement();
                        line = "";
                        line += nextOT.getState("CourseDisciplineCode") + ",";
                        line += nextOT.getState("CourseNumber");
                        out.println(line);
                    }

                }

            }
            catch (Exception e) {//SQLException e) {
                // Need to handle this exception
                System.out.println(e);
                e.printStackTrace();
            }

            // Finally, print the time-stamp
            DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
            DateFormat timeFormat = new SimpleDateFormat("hh:mm aaa");
            Date date = new Date();
            String timeStamp = dateFormat.format(date) + " " +
                    timeFormat.format(date);

            out.println("");
            out.println("Report created on " + timeStamp);

            out.close();
        }
        catch (FileNotFoundException e)
        {
            // Make these Alerts
            //JOptionPane.showMessageDialog(null, "Could not access file to save: "
            //      + fName, "Save Error", JOptionPane.ERROR_MESSAGE );
        }
        catch (IOException e)
        {
            //JOptionPane.showMessageDialog(null, "Error in saving to file: "
            //      + e.toString(), "Save Error", JOptionPane.ERROR_MESSAGE );

        }
    }
}
