package beacon.project.com.beaconsystem;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import beacon.project.com.beaconsystem.R;



@SuppressLint("ValidFragment")
class Fragment_Login extends Fragment implements View.OnClickListener{
    private EditText user,pass;
    private String username,password;
    Context context;
    FirebaseAuth mAuthen;
    boolean state = false;

    public Fragment_Login(Context context){
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.login_fragment,
                container,
                false);
        initInstance(view);
        return view;
    }

    private void initInstance(View view){

        view.findViewById(R.id.btnLogin).setOnClickListener(this);
        view.findViewById(R.id.btnRegister).setOnClickListener(this);
        // init edit text
        user = view.findViewById(R.id.et_username);
        pass = view.findViewById(R.id.et_password);

    }
    boolean checkUser(String userName,String passWord){

        mAuthen =  FirebaseAuth.getInstance();
        mAuthen.signInWithEmailAndPassword(userName,passWord)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            state = true;
//                            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                        }else{
                            state = false;
                        }
                    }
                });

        return state;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLogin:  //if push  button login
                //get text username password
                username = user.getText().toString().trim();
                password = pass.getText().toString().trim();

                if(checkUser(username,password)){
                    Toast.makeText(context, "เข้าสู่ระบบเสร็จสิ้น", Toast.LENGTH_SHORT).show();
                }else{
                    user.setText("");
                    pass.setText("");
                    Toast.makeText(getActivity().getApplicationContext(), "ไม่สามารถเข้าสู่ระบบได้", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnRegister:

                Fragment_Register register = new Fragment_Register(context);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content,register).commit();
                break;
        }
    }


}
