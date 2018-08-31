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

import java.util.HashMap;
import java.util.Map;

public class DeactivateAccount extends AppCompatActivity {

    private static String TAG = Home.class.getSimpleName();
    private String status, message;
    private ImageView logo,backLogo,accountLogo;
    private Button btnDeactivate;
    private EditText edtDeactPass;
    SharedPreferences SharedPreference;
    SharedPreferences.Editor editor;
    private static final String PreferenceName = "UserPreference";
    private static final String USERID_KEY = "UserIDKey";
    private static final String PROFID_KEY = "ProfIDKey";
    private String userIDPref,profIDPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deactivate_account);
        SharedPreference = getSharedPreferences(PreferenceName, Context.MODE_PRIVATE);

        if(!SharedPreference.contains(USERID_KEY)){
            Intent myIntent = new Intent(DeactivateAccount.this, Home.class);
            startActivity(myIntent);
        }
        else{
            userIDPref= SharedPreference.getString(USERID_KEY,"");
            profIDPref= SharedPreference.getString(PROFID_KEY,"");
        }
        initResources();
        initEvents();

        logo=(ImageView)findViewById(R.id.logoButton);
        backLogo=(ImageView)findViewById(R.id.backButton);

        backLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DeactivateAccount.this, Home2.class);
                startActivity(i);

            }
        });
    }

    private void initEvents() {
        btnDeactivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StringRequest strRequest = new StringRequest(Request.Method.POST, getString(R.string.deactUserAPI), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject object = new JSONObject(response);
                            status=object.getString("status");
                            message=object.getString("message");
                            if(status.equals("success")){

                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                editor = SharedPreference.edit();
                                editor.clear();
                                editor.commit();
                                Intent myIntent = new Intent(DeactivateAccount.this, Home.class);
                                startActivity(myIntent);
                                finish();

                            } else if(status.equals("failed")){
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
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
                        parameters.put("password", edtDeactPass.getText().toString());
                        return parameters;
                    }
                };
                AppController.getInstance().addToRequestQueue(strRequest);
            }
        });
    }

    private void initResources() {

        btnDeactivate = (Button) findViewById(R.id.btnDeactivate);
        edtDeactPass = (EditText) findViewById(R.id.txtDeactPassword);
    }
}
