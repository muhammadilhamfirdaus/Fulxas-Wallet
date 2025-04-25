package com.kelompok3.fulxas.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kelompok3.fulxas.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AddActivity extends AppCompatActivity {

    private EditText etTanggal, etWaktu, etJumlah;
    private Spinner spinnerKategori, spinnerRekening;
    private Button btnPendapatan, btnPengeluaran, btnSubmit;
    private final Calendar calendar = Calendar.getInstance();
    private List<String> kategoriPendapatan, kategoriPengeluaran;
    private ArrayAdapter<String> kategoriAdapter;

    private String currentTipe = "Pengeluaran"; // Default

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        LinearLayout navHistory = findViewById(R.id.nav_history);
        FloatingActionButton fab = findViewById(R.id.fab);
        LinearLayout navAi = findViewById(R.id.nav_help);
        LinearLayout navChart = findViewById(R.id.nav_chart);


        navHistory.setOnClickListener(v -> {
            Toast.makeText(AddActivity.this, "History Clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AddActivity.this, HistoryActivity.class));
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddActivity.this, AddActivity.class));
            }
        });

        navAi.setOnClickListener(v -> {
            Toast.makeText(AddActivity.this, "AI Clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AddActivity.this, AiActivity.class));
        });

        navChart.setOnClickListener(v -> {
            Toast.makeText(AddActivity.this, "Chart Clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AddActivity.this, GrafikActivity.class));
        });

        // Inisialisasi komponen
        btnPendapatan = findViewById(R.id.btnPendapatan);
        btnPengeluaran = findViewById(R.id.btnPengeluaran);
        spinnerKategori = findViewById(R.id.spinnerKategori);
        spinnerRekening = findViewById(R.id.spinnerRekening);
        etTanggal = findViewById(R.id.etTanggal);
        etWaktu = findViewById(R.id.etWaktu);
        etJumlah = findViewById(R.id.etJumlah);
        btnSubmit = findViewById(R.id.btnSubmit);

        // Data kategori Pendapatan
        kategoriPendapatan = new ArrayList<>();
        kategoriPendapatan.add("Gaji");
        kategoriPendapatan.add("Bonus");
        kategoriPendapatan.add("Investasi");
        kategoriPendapatan.add("Hadiah");

        // Data kategori Pengeluaran
        kategoriPengeluaran = new ArrayList<>();
        kategoriPengeluaran.add("Makanan & Minuman");
        kategoriPengeluaran.add("Transportasi");
        kategoriPengeluaran.add("Belanja");
        kategoriPengeluaran.add("Tagihan");
        kategoriPengeluaran.add("Hiburan");

        // Inisialisasi Adapter dengan Kategori Pengeluaran (default)
        kategoriAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, kategoriPengeluaran);
        spinnerKategori.setAdapter(kategoriAdapter);

        // Data rekening
        List<String> rekeningList = new ArrayList<>();
        rekeningList.add("Rekening Utama");
        rekeningList.add("Dompet Digital");
        rekeningList.add("Kartu Kredit");

        // Adapter untuk Spinner Rekening
        ArrayAdapter<String> rekeningAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, rekeningList);
        spinnerRekening.setAdapter(rekeningAdapter);

        // Toggle button untuk Pendapatan
        btnPendapatan.setOnClickListener(v -> {
            updateKategoriSpinner(kategoriPendapatan, "Mode Pendapatan");
            currentTipe = "Pendapatan";
            // Ubah border tombol aktif
            btnPendapatan.setBackgroundResource(R.drawable.button_pendapatan); // Border hijau
            btnPendapatan.setBackgroundResource(R.drawable.border_inactive);

        });

// Toggle button untuk Pengeluaran
        btnPengeluaran.setOnClickListener(v -> {
            updateKategoriSpinner(kategoriPengeluaran, "Mode Pengeluaran");
            currentTipe = "Pengeluaran";
            // Ubah border tombol aktif
            btnPengeluaran.setBackgroundResource(R.drawable.button_pengeluaran); // Border merah
            btnPengeluaran.setBackgroundResource(R.drawable.border_inactive);
        });



        etTanggal.setOnClickListener(v -> showDatePicker());
        etWaktu.setOnClickListener(v -> showTimePicker());

        // Submit button untuk menambahkan data
        btnSubmit.setOnClickListener(v -> submitData());
    }

    // Method untuk memperbarui Spinner Kategori
    private void updateKategoriSpinner(List<String> kategoriList, String mode) {
        kategoriAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, kategoriList);
        spinnerKategori.setAdapter(kategoriAdapter);
        Toast.makeText(this, mode, Toast.LENGTH_SHORT).show();
    }

    private void showDatePicker() {
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            etTanggal.setText(dateFormat.format(calendar.getTime()));
        },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showTimePicker() {
        new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            etWaktu.setText(timeFormat.format(calendar.getTime()));
        },
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
    }

    // Method untuk mengirim data ke server menggunakan Volley
    private void submitData() {
        String kategori = spinnerKategori.getSelectedItem().toString();
        String tanggal = etTanggal.getText().toString();
        String waktu = etWaktu.getText().toString();
        String rekening = spinnerRekening.getSelectedItem().toString();
        String jumlah = etJumlah.getText().toString();
        String tipe = currentTipe;

        String url = "http://10.0.2.2:8080/fulxas_api/add_transaksi.php"; // Ganti dengan URL server Anda

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(AddActivity.this, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AddActivity.this, "Gagal menambahkan data", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("kategori", kategori);
                params.put("tanggal", tanggal);
                params.put("waktu", waktu);
                params.put("rekening", rekening);
                params.put("jumlah", jumlah);
                params.put("tipe", tipe);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
