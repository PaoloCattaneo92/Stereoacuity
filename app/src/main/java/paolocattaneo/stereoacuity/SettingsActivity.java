package paolocattaneo.stereoacuity;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import utils.DefaultValues;
import utils.NoticeDialogFragment;
import utils.Setting;

public class SettingsActivity extends FragmentActivity
implements NoticeDialogFragment.NoticeDialogListener
{

    //DEBUG TAG
    private static final String TAG = TestActivity.class.getSimpleName();

    /** Shared Preferences */
    public static final String PREFS = "prefs";    //preferences file name
    //Test parameters
    public static final String PREF_MAXDISPARITY = "pref_maxdisparity";
    public static final String PREF_MINDISPARITY = "pref_mindisparity";
    public static final String PREF_OFFSET = "pref_offset";
    public static final String PREF_NCORR_TO_NEXTLEVEL = "pref_ncorrtonextlevel";
    public static final String PREF_NERR_TOSTOPTEST = "pref_nerrtostoptest";
    //TODO Algorithm parameters preferences
    public SharedPreferences sharedPref;

    /** String for naming values */
    public static final String name_maxdisparity = "Maximum Disparity";
    public static final String name_mindisparity = "Minimum Disparity";
    public static final String name_offset = "Offset disparity";
    public static final String name_ncorr = "N. Corr. to Next";
    public static final String name_nerr = "N. Err. to Stop";

    /**
     * Constants loaded from the preferences or set to Default Values */
    /** Test Constants */
    private static int MAXDISPARITY;        private int actual_maxdisparity;
    private static int MINDISPARITY;        private int actual_mindisparity;
    private static int OFFSET;              private int actual_offset;
    private static int NCORR_TO_NEXTLEVEL;  private int actual_ncorr_to_nextlevel;
    private static int NERR_TO_STOPTEST;    private int actual_nerr_to_stoptest;
    /** Algorithm Constants */

    /** List of Settings */
    private ArrayList<Setting> settingsList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //load from prefs
        sharedPref = getSharedPreferences(PREFS, MODE_PRIVATE);
        MAXDISPARITY = sharedPref.getInt(PREF_MAXDISPARITY, 0);
        MINDISPARITY = sharedPref.getInt(PREF_MINDISPARITY,0);
        OFFSET = sharedPref.getInt(PREF_OFFSET,0);
        NCORR_TO_NEXTLEVEL = sharedPref.getInt(PREF_NCORR_TO_NEXTLEVEL,0);
        NERR_TO_STOPTEST = sharedPref.getInt(PREF_NERR_TOSTOPTEST,0);

        //set actual to the loaded values
        actual_maxdisparity = MAXDISPARITY;
        actual_mindisparity = MINDISPARITY;
        actual_offset = OFFSET;
        actual_ncorr_to_nextlevel = NCORR_TO_NEXTLEVEL;
        actual_nerr_to_stoptest = NERR_TO_STOPTEST;




        //create the settingsList with the loaded values
        settingsList.add(new Setting(PREF_MAXDISPARITY,name_maxdisparity, getString(R.string.setting_description_maxdisparity),actual_maxdisparity));
        settingsList.add(new Setting(PREF_MINDISPARITY,name_mindisparity, getString(R.string.setting_description_mindisparity),actual_mindisparity));
        settingsList.add(new Setting(PREF_OFFSET,name_offset, getString(R.string.setting_description_offset), actual_offset));
        settingsList.add(new Setting(PREF_NCORR_TO_NEXTLEVEL,name_ncorr, getString(R.string.setting_description_ncorrtonextlevel), actual_ncorr_to_nextlevel));
        settingsList.add(new Setting(PREF_NERR_TOSTOPTEST,name_nerr, getString(R.string.setting_description_nerrtostop), actual_nerr_to_stoptest));

        //prepare data
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

        for(Setting s : settingsList) {
            HashMap<String, String> sMap = new HashMap<String, String>();
            sMap.put("setting_name",s.name);
            sMap.put("setting_description",s.description);
            sMap.put("setting_actualValue",String.valueOf(s.valueToShow));
            data.add(sMap);
        }

        //building adapter
        String[] from = {"setting_name","setting_description","setting_actualValue"};
        int[] to = {R.id.txt_settings_name, R.id.txt_settings_description, R.id.edittext_actualvalue};

        SimpleAdapter adapter = new SimpleAdapter(
                getApplicationContext(), //ok
                data,                    //data = ArrayList di HashMap(s)
                R.layout.settings_line,  //il layout della singola linea
                from,                    //array di stringhe id (valori)
                to);                     //array di int id (layouts)

        //adapter use
        final ListView settingsListView = (ListView)findViewById(R.id.settingsList);
        settingsListView.setAdapter(adapter);



    }//end On Create


    public void showNoticeDialog() {
        DialogFragment dialog = new NoticeDialogFragment();
        dialog.show(getFragmentManager(),"NoticeDialogFragment");
    }

    //BUTTONS
    /**
     * Button BACK to go back without save any changes (actual does not override OLD)
     * @param view dunno
     */
    public void back(View view) {
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i); //lose all the changes made
    }

    /**
     * Button RESET to reset all the settings to DefaultValues constants (DefaultValues in actual in OLD)
     * @param view dunno
     */
    public void reset(View view) {
        actual_mindisparity = DefaultValues.DEFAULT_MINDISPARITY;
        actual_maxdisparity = DefaultValues.DEFAULT_MAXDISPARITY;
        actual_offset = DefaultValues.DEFAULT_OFFSET;
        actual_nerr_to_stoptest = DefaultValues.DEFAULT_NERR_TOSTOPTEST;
        actual_ncorr_to_nextlevel = DefaultValues.DEFAULT_NCORR_TONEXTLEVE;
        saveActualsInPreferences();
    }

    /**
     * Button CONFIRM to save in the sharedpref the actual variables (actual in OLD)
     * @param view dunno
     */
    public void confirm(View view) {
        saveActualsInPreferences();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    /**
     * Auxiliar method called in buttons method
     */
    private void saveActualsInPreferences() {
        SharedPreferences.Editor e = sharedPref.edit();
        e.putInt(PREF_MAXDISPARITY, actual_maxdisparity);
        e.putInt(PREF_MINDISPARITY, actual_mindisparity);
        e.putInt(PREF_OFFSET, actual_offset);
        e.putInt(PREF_NERR_TOSTOPTEST, actual_nerr_to_stoptest);
        e.putInt(PREF_NCORR_TO_NEXTLEVEL, actual_ncorr_to_nextlevel);
        e.commit();
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {

    }

    @Override
    public void onDialogNegativeClicK(DialogFragment dialog) {

    }
}
