package userinterface;

import impresario.IModel;

public class ModifyReflectionView extends AddReflectionView{
    //--------------------------------------------------------------------------
    public ModifyReflectionView(IModel model)
    {
        super(model);
    }

    //--------------------------------------------------------------------------
    protected String getActionText()
    {
        return "Update Instructor Reflection: ";
    }
}
