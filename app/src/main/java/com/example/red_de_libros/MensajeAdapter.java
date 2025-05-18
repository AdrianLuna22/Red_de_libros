package com.example.red_de_libros;

import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MensajeAdapter extends RecyclerView.Adapter<MensajeAdapter.ViewHolder> {

    private List<Mensaje> mensajes = new ArrayList<>();
    private String currentUserId;

    public MensajeAdapter() {
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mensaje, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Mensaje mensaje = mensajes.get(position);

        holder.tvNombre.setText(mensaje.getNombreUsuario());
        holder.tvMensaje.setText(mensaje.getTexto());
        holder.tvHora.setText(DateUtils.formatDateTime(
                holder.itemView.getContext(),
                mensaje.getTimestamp(),
                DateUtils.FORMAT_SHOW_TIME
        ));

        // Alinear mensajes propios a la derecha
        if(mensaje.getUsuarioId().equals(currentUserId)) {
            holder.lyMensaje.setBackgroundResource(R.drawable.bg_mi_mensaje);
            ((LinearLayout.LayoutParams) holder.lyMensaje.getLayoutParams()).gravity = Gravity.END;
        } else {
            holder.lyMensaje.setBackgroundResource(R.drawable.bg_otro_mensaje);
            ((LinearLayout.LayoutParams) holder.lyMensaje.getLayoutParams()).gravity = Gravity.START;
        }
    }

    @Override
    public int getItemCount() {
        return mensajes.size();
    }

    public void addMensaje(Mensaje mensaje) {
        mensajes.add(mensaje);
        notifyItemInserted(mensajes.size() - 1);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout lyMensaje;
        TextView tvNombre, tvMensaje, tvHora;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lyMensaje = itemView.findViewById(R.id.lyMensaje);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvMensaje = itemView.findViewById(R.id.tvMensaje);
            tvHora = itemView.findViewById(R.id.tvHora);
        }
    }
}