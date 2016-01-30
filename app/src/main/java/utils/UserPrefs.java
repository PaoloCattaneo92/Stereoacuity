package utils;

import android.content.Context;
import android.content.SharedPreferences;

//DEPRECATED AS 22/11/2015

/**
 * Created by paolo on 20/11/2015.
 * This class help the utilisation of SharedPreferences for the Users list
 */
public class UserPrefs {

    public static final String USERPREFS_NAME = "user_prefs";
    public static SharedPreferences userPref;
    private static SharedPreferences.Editor editor;

    private static final String KEY_PREFIX = "KEY";
    private static final String KEY_USERNAME = "KEY_USERNAME";
    private static final String KEY_CERTIFICATIONVALUE = "KEY_CERTIFICATIONVALUE";

    private static final String KEY_LASTUSER_USERNAME = "KEY_LASTUSER_USERNAME";


    /**
     * Constructor
     * @param ctx dunno
     */
    public UserPrefs(Context ctx) {
       if(userPref==null) {
           userPref = ctx.getSharedPreferences(USERPREFS_NAME, Context.MODE_PRIVATE);
       }
        editor = userPref.edit();
    }

    /**
     * Get User with his username saved in "Last_User"
     * @return last user that has been selected to be Last User
     */
    public User getLast() {
        String last_username = userPref.getString(KEY_LASTUSER_USERNAME, "");
        if(last_username.length()>0) {
            return getUser(last_username);
        } else return null;
    }

    /**
     * This method add a User to the preferences
     * @param user the new User object to add
     */
    public void addUser(User user) {
        if(user==null) return;

        String id = user.username;
        editor.putString(getFieldKey(id,KEY_USERNAME), user.username);
        editor.putString(getFieldKey(id,KEY_CERTIFICATIONVALUE), user.highestCertification.toString());
        editor.commit();
    }

    /**
     * This method get a User from the preferences using his username
     * @param username id to get the user
     * @return a User object with the given username and his found highest certification
     */
    public User getUser(String username) {
        String savedCertification = userPref.getString(getFieldKey(username,KEY_CERTIFICATIONVALUE),"");
        return new User(username, savedCertification);
    }

    /**
     * This method delete an user (username-highestcertificationvalue)
     * @param username the username of the User to kill
     */
    public void deleteUser(String username) {

        String id = username;
        editor.remove(getFieldKey(id,KEY_USERNAME));
        editor.remove(getFieldKey(id,KEY_CERTIFICATIONVALUE));
        editor.commit();
    }

    /** Auxiliar Methods */
    private String getFieldKey(String id, String fieldKey) {
        return KEY_PREFIX + id + "_" + fieldKey;
    }



}
