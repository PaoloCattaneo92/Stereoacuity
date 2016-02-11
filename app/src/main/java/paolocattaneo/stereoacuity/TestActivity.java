package paolocattaneo.stereoacuity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

import utils.Certification_Value;
import utils.DefaultValues;
import utils.Session;
import utils.Shape;
import utils.Test;

public class TestActivity extends AppCompatActivity {

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

    public SharedPreferences sharedPref;

    /**
     * Constants loaded from the preferences or set to Default Values */
    /** Test Constants */
    public static int MAXDISPARITY;
    public static int MINDISPARITY;
    public static int OFFSET;
    public static int NCORR_TO_NEXTLEVEL;
    public static int NERR_TO_STOPTEST;
    /** Algorithm Constants */

    /** Views elements */
    ImageView testImgView;

    /** Test variables */
    int numTest;
    Session session;
    Shape shape;
    String shapestring;
    ArrayList<Test> testList;
    String actualUser;

    /** Test parameters */
    int displayWidth;
    int displayHeigth;
    int displayDensity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        //identify layouts
        testImgView = (ImageView) findViewById(R.id.testImgView);

        //initialize variables
        sharedPref = getSharedPreferences(PREFS, MODE_PRIVATE);
        loadPreferences();
        shape = Shape.NOIMAGE;
        shapestring = shape.toString();
        numTest=1;
        Bundle dataFromMain = getIntent().getExtras();
        actualUser = dataFromMain.getString("actualUser");

        //initialize screen dimensions
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        displayWidth=dm.widthPixels;
        displayHeigth=dm.heightPixels;
        displayDensity=dm.densityDpi;

        //session handler
        testList = new ArrayList<>();
        session.disparity = MAXDISPARITY;
        session = new Session(actualUser,sharedPref);

        //start  test
        startTest();

    }

    /**
     * This method create a new randomized Test and set that to actualTest
     */
    private void startTest() {
        String buff;
        if(session.actualTest!=null) {
            buff = session.actualTest.toString()+", nErr= "+session.nErr+", nCorr= "+session.nCorr;
            Log.d(TAG,"Previous test was "+buff);
        }

       //create and show actual test of the session
        session.actualTest = new Test(numTest,session.disparity,Test.randomShape());
        session.actualTest.buildBitmap(displayWidth,displayHeigth, session.disparity);
        testImgView.setImageBitmap(session.actualTest.bitmap);

        buff = session.actualTest.toString()+", nErr= "+session.nErr+", nCorr= "+session.nCorr;
        Log.d(TAG,"Actual test is "+buff);
    }


    /**
     * Method used to load preferences and save them to the variable.
     * If preferences are not viable (0), it loads them from the class DefaultValues
     * A little different from its TrainingActivity counterpart because it has more preferences to load.
     */
    private void loadPreferences() {
        MAXDISPARITY = sharedPref.getInt(PREF_MAXDISPARITY, 0);
        MINDISPARITY = sharedPref.getInt(PREF_MINDISPARITY, 0);
        OFFSET = sharedPref.getInt(PREF_OFFSET,0);
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
        savePreferences();  //every time you load you also save

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

    /** BUTTONS */
    /**
     * This Button handles the Skip-Button => Generates a new Test with the same
     * disparity level not increasing the "n" variable.
     * @param view dunno
     */
    public void OnClickSkip(View view) {
        numTest++;
        session.nErr++; // skip counts as an error
        session.testsList.add(session.actualTest);
        startTest();
    }

    /** BUTTONS */
    public void OnClickSquare(View view) {
        numTest++; //always increase numTest
        session.testsList.add(session.actualTest);

        if(session.Continue(Shape.SQUARE)) {
            startTest();
        } else {
            CertificateNow();
        }

    } /** end Button*/

    public void OnClickCircle(View view) {
        numTest++; //always increase numTest
        session.testsList.add(session.actualTest);

        if(session.Continue(Shape.CIRCLE)) {
            startTest();
        } else {
            CertificateNow();
        }

        }


    public void OnClickTriangle(View view) {
        numTest++; //always increase numTest
        session.testsList.add(session.actualTest);

        if(session.Continue(Shape.TRIANGLE)) {
            startTest();
        } else {
            CertificateNow();
        }
    }

    public void OnClickRectangle(View view) {
        numTest++; //always increase numTest
        session.testsList.add(session.actualTest);

        if(session.Continue(Shape.RECTANGLE)) {
            startTest();
        } else {
            CertificateNow();
        }

    } /** end Button*/


    /**
     * This method saves the certifications.
     */
    private void CertificateNow() {
        //close the session and certificate
        session.certification_value = Certification_Value.intToCertificationValue(session.disparity,sharedPref);

        //TODO salva la certificazione da qualche parte
        Intent intent = new Intent(this, MainActivity.class);
        Log.d(TAG,"Last Session is "+session.toString());
        Log.v(TAG, "(verbose)Last session is " + session.toStringVerbose());


        startActivity(intent);
    }
}
