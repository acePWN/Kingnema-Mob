package kingnema.kingnema;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import org.w3c.dom.Text;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class Movie extends AppCompatActivity {


    private static String TAG = Confirmation.class.getSimpleName();
    private String status;
    private String message;
    private String rating;
    private String genre;
    private String length;
    private String price;
    private String movie_name;
    private String picture;
    private String chosenSlot;
    private String cinemaNo;
    private String branchID;
    private String balance;
    int result;
    int total;
    private String referenceID;
    private ArrayList<String> timeSlots, dates,spinnerTime,spinnerDate,showIDs,seats;

    private ImageView logo, backLogo, accountLogo;
    private Button btnReserve;
    private TextView txtMovieTitle;
    private TextView txtRating;
    private TextView txtMovieLength;
    private TextView txtMovieGenre;
    private TextView txtMoviePrice,txtShowDate,textAvailableSeats;
    private ImageView imgMoviePoster;
    private TextView txtTotal,txtDate;
    private Spinner spinnerTimeSlots, spinnerDates;
    private EditText edtNumberTix;
    private String intentMovie_title;
    private String intentBranch_id;
    private String intentCinema_no;
    private Intent intent;
    private String userIDPref;
    private String watch_id;
    private String Date;
    SharedPreferences SharedPreference;
    SharedPreferences.Editor editor;
    private static final String PreferenceName = "UserPreference";
    private static final String USERID_KEY = "UserIDKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        Thread clockThread;
        Runnable runnable = new Movie.CountDownRunner();
        clockThread= new Thread(runnable);
        clockThread.start();
        SharedPreference = getSharedPreferences(PreferenceName, Context.MODE_PRIVATE);

        if (!SharedPreference.contains(USERID_KEY)) {
            Intent myIntent = new Intent(Movie.this, Home.class);
            startActivity(myIntent);
        } else {
            userIDPref = SharedPreference.getString(USERID_KEY, "");
        }
        logo = (ImageView) findViewById(R.id.logoButton);
        backLogo = (ImageView) findViewById(R.id.backButton);
        accountLogo = (ImageView) findViewById(R.id.accountButton);

        accountLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Movie.this, Account.class);
                startActivity(i);
            }
        });


        backLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Movie.this, Home2.class);
                startActivity(i);

            }
        });
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Movie.this, Home2.class);
                startActivity(i);

            }
        });
        spinnerTimeSlots = (Spinner) findViewById(R.id.spinnerTimeSlots);
        spinnerDates = (Spinner) findViewById(R.id.spinnerDates);


        initResources();
        initEvents();

    }

    private void initResources() {
        btnReserve = (Button) findViewById(R.id.btnReserve);
        txtMovieTitle = (TextView) findViewById(R.id.txtMovieTitle);
        txtRating = (TextView) findViewById(R.id.txtRating);
        txtMovieLength = (TextView) findViewById(R.id.txtMovieLength);
        txtMovieGenre = (TextView) findViewById(R.id.txtGenre);
        txtMoviePrice = (TextView) findViewById(R.id.txtTicketPrice);
        txtTotal = (TextView) findViewById(R.id.txtTotal);
        edtNumberTix = (EditText) findViewById(R.id.editNoOfTickets);
        imgMoviePoster = (ImageView) findViewById(R.id.imgMoviePoster);
        txtShowDate =(TextView) findViewById(R.id.txtDate);
        textAvailableSeats=(TextView) findViewById(R.id.textAvailableSeats);
    }

    private void initEvents() {
        StringRequest strRequest = new StringRequest(Request.Method.POST, getString(R.string.getMovieAPI), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    dates = new ArrayList<String>();
                    timeSlots = new ArrayList<String>();
                    spinnerDate = new ArrayList<String>();
                    showIDs = new ArrayList<String>();
                    seats = new ArrayList<String>();
                    JSONObject trylang = new JSONObject(response);
                    status = trylang.getString("status");
                    message = trylang.getString("message");

                    if (status.equals("success")) {
                        JSONArray infos = trylang.getJSONArray("infos");
                        for (int i = 0; i < infos.length(); i++) {
                            JSONObject c = infos.getJSONObject(i);
                            movie_name = c.getString("movie_title");
                            rating = c.getString("rating");
                            genre = c.getString("movie_genre");
                            length = c.getString("movie_length");
                            price = c.getString("price");
                            picture = c.getString("movie_poster");
                            timeSlots.add(c.getString("time_slot"));
                            dates.add(c.getString("show_date"));
                            showIDs.add(c.getString("show_id"));
                            cinemaNo = c.getString("cinema_no");
                            branchID = c.getString("branch_id");
                            seats.add(c.getString("available_seat"));
                            if(!spinnerDate.contains(c.getString("show_date"))) {
                                spinnerDate.add(c.getString("show_date"));
                            }
                        }


                        if(spinnerDate.size()==1){
                            txtShowDate.setText(spinnerDate.get(0));
                        }
                        else{
                            txtShowDate.setText(spinnerDate.get(0) + " - " + spinnerDate.get(spinnerDate.size()-1));
                        }
                        txtMovieTitle.setText(movie_name);
                        txtRating.setText(rating);
                        txtMovieGenre.setText(genre);
                        txtMoviePrice.setText(price);
                        txtMovieLength.setText(length);
                        Glide.with(getApplicationContext()).asBitmap().load(getString(R.string.siteURL) + picture).into(imgMoviePoster);


                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_layout, spinnerDate);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerDates.setAdapter(dataAdapter);
                        spinnerDates.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                getTimeSlots(spinnerDate.get(i));
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

                    } else if (status.equals("failed")) {
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
                intent = getIntent();
                intentMovie_title = intent.getStringExtra("movie_title");
                intentBranch_id = intent.getStringExtra("branch_id");
                intentCinema_no = intent.getStringExtra("cinema_no");
                parameters.put("movie_id", intentMovie_title);
                parameters.put("cinema_no", intentCinema_no);
                parameters.put("branch_id", intentBranch_id);

                return parameters;
            }
        };
        AppController.getInstance().addToRequestQueue(strRequest);

        edtNumberTix.addTextChangedListener(new TextWatcher() {
                                                @Override
                                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                                }

                                                @Override
                                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                                    result = 0;
                                                    txtTotal.setText("" + result);
                                                }

                                                @Override
                                                public void afterTextChanged(Editable s) {
                                                    if (edtNumberTix.getText().toString().isEmpty()) {
                                                        result = 0;
                                                        txtTotal.setText("" + result);
                                                    } else {
                                                        result = Integer.valueOf(edtNumberTix.getText().toString()) * Integer.valueOf(price);
                                                        txtTotal.setText("" + result);
                                                    }

                                                }
                                            }
        );

        btnReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest strRequest = new StringRequest(Request.Method.POST, getString(R.string.checkBalanceAPI), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject trylang = new JSONObject(response);
                            status = trylang.getString("status");
                            if (status.equals("success")) {
                                JSONArray infos = trylang.getJSONArray("infos");
                                for (int i = 0; i < infos.length(); i++) {
                                    JSONObject c = infos.getJSONObject(i);
                                    balance = c.getString("balance");
                                }
                                if( !edtNumberTix.getText().toString().equals("")){
                                    if (Integer.valueOf(edtNumberTix.getText().toString()) > 0  ) {
                                        if (Integer.valueOf(balance) >= result) {

                                            total = Integer.valueOf(balance) - result;
                                            StringRequest strRequest = new StringRequest(Request.Method.POST, getString(R.string.makeReservationAPI), new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    try {

                                                        JSONObject trylang = new JSONObject(response);
                                                        status = trylang.getString("status");
                                                        message = trylang.getString("message");

                                                        if (status.equals("success")) {
                                                            watch_id = trylang.getString("watch_id");

                                                            StringRequest strRequest = new StringRequest(Request.Method.POST, getString(R.string.updateAccountAPI), new Response.Listener<String>() {
                                                                @Override
                                                                public void onResponse(String response) {
                                                                    try {
                                                                        JSONObject trylang = new JSONObject(response);
                                                                        status = trylang.getString("status");
                                                                        message = trylang.getString("message");
                                                                        if (status.equals("success")) {

                                                                            Intent i = new Intent(Movie.this, Confirmation.class);
                                                                            i.putExtra("watch_id", watch_id);
                                                                            startActivity(i);
                                                                            finish();
                                                                        } else if (status.equals("failed")) {

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
                                                                            "error", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }) {
                                                                @Override
                                                                protected Map<String, String> getParams() throws AuthFailureError {
                                                                    Map<String, String> parameters = new HashMap<String, String>();
                                                                    parameters.put("user_id", userIDPref);
                                                                    parameters.put("balance", Integer.toString(total));
                                                                    return parameters;
                                                                }
                                                            };
                                                            AppController.getInstance().addToRequestQueue(strRequest);


                                                        } else {

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
                                                            "error", Toast.LENGTH_SHORT).show();
                                                }
                                            }) {
                                                @Override
                                                protected Map<String, String> getParams() throws AuthFailureError {
                                                    Map<String, String> parameters = new HashMap<String, String>();
                                                    parameters.put("show_id", referenceID);
                                                    parameters.put("user_id", userIDPref);
                                                    parameters.put("num_tickets", edtNumberTix.getText().toString());
                                                    return parameters;
                                                }
                                            };
                                            AppController.getInstance().addToRequestQueue(strRequest);


                                        } else {
                                            Toast.makeText(getApplicationContext(),
                                                    "You don't have enough balance.", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Enter Number of Tickets", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else{
                                    Toast.makeText(getApplicationContext(), "Enter Number of Tickets", Toast.LENGTH_SHORT).show();
                                }



                            } else if (status.equals("failed")) {
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
                        parameters.put("user_id", userIDPref);
                        return parameters;
                    }
                };
                AppController.getInstance().addToRequestQueue(strRequest);


            }

        });

    }
    public void getTimeSlots(final String date){
        spinnerTime = new ArrayList<String>();
        for(int i=0; i<dates.size();i++){
            if(date.equals(dates.get(i))){
                spinnerTime.add(timeSlots.get(i));
            }
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_layout, spinnerTime);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTimeSlots.setAdapter(dataAdapter);
        spinnerTimeSlots.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                for(int j=0; j<timeSlots.size();j++){
                    if(spinnerTime.get(i).equals(timeSlots.get(j)) && date.equals(dates.get(j))){
                        referenceID=showIDs.get(j);
                        if(seats.get(j).equals("0")){
                            textAvailableSeats.setText("SOLD OUT");
                        }
                        else{
                            textAvailableSeats.setText(seats.get(j));
                        }
                    }
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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

