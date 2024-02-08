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
package model;

// system imports
import javafx.scene.Scene;
import java.util.Properties;

// project imports
import event.Event;
import exception.InvalidPrimaryKeyException;
import exception.MultiplePrimaryKeysException;

import userinterface.View;
import userinterface.ViewFactory;

//This transaction will manage the entering of student categorization data and instructor reflections
//=================================================================================
public class AddStudentCategorizationReflectionTransaction extends  Transaction {

    protected SemesterCollection mySemesterList;
    protected Semester mySelectedSemester;
    protected GenEdAreaCollection myGenEdAreaList;
    protected GenEdArea mySelectedGenEdArea;
    protected SLOCollection mySLOList;
    protected AssessmentTeam mySelectedAssessmentTeam;
    protected ReflectionQuestionCollection allReflectionQuestions;
    protected PerformanceCategoryCollection allPCs;
    protected StudentCategorization mySC1;
    protected StudentCategorization mySC2;
    protected StudentCategorization mySC3;
    protected StudentCategorization mySC4;
    protected InstructorReflections myReflection;

    // GUI Components
    protected String transactionErrorMessage = "";

    /**
     * Constructor for this class.
     *
     */
    //----------------------------------------------------------
    public AddStudentCategorizationReflectionTransaction() throws Exception{
        super();
        allPCs = new PerformanceCategoryCollection();
        allPCs.findAll();
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

        myRegistry.setDependencies(dependencies);
    }

    //-----------------------------------------------------------
    public Object getState(String key)
    {

        if (key.equals("SemesterList") == true)
        {
            return mySemesterList;
        }
        else
        if (key.equals("GenEdAreaList") == true)
        {
            return myGenEdAreaList;
        }
        else
        if(key.equals("AssessmentTeamID") == true)
        {
            if(mySelectedAssessmentTeam != null){
                return mySelectedAssessmentTeam.getState("ID");
            }
            else{
                return null;
            }
        }

        else
        if (key.equals("TransactionError") == true)
        {
            return transactionErrorMessage;
        }
        else if(key.equals("SLOList")){
            return mySLOList;
        }
        else if(key.equals("GenEdAreaData")){
            if (mySelectedGenEdArea != null)
                return mySelectedGenEdArea.getState("AreaName");
            else
                return "";
        }
        else if(key.equals("SemData")){
            if(mySelectedSemester != null){
                String fullSemester = (String)mySelectedSemester.getState("SemName") + " " +
                        (String)mySelectedSemester.getState("Year");
                return fullSemester;
            }
            else{
                return "";
            }
        }
        else if(key.equals("ReflectionQuestionList")){
            return allReflectionQuestions;
        }
        else if(key.startsWith("Category")){
            String catNum = key.substring(key.length()-1);
            if(allPCs != null){
                PerformanceCategory p = allPCs.retrieve(catNum);
                return p.getState("Name");
            }
            else{
                return "Unknown";
            }
        }
        else if(key.equals("FreshmenSCData")){
            return mySC1;
        }
        else if(key.equals("SophomoreSCData")){
            return mySC2;
        }
        else if(key.equals("JuniorSCData")){
            return mySC3;
        }
        else if(key.equals("SeniorSCData")){
            return mySC4;
        }
        else if(key.equals("RQData")){
            return myReflection;
        }

        return null;
    }

    //-----------------------------------------------------------
    public void stateChangeRequest(String key, Object value)
    {
        // DEBUG System.out.println("AddOfferingTransaction.sCR: key: " + key);

        if (key.equals("DoYourJob") == true)
        {
            doYourJob();
        }
        else
        if (key.equals("SearchSemester") == true)
        {
            Properties props = (Properties)value;

            mySemesterList = new SemesterCollection();

            String semNm = props.getProperty("SemName");
            String semYr = props.getProperty("Year");
            mySemesterList.findBySemNameAndYear(semNm, semYr);

            try
            {
                Scene newScene = createSemesterCollectionView();
                swapToView(newScene);
            }
            catch (Exception ex)
            {
                new Event(Event.getLeafLevelClassName(this), "processTransaction",
                        "Error in creating SemesterCollectionView", Event.ERROR);
            }
        }
        else
        if (key.equals("SemesterSelected") == true)
        {

            String semIdSent = (String)value;
            int semIdSentVal =
                    Integer.parseInt(semIdSent);
            mySelectedSemester = mySemesterList.retrieve(semIdSentVal);
            //todo check if there is an assessment team
            try
            {

                Scene newScene = createSearchGenEdAreaView();

                swapToView(newScene);

            }
            catch (Exception ex)
            {
                new Event(Event.getLeafLevelClassName(this), "stateChangeRequest",
                        "Error in creating SearchGenEdAreaView", Event.ERROR);
                ex.printStackTrace();
            }
        }

        else
        if (key.equals("SearchArea") == true) {

            Properties props = (Properties)value;

            myGenEdAreaList = new GenEdAreaCollection();

            String genNm = props.getProperty("GenEdAreaName");
            String desc = props.getProperty("Notes");
            myGenEdAreaList.findByNameAndNotesPart(genNm, desc);

            try
            {
                Scene newScene = createGenEdAreaCollectionView();
                swapToView(newScene);
            }
            catch (Exception ex)
            {
                new Event(Event.getLeafLevelClassName(this), "stateChangeRequest",
                        "Error in creating GenEdAreaCollectionView", Event.ERROR);
            }

        }
        else
        if (key.equals("GenEdAreaSelected") == true)
        {
            String genNumSent = (String)value;

            mySelectedGenEdArea = myGenEdAreaList.retrieve(genNumSent);

            mySLOList = new SLOCollection();
            mySLOList.findByGenEdArea((String)mySelectedGenEdArea.getState("ID"));

            boolean validAT = checkForValidAssessmentTeam();
            if (validAT){
                Scene s = createStudentCategorizationAndReflectionChoiceView();
                swapToView(s);
            }
        }
        else if(key.equals("AddNewStudentCategorization")){
            Scene s = createAddStudentCategorizationView();
            swapToView(s);
        }
        else if(key.equals("AddNewInstructorReflection")){
            allReflectionQuestions = new ReflectionQuestionCollection();
            allReflectionQuestions.findAll();
            Scene s = createAddReflectionView();
            swapToView(s);
        }
        else if (key.equals("ReflectionData")){
            Properties props = (Properties)value;
            String atID = props.getProperty("AssessmentTeamID");
            String rqID = props.getProperty("ReflectionQuestionID");

            try{
                myReflection = new InstructorReflections(atID, rqID);
            }
            catch(InvalidPrimaryKeyException ex){
                myReflection = new InstructorReflections(props);
                myReflection.update();
                transactionErrorMessage = (String)myReflection.getState("UpdateStatusMessage");
            }
            catch(MultiplePrimaryKeysException ex2){}


        }
        else if(key.equals("StudentCategorizationData")){
            Properties props = (Properties)value;
            String sloID = props.getProperty("SLOID");

            String scfr1 = props.getProperty("scfr1");
            String scfr2 = props.getProperty("scfr2");
            String scfr3 = props.getProperty("scfr3");
            String scfr4 = props.getProperty("scfr4");

            mySC1 = createSC(sloID, "Freshmen", scfr1, scfr2, scfr3, scfr4);

            String scso1 = props.getProperty("scso1");
            String scso2 = props.getProperty("scso2");
            String scso3 = props.getProperty("scso3");
            String scso4 = props.getProperty("scso4");

            mySC2 = createSC(sloID, "Sophomore", scso1, scso2, scso3, scso4);

            String scjr1 = props.getProperty("scjr1");
            String scjr2 = props.getProperty("scjr2");
            String scjr3 = props.getProperty("scjr3");
            String scjr4 = props.getProperty("scjr4");

            mySC3 = createSC(sloID, "Junior", scjr1, scjr2, scjr3, scjr4);

            String scsr1 = props.getProperty("scsr1");
            String scsr2 = props.getProperty("scsr2");
            String scsr3 = props.getProperty("scsr3");
            String scsr4 = props.getProperty("scsr4");

            mySC4 = createSC(sloID, "Senior", scsr1, scsr2, scsr3, scsr4);

            if(saveStudentCategorization(sloID, "Freshmen", mySC1)) {
                if(saveStudentCategorization(sloID, "Sophomore", mySC2)){
                    if(saveStudentCategorization(sloID, "Junior", mySC3)){
                        if(saveStudentCategorization(sloID, "Senior", mySC4)){
                            transactionErrorMessage = "All student categorizations saved successfully";
                        }
                    }
                }
            }
        }

        myRegistry.updateSubscribers(key, this);
    }

    //------------------------------------------------------
    protected StudentCategorization createSC(String sloID, String sLevel,
                                           String cat1N, String cat2N, String cat3N, String cat4N){
        Properties props = new Properties();
        props.setProperty("AssessmentTeamID", (String)mySelectedAssessmentTeam.getState("ID"));
        props.setProperty("SLOID", sloID);
        props.setProperty("StudentLevel", sLevel);
        props.setProperty("Cat1Number", cat1N);
        props.setProperty("Cat2Number", cat2N);
        props.setProperty("Cat3Number", cat3N);
        props.setProperty("Cat4Number", cat4N);
        return new StudentCategorization(props);
    }

    /**
     * This method encapsulates all the logic of adding the Student Categorization,
     * verifying that it does not already exist, for example, etc.
     */
    //----------------------------------------------------------
    protected boolean saveStudentCategorization(String sloID, String sLevel, StudentCategorization sc)
    {
        boolean retVal = false;
        String aTID = (String) mySelectedAssessmentTeam.getState("ID");
        try
        {
            StudentCategorization oldStudentCategorization = new StudentCategorization(aTID, sloID, sLevel);

            transactionErrorMessage = "ERROR: Data for this gen ed area/semester/slo/" + sLevel + " already entered" +
                    " - please modify if needed!";
            new Event(Event.getLeafLevelClassName(this), "processStudentCategorizationData",
                    "Already exists!",
                    Event.ERROR);
            return retVal;
        }
        catch (InvalidPrimaryKeyException ex)
        {
            try
            {
                sc.update();
                transactionErrorMessage = (String)sc.getState("UpdateStatusMessage");
                retVal = true;
                return retVal;
            }
            //fix these catch blocks or remove them ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            catch (Exception excep)
            {
                transactionErrorMessage = "Error in saving Student Categorization: " + excep.toString();
                new Event(Event.getLeafLevelClassName(this), "processTransaction",
                        "Error in saving Student Categorization: " + excep.toString(),
                        Event.ERROR);
                return retVal;
            }
        }
        catch (MultiplePrimaryKeysException ex2)
        {
            return retVal;
        }
    }

    //------------------------------------------------------
    protected boolean checkForValidAssessmentTeam(){
        String semID = (String)mySelectedSemester.getState("ID");
        String genEdID = (String)mySelectedGenEdArea.getState("ID");

        try{
            mySelectedAssessmentTeam = new AssessmentTeam(genEdID, semID);
            return true;
        }
        catch(Exception ex){
            transactionErrorMessage = "ERROR: Invalid Gen Ed Area / Semester combination chosen!";
            return false;
        }
    }

    /**
     * Create the view of this class. And then the super-class calls
     * swapToView() to display the view in the frame
     */
    //------------------------------------------------------
    protected Scene createView()
    {
        Scene currentScene = myViews.get("SearchSemesterForASCIRView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("SearchSemesterForASCIRView", this);
            currentScene = new Scene(newView);
            myViews.put("SearchSemesterForASCIRView", currentScene);

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
        View newView = ViewFactory.createView("SemesterCollectionForASCIRView", this);
        Scene currentScene = new Scene(newView);

        return currentScene;

    }

    //------------------------------------------------------
    protected Scene createSearchGenEdAreaView()
    {

        Scene currentScene = myViews.get("SearchGenEdAreaForASCIRView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("SearchGenEdAreaForASCIRView", this);
            currentScene = new Scene(newView);
            myViews.put("SearchGenEdAreaForASCIRView", currentScene);

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
        View newView = ViewFactory.createView("GenEdAreaCollectionForASCIRView", this);
        Scene currentScene = new Scene(newView);

        return currentScene;

    }

    //------------------------------------------------------
    protected Scene createStudentCategorizationAndReflectionChoiceView(){
        View newView = ViewFactory.createView("StudentCategorizationAndReflectionChoiceView", this);
        Scene currentScene = new Scene(newView);

        return currentScene;
    }

    //------------------------------------------------------
    protected Scene createAddStudentCategorizationView(){
        View newView = ViewFactory.createView("AddStudentCategorizationView", this);
        Scene currentScene = new Scene(newView);

        return currentScene;
    }

    //------------------------------------------------------
    protected Scene createAddReflectionView(){
        View newView = ViewFactory.createView("AddReflectionView", this);
        Scene currentScene = new Scene(newView);

        return currentScene;
    }


}
