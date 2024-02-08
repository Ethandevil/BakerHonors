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
import java.util.ArrayList;
import java.util.Vector;
import java.util.Properties;

/** The class containing the Student Categorization Display information for the Gen Ed Data Management
 * application
 */
//==============================================================
public class StudentCategorizationDisplay
{

    //
    private ArrayList<String> percentageBySLO;
    private String performanceCategoryName;

    // constructor for this class
    //----------------------------------------------------------
    public StudentCategorizationDisplay(String performanceCategoryName, ArrayList<String> values) {
        this.performanceCategoryName = performanceCategoryName;
        percentageBySLO = values;
    }

    //-----------------------------------------------------------------------------------
    public StudentCategorizationDisplay(){
        percentageBySLO = new ArrayList<>();
        performanceCategoryName = "";
    }

    //-----------------------------------------------------------------------------------
    public String getPerformanceCategoryName() {
        return performanceCategoryName;
    }

    //-----------------------------------------------------------------------------------
    public String getPercentage(int index) {
        if((index >= 0) && (index < percentageBySLO.size())){
            return percentageBySLO.get(index);
        }
        else{
            return "-1";
        }
    }

    //-----------------------------------------------------------------------------------
    public void setPerformanceCategoryName(String name){performanceCategoryName = name;}

    //-----------------------------------------------------------------------------------
    public void setPercentageBySLO(int index, String value){
        //System.out.println("size: " + percentageBySLO.size() + " index: " + index);
        if(percentageBySLO.size() <= index){
            percentageBySLO.add(value);
        }
        else{
            percentageBySLO.set(index - 1, value);
        }
    }

    /**
     * This method is needed solely to enable the Student Categorization Display information to be displayable in a table
     *
     */
    //--------------------------------------------------------------------------
    public Vector<String> getEntryListView()
    {
        Vector<String> v = new Vector<String>();

        v.add(performanceCategoryName);
        for(int i = 0; i < percentageBySLO.size(); i++){
            v.add(getPercentage(i));
        }

        return v;
    }

    //--------------------------------------------------------------------------
    public String toString(){
        String retVal = "";
        retVal += performanceCategoryName + ": ";
        for(int i = 0; i < percentageBySLO.size(); i++){
            retVal += percentageBySLO.get(i) + " ";
        }
        return retVal;
    }

}