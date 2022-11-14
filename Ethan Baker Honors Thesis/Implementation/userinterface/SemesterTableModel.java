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
package userinterface;

import java.util.Vector;

import javafx.beans.property.SimpleStringProperty;

//==============================================================================
public class SemesterTableModel
{
	private final SimpleStringProperty id;
	private final SimpleStringProperty semName;
	private final SimpleStringProperty year;

	//----------------------------------------------------------------------------
	public SemesterTableModel(Vector<String> semData)
	{
		id =  new SimpleStringProperty(semData.elementAt(0));
		semName =  new SimpleStringProperty(semData.elementAt(1));
		year =  new SimpleStringProperty(semData.elementAt(2));
		
	}

	//----------------------------------------------------------------------------
	public String getId() {
		return id.get();
	}

	//----------------------------------------------------------------------------
	public void setId(String idv) {
		id.set(idv);
	}

	//----------------------------------------------------------------------------
	public String getSemName() {
		return semName.get();
	}

	//----------------------------------------------------------------------------
	public void setSemName(String nm) {
		semName.set(nm);
	}

	//----------------------------------------------------------------------------
	public String getYear() {
		return year.get();
	}

	//----------------------------------------------------------------------------
	public void setYear(String yr) {
		year.set(yr);
	}

	
}
