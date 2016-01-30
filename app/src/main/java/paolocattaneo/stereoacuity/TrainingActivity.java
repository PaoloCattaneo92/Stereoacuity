package paolocattaneo.stereoacuity;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import utils.DefaultValues;
import utils.Shape;
import utils.Test;

public class TrainingActivity extends AppCompatActivity {

    /** Shared Preferences */
    public static final String PREFS = "prefs";    //preferences file name
    public static final String PREF_MAXDISPARITY = "pref_maxdisparity";
    public static final String PREF_MINDISPARITY = "pref_mindisparity";

    /** Test parameters */
    int displayWidth;
    int displayHeigth;
    int displayDensity;


    public SharedPreferences sharedPref;

    /**
     * Constants loaded from the preferences or set to Default Values */
    public static int MAXDISPARITY;
    public static int MINDISPARITY;


    /** Views elements */
    TextView txt_disparity;
    TextView txt_disparitymessage;
    ImageView testImgView;

    /** Test variables */
    Test actualTest;
    int numTest;
    int disparity;
    Shape shape;
    String shapestring;

    /** Test Image */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        //identify layouts
        txt_disparity = (TextView) findViewById(R.id.txt_disparity);
        txt_disparitymessage = (TextView) findViewById(R.id.txt_disparitymessage);
        testImgView = (ImageView) findViewById(R.id.testImgView);

        //initialize variables
        sharedPref = getSharedPreferences(PREFS, MODE_PRIVATE);
        loadPreferences();
        disparity = MAXDISPARITY;
        shape = Shape.NOIMAGE;
        shapestring = shape.toString();
        numTest=1;

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        displayWidth=dm.widthPixels;
        displayHeigth=dm.heightPixels;
        displayDensity=dm.densityDpi;

        //set TextView texts
        txt_disparity.setText(Integer.toString(disparity));

        //start first test
        startTest();

    }


    /**
     * +1 to the disparity (if it's possible) and sets the text view
     * @param view dunno
     */
    public void addDisparity(View view) {
        if(disparity<MAXDISPARITY) {
            disparity++;
            txt_disparity.setText(disparity);
            txt_disparitymessage.setText(R.string.disparity_message_decrease);
            Log.v("TAG","disparity increased, is now "+disparity);
            startTest();
        }
        else  Toast.makeText(this, "Disparity at Maximum level", Toast.LENGTH_SHORT).show();
    }

    /**
     * -1 to the disparity (if it's possible) and sets the Text View
     * @param view dunno
     */
    public void subDisparity(View view) {
        if(disparity>MINDISPARITY) {
            disparity--;
            txt_disparity.setText(disparity);
            txt_disparitymessage.setText(R.string.disparity_message_increase);
            Log.v("TAG", "disparity decreased, is now " + disparity);
            startTest();
        }
        else  Toast.makeText(this, "Disparity at Minimum level", Toast.LENGTH_SHORT).show();
    }



    /** BUTTONS */
    public void OnClickSquare(View view) {
            if(actualTest.shape.equals(Shape.SQUARE)) {
                actualTest.result=true;
                Toast.makeText(this, "Correct choice", Toast.LENGTH_SHORT).show();
                numTest++;
            } else {
                Toast.makeText(this, "Wrong choice, try again.", Toast.LENGTH_SHORT).show();

            }
        Log.d("TAG","Test done is "+ actualTest.toString());
        if(actualTest.result) startTest();
    }

    public void OnClickTriangle(View view) {
        if(actualTest.shape.equals(Shape.TRIANGLE)) {
            actualTest.result=true;
            Toast.makeText(this, "Correct choice", Toast.LENGTH_SHORT).show();
            numTest++;
        } else {
            Toast.makeText(this, "Wrong choice, try again.", Toast.LENGTH_SHORT).show();

        }
        Log.d("TAG","Test done is "+ actualTest.toString());
        if(actualTest.result) startTest();
    }

    public void OnClickCircle(View view) {
        if(actualTest.shape.equals(Shape.CIRCLE)) {
            actualTest.result=true;
            Toast.makeText(this, "Correct choice", Toast.LENGTH_SHORT).show();
            numTest++;
        } else {
            Toast.makeText(this, "Wrong choice, try again.", Toast.LENGTH_SHORT).show();

        }
        Log.d("TAG","Test done is "+ actualTest.toString());
        if(actualTest.result) startTest();
    }

    public void OnClickRectangle(View view) {
        if(actualTest.shape.equals(Shape.RECTANGLE)) {
            actualTest.result=true;
            Toast.makeText(this, "Correct choice", Toast.LENGTH_SHORT).show();
            numTest++;
        } else {
            Toast.makeText(this, "Wrong choice, try again.", Toast.LENGTH_SHORT).show();

        }
        Log.d("TAG","Test done is "+ actualTest.toString());
        if(actualTest.result) startTest();
    }



    /**
     * This method create a new randomized Test and set that to actualTest
     */
    private void startTest() {
        actualTest = new Test(numTest,disparity,Test.randomShape());
        actualTest.buildBitmap(displayWidth,displayHeigth,disparity);
        testImgView.setImageBitmap(actualTest.bitmap);
        Log.d("TAG","Actual Test is "+ actualTest.toString());
    }

    /**
     * Method used to load preferences and save them to the variable.
     * If preferences are not viable (0), it loads them from the class DefaultValues
     */
    private void loadPreferences() {
        MAXDISPARITY = sharedPref.getInt(PREF_MAXDISPARITY, 0);
        MINDISPARITY = sharedPref.getInt(PREF_MINDISPARITY, 0);

        //if there is no preferences saved use default values
        if(MAXDISPARITY==0) {
            MAXDISPARITY = DefaultValues.DEFAULT_MAXDISPARITY;
            savePreferences();
        }
        if(MINDISPARITY==0) {
            MINDISPARITY = DefaultValues.DEFAULT_MINDISPARITY;
            savePreferences();
        }

    }

    /**
     * Method used to save the actual variables into the sharedPreferences
     */
    private void savePreferences() {
        // Put it into memory (don't forget to commit!)
        SharedPreferences.Editor e = sharedPref.edit();
        e.putInt(PREF_MAXDISPARITY, MAXDISPARITY);
        e.putInt(PREF_MINDISPARITY, MINDISPARITY);
        e.commit();
    }





}
