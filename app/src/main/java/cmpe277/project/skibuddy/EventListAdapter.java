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
import cmpe277.project.skibuddy.common.ParticipationStatus;

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
        public TextView label, tvStatus,tvEventType;
        public ImageView logo1;
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
            view = inflater.inflate(R.layout.list_events, parent, false);
            holder = new ViewHolder();
            holder.label = (TextView) view.findViewById(R.id.label);
            holder.logo1 = (ImageView) view.findViewById(R.id.logo1);
            holder.tvStatus = (TextView)view.findViewById(R.id.tvStatus);
            holder.tvEventType = (TextView)view.findViewById(R.id.tvEventtype);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder)view.getTag();
        }

        EventRelation event = values.get(position);

        Object status = event.getParticipationStatus();
        String pStatus = new String();

        if(status == ParticipationStatus.HOST)
        {
            pStatus = "Hosted by you";
        }

        if(status == ParticipationStatus.INVITEE)
        {
            pStatus = "Invitation";
        }

        if(status == ParticipationStatus.PARTICIPANT)
        {
            pStatus = "Participant";
        }

        holder.label.setText(event.getName());
        //TODO: this used to be the host's name
        holder.tvStatus.setText("");
        holder.tvEventType.setText(pStatus);

        // Change icon based on name
        String s = event.getName();

        System.out.println("events "+ s);
        holder.logo1.setImageResource(R.drawable.event);

        return view;
    }
}
