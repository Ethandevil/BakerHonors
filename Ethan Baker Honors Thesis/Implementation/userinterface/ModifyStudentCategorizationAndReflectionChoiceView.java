package userinterface;

import impresario.IModel;

public class ModifyStudentCategorizationAndReflectionChoiceView extends StudentCategorizationAndReflectionChoiceView{
    public ModifyStudentCategorizationAndReflectionChoiceView(IModel model){
        super(model);
    }

    //-------------------------------------------------------------
    protected String getSCButtonText(){
        return "Update Student Categorizations";
    }

    //-------------------------------------------------------------
    protected String getIRButtonText(){
        return "Update Instructor Reflections";
    }
}
