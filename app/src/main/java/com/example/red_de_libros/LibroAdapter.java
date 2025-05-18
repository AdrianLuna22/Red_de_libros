package com.example.red_de_libros;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;
import com.example.red_de_libros.Libro;


public class LibroAdapter extends RecyclerView.Adapter<LibroAdapter.ViewHolder> {
    private List<Libro> libros;

    public LibroAdapter(List<Libro> libros) {
        this.libros = libros;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_libro, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Libro libro = libros.get(position);
        holder.tvTitulo.setText(libro.getTitulo());
        holder.tvAutor.setText("Autor: " + libro.getAutor());
        holder.tvAnio.setText("Año: " + libro.getAño());
        holder.tvDueno.setText("Dueño: " + libro.getDuenoId());

        Glide.with(holder.itemView.getContext())
                .load(libro.getPortadaUrl()) // URL de Imgur
                .placeholder(R.drawable.ic_launcher_background) // Puedes usar un drawable temporal
                .into(holder.ivPortada);
    }

    @Override
    public int getItemCount() {
        return libros.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPortada;
        TextView tvTitulo, tvAutor, tvAnio, tvDueno;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPortada = itemView.findViewById(R.id.ivPortada);
            tvTitulo = itemView.findViewById(R.id.tvTitulo);
            tvAutor = itemView.findViewById(R.id.tvAutor);
            tvAnio = itemView.findViewById(R.id.tvAnio);
            tvDueno = itemView.findViewById(R.id.tvDueno);
        }
    }
}


