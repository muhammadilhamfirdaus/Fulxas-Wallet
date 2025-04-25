package com.kelompok3.fulxas.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kelompok3.fulxas.R;
import com.kelompok3.fulxas.adapters.TransaksiAdapter;
import com.kelompok3.fulxas.models.Transaksi;
import com.kelompok3.fulxas.network.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<Transaksi> transaksiList;
    TransaksiAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        LinearLayout navHistory = findViewById(R.id.nav_history);
        FloatingActionButton fab = findViewById(R.id.fab);
        LinearLayout navAi = findViewById(R.id.nav_help);
        LinearLayout navChart = findViewById(R.id.nav_chart);


        navHistory.setOnClickListener(v -> {
            Toast.makeText(HistoryActivity.this, "History Clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(HistoryActivity.this, HistoryActivity.class));
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HistoryActivity.this, AddActivity.class));
            }
        });

        navAi.setOnClickListener(v -> {
            Toast.makeText(HistoryActivity.this, "AI Clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(HistoryActivity.this, AiActivity.class));
        });

        navChart.setOnClickListener(v -> {
            Toast.makeText(HistoryActivity.this, "Chart Clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(HistoryActivity.this, GrafikActivity.class));
        });

        recyclerView = findViewById(R.id.recyclerView);
        transaksiList = new ArrayList<>();
        adapter = new TransaksiAdapter(this, transaksiList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        ambilDataTransaksi();
    }

    private void ambilDataTransaksi() {
        String URL = "http://10.0.2.2:8080/fulxas_api/get_transaksi.php"; // ganti IP kamu sesuai server

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
                                transaksiList.add(t);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this, "Gagal koneksi: " + error.toString(), Toast.LENGTH_SHORT).show()
        );

        Volley.newRequestQueue(this).add(request);
    }
}