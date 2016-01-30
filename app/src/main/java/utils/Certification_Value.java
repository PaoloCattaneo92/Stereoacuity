package utils;

import android.content.SharedPreferences;

/**
 * Created by paolo on 19/11/2015.
 * This Enum contains the possibles values for the certification.
 */
public enum Certification_Value {

    POOR,  AVERAGE, GOOD, EXCELLENT;

    /**
     * This method create a link between a disparity value and 1 of N possibles Certification Values
     * @param val the disparity certificated
     * @param sharedPref the sharedPref of the android application (used to get the parameters for disparity)
     * @return a Certification_Value enum
     */
    public static Certification_Value intToCertificationValue(int val, SharedPreferences sharedPref) {
        int MAXDISPARITY = sharedPref.getInt("pref_maxdisparity", 0);
        int MINDISPARITY = sharedPref.getInt("pref_mindisparity", 0);
        int delta = (MAXDISPARITY - MINDISPARITY) / Certification_Value.values().length;
        if (val <= MINDISPARITY + delta) return EXCELLENT;
        else if (val >= MINDISPARITY + (delta * 2)) return GOOD;
        else if (val>= MINDISPARITY+ (delta*3)) return AVERAGE;
        else
            return POOR;
    }

    /**
     * This method is used to link Certification Values saved with Strings to their object
     * @param nameOfCertification the given string name
     * @return the correspondant object of Certification_Value
     */
    public static Certification_Value StringToCertificationValue(String nameOfCertification) {
        if(nameOfCertification.equals(Certification_Value.POOR.name())) return Certification_Value.POOR;
        else if (nameOfCertification.equals(Certification_Value.AVERAGE.name())) return Certification_Value.AVERAGE;
        else if(nameOfCertification.equals(Certification_Value.GOOD.name())) return Certification_Value.GOOD;
        else if (nameOfCertification.equals(Certification_Value.EXCELLENT.name())) return Certification_Value.EXCELLENT;
        else return null;
    }

}
