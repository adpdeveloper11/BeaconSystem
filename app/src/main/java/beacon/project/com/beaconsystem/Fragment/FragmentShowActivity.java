package beacon.project.com.beaconsystem.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import beacon.project.com.beaconsystem.R;

/**
 * Created by Dell4050 on 11/26/2017.
 */

public class FragmentShowActivity extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v  =inflater.inflate(R.layout.fragment_showall_activity,container,false);
        return v;
    }
}
