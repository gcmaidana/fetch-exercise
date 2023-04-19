/**
 * @author: Gean Maidana Dollanarte
 * */

package com.example.fetch_exercise;


import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Class retrieves JSON data from a URL, eliminates instances where name is
 * either null or blank, then sorts it according to
 * listId, and when the listId is the same, it sorts by id.
 * The data is displayed in a table with rows for each JSON data by calling
 * the populateTable() method in the MainActivity class.
 *
 * */
public class JSONDataHandler implements Runnable
{
    // This variable has setter and getter methods for it.
    // It will be set at the end of the sortJSONData() method.
    // It will retrieved in the populateTable() method (in the
    // MainActivity class) when we have to dynamically create the rows for the
    // table using the sorted JSON data.
    private static JSONArray sortedJsonArray;

    // Static variable for MainActivity, used in the constructor
    // so that we have the same reference for the MainActivity class
    // throughout the program running
    private MainActivity mainActivity;

    public JSONDataHandler(MainActivity mainActivity)
    {
        this.mainActivity = mainActivity;
    }



    /**
     * fetchJSONData(): This method opens the connection
     * with the FETCH_URL and retrieves JSON data.
     * @param:  inputURL    inputURL is used to open the URL connection
     *                      and get the JSON data
     * */
    public void fetchJSONData(URL inputURL)
    {
        // This method is called in onCreate() in MainActivity and Android
        // does not allow network operations in the UI thread so a new
        // thread needs to be created
        new Thread(()-> {
            try
            {

               HttpURLConnection con = createHttpURLConnection(inputURL);

                // Read response from server
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while((inputLine = in.readLine()) != null)
                {
                    response.append(inputLine);
                }
                in.close();

                // jsonArray holds all the JSON Data but it's not sorted
                JSONArray jsonArray = new JSONArray(response.toString());

                // sortJSONData() is not called in the onCreate() method
                // because we don't want the threads in fetchJSONData() and
                // sortJSONData() to run concurrently because that can cause
                // data inconsistency problems, we want these two threads to
                // run sequentially, so sortJSONData() is called at end of
                // the current thread we are in
                sortJSONData(jsonArray);
            }
            catch(Exception e)
            {
                Log.e("MyApp", "Exception", e);
            }
        }).start(); // End of thread
    } // End of fetchJSONData()


    /**
     * sortJSONData(): This method takes an input JSONArray object and
     * uses it sort the inputted JSON array.
     * @param:  jsonArray   the jsonArray is the unsorted JSON Array that
     *                      will be sorted in this method
     * */

    public void sortJSONData(JSONArray jsonArray) throws JSONException
    {

        new Thread(()-> {
            try {
                // This list will hold jsonList after we remove
                // names that are null or blank for the jsonArray parameter
                // Afterwards, jsonList will be sorted according to listId, and
                // also by id in cases where listId are the same
                List<JSONObject> jsonList = new ArrayList<JSONObject>();


                for (int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject obj = jsonArray.getJSONObject(i);

                    if (!obj.isNull("name") &&
                            !obj.getString("name").isEmpty())
                    {
                        jsonList.add(obj);
                    }
                }

                // Sort the list by "listId" and "id" using a Comparator
                Collections.sort(jsonList, new Comparator<JSONObject>()
                {
                    @Override
                    public int compare(JSONObject o1, JSONObject o2)
                    {
                        int listId1 = o1.optInt("listId");
                        int listId2 = o2.optInt("listId");
                        if (listId1 == listId2)
                        {
                            String id1 = o1.optString("id", "");
                            String id2 = o2.optString("id", "");
                            int len1 = id1.length();
                            int len2 = id2.length();
                            if(len1 == len2)
                            {
                                return id1.compareTo(id2);
                            }
                            else
                            {
                                return Integer.compare(len1, len2);
                            }

                        }

                        return Integer.compare(listId1, listId2);
                    }
                });

                // Convert the sorted list back to a JSONArray
                sortedJsonArray = new JSONArray(jsonList);
                setSortedJsonArray(sortedJsonArray);

                run();
            }
            catch(Exception e)
            {
                Log.e("MyApp", "Exception", e);
            }
        }).start(); // End of thread
    } // End of sortJSONData()


    public void setSortedJsonArray(JSONArray sortedJsonArray)
    {
        this.sortedJsonArray = sortedJsonArray;
    }

    public static JSONArray getSortedJsonArray()
    {
        return sortedJsonArray;
    }


    /**
     * run() is a method defined in the Runnable interface
     * This will call the populateTable() method in MainActivity
     * */
    @Override
    public void run()
    {
        mainActivity.populateTable();
    }


    /**
     * This method opens a URL connection with the given parameter
     * @param: url  used to URL to open the connection
     * @return      the url connection
     * */
    public HttpURLConnection createHttpURLConnection(URL url) throws IOException
    {
        HttpURLConnection urlConnection = (HttpURLConnection)
                url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setConnectTimeout(5000);
        urlConnection.setReadTimeout(5000);
        urlConnection.connect();
        return urlConnection;
    }


}
