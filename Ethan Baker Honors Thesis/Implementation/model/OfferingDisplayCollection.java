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

/** The class containing the Offering Display Collection for the Gen Ed Assessment Data Management
 * application
 */
//==============================================================
public class OfferingDisplayCollection
{

    //
	private Vector<OfferingDisplay> odisps;

    // constructor for this class
    //----------------------------------------------------------
    public OfferingDisplayCollection() {
		
		odisps = new Vector<OfferingDisplay>();
    }

	//---------------------------------------------------------
	public OfferingDisplayCollection(OfferingCollection oc)
	{
		odisps = new Vector<OfferingDisplay>();
		Vector<Offering> currOfferings = (Vector)oc.getState("Offerings");
		for (int cnt = 0; cnt < currOfferings.size(); cnt++)
		{
			Offering nextOffering = currOfferings.get(cnt);
			String offId = (String)nextOffering.getState("ID");
			String areaId = (String)nextOffering.getState("GenEdAreaID");
			String semId = (String)nextOffering.getState("SemesterID");
			try {
				GenEdArea nextGenEdArea = new GenEdArea(areaId);
				String areaNm = nextGenEdArea.getName();
				Semester nextSem = new Semester(semId);
				String semName = nextSem.getSemester();
				String semYr = nextSem.getYear();
				OfferingDisplay od = new OfferingDisplay(offId, semName, semYr, areaNm);
				addOfferingDisplay(od);
			}
			catch (Exception ex) {
				new Event(Event.getLeafLevelClassName(this), "<init>",
					"Unexplained error: Gen Ed Area or Semester IDs not found in database!", Event.ERROR);
			}
		}
	}
	
	//--------------------------------------------------------
	public void addOfferingDisplay(OfferingDisplay od)
	{
		int index = findIndexToAdd(od);
		odisps.insertElementAt(od,index); // To build up a collection sorted on some key
	}

	//----------------------------------------------------------------------------------
	private int findIndexToAdd(OfferingDisplay od)
	{
		int low=0;
		int high = odisps.size()-1;
		int middle;

		while (low <=high)
		{
			middle = (low+high)/2;

			OfferingDisplay midSession = odisps.elementAt(middle);

			int result = OfferingDisplay.compare(od,midSession);

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
	public OfferingDisplay retrieve(int index) {
	
		if ((index >= 0) && (index < odisps.size())) {
			return odisps.elementAt(index);
		}
		else {
			return null;
		}
	}

	//-----------------------------------------------------------
	public int getSize() {
		if (odisps != null) return odisps.size();
			else
		return 0;
	}

	//---------------------------------------------------------
	public Vector getOfferingDisplays() {
		return odisps;
	}
    

}

