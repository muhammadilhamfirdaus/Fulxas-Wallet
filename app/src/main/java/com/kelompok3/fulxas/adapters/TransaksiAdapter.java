package com.kelompok3.fulxas.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.kelompok3.fulxas.R;
import com.kelompok3.fulxas.models.Transaksi;
import java.util.List;

public class TransaksiAdapter extends RecyclerView.Adapter<TransaksiAdapter.ViewHolder> {
    private Context context;
    private List<Transaksi> list;

    public TransaksiAdapter(Context context, List<Transaksi> list) {
        this.context = context;
        this.list = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView kategori, tanggal, jumlah, tipe;

        public ViewHolder(View itemView) {
            super(itemView);
            kategori = itemView.findViewById(R.id.tvKategori);
            tanggal = itemView.findViewById(R.id.tvTanggal);
            jumlah = itemView.findViewById(R.id.tvJumlah);
            tipe = itemView.findViewById(R.id.tvTipe);
        }
    }

    @Override
    public TransaksiAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_transaksi, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TransaksiAdapter.ViewHolder holder, int position) {
        Transaksi t = list.get(position);

        Log.d("TransaksiAdapter", "Tipe: " + t.tipe);

        holder.kategori.setText(t.kategori);
        holder.tanggal.setText(t.tanggal + " " + t.waktu);


        double jumlah = t.jumlah;

        String tipe = t.tipe.trim().toLowerCase();

        if (tipe.equals("pendapatan")) {
            holder.jumlah.setText("+ Rp " + String.format("%.2f", jumlah));
            holder.jumlah.setTextColor(context.getResources().getColor(R.color.green));
        } else if (tipe.equals("pengeluaran")) {
            holder.jumlah.setText("- Rp " + String.format("%.2f", jumlah));
            holder.jumlah.setTextColor(context.getResources().getColor(R.color.red));
        } else {
            holder.jumlah.setText("Rp " + String.format("%.2f", jumlah));
            holder.jumlah.setTextColor(context.getResources().getColor(R.color.black));
        }

        holder.tipe.setText(t.tipe);
    }



    @Override
    public int getItemCount() {
        return list.size();
    }
}
