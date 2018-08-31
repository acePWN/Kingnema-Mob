package kingnema.kingnema;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TopUp extends AppCompatActivity {

    private Button btnSubmit;
    private EditText editTextAmount;
    private  ImageView logo,backLogo,accountLogo;

    private static String TAG = TopUp.class.getSimpleName();
    private String userIDPref;
    SharedPreferences SharedPreference;
    SharedPreferences.Editor editor;
    private static final String PreferenceName = "UserPreference";
    private static final String USERID_KEY = "UserIDKey";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_up);


        SharedPreference = getSharedPreferences(PreferenceName, Context.MODE_PRIVATE);

        if(!SharedPreference.contains(USERID_KEY)){
            Intent myIntent = new Intent(TopUp.this, Home.class);
            startActivity(myIntent);
        }
        else{
            userIDPref= SharedPreference.getString(USERID_KEY,"");
        }


        logo=(ImageView)findViewById(R.id.logoButton);
        backLogo=(ImageView)findViewById(R.id.backButton);
        accountLogo=(ImageView)findViewById(R.id.accountButton);

        accountLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TopUp.this, Account.class);
                startActivity(i);
            }
        });


        backLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TopUp.this, Account.class);
                startActivity(i);
            }
        });
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TopUp.this, Home2.class);
                startActivity(i);

            }
        });
        initResources();
        initEvents();

    }

    private void initResources() {

        btnSubmit = (Button)findViewById(R.id.btnSubmit);
        editTextAmount = (EditText)findViewById(R.id.edtCardCode);


    }

    private void initEvents(){

        btnSubmit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                topUp();
            }
        });


    }

    public void topUp(){
        StringRequest strRequest = new StringRequest(Request.Method.POST, getString(R.string.checkCardAPI), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    JSONObject trylang = new JSONObject(response);
                    Toast.makeText(getApplicationContext(), trylang.getString("message"), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Intent i = new Intent(TopUp.this, Account.class);
                startActivity(i);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();

                parameters.put("card_code", editTextAmount.getText().toString());
                parameters.put("user_id", userIDPref);

                return parameters;
            }
        };
        AppController.getInstance().addToRequestQueue(strRequest);
    }
}