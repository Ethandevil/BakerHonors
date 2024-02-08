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
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;
import javafx.scene.Scene;

// project imports

import impresario.IView;

import userinterface.View;
import userinterface.ViewFactory;


/** The class containing the Student Categorization Collection for the Gen Ed Data
 * Management application
 */
//==============================================================
public class StudentCategorizationDisplayCollection  extends EntityBase implements IView
{
    private static final String myTableName = "StudentCategorization";

    private StudentCategorizationDisplay cat1StudentCats;
    private StudentCategorizationDisplay cat2StudentCats;
    private StudentCategorizationDisplay cat3StudentCats;
    private StudentCategorizationDisplay cat4StudentCats;
    private StudentCategorizationDisplay cat3AndCat4StudentCats;
    private PerformanceCategoryCollection allCats;
    // GUI Components

    // constructor for this class
    //----------------------------------------------------------
    public StudentCategorizationDisplayCollection( )
    {
        super(myTableName);
        cat1StudentCats = new StudentCategorizationDisplay();
        cat2StudentCats = new StudentCategorizationDisplay();
        cat3StudentCats = new StudentCategorizationDisplay();
        cat4StudentCats = new StudentCategorizationDisplay();
        cat3AndCat4StudentCats = new StudentCategorizationDisplay();
        allCats = new PerformanceCategoryCollection();
        allCats.findAll();
    }

    //-----------------------------------------------------------
    private void populateCollectionHelper(String query)
    {

        ArrayList<ArrayList<String>> fullMatrix = new ArrayList<>();
        Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

        if (allDataRetrieved != null)
        {
            for(int i = 0; i < allDataRetrieved.size(); i++){
                Properties p = allDataRetrieved.get(i);
                Enumeration allKeys = p.propertyNames();
                String sloNum = "0";
                String cat1Value = "";
                String cat2Value = "";
                String cat3Value = "";
                String cat4Value = "";
                String cat3AndCat4Value = "";
                while(allKeys.hasMoreElements()) {
                    String nextKey = (String) allKeys.nextElement();
                    String nextValue = p.getProperty(nextKey);
                    if (nextKey.startsWith("SLOID")) {
                        sloNum = nextValue;
                    } else if (nextKey.startsWith("Cat1")) {
                        cat1Value = nextValue;
                    } else if (nextKey.startsWith("Cat2")) {
                        cat2Value = nextValue;
                    } else if (nextKey.startsWith("Cat3")) {
                        cat3Value = nextValue;
                    } else if (nextKey.startsWith("Cat4")) {
                        cat4Value = nextValue;
                    }
                }
                //System.out.println(cat1Value + " " + cat2Value + " " + cat3Value + " " + cat4Value);
                int totalVal = Integer.parseInt(cat1Value) + Integer.parseInt(cat2Value) + Integer.parseInt(cat3Value) + Integer.parseInt(cat4Value);
                int cat3AndCat4Val = Integer.parseInt(cat3Value) + Integer.parseInt(cat4Value);
                double cat1Percent = (Integer.parseInt(cat1Value) * 100.0) / totalVal;
                double cat2Percent = (Integer.parseInt(cat2Value) * 100.0) / totalVal;
                double cat3Percent = (Integer.parseInt(cat3Value) * 100.0) / totalVal;
                double cat4Percent = (Integer.parseInt(cat4Value) * 100.0) / totalVal;
                double cat3AndCat4Percent = cat3Percent + cat4Percent;
                NumberFormat formatter = new DecimalFormat("#0.00");
                cat1Value = cat1Value + " (" + formatter.format(cat1Percent) + "%) ";
                cat2Value = cat2Value + " (" + formatter.format(cat2Percent) + "%) ";
                cat3Value = cat3Value + " (" + formatter.format(cat3Percent) + "%) ";
                cat4Value = cat4Value + " (" + formatter.format(cat4Percent) + "%) ";
                cat3AndCat4Value = cat3AndCat4Val + " (" + formatter.format(cat3AndCat4Percent) + "%) ";
                //System.out.println(cat1Value + " " + cat2Value + " " + cat3Value + " " + cat4Value);
                ArrayList<String> valueSet = new ArrayList<>();
                valueSet.add(sloNum);
                valueSet.add(cat4Value);
                valueSet.add(cat3Value);
                valueSet.add(cat2Value);
                valueSet.add(cat1Value);
                valueSet.add(cat3AndCat4Value);
                fullMatrix.add(valueSet);
                //for(int j = 0; j < valueSet.size(); j++) System.out.println(valueSet.get(j) + " ");
                //System.out.println(nextKey + " = " + p.getProperty(nextKey));
                //System.out.println("----");
            }

            //do something else to get us cat1StudentCats - cat4StudentCats
            cat1StudentCats = new StudentCategorizationDisplay();
            cat2StudentCats = new StudentCategorizationDisplay();
            cat3StudentCats = new StudentCategorizationDisplay();
            cat4StudentCats = new StudentCategorizationDisplay();
            cat3AndCat4StudentCats = new StudentCategorizationDisplay();
            String cat4Name = (String)(((PerformanceCategory)allCats.retrieve("4")).getState("Name"));
            String cat3Name = (String)(((PerformanceCategory)allCats.retrieve("3")).getState("Name"));
            String cat2Name = (String)(((PerformanceCategory)allCats.retrieve("2")).getState("Name"));
            String cat1Name = (String)(((PerformanceCategory)allCats.retrieve("1")).getState("Name"));
            cat4StudentCats.setPerformanceCategoryName(cat4Name);
            cat3StudentCats.setPerformanceCategoryName(cat3Name);
            cat2StudentCats.setPerformanceCategoryName(cat2Name);
            cat1StudentCats.setPerformanceCategoryName(cat1Name);
            for(int i = 0; i < fullMatrix.size(); i++){
                ArrayList<String> temp = fullMatrix.get(i);
                cat4StudentCats.setPercentageBySLO(i + 1,temp.get(1));
                cat3StudentCats.setPercentageBySLO(i + 1,temp.get(2));
                cat2StudentCats.setPercentageBySLO(i + 1,temp.get(3));
                cat1StudentCats.setPercentageBySLO(i + 1,temp.get(4));
                //for(int j = 0; j < temp.size(); j++) System.out.println(temp.get(j));
                //System.out.println("-*-*-*-*-*-*-");
            }
            cat3AndCat4StudentCats.setPerformanceCategoryName(cat4Name + " and " + cat3Name);
            for(int i = 0; i < fullMatrix.size(); i++){
                ArrayList<String> temp = fullMatrix.get(i);
                cat3AndCat4StudentCats.setPercentageBySLO(i + 1, temp.get(5));
            }
            /*System.out.println(cat1StudentCats);
            System.out.println(cat2StudentCats);
            System.out.println(cat3StudentCats);
            System.out.println(cat4StudentCats);
            System.out.println(cat3AndCat4StudentCats);*/
        }
    }

    //-----------------------------------------------------------
    public void findByAssessmentTeamAndStudentLevel(String studentLevel, String atID){
        String query = "SELECT SLOID, Cat1Number, Cat2Number, Cat3Number, Cat4Number FROM " + myTableName +
                " WHERE ((AssessmentTeamID = " + atID + ") AND (StudentLevel = '" + studentLevel +
                "')) ORDER BY SLOID";
        //System.out.println(query);
        populateCollectionHelper(query);
    }

    //-----------------------------------------------------------
    public void findByAssessmentTeam(String atID){
        String query = "SELECT SLOID, SUM(Cat1Number) AS Cat1Number, SUM(Cat2Number) AS Cat2Number," +
                " SUM(Cat3Number) AS Cat3Number, SUM(Cat4Number) AS Cat4Number FROM " +
                myTableName + " WHERE (AssessmentTeamID = " + atID + ") GROUP BY SLOID ORDER BY SLOID";
        populateCollectionHelper(query);
    }

    /**
     *
     */
    //----------------------------------------------------------
    public Object getState(String key)
    {
        if (key.equals("StudentCategorization1"))
            return cat1StudentCats;
        else
        if (key.equals("StudentCategorization2"))
            return cat2StudentCats;
        else
        if (key.equals("StudentCategorization3"))
            return cat3StudentCats;
        else
        if (key.equals("StudentCategorization4"))
            return cat4StudentCats;
        else if(key.equals("StudentCategorization3And4")){
            return cat3AndCat4StudentCats;
        }
        else
        if (key.equals("StudentCategorizationList"))
            return this;
        return null;
    }

    //----------------------------------------------------------------
    public void stateChangeRequest(String key, Object value)
    {
        myRegistry.updateSubscribers(key, this);
    }


    /** Called via the IView relationship */
    //----------------------------------------------------------
    public void updateState(String key, Object value)
    {
        stateChangeRequest(key, value);
    }


    //-----------------------------------------------------------------------------------
    protected void initializeSchema(String tableName)
    {
        if (mySchema == null)
        {
            mySchema = getSchemaInfo(tableName);
        }
    }
}
