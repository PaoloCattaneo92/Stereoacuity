package paolocattaneo.stereoacuity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import utils.Session;

public class ResultsActivity extends AppCompatActivity {

    String actualUser;
    ArrayList<Session> sessions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        //get User
        Bundle dataFromMain = getIntent().getExtras();
        actualUser = dataFromMain.getString("actualUser");

        //search local sessions


        //set listview
        ListView listView_sessions = (ListView) findViewById(R.id.listview_sessions);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,sessions) ;
        listView_sessions.setAdapter(adapter);


    }
}
