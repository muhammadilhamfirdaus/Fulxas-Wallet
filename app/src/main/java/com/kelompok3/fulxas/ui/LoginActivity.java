package com.kelompok3.fulxas.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.kelompok3.fulxas.R;
import com.kelompok3.fulxas.ui.HomeActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail, edtPassword;
    private Button btnLogin;
    private ImageButton btnBack;
    private TextView txtSignUp;
    private static final String LOGIN_URL = "http://10.0.2.2:8080/fulxas_api/login.php"; // Ganti dengan URL API Anda

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnBack = findViewById(R.id.btnBack);
        txtSignUp = findViewById(R.id.txtSignUp);

        txtSignUp.setText(android.text.Html.fromHtml(
                "Don't have an account? <font color='#2196F3'>Sign Up</font>"
        ));


        // Tombol back kembali ke MainActivity
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIntent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(backIntent);
                finish(); // Tutup LoginActivity biar tidak bisa tekan tombol back ke sini
            }
        });

        // Teks Sign Up pergi ke RegisterActivity
        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edtEmail.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    edtEmail.setError("Email is required");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    edtPassword.setError("Password is required");
                    return;
                }

                loginUser(email, password);
            }
        });
    }

    private void loginUser(final String email, final String password) {
        JSONObject requestParams = new JSONObject();
        try {
            requestParams.put("email", email);
            requestParams.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Membuat request POST untuk login
        JsonObjectRequest loginRequest = new JsonObjectRequest(
                Request.Method.POST,
                LOGIN_URL,
                requestParams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("Login Response", response.toString()); // Menambahkan log response
                            String status = response.getString("status");
                            String message = response.getString("message");

                            if (status.equals("success")) {
                                // Login berhasil
                                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();

                                // Pindah ke HomeActivity setelah login berhasil
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish(); // Tutup LoginActivity agar tidak bisa kembali ke login setelah login
                            } else {
                                // Login gagal
                                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this, "JSON error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Login Error", error.toString());
                        Toast.makeText(LoginActivity.this, "Login failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Menambahkan request ke request queue
        Volley.newRequestQueue(this).add(loginRequest);
    }
}
