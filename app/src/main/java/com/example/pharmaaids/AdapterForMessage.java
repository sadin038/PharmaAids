package com.example.pharmaaids;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class AdapterForMessage extends RecyclerView.Adapter<AdapterForMessage.MyViewHolder> {

    private Context context;
    private List<Chat> chats;
    public static final int RIGHT = 0;
    public static final int LEFT = 1;
    FirebaseUser firebaseUser;

    public AdapterForMessage(Context context, List<Chat> chats) {
        this.context = context;
        this.chats = chats;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view;
       if(viewType==RIGHT)
       {
           view = layoutInflater.inflate(R.layout.fragment_chat_item_right,parent,false);
       }
       else
       {
            view = layoutInflater.inflate(R.layout.fragment_chat_item_left,parent,false);
       }
       return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Chat chat = chats.get(position);
        holder.textView.setText(chat.getMessage());

    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.show_msgId);
        }
    }

    @Override
    public int getItemViewType(int position) {
       firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
       if(chats.get(position).getSender().equals(firebaseUser.getUid()))
       {
           return RIGHT;
       }
       else
       {
           return LEFT;
       }
    }
}
