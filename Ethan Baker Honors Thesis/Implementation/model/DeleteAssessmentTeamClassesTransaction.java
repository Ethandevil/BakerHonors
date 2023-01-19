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

import exception.InvalidPrimaryKeyException;
import exception.MultiplePrimaryKeysException;
import javafx.scene.Scene;
import userinterface.View;
import userinterface.ViewFactory;

import java.util.Properties;

public class DeleteAssessmentTeamClassesTransaction extends ModifyAssessmentTeamClassesTransaction {

    public DeleteAssessmentTeamClassesTransaction() throws Exception {
        super();
		// DEBUG System.out.println("Delete Offering Teacher Transaction");
    }

    /**
     * Create the view for user to update data to for sampled course
     */
    protected Scene createModifyAssessmentTeamClassesView() {
		// DEBUG System.out.println("Delete Offering Teacher Trans: about to create delete offering teacher view");
        View newView = ViewFactory.createView("DeleteAssessmentTeamClassesView", this);
        Scene currentScene = new Scene(newView);

        return currentScene;

    }

    /**
     * Helper method for Offering Teacher update
     */
    //--------------------------------------------------------------------------
    protected void AssessmentTeamClassesModificationHelper(Properties props)
    {
        String atcNum = props.getProperty("CourseNumber");
        String atcCode = props.getProperty("CourseDisciplineCode");


        // Everything OK
        myAssessmentTeamClasses.stateChangeRequest("CourseNumber", atcNum);
        myAssessmentTeamClasses.stateChangeRequest("CourseDisciplineCode", atcCode);
        try {
            AssessmentTeamClasses someOT = new AssessmentTeamClasses(atcCode, atcNum);
            myAssessmentTeamClasses.remove();
            transactionErrorMessage = (String)myAssessmentTeamClasses.getState("UpdateStatusMessage");
        }
        catch (InvalidPrimaryKeyException excep) {

            transactionErrorMessage = "Sampled course with entered data does not exist for selected ISLO/Semester link";
        }
        catch (MultiplePrimaryKeysException excep2) {
            transactionErrorMessage = "Impossible: multiple sampled courses already exist for ISLO/Semester link";
        }

    }
}
