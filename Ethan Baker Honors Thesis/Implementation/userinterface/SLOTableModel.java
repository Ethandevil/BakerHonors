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
public class SLOTableModel
{
    private final SimpleStringProperty sloID;
    private final SimpleStringProperty genEdAreaID;
    private final SimpleStringProperty sloText;
    private final SimpleStringProperty notes;

    //----------------------------------------------------------------------------
    public SLOTableModel(Vector<String> SLOData)
    {
        sloID =  new SimpleStringProperty(SLOData.elementAt(0));
        genEdAreaID =  new SimpleStringProperty(SLOData.elementAt(1));
        sloText =  new SimpleStringProperty(SLOData.elementAt(2));
        notes =  new SimpleStringProperty(SLOData.elementAt(3));
    }

    //----------------------------------------------------------------------------
    public String getSloID() {
        return sloID.get();
    }

    //----------------------------------------------------------------------------
    public void setSloID(String num) {
        sloID.set(num);
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
    public String getSloText() {
        return sloText.get();
    }

    //----------------------------------------------------------------------------
    public void setSloText(String text) {
        sloText.set(text);
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
