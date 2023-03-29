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
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;


// project imports
import exception.InvalidPrimaryKeyException;
import exception.MultiplePrimaryKeysException;
import event.Event;
import database.*;

import impresario.IView;

import userinterface.View;
import userinterface.ViewFactory;

/** The class containing the Translations String collection information for the
 * internationalizable Gen Ed Data Management application. The collection is held
 * in ONE //Properties object
 */
//==============================================================
public class AllTranslationStrings extends EntityBase implements IView
{
    private static String myTableName = "Translations_en_US";

    private Properties allTranslatedStrings;

    // GUI Components


    // constructor for this class
    //----------------------------------------------------------
    public AllTranslationStrings()
    {
        super(myTableName);
    }

    //----------------------------------------------------------
    public void populate() throws Exception
    {
        String query = "SELECT * FROM " + myTableName;

        Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

        // You must get one article type at least
        if (allDataRetrieved != null)
        {
            allTranslatedStrings = new Properties();
            for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
            {

                Properties nextTranslatedString =
                        (Properties)allDataRetrieved.elementAt(cnt);
                Enumeration allKeys = nextTranslatedString.propertyNames();
                while (allKeys.hasMoreElements() == true)
                {
                    String nextKey = (String)allKeys.nextElement();
                    String nextValue = nextTranslatedString.getProperty(nextKey);

                    if (nextValue != null)
                    {
                        allTranslatedStrings.setProperty(nextKey, nextValue);
                    }
                }

            }
        }
        else
        {
            throw new InvalidPrimaryKeyException("No Translation Strings found.");
        }
    }


    //----------------------------------------------------------
    public Object getState(String key)
    {
        String retVal = "ERR_Unknown";
        if (key.startsWith("LBL_") == true)
            retVal = persistentState.getProperty(key);

        return retVal;
    }

    //------------------------------------------------------------
    public String getTranslatedString(String key)
    {
        return (String)getState(key);
    }

    //----------------------------------------------------------------
    public void stateChangeRequest(String key, Object value)
    {

        myRegistry.updateSubscribers(key, this);
    }

    /** Called via the IView relationship */
    //----------------------------------------------------------
    public void updateState(String key, Object value)
    {
        stateChangeRequest(key, value);
    }

    //-----------------------------------------------------------------------------------
    public void setTableNameForLocale(String locale)
    {
        String newTableName = "Translations_" + locale;
        myTableName = newTableName;
        initializeSchema(newTableName);
    }

    //-----------------------------------------------------------------------------------
    protected void initializeSchema(String tableName)
    {
        if (mySchema == null)
        {
            mySchema = getSchemaInfo(tableName);
        }
    }
}
