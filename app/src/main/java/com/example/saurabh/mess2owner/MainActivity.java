package com.example.saurabh.mess2owner;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    Button scanbtn,nextbtn;
    ListView listView;
    TextView result;
    ArrayAdapter adapter;
    Map UserMap = new HashMap();


    public static final int REQUEST_CODE = 100;
    public static final int PERMISSION_REQUEST = 200;
    public static User UserDataObj;
    public static Map<String,String> timeMap;
    public static String MESS_ID="Mess2";
    HashMap<String,String> map2=new HashMap<>();
    private DatabaseReference mDatabaseUser= FirebaseDatabase.getInstance().getReference().child("users");
    private boolean connected;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // FirebaseApp.initializeApp(this);
        scanbtn = (Button) findViewById(R.id.scanbtn);
        nextbtn = (Button) findViewById(R.id.NextBtn);


        result = (TextView) findViewById(R.id.result);
        listView = (ListView) findViewById(R.id.ScanlistView);



            FirebaseAuth.getInstance().signInWithEmailAndPassword("anandmess@messedup.com", "anandfoodxprs")
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {


                            adapter = new ArrayAdapter<HashMap<String, String>>(getApplicationContext(), R.layout.costum_listview);
                            listView.setAdapter(adapter);

                            populateListView();


                            getCurrentDate();

                            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST);
                            }

                            scanbtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    if (isConnected()) {

                                        Vibrator vib = (Vibrator) getBaseContext().getSystemService(Context.VIBRATOR_SERVICE);
                                        vib.vibrate(20);
                                        Intent intent = new Intent(MainActivity.this, ScanActivity.class);
                                        startActivityForResult(intent, REQUEST_CODE);
                                    } else {
                                        Vibrator vib = (Vibrator) getBaseContext().getSystemService(Context.VIBRATOR_SERVICE);
                                        long[] pattern = {0, 75, 100, 75};

                                        // The '-1' here means to vibrate once, as '-1' is out of bounds in the pattern array
                                        vib.vibrate(pattern, -1);
                                        Toast.makeText(getBaseContext(), "No Internet! Please Check your Connection", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });


                            nextbtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent MonthDetails = new Intent(MainActivity.this, CalenderActivity.class);
                                    startActivity(MonthDetails);
                                }
                            });
                        }
    });


}



    private void addinShared(String uid,String value) {

try {
    SharedPreferences mPrefs = getPreferences(MODE_PRIVATE);

    Gson gson = new Gson();
    String json = mPrefs.getString("MyObject", "");
    HashMap<String, String> obj=new HashMap<>();
    if(gson.fromJson(json, HashMap.class)!=null)
        obj = gson.fromJson(json, HashMap.class);


    obj.put(uid, value);


    SharedPreferences mPrefs2 = getPreferences(MODE_PRIVATE);

    SharedPreferences.Editor prefsEditor2 = mPrefs2.edit();
    Gson gson2 = new Gson();
    String json2 = gson2.toJson(obj);
    prefsEditor2.putString("MyObject", json2);
    prefsEditor2.apply();
    prefsEditor2.commit();

    populateListView();

}
catch (Exception e)
{
   // Toast.makeText(MainActivity.this,"ALA RE ALA 2",Toast.LENGTH_LONG).show();

}
    }

    private void populateListView() {


        Log.v("E_VALUE","INSIDE POPULATELISTVIEW ---- ");


       // File file = new File(getDir("data", MODE_PRIVATE), "map");

        try{
            SharedPreferences  mPrefs = getPreferences(MODE_PRIVATE);

            Gson gson = new Gson();
            String json = mPrefs.getString("MyObject", "");
            HashMap<String,String> obj = gson.fromJson(json, HashMap.class);
          //  ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file));
           // Object tempmap=inputStream.readObject();

          // HashMap<String,String> mapmap=((HashMap<String,String>) tempmap);
          //  Log.v("E_VALUE","INSIDE POPULATELISTVIEW  SHOW MAP---- " + mapmap);



            for(String s :obj.values())
                adapter.add(s);
            adapter.notifyDataSetChanged();
            // inputStream.flush();
          //  inputStream.close();
        }
        catch(Exception e)
        {
            Log.v("E_VALUE","EXCEPTION--- ");

         //   Toast.makeText(MainActivity.this,"ALA RE ALA",Toast.LENGTH_LONG).show();
        }


        /*try {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
            outputStream.writeObject(UserMap);
            outputStream.flush();
            outputStream.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }*/
    }

    private void getCurrentDate() {
        timeMap= ServerValue.TIMESTAMP;

     //   final boolean[] Complete = {false};

        final int[] scanDate = new int[1];

        FirebaseDatabase.getInstance().getReference().child("mess").child(MESS_ID).child("lastscan").setValue(timeMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        FirebaseDatabase.getInstance().getReference().child("mess").child(MESS_ID).child("lastscan")
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        Date scanTime=new Date(((Long) dataSnapshot.getValue()));

                                        scanDate[0] =scanTime.getDate();
                                       // Log.v("E_VALUE","database date ---- "+scanDate[0]);

                                        int nowdate = scanDate[0];
                                        SharedPreferences prefs = MainActivity.this.getSharedPreferences(
                                                "datedatabase", Context.MODE_PRIVATE);

                                        String dateTimeKey = "datedatabase";

                                        String getSPTime = prefs.getString(dateTimeKey, "27");

                                        Log.v("E_VALUE", "getSPTime ---- " + getSPTime);
                                        Log.v("E_VALUE", "database date ---- " + nowdate);

                                        if (getSPTime.equals(String.valueOf(nowdate))) {
                                            Log.v("E_VALUE", "BEFORE POPULATELISTVIEW ---- " + getSPTime);

                                           /// populateListView();
                                        } else {

                                            /*prefs.edit().clear();
                                            prefs.edit().putString(dateTimeKey, String.valueOf(nowdate));
                                            prefs.edit().apply();
                                            prefs.edit().commit();*/

                                            SharedPreferences  mPrefs2 = getPreferences(MODE_PRIVATE);

                                            SharedPreferences.Editor prefsEditor2 = mPrefs2.edit();
                                            Gson gson2 = new Gson();
                                            String json2= gson2.toJson(new HashMap<String,String>());
                                            prefsEditor2.putString("MyObject", json2);
                                            prefsEditor2.apply();
                                            prefsEditor2.commit();


                                            adapter.clear();
                                            adapter.notifyDataSetChanged();
                                        }

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });


                    }
                });        // }




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){



            if(data != null){
                final Barcode barcode = data.getParcelableExtra("barcode");
                final Barcode barcode2 = data.getParcelableExtra("barcode");
                result.post(new Runnable() {
                    @Override
                    public void run() {


                        String text2 = barcode2.displayValue;

                        if(isConnected()) {

                            Vibrator vib = (Vibrator) getBaseContext().getSystemService(Context.VIBRATOR_SERVICE);
                            vib.vibrate(20);
                            UserValidation(text2);/*) {
                                showPostValidationDialog(text2);
                            }*/

                            Toast.makeText(MainActivity.this,text2,Toast.LENGTH_LONG).show();

                        }
                        else
                        {
                            Vibrator vib = (Vibrator) getBaseContext().getSystemService(Context.VIBRATOR_SERVICE);
                            long[] pattern = {0, 75,100,75};

                            // The '-1' here means to vibrate once, as '-1' is out of bounds in the pattern array
                            vib.vibrate(pattern, -1);
                            Toast.makeText(getBaseContext(),"No Internet! Please Check your Connection",Toast.LENGTH_LONG).show();
                        }






                        //*****************************************************
                       // result.setText(barcode.displayValue);  //barcode.displayValue (gives the String value of
                                                               //             scanned data result)
                        String text = barcode.displayValue;


                        //TODO: send request to databse from here the com.example.saurabh.mess2owner.User Id will be there in barcode.DisplayValue
                        //TODO: Take it in a String and call the check if user valid method


                        //*****************************************************
                    }
                });
            }
        }
    }

    private void showPostValidationDialog(String text2) {



    }

    private boolean UserValidation(final String text2) {

        mDatabaseUser.child(text2)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            String tp = dataSnapshot.child("batch").getValue().toString();
                          //  Toast.makeText(MainActivity.this, "BATCH OF USER " + tp, Toast.LENGTH_LONG).show();
                            Log.v("E_VALUE", "BATCH OF USER " + tp);

                            UserDataObj = dataSnapshot.getValue(User.class);
                            UserDataObj.setUid(text2);

                            Log.v("E_VALUE","+++++++USER OBJ++++++"+UserDataObj);

                            getServerTime(text2);
                            /*if(getServerTime(text2))
                            {

                            }
                            else
                            {
                                //return false;
                            }*/

                        }



                        catch(NullPointerException e)
                        {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "SOME ERROR OCCURED PLEASE SCAN AGAIN!",Toast.LENGTH_LONG).show();

                        }



                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        return true;


    }

    private void getServerTime(String text2) {

        timeMap= ServerValue.TIMESTAMP;



            FirebaseDatabase.getInstance().getReference().child("mess").child(MESS_ID).child("lastscan").setValue(timeMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            FirebaseDatabase.getInstance().getReference().child("mess").child(MESS_ID).child("lastscan")
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {


                                           // SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy");

                                            Date scanTime=new Date(((Long) dataSnapshot.getValue()));

                                          //  Toast.makeText(MainActivity.this ,"Paid Time(Date) "+scanTime,Toast.LENGTH_LONG).show();
                                          //  Log.v("E_VALUE","-----------TIME----------- "+scanTime);
                                            //checkDate(paidDate,from);

                                            int scanDate=scanTime.getDate();
                                            int scanHour=scanTime.getHours();
                                            int scanMonth=scanTime.getMonth()+1;
                                            int scanYear=scanTime.getYear();

                                            if(scanHour>=17&&scanHour<23)
                                            {
                                                if(checkforDinner(scanDate,scanMonth))
                                                {
                                                  //  Toast.makeText(MainActivity.this,"Scanning Dinner",Toast.LENGTH_LONG).show();

                                                    checkforMess("scanneddinner",scanDate,scanMonth,scanYear);
                                                }
                                                else
                                                {
                                                    // return false;
                                                }

                                            }
                                            else if(scanHour>9&&scanHour<17)
                                            {
                                                if(checkforLunch(scanDate,scanMonth))
                                                {
                                                 //   Toast.makeText(MainActivity.this,"Scanning Lunch",Toast.LENGTH_LONG).show();

                                                    checkforMess("scannedlunch",scanDate,scanMonth,scanYear);
                                                }
                                                else
                                                {
                                                   // return false;
                                                }

                                            }
                                            else
                                            {
                                                showNegativeDialog();
                                                Toast.makeText(MainActivity.this,"Cant Scan",Toast.LENGTH_LONG).show();
                                            }


                                         //   Log.v("E_VALUE","SCAN TIME DATE "+scanDate);
                                         //   Log.v("E_VALUE","SCAN TIME HOUR "+scanHour);


                                         /*   Date d=dateFormat.parse(scanTime);*/



                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });


                        }
                    });
       // }




    }

    private void checkforMess(final String scanned, final int scanDate, final int scanMonth, final int scanYear) {

        String getbatch=UserDataObj.getBatch();
        String TEMP_MESS;
        final boolean[] GROUP_FOUND = new boolean[1];
        GROUP_FOUND[0]=false;

        if(getbatch.equals("batch1"))
        {
            TEMP_MESS="mess";
        }
        else if(getbatch.equals("batch2"))
        {
            TEMP_MESS="mess2";
        }
        else
        {
            showNegativeDialog();
            return ;
        }

        FirebaseDatabase.getInstance().getReference().child(TEMP_MESS).child(MESS_ID).child("grouplist")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot dsp : dataSnapshot.getChildren())
                        {
                            if(dsp.getKey().equals(UserDataObj.getGroupid()))
                            {
                                GROUP_FOUND[0] =true;
                              //  Toast.makeText(MainActivity.this,"USER IS GOOD!",Toast.LENGTH_LONG).show();

                                showPositiveDialog(scanned,scanDate,scanMonth,scanYear);

                                //setValues(scanned,scanDate,scanMonth,scanYear);
                                break;
                            }
                        }

                        if(!GROUP_FOUND[0])
                        {
                            showNegativeDialog();
                        }


                    }



                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });




    }

    private void showPositiveDialog(final String scanned, final int scanDate, final int scanMonth, final int scanYear) {



        final TextView input = new TextView(getBaseContext());
        input.setText("SCAN SUCCESS\n"+"NAME: "+UserDataObj.getName());
        input.setTextSize(37);
        input.setTextColor(Color.GREEN);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        FrameLayout container = new FrameLayout(getBaseContext());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = 50;
        params.rightMargin = 50;
        input.setLayoutParams(params);
        container.addView(input);
        builder.setView(container);
      //  builder.setTitle(input);
       // builder.setMessage("NAME : "+ UserDataObj.getName());

        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user pressed "yes", then he is allowed to exit from application
                setValues(scanned,scanDate,scanMonth,scanYear);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();


    }

    private void showNegativeDialog() {



        final TextView input = new TextView(getBaseContext());
        input.setText("SCAN FAIL\n"+"NAME: "+UserDataObj.getName());
        input.setTextSize(37);
        input.setTextColor(Color.RED);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        FrameLayout container = new FrameLayout(getBaseContext());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = 50;
        params.rightMargin = 50;
        input.setLayoutParams(params);
        container.addView(input);
        builder.setView(container);
        //  builder.setTitle(input);
        // builder.setMessage("NAME : "+ UserDataObj.getName());

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user pressed "yes", then he is allowed to exit from application
               // setValues(scanned,scanDate,scanMonth,scanYear);
                dialog.cancel();
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();


    }



    private void setValues(String scanned, int scanDate, int scanMonth, int scanYear) {

        mDatabaseUser.child(UserDataObj.getUid()).child(scanned).setValue(scanDate+"/"+scanMonth+"/"+(scanYear+1900));

        String endsubString=UserDataObj.getEndsub();

        int endSubInt=Integer.parseInt(endsubString);

        endSubInt--;

        mDatabaseUser.child(UserDataObj.getUid()).child("endsub").setValue(String.valueOf(endSubInt));


       // adapter.add(UserDataObj.getName());
       // adapter.notifyDataSetChanged();


        FirebaseDatabase.getInstance().getReference().child("MessBuffer").child(MESS_ID).child(scanned)
                .child(scanDate+"-"+(scanMonth)).child(UserDataObj.getUid()).setValue(UserDataObj.getName()+"-"+UserDataObj.getBatch());

        addinShared(UserDataObj.getUid(),UserDataObj.getName()+"-"+UserDataObj.getBatch());



    }

    private boolean checkforLunch(int scanDate,int scanMonth) {

        String scannedlu=UserDataObj.getScannedLunch();

        if(scannedlu.equals("-1"))
        {
            return true;
        }
        else
        {
            SimpleDateFormat myFormat=new SimpleDateFormat("dd/MM/yyyy");
            try {
                Date b1 = myFormat.parse(scannedlu);

                Log.v("E_VALUE","USER LAST SCAN MONTH "+b1.getMonth());
                Log.v("E_VALUE","CURRENT SCAN MONTH "+scanMonth);

                if(b1.getDate()==scanDate)
                {
                    Toast.makeText(MainActivity.this,"Scanned Lunch for second time  ",Toast.LENGTH_LONG).show();
                    showNegativeDialog();
                    return false;
                }
                else
                {
                    return true;
                }

            }
            catch(Exception e)
            {
                e.printStackTrace();
                return false;
            }

        }


    }

    private boolean checkforDinner(int scanDate , int scanMonth) {

        String scanneddi=UserDataObj.getScanneddinner();

        if(scanneddi.equals("-1"))
        {
            return true;
        }
        else
        {
            SimpleDateFormat myFormat=new SimpleDateFormat("dd/MM/yyyy");
            try {


                Date b1 = myFormat.parse(scanneddi);

                Log.v("E_VALUE","USER LAST SCAN MONTH "+b1.getMonth());
                Log.v("E_VALUE","CURRENT SCAN MONTH "+scanMonth);

                if(b1.getDate()==scanDate)
                {
                    Toast.makeText(MainActivity.this,"Scanned Dinner for second time ",Toast.LENGTH_LONG).show();
                    showNegativeDialog();
                    return false;
                }
                else
                {
                    return true;
                }

            }
            catch(Exception e)
            {
                e.printStackTrace();
                return false;

            }

        }



    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected=true;
            return true;
        }
        else
            connected = false;
        return false;
    }

     @Override
     protected void attachBaseContext(Context newBase) {
         super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
     }

}
