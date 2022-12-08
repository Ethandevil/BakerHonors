package userinterface;

import impresario.IModel;
//==============================================================================

public class AssessmentTeamDisplayCollectionForUpdatingAssessmentTeamClassesView extends AssessmentTeamDisplayCollectionView{

        //--------------------------------------------------------------------------
        public AssessmentTeamDisplayCollectionForUpdatingAssessmentTeamClassesView(IModel modt)
        {
            super(modt);

        }

        //---------------------------------------------------------
        protected String getPromptText() {
            return "Select the semester for modifying a course:";
        }



    }


