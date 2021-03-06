package org.underpressureapps.unconflicto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import java.util.List;



public class UserCustomAdapter extends BaseAdapter {
    private Context context;
    private List<User> values;
    private View.OnClickListener listener;

    //@BindView(R.id.username_checkbox)  CheckBox checkBox;

    public UserCustomAdapter(Context context, List<User> values, View.OnClickListener listener) {
        this.context = context;
        this.values = values;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public Object getItem(int position) {
        return values.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        User user = values.get(i);
        if (view == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row_users, null);
        }

        CheckBox checkBox = (CheckBox)view.findViewById(R.id.username_checkbox);
        checkBox.setText(user.name);
        checkBox.setOnClickListener(listener);
        return view;
    }
}