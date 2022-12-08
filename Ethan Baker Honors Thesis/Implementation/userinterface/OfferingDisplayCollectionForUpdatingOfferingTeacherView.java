package userinterface;

import impresario.IModel;
//==============================================================================

public class OfferingDisplayCollectionForUpdatingOfferingTeacherView extends OfferingDisplayCollectionView{

        //--------------------------------------------------------------------------
        public OfferingDisplayCollectionForUpdatingOfferingTeacherView(IModel modt)
        {
            // mdot - model - Modify Offering Display Transaction acronym
            super(modt);

        }

        //---------------------------------------------------------
        protected String getPromptText() {
            return "Select the semester for modifying a course/teacher:";
        }



    }


