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
import java.util.Vector;

/** The class containing the Instructor Reflections Display information for the Gen Ed Assessment Data Management
 * application
 */
//==============================================================
public class InstructorReflectionsDisplay {
    //
    private String questionText;
    private String reflectionText;

    // constructor for this class
    //----------------------------------------------------------
    public InstructorReflectionsDisplay(String qstnText,
                                 String rflnText) {

        questionText = qstnText;
        reflectionText = rflnText;
    }

    //--------------------------------------------------------
    public String getReflectionText()
    {
        return reflectionText;
    }

    //--------------------------------------------------------
    public String getQuestionText()
    {
        return questionText;
    }

    //-----------------------------------------------------------------------------------
    public static int compare(InstructorReflectionsDisplay a, InstructorReflectionsDisplay b)
    {
        String aVal = a.getReflectionText();
        String bVal = b.getReflectionText();
        return aVal.compareTo(bVal);
    }


    /**
     * This method is needed solely to enable the Assessment Team Display information to be displayable in a table
     *
     */
    //--------------------------------------------------------------------------
    public Vector<String> getEntryListView()
    {
        Vector<String> v = new Vector<String>();

        v.add(questionText);
        v.add(reflectionText);
        return v;
    }
}
