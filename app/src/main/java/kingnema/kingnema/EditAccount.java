package kingnema.kingnema;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditAccount extends AppCompatActivity {

    private static String TAG = Confirmation.class.getSimpleName();
    private String status, message;

    private String toastmessage,userIDPref,profIDPref;
    private ImageView logo,backLogo,accountLogo;
    private EditText edtUsername;
    private EditText edtPassword;
    private EditText edtEmail;
    private EditText edtContactNumber;
    private EditText edtAddress;
    private EditText edtCardNumber;
    private Button btnEdiitAccount;
    private TextView txtDeactivate;
    SharedPreferences SharedPreference;
    SharedPreferences.Editor editor;
    private static final String PreferenceName = "UserPreference";
    private static final String USERID_KEY = "UserIDKey";
    private static final String PROFID_KEY = "ProfIDKey";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);
        SharedPreference = getSharedPreferences(PreferenceName, Context.MODE_PRIVATE);

        if(!SharedPreference.contains(USERID_KEY)){
            Intent myIntent = new Intent(EditAccount.this, Home.class);
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
        txtDeactivate=(TextView) findViewById(R.id.textDeac);
        backLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(EditAccount.this, Account.class);
                startActivity(i);

            }
        });
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(EditAccount.this, Home2.class);
                startActivity(i);

            }
        });
        txtDeactivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditAccount.this, DeactivateAccount.class);
                startActivity(i);
            }
        });
    }

    private void initEvents() {
        btnEdiitAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!edtUsername.getText().toString().isEmpty()){
                    StringRequest strRequest = new StringRequest(Request.Method.POST, getString(R.string.editUsernameAPI), new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject trylang = new JSONObject(response);
                                status = trylang.getString("status");
                                message = trylang.getString("message");
                                if(status.equals("success")){
                                    Toast.makeText(getApplicationContext(),
                                            message, Toast.LENGTH_SHORT).show();
                                }else if(status.equals("failed")){
                                    Toast.makeText(getApplicationContext(),
                                            message, Toast.LENGTH_SHORT).show();
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
                            parameters.put("username", edtUsername.getText().toString());
                            parameters.put("profile_id", profIDPref);
                            return parameters;
                        }
                    };
                    AppController.getInstance().addToRequestQueue(strRequest);
                }

                if(!edtPassword.getText().toString().isEmpty()){
                    StringRequest strRequest = new StringRequest(Request.Method.POST, getString(R.string.editPasswordAPI), new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject trylang = new JSONObject(response);
                                status = trylang.getString("status");
                                message = trylang.getString("message");
                                if(status.equals("success")){
                                    Toast.makeText(getApplicationContext(),
                                            message, Toast.LENGTH_SHORT).show();
                                }else if(status.equals("failed")){
                                    Toast.makeText(getApplicationContext(),
                                            message, Toast.LENGTH_SHORT).show();
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
                            parameters.put("password", edtPassword.getText().toString());
                            parameters.put("profile_id", profIDPref);
                            return parameters;
                        }
                    };
                    AppController.getInstance().addToRequestQueue(strRequest);
                }

                if(!edtEmail.getText().toString().isEmpty()){
                    StringRequest strRequest = new StringRequest(Request.Method.POST, getString(R.string.editEmailAPI), new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject trylang = new JSONObject(response);
                                status = trylang.getString("status");
                                message = trylang.getString("message");
                                if(status.equals("success")){
                                    Toast.makeText(getApplicationContext(),
                                            message, Toast.LENGTH_SHORT).show();
                                }else if(status.equals("failed")){
                                    Toast.makeText(getApplicationContext(),
                                            message, Toast.LENGTH_SHORT).show();
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
                            parameters.put("email", edtEmail.getText().toString());
                            parameters.put("profile_id", profIDPref);
                            return parameters;
                        }
                    };
                    AppController.getInstance().addToRequestQueue(strRequest);
                }

                if(!edtContactNumber.getText().toString().isEmpty()){
                    StringRequest strRequest = new StringRequest(Request.Method.POST, getString(R.string.editContactNumberAPI), new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject trylang = new JSONObject(response);
                                status = trylang.getString("status");
                                message = trylang.getString("message");
                                if(status.equals("success")){
                                    Toast.makeText(getApplicationContext(),
                                            message, Toast.LENGTH_SHORT).show();
                                }else if(status.equals("failed")){
                                    Toast.makeText(getApplicationContext(),
                                            message, Toast.LENGTH_SHORT).show();
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
                            parameters.put("contact_number", edtContactNumber.getText().toString());
                            parameters.put("profile_id", profIDPref);
                            return parameters;
                        }
                    };
                    AppController.getInstance().addToRequestQueue(strRequest);
                }

                if(!edtAddress.getText().toString().isEmpty()){
                    StringRequest strRequest = new StringRequest(Request.Method.POST, getString(R.string.editAddressAPI), new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject trylang = new JSONObject(response);
                                status = trylang.getString("status");
                                message = trylang.getString("message");
                                if(status.equals("success")){
                                    Toast.makeText(getApplicationContext(),
                                            message, Toast.LENGTH_SHORT).show();
                                }else if(status.equals("failed")){
                                    Toast.makeText(getApplicationContext(),
                                            message, Toast.LENGTH_SHORT).show();
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
                            parameters.put("address", edtAddress.getText().toString());
                            parameters.put("profile_id", profIDPref);
                            return parameters;
                        }
                    };
                    AppController.getInstance().addToRequestQueue(strRequest);
                }


            }
        });
    }

    private void initResources() {
        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtContactNumber = (EditText) findViewById(R.id.edtContactNumber);
        edtAddress = (EditText) findViewById(R.id.edtAddress);
        btnEdiitAccount = (Button) findViewById(R.id.btnEditAccount);
    }
}
