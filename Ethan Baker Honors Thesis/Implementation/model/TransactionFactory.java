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


// project imports

/** The class containing the TransactionFactory for the ISLO Data Management application 
 */
//==============================================================
public class TransactionFactory
{

	/**
	 *
	 */
	//----------------------------------------------------------
	public static Transaction createTransaction(String transType)
		throws Exception
	{
		Transaction retValue = null;

		if (transType.equals("AddNewGenEdArea") == true)
		{
			retValue = new AddGenEdAreaTransaction();
		}
		else
		if (transType.equals("UpdateGenEdArea") == true)
		{
			retValue = new ModifyGenEdAreaTransaction();
		}
		else if(transType.equals("DeleteGenEdArea") == true){
			retValue = new DeleteGenEdAreaTransaction();
		}
		else if(transType.equals("AddNewGESLO")){
			retValue = new AddGenEdAreaSLOTransaction();
		}
		else if(transType.equals("UpdateGESLO")){
			retValue = new ModifyGenEdAreaSLOTransaction();
		}
		else
		if (transType.equals("AddNewSemester") == true)
		{
			retValue = new AddSemesterTransaction();
		}
		else
		if (transType.equals("UpdateSemester") == true)
		{
			retValue = new UpdateSemesterTransaction();
		}
		else
		if (transType.equals("AddNewAssessmentTeam") == true)
		{
			retValue = new AddAssessmentTeamTransaction();
		}
		else
		if (transType.equals("UpdateAssessmentTeam") == true)
		{
			retValue = new ModifyAssessmentTeamTransaction();
		}
		else
		if (transType.equals("AddNewCategoryName") == true)
		{
			retValue = new AddPerformanceCategoryTransaction();
		}
		else
		if (transType.equals("UpdateCategoryName") == true)
		{
			retValue = new UpdatePerformanceCategoryTransaction();
		}
		else if (transType.equals("AddNewReflectionQuestion")){
			retValue = new AddReflectionQuestionTransaction();
		}
		else if(transType.equals("UpdateReflectionQuestion")){
			retValue = new ModifyReflectionQuestionTransaction();
		}
		else
		if (transType.equals("AddCourseToTeam") == true)
		{
			retValue = new AddAssessmentTeamClassesTransaction();
		}
		else
		if (transType.equals("UpdateCourseInTeam") == true)
		{
			retValue = new ModifyAssessmentTeamClassesTransaction();
		}
		else
		if (transType.equals("DeleteCourseInTeam") == true)
		{
			retValue = new DeleteAssessmentTeamClassesTransaction();
		}
		else
		if (transType.equals("AddNewStudentCategorization") == true)
		{
			retValue = new AddStudentCategorizationReflectionTransaction();
		}/*
		else
		if (transType.equals("UpdateStudentCategorization") == true)
		{
			retValue = new UpdateStudentCategorizationTransaction();
		}
		else
		if (transType.equals("Reports") == true)
		{
			retValue = new ReportGeneratorTransaction();
		} */
		return retValue;
	}
}
