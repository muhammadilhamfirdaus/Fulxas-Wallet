package com.kelompok3.fulxas.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kelompok3.fulxas.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;

public class HomeActivity extends AppCompatActivity {

    private TextView tvMonthYear;
    private int month, year;
    private final String[] months = {
            "Januari", "Februari", "Maret", "April", "Mei", "Juni",
            "Juli", "Agustus", "September", "Oktober", "November", "Desember"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        LinearLayout navHome = findViewById(R.id.nav_home);
        LinearLayout navHistory = findViewById(R.id.nav_history);
        FloatingActionButton fab = findViewById(R.id.fab);
        LinearLayout navAi = findViewById(R.id.nav_help);
        LinearLayout navChart = findViewById(R.id.nav_chart);

        navHome.setOnClickListener(v -> {
            Toast.makeText(HomeActivity.this, "Home Clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(HomeActivity.this, HomeActivity.class));
        });

        navHistory.setOnClickListener(v -> {
            Toast.makeText(HomeActivity.this, "History Clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(HomeActivity.this, HistoryActivity.class));
        });

        fab.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, AddActivity.class));
        });

        navAi.setOnClickListener(v -> {
            Toast.makeText(HomeActivity.this, "AI Clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(HomeActivity.this, AiActivity.class));
        });

        navChart.setOnClickListener(v -> {
            Toast.makeText(HomeActivity.this, "Chart Clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(HomeActivity.this, GrafikActivity.class));
        });

        tvMonthYear = findViewById(R.id.tvMonthYear);
        TextView btnPrev = findViewById(R.id.btnPrev);
        TextView btnNext = findViewById(R.id.btnNext);

        Calendar calendar = Calendar.getInstance();
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
        updateDate();

        btnPrev.setOnClickListener(v -> {
            if (month == 0) {
                month = 11;
                year--;
            } else {
                month--;
            }
            updateDate();
        });

        btnNext.setOnClickListener(v -> {
            if (month == 11) {
                month = 0;
                year++;
            } else {
                month++;
            }
            updateDate();
        });

        // Ambil referensi TextView untuk kalkulasi
        TextView textPendapatan = findViewById(R.id.txtPendapatan);
        TextView textPengeluaran = findViewById(R.id.txtPengeluaran);
        TextView textTotal = findViewById(R.id.txtTotal);

        // Ambil data kalkulasi dari server
        getKalkulasiData(textPendapatan, textPengeluaran, textTotal);
    }

    private void updateDate() {
        tvMonthYear.setText(months[month] + " " + year);
    }

    private void getKalkulasiData(TextView textPendapatan, TextView textPengeluaran, TextView textTotal) {
        String url = "http://10.0.2.2:80/fulxas_api/kalkulasi.php"; // GANTI IP sesuai alamat server lokal atau hosting kamu

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    int totalPendapatan = 0;
                    int totalPengeluaran = 0;

                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject obj = response.getJSONObject(i);
                            String tipe = obj.getString("tipe");
                            int jumlah = obj.getInt("total");

                            if (tipe.equalsIgnoreCase("pendapatan")) {
                                totalPendapatan += jumlah;
                            } else if (tipe.equalsIgnoreCase("pengeluaran")) {
                                totalPengeluaran += jumlah;
                            }
                        }

                        int total = totalPendapatan - totalPengeluaran;

                        textPendapatan.setText("Rp" + totalPendapatan);
                        textPengeluaran.setText("Rp" + totalPengeluaran);
                        textTotal.setText("Rp" + total);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Gagal parsing data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Gagal mengambil data dari server", Toast.LENGTH_SHORT).show();
                });

        queue.add(request);
    }
}
