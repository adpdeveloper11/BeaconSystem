package beacon.project.com.beaconsystem.Fragment.Admin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import beacon.project.com.beaconsystem.POJO.Member;
import beacon.project.com.beaconsystem.R;


public class FragmentManageMember extends Fragment implements View.OnClickListener{

    private FirebaseDatabase database;
    private DatabaseReference mRef;
    private List<String> dMember = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter ;
    private ListView listViewMember;


    // value get member user
    private String username,nameuser,password,email, permission, path;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_admin_main,container,false);
        try{
            init(view);
        }catch (Exception e){
            Log.e("Error at Member",e.getMessage());
        }

        return view;
    }

    private void init(View view){

        Toast.makeText(getContext(), "Wellcome to Admin.", Toast.LENGTH_SHORT).show();

        listViewMember = view.findViewById(R.id.list_user);
        view.findViewById(R.id.btn_add_admin).setOnClickListener(this);
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference();

        getMemberList();

    }

    private void getMemberList(){

        mRef = mRef.child("member");
        Query query =  mRef.limitToFirst(20);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String,Object> getVal = (Map<String,Object>) dataSnapshot.getValue();
                String nameUser = getVal.get("nameuser").toString();

                dMember.add("Username : "+nameUser);

                if(arrayAdapter ==null){
                    arrayAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,dMember);
                    listViewMember.setAdapter(arrayAdapter);

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

    private void insertMember(){

        EditText et_nameUser,et_userName,et_password,et_email;

        Spinner permissionSP;

        List<String> perVal = new ArrayList<>();
        perVal.add("admin");
        perVal.add("user");

        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.add_user,null);
            et_nameUser = alertLayout.findViewById(R.id.D_et_nameUser);
            et_userName = alertLayout.findViewById(R.id.D_et_username);
            et_password = alertLayout.findViewById(R.id.D_et_password);
            et_email = alertLayout.findViewById(R.id.D_et_email);
            permissionSP = alertLayout.findViewById(R.id.sp_permission);

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,perVal);
            permissionSP.setAdapter(arrayAdapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Insert Member.");

        builder.setView(alertLayout);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(et_email.length() < 0
                        || et_nameUser.length() <0
                        || et_userName.length() < 0
                        || et_password.length()< 0
                   )
                {
                    Toast.makeText(getContext(), "กรุณากรอกข้อมูลให้ครบ !!!", Toast.LENGTH_SHORT).show();

                }else {

                    path = "http://profile2.chiangraisoftware.com/img/admin.png";
                    username = et_userName.getText().toString().trim();
                    nameuser = et_nameUser.getText().toString().trim();
                    email  = et_email.getText().toString().trim();
                    password = et_password.getText().toString().trim();
                    permission = permissionSP.getSelectedItem().toString().substring(0,1);
                    Member member = new Member(username,nameuser,password,email, permission, path);
                    inputMember(member);

                }

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
            }
        });
      builder.show();
    }

    private void inputMember(Member member){

        DatabaseReference mRef2 = FirebaseDatabase.getInstance().getReference();
        mRef2 = mRef2.child("member");
        mRef2.push().setValue(member);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_add_admin:
                insertMember();
                break;
        }
    }
}
