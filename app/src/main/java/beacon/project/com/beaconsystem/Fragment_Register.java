package beacon.project.com.beaconsystem;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

import beacon.project.com.beaconsystem.R;


@SuppressLint("ValidFragment")
class Fragment_Register extends Fragment implements View.OnClickListener{
    private String emailRegis,passwordRegis,passwordCon,nameRegis;
    private FirebaseAuth mAuth;
    private EditText etName,etEmail,etPassword,etConPassword;
    private ProgressDialog progressDialog;
    Context context;

    public Fragment_Register(Context context){
        this.context = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.register_fragment, container,false);
        initInstance(view);
        return view;
    }

    private void initInstance(View view){

        etName = view.findViewById(R.id.et_Name_Regis);
        etEmail = view.findViewById(R.id.et_Email_Regis);
        etPassword = view.findViewById(R.id.et_Password_regis);
        etConPassword = view.findViewById(R.id.et_Confirm_Password);

        view.findViewById(R.id.btnRegisterToFirebase).setOnClickListener(this);

    }

    private void addUser(String email,String pass){
//        Toast.makeText(context, "EMAIL = "+email+" PASS = "+pass, Toast.LENGTH_SHORT).show();
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email,pass)
                .addOnCompleteListener((Activity) context,
                        new OnCompleteListener<AuthResult>() {
                            @RequiresApi(api = Build.VERSION_CODES.M)
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(progressDialog.isShowing()){
                                    progressDialog.dismiss();
                                }
                                if (task.isSuccessful()){
                                    alert("สมัครสมาชิก เรียบร้อย ");

                                    Fragment_Login register = new Fragment_Login(context);
                                    getActivity().getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.content,register).commit();

                                }else {
                                    alert("ไม่สามารถทำรายการได้ \n กรุณาลองใหม่่อีกครั้ง");
                                }
                            }
                });
    }
    private boolean checkInputText(){

        emailRegis = etEmail.getText().toString().trim();
         nameRegis = etName.getText().toString().trim();
         passwordRegis = etPassword.getText().toString().trim();
         passwordCon = etConPassword.getText().toString().trim();

         if(!passwordRegis.equals(passwordCon)){
            alert("กรุณาป้อน Password ให้ตรงกัน ");
            return false;
         }else  if(emailRegis.isEmpty() ||
            nameRegis.isEmpty() ||
            passwordRegis.isEmpty() ||
            passwordCon.isEmpty() )
         {
            alert("กรุณาเป้อนข้อมูลให้ครบ");
            return false;
         }else
             return true;
    }

    private void alert(String msg){
        Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnRegisterToFirebase:
                if(checkInputText()){
                    addUser(emailRegis,passwordRegis);
                    progressDialog = new ProgressDialog(context);
                    progressDialog.setMessage("Loading....");
                    progressDialog.show();
                }

                break;
        }
    }

}
