package beacon.project.com.beaconsystem;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Dell4050 on 11/21/2017.
 */

public class FragmentHomeApp extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_app_activity
                ,container,false);
                initInstance(view);
        return view;
    }

    private void initInstance(View view){

    }
}
