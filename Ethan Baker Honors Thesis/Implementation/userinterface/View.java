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

// system imports
import java.util.Properties;
import java.util.Vector;
import java.util.EventObject;
import javafx.scene.Group;

// project imports
import common.StringList;
import impresario.IView;
import impresario.IModel;
import impresario.IControl;
import impresario.ControlRegistry;
import java.awt.Component;
import java.awt.Desktop;
import java.io.File;
import javafx.stage.FileChooser;
import javax.swing.JOptionPane;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

//==============================================================
public abstract class View extends Group
        implements IView, IControl
{
    // private data
    protected IModel myModel;
    protected ControlRegistry myRegistry;


    // GUI components


    // Class constructor
    //----------------------------------------------------------
    public View(IModel model, String classname)
    {
        myModel = model;

        myRegistry = new ControlRegistry(classname);
    }


    //----------------------------------------------------------
    public void setRegistry(ControlRegistry registry)
    {
        myRegistry = registry;
    }

    // Allow models to register for state updates
    //----------------------------------------------------------
    public void subscribe(String key,  IModel subscriber)
    {
        myRegistry.subscribe(key, subscriber);
    }


    // Allow models to unregister for state updates
    //----------------------------------------------------------
    public void unSubscribe(String key, IModel subscriber)
    {
        myRegistry.unSubscribe(key, subscriber);
    }

    //-------------------------------------------------------------
    protected void writeToFile(String fName)
    {

    }

    //--------------------------------------------------------------------------
    protected void saveToExcelFile()
    {
        // Put up File Chooser
        // Retrieve full path name of file user selects
        // Create the file appropriately if it does not exist
        String reportsPath = System.getProperty("user.dir") + File.separator + "reports";
        File reportsDir = new File(reportsPath);

        // if the directory does not exist, create it
        if (!reportsDir.exists())
        {
            reportsDir.mkdir();
        }

        FileChooser chooser = new FileChooser();
        chooser.setInitialDirectory(reportsDir);
        chooser.setTitle("Saving report: ");

        //JFileChooser chooser = new JFileChooser("." + File.separator +
        //        "ReportFiles");

        chooser.getExtensionFilters().addAll(
                new ExtensionFilter("CSV Files", "*.csv"));

        try
        {
            String fileName = "";

            File selectedFile = chooser.showSaveDialog(MainStageContainer.getInstance());

            //selectedFile.mkdirs();
            fileName = selectedFile.getCanonicalPath();

            String tempName = fileName.toLowerCase();

            // Check if user specified the file type. If not, add the type.
            if(tempName.endsWith(".csv"))
            {
                writeToFile(fileName);
            }
            else
            {
                fileName += ".csv";
                writeToFile(fileName);
            }

            File reportPath = new File(fileName);
            if(reportPath.exists()) {
                Alert successPopUp = new Alert(AlertType.INFORMATION,
                        "Reports saved successfully in file: " + reportPath.getCanonicalPath());
                successPopUp.showAndWait();
            }
            else {
                Alert failurePopUp = new Alert(AlertType.ERROR, "Error in saving report");
                failurePopUp.showAndWait();
            }
        }
        catch (Exception ex)
        {
            //JOptionPane.showMessageDialog(null,);
            Alert alert = new Alert(AlertType.ERROR,  "Error in saving to file: " + ex.toString());
            alert.showAndWait();
        }
    }
}

