package com.example.saurabh.mess2owner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import static com.example.saurabh.mess2owner.MainActivity.MESS_ID;

public class CalenderActivity extends AppCompatActivity {


    ListView listView;
    TextView result;
    ArrayAdapter adapter2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);

        listView=(ListView)findViewById(R.id.PreviousMonthLV);

        adapter2 = new ArrayAdapter<HashMap<String,String>>(getApplicationContext(), R.layout.costum_listview);
        listView.setAdapter(adapter2);


        Calendar c = Calendar.getInstance();
        final String time = String.valueOf(c.get(Calendar.MONTH)+1);

        ArrayList<String> days = new ArrayList<String>();
        int thisDate = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        for (int i = 1; i <= thisDate; i++) {
            days.add(Integer.toString(i)+"-"+time);


        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, days);

        final Spinner spinYear = (Spinner)findViewById(R.id.spinner2);
        spinYear.setAdapter(adapter);



        spinYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final String dateSelected=spinYear.getItemAtPosition(i).toString();
                adapter2.clear();
                adapter2.notifyDataSetChanged();


                final int[] count1 = {0};

                FirebaseDatabase.getInstance().getReference().child("MessBuffer").child(MESS_ID)
                        .child("scanneddinner").child(dateSelected).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                      /*  adapter2.clear();
                        adapter2.notifyDataSetChanged();
*/

                        ArrayList<String> scannedArrayList = new ArrayList<>();
                        for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                            scannedArrayList.add("Dinner: "+dsp.getValue(String.class));
                            count1[0]++;
                        }


                        Log.v("E_VALUE",scannedArrayList+"");
                        Log.v("E_VALUE",dateSelected);

                        TextView textView2tp=(TextView)findViewById(R.id.textView2);
                      //  textView2tp.setText("Select Date | Total Students on "+dateSelected+" : "+ count1[0]);

                        for(String s : scannedArrayList)
                        {
                            adapter2.add(s);
                        }

                        adapter2.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                FirebaseDatabase.getInstance().getReference().child("MessBuffer").child(MESS_ID)
                        .child("scannedlunch").child(dateSelected).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                       /* adapter2.clear();
                        adapter2.notifyDataSetChanged();*/

                        int count2=0;

                        ArrayList<String> scannedArrayList2 = new ArrayList<>();
                        for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                            scannedArrayList2.add("Lunch: "+dsp.getValue(String.class));
                            count1[0]++;
                        }

                        TextView textView2tp=(TextView)findViewById(R.id.textView2);
                        textView2tp.setText("Select Date | Total Students on "+dateSelected+" : "+count1[0]);


                        Log.v("E_VALUE",scannedArrayList2+"");
                        Log.v("E_VALUE",dateSelected);



                        for(String s : scannedArrayList2)
                        {
                            adapter2.add(s);
                        }

                        adapter2.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });




            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }
}
