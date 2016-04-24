package edu.bentley.casca;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class displayDetail extends AppCompatActivity {

    private SQLHelper helper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_detail);

        helper = new SQLHelper(this);

        Bundle extras = getIntent().getExtras();
        String id = extras.getString("id");
        event ent = helper.queryEvent(id);

        Log.d("DebugDetailId", ent.getId()+"");
        Log.d("DebugDetailId", ent.getStartTime()+"");

    }
}
