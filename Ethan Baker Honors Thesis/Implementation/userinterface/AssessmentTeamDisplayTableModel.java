// tabs=4
//************************************************************
//	COPYRIGHT 2021, Kyle D. Adams, Matthew E. Morgan and 
//   Sandeep Mitra, State University of New York. - Brockport
//   (SUNY Brockport) 
//	ALL RIGHTS RESERVED
//
// This file is the product of SUNY Brockport and cannot 
// be reproduced, copied, or used in any shape or form without 
// the express written consent of SUNY Brockport.
//************************************************************
//
// specify the package

package userinterface;

import java.util.Vector;

import javafx.beans.property.SimpleStringProperty;

//==============================================================================
public class AssessmentTeamDisplayTableModel
{
	private final SimpleStringProperty assessmentTeamId;
	private final SimpleStringProperty areaName;
	private final SimpleStringProperty semAndYear;

	//----------------------------------------------------------------------------
	public AssessmentTeamDisplayTableModel(Vector<String> odData)
	{
		assessmentTeamId =  new SimpleStringProperty(odData.elementAt(0));
		areaName =  new SimpleStringProperty(odData.elementAt(1));
		semAndYear =  new SimpleStringProperty(odData.elementAt(2));
		
	}

	//----------------------------------------------------------------------------
	public String getAssessmentTeamId() {
		return assessmentTeamId.get();
	}

	//----------------------------------------------------------------------------
	public void setAssessmentTeamId(String id) {
		assessmentTeamId.set(id);
	}

	//----------------------------------------------------------------------------
	public String getGenEdAreaName() {
		return areaName.get();
	}

	//----------------------------------------------------------------------------
	public void setGenEdAreaName(String nm) {
		areaName.set(nm);
	}
	
	//----------------------------------------------------------------------------
	public String getSemAndYear() {
		return semAndYear.get();
	}

	//----------------------------------------------------------------------------
	public void setSemAndYear(String mtkyl) {
		semAndYear.set(mtkyl);
	}

	

	
}
