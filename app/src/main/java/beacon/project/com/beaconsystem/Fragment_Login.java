package beacon.project.com.beaconsystem;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

@SuppressLint("ValidFragment")
class Fragment_Login extends Fragment implements View.OnClickListener{
    private EditText user,pass;
    private String username,password;
    Context context;
    FirebaseAuth mAuthen;
    boolean state = false;
    private ProgressDialog progressDialog;

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

    private void checkUser(String userName,String passWord){



        mAuthen =  FirebaseAuth.getInstance();
        mAuthen.signInWithEmailAndPassword(userName,passWord)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            if(progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }
                            Toast.makeText(context, "เข้าสู่ระบบเสร็จสิ้น", Toast.LENGTH_SHORT).show();
                            goMainApps();


                        }
                        else
                        {
                            if(progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }

                            user.setText("");
                            pass.setText("");
                            Toast.makeText(getActivity().getApplicationContext(), "ไม่สามารถเข้าสู่ระบบได้", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void hide(View view){
        view = getActivity().getCurrentFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void goMainApps(){
        Intent goMain = new Intent(getActivity()
                                    .getApplicationContext()
                                    ,MainActivity.class);
        getActivity().startActivity(goMain);
        getActivity().finish();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLogin:  //if push  button login
                //get text username password
                hide(getView());

                username = user.getText().toString().trim();
                password = pass.getText().toString().trim();

                if(!username.isEmpty() && !password.isEmpty()){
                    progressDialog = new ProgressDialog(getContext());
                    progressDialog.setMessage("Loading.......");
                    progressDialog.show();
                    checkUser(username,password);
                }else {
                    Toast.makeText(context, "กรุณาเช็ค username และ password", Toast.LENGTH_SHORT).show();
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
