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

		if (transType.equals("AddNewArea") == true)
		{
			retValue = new AddGenEdAreaTransaction();
		} /*
		else
		if (transType.equals("UpdateISLO") == true)
		{
			retValue = new UpdateISLOTransaction();
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
		if (transType.equals("AddNewOffering") == true)
		{
			retValue = new AddOfferingTransaction();
		}
		else
		if (transType.equals("UpdateOffering") == true)
		{
			retValue = new UpdateOfferingTransaction();
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
		else
		if (transType.equals("AddNewOfferingTeacher") == true)
		{
			retValue = new AddOfferingTeacherTransaction();
		}
		else
		if (transType.equals("UpdateOfferingTeacher") == true)
		{
			retValue = new UpdateOfferingTeacherTransaction();
		}
		else
		if (transType.equals("DeleteOfferingTeacher") == true)
		{
			retValue = new DeleteOfferingTeacherTransaction();
		}
		else
		if (transType.equals("AddNewStudentCategorization") == true)
		{
			retValue = new AddStudentCategorizationTransaction();
		}
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
