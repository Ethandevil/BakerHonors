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

import impresario.IModel;
import model.InstructorReflections;
import model.ReflectionQuestion;

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
        return "Update Instructor Reflection: ";
    }

    //-------------------------------------------------------------
    protected String getSubmitButtonText(){
        return "Update";
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
