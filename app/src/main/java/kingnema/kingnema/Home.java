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

public class Home extends AppCompatActivity {

    SharedPreferences SharedPreference;
    SharedPreferences.Editor editor;
    private static final String PreferenceName = "UserPreference";
    private static final String USERID_KEY = "UserIDKey";
    private static final String PROFID_KEY = "ProfIDKey";
    private static String TAG = Home.class.getSimpleName();
    private Button signIn,signUp;
    private EditText edtusername,edtpassword;
    String status, username, password, message, userid,profid;
    private TextView txtForgotPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        SharedPreference = getSharedPreferences(PreferenceName, Context.MODE_PRIVATE);
        if(SharedPreference.contains(USERID_KEY)){
            Intent myIntent = new Intent(Home.this, Home2.class);
             startActivity(myIntent);
        }
        initResources();
        initEvents();
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Home.this, SignUp.class);
                startActivity(i);
            }
        });
    }


    private void initResources(){
        signIn=(Button)findViewById(R.id.buttonSignIn);
        signUp=(Button)findViewById(R.id.buttonSignUp);
        edtusername=(EditText)findViewById(R.id.username);
        edtpassword=(EditText)findViewById(R.id.password);
        txtForgotPass = (TextView) findViewById(R.id.txtForgotPassword);
        SharedPreference = getSharedPreferences(PreferenceName, Context.MODE_PRIVATE);
    }

    private void initEvents(){
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringRequest strRequest = new StringRequest(Request.Method.POST, getString(R.string.signInAPI), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject object = new JSONObject(response);
                            status=object.getString("status");
                            if(status.equals("success")){
                                JSONArray result = object.getJSONArray("infos");
                                for(int i=0; i<result.length(); i++){
                                    JSONObject c = result.getJSONObject(i);
                                    userid = (String) c.getString("user_id");
                                    profid= (String) c.getString("profile_id");
                                }

                                editor = SharedPreference.edit();
                                editor.putString(USERID_KEY, userid);
                                editor.putString(PROFID_KEY, profid);
                                editor.commit();
                                Toast.makeText(getApplicationContext(), "Welcome", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(Home.this, Home2.class);
                                startActivity(i);
                                 finish();

                            } else if(status.equals("failed")){
                                message=object.getString("message");
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
                        parameters.put("username", edtusername.getText().toString());
                        parameters.put("password", edtpassword.getText().toString());
                        return parameters;
                    }
                };
                AppController.getInstance().addToRequestQueue(strRequest);
            }
        });

        txtForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Home.this, ForgotPassword.class);
                startActivity(i);
            }
        });
    }
}
