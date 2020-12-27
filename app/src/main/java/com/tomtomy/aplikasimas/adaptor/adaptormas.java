package com.tomtomy.aplikasimas.adaptor;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tomtomy.aplikasimas.R;
import com.tomtomy.aplikasimas.model.modelmas;

import java.io.IOException;
import java.util.ArrayList;

public class adaptormas extends RecyclerView.Adapter<adaptormas.MasViewHolder> {
    private Context mContext;
    private ArrayList<modelmas> mMasModel;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public adaptormas(Context context, ArrayList<modelmas> masModels) {
        mContext = context;
        mMasModel = masModels;
    }

    @NonNull
    @Override
    public MasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
        return new MasViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull adaptormas.MasViewHolder holder, int position) {
        modelmas currentitem = mMasModel.get(position);

        String no = currentitem.getNo();
        String tgl = currentitem.getTgl();
        String jam = currentitem.getJam();
        String nama = currentitem.getUser();
        String kategori = currentitem.getKategori();
        String keterangan = currentitem.getKeterangan();
        String status = currentitem.getStatus();
        String jam_selesai = currentitem.getJam_Selesai();
        String solusi = currentitem.getSolusi();

        holder.tgl.setText(tgl);
        holder.nama.setText(nama);
        holder.kategori.setText(kategori);
        holder.status.setText(status);
        holder.jam_selesai.setText(jam_selesai);
        holder.keterangan.setText(keterangan);
        holder.solusi.setText(solusi);
    }

    @Override
    public int getItemCount() {
        return mMasModel.size();
    }

    public class MasViewHolder extends RecyclerView.ViewHolder {
        public TextView tgl, nama, kategori, status, keterangan, jam_selesai, solusi;

        public MasViewHolder(@NonNull View itemView) {
            super(itemView);
            tgl = itemView.findViewById(R.id.id_tgl);
            nama = itemView.findViewById(R.id.id_nama);
            kategori = itemView.findViewById(R.id.id_kategori);
            status = itemView.findViewById(R.id.id_status);
            keterangan = itemView.findViewById(R.id.id_keterangans);
            jam_selesai = itemView.findViewById(R.id.id_jam_selesais);
            solusi = itemView.findViewById(R.id.id_solusis);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener !=null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            mListener.onItemClick(position);
                        }
                    }
                }
            });

        }
    }
}
