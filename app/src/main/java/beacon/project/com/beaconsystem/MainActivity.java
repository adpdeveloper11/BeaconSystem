package beacon.project.com.beaconsystem;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import beacon.project.com.beaconsystem.Fragment.User.FragmentListActivity;
import beacon.project.com.beaconsystem.Fragment.User.FragmentLogin;
import uk.co.alt236.bluetoothlelib.device.BluetoothLeDevice;
import uk.co.alt236.bluetoothlelib.device.beacon.BeaconType;
import uk.co.alt236.bluetoothlelib.device.beacon.BeaconUtils;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    //bluetooth
    private BluetoothManager btManager;
    private BluetoothAdapter btAdapter;
    private BluetoothLeDevice deviceLE;


    private Handler scanHandler = new Handler();
    private int scan_interval_ms = 5000;
    private boolean isScanning = false;
    private String TAG = "Main Activity";


    private String name,email,path_img;
    private int distance;

    //Component
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;
    private ImageView img_user;
    private TextView tv_name,tv_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    private void  initMainApps(){

        name = "กิจกรรมวิศวกรรมคอมพิวเตอร์";
        path_img = "http://lst.nectec.or.th/rmutl_dss/img/logo/gallery_20160623151545_9113338.png";
        email = "";


        //init BLE
        btManager = (BluetoothManager)getSystemService(Context.BLUETOOTH_SERVICE);
        btAdapter = btManager.getAdapter();

        scanHandler.post(scanRunnable);   //start scan

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("กิจกรรมวิศวกรรมคอมพิวเตอร์");
        setSupportActionBar(toolbar);


        drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);

        //set name , email , picture
        View headerView = navigationView.getHeaderView(0);
        tv_name   = headerView.findViewById(R.id.tv_nameUser);
        tv_email  = headerView.findViewById(R.id.tv_emailUser);
        img_user = headerView.findViewById(R.id.img_user);
        tv_name.setText(name);
        tv_email.setText(email);
        Picasso.with(this).load(path_img).into(img_user);

        navigationView.setNavigationItemSelectedListener(this);

        replaceFragment(new FragmentListActivity(),null);
    }


    @Override
    protected void onResume() {
        super.onResume();
        try {
            initMainApps();
        }catch (Exception e){
            Log.e(TAG,e.getMessage());
        }
    }

    @Override
    public void onBackPressed() {
        replaceFragment(new FragmentListActivity(),null);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{
            scanHandler.removeCallbacksAndMessages(null);
        }catch (Exception e){
            Log.e(TAG,e.getMessage());
        }
    }

    public void replaceFragment(Fragment fragment, Bundle bundle) {

        if (bundle != null)
            fragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.nav_main_app:
                    replaceFragment(new FragmentListActivity(),null);
                break;

            case R.id.nav_show_detail:
                    replaceFragment(new FragmentLogin(this),null);
                break;

            case R.id.nav_exit:
                confirmExit("Exit");
                break;

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void confirmExit(final String msg){

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Notification");
        builder.setMessage("Do you want "+msg+" ?")
        .setCancelable(true)

        .setPositiveButton(getResources().getString(R.string.yes)
                            , new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(msg.equals("Exit")){
                    finish();
                }else{
                    try{
                        scanHandler.removeCallbacksAndMessages(null);

                        Intent goLogin  = new Intent(MainActivity.this,LoginMainActivity.class);
                        startActivity(goLogin);
                        finish();

                    }catch (Exception e){
                        Log.e(TAG,e.getMessage());
                    }
                }
            }
        })
        .setNegativeButton(getResources().getString(R.string.no),
                             new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private Runnable scanRunnable = new Runnable()
    {
        @Override
        public void run() {

            if (isScanning)
            {
                if (btAdapter != null)
                {
                    btAdapter.stopLeScan(leScanCallback);
                }
            }
            else
            {
                if (btAdapter != null)
                {
                    btAdapter.startLeScan(leScanCallback);
                }
            }

            isScanning = !isScanning;

            scanHandler.postDelayed(this, scan_interval_ms);
        }
    };

    private BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback()
    {
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord)
        {
            deviceLE = new BluetoothLeDevice(device,rssi,scanRecord,System.currentTimeMillis());
            String scan = null;
            String strRSSI = String.valueOf(rssi);
            String strMac = String.valueOf(device.getAddress());
            int Rssi_int = Integer.parseInt(strRSSI.substring(1));
            if(BeaconUtils.getBeaconType(deviceLE) == BeaconType.IBEACON ){

                if (Rssi_int < 77){
//                    distance = getDistance(rssi,deviceLE.getTimestamp());
                    Log.e("DATA BLE","rssi = "+strRSSI+" MAC : "+strMac+" Distance = "+getDistance(rssi,-71)
                            +" rssi int = "+Rssi_int);
//                    Toast.makeText(MainActivity.this, "rssi = "+strRSSI+" MAC : "+strMac, Toast.LENGTH_SHORT).show();

                }
            }

        }
    };

    private void checkNotification(){

    }

    double getDistance(int rssi, int txPower) {
        float distance = (float) Math.pow(10d, ((double) txPower - rssi) / (10 * 2));

        return distance;
    }
}
