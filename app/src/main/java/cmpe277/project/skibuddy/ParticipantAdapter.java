package cmpe277.project.skibuddy;

import android.content.Context;
import android.support.v7.widget.RecyclerView.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cmpe277.project.skibuddy.common.EventParticipant;
import cmpe277.project.skibuddy.common.EventRelation;

/**
 * Created by akankshanagpal on 12/2/15.
 */
public class ParticipantAdapter extends BaseAdapter {

    private final Context context;
    private LayoutInflater inflater;
    private final List<EventParticipant> values;


    public ParticipantAdapter(Context context, List<EventParticipant> values) {
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

        EventParticipant participant = values.get(position);
        holder.label.setText(participant.getName());
        holder.tvStatus.setText(participant.getParticipationStatus().toString());

        // Change icon based on name
        String s = participant.getName();

        LoadProfilePicture profilePicture  = new LoadProfilePicture(holder.logo);
        profilePicture.loadPicture(participant);

        System.out.println("events " + s);

        return view;
    }
}
