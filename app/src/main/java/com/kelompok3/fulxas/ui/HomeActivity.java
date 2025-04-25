package com.kelompok3.fulxas.ui;

import android.content.Intent;
import android.os.Bundle;

import java.util.Calendar;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kelompok3.fulxas.R;

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
        setContentView(R.layout.activity_home); // Pastikan layout ini sesuai

        LinearLayout navHistory = findViewById(R.id.nav_history);
        FloatingActionButton fab = findViewById(R.id.fab);
        LinearLayout navAi = findViewById(R.id.nav_help);
        LinearLayout navChart = findViewById(R.id.nav_chart);


        navHistory.setOnClickListener(v -> {
            Toast.makeText(HomeActivity.this, "History Clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(HomeActivity.this, HistoryActivity.class));
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, AddActivity.class));
            }
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

        // Ambil bulan dan tahun saat ini
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
    }

    private void updateDate() {
        tvMonthYear.setText(months[month] + " " + year);
    }
}
