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
public class PerformanceCategoryTableModel
{
    private final SimpleStringProperty id;
    private final SimpleStringProperty catNumber;
    private final SimpleStringProperty catName;

    //----------------------------------------------------------------------------
    public PerformanceCategoryTableModel(Vector<String> catData)
    {
        id =  new SimpleStringProperty(catData.elementAt(0));
        catNumber =  new SimpleStringProperty(catData.elementAt(1));
        catName =  new SimpleStringProperty(catData.elementAt(2));

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
    public String getCatNumber() {
        return catNumber.get();
    }

    //----------------------------------------------------------------------------
    public void setCatNumber(String num) {
        catNumber.set(num);
    }

    //----------------------------------------------------------------------------
    public String getCatName() {
        return catName.get();
    }

    //----------------------------------------------------------------------------
    public void setCatName(String name) {
        catName.set(name);
    }




}
