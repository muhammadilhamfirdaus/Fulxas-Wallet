package com.kelompok3.fulxas.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kelompok3.fulxas.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AiActivity extends AppCompatActivity {
    private EditText etUserInput;
    private Button btnSend;
    private TextView tvChatResponse;

    private final String GEMINI_API_KEY = "AIzaSyCqWz2BX7fDTStFrQdtDWiigA1DI982vek"; // Ganti dengan key asli kamu
    private final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + GEMINI_API_KEY;

    private List<String> transaksiHistory = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai);

        LinearLayout navHome = findViewById(R.id.nav_home);
        LinearLayout navHistory = findViewById(R.id.nav_history);
        FloatingActionButton fab = findViewById(R.id.fab);
        LinearLayout navAi = findViewById(R.id.nav_help);
        LinearLayout navChart = findViewById(R.id.nav_chart);


        navHome.setOnClickListener(v -> {
            Toast.makeText(AiActivity.this, "Home Clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AiActivity.this, HomeActivity.class));
        });

        navHistory.setOnClickListener(v -> {
            Toast.makeText(AiActivity.this, "History Clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AiActivity.this, HistoryActivity.class));
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AiActivity.this, AddActivity.class));
            }
        });

        navAi.setOnClickListener(v -> {
            Toast.makeText(AiActivity.this, "AI Clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AiActivity.this, AiActivity.class));
        });

        navChart.setOnClickListener(v -> {
            Toast.makeText(AiActivity.this, "Chart Clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AiActivity.this, GrafikActivity.class));
        });

        etUserInput = findViewById(R.id.etUserInput);
        btnSend = findViewById(R.id.btnSend);
        tvChatResponse = findViewById(R.id.tvChatResponse);

        ambilHistoryTransaksi(); // Panggil saat activity dibuat

        btnSend.setOnClickListener(view -> {
            String input = etUserInput.getText().toString().trim();
            if (!input.isEmpty()) {
                sendMessageToGemini(input);
            } else {
                Toast.makeText(this, "Masukkan pertanyaan terlebih dahulu!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ambilHistoryTransaksi() {
        String URL = "http://10.0.2.2:80/fulxas_api/get_transaksi.php"; // Ganti jika IP server kamu beda

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(URL).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> Toast.makeText(AiActivity.this, "Gagal ambil history: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(responseBody);
                    if (jsonObject.getBoolean("success")) {
                        JSONArray data = jsonObject.getJSONArray("data");
                        transaksiHistory.clear();
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject obj = data.getJSONObject(i);
                            String ringkasan = obj.getString("tanggal") + " - " +
                                    obj.getString("kategori") + " - " +
                                    obj.getDouble("jumlah") + " (" + obj.getString("tipe") + ")";
                            transaksiHistory.add(ringkasan);
                        }
                    }
                } catch (Exception e) {
                    runOnUiThread(() -> Toast.makeText(AiActivity.this, "Gagal parsing history", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    private void sendMessageToGemini(String userMessage) {
        OkHttpClient client = new OkHttpClient();

        // Gabungkan history transaksi ke dalam prompt
        StringBuilder promptBuilder = new StringBuilder();
        promptBuilder.append("Berikut ini adalah riwayat transaksi saya:\n");
        for (String t : transaksiHistory) {
            promptBuilder.append("- ").append(t).append("\n");
        }
        promptBuilder.append("\nPertanyaan saya: ").append(userMessage);

        String finalPrompt = promptBuilder.toString();

        // Buat JSON request untuk Gemini
        JSONObject content = new JSONObject();
        JSONArray parts = new JSONArray();
        try {
            JSONObject part = new JSONObject();
            part.put("text", finalPrompt);
            parts.put(part);
            content.put("contents", new JSONArray().put(new JSONObject().put("parts", parts)));
        } catch (Exception e) {
            runOnUiThread(() -> tvChatResponse.setText("Error saat membuat JSON: " + e.getMessage()));
            return;
        }

        RequestBody body = RequestBody.create(content.toString(), MediaType.get("application/json"));
        Request request = new Request.Builder()
                .url(GEMINI_API_URL)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> tvChatResponse.setText("Gagal menghubungi AI: " + e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                try {
                    JSONObject jsonResponse = new JSONObject(responseBody);
                    JSONArray candidates = jsonResponse.getJSONArray("candidates");
                    if (candidates.length() > 0) {
                        JSONObject first = candidates.getJSONObject(0);
                        JSONObject content = first.getJSONObject("content");
                        JSONArray parts = content.getJSONArray("parts");
                        String reply = parts.getJSONObject(0).getString("text");

                        runOnUiThread(() -> tvChatResponse.setText(reply));
                    } else {
                        runOnUiThread(() -> tvChatResponse.setText("Tidak ada jawaban dari AI."));
                    }
                } catch (Exception e) {
                    runOnUiThread(() -> tvChatResponse.setText("Gagal parsing respon: " + e.getMessage()));
                }
            }
        });
    }
}
