// tabs=4
//************************************************************
//	COPYRIGHT 2022, Ethan L. Baker, Matthew E. Morgan and
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
import utilities.GlobalVariables;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.util.Properties;
import java.util.Vector;

// project imports
import event.Event;
import exception.InvalidPrimaryKeyException;
import exception.MultiplePrimaryKeysException;

import userinterface.View;
import userinterface.ViewFactory;

/** The class containing the ReportGeneratorTransaction for the ISLO Data Management application */
//==============================================================
public class ReportGeneratorTransaction extends Transaction
{

    protected String transactionErrorMessage = "";
    protected GenEdArea mySelectedGenEdArea;
    protected GenEdAreaCollection myGenEdAreaList;
    protected Semester mySelectedSemester;
    protected SemesterCollection mySemesterList;
    protected SLOCollection mySLOList;
    protected AssessmentTeamCollection myATList;
    protected AssessmentTeamDisplayCollection myATDisplayList;
    protected InstructorReflectionsDisplayCollection myIRDiplayList;
    protected AssessmentTeam mySelectedAT;
    protected StudentCategorizationDisplayCollection myStudentCats;
    protected InstructorReflectionsCollection myInstructorReflections;
    protected AssessmentTeamClassesCollection myAssessmentTeamClasses;
    protected PerformanceCategoryCollection myPCC;
    protected String reportKind = "Basic";


    /**
     * Constructor for this class.
     *
     */
    //----------------------------------------------------------
    public ReportGeneratorTransaction() throws Exception
    {
        super();
        myPCC = new PerformanceCategoryCollection();
        myPCC.findAll();
    }

    //----------------------------------------------------------
    protected void setDependencies()
    {
        dependencies = new Properties();

        dependencies.setProperty("CancelStudentCategorizationList", "CancelTransaction");
        dependencies.setProperty("CancelSemesterList", "CancelTransaction");
        dependencies.setProperty("CancelAreaList", "CancelTransaction");
        dependencies.setProperty("CancelSearchArea", "CancelTransaction");
        dependencies.setProperty("CancelReportGenerator", "CancelTransaction");
        dependencies.setProperty("GenEdAreaSelected", "TransactionError");
        dependencies.setProperty("UpdateStudentCategorization", "StudentCategorizationUpdated");
        myRegistry.setDependencies(dependencies);
    }

    //----------------------------------------------------------
    public void processTransaction(Properties props)
    {
        try {
            Scene newScene = createSearchSemesterView();
            swapToView(newScene);
        }
        catch (Exception excep) {
            new Event(Event.getLeafLevelClassName(this), "processTransaction",
                    "Error in creating Search Semester View", Event.ERROR);
        }
    }

    //-------------------------------------------------------------
    public void processSearchTransaction(Properties props) {

        mySemesterList = new SemesterCollection();

        String semNm = props.getProperty("SemName");
        String semYr = props.getProperty("Year");
        mySemesterList.findBySemNameAndYear(semNm, semYr);

        try {
            Scene newScene = createSemesterCollectionView();
            swapToView(newScene);
        }
        catch (Exception excep) {
            new Event(Event.getLeafLevelClassName(this), "processTransaction",
                    "Error in creating Semester Collection View", Event.ERROR);
        }
    }

    //-----------------------------------------------------------
    public Object getState(String key)
    {
        if (key.equals("PerformanceCategory1") == true) {
            return findCategoryName("1");
        }
        else if (key.equals("PerformanceCategory2") == true) {
            return findCategoryName("2");
        }
        else if (key.equals("PerformanceCategory3") == true) {
            return findCategoryName("3");
        }
        else if (key.equals("PerformanceCategory4") == true) {
            return findCategoryName("4");
        }
        else
        if (key.equals("TransactionError") == true)
        {
            return transactionErrorMessage;
        }
        else if (key.equals("SemesterList") == true)
        {
            return mySemesterList;
        }
        else if (key.equals("GenEdAreaList") == true)
        {
            return myGenEdAreaList;
        }
        else if ((key.equals("AreaName") == true) || (key.equals("GenEdAreaData") == true))
        {
            if (mySelectedGenEdArea != null)
                return mySelectedGenEdArea.getName();
            else
                return "";
        }
        else if ((key.equals("Semester") == true) || (key.equals("SemData") == true))
        {
            if(mySelectedSemester != null){
                String fullSemester = (String)mySelectedSemester.getState("SemName") + " " +
                        (String)mySelectedSemester.getState("Year");
                return fullSemester;
            }
            else
                return "";
        }
        else if (key.equals("StudentCategorizationList") == true)
        {
            return myStudentCats;
        }
        else if(key.equals("NumSLOs") == true){
            return mySLOList.getSize();
        }
        else if(key.equals("InstructorReflectionsDisplayList")){
            return myIRDiplayList;
        }
        else if(key.equals("AssessmentTeamClassesDisplayList")){
            AssessmentTeamClassesCollection atc = new AssessmentTeamClassesCollection();
            atc.findByAssessmentTeamID((String)mySelectedAT.getState("ID"));
            return atc;
        }

        return null;
    }

    //------------------------------------------------------------
    protected String findCategoryName(String number)
    {
        String retVal = "";
        Vector<PerformanceCategory> categories = (Vector)myPCC.getState("Categories");
        for (int cnt = 0; cnt < categories.size(); cnt++) {
            PerformanceCategory nextPC = categories.get(cnt);
            String catNum = (String)nextPC.getState("Number");
            if (catNum.equals(number) == true) {
                retVal = (String)nextPC.getState("Name");
                break;
            }
        }
        return retVal;
    }

    //-----------------------------------------------------------
    public void stateChangeRequest(String key, Object value)
    {
        if (key.equals("DoYourJob") == true)
        {
            doYourJob();
        }
        else
        if (key.equals("ReportData") == true)
        {
            reportKind = "Basic";
            processTransaction((Properties)value);
        }
        else if(key.equals("ReflectionData") == true){
            reportKind = "Reflection";
            processTransaction((Properties)value);
        }
        else if(key.equals("ATData") == true){
            reportKind = "ATData";
            processTransaction((Properties)value);
        }
        else
        if (key.equals("SearchSemester") == true)
        {
            processSearchTransaction((Properties)value);
        }
        else if (key.equals("SemesterSelected") == true) {
            String semIdSent = (String) value;
            int semIdSentVal =
                    Integer.parseInt(semIdSent);
            mySelectedSemester = mySemesterList.retrieve(semIdSentVal);

            try {
                Scene newScene = createSearchGenEdAreaView();
                swapToView(newScene);
            } catch (Exception ex) {
                new Event(Event.getLeafLevelClassName(this), "stateChangeRequest",
                        "Error in creating SearchGenEdAreaView", Event.ERROR);
            }
        }
        else if(key.equals("SearchArea")){
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
        else if (key.equals("GenEdAreaSelected") == true) {
            if(reportKind.equals("Basic")) {
                String genNumSent = (String) value;
                mySelectedGenEdArea = myGenEdAreaList.retrieve(genNumSent);

                mySLOList = new SLOCollection();
                mySLOList.findByGenEdArea((String) mySelectedGenEdArea.getState("ID"));

                boolean validAT = checkForValidAssessmentTeam();
                if (validAT) {

                    myStudentCats = new StudentCategorizationDisplayCollection();
                    String atID = (String) mySelectedAT.getState("ID");
                    myStudentCats.findByAssessmentTeam(atID);

                    Scene s = createStudentCategorizationDisplayCollectionView();
                    swapToView(s);
                } else {
                    transactionErrorMessage = "ERROR: Invalid Gen Ed Area / Semester combination selected!";
                }
            }
            else if(reportKind.equals("Reflection")){
                String genNumSent = (String) value;
                mySelectedGenEdArea = myGenEdAreaList.retrieve(genNumSent);

                boolean validAT = checkForValidAssessmentTeam();
                if (validAT) {

                    myInstructorReflections = new InstructorReflectionsCollection();
                    String atID = (String) mySelectedAT.getState("ID");
                    myInstructorReflections.findByAssessmentTeamID(atID);

                    myIRDiplayList = new InstructorReflectionsDisplayCollection(myInstructorReflections);

                    Scene s = createInstructorReflectionsCollectionView();
                    swapToView(s);
                } else {
                    transactionErrorMessage = "ERROR: Invalid Gen Ed Area / Semester combination selected!";
                }
            }
            else if(reportKind.equals("ATData")){
                String genNumSent = (String) value;
                mySelectedGenEdArea = myGenEdAreaList.retrieve(genNumSent);

                boolean validAT = checkForValidAssessmentTeam();
                if (validAT) {

                    myAssessmentTeamClasses = new AssessmentTeamClassesCollection();
                    String atID = (String) mySelectedAT.getState("ID");
                    myAssessmentTeamClasses.findByAssessmentTeamID(atID);

                    Scene s = createAssessmentTeamClassesCollectionView();
                    swapToView(s);
                } else {
                    transactionErrorMessage = "ERROR: Invalid Gen Ed Area / Semester combination selected!";
                }
            }
        }
        else if(key.equals("UpdateStudentCategorization")){
            String atID = (String)mySelectedAT.getState("ID");
            if(((String)value).equals("All")){
                //System.out.println("Testing");
                myStudentCats.findByAssessmentTeam(atID);
            }
            else{
                myStudentCats.findByAssessmentTeamAndStudentLevel((String)value,atID);
            }
        }

        myRegistry.updateSubscribers(key, this);
    }

    //------------------------------------------------------
    protected boolean checkForValidAssessmentTeam(){
        String semID = (String)mySelectedSemester.getState("ID");
        String genEdID = (String)mySelectedGenEdArea.getState("ID");
        //System.out.println("SemID: " + semID + ", GenEdId: " + genEdID);

        try{
            mySelectedAT = new AssessmentTeam(genEdID, semID);
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
        Scene currentScene = myViews.get("ReportGeneratorView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("ReportGeneratorView", this);
            currentScene = new Scene(newView);
            myViews.put("ReportGeneratorView", currentScene);

            return currentScene;
        }
        else
        {
            return currentScene;
        }
    }

    //------------------------------------------------------
    protected Scene createStudentCategorizationDisplayCollectionView() {
        Scene currentScene = myViews.get("StudentCategorizationDisplayCollectionView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("StudentCategorizationDisplayCollectionView", this);
            currentScene = new Scene(newView);
            myViews.put("StudentCategorizationDisplayCollectionView", currentScene);

            return currentScene;
        }
        else
        {
            return currentScene;
        }
    }

    //------------------------------------------------------
    protected Scene createInstructorReflectionsCollectionView() {
        Scene currentScene = myViews.get("InstructorReflectionsCollectionView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("InstructorReflectionsCollectionView", this);
            currentScene = new Scene(newView);
            myViews.put("InstructorReflectionsCollectionView", currentScene);

            return currentScene;
        }
        else
        {
            return currentScene;
        }
    }

    //------------------------------------------------------
    protected Scene createAssessmentTeamClassesCollectionView() {
        Scene currentScene = myViews.get("AssessmentTeamClassesCollectionView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("AssessmentTeamClassesCollectionView", this);
            currentScene = new Scene(newView);
            myViews.put("AssessmentTeamClassesCollectionView", currentScene);

            return currentScene;
        }
        else
        {
            return currentScene;
        }
    }

    //------------------------------------------------------
    protected Scene createSearchSemesterView() {
        Scene currentScene = myViews.get("SearchSemesterView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("SearchSemesterView", this);
            currentScene = new Scene(newView);
            myViews.put("SearchSemesterView", currentScene);

            return currentScene;
        }
        else
        {
            return currentScene;
        }
    }

    //------------------------------------------------------
    protected Scene createSemesterCollectionView() {
        Scene currentScene = myViews.get("SemesterCollectionView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("SemesterCollectionView", this);
            currentScene = new Scene(newView);
            myViews.put("SemesterCollectionView", currentScene);

            return currentScene;
        }
        else
        {
            return currentScene;
        }
    }

    //------------------------------------------------------
    protected Scene createSearchGenEdAreaView() {
        Scene currentScene = myViews.get("SearchGenEdAreaView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("SearchGenEdAreaView", this);
            currentScene = new Scene(newView);
            myViews.put("SearchGenEdAreaView", currentScene);

            return currentScene;
        }
        else
        {
            return currentScene;
        }
    }

    //------------------------------------------------------
    protected Scene createGenEdAreaCollectionView() {
        Scene currentScene = myViews.get("GenEdAreaCollectionView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("GenEdAreaCollectionView", this);
            currentScene = new Scene(newView);
            myViews.put("GenEdAreaCollectionView", currentScene);

            return currentScene;
        }
        else
        {
            return currentScene;
        }
    }
}