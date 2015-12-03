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
import cmpe277.project.skibuddy.common.EventRelation;

/**
 * Created by akankshanagpal on 12/2/15.
 */
public class EventListAdapter extends BaseAdapter {

    private final Context context;
    private LayoutInflater inflater;
    private final List<EventRelation> values;

    public EventListAdapter(Context context, List<EventRelation> values) {
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

        EventRelation event = values.get(position);
        holder.label.setText(event.getName());
        holder.tvStatus.setText(event.getHost().getName());

        // Change icon based on name
        String s = event.getName();

        System.out.println("events "+ s);
        holder.logo.setImageResource(R.drawable.invite);

        return view;
    }
}
