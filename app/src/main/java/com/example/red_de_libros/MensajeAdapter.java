package com.example.red_de_libros;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class MensajeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int MENSAJE_ENVIADO = 1;
    private static final int MENSAJE_RECIBIDO = 2;

    private List<Mensaje> mensajes;
    private String currentUserId;

    public MensajeAdapter(List<Mensaje> mensajes) {
        this.mensajes = mensajes;
        FirebaseAuth auth = FirebaseAuth.getInstance();
        this.currentUserId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : "";
    }

    @Override
    public int getItemViewType(int position) {
        Mensaje mensaje = mensajes.get(position);
        return mensaje.getEmisorId().equals(currentUserId) ? MENSAJE_ENVIADO : MENSAJE_RECIBIDO;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MENSAJE_ENVIADO) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_mensaje_enviado, parent, false);
            return new MensajeEnviadoHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_mensaje_recibido, parent, false);
            return new MensajeRecibidoHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Mensaje mensaje = mensajes.get(position);

        if (holder.getItemViewType() == MENSAJE_ENVIADO) {
            ((MensajeEnviadoHolder) holder).bind(mensaje);
        } else {
            ((MensajeRecibidoHolder) holder).bind(mensaje);
        }
    }

    @Override
    public int getItemCount() {
        return mensajes.size();
    }

    public void setMensajes(List<Mensaje> mensajes) {
        this.mensajes = mensajes;
        notifyDataSetChanged();
    }

    // ViewHolder para mensajes enviados (derecha)
    private static class MensajeEnviadoHolder extends RecyclerView.ViewHolder {
        TextView tvMensaje, tvHora;

        MensajeEnviadoHolder(View itemView) {
            super(itemView);
            tvMensaje = itemView.findViewById(R.id.tvMensaje);
            tvHora = itemView.findViewById(R.id.tvHora);
        }

        void bind(Mensaje mensaje) {
            tvMensaje.setText(mensaje.getTexto());
            tvHora.setText(DateFormat.format("HH:mm", mensaje.getTimestamp()));
        }
    }

    // ViewHolder para mensajes recibidos (izquierda)
    private static class MensajeRecibidoHolder extends RecyclerView.ViewHolder {
        TextView tvMensaje, tvHora;

        MensajeRecibidoHolder(View itemView) {
            super(itemView);
            tvMensaje = itemView.findViewById(R.id.tvMensaje);
            tvHora = itemView.findViewById(R.id.tvHora);
        }

        void bind(Mensaje mensaje) {
            tvMensaje.setText(mensaje.getTexto());
            tvHora.setText(DateFormat.format("HH:mm", mensaje.getTimestamp()));
        }
    }
}