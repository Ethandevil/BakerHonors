// tabs=4
//************************************************************
//	COPYRIGHT 2021, Ethan L. Baker, Matthew E. Morgan and 
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

import javax.swing.filechooser.FileFilter;

import java.io.*;

public class CSVFileFilter extends FileFilter
{

    //-------------------------------------------
    public boolean accept(File f)
    {
        String fileName = f.getName();
        
        if ((fileName.endsWith(".csv")) || (fileName.endsWith(".CSV")) || (f.isDirectory()))
            return true;
        
        else
            return false;
    }
    
    //-------------------------------------------
    public String getDescription()
    {
        return "CSV File Filter";
    }
}