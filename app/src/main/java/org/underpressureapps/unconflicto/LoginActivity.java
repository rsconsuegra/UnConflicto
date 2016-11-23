package org.underpressureapps.unconflicto;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {


    @BindView(R.id.tv) TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Intent i = getIntent();
        Schedule schedule = (Schedule) i.getSerializableExtra("Schedule");
        String codigo = i.getStringExtra("Codigo");
        ButterKnife.bind(this);
        text.setText(schedule.getBlock(1).getCourseName().toString()+" "+codigo);
    }

    public void onClickIngreso(View view) {
    }
}
