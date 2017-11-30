package beacon.project.com.beaconsystem.Fragment;

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
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import beacon.project.com.beaconsystem.R;


public class FragmentAdmin extends Fragment implements View.OnClickListener{

    private FirebaseDatabase database;
    private DatabaseReference mRef;
    private List<String> dMember = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter ;
    private ListView listViewMember;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_admin_main,container,false);
        init(view);
        return view;
    }

    private void init(View view){

        Toast.makeText(getContext(), "Wellcome to Admin.", Toast.LENGTH_SHORT).show();

        listViewMember = view.findViewById(R.id.list_user);

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
                    listViewMember.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                          insertMember();
                        }
                    });
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
        EditText et_nameUser,et_userName,et_password,et_email,et_per;
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.add_user,null);
            et_nameUser = alertLayout.findViewById(R.id.D_et_nameUser);
            et_userName = alertLayout.findViewById(R.id.D_et_username);
            et_password = alertLayout.findViewById(R.id.D_et_password);
            et_email = alertLayout.findViewById(R.id.D_et_email);
            et_per = alertLayout.findViewById(R.id.D_et_permission);


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Insert Member.");
        builder.setView(alertLayout);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), ""+et_nameUser.getText().toString().trim(), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onClick(View v) {

    }
}
