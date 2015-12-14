package cmpe277.project.skibuddy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Robin on 12/13/2015.
 */
public class NoUserAdapter  extends ArrayAdapter<String>{
    private final Context context;
    String[] nuser;

    public NoUserAdapter(Context context, int resource, String[] nouser) {
        super(context, resource,  nouser);
        this.context = context;
        this.nuser = nouser;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = convertView;
        if(rowView == null)
        {
            LayoutInflater vi ;
            vi = LayoutInflater.from(getContext());
            rowView= vi.inflate(R.layout.user_list, parent, false);
        }

        TextView textView = (TextView) rowView.findViewById(R.id.tvUserName);
        textView.setText(nuser[position]);

        Button inv = (Button) rowView.findViewById(R.id.ibTick);
        inv.setVisibility(View.GONE);

        return rowView;
    }
}

