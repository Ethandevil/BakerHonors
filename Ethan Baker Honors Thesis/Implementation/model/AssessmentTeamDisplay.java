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
import java.util.Vector;

/** The class containing the Assessment Team Display information for the Gen Ed Assessment Data Management
 * application
 */
//==============================================================
public class AssessmentTeamDisplay
{

    //
	private String assessmentTeamID;
	private String semName;
	private String year;
	private String areaName;

    // constructor for this class
    //----------------------------------------------------------
    public AssessmentTeamDisplay(String offrngID,
	String semNm, String yr, String areaNm) {
		
		assessmentTeamID = offrngID;
		semName = semNm;
		year = yr;
		areaName = areaNm;
    }

	//--------------------------------------------------------
	public String getGenEdAreaName()
	{
		return areaName;
	}

//-----------------------------------------------------------------------------------
    public static int compare(AssessmentTeamDisplay a, AssessmentTeamDisplay b)
    {
        String aVal = a.getGenEdAreaName();
        String bVal = b.getGenEdAreaName();
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

		v.add(assessmentTeamID);
		v.add(areaName);
		v.add(semName + " " + year);

        return v;
    }

}

