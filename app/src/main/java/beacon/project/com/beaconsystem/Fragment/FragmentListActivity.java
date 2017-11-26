package beacon.project.com.beaconsystem.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import beacon.project.com.beaconsystem.Adapter.ActivityAdapter;
import beacon.project.com.beaconsystem.Interface.RecycleViewOnclickListener;
import beacon.project.com.beaconsystem.POJO.ActivityPOJO;
import beacon.project.com.beaconsystem.R;

/**
 * Created by Dell4050 on 11/26/2017.
 */

public class FragmentListActivity extends Fragment{

    public static String TAG_NAME_ACTIVITY = "nameActivity";
    public static String TAG_DETAIL_ACTIVITY = "detail";
    public static String TAG_DATETIME_START = "datetimeStart";
    public static String TAG_DATETIME_END = "datetimeEnd";
    public static String TAG_PATH_PHOTO= "worksheet";

    private List<ActivityPOJO> mData = new ArrayList<>();
    private ActivityAdapter activityAdapter;
    private RecyclerView recyclerView;
    private ActivityPOJO activityPOJO;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity_list,container,false);
        init(view);
        return view;
    }

    private void init(View view){


        String url = "https://www.tes.com/sites/default/files/styles/news_article_hero/public/news_article_images/smiling_teacher_3.jpg?itok=O0jei0lm";
        for (int i = 1;i<= 30;i++){
            mData.add(new ActivityPOJO("กิจกรรมที่ "+i,url,"12-11-201"+i));
        }

        RecycleViewOnclickListener mlistener = (view1,position)->{
            Bundle bundle = new Bundle();
            FragmentHomeApp homeApp = new FragmentHomeApp();

            bundle.putString(TAG_NAME_ACTIVITY,mData.get(position).getNameActivity());
            bundle.putString(TAG_PATH_PHOTO,mData.get(position).getPath_photo());
            bundle.putString(TAG_DATETIME_START,mData.get(position).getDatesave());
            bundle.putString(TAG_DETAIL_ACTIVITY," ทดสอบ ทดสอบ  ทดสอบ ทดสอบ  ทดสอบ ทดสอบ  ทดสอบ ทดสอบ  ทดสอบ ทดสอบ  ทดสอบ ทดสอบ  ทดสอบ ทดสอบ ");
            bundle.putString(TAG_DATETIME_END,"11-12-2017");


            homeApp.setArguments(bundle);
            FragmentListActivity fragment = new FragmentListActivity();
            getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.contentMain,homeApp)
                    .commit();


        };
        activityAdapter = new ActivityAdapter(mlistener);
        activityAdapter.upDateData(mData);

        recyclerView = view.findViewById(R.id.recycleViewActivity);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(activityAdapter);


    }
}
