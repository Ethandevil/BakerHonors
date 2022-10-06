package model;

// system imports
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;


// project imports
import exception.InvalidPrimaryKeyException;
import exception.MultiplePrimaryKeysException;
import database.*;

import impresario.IView;

import userinterface.View;
import userinterface.ViewFactory;

/** The class containing the ISLO information for the ISLO Data Management
 * application
 */
//==============================================================
public class GenEdArea extends EntityBase implements IView
{
    private static final String myTableName = "GenEdArea";

    protected Properties dependencies;

    // GUI Components
    private String updateStatusMessage = "";

    // constructor for this class
    //----------------------------------------------------------
    public GenEdArea(String GenEdAreaID) throws InvalidPrimaryKeyException, MultiplePrimaryKeysException
    {
        super(myTableName);

        setDependencies();

        GenEdAreaID = GenEdAreaID.trim();
        String query = "SELECT * FROM " + myTableName + " WHERE (ID = " + GenEdAreaID + ")";

        Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

        // You must get one article type at least
        if (allDataRetrieved != null)
        {
            int size = allDataRetrieved.size();
            // if size = 0 throw the Invalid Primary Key Exception
            if (size == 0)
            {
                throw new InvalidPrimaryKeyException("No Gen Ed Area matching Gen Ed ID : "
                        + GenEdAreaID + " found.");
            }
            else
                // There should be EXACTLY one ISLO. More than that is an error
                if (size != 1)
                {

                    throw new MultiplePrimaryKeysException("Multiple Gen Ed Areas matching Gen Ed Area ID : "
                            + GenEdAreaID + " found.");
                }
                else
                {
                    // copy all the retrieved data into persistent state
                    Properties retrievedISLOData = allDataRetrieved.elementAt(0);
                    persistentState = new Properties();

                    Enumeration allKeys = retrievedISLOData.propertyNames();
                    while (allKeys.hasMoreElements() == true)
                    {
                        String nextKey = (String)allKeys.nextElement();
                        String nextValue = retrievedISLOData.getProperty(nextKey);


                        if (nextValue != null)
                        {
                            persistentState.setProperty(nextKey, nextValue);
                        }
                    }

                }
        }
        // If ISLO found for this ISLO Number, throw an Invalid Primary key exception
        else
        {
            throw new InvalidPrimaryKeyException("No Gen Ed Area matching Gen Ed Area ID : "
                    + GenEdAreaID + " found.");
        }
    }

    public GenEdArea(String GenEdAreaName, boolean flag) throws InvalidPrimaryKeyException, MultiplePrimaryKeysException
    {
        super(myTableName);

        setDependencies();

        GenEdAreaName = GenEdAreaName.trim();
        String query = "SELECT * FROM " + myTableName + " WHERE (AreaName = '" + GenEdAreaName + "')";

        Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

        // You must get one article type at least
        if (allDataRetrieved != null)
        {
            int size = allDataRetrieved.size();
            // if size = 0 throw the Invalid Primary Key Exception
            if (size == 0)
            {
                throw new InvalidPrimaryKeyException("No Gen Ed Area matching Gen Ed Name : "
                        + GenEdAreaName + " found.");
            }
            else
                // There should be EXACTLY one ISLO. More than that is an error
                if (size != 1)
                {

                    throw new MultiplePrimaryKeysException("Multiple Gen Ed Areas matching Gen Ed Area Name : "
                            + GenEdAreaName + " found.");
                }
                else
                {
                    // copy all the retrieved data into persistent state
                    Properties retrievedISLOData = allDataRetrieved.elementAt(0);
                    persistentState = new Properties();

                    Enumeration allKeys = retrievedISLOData.propertyNames();
                    while (allKeys.hasMoreElements() == true)
                    {
                        String nextKey = (String)allKeys.nextElement();
                        String nextValue = retrievedISLOData.getProperty(nextKey);


                        if (nextValue != null)
                        {
                            persistentState.setProperty(nextKey, nextValue);
                        }
                    }

                }
        }
        // If ISLO found for this ISLO Number, throw an Invalid Primary key exception
        else
        {
            throw new InvalidPrimaryKeyException("No Gen Ed Area matching Gen Ed Area Name : "
                    + GenEdAreaName + " found.");
        }
    }



    /**
     * Alternate constructor. Can be used to create a NEW ISLO
     */
    //----------------------------------------------------------
    public GenEdArea(Properties props)
    {
        super(myTableName);

        setDependencies();
        persistentState = new Properties();
        Enumeration allKeys = props.propertyNames();
        while (allKeys.hasMoreElements() == true)
        {
            String nextKey = (String)allKeys.nextElement();
            String nextValue = props.getProperty(nextKey);

            if (nextValue != null)
            {
                persistentState.setProperty(nextKey, nextValue);
            }
        }
    }

    //-----------------------------------------------------------------------------------
    private void setDependencies()
    {
        dependencies = new Properties();

        myRegistry.setDependencies(dependencies);
    }

    //----------------------------------------------------------
    public Object getState(String key)
    {
        if (key.equals("UpdateStatusMessage") == true)
            return updateStatusMessage;

        return persistentState.getProperty(key);
    }

    //----------------------------------------------------------------
    public void stateChangeRequest(String key, Object value)
    {
        if (persistentState.getProperty(key) != null)
        {
            persistentState.setProperty(key, (String)value);
        }
        myRegistry.updateSubscribers(key, this);
    }

    /** Called via the IView relationship */
    //----------------------------------------------------------
    public void updateState(String key, Object value)
    {
        stateChangeRequest(key, value);
    }


    //-----------------------------------------------------------------------------------
    public static int compare(GenEdArea a, GenEdArea b)
    {
        String aVal = (String)a.getState("AreaName");
        String bVal = (String)b.getState("AreaName");

        return aVal.compareTo(bVal);
    }

    //-----------------------------------------------------------------------------------
    public void update()
    {
        updateStateInDatabase();
    }
    public void remove(){
        removeStateInDatabase();
    }
    //-----------------------------------------------------------------------------------
    private void updateStateInDatabase()
    {
        try
        {
            if (persistentState.getProperty("ID") != null)
            {
                Properties whereClause = new Properties();
                whereClause.setProperty("ID", persistentState.getProperty("ID"));
                updatePersistentState(mySchema, persistentState, whereClause);
                updateStatusMessage = "Gen Ed Area : " + persistentState.getProperty("AreaName") + " updated successfully!";
            }
            else
            {
                Integer ISLOID =
                        insertAutoIncrementalPersistentState(mySchema, persistentState);
                persistentState.setProperty("ID", "" + ISLOID.intValue());
                updateStatusMessage = "Gen Ed Area : " +  persistentState.getProperty("AreaName")
                        + " installed successfully!";
            }
        }
        catch (SQLException ex)
        {
            updateStatusMessage = "Error in installing Gen Ed Area data in database!";
        }
        //DEBUG System.out.println("updateStateInDatabase " + updateStatusMessage);
    }

    // Probably not needed
    private void removeStateInDatabase()
    {
        try
        {
            if (persistentState.getProperty("ID") != null)
            {
                Properties whereClause = new Properties();
                whereClause.setProperty("ID", persistentState.getProperty("ID"));
                updatePersistentState(mySchema, persistentState, whereClause);
                updateStatusMessage = "Gen Ed Area : " + persistentState.getProperty("AreaName") + " removed successfully!";
            }
        }
        catch (SQLException ex)
        {
            updateStatusMessage = "Error in removing Gen Ed Area data in database!";
        }
        //DEBUG System.out.println("updateStateInDatabase " + updateStatusMessage);
    }
    /**
     * This method is needed solely to enable the ISLO information to be displayable in a table
     *
     */
    //--------------------------------------------------------------------------
    public Vector<String> getEntryListView()
    {
        Vector<String> v = new Vector<String>();

        v.addElement(persistentState.getProperty("AreaName"));
        v.addElement(persistentState.getProperty("Description"));


        return v;
    }

    // Short cut methods
        public String getName(){
        return persistentState.getProperty("AreaName");
    }

    public String getDescription(){
        return persistentState.getProperty("Description");
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

