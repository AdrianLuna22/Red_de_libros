package com.example.red_de_libros;

import androidx.recyclerview.widget.RecyclerView;

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
        // Configurar imagen con Glide/Picasso
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo;
        ImageView ivPortada;
        Button btnOfrecer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvTitulo);
            ivPortada = itemView.findViewById(R.id.ivPortada);
            btnOfrecer = itemView.findViewById(R.id.btnOfrecer);
        }
    }
}
