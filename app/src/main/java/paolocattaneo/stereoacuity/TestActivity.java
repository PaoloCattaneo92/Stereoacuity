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
    Test actualTest;
    int numTest;
    int nCorr;
    int nErr;
    int disparity;
    Shape shape;
    String shapestring;
    ArrayList<Test> testList;

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
        disparity = MAXDISPARITY;
        shape = Shape.NOIMAGE;
        shapestring = shape.toString();
        numTest=1;
        nCorr=0;
        nErr=0;
        testList = new ArrayList<>();

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        displayWidth=dm.widthPixels;
        displayHeigth=dm.heightPixels;
        displayDensity=dm.densityDpi;

        //start first test
        startTest();

        //from now on everything is ruled with buttons
    }

    /**
     * This method create a new randomized Test and set that to actualTest
     */
    private void startTest() {
        String buff;
        if(actualTest!=null) {
            buff = actualTest.toString()+", nErr= "+nErr+", nCorr= "+nCorr;
            Log.d(TAG,"Previous test was "+buff);
        }

        actualTest = new Test(numTest,disparity,Test.randomShape());
        actualTest.buildBitmap(displayWidth,displayHeigth, actualTest.disparity);
        testImgView.setImageBitmap(actualTest.bitmap);

        buff = actualTest.toString()+", nErr= "+nErr+", nCorr= "+nCorr;
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
        testList.add(actualTest);
        startTest();
    }

    /** BUTTONS */
    public void OnClickSquare(View view) {
        numTest++; //always increase numTest
        testList.add(actualTest); //and add it to the list

        //Correct Choice
        if(actualTest.shape.equals(Shape.SQUARE)) {
            actualTest.result=true;
            Toast.makeText(this, "Correct choice", Toast.LENGTH_SHORT).show();
            nCorr++;

            if(nCorr<3) {
                startTest(); //new Test with the same disparity
                            //remember nCorr,nErr to see how long to stay in this disparity level
            } else {
                if(disparity==MINDISPARITY) {
                    CertificateNow();
                } else {
                    disparity-=OFFSET;  //decrease disparity
                    nCorr=0;            //reset nCorr, nErr
                    nErr=0;
                    startTest();        //new Test in a new lesser disparity level
                }
            }

        }
        else
        //Wrong Choice
        {
            actualTest.result=false;
            Toast.makeText(this, "Wrong choice, try again.", Toast.LENGTH_SHORT).show();
            nErr++;

            if(nErr<3) {
                startTest(); //new Test with the same disparity
                            //remember nCorr, nErr, to see how long to stay in this disparity level
            } else {
                disparity+=OFFSET; //you certificate previous disparity
                CertificateNow();
            }

        }

    } /** end Button*/

    public void OnClickCircle(View view) {
        numTest++; //always increase numTest
        testList.add(actualTest); //and add it to the list

        //Correct Choice
        if(actualTest.shape.equals(Shape.CIRCLE)) {
            actualTest.result=true;
            Toast.makeText(this, "Correct choice", Toast.LENGTH_SHORT).show();
            nCorr++;
            //txt_prevTestDescription.setText(actualTest.toString());

            if(nCorr<3) {
                startTest(); //new Test with the same disparity
                //remember nCorr,nErr to see how long to stay in this disparity level
            } else {
                if(disparity==MINDISPARITY) {
                    CertificateNow();
                } else {
                    disparity-=OFFSET;  //decrease disparity
                    nCorr=0;            //reset nCorr, nErr
                    nErr=0;
                    startTest();        //new Test in a new lesser disparity level
                }
            }

        }
        else
        //Wrong Choice
        {
            actualTest.result=false;
            Toast.makeText(this, "Wrong choice, try again.", Toast.LENGTH_SHORT).show();
            nErr++;
            //txt_prevTestDescription.setText(actualTest.toString());

            if(nErr<3) {
                startTest(); //new Test with the same disparity
                //remember nCorr, nErr, to see how long to stay in this disparity level
            } else {
                disparity+=OFFSET; //you certificate previous disparity
                CertificateNow();
            }

        }

    } /** end Button*/

    public void OnClickTriangle(View view) {
        numTest++; //always increase numTest
        testList.add(actualTest); //and add it to the list

        //Correct Choice
        if(actualTest.shape.equals(Shape.TRIANGLE)) {
            actualTest.result=true;
            Toast.makeText(this, "Correct choice", Toast.LENGTH_SHORT).show();
            nCorr++;
            //txt_prevTestDescription.setText(actualTest.toString());

            if(nCorr<3) {
                startTest(); //new Test with the same disparity
                //remember nCorr,nErr to see how long to stay in this disparity level
            } else {
                if(disparity==MINDISPARITY) {
                    CertificateNow();
                } else {
                    disparity-=OFFSET;  //decrease disparity
                    nCorr=0;            //reset nCorr, nErr
                    nErr=0;
                    startTest();        //new Test in a new lesser disparity level
                }
            }

        }
        else
        //Wrong Choice
        {
            actualTest.result=false;
            Toast.makeText(this, "Wrong choice, try again.", Toast.LENGTH_SHORT).show();
            nErr++;
            //txt_prevTestDescription.setText(actualTest.toString());

            if(nErr<3) {
                startTest(); //new Test with the same disparity
                //remember nCorr, nErr, to see how long to stay in this disparity level
            } else {
                disparity+=OFFSET; //you certificate previous disparity
                CertificateNow();
            }

        }

    } /** end Button*/

    public void OnClickRectangle(View view) {
        numTest++; //always increase numTest
        testList.add(actualTest); //and add it to the list

        //Correct Choice
        if(actualTest.shape.equals(Shape.RECTANGLE)) {
            actualTest.result=true;
            Toast.makeText(this, "Correct choice", Toast.LENGTH_SHORT).show();
            nCorr++;
            //txt_prevTestDescription.setText(actualTest.toString());

            if(nCorr<3) {
                startTest(); //new Test with the same disparity
                //remember nCorr,nErr to see how long to stay in this disparity level
            } else {
                if(disparity==MINDISPARITY) {
                    CertificateNow();
                } else {
                    disparity-=OFFSET;  //decrease disparity
                    nCorr=0;            //reset nCorr, nErr
                    nErr=0;
                    startTest();        //new Test in a new lesser disparity level
                }
            }

        }
        else
        //Wrong Choice
        {
            actualTest.result=false;
            Toast.makeText(this, "Wrong choice, try again.", Toast.LENGTH_SHORT).show();
            nErr++;
            //txt_prevTestDescription.setText(actualTest.toString());

            if(nErr<3) {
                startTest(); //new Test with the same disparity
                //remember nCorr, nErr, to see how long to stay in this disparity level
            } else {
                disparity+=OFFSET; //you certificate previous disparity
                CertificateNow();
            }

        }

    } /** end Button*/


    /**
     * This method saves the certifications.
     */
    private void CertificateNow() {
        //TODO controlla vecchie certificazioni dello stesso utente
        //
        Session lastSession = new Session(testList, disparity, "Mario Rossi",sharedPref);
        Toast.makeText(this, "Test ended. Certification "+disparity+" disparity value", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        Log.d(TAG,"Last Session is "+lastSession.toStringVerbose());

    }
}
