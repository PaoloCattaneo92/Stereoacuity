package utils;

import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

import paolocattaneo.stereoacuity.TestActivity;

/**
 * Created by paolo on 18/11/2015.
 * The Session contains all the useful data for a Test Session, to see if there's an improvement.
 */
public class Session{

    public ArrayList<Test> testsList;
    Calendar calendar;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd HH:mm:ss");
    SimpleDateFormat condensatedf = new SimpleDateFormat("yyyymmmddhhmmss");
    public Certification_Value certification_value;
    String username;
    public String id;

    public int nCorr;
    public int nErr;
    public int disparity;   //actual value of disparity
    public Test actualTest;

    /**
     * Constructor for the Session
     * @param username the username of the actual user when the Session was done
     */
    public Session(String username, SharedPreferences sharedPreferences) {
        this.testsList = new ArrayList<>();
        this.calendar = new GregorianCalendar();
        this.username=username;
        this.id = idGenerator();
        nCorr=0;
        nErr=0;
    }

    /**
     * You know.
     * @return a string with the data of the session (without the lists of the Tests involved)
     */
    public String toString() {
        String buff = "Session ID:+"+id+". Username: "+username+", Date: "+sdf.format(calendar.getTime())+
                ", certification value= "+certification_value.name();
        return buff;
    }

    /**
     * Similar to toString() but it adds ALL the informations about the Tests in the list.
     * @return a string with the data of the session, including all the Test involved
     */
    public String toStringVerbose() {
        String buff = this.toString()+" - ";
        buff += "And all the Test are:\n";
        for(Test t : testsList) {
            buff += t.toString() + "\n";
        }
        return buff;
    }

    /**
     * This method create an unique id concatenating the username with the date of the session
     * and also adding a random value 0-9
     * @return a String with an automatic ID
     */
    public String idGenerator() {
        return username.trim() + condensatedf.format(calendar.getTime()) + new Random().nextInt(9);
    }


    public boolean Continue(Shape chosenShape) {
        //CORRECT ANSWER
        if(this.actualTest.CheckAnswer(chosenShape)) {
            actualTest.result=true;
            nCorr++;

            if(nCorr<3) {
                return true; //true = new Test with the same disparity
                //remember nCorr,nErr to see how long to stay in this disparity level
            } else {
                if(disparity== TestActivity.MAXDISPARITY) {
                    return false;   // close session and certificate
                } else {
                    disparity-=TestActivity.OFFSET;  //decrease disparity
                    nCorr=0;            //reset nCorr, nErr
                    nErr=0;
                    return  true;        //new Test in a new lesser disparity level
                }
            }

        }
        else
        //Wrong Choice
        {
            actualTest.result=false;
            nErr++;

            if(nErr<3) {
                return true; //new Test with the same disparity
                //remember nCorr, nErr, to see how long to stay in this disparity level
            } else {
                disparity+=TestActivity.OFFSET; //go back to the previous disparity
                return false; //and stop session for certification
            }

        }
    }


}

