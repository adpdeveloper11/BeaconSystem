package beacon.project.com.beaconsystem.Fragment;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Map;

import beacon.project.com.beaconsystem.AdminActivity;
import beacon.project.com.beaconsystem.MainActivity;
import beacon.project.com.beaconsystem.R;

@SuppressLint("ValidFragment")
public class FragmentLogin extends Fragment implements View.OnClickListener{
    private EditText user,pass;
    private String username,password;
    Context context;
    private ProgressDialog progressDialog;
    public static String KEY_NAME = "name_extra";
    public static String KEY_EMAIL = "email_extra";
    public static String KEY_PATH = "path_extra";


    public FragmentLogin(Context context){
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_login,
                container,
                false);
        initInstance(view);
        return view;
    }

    private void initInstance(View view){

        view.findViewById(R.id.btnLogin).setOnClickListener(this);
        // init edit text
        user = view.findViewById(R.id.et_username);
        pass = view.findViewById(R.id.et_password);
//        checkMember("admin","admin");
    }

    private void checkMember(final String userCheck, final String passCheck){

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mRef = firebaseDatabase.getReference();

        mRef =  mRef.child("member");
        Query query = mRef.limitToFirst(30);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Map<String,Object> valeRes = (Map<String, Object>) dataSnapshot.getValue();
                String username = valeRes.get("username").toString();
                String password = valeRes.get("password").toString();
                String names = valeRes.get("nameuser").toString();
                String per = valeRes.get("permission").toString();
                String path = valeRes.get("path").toString();
                String email = valeRes.get("email").toString();

                if(userCheck.equals(username) && passCheck.equals(password)){
                    Toast.makeText(getContext(), "Login successfully.", Toast.LENGTH_SHORT).show();
                    goMainApps(names,path,per,email);    //go main app

                }else{

                    if (progressDialog.isShowing()){
//                        Toast.makeText(getContext(), "Login Fail.", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                    user.setText("");
                    pass.setText("");

                }

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

    private void hide(View view){
        view = getActivity().getCurrentFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void goMainApps(String nameMember,String pathMember,String perMember,String emailMember){
        if(perMember.equals("u")){
            Intent goMain = new Intent(getActivity().getApplicationContext()
                    ,MainActivity.class);

            //send data to main app
            goMain.putExtra(KEY_NAME,nameMember);
            goMain.putExtra(KEY_EMAIL,emailMember);
            goMain.putExtra(KEY_PATH,pathMember);

            getActivity().startActivity(goMain);
            getActivity().finish();

        }else {

            Intent goAdmin = new Intent(getActivity().getApplicationContext()
                    ,AdminActivity.class);

            //send data to main app
            goAdmin.putExtra(KEY_NAME,nameMember);
            goAdmin.putExtra(KEY_EMAIL,emailMember);
            goAdmin.putExtra(KEY_PATH,pathMember);

            getActivity().startActivity(goAdmin);
            getActivity().finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLogin:  //if push  button login
                //get text username password
                try{
                    username = user.getText().toString().trim();
                    password = pass.getText().toString().trim();

                    if(!username.isEmpty() && !password.isEmpty()){
                        progressDialog = new ProgressDialog(getContext());
                        progressDialog.setMessage("Loading.......");
                        progressDialog.show();
                        checkMember(username,password);
                    }else {
                        Toast.makeText(context, "กรุณาเช็ค username และ password", Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    Log.e("Error Fragment login",e.getMessage());
                }
                break;
        }
    }
}
