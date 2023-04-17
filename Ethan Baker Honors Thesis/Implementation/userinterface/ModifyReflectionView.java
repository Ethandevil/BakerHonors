package userinterface;

import impresario.IModel;
import model.InstructorReflections;
import model.ReflectionQuestion;
import utilities.GlobalVariables;

public class ModifyReflectionView extends AddReflectionView{
    //--------------------------------------------------------------------------
    public ModifyReflectionView(IModel model)
    {
        super(model);
        myModel.subscribe("RQData", this);
    }

    //--------------------------------------------------------------------------
    protected String getActionText()
    {
        try {
            ts.populate("LBL_ModifyReflection");
        }
        catch (Exception ex) {

        }
        return ts.getDisplayString();
    }

    //-------------------------------------------------------------
    protected String getSubmitButtonText(){

        ts.setTableNameForLocale(GlobalVariables.locale);
        try {
            ts.populate("LBL_Update");
        }
        catch (Exception ex) {

        }
        return ts.getDisplayString();
    }

    //-------------------------------------------------------------
    protected String getSubmitButtonIcon(){return "/images/savecolor.png";}

    //-------------------------------------------------------------
    protected void populateFieldsHelper(){
        InstructorReflections insRfl = (InstructorReflections)myModel.getState("RQData");
        if(insRfl != null){
            reflectionAnswer.setText((String)insRfl.getState("ReflectionText"));
        }
    }

    //-------------------------------------------------------------
    protected void processQuestionSelected(){
        ReflectionQuestionTableModel selectedItem = tableQuestions.getSelectionModel().getSelectedItem();
        myModel.stateChangeRequest("QuestionSelected", selectedItem);
    }

    //-------------------------------------------------------------
    public void updateState(String key, Object value){
        if(key.equals("RQData")){
            InstructorReflections insRfl = (InstructorReflections)value;
            reflectionAnswer.setText((String)insRfl.getState("ReflectionText"));
        }
        else{
            super.updateState(key, value);
        }
    }
}
