package beacon.project.com.beaconsystem;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import beacon.project.com.beaconsystem.Fragment.FragmentLogin;

public class LoginMainActivity extends AppCompatActivity {

    private Fragment fragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_login);
        getSupportActionBar().hide();

        fragmentManager = getSupportFragmentManager();
        fragment = new FragmentLogin(this);
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content,fragment).commit();

    }

    @Override
    public void onBackPressed() {
        if(fragment != null)
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();

        fragmentManager = getSupportFragmentManager();
        fragment = new FragmentLogin(this);
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content,fragment).commit();
    }
}
