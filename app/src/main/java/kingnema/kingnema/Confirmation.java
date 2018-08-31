package kingnema.kingnema;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Confirmation extends AppCompatActivity {


    private static String TAG = Confirmation.class.getSimpleName();
    private String status;
    private String branch_name;
    private String movie_name;
    private String num_tix;
    private String time_slot, message,movie_poster,show_date;

    private Button btnCancel,btnOkay;
    private TextView txtNumTix;
    private TextView txtMovie;
    private TextView txtBranch;
    private TextView txtTimeSlot,txtDate;
    private ImageView imgMoviePoster;
    private String userIDPref;
    private String watchIDPref;
    private ImageView logo,backLogo,accountLogo;
    private Intent intent;
    SharedPreferences SharedPreference;
    SharedPreferences.Editor editor;
    private static final String PreferenceName = "UserPreference";
    private static final String USERID_KEY = "UserIDKey";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
        SharedPreference = getSharedPreferences(PreferenceName, Context.MODE_PRIVATE);

        if(!SharedPreference.contains(USERID_KEY)){
            Intent myIntent = new Intent(Confirmation.this, Home.class);
            startActivity(myIntent);
        }
        else{
            intent = getIntent();
            watchIDPref = intent.getStringExtra("watch_id");
            userIDPref= SharedPreference.getString(USERID_KEY,"");

        }
        initResoures();
        initEvents();
    }

    private void initResoures(){
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnOkay= (Button) findViewById(R.id.btnOkay);
        txtNumTix = (TextView) findViewById(R.id.txtNumTickets);
        txtMovie = (TextView) findViewById(R.id.txtMovie);
        txtBranch = (TextView) findViewById(R.id.txtBranch);
        txtTimeSlot = (TextView) findViewById(R.id.txtTimeSlot);
        txtDate= (TextView) findViewById(R.id.txtDate);
        logo=(ImageView)findViewById(R.id.logoButton);
        backLogo=(ImageView)findViewById(R.id.backButton);
        accountLogo=(ImageView)findViewById(R.id.accountButton);
        imgMoviePoster=(ImageView)findViewById(R.id.moviePoster);
        accountLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Confirmation.this, Account.class);
                startActivity(i);
            }
        });


        backLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Confirmation.this, Home2.class);
                startActivity(i);

            }
        });
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Confirmation.this, Home2.class);
                startActivity(i);

            }
        });
    }

    private void initEvents(){

        StringRequest strRequest = new StringRequest(Request.Method.POST, getString(R.string.getReserAPI), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject trylang = new JSONObject(response);
                    status = trylang.getString("status");
                    if(status.equals("success")){
                        JSONArray infos = trylang.getJSONArray("infos");
                        for(int i = 0; i < infos.length(); i++){
                            JSONObject c = infos.getJSONObject(i);
                            movie_name = (String) c.getString("movie_name");
                            branch_name = (String) c.getString("branch_name");
                            num_tix = (String) c.getString("num_tickets");
                            time_slot = (String) c.getString("time_slot");
                            movie_poster = (String) c.getString("movie_poster");
                            show_date = (String) c.getString("reservation_date");
                        }
                        txtBranch.setText(branch_name);
                        txtMovie.setText(movie_name);
                        txtNumTix.setText(num_tix);
                        txtTimeSlot.setText(time_slot);
                        txtDate.setText(show_date);
                        Glide.with(getApplicationContext()).asBitmap().load(getString(R.string.siteURL)+movie_poster).into(imgMoviePoster);
                    }else if(status.equals("failed")){
                        message = trylang.getString("message");
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
                parameters.put("watch_id", watchIDPref);
                parameters.put("user_id", userIDPref);
                return parameters;
            }
        };
        AppController.getInstance().addToRequestQueue(strRequest);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest strRequest = new StringRequest(Request.Method.POST, getString(R.string.deleteReservationAPI), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject trylang = new JSONObject(response);
                            status = trylang.getString("status");
                            message = trylang.getString("message");
                            if(status.equals("success")){
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(Confirmation.this, Account.class);
                                startActivity(i);
                            }else if(status.equals("failed")){
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(Confirmation.this, Account.class);
                                startActivity(i);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
                        parameters.put("watch_id", watchIDPref);
                        parameters.put("user_id", userIDPref);
                        return parameters;
                    }
                };
                AppController.getInstance().addToRequestQueue(strRequest);
            }
        });

        btnOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Confirmation.this, Account.class);
                startActivity(i);
            }
        });

    }
}
