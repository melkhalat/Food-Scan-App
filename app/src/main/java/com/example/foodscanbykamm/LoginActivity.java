package com.example.foodscanbykamm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.os.AsyncTask;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    // Declare variables

    EditText editEmail, editPassword, editName;
    Button btnSignIn, btnRegister;

    String URL = "http://10.0.2.2:8080/FoodScan/index.php";

    JSONParser jsonParser = new JSONParser();

    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editEmail = findViewById(R.id.editEmail);
        editName = findViewById(R.id.editName);
        editPassword = findViewById(R.id.editPassword);

        btnSignIn = findViewById(R.id.btnSignIn);
        btnRegister = findViewById(R.id.btnRegister);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AttemptLogin attemptLogin = new AttemptLogin();
                attemptLogin.execute(editName.getText().toString(), editPassword.getText().toString(), "");
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (i == 0) {
                    i = 1;
                    editEmail.setVisibility(View.VISIBLE);
                    btnSignIn.setVisibility(View.GONE);
                    btnRegister.setText("CREATE ACCOUNT");
                } else {

                    btnRegister.setText("REGISTER");
                    editEmail.setVisibility(View.GONE);
                    btnSignIn.setVisibility(View.VISIBLE);
                    i = 0;

                    AttemptLogin attemptLogin = new AttemptLogin();
                    attemptLogin.execute(editName.getText().toString(), editPassword.getText().toString(), editEmail.getText().toString());

                }

            }
        });


    }

    private class AttemptLogin extends AsyncTask<String, String, JSONObject> {

        @Override

        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override

        protected JSONObject doInBackground(String... args) {


            String email = args[2];
            String password = args[1];
            String name = args[0];

            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("username", name));
            params.add(new BasicNameValuePair("password", password));
            if (email.length() > 0)
                params.add(new BasicNameValuePair("email", email));

            JSONObject json = jsonParser.makeHttpRequest(URL, "POST", params);


            return json;

        }

        protected void onPostExecute(JSONObject result) {

            try {
                if (result != null) {
                    Toast.makeText(getApplicationContext(), result.getString("message"), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Unable to retrieve any data from server", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

    }
}
