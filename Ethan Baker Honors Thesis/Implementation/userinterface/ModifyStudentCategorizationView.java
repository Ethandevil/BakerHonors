package userinterface;

import impresario.IModel;
import model.StudentCategorization;

public class ModifyStudentCategorizationView extends AddStudentCategorizationView {
    public ModifyStudentCategorizationView(IModel model) {
        super(model);
        myModel.subscribe("FreshmenSCData", this);
        myModel.subscribe("SophomoreSCData", this);
        myModel.subscribe("JuniorSCData", this);
        myModel.subscribe("SeniorSCData", this);
    }

    //---------------------------------------------------------
    protected String getPromptText() {
        return "Student Categorization Data Modification Form";
    }

    //---------------------------------------------------------
    protected String getSubmitButtonText() {
        return "Update";
    }

    //---------------------------------------------------------
    protected String getSubmitButtonIconFilePathName() {
        return "/images/savecolor.png";
    }

    //-------------------------------------------------------
    protected void populateFieldsHelper(){
        StudentCategorization freshmen = (StudentCategorization)myModel.getState("FreshmenSCData");
        StudentCategorization sophomore = (StudentCategorization)myModel.getState("SophomoreSCData");
        StudentCategorization junior = (StudentCategorization)myModel.getState("JuniorSCData");
        StudentCategorization senior = (StudentCategorization)myModel.getState("SeniorSCData");

        if((freshmen != null) && (sophomore != null) && (junior != null) && (senior != null)) {
            fr1.setText((String) freshmen.getState("Cat1Number"));
            fr2.setText((String) freshmen.getState("Cat2Number"));
            fr3.setText((String) freshmen.getState("Cat3Number"));
            fr4.setText((String) freshmen.getState("Cat4Number"));
            so1.setText((String) sophomore.getState("Cat1Number"));
            so2.setText((String) sophomore.getState("Cat2Number"));
            so3.setText((String) sophomore.getState("Cat3Number"));
            so4.setText((String) sophomore.getState("Cat4Number"));
            jr1.setText((String) junior.getState("Cat1Number"));
            jr2.setText((String) junior.getState("Cat2Number"));
            jr3.setText((String) junior.getState("Cat3Number"));
            jr4.setText((String) junior.getState("Cat4Number"));
            sr1.setText((String) senior.getState("Cat1Number"));
            sr2.setText((String) senior.getState("Cat2Number"));
            sr3.setText((String) senior.getState("Cat3Number"));
            sr4.setText((String) senior.getState("Cat4Number"));
        }
    }

    //-------------------------------------------------------------
    protected void processSLOSelected(){
        SLOTableModel selectedItem = tableOfSLOs.getSelectionModel().getSelectedItem();
        myModel.stateChangeRequest("SLOSelected", selectedItem);
    }

    //---------------------------------------------------------
    public void updateState(String key, Object value) {
        if(key.equals("FreshmenSCData")){
            StudentCategorization freshmen = (StudentCategorization)value;
            fr1.setText((String) freshmen.getState("Cat1Number"));
            fr2.setText((String) freshmen.getState("Cat2Number"));
            fr3.setText((String) freshmen.getState("Cat3Number"));
            fr4.setText((String) freshmen.getState("Cat4Number"));
        }
        else if(key.equals("SophomoreSCData")){
            StudentCategorization sophomore = (StudentCategorization)value;
            so1.setText((String) sophomore.getState("Cat1Number"));
            so2.setText((String) sophomore.getState("Cat2Number"));
            so3.setText((String) sophomore.getState("Cat3Number"));
            so4.setText((String) sophomore.getState("Cat4Number"));
        }
        else if(key.equals("JuniorSCData")){
            StudentCategorization junior = (StudentCategorization)value;
            jr1.setText((String) junior.getState("Cat1Number"));
            jr2.setText((String) junior.getState("Cat2Number"));
            jr3.setText((String) junior.getState("Cat3Number"));
            jr4.setText((String) junior.getState("Cat4Number"));
        }
        else if(key.equals("SeniorSCData")){
            StudentCategorization senior = (StudentCategorization)value;
            sr1.setText((String) senior.getState("Cat1Number"));
            sr2.setText((String) senior.getState("Cat2Number"));
            sr3.setText((String) senior.getState("Cat3Number"));
            sr4.setText((String) senior.getState("Cat4Number"));
        }
        else{
            super.updateState(key, value);
        }
    }
}
