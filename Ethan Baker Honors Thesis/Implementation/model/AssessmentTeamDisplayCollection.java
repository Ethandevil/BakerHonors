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

// project imports
import event.Event;

/** The class containing the Assessment Team Display Collection for the Gen Ed Assessment Data Management
 * application
 */
//==============================================================
public class AssessmentTeamDisplayCollection
{

    //
	private Vector<AssessmentTeamDisplay> atdisps;

    // constructor for this class
    //----------------------------------------------------------
    public AssessmentTeamDisplayCollection() {
		
		atdisps = new Vector<AssessmentTeamDisplay>();
    }

	//---------------------------------------------------------
	public AssessmentTeamDisplayCollection(AssessmentTeamCollection oc)
	{
		atdisps = new Vector<AssessmentTeamDisplay>();
		Vector<AssessmentTeam> currAssessmentTeams = (Vector)oc.getState("AssessmentTeams");
		for (int cnt = 0; cnt < currAssessmentTeams.size(); cnt++)
		{
			AssessmentTeam nextAssessmentTeam = currAssessmentTeams.get(cnt);
			String offId = (String)nextAssessmentTeam.getState("ID");
			String areaId = (String)nextAssessmentTeam.getState("GenEdAreaID");
			String semId = (String)nextAssessmentTeam.getState("SemesterID");
			try {
				GenEdArea nextGenEdArea = new GenEdArea(areaId);
				String areaNm = nextGenEdArea.getName();
				Semester nextSem = new Semester(semId);
				String semName = nextSem.getSemester();
				String semYr = nextSem.getYear();
				AssessmentTeamDisplay od = new AssessmentTeamDisplay(offId, semName, semYr, areaNm);
				addAssessmentTeamDisplay(od);
			}
			catch (Exception ex) {
				new Event(Event.getLeafLevelClassName(this), "<init>",
					"Unexplained error: Gen Ed Area or Semester IDs not found in database!", Event.ERROR);
			}
		}
	}
	
	//--------------------------------------------------------
	public void addAssessmentTeamDisplay(AssessmentTeamDisplay od)
	{
		int index = findIndexToAdd(od);
		atdisps.insertElementAt(od,index); // To build up a collection sorted on some key
	}

	//----------------------------------------------------------------------------------
	private int findIndexToAdd(AssessmentTeamDisplay atd)
	{
		int low=0;
		int high = atdisps.size()-1;
		int middle;

		while (low <=high)
		{
			middle = (low+high)/2;

			AssessmentTeamDisplay midSession = atdisps.elementAt(middle);

			int result = AssessmentTeamDisplay.compare(atd,midSession);
			if (result ==0)
			{
				return middle;
			}
			else if (result<0)
			{
				high=middle-1;
			}
			else
			{
				low=middle+1;
			}
		}
		return low;
	}


	//-----------------------------------------------------------
	public AssessmentTeamDisplay retrieve(int index) {
	
		if ((index >= 0) && (index < atdisps.size())) {
			return atdisps.elementAt(index);
		}
		else {
			return null;
		}
	}

	//-----------------------------------------------------------
	public int getSize() {
		if (atdisps != null) return atdisps.size();
			else
		return 0;
	}

	//---------------------------------------------------------
	public Vector getAssessmentTeamDisplays() {
		return atdisps;
	}
    

}

