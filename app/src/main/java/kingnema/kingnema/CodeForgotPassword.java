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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class CodeForgotPassword extends AppCompatActivity {

    private static String TAG = Confirmation.class.getSimpleName();
    private String status, message,email;

    private EditText edtAnswer;
    private Button btnSubmitAnswer;
    private TextView txtSecretQ, txtYourPass, txtPassword;
    private String question, profile_id, password;
    private Integer ForgotPass;
    private ImageView backLogo;
    SharedPreferences SharedPreference;
    SharedPreferences.Editor editor;
    private static final String PreferenceName = "UserPreference";
    private static final String USERID_KEY = "UserIDKey";
    private static final String PROFID_KEY = "ProfIDKey";
    private String userIDPref,profIDPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_forgot_password);
        SharedPreference = getSharedPreferences(PreferenceName, Context.MODE_PRIVATE);

        if(!SharedPreference.contains(USERID_KEY)){

        }
        else{
            userIDPref= SharedPreference.getString(USERID_KEY,"");
            profIDPref= SharedPreference.getString(PROFID_KEY,"");
        }
        backLogo = (ImageView) findViewById(R.id.backButton);
        backLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });
        initResources();
        initEvents();
    }

    private void initResources() {
        edtAnswer = (EditText) findViewById(R.id.edtAnswer);
        btnSubmitAnswer = (Button) findViewById(R.id.btnSubmitAnswer);
        txtSecretQ = (TextView) findViewById(R.id.outputsecretQ);
        txtYourPass = (TextView) findViewById(R.id.txtYourPassword);
        txtPassword = (TextView) findViewById(R.id.txtshowPass);

        txtYourPass.setVisibility(View.GONE);
        txtPassword.setVisibility(View.GONE);

    }

    private void initEvents() {

        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        if(b == null){
            ForgotPass = 0;
        }else{
            ForgotPass = (Integer) b.get("ForgotPass");
            email = (String) b.get("email");
        }

        if(ForgotPass == 1){
            StringRequest strRequest = new StringRequest(Request.Method.POST, getString(R.string.getQuestionAPI), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject trylang = new JSONObject(response);
                        status = trylang.getString("status");
                        question = trylang.getString("infos");
                        profile_id = trylang.getString("user");

                        if(status.equals("success")){
                            txtSecretQ.setText(question+"?");
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
                    parameters.put("email", email);
                    return parameters;
                }
            };
            AppController.getInstance().addToRequestQueue(strRequest);

            btnSubmitAnswer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StringRequest strRequest = new StringRequest(Request.Method.POST, getString(R.string.checkAnswerAPI), new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject trylang = new JSONObject(response);
                                status = trylang.getString("status");
                                if(status.equals("success")){
                                    password = trylang.getString("infos");
                                    txtYourPass.setVisibility(View.VISIBLE);
                                    txtPassword.setVisibility(View.VISIBLE);
                                    txtPassword.setText(password);
                                }else if(status.equals("failed")){
                                    message = trylang.getString("message");
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
                            parameters.put("profile_id",profile_id);
                            parameters.put("question_answer",edtAnswer.getText().toString());
                            return parameters;
                        }
                    };
                    AppController.getInstance().addToRequestQueue(strRequest);

                }
            });
        }else{
            StringRequest strRequest = new StringRequest(Request.Method.POST, getString(R.string.getQuestion2API), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject trylang = new JSONObject(response);
                        status = trylang.getString("status");
                        question = trylang.getString("infos");
                        if(status.equals("success")){
                            txtSecretQ.setText(question+"?");
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
                    parameters.put("profile_id", profIDPref);
                    return parameters;
                }
            };
            AppController.getInstance().addToRequestQueue(strRequest);

            btnSubmitAnswer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StringRequest strRequest = new StringRequest(Request.Method.POST, getString(R.string.checkAnswerAPI), new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject trylang = new JSONObject(response);
                                status = trylang.getString("status");
                                if(status.equals("success")){
                                    Intent i = new Intent(CodeForgotPassword.this, EditAccount.class);
                                    startActivity(i);
                                }else if(status.equals("failed")){
                                    message = trylang.getString("message");
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
                            parameters.put("profile_id",profIDPref);
                            parameters.put("question_answer",edtAnswer.getText().toString());
                            return parameters;
                        }
                    };
                    AppController.getInstance().addToRequestQueue(strRequest);

                }
            });
        }
    }
}
