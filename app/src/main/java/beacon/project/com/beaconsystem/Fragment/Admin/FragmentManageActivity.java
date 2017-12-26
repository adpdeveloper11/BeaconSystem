package beacon.project.com.beaconsystem.Fragment.Admin;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import beacon.project.com.beaconsystem.POJO.Activity;
import beacon.project.com.beaconsystem.POJO.Member;
import beacon.project.com.beaconsystem.R;

/**
 * Created by Dell4050 on 12/6/2017.
 */

public class FragmentManageActivity extends Fragment implements View.OnClickListener{
    private ListView listActivity;
    private List<String> dataList;
    private ArrayAdapter<String> adapter;
    private String dateDisplay;

    private String activity_name;
    private String date_issuer;
    private String detail_activity;
    private String datetime_start;
    private String datetime_end;
    private String mac_beacon;
    private String select_Ble;
    private ArrayAdapter<String> arrayAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_manage_activity,container,false);

        try{

            init(view);

        }catch (Exception e){
            Log.e("Error",e.getMessage());
        }


        return view;
    }

    private void init(View view){

        dataList = new ArrayList<>();
        listActivity = view.findViewById(R.id.list_activity);

        view.findViewById(R.id.btn_addActivity).setOnClickListener(this);

        getDataActivity();

    }

   private void getDataActivity(){

       DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
       mRef = mRef.child("Activity");

       Query query  = mRef.limitToLast(5);
       query.addChildEventListener(new ChildEventListener() {
           @Override
           public void onChildAdded(DataSnapshot dataSnapshot, String s) {

               Map<String,Object> value = (Map<String,Object>) dataSnapshot.getValue();
               String activityName = value.get("activity_name").toString();
               dataList.add(activityName);

               if(adapter == null){
                   adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,dataList);
                   listActivity.setAdapter(adapter);
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


    private void insertAlert(){

        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
        mRef = mRef.child("Beacon");

        EditText et_nameActivity,et_description;
        TextView tv_timeStart,tv_timeEnd;
        Spinner spinner;


        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.add_activity,null);
        et_nameActivity = v.findViewById(R.id.A_et_ActivityName);
        et_description = v.findViewById(R.id.A_et_description);
        tv_timeStart = v.findViewById(R.id.tv_datetimeStart_alert);
        tv_timeEnd = v.findViewById(R.id.tv_datetimeEnd_alert);
        spinner = v.findViewById(R.id.sp_ble);

        List<String> mac = new ArrayList<>();
        List<String> names = new ArrayList<>();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        Query query = mRef.limitToLast(10);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String,Object> data = (Map<String, Object>) dataSnapshot.getValue();
                String nameBeacon = data.get("macAddress").toString();
                String name = data.get("name").toString();

                mac.add(nameBeacon);
                names.add(name);
                arrayAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,names);
                spinner.setAdapter(arrayAdapter);

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

        tv_timeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Calendar calendar = Calendar.getInstance();
                int yy = calendar.get(Calendar.YEAR);
                int mm = calendar.get(Calendar.MONTH);
                int dd = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        dateDisplay = String.valueOf(dayOfMonth)+"-"+String.valueOf(monthOfYear+1)+"-"+
                                String.valueOf(year);
                        tv_timeStart.setText(dateDisplay+" 00:00");
                    }
                }, yy, mm, dd);
                datePicker.show();

            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                select_Ble = mac.get(position);
//                Toast.makeText(getContext(), ""+select_Ble, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        tv_timeEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int yy = calendar.get(Calendar.YEAR);
                int mm = calendar.get(Calendar.MONTH);
                int dd = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        dateDisplay = String.valueOf(dayOfMonth)+"-"+String.valueOf(monthOfYear+1)+"-"+
                                String.valueOf(year);
                        tv_timeEnd.setText(dateDisplay+" 23:59");
                    }
                }, yy, mm, dd);
                datePicker.show();

            }
        });



        builder.setTitle("Insert Activity.");

        builder.setView(v);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(getContext(), "Data Select "+select_Ble, Toast.LENGTH_SHORT).show();
                activity_name = et_nameActivity.getText().toString().trim();
                date_issuer = getCurrentTime();
                detail_activity = et_description.getText().toString().trim();
                datetime_start = tv_timeStart.getText().toString();
                datetime_end  =  tv_timeEnd.getText().toString();
                mac_beacon = select_Ble;
                if(activity_name.isEmpty() || date_issuer.isEmpty()
                        || detail_activity.isEmpty()
                        || datetime_start.isEmpty()
                        || datetime_end.isEmpty()
                   )
                {
                    Toast.makeText(getContext(), "กรุณากรอกข้อมูลให้ครบ !!", Toast.LENGTH_SHORT).show();
                }
                    else
                {
                    Activity activity = new Activity(   activity_name,
                            date_issuer,
                            detail_activity,
                            datetime_start,
                            datetime_end,
                            mac_beacon
                    );

                    insertDataActivity(activity);
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

    private String getCurrentTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());
        return currentDateandTime;
    }

    private void insertDataActivity(Activity activity){

        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
        mRef = mRef.child("Activity");
        mRef.push().setValue(activity);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_addActivity:
                insertAlert();
                break;
        }
    }

}
