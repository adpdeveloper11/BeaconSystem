package beacon.project.com.beaconsystem.Fragment.Admin;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconData;
import org.altbeacon.beacon.BeaconDataNotifier;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import beacon.project.com.beaconsystem.Adapter.BeaconAdapter;
import beacon.project.com.beaconsystem.POJO.BeaconDataPojo;
import beacon.project.com.beaconsystem.R;

/**
 * Created by Dell4050 on 12/6/2017.
 */

public class FragmentManageBeacon extends Fragment implements View.OnClickListener,AdapterView.OnItemSelectedListener{

    protected static final String TAG = "RangingActivity";
    private BeaconManager beaconManager ;
    private ListView listView;
    private TextView statusView;
    private BeaconAdapter beaconAdapter;
    private Context context;
    private BluetoothAdapter mBluetoothAdapter;
    private List<String> mFindBle;
    private ArrayAdapter mArr;
    private DatabaseReference mRef;
    private DatabaseReference mRefSave;
    BroadcastReceiver mReceiver;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_beacon,container,false);
        try{
            init(view);
        }catch (Exception e){
            Log.e("Error at Beacon ",e.getMessage());
        }

        return view;
    }

    private void init(View view){

        context = getContext();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter(); // get a handle on the bluetooth radio

        mFindBle = new ArrayList<>();

        listView = view.findViewById(R.id.listBeacon);
        view.findViewById(R.id.add_beacon).setOnClickListener(this);
//        this.statusView = (TextView) view.findViewById(R.id.currentStatus);
        listView.setOnItemClickListener(this::onItemSelected);
        mRef = FirebaseDatabase.getInstance().getReference();
        mRef  = mRef.child("Beacon");

        getInitData();
    }

    private void getInitData(){

        Query query = mRef.limitToLast(20);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String,Object> data = (Map<String, Object>) dataSnapshot.getValue();
                String name = data.get("name").toString();
                String macAddress = data.get("macAddress").toString();
                mFindBle.add(name+"\n"+macAddress);

                mArr = new ArrayAdapter(context,android.R.layout.simple_list_item_1,mFindBle);
                listView.setAdapter(mArr);

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

    private void addBeacon(){

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        builderSingle.setIcon(R.drawable.ic_bluetooth_searching);
        builderSingle.setTitle("เลือกอุปกรณ์");
        ProgressBar progressBar = new ProgressBar(context);

        builderSingle.setView(progressBar);
//        TextView textView = new TextView(context);
//        textView.setText("กดเลือกเพื่อบันทึก iBeacon");
//        textView.setGravity(Gravity.CENTER);
//        builderSingle.setView(textView);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                getContext(),
                android.R.layout.select_dialog_singlechoice);

        mBluetoothAdapter.startDiscovery();
        mReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                //Finding devices
                if (BluetoothDevice.ACTION_FOUND.equals(action))
                {
                    // Get the BluetoothDevice object from the Intent
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    // Add the name and address to an array adapter to show in a ListView
//                    Toast.makeText(context, ""+device.getAddress(), Toast.LENGTH_SHORT).show();
                    arrayAdapter.add(device.getName() + "\n" + device.getAddress());
                }
            }
        };

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        getActivity().registerReceiver(mReceiver, filter);


        builderSingle.setAdapter(arrayAdapter,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        String ibeacon =  arrayAdapter.getItem(position);
                        String[] arrSub = ibeacon.split("\n");
//                        mRef.push().setValue(ibeacon);
                        configBeacon(arrSub[1]);

//                        Toast.makeText(context, ""+ibeacon, Toast.LENGTH_SHORT).show();
                    }
                });
        builderSingle.show();
    }

    private void configBeacon(String macAddress){

        AlertDialog.Builder builder  = new AlertDialog.Builder(context);
        builder.setTitle("ใส่ชื่อ Beacon : "+macAddress);
        builder.setIcon(R.drawable.ic_bluetooth_searching);
        EditText editText = new EditText(context);
        editText.setHint("ชื่อ iBeacon");
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(editText);
        builder.setPositiveButton("บันทึก", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nameInput = editText.getText().toString().trim();
                if(nameInput.length() > 0){
                    BeaconDataPojo pojoData = new BeaconDataPojo(macAddress,nameInput);
                    mRef.push().setValue(pojoData);
                    Toast.makeText(context, "Save data Successfully.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        builder.show();

    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_beacon:

                addBeacon();

                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(context, "Data = "+mFindBle.get(position), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
