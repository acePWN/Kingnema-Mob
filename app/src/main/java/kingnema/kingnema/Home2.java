package kingnema.kingnema;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import static java.security.AccessController.getContext;
import static kingnema.kingnema.R.id.txtDate;

public class Home2 extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private ArrayList<String> branchIDs;

    private ArrayList<String> nowShowingNames;
    private ArrayList<String> nowShowingImages;
    private ArrayList<String> nowShowingBranchIDs;
    private ArrayList<String> nowShowingMovieIDs;
    private ArrayList<String> nowShowingCinemas;

    private ArrayList<String> comingSoonImages;
    private ArrayList<String> comingSoonMovieIDs;
    private ArrayList<String> comingSoonBranchIDs;
    private ArrayList<String> comingSoonNames;
    private ArrayList<String> comingSoonCinemas;
    private String Date;
    private TextView txtDate;
    private ImageView logo,backLogo,accountLogo;
    private static String TAG = Home2.class.getSimpleName();
    private String status;
    private ArrayList<String> branches;
    private String asd;
    private Spinner spinner;
    SharedPreferences SharedPreference;
    SharedPreferences.Editor editor;
    private static final String PreferenceName = "UserPreference";
    private static final String USERID_KEY = "UserIDKey";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);
        Thread clockThread;
        Runnable runnable = new CountDownRunner();
        clockThread= new Thread(runnable);
        clockThread.start();

        SharedPreference = getSharedPreferences(PreferenceName, Context.MODE_PRIVATE);
        if(!SharedPreference.contains(USERID_KEY)){
            Intent myIntent = new Intent(Home2.this, Home.class);
            startActivity(myIntent);
        }
        spinner=(Spinner) findViewById(R.id.spinnerBranch);
        spinner.setOnItemSelectedListener(this);
        ArrayList<String> branches = new ArrayList<String>();
        ArrayList<String> branchIDs = new ArrayList<String>();

         getBranches();










        logo=(ImageView)findViewById(R.id.logoButton);
        backLogo=(ImageView)findViewById(R.id.backButton);
        accountLogo=(ImageView)findViewById(R.id.accountButton);

        accountLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Home2.this, Account.class);
                startActivity(i);

            }
        });



        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void initRecyclerView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewNowShowing);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this,nowShowingNames,nowShowingImages,nowShowingMovieIDs,nowShowingBranchIDs,nowShowingCinemas);
        recyclerView.setAdapter(adapter);


    }
    private void initRecyclerView2(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewUpcoming);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this,comingSoonNames,comingSoonImages,comingSoonMovieIDs,comingSoonBranchIDs,comingSoonCinemas);
        recyclerView.setAdapter(adapter);


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        getMovies(branchIDs.get(adapterView.getSelectedItemPosition()));


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    public void getBranches(){
        StringRequest strRequest = new StringRequest(Request.Method.POST, getString(R.string.getBranchesURL), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try{
                    JSONObject object = new JSONObject(response);
                    status=object.getString("status");
                    if(status.equals("success")){
                        JSONArray result = object.getJSONArray("result");
                        branches = new ArrayList<String>();
                        branchIDs = new ArrayList<String>();
                        for(int i=0; i<result.length(); i++){
                            JSONObject c = result.getJSONObject(i);
                            branches.add(c.getString("branch_name"));
                            branchIDs.add(c.getString("branch_id"));


                        }
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_layout, branches);
                        dataAdapter.setDropDownViewResource( R.layout.spinner_layout);
                        spinner.setAdapter(dataAdapter);




                    }
                    else{
                        Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                    }

                }catch(JSONException e){
                    e.printStackTrace();
                }

                Log.d(TAG, response.toString());


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("password", "asd");
                return parameters;
            }
        };

        AppController.getInstance().addToRequestQueue(strRequest);
    }
    public void getMovies(final String branchID){
        StringRequest strRequest = new StringRequest(Request.Method.POST, getString(R.string.getMovieByBranchURL), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try{
                    JSONObject object = new JSONObject(response);
                    status=object.getString("status");
                    nowShowingNames = new ArrayList<String>();
                    nowShowingImages = new ArrayList<String>();
                    nowShowingBranchIDs= new ArrayList<String>();
                    nowShowingMovieIDs= new ArrayList<String>();
                    nowShowingCinemas = new ArrayList<String>();

                    comingSoonMovieIDs= new ArrayList<String>();
                    comingSoonBranchIDs= new ArrayList<String>();
                    comingSoonImages = new ArrayList<String>();
                    comingSoonNames = new ArrayList<String>();
                    comingSoonCinemas = new ArrayList<String>();


                    Boolean moviesShowing=false;
                    Boolean moviesUpcoming=false;
                    if(status.equals("success")){
                        JSONArray result = object.getJSONArray("result");


                        for(int i=0; i<result.length(); i++){
                            JSONObject c = result.getJSONObject(i);
                            if(c.getString("status").equals("now")){
                                if(!nowShowingCinemas.contains(c.getString("cinema_no"))){
                                    moviesShowing=true;
                                    nowShowingNames.add(c.getString("movie_title"));
                                    nowShowingImages.add(c.getString("movie_poster"));
                                    nowShowingBranchIDs.add(c.getString("branch_id"));
                                    nowShowingMovieIDs.add(c.getString("movie_id"));
                                    nowShowingCinemas.add(c.getString("cinema_no"));
                                }
                            }
                            else if (c.getString("status").equals("soon")) {
                                if(!comingSoonCinemas.contains(c.getString("cinema_no"))){
                                    moviesUpcoming=true;
                                    comingSoonNames.add(c.getString("movie_title"));
                                    comingSoonImages.add(c.getString("movie_poster"));
                                    comingSoonBranchIDs.add(c.getString("branch_id"));
                                    comingSoonMovieIDs.add(c.getString("movie_id"));
                                    comingSoonCinemas.add(c.getString("cinema_no"));
                                }
                            }
                        }
                    }

                    if(!moviesShowing){
                        nowShowingNames.add("More Movies Coming Soon");
                        nowShowingImages.add(getString(R.string.noShowingImageURL) );
                        nowShowingBranchIDs.add("none");
                        nowShowingMovieIDs.add("none");
                        nowShowingCinemas.add("---");
                    }
                    if(!moviesUpcoming){
                        comingSoonNames.add("More Movies Coming Soon");
                        comingSoonImages.add(getString(R.string.noUpcomingImageURL) );
                        comingSoonBranchIDs.add("none");
                        comingSoonMovieIDs.add("none");
                        comingSoonCinemas.add("---");
                    }
                    initRecyclerView();
                    initRecyclerView2();

                }catch(JSONException e){
                    e.printStackTrace();
                }

                Log.d(TAG, response.toString());


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("branch_id", branchID);
                return parameters;
            }
        };

        AppController.getInstance().addToRequestQueue(strRequest);
    }
    public void doWork() {
        runOnUiThread(new Runnable() {
            public void run() {
                try{
                    txtDate = (TextView) findViewById(R.id.textDate);
                    Calendar c = Calendar .getInstance();
                    System.out.println("Current time => "+c.getTime());
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a", Locale.ENGLISH);
                    df.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
                    Date = df.format(c.getTime());
                    txtDate.setText(Date);
                }catch (Exception e) {}
            }
        });
    }


    class CountDownRunner implements Runnable{
        // @Override
        public void run() {
            while(!Thread.currentThread().isInterrupted()){
                try {
                    doWork();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }catch(Exception e){
                }
            }
        }
    }
}

