package beacon.project.com.beaconsystem.Fragment.User;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import beacon.project.com.beaconsystem.R;

public class FragmentHomeApp extends Fragment {
    private  FirebaseDatabase database;
    private  TextView tv_nameActivity,tv_detail,tv_dateStart,tv_dateEnd;
    private  ImageView imgActivity;
    private Bundle bundle = new Bundle();
    private String nameActivity,detailActivity,dateStartActitity,dateEndActivity,path_photo;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_app_activity
                ,container,false);
        try{
            initInstance(view);
        }catch (Exception e){
            Log.e("Error at Homeapp",e.getMessage());
        }

        return view;
    }

    private void initInstance(View v){

        tv_nameActivity = v.findViewById(R.id.tv_activityName);
        tv_detail = v.findViewById(R.id.tv_detailActivity);
        tv_dateStart = v.findViewById(R.id.tv_datetimeStart);
        tv_dateEnd = v.findViewById(R.id.tv_datetimeEnd);
        imgActivity =v.findViewById(R.id.imgActivity);

        bundle  = this.getArguments();

        if(bundle != null){

            //get date from bundle
            nameActivity = bundle.getString(FragmentListActivity.TAG_NAME_ACTIVITY);
            detailActivity = bundle.getString(FragmentListActivity.TAG_DETAIL_ACTIVITY);
            dateStartActitity =bundle.getString(FragmentListActivity.TAG_DATETIME_START);
            dateEndActivity = bundle.getString(FragmentListActivity.TAG_DATETIME_END);
            path_photo = bundle.getString(FragmentListActivity.TAG_PATH_PHOTO);


            // set imgage view
            Picasso.with(getContext()).load(path_photo).into(imgActivity);
            //set text view
            tv_nameActivity.setText(nameActivity);
            tv_detail.setText(detailActivity);
            tv_dateStart.setText(dateStartActitity);
            tv_dateEnd.setText(dateEndActivity);

        }


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mRef = database.getReference("TestPut");
        String timeStamp = new SimpleDateFormat("dd-mm-yyyy  HH:mm:ss").format(Calendar.getInstance().getTime());
        mRef.setValue(timeStamp);

    }
}
