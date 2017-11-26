package beacon.project.com.beaconsystem.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import beacon.project.com.beaconsystem.Interface.RecycleViewOnclickListener;
import beacon.project.com.beaconsystem.POJO.ActivityPOJO;
import beacon.project.com.beaconsystem.R;


/**
 * Created by Dell4050 on 11/26/2017.
 */

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ActivityHolder> {
    private List<ActivityPOJO> mData = new ArrayList<>();
    private RecycleViewOnclickListener listener;

    public ActivityAdapter(RecycleViewOnclickListener listener){
        this.listener = listener;
    }

    public void upDateData( List<ActivityPOJO> mData ){
        this.mData = mData;
    }

    @Override
    public ActivityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_mainapps,parent,false);
        return new ActivityHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(ActivityHolder holder, int position) {
        ActivityPOJO activityPOJO = mData.get(position);
        holder.tv_name.setText(activityPOJO.getNameActivity());
        holder.tv_datesave.setText(activityPOJO.getDatesave());
        holder.setUrl(activityPOJO.getPath_photo());

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ActivityHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView Img;
        private TextView tv_name;
        private TextView tv_datesave;
        private RecycleViewOnclickListener listener;



        public ActivityHolder(View itemView,RecycleViewOnclickListener listener) {
            super(itemView);


            Img = itemView.findViewById(R.id.img_activity_list);
            tv_name =  itemView.findViewById(R.id.tv_row_name);
            tv_datesave = itemView.findViewById(R.id.tv_row_datesave);
            this.listener = listener;

            itemView.setOnClickListener(this);
        }

        private void setUrl(String url){
            Picasso.with(itemView.getContext()).load(url).into(Img);

        }


        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION){
                listener.onClick(v,position);
            }
        }
    }
}
