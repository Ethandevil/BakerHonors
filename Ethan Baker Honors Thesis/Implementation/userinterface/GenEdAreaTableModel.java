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
public class GenEdAreaTableModel
{
    private final SimpleStringProperty genEdAreaID;
    private final SimpleStringProperty genEdAreaName;
    private final SimpleStringProperty notes;

    //----------------------------------------------------------------------------
    public GenEdAreaTableModel(Vector<String> genEdAreaData)
    {
        genEdAreaID =  new SimpleStringProperty(genEdAreaData.elementAt(0));
        genEdAreaName =  new SimpleStringProperty(genEdAreaData.elementAt(1));
        notes =  new SimpleStringProperty(genEdAreaData.elementAt(2));

    }

    //----------------------------------------------------------------------------
    public String getGenEdAreaID() {
        return genEdAreaID.get();
    }

    //----------------------------------------------------------------------------
    public void setGenEdAreaID(String num) {
        genEdAreaID.set(num);
    }

    //----------------------------------------------------------------------------
    public String getGenEdAreaName() {
        return genEdAreaName.get();
    }

    //----------------------------------------------------------------------------
    public void setGenEdAreaName(String nm) {
        genEdAreaName.set(nm);
    }

    //----------------------------------------------------------------------------
    public String getNotes() {
        return notes.get();
    }

    //----------------------------------------------------------------------------
    public void setNotes(String desc) {
        notes.set(desc);
    }


}
