package beacon.project.com.beaconsystem;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Dell4050 on 11/14/2017.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText user,pass;
    private String username,password;
    private Button btnLogin,btnRegister;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login);
        getSupportActionBar().hide();
        init();

    }

    private void init(){
        //init btn
        findViewById(R.id.btnLogin).setOnClickListener(this);

        // init edit text
        user = findViewById(R.id.et_username);
        pass = findViewById(R.id.et_password);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btnLogin:  //if push  button login
                //get text username password
                username = user.getText().toString().trim();
                password = pass.getText().toString().trim();

                if(checkUser(username,password)){
                    Intent goMain = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(goMain);
                    finish();
                }else{
                    user.setText("");
                    pass.setText("");
                    Toast.makeText(this, "Login Fail.", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    boolean checkUser(String use,String pwd){
//        Toast.makeText(this, "user = "+use+" pass = "+pwd, Toast.LENGTH_SHORT).show();
        if(use.equals("admin") && pwd.equals("1234"))
            return true;
        else
            return false;
    }

}
