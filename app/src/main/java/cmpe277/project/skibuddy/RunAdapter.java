package cmpe277.project.skibuddy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cmpe277.project.skibuddy.common.EventParticipant;
import cmpe277.project.skibuddy.common.Run;

/**
 * Created by akankshanagpal on 12/2/15.
 */
public class RunAdapter extends BaseAdapter{

    private final Context context;
    private LayoutInflater inflater;
    private final List<Run> values;

    public RunAdapter(Context context, List<Run> values) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.values = values;
    }

    private class ViewHolder {
        public TextView label, tvStatus;
        public ImageView logo;
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
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder holder;

        if(convertView == null){
            view = inflater.inflate(R.layout.list_participant, parent, false);
            holder = new ViewHolder();
            holder.label = (TextView) view.findViewById(R.id.label);
            holder.logo = (ImageView) view.findViewById(R.id.logo);
            holder.tvStatus = (TextView)view.findViewById(R.id.tvStatus);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder)view.getTag();
        }

        Run run = values.get(position);

        // TODO: get objects from the user objects
        holder.label.setText("User name");
        holder.tvStatus.setText("User distance");

        // Change icon based on name
        //String s = run.getUser().getName();

        //System.out.println("events "+ s);
        holder.logo.setImageResource(R.drawable.invite);

        return view;
    }
}
