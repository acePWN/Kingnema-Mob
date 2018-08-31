package kingnema.kingnema;

import android.content.Intent;
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

public class ForgotPassword extends AppCompatActivity {

    private static String TAG = Confirmation.class.getSimpleName();
    private String status, message;

    private Button btnSubmit;
    private EditText edtEmail;
    private ImageView backLogo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        backLogo = (ImageView) findViewById(R.id.backButton);
        backLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ForgotPassword.this, Home.class);
                startActivity(i);

            }
        });
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        edtEmail = (EditText) findViewById(R.id.edtEmailAddress);

        initEvents();
    }

    private void initEvents() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest strRequest = new StringRequest(Request.Method.POST, getString(R.string.checkEmail), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject trylang = new JSONObject(response);
                            status = trylang.getString("status");
                            if(status.equals("success")){
                                Intent myIntent = new Intent(ForgotPassword.this, CodeForgotPassword.class);
                                myIntent.putExtra("ForgotPass",1);
                                myIntent.putExtra("email",edtEmail.getText().toString());
                                startActivity(myIntent);
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
                        parameters.put("email", edtEmail.getText().toString());
                        return parameters;

                    }
                };
                AppController.getInstance().addToRequestQueue(strRequest);
            }
        });

    }
}
