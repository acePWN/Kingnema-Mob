package kingnema.kingnema;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    private String status,message;

    private ArrayList<String> arrayQuestions;
    private ArrayList<String> arrayQuestionID;
    private String chosenQuestion, chosenQID, referenceID;
    private EditText lastname;
    private EditText firstname;
    private EditText address;
    private EditText contactno;
    private EditText email;
    private EditText cardno;
    private EditText username;
    private EditText password;
    private Button submit;
    private TextView txtEULA;
    private Spinner spinnerQuestions;
    private EditText answerQuestion;
    private static String TAG = SignUp.class.getSimpleName();

    ImageView logo,backLogo,accountLogo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initResources();
        initEvents();

        logo=(ImageView)findViewById(R.id.logoButton);
        backLogo=(ImageView)findViewById(R.id.backButton);
        accountLogo=(ImageView)findViewById(R.id.accountButton);

        backLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignUp.this, Home.class);
                startActivity(i);
            }
        });
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignUp.this, Home.class);
                startActivity(i);
            }
        });
    }

    public void initResources(){

        lastname = (EditText)findViewById(R.id.editLastName);
        firstname = (EditText)findViewById(R.id.editFirstName);
        address = (EditText)findViewById(R.id.editAddress);
        contactno = (EditText)findViewById(R.id.editContact);
        email = (EditText)findViewById(R.id.editEmail);
        username = (EditText)findViewById(R.id.editUsername);
        password = (EditText)findViewById(R.id.editPassword);
        submit = (Button)findViewById(R.id.buttonSubmit);
        txtEULA = (TextView) findViewById(R.id.textEULA2);
        spinnerQuestions = (Spinner) findViewById(R.id.spinnerQuestions);
        answerQuestion = (EditText) findViewById(R.id.editAnswerQuestion);

    }

    public void initEvents(){
        StringRequest strRequest = new StringRequest(Request.Method.POST, getString(R.string.getQuestionsAPI), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject trylang = new JSONObject(response);
                    status = trylang.getString("status");


                    if(status.equals("success")){
                        arrayQuestions = new ArrayList<String>();
                        arrayQuestionID = new ArrayList<String>();
                        JSONArray infos = trylang.getJSONArray("infos");
                        for(int i = 0; i < infos.length(); i++){
                            JSONObject c = infos.getJSONObject(i);
                            arrayQuestionID.add(c.getString("question_id"));
                            arrayQuestions.add(c.getString("question"));
                        }

                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(),  R.layout.spinner_layout, arrayQuestions);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerQuestions.setAdapter(dataAdapter);
                        spinnerQuestions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                chosenQuestion = arrayQuestions.get(i);
                                chosenQID = arrayQuestionID.get(i);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });


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
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                return parameters;
            }
        };
        AppController.getInstance().addToRequestQueue(strRequest);

        submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                signUp();
            }
        });
        txtEULA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewEULA();
            }
        });
    }
    private void viewEULA() {
         Intent i = new Intent(SignUp.this, EULA.class);
         startActivity(i);
    }

    void signUp(){
        StringRequest strRequest = new StringRequest(Request.Method.POST, getString(R.string.signUp), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                JSONObject trylang = null;
                try {
                    trylang = new JSONObject(response);
                    message = trylang.getString("message");
                    Toast.makeText(getApplicationContext(),message, Toast.LENGTH_LONG).show();
                    if(trylang.getString("status").equals("success")){
                        Intent i = new Intent(SignUp.this, Home.class);
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
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();

                parameters.put("last_name", lastname.getText().toString());
                parameters.put("first_name", firstname.getText().toString());
                parameters.put("address", address.getText().toString());
                parameters.put("contact_number", contactno.getText().toString());
                parameters.put("email", email.getText().toString());
                parameters.put("username", username.getText().toString());
                parameters.put("password", password.getText().toString());
                parameters.put("question_id", chosenQID);
                parameters.put("question_answer", answerQuestion.getText().toString());

                return parameters;
            }
        };
        AppController.getInstance().addToRequestQueue(strRequest);
    }



}