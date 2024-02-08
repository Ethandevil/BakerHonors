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

import java.util.Vector;

import javafx.beans.property.SimpleStringProperty;

//==============================================================================
public class AssessmentTeamClassesTableModel
{
    private final SimpleStringProperty id;
    private final SimpleStringProperty assessmentTeamId;
    private final SimpleStringProperty courseDisciplineCode;
    private final SimpleStringProperty courseNumber;


    //----------------------------------------------------------------------------
    public AssessmentTeamClassesTableModel(Vector<String> atData)
    {
        id = new SimpleStringProperty(atData.elementAt(0));
        assessmentTeamId =  new SimpleStringProperty(atData.elementAt(1));
        courseDisciplineCode =  new SimpleStringProperty(atData.elementAt(2));
        courseNumber =  new SimpleStringProperty(atData.elementAt(3));
    }

    //----------------------------------------------------------------------------
    public String getId() {return id.get();}
    //----------------------------------------------------------------------------
    public void setId(String Id) {id.set(Id);}
    //----------------------------------------------------------------------------
    public String getAssessmentTeamId() {
        return assessmentTeamId.get();
    }
    //----------------------------------------------------------------------------
    public void setAssessmentTeamId(String id) {
        assessmentTeamId.set(id);
    }
    //----------------------------------------------------------------------------
    public String getCourseDisciplineCode() {
        return courseDisciplineCode.get();
    }
    //----------------------------------------------------------------------------
    public void setCourseDisciplineCode(String code) {
        courseDisciplineCode.set(code);
    }
    //----------------------------------------------------------------------------
    public String getCourseNumber() {
        return courseNumber.get();
    }
    //----------------------------------------------------------------------------
    public void setCourseNumber(String num) {
        courseNumber.set(num);
    }
    //----------------------------------------------------------------------------

}
