package beacon.project.com.beaconsystem.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import beacon.project.com.beaconsystem.Adapter.ActivityAdapter;
import beacon.project.com.beaconsystem.Interface.RecycleViewOnclickListener;
import beacon.project.com.beaconsystem.POJO.ActivityPOJO;
import beacon.project.com.beaconsystem.R;

/**
 * Created by Dell4050 on 11/26/2017.
 */

public class FragmentListActivity extends Fragment{

    RecycleViewOnclickListener mlistener;
    public static String TAG_NAME_ACTIVITY = "nameActivity";
    public static String TAG_DETAIL_ACTIVITY = "detail";
    public static String TAG_DATETIME_START = "datetimeStart";
    public static String TAG_DATETIME_END = "datetimeEnd";
    public static String TAG_PATH_PHOTO= "worksheet";

    private List<ActivityPOJO> mData = new ArrayList<>();
    private ActivityAdapter activityAdapter;
    private RecyclerView recyclerView;
    private ActivityPOJO activityPOJO;
    private List<String> nameList,issuerList,dateStartList,dateEndList,detailActivityList,macBeaconlList;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity_list,container,false);
        init(view);
        return view;
    }

    private void init(View view){

        nameList = new ArrayList<>();
        issuerList = new ArrayList<>();
        dateStartList = new ArrayList<>();
        dateEndList = new ArrayList<>();
        detailActivityList = new ArrayList<>();
        macBeaconlList = new ArrayList<>();

        mlistener = (view1,position)->{

            Bundle bundle = new Bundle();
//            FragmentHomeApp homeApp = new FragmentHomeApp();

            bundle.putString(TAG_NAME_ACTIVITY,nameList.get(position));
            bundle.putString(TAG_PATH_PHOTO,mData.get(position).getPath_photo());
            bundle.putString(TAG_DATETIME_START,dateStartList.get(position));
            bundle.putString(TAG_DATETIME_END,dateEndList.get(position));
            bundle.putString(TAG_DETAIL_ACTIVITY,detailActivityList.get(position));
            Toast.makeText(getContext(), "Position = "+nameList.get(position), Toast.LENGTH_SHORT).show();
//            homeApp.setArguments(bundle);


            replaceFragment(new FragmentHomeApp(),bundle);

        };


        String url = "http://i.imgur.com/KjyvQ6I.jpg";
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
        mRef = mRef.child("Activity");
        Query query = mRef.limitToLast(5);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                Toast.makeText(getContext(), ""+dataSnapshot, Toast.LENGTH_SHORT).show();
                Map<String,Object> value = (Map<String, Object>) dataSnapshot.getValue();

                String activity_name = value.get("activity_name").toString();
                String date_issuer = value.get("date_issuer").toString();
                String datetime_end = value.get("datetime_end").toString();
                String datetime_start = value.get("datetime_start").toString();
                String detail_activity = value.get("detail_activity").toString();
                String mac_beacon = value.get("mac_beacon").toString();

                nameList.add(activity_name);
                issuerList.add(date_issuer);
                dateStartList.add(datetime_start);
                dateEndList.add(datetime_end);
                detailActivityList.add(detail_activity);
                macBeaconlList.add(mac_beacon);

                mData.add(new ActivityPOJO(activity_name,url,date_issuer));

                activityAdapter = new ActivityAdapter(mlistener);
                activityAdapter.upDateData(mData);

                recyclerView = view.findViewById(R.id.recycleViewActivity);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setHasFixedSize(true);

                recyclerView.setAdapter(activityAdapter);


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void replaceFragment(Fragment fragment, Bundle bundle) {

        if (bundle != null)
            fragment.setArguments(bundle);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment oldFragment = fragmentManager.findFragmentByTag(fragment.getClass().getName());

        //if oldFragment already exits in fragmentManager use it
        if (oldFragment != null) {
            fragment = oldFragment;
        }

        fragmentTransaction.replace(R.id.contentMain, fragment, fragment.getClass().getName());

        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

        fragmentTransaction.commit();
    }
}
