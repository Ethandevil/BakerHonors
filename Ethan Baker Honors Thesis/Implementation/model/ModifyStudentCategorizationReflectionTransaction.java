package model;

import exception.InvalidPrimaryKeyException;
import exception.MultiplePrimaryKeysException;
import javafx.scene.Scene;
import userinterface.SLOTableModel;
import userinterface.View;
import userinterface.ViewFactory;

import java.util.Properties;

public class ModifyStudentCategorizationReflectionTransaction extends AddStudentCategorizationReflectionTransaction{
    public ModifyStudentCategorizationReflectionTransaction() throws Exception{
        super();
    }

    //----------------------------------------------------------
    protected void setDependencies()
    {
        dependencies = new Properties();
        dependencies.setProperty("CancelAddStudentCategorization", "CancelTransaction");
        dependencies.setProperty("CancelAddInstructorReflection", "CancelTransaction");
        dependencies.setProperty("CancelSemesterList", "CancelTransaction");
        dependencies.setProperty("CancelSearchArea", "CancelTransaction");
        dependencies.setProperty("CancelAreaList", "CancelTransaction");
        dependencies.setProperty("GenEdAreaSelected", "TransactionError");
        dependencies.setProperty("CancelAddStudentCategorizationData", "CancelTransaction");
        dependencies.setProperty("CancelAddReflection", "CancelTransaction");
        dependencies.setProperty("StudentCategorizationData", "TransactionError");
        dependencies.setProperty("SLOSelected", "TransactionError,FreshmenSCData,SophomoreSCData,JuniorSCData,SeniorSCData");

        myRegistry.setDependencies(dependencies);
    }



    //--------------------------------------------------------------------------------------------
    public void stateChangeRequest(String key, Object value){
        if(key.equals("SLOSelected")){
            SLOTableModel selectedItem = (SLOTableModel) value;
            getAllRelevantSCs(selectedItem.getSloID());
            myRegistry.updateSubscribers(key, this);
        }
        else if(key.equals("StudentCategorizationData")) {
            Properties props = (Properties) value;
            String sloID = props.getProperty("SLOID");

            String scfr1 = props.getProperty("scfr1");
            String scfr2 = props.getProperty("scfr2");
            String scfr3 = props.getProperty("scfr3");
            String scfr4 = props.getProperty("scfr4");

            mySC1.setCategory1Val(scfr1);
            mySC1.setCategory2Val(scfr2);
            mySC1.setCategory3Val(scfr3);
            mySC1.setCategory4Val(scfr4);

            String scso1 = props.getProperty("scso1");
            String scso2 = props.getProperty("scso2");
            String scso3 = props.getProperty("scso3");
            String scso4 = props.getProperty("scso4");

            mySC2.setCategory1Val(scso1);
            mySC2.setCategory2Val(scso2);
            mySC2.setCategory3Val(scso3);
            mySC2.setCategory4Val(scso4);

            String scjr1 = props.getProperty("scjr1");
            String scjr2 = props.getProperty("scjr2");
            String scjr3 = props.getProperty("scjr3");
            String scjr4 = props.getProperty("scjr4");

            mySC3.setCategory1Val(scjr1);
            mySC3.setCategory2Val(scjr2);
            mySC3.setCategory3Val(scjr3);
            mySC3.setCategory4Val(scjr4);

            String scsr1 = props.getProperty("scsr1");
            String scsr2 = props.getProperty("scsr2");
            String scsr3 = props.getProperty("scsr3");
            String scsr4 = props.getProperty("scsr4");

            mySC4.setCategory1Val(scsr1);
            mySC4.setCategory2Val(scsr2);
            mySC4.setCategory3Val(scsr3);
            mySC4.setCategory4Val(scsr4);

            mySC1.update();
            transactionErrorMessage = (String)mySC1.getState("UpdateStatusMessage");
            mySC2.update();
            transactionErrorMessage = (String)mySC2.getState("UpdateStatusMessage");
            mySC3.update();
            transactionErrorMessage = (String)mySC3.getState("UpdateStatusMessage");
            mySC4.update();
            transactionErrorMessage = (String)mySC4.getState("UpdateStatusMessage");

            if(transactionErrorMessage.startsWith("ERR") == false){
                transactionErrorMessage = "All student categorizations updated successfully!";
            }
            myRegistry.updateSubscribers(key, this);
        }
        else{
            super.stateChangeRequest(key, value);
        }
    }

    //--------------------------------------------------------------------------------------------
    protected void getAllRelevantSCs(String sloID){
        try {
            mySC1 = new StudentCategorization((String) mySelectedAssessmentTeam.getState("ID"), sloID, "Freshmen");
            transactionErrorMessage = "";
        }
        catch(InvalidPrimaryKeyException ex){
            transactionErrorMessage = "ERROR: No student categorization data available for selected SLO! Please add!";
        }
        catch(MultiplePrimaryKeysException ex2){
        }

        try {
            mySC2 = new StudentCategorization((String) mySelectedAssessmentTeam.getState("ID"), sloID, "Sophomore");
            transactionErrorMessage = "";
        }
        catch(InvalidPrimaryKeyException ex){
            transactionErrorMessage = "ERROR: No student categorization data available for selected SLO! Please add!";
        }
        catch(MultiplePrimaryKeysException ex2){
        }

        try {
            mySC3 = new StudentCategorization((String) mySelectedAssessmentTeam.getState("ID"), sloID, "Junior");
            transactionErrorMessage = "";
        }
        catch(InvalidPrimaryKeyException ex){
            transactionErrorMessage = "ERROR: No student categorization data available for selected SLO! Please add!";
        }
        catch(MultiplePrimaryKeysException ex2){
        }

        try {
            mySC4 = new StudentCategorization((String) mySelectedAssessmentTeam.getState("ID"), sloID, "Senior");
            transactionErrorMessage = "";
        }
        catch(InvalidPrimaryKeyException ex){
            transactionErrorMessage = "ERROR: No student categorization data available for selected SLO! Please add!";
        }
        catch(MultiplePrimaryKeysException ex2){
        }
    }

    /**
     * Create the view of this class. And then the super-class calls
     * swapToView() to display the view in the frame
     */
    //------------------------------------------------------
    protected Scene createView()
    {
        Scene currentScene = myViews.get("SearchSemesterForMSCIRView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("SearchSemesterForMSCIRView", this);
            currentScene = new Scene(newView);
            myViews.put("SearchSemesterForMSCIRView", currentScene);

            return currentScene;
        }
        else
        {
            return currentScene;
        }
    }


    /**
     * Create the view containing the table of all matching Semesters on the search criteria sent
     */
    //------------------------------------------------------
    protected Scene createSemesterCollectionView()
    {
        View newView = ViewFactory.createView("SemesterCollectionForMSCIRView", this);
        Scene currentScene = new Scene(newView);

        return currentScene;

    }

    //------------------------------------------------------
    protected Scene createSearchGenEdAreaView()
    {

        Scene currentScene = myViews.get("SearchGenEdAreaForMSCIRView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("SearchGenEdAreaForMSCIRView", this);
            currentScene = new Scene(newView);
            myViews.put("SearchGenEdAreaForMSCIRView", currentScene);

            return currentScene;
        }
        else
        {
            return currentScene;
        }

    }

    //------------------------------------------------------
    protected Scene createGenEdAreaCollectionView()
    {
        View newView = ViewFactory.createView("GenEdAreaCollectionForMSCIRView", this);
        Scene currentScene = new Scene(newView);

        return currentScene;

    }

    //------------------------------------------------------
    protected Scene createStudentCategorizationAndReflectionChoiceView(){
        View newView = ViewFactory.createView("ModifyStudentCategorizationAndReflectionChoiceView", this);
        Scene currentScene = new Scene(newView);

        return currentScene;
    }

    //------------------------------------------------------
    protected Scene createAddReflectionView(){
        View newView = ViewFactory.createView("ModifyReflectionView", this);
        Scene currentScene = new Scene(newView);

        return currentScene;
    }

    //--------------------------------------------------------------------------------------------
    protected Scene createAddStudentCategorizationView() {
        View newView = ViewFactory.createView("ModifyStudentCategorizationView", this);
        Scene currentScene = new Scene(newView);

        return currentScene;
    }
}
