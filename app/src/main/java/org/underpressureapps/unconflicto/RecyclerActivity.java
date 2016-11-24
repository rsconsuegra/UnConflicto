package org.underpressureapps.unconflicto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import butterknife.BindView;

public class RecyclerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);

        Intent i = getIntent();
        Schedule schedule = (Schedule) i.getSerializableExtra("Schedule");
        String codigo = i.getStringExtra("Codigo");
    }
}
