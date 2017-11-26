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
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import beacon.project.com.beaconsystem.Fragment.FragmentHomeApp;
import beacon.project.com.beaconsystem.Fragment.FragmentListActivity;
import beacon.project.com.beaconsystem.Fragment.FragmentShowDataUser;
import beacon.project.com.beaconsystem.Fragment.FragmentLogin;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private BluetoothManager btManager;
    private BluetoothAdapter btAdapter;
    private Handler scanHandler = new Handler();
    private int scan_interval_ms = 5000;
    private boolean isScanning = false;
    private String TAG = "Main Activity";
    private String name,email,path_img;
    private Bundle bundle = new Bundle();
    NavigationView navigationView;
    DrawerLayout drawer;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    ImageView img_user;
    TextView tv_name,tv_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    private void  initMainApps(){

        bundle = getIntent().getExtras();
        if (bundle != null){
            name = bundle.getString(FragmentLogin.KEY_NAME);
            path_img  = bundle.getString(FragmentLogin.KEY_PATH);
            email = bundle.getString(FragmentLogin.KEY_EMAIL);
        }

        getSupportFragmentManager().beginTransaction().add(R.id.contentMain,new FragmentListActivity()
                ,"FragmentMainApps").commit();

        //init BLE
        btManager = (BluetoothManager)getSystemService(Context.BLUETOOTH_SERVICE);
        btAdapter = btManager.getAdapter();

//        scanHandler.post(scanRunnable);   //start scan

        toolbar = findViewById(R.id.toolbar);
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
       FragmentHomeApp fragment = new FragmentHomeApp();
           getSupportFragmentManager().beginTransaction().remove(fragment).commit();

        getSupportFragmentManager().beginTransaction().add(R.id.contentMain,new FragmentListActivity()).commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
        try{
            scanHandler.removeCallbacksAndMessages(null);
        }catch (Exception e){
            Log.e(TAG,e.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_exit) {
            confirmExit("Exit");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.nav_main_app:
                getSupportFragmentManager().beginTransaction().add(R.id.contentMain,new FragmentListActivity()).commit();
                break;
            case R.id.nav_show_detail:
                getSupportFragmentManager().beginTransaction().add(R.id.contentMain,new FragmentShowDataUser()).commit();
                break;
            case R.id.nav_logout:
                confirmExit("Logout");
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
            int startByte = 2;
            boolean patternFound = false;
            while (startByte <= 5)
            {
                if (    ((int) scanRecord[startByte + 2] & 0xff) == 0x02 && //Identifies an iBeacon
                        ((int) scanRecord[startByte + 3] & 0xff) == 0x15)
                { //Identifies correct data length
                    patternFound = true;
                    break;
                }
                startByte++;
            }

            if (patternFound)
            {
                //Convert to hex String
                byte[] uuidBytes = new byte[16];
                System.arraycopy(scanRecord, startByte + 4, uuidBytes, 0, 16);
                String hexString = bytesToHex(uuidBytes);

                //UUID detection
                String uuid =  hexString.substring(0,8) + "-" +
                        hexString.substring(8,12) + "-" +
                        hexString.substring(12,16) + "-" +
                        hexString.substring(16,20) + "-" +
                        hexString.substring(20,32);

                // major
                final int major = (scanRecord[startByte + 20] & 0xff) * 0x100 + (scanRecord[startByte + 21] & 0xff);

                // minor
                final int minor = (scanRecord[startByte + 22] & 0xff) * 0x100 + (scanRecord[startByte + 23] & 0xff);

                Log.i("BEACON","UUID: " +uuid + "\\nmajor: " +major +"\\nminor" +minor);
            }

        }
    };

    static final char[] hexArray = "0123456789ABCDEF".toCharArray();
    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

}
