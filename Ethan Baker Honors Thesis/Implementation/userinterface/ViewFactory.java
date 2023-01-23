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

import impresario.IModel;
import model.AddGenEdAreaSLOTransaction;

//==============================================================================
public class ViewFactory {

	public static View createView(String viewName, IModel model)
	{

		if(viewName.equals("AssessmentCoordinatorView") == true)
		{
			return new AssessmentCoordinatorView(model);

		}
		else if(viewName.equals("AddGenEdAreaView") == true)
		{
			return new AddGenEdAreaView(model);
		}
		else if(viewName.equals("ModifyGenEdAreaView") == true)
		{
			return new ModifyGenEdAreaView(model);
		}
		else if(viewName.equals("GenEdAreaCollectionView") == true)
		{
			return new GenEdAreaCollectionView(model);
		}
		else if(viewName.equals("SearchGenEdAreaView") == true)
		{
			return new SearchGenEdAreaView(model);
		}
		else if(viewName.equals("DeleteGenEdAreaView")){
			return new DeleteGenEdAreaView(model);
		}
		else if(viewName.equals("AddGenEdAreaSLOView")){
			return new AddGenEdAreaSLOView(model);
		}
		else if(viewName.equals("ModifyGenEdAreaSLOView")){
			return new ModifyGenEdAreaSLOView(model);
		}
		else if(viewName.equals("SearchSLOView")){
			return new SearchSLOView(model);
		}
		else if(viewName.equals("SLOCollectionView")){
			return new SLOCollectionView(model);
		}
		else if(viewName.equals("AddSemesterView") == true)
		{
			return new AddSemesterView(model);
		}
		 else if(viewName.equals("ModifySemesterView") == true)
		{
			return new ModifySemesterView(model);
		}
		else if(viewName.equals("SearchSemesterView") == true)
		{
			return new SearchSemesterView(model);
		}
		else if(viewName.equals("SemesterCollectionView") == true)
		{
			return new SemesterCollectionView(model);
		}
		else if(viewName.equals("SearchSemesterForAssessmentTeamView") == true)
		{
			return new SearchSemesterForAssessmentTeamView(model);
		}
		else if(viewName.equals("SemesterCollectionForAssessmentTeamView") == true)
		{
			return new SemesterCollectionForAssessmentTeamView(model);
		}
		else if(viewName.equals("GenEdAreaCollectionForAssessmentTeamView") == true)
		{
			return new GenEdAreaCollectionForAssessmentTeamView(model);
		}
		else if(viewName.equals("SearchGenEdAreaForAssessmentTeamView") == true)
		{
			return new SearchGenEdAreaForAssessmentTeamView(model);
		}
		else if(viewName.equals("SearchGenEdAreaForModifyingAssessmentTeamView") == true)
		{
			return new SearchGenEdAreaForModifyingAssessmentTeamView(model);
		}
		else if(viewName.equals("GenEdAreaCollectionForModifyingAssessmentTeamView") == true)
		{
			return new GenEdAreaCollectionForModifyingAssessmentTeamView(model);
		}
		else if(viewName.equals("AssessmentTeamDisplayCollectionView") == true)
		{
			return new AssessmentTeamDisplayCollectionView(model);
		}
       	else if(viewName.equals("SemesterCollectionForModifyAssessmentTeamView") == true)
       	{
           		return new SemesterCollectionForModifyAssessmentTeamView(model);
	    }
		else if(viewName.equals("AddPerformanceCategoryView") == true)
		{
			return new AddPerformanceCategoryView(model);
		}
		else if(viewName.equals("PerformanceCategoryCollectionView") == true)
		{
			return new PerformanceCategoryCollectionView(model);
		}
		else if(viewName.equals("ModifyPerformanceCategoryView") == true)
		{
			return new ModifyPerformanceCategoryView(model);
		}
		else if(viewName.equals("SearchReflectionQuestionView") == true)
		{
			return new SearchReflectionQuestionView(model);
		}
		else if(viewName.equals("ReflectionQuestionCollectionView") == true)
		{
			return new ReflectionQuestionCollectionView(model);
		}
		else if (viewName.equals("AddReflectionQuestionView")){
			return new AddReflectionQuestionView(model);
		}
		else if(viewName.equals("ModifyReflectionQuestionView") == true)
		{
			return new ModifyReflectionQuestionView(model);
		}

        	else if(viewName.equals("SearchGenEdAreaForAssessmentTeamClassesView") == true)
		{
			return new SearchGenEdAreaForAssessmentTeamClassesView(model);
		}
		else if(viewName.equals("GenEdAreaCollectionForAssessmentTeamCoursesView") == true)
		{
			return new GenEdAreaCollectionForAssessmentTeamCoursesView(model);
		}
		else if(viewName.equals("AssessmentTeamDisplayCollectionForAssessmentTeamClassesView") == true)
		{
			return new AssessmentTeamDisplayCollectionForAssessmentTeamClassesView(model);
		}
		else if(viewName.equals("AddAssessmentTeamClassesView") == true)
		{
			return new AddAssessmentTeamClassesView(model);
		}
		else if(viewName.equals("AssessmentTeamDisplayCollectionForUpdatingAssessmentTeamClassesView") == true)
		{
			return new AssessmentTeamDisplayCollectionForUpdatingAssessmentTeamClassesView(model);
		}
		else if(viewName.equals("ModifyAssessmentTeamClassesView") == true)
		{
			return new ModifyAssessmentTeamClassesView(model);
		}
		else if(viewName.equals("AssessmentTeamClassesCollectionView") == true)
		{
			return new AssessmentTeamClassesCollectionView(model);
		}
		else if(viewName.equals("DeleteAssessmentTeamClassesView") == true)
		{
			return new DeleteAssessmentTeamClassesView(model);
		}/*
		else if(viewName.equals("SearchISLOForStudentCategorizationView") == true)
		{
			return new SearchISLOForStudentCategorizationView(model);
		}
		else if(viewName.equals("ISLOCollectionForStudentCategorizationView") == true)
		{
			return new ISLOCollectionForStudentCategorizationView(model);
		}
		else if(viewName.equals("OfferingDisplayCollectionForStudentCategorizationView") == true)
		{
			return new OfferingDisplayCollectionForStudentCategorizationView(model);
		}
		else if(viewName.equals("OfferingTeacherCollectionForStudentCategorizationView") == true)
		{
			return new OfferingTeacherCollectionForStudentCategorizationView(model);
		}
		else if(viewName.equals("AddStudentCategorizationView") == true)
		{
			return new AddStudentCategorizationView(model);
		}
		else if(viewName.equals("ModifyStudentCategorizationView") == true)
		{
			return new ModifyStudentCategorizationView(model);
		}
		else if(viewName.equals("OfferingDisplayCollectionForReportGenerationView") == true)
		{
			return new OfferingDisplayCollectionForReportGenerationView(model);
		}
		else if(viewName.equals("ReportGeneratorView") == true)
		{
			return new ReportGeneratorView(model);
		}
		else if(viewName.equals("StudentCategorizationDisplayCollectionView") == true)
		{
			return new StudentCategorizationDisplayCollectionView(model);
		}
		else */
		return null;
	}
}
