package org.underpressureapps.unconflicto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;


public class ScheduleCustomAdapter extends BaseAdapter {
    private Context context;
    private List<Block> values;


    //@BindView(R.id.username_checkbox)  CheckBox checkBox;

    public ScheduleCustomAdapter(Context context, List<Block> values) {
        this.context = context;
        this.values = values;

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
        Block block = values.get(i);
        if (view == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row_my_schedule, null);
        }
        String day = "";
        switch (block.getDay()){
            case "L":
                day = "Lunes";
                break;
            case "M":
                day = "Martes";
                break;
            case "I":
                day = "Miércoles";
                break;
            case "J":
                day = "Jueves";
                break;
            case "V":
                day = "Viernes";
                break;
            case "S":
                day = "Sábado";
                break;
            case "D":
                day = "Domingo";
                break;
        }


        TextView dia = (TextView) view.findViewById(R.id.tvDia);
        TextView clase = (TextView) view.findViewById(R.id.tvHora);
        dia.setText(day);
        String Clase = block.getCourseName() + "\n" + block.getStartHour() + " - " + block.getEndHour();
        clase.setText(Clase);
        return view;
    }
}