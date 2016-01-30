package paolocattaneo.stereoacuity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import utils.Certification_Value;
import utils.User;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    /**DEBUG TAG */
    private static final String TAG = TestActivity.class.getSimpleName();


    /**
     * ArrayList of all the users/usernames loaded
     */
    ArrayList<User> usersList = new ArrayList<>();
    ArrayList<String> usernamelist = new ArrayList<>();

    /**
     * Preferences: used only to remember the last user of the app
     */
    public static final String PREFS = "prefs";    //preferences file name
    public static final String LAST_USER = "last_username";
    public SharedPreferences sharedPref;
    private static SharedPreferences.Editor editor;


    /**
     * userslist FILE management
     */
    String FILENAME = "foo3.txt";
    String newUser = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

       sharedPref = getSharedPreferences(PREFS, MODE_PRIVATE);
        editor = sharedPref.edit(); editor.apply();

        //read from FILENAME a list of all the saved users and show them in the listview
        usersList = Read();
        for(User u : usersList) usernamelist.add(u.username);
        ListView users_listview = (ListView) findViewById(R.id.users_listview);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, usernamelist);
        users_listview.setAdapter(adapter);

        //press an item to set user
        users_listview.setOnItemClickListener(this);


    }

    /**
     * This method reads the file FILENAME and gives you the Arraylist of users read
     * @return an ArrayList<User> with all the users saved in the file
     */
    public ArrayList<User> Read() {
        InputStream in;
        BufferedReader reader;
        String line;
        String[] elements;


        try {
            in = openFileInput(FILENAME);
            reader = new BufferedReader(new InputStreamReader(in));

            line = reader.readLine();
            while(line!=null) {
                Log.d(TAG, "I've read: " + line);
                elements = line.split("-");
                usersList.add(new User(elements[0],elements[1]));
                line = reader.readLine();
            }
            Log.d(TAG, "End of UsersList reached");
        } catch (Exception e) {
            Log.d(TAG,"EXCEPTION IN Read(): "+e.getMessage());
            e.printStackTrace();
        }

        return usersList;
    }

    /**
     * This method write a new line at the end of the users list file.
     * Or at least I hope so.
     * @param newLine new line that you want at the end of your file.
     */
    public void Write(String newLine) {

        InputStream in;
        BufferedReader reader;
        String line;
        String buff ="";

        try {
            in = openFileInput(FILENAME);
            reader = new BufferedReader(new InputStreamReader(in));

            line = reader.readLine();
            while(line!=null) {
                Log.d(TAG, "Line: " + line);
                buff += line;
                Log.d(TAG, "Buff: " + buff);
                line = reader.readLine();
            }
        } catch (Exception e) {
            Log.d(TAG,"EXCEPTION IN Write(String newLine) nella parte dove legge: "+e.getMessage());
            e.printStackTrace();
        }

        newLine = buff+"\n\n"+newLine;
        Log.d(TAG, "Newline: "+newLine);

        try {
            Log.d(TAG,"I'm writing");
            FileOutputStream out = openFileOutput(FILENAME, MODE_PRIVATE);
            OutputStreamWriter writer = new OutputStreamWriter(out);
            writer.write(newLine);
            Log.d(TAG, "In teoria ho writato");
            writer.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG,"EXCEPTION IN Write(String newLine) nella parte dove SCRIVE (filenotfound): "+e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            Log.d(TAG,"EXCEPTION IN Write(String newLine) nella parte dove SCRIVE (ioeexception): "+e.getMessage());
            e.printStackTrace();
        }

    }

    public void back(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        //go back, do nothing
    }


    public void confirmLogin(View view) throws IOException {

        EditText editText_input = (EditText)findViewById(R.id.edittext_username);
        newUser = String.valueOf(editText_input.getText());
        newUserToMainActivity();

        String newLine = newUser+"-"+ Certification_Value.POOR.name();
        Log.d(TAG,"New line to write is: "+newLine);
        Write(newLine);

    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Log.d(TAG, position + ": " + usernamelist.get(position));
        newUser = usernamelist.get(position);
        newUserToMainActivity();

    }

    private void newUserToMainActivity() {

        //intent.putExtra("changedUser", ""); non serve perchè lo salvo nelle shared come lastuser
                        //e lo riapre dal main
        editor.putString(LAST_USER,newUser);
        editor.commit();
        Log.d(TAG,"New User set: "+newUser);
        Log.d(TAG, "In fact we can read in the sharedPref last user="+ sharedPref.getString(LAST_USER,"lolwut?"));

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }


}
