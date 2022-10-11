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
    //private final SimpleStringProperty isloNumber;
    private final SimpleStringProperty genEdAreaName;
    private final SimpleStringProperty notes;

    //----------------------------------------------------------------------------
    public GenEdAreaTableModel(Vector<String> genEdAreaData)
    {
        //isloNumber =  new SimpleStringProperty(isloData.elementAt(0));
        genEdAreaName =  new SimpleStringProperty(genEdAreaData.elementAt(1));
        notes =  new SimpleStringProperty(genEdAreaData.elementAt(2));

    }

    //----------------------------------------------------------------------------
    /*public String getIsloNumber() {
        return isloNumber.get();
    }

    //----------------------------------------------------------------------------
    public void setIsloNumber(String num) {
        isloNumber.set(num);
    }*/

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
