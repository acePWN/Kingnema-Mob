package kingnema.kingnema;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Account extends AppCompatActivity {
    private ArrayList<String> movieNames;
    private ArrayList<Integer> images;
    private ImageView logo,backLogo,accountLogo;
    private Button getMore;
    private String status;
    private String username, balance;
    private TextView textName,textMoney,textLogOut,textSettings;
    private static String TAG = SignUp.class.getSimpleName();
    private ArrayList<String> reservedNames;
    private ArrayList<String> reservedImages;
    private ArrayList<String> reservedBranchIDs;
    private ArrayList<String> reservedMovieIDs;
    private ArrayList<String> reservedCinemas;
    private ArrayList<String> reservedWatchIDs;
    private String userIDPref;
    SharedPreferences SharedPreference;
    SharedPreferences.Editor editor;
    private static final String PreferenceName = "UserPreference";
    private static final String USERID_KEY = "UserIDKey";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        logo=(ImageView)findViewById(R.id.logoButton);
        backLogo=(ImageView)findViewById(R.id.backButton);

        textLogOut=(TextView) findViewById(R.id.textLogOut);
        SharedPreference = getSharedPreferences(PreferenceName, Context.MODE_PRIVATE);

        if(!SharedPreference.contains(USERID_KEY)){
            Intent myIntent = new Intent(Account.this, Home.class);
            startActivity(myIntent);
        }
        else{
            userIDPref= SharedPreference.getString(USERID_KEY,"");
        }




        backLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Account.this, Home2.class);
                startActivity(i);

            }
        });
        textLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor = SharedPreference.edit();
                editor.clear();
                editor.commit();
                Intent myIntent = new Intent(Account.this, Home.class);
                startActivity(myIntent);
                finish();

            }
        });
        initResources();

        movieNames = new ArrayList<String>();
        images = new ArrayList<Integer>();
        textName = (TextView)findViewById(R.id.textName);
        textMoney = (TextView)findViewById(R.id.textMoney);


        getAccount();
        getReserved();;
        logo=(ImageView)findViewById(R.id.logoButton);
        backLogo=(ImageView)findViewById(R.id.backButton);
        accountLogo=(ImageView)findViewById(R.id.accountButton);
        getMore=(Button)findViewById(R.id.buttonGetMore);
        textSettings= (TextView)findViewById(R.id.textEditAcc);





        textSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Account.this, CodeForgotPassword.class);
                startActivity(i);
            }
        });

        getMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Account.this, TopUp.class);
                startActivity(i);
            }
        });


    }


    private void initRecyclerView(){

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewReservedMovies);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this,reservedNames,reservedImages,reservedCinemas,reservedWatchIDs);
        recyclerView.setAdapter(adapter);

    }


    public void getAccount(){

        StringRequest strRequest = new StringRequest(Request.Method.POST, getString(R.string.displayAccount), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try{

                    JSONObject object = new JSONObject(response);
                    status=object.getString("status");

                    if(status.equals("success")){
                        JSONArray result = object.getJSONArray("infos");

                        for(int i=0; i<result.length(); i++){
                            JSONObject c = result.getJSONObject(i);
                            username=c.getString("username");
                            balance=c.getString("balance");

                        }

                        textName.setText(username);
                        textMoney.setText(balance);

                    }
                    else{
                        // Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
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
                        "ERROR", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("user_id", userIDPref);
                return parameters;
            }
        };

        AppController.getInstance().addToRequestQueue(strRequest);

    }

    private void initResources(){

        getMore = (Button)findViewById(R.id.buttonGetMore);

    }

    public void getReserved(){
        StringRequest strRequest = new StringRequest(Request.Method.POST, getString(R.string.getReservedMoviesURL), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try{
                    JSONObject object = new JSONObject(response);
                    status=object.getString("status");
                    reservedNames= new ArrayList<String>();
                    reservedImages= new ArrayList<String>();
                    reservedBranchIDs= new ArrayList<String>();
                    reservedMovieIDs= new ArrayList<String>();
                    reservedCinemas= new ArrayList<String>();
                    reservedWatchIDs = new ArrayList<String>();




                    Boolean moviesReserved=false;

                    if(status.equals("success")){
                        JSONArray result = object.getJSONArray("result");


                        for(int i=0; i<result.length(); i++){
                            JSONObject c = result.getJSONObject(i);
                            if(c.getString("status").equals("1")){
                                moviesReserved=true;
                                reservedNames.add(c.getString("movie_title"));
                                reservedImages.add(c.getString("movie_poster"));
                                reservedCinemas.add(c.getString("cinema_no"));
                                reservedWatchIDs.add(c.getString("watch_id"));

                            }

                        }
                    }
                    else{

                    }
                    if( moviesReserved){
                        initRecyclerView();

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
                parameters.put("user_id", userIDPref);
                return parameters;
            }
        };

        AppController.getInstance().addToRequestQueue(strRequest);
    }

}
