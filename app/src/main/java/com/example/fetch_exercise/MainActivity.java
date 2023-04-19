/**
 * @author: Gean Maidana Dollanarte
 *
 * This code generally follows the coding conventions for the Java
 * programming language set forth by Oracle but doesn't adhere to it strictly.
 *
 * For example, Oracle states to write if statements with the braces on the same
 * line, but I don't do that because I find it more readable to put it on its
 * own line.
 *
 *
 * https://www.oracle.com/java/technologies/javase/codeconventions-fileorganization.html
 * */

package com.example.fetch_exercise;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import org.json.JSONObject;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * This class passes the FETCH_URL to the appropriate JSONDataHandler method
 * using the JSONDataHandler object.
 *
 *
 * The populateTable() uses the sorted JSON data to create new
 * rows for the table. populateTable() is called in the JSONDataHandler class,
 * but the method resides in this class because it's changing the UI,
 * and I'm trying to separate UI and non-UI functionality into different classes.
 *
 * */
public class MainActivity extends AppCompatActivity {

    // JSON data hosted @ FETCH_URL
    private static final URL FETCH_URL;
    static
    {
        try
        {
            FETCH_URL = new URL("https://fetch-hiring.s3.amazonaws.com/hiring.json");
        }
        catch (MalformedURLException e)
        {

            throw new RuntimeException(e);
        }
    }


    // Text size used for JSON data when dynamically creating rows for the table
    private static final int TEXT_SIZE = 14;

    // Padding used for JSON data when dynamically creating rows for the table
    private static final int PADDING = 10;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        JSONDataHandler obj = new JSONDataHandler(MainActivity.this);
        obj.fetchJSONData(FETCH_URL);

    } // end of onCreate()

    /**
     * populateTable(): This method retrieves the sorted
     * JSON data and then creates and populates the rows
     * on the table using the sorted JSON data.
     */
    public void populateTable()
    {
        // This method is called in JSONDataHandler, which is run on a different
        // thread, but since populateTable() is adding rows to the table,
        // it needs to run on the UI thread
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                // Gets TableLayout so that we can create a new row
                TableLayout tableLayout = findViewById(R.id.table_layout);

                // Loops through the sorted JSON array and adds a new
                // row for each item
                for (int i = 0; i < JSONDataHandler.getSortedJsonArray()
                        .length(); i++)
                {
                    try
                    {
                        JSONObject item = JSONDataHandler.getSortedJsonArray()
                                .getJSONObject(i);
                        JSONDataHandler.getSortedJsonArray().getJSONObject(i);
                        TableRow row = new TableRow(MainActivity.this);

                        // This params is applied to each TextView below to make
                        // sure the JSON data is formatted properly on the table
                        TableRow.LayoutParams params = new TableRow.LayoutParams(
                                0,      // width
                                TableRow.LayoutParams.WRAP_CONTENT   // height
                        );
                        params.weight = 4;
                        row.setLayoutParams(params);


                        // Creates new TextViews to hold the values of the
                        // JSON object
                        TextView idTextView = new TextView(
                                MainActivity.this);
                        TextView listIdTextView = new TextView(
                                MainActivity.this);
                        TextView nameTextView = new TextView(
                                MainActivity.this);


                        // This is for setting the text to a custom font I
                        // have chosen added in the font folder within
                        // the resource folder
                        Typeface typeface = ResourcesCompat.getFont(
                                MainActivity.this,
                                R.font.quicksand_medium);


                        // Sets the values of the TextViews to the corresponding
                        // values in the JSON object. These values that are
                        // being set are following the same property attributes
                        // set in the header row in the XML file
                        idTextView.setText(item.getString("id"));
                        idTextView.setTextSize(TEXT_SIZE);
                        idTextView.setPadding(
                                PADDING, PADDING, PADDING, PADDING);
                        idTextView.setGravity(Gravity.CENTER_HORIZONTAL);
                        idTextView.setLayoutParams(params);
                        idTextView.setTypeface(typeface);


                        listIdTextView.setText(item.getString("listId"));
                        listIdTextView.setTextSize(TEXT_SIZE);
                        listIdTextView.setPadding(
                                PADDING, PADDING, PADDING, PADDING);
                        listIdTextView.setGravity(Gravity.CENTER_HORIZONTAL);
                        listIdTextView.setLayoutParams(params);
                        listIdTextView.setTypeface(typeface);

                        nameTextView.setText(item.getString("name"));
                        nameTextView.setTextSize(TEXT_SIZE);
                        nameTextView.setPadding(
                                PADDING, PADDING, PADDING, PADDING);
                        nameTextView.setGravity(Gravity.CENTER_HORIZONTAL);
                        nameTextView.setLayoutParams(params);
                        nameTextView.setTypeface(typeface);


                        // Adds the TextViews to the TableRow
                        row.addView(idTextView);
                        row.addView(listIdTextView);
                        row.addView(nameTextView);

                        // Adds the TableRow to the TableLayout
                        tableLayout.addView(row);
                    }
                    catch (Exception jsonError)
                    {
                        Log.e("MyApp", "Exception with JSON data",
                                jsonError);
                    }
                } // end of for-loop
            } // end of run() method
        }); // end of runOnUiThread()
    } // End of populateTable()
} // end of MainActivity class
