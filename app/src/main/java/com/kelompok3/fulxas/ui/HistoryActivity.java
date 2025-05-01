package com.kelompok3.fulxas.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kelompok3.fulxas.R;
import com.kelompok3.fulxas.adapters.TransaksiAdapter;
import com.kelompok3.fulxas.models.Transaksi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private TextView tvMonthYear;
    private int month, year;
    private final String[] months = {
            "Januari", "Februari", "Maret", "April", "Mei", "Juni",
            "Juli", "Agustus", "September", "Oktober", "November", "Desember"
    };

    RecyclerView recyclerView;
    List<Transaksi> transaksiList;
    List<Transaksi> allTransaksiList;
    TransaksiAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        TextView textViewAll = findViewById(R.id.textViewAll);

        // Inisialisasi UI
        LinearLayout navHome = findViewById(R.id.nav_home);
        LinearLayout navHistory = findViewById(R.id.nav_history);
        FloatingActionButton fab = findViewById(R.id.fab);
        LinearLayout navAi = findViewById(R.id.nav_help);
        LinearLayout navChart = findViewById(R.id.nav_chart);

        tvMonthYear = findViewById(R.id.tvMonthYear);
        TextView btnPrev = findViewById(R.id.btnPrev);
        TextView btnNext = findViewById(R.id.btnNext);

        // Setup RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        transaksiList = new ArrayList<>();
        allTransaksiList = new ArrayList<>();
        adapter = new TransaksiAdapter(this, transaksiList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Ambil bulan dan tahun saat ini
        Calendar calendar = Calendar.getInstance();
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);

        updateDate();

        // Set OnClickListener
        textViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent untuk memulai ViewAllActivity
                Intent intent = new Intent(HistoryActivity.this, ViewAllActivity.class);
                startActivity(intent);
            }
        });

        // Setup navigasi tombol bawah
        navHome.setOnClickListener(v -> {
            Toast.makeText(HistoryActivity.this, "Home Clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(HistoryActivity.this, HomeActivity.class));
        });

        navHistory.setOnClickListener(v -> {
            Toast.makeText(HistoryActivity.this, "History Clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(HistoryActivity.this, HistoryActivity.class));
        });

        fab.setOnClickListener(v -> startActivity(new Intent(HistoryActivity.this, AddActivity.class)));

        navAi.setOnClickListener(v -> {
            Toast.makeText(HistoryActivity.this, "AI Clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(HistoryActivity.this, AiActivity.class));
        });

        navChart.setOnClickListener(v -> {
            Toast.makeText(HistoryActivity.this, "Chart Clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(HistoryActivity.this, GrafikActivity.class));
        });

        // Tombol navigasi bulan
        btnPrev.setOnClickListener(v -> {
            if (month == 0) {
                month = 11;
                year--;
            } else {
                month--;
            }
            updateDate();
            filterDataTransaksi();
        });

        btnNext.setOnClickListener(v -> {
            if (month == 11) {
                month = 0;
                year++;
            } else {
                month++;
            }
            updateDate();
            filterDataTransaksi();
        });

        ambilDataTransaksi();
    }

    private void ambilDataTransaksi() {
        String URL = "http://10.0.2.2:80/fulxas_api/get_transaksi.php"; // Ganti IP kamu sesuai server

        StringRequest request = new StringRequest(Request.Method.GET, URL,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getBoolean("success")) {
                            JSONArray data = jsonObject.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject obj = data.getJSONObject(i);
                                Transaksi t = new Transaksi(
                                        obj.getInt("id"),
                                        obj.getString("kategori"),
                                        obj.getString("tanggal"),
                                        obj.getString("waktu"),
                                        obj.getString("rekening"),
                                        obj.getDouble("jumlah"),
                                        obj.getString("tipe")
                                );
                                allTransaksiList.add(t);
                            }
                            filterDataTransaksi();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this, "Gagal koneksi: " + error.toString(), Toast.LENGTH_SHORT).show()
        );

        Volley.newRequestQueue(this).add(request);
    }


    private void filterDataTransaksi() {
        transaksiList.clear();
        for (Transaksi t : allTransaksiList) {
            String[] tanggalParts = t.tanggal.split("-");
            int tYear = Integer.parseInt(tanggalParts[0]);
            int tMonth = Integer.parseInt(tanggalParts[1]) - 1; // Karena bulan di Calendar mulai dari 0

            if (tYear == year && tMonth == month) {
                transaksiList.add(t);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void updateDate() {
        tvMonthYear.setText(months[month] + " " + year);
    }
}
