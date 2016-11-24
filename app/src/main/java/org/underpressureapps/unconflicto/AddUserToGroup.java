package org.underpressureapps.unconflicto;

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
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AddUserToGroup extends AppCompatActivity implements ValueEventListener {
    private AddUserInteractionListener listener;
    private UserCustomAdapter adapter = null;
    private String topic;
    private List<User> users;
    private List<String> codigos = new ArrayList<String>();
    private List<Schedule> schedules = new ArrayList<Schedule>();
    //private List<Block> bloq = new ArrayList<Block>();

    @BindView(R.id.users_list)  ListView users_list;

    public AddUserToGroup() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.users_list);

        Intent i = getIntent();
        String codigo = i.getStringExtra("Codigo");
        ButterKnife.bind(this);

            //listener = (AddUserInteractionListener) this;

            //Bundle args = getArguments();
            //topic = args.getString("topic");
            // Load the users from the database
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference usersRef = database.getReference("users");

            Log.d("TAG", "Called");
            usersRef.addValueEventListener(this);


    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        List<User> users = new ArrayList<User>();
        for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
            User user = userSnapshot.getValue(User.class);
            users.add(user);
        }

        this.users = users;
        adapter = new UserCustomAdapter(this, users, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox checkbox = (CheckBox)view;

                // Get the selected user's uid
                String cod = "";
                for (User u : AddUserToGroup.this.users) {
                    if (u.name.equals(checkbox.getText())) {
                        cod = u.uni_code;
                        break;
                    }
                }

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference accessListRef = database.getReference("users/" + "name/");

                if (checkbox.isChecked()) {
                    codigos.add(cod);
                } else {
                    codigos.remove(codigos.indexOf(cod));
                }
            }
        });
        users_list.setAdapter(adapter);
    }


    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    public void onClickCrearGrupo(View view) {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(String cod : codigos) {
                    //obtener horario de cada usuario seleccionado y hacer matriz de conflicto.
                    dataSnapshot.child("users").child(cod).child("schedule").child("Blocks").getValue();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public interface AddUserInteractionListener {

    }
}
