package cmpe277.project.skibuddy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Robin on 12/1/2015.
 */
public class UserListAdapter extends ArrayAdapter<String>{
    private final Context context;
    private final String[] values;

    public UserListAdapter(Context context, String[] resource) {
        super(context, R.layout.user_list,resource);
        this.context = context;
        this.values = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.user_list, parent, false);

        TextView textView = (TextView) rowView.findViewById(R.id.tvUserName);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.ivProfile);

        textView.setText("Name");

        return rowView;
    }
}
