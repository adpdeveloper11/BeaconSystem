package beacon.project.com.beaconsystem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import beacon.project.com.beaconsystem.Fragment.Admin.FragmentManageMember;
import beacon.project.com.beaconsystem.Fragment.User.FragmentLogin;
import beacon.project.com.beaconsystem.Fragment.Admin.FragmentManageActivity;
import beacon.project.com.beaconsystem.Fragment.Admin.FragmentManageBeacon;

public class AdminActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Handler scanHandler = new Handler();
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
        setContentView(R.layout.activity_admin);
        initMainApps();
    }

    private void  initMainApps(){

        bundle = getIntent().getExtras();
        if (bundle != null){
            name = bundle.getString(FragmentLogin.KEY_NAME);
            path_img  = bundle.getString(FragmentLogin.KEY_PATH);
            email = bundle.getString(FragmentLogin.KEY_EMAIL);
        }

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Admin");
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
//        Toast.makeText(this, ""+email, Toast.LENGTH_SHORT).show();
        //set name , email , picture
        View headerView = navigationView.getHeaderView(0);
        tv_name   = headerView.findViewById(R.id.tv_nameUseradmin);
        tv_email  = headerView.findViewById(R.id.tv_emailUserAdmin);
        img_user = headerView.findViewById(R.id.img_profile);
        tv_name.setText(name);
        tv_email.setText(email);
        Picasso.with(this).load(path_img).into(img_user);

        navigationView.setNavigationItemSelectedListener(this);
        replaceFragment(new FragmentManageMember(),null);

    }
    private void setFragment(Fragment fragment,String tag){

        getSupportFragmentManager().beginTransaction().add(R.id.contentAdmin,fragment
                ,tag).commit();
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onBackPressed() {
        replaceFragment(new FragmentManageMember(),null);
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
            case R.id.nav_add_user:
//                getSupportFragmentManager().beginTransaction().add(R.id.contentAdmin,new FragmentManageMember()).commit();
                replaceFragment(new FragmentManageMember(),null);
                break;
            case R.id.nav_activity:
//                getSupportFragmentManager().beginTransaction().add(R.id.contentAdmin,new FragmentManageActivity()).commit();
                replaceFragment(new FragmentManageActivity(),null);
                break;
            case R.id.nav_manageBLE:
//                getSupportFragmentManager().beginTransaction().add(R.id.contentAdmin,new FragmentManageBeacon()).commit();
                replaceFragment(new FragmentManageBeacon(),null);
                break;
            case R.id.nav_sigout:
                confirmExit("Logout");
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void confirmExit(final String msg){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
                                        Intent goLogin  = new Intent(AdminActivity.this,MainActivity.class);
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

        fragmentTransaction.replace(R.id.contentAdmin, fragment, fragment.getClass().getName());

        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

        fragmentTransaction.commit();
    }

}
