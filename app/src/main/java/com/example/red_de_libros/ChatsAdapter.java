package com.example.red_de_libros;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ChatViewHolder> {

    private List<Chat> chats;
    private final OnChatClickListener listener;

    public interface OnChatClickListener {
        void onChatClick(Chat chat);
    }

    public ChatsAdapter(List<Chat> chats, OnChatClickListener listener) {
        this.chats = chats;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Chat chat = chats.get(position);
        holder.bind(chat, listener);
    }

    @Override
    public int getItemCount() {
        return chats != null ? chats.size() : 0;
    }

    public void setChats(List<Chat> chats) {
        this.chats = chats;
        notifyDataSetChanged();
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvNombre;
        private final TextView tvLibro;
        private final TextView tvUltimoMensaje;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvLibro = itemView.findViewById(R.id.tvLibro);
            tvUltimoMensaje = itemView.findViewById(R.id.tvUltimoMensaje);
        }

        public void bind(Chat chat, OnChatClickListener listener) {
            tvNombre.setText(chat.getNombreOtroUsuario() != null ?
                    chat.getNombreOtroUsuario() : "Usuario desconocido");

            tvLibro.setText(chat.getTituloLibro() != null ?
                    chat.getTituloLibro() : "Libro no especificado");

            tvUltimoMensaje.setText(chat.getUltimoMensaje() != null ?
                    chat.getUltimoMensaje() : "No hay mensajes");

            itemView.setOnClickListener(v -> listener.onChatClick(chat));
        }
    }
}