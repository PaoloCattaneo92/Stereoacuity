package paolocattaneo.stereoacuity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import utils.DefaultValues;
import utils.User;

public class MainActivity extends AppCompatActivity {
    /** TAG for Log */
    private static final String TAG = TestActivity.class.getSimpleName();

    /** Shared Preferences */
    public static final String PREFS = "prefs";
    public static final String LAST_USER = "last_username";
    public SharedPreferences sharedPref;

    /** Test Parameters */
    public static final String PREF_MAXDISPARITY = "pref_maxdisparity";
    public static final String PREF_MINDISPARITY = "pref_mindisparity";
    public static final String PREF_OFFSET = "pref_offset";
    public static final String PREF_NCORR_TO_NEXTLEVEL = "pref_ncorrtonextlevel";
    public static final String PREF_NERR_TOSTOPTEST = "pref_nerrtostoptest";


    /**
     * Constants loaded from the preferences or set to Default Values */
    /** Test Constants */
    private static int MAXDISPARITY;        private int actual_maxdisparity;
    private static int MINDISPARITY;        private int actual_mindisparity;
    private static int OFFSET;              private int actual_offset;
    private static int NCORR_TO_NEXTLEVEL;  private int actual_ncorr_to_nextlevel;
    private static int NERR_TO_STOPTEST;    private int actual_nerr_to_stoptest;
    /** Algorithm Constants */
    public static int imgWidth = 400;
    public static int imgHeight = 400;


    /**
     * The actual User
     */
    public String actualUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //load last user
        sharedPref = getSharedPreferences(PREFS, MODE_PRIVATE);
        String lastUser = sharedPref.getString(LAST_USER,"No User Found");
        if(lastUser.equals("No User Found")) askNewUser();
        TextView txt_username = (TextView)findViewById(R.id.txt_username);
        txt_username.setText(lastUser);
        Log.d(TAG,"Last User loaded is "+lastUser);

        //loadotherpreferences
        loadPreferences();

    }

    /**
     * Method used to load preferences and save them to the variable.
     * If preferences are not viable (0), it loads them from the class DefaultValues
     * A little different from its TrainingActivity counterpart because it has more preferences to load.
     */
    private void loadPreferences() {
        MAXDISPARITY = sharedPref.getInt(PREF_MAXDISPARITY, 0);
        MINDISPARITY = sharedPref.getInt(PREF_MINDISPARITY, 0);
        OFFSET = sharedPref.getInt(PREF_OFFSET, 0);
        NCORR_TO_NEXTLEVEL = sharedPref.getInt(PREF_NCORR_TO_NEXTLEVEL,0);
        NERR_TO_STOPTEST = sharedPref.getInt(PREF_NERR_TOSTOPTEST,0);


        //if there is no preferences saved for any of them use default values
        if(MAXDISPARITY==0) {
            MAXDISPARITY = DefaultValues.DEFAULT_MAXDISPARITY;
        }
        if(MINDISPARITY==0) {
            MINDISPARITY = DefaultValues.DEFAULT_MINDISPARITY;
        }

        if(OFFSET==0) {
            OFFSET = DefaultValues.DEFAULT_OFFSET;
        }

        if(NCORR_TO_NEXTLEVEL==0) {
            NCORR_TO_NEXTLEVEL = DefaultValues.DEFAULT_NCORR_TONEXTLEVE;
        }

        if(NERR_TO_STOPTEST==0) {
            NERR_TO_STOPTEST = DefaultValues.DEFAULT_NERR_TOSTOPTEST;
        }
        savePreferences();
    }

    /**
     * Method used to save the actual variables into the sharedPreferences
     * A little different from its TrainingActivity counterpart because it has more preferences to save.
     */
    private void savePreferences() {
        // Put it into memory (don't forget to commit!)
        SharedPreferences.Editor e = sharedPref.edit();
        e.putInt(PREF_MAXDISPARITY, MAXDISPARITY);
        e.putInt(PREF_MINDISPARITY, MINDISPARITY);
        e.putInt(PREF_OFFSET, OFFSET);
        e.putInt(PREF_NERR_TOSTOPTEST, NERR_TO_STOPTEST);
        e.putInt(PREF_NCORR_TO_NEXTLEVEL, NCORR_TO_NEXTLEVEL);
        e.commit();
    }



    /**
     * This method ask for new user if No User Found from shared preferences
     */
    private void askNewUser() {

        // Read the user's name,
        // or an empty string if nothing found
        String name = sharedPref.getString(LAST_USER, "");

        if (name.length()==0) {
            //show a dialog to ask for their name
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Welcome to Stereoacutity Test!");
            alert.setMessage("What is your name?");

            // Create EditText for entry
            final EditText input = new EditText(this);
            alert.setView(input);

            // Make an "OK" button to save the name
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                    // Grab the EditText's input
                    String inputName = input.getText().toString();

                    // Put it into memory (don't forget to commit!)
                    SharedPreferences.Editor e = sharedPref.edit();
                    e.putString(LAST_USER, inputName);
                    e.commit();

                    // Welcome the new user
                    //Toast.makeText(getApplicationContext(), "Welcome, " + inputName + "!", Toast.LENGTH_LONG).show();
                }
            });

            // Make a "Cancel" button
            // that simply dismisses the alert
            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int whichButton) {}
            });

            alert.show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


     /** The buttons intents of the main menu */
    public void openLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void openTraining(View view) {
        Intent intent = new Intent(this, TrainingActivity.class);
        startActivity(intent);
    }

    public void openTest(View view) {
        Intent intent = new Intent(this, TestActivity.class);
        startActivity(intent);
    }

    public void openSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }



}
