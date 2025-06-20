package com.kelompok3.fulxas.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.github.mikephil.charting.animation.Easing;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kelompok3.fulxas.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class GrafikActivity extends AppCompatActivity {

    PieChart chartPendapatan, chartPengeluaran;
    TextView txtPendapatan, txtPengeluaran, txtTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafik);

        LinearLayout navHome = findViewById(R.id.nav_home);
        LinearLayout navHistory = findViewById(R.id.nav_history);
        FloatingActionButton fab = findViewById(R.id.fab);
        LinearLayout navAi = findViewById(R.id.nav_help);
        LinearLayout navChart = findViewById(R.id.nav_chart);


        navHome.setOnClickListener(v -> {
            Toast.makeText(GrafikActivity.this, "Home Clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(GrafikActivity.this, HomeActivity.class));
        });

        navHistory.setOnClickListener(v -> {
            Toast.makeText(GrafikActivity.this, "History Clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(GrafikActivity.this, HistoryActivity.class));
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GrafikActivity.this, AddActivity.class));
            }
        });

        navAi.setOnClickListener(v -> {
            Toast.makeText(GrafikActivity.this, "AI Clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(GrafikActivity.this, AiActivity.class));
        });

        navChart.setOnClickListener(v -> {
            Toast.makeText(GrafikActivity.this, "Chart Clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(GrafikActivity.this, GrafikActivity.class));
        });

        chartPendapatan = findViewById(R.id.chartPendapatan);
        chartPengeluaran = findViewById(R.id.chartPengeluaran);
        txtPendapatan = findViewById(R.id.txtPendapatan);
        txtPengeluaran = findViewById(R.id.txtPengeluaran);
        txtTotal = findViewById(R.id.txtTotal);

        ambilDataTransaksi();
    }

    private void ambilDataTransaksi() {
        String url = "http://10.0.2.2:8080/fulxas_api/grafik.php"; // Ganti dengan URL server kamu

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    Map<String, Float> pendapatanMap = new HashMap<>();
                    Map<String, Float> pengeluaranMap = new HashMap<>();
                    float totalPendapatan = 0;
                    float totalPengeluaran = 0;

                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            String kategori = obj.getString("kategori");
                            float jumlah = (float) obj.getDouble("total");
                            String tipe = obj.getString("tipe");

                            if (tipe.equalsIgnoreCase("Pendapatan")) {
                                pendapatanMap.put(kategori, jumlah);
                                totalPendapatan += jumlah;
                            } else {
                                pengeluaranMap.put(kategori, jumlah);
                                totalPengeluaran += jumlah;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    txtPendapatan.setText("Pendapatan: " + formatRupiah(totalPendapatan));
                    txtPendapatan.setTextColor(Color.parseColor("#D07A7A")); // hijau neon

                    txtPengeluaran.setText("Pengeluaran: " + formatRupiah(totalPengeluaran));
                    txtPengeluaran.setTextColor(Color.parseColor("#FAF5F5")); // merah terang

                    txtTotal.setText("Total: " + formatRupiah(totalPendapatan - totalPengeluaran));
                    txtTotal.setTextColor(Color.WHITE);
                    txtTotal.setTextSize(18f);


                    tampilkanChart(chartPendapatan, pendapatanMap, "Pendapatan");
                    tampilkanChart(chartPengeluaran, pengeluaranMap, "Pengeluaran");
                },
                error -> Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
        );

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private void tampilkanChart(PieChart chart, Map<String, Float> dataMap, String label) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        for (Map.Entry<String, Float> entry : dataMap.entrySet()) {
            entries.add(new PieEntry(entry.getValue(), entry.getKey()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(new int[]{
                Color.parseColor("#00E676"), // Hijau neon
                Color.parseColor("#FFD600"), // Kuning terang
                Color.parseColor("#FF3D00"), // Oranye terang
                Color.parseColor("#2979FF"), // Biru neon
                Color.parseColor("#4CAF50"),
                Color.parseColor("#03A9F4"),
                Color.parseColor("#9C27B0"),
                Color.parseColor("#795548"),
                Color.parseColor("#607D8B"),
                Color.parseColor("#E91E63"),
                Color.parseColor("#009688")
        });

        dataSet.setSliceSpace(2f);
        dataSet.setSelectionShift(8f);

        // Label di luar pie
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        // Garis penunjuk
        dataSet.setValueLinePart1Length(0.6f);
        dataSet.setValueLinePart2Length(0.5f);
        dataSet.setValueLineColor(Color.DKGRAY);
        dataSet.setValueLineWidth(2f);

        // Format ke persen
        dataSet.setValueFormatter(new PercentFormatter(chart));
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(12f);
        dataSet.setValueTypeface(Typeface.DEFAULT_BOLD);

        PieData pieData = new PieData(dataSet);
        chart.setData(pieData);

        chart.setUsePercentValues(true);
        chart.setDrawHoleEnabled(true);
        chart.setHoleRadius(45f);
        chart.setTransparentCircleRadius(55f);
        chart.setHoleColor(Color.TRANSPARENT);

        // ❌ Hapus teks tengah chart
        chart.setCenterText(""); // kosongkan
        chart.setCenterTextSize(0f);

        // Sembunyikan kategori dalam pie
        chart.setDrawEntryLabels(false);

        // ✅ Atur legend agar ada padding antar item
        Legend legend = chart.getLegend();
        legend.setEnabled(true);
        legend.setWordWrapEnabled(true);
        legend.setTextColor(Color.WHITE);
        legend.setTextSize(12f);
        legend.setFormSize(12f);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setXEntrySpace(20f); // padding horizontal antar legend
        legend.setYEntrySpace(10f); // padding vertikal antar legend

        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(10, 10, 10, 10);

        chart.animateY(1500, Easing.EaseInOutCubic);
        chart.invalidate();
    }



    private String formatRupiah(float jumlah) {
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("in", "ID"));
        return format.format(jumlah);
    }
}
