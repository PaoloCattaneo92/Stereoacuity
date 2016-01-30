package utils;

import android.content.SharedPreferences;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

/**
 * Created by paolo on 18/11/2015.
 * The Session contains all the useful data for a Test Session, to see if there's an improvement.
 */
public class Session {

    ArrayList<Test> testsList;
    Calendar calendar;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd HH:mm:ss");
    SimpleDateFormat condensatedf = new SimpleDateFormat("yyyymmmddhhmmss");
    Certification_Value certification_value;
    String username;
    String id;

    /**
     * Constructor for the Session
     * @param testsList an ArrayList containing all the Tests that bring the certification
     * @param exit_disparity the final value got
     * @param username the username of the actual user when the Session was done
     */
    public Session(ArrayList<Test> testsList, int exit_disparity, String username, SharedPreferences sharedPreferences) {
        this.testsList=testsList;
        this.calendar = new GregorianCalendar();
        this.username=username;
        this.certification_value = Certification_Value.intToCertificationValue(exit_disparity, sharedPreferences);
        this.id = idGenerator();

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
        String buff = "Session id:"+id+". Username: "+username+", Date: "+sdf.format(calendar.getTime())+
                ", certification value= "+certification_value+" - ";
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




}
