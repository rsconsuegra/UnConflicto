package org.underpressureapps.unconflicto;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;


public class MySchedule extends AppCompatActivity implements ValueEventListener {
    private TopicInviteInteractionListener listener;
    private ScheduleCustomAdapter adapter = null;
    private ListView users_list;
    private String topic;
    private List<Block> blocks;

    public MySchedule() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_schedule_list);

        Intent i = getIntent();
        String codigo = i.getStringExtra("Codigo");
        
        ButterKnife.bind(this);

        // Load the users from the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference scheduleRef = database.getReference("users/" + codigo + "/schedule");

        Log.d("TAG", "Called");
        scheduleRef.addValueEventListener(this);
    }


    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        List<Block> blocks = new ArrayList<Block>();
        for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
            Block block = userSnapshot.getValue(Block.class);
            blocks.add(block);
        }

        this.blocks = blocks;
        adapter = new ScheduleCustomAdapter(this, blocks);
        users_list.setAdapter(adapter);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    public interface TopicInviteInteractionListener {

    }
}