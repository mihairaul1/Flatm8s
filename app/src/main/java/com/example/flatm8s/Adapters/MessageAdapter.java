package com.example.flatm8s.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.flatm8s.AllMethods;
import com.example.flatm8s.Message;
import com.example.flatm8s.R;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageAdaptorViewHolder> {

    Context context;
    List<Message> messages;
    DatabaseReference databaseReference;

    public MessageAdapter(Context context, List<Message> messages, DatabaseReference databaseReference){

        this.context = context;
        this.databaseReference = databaseReference;
        this.messages = messages;

    }

    @NonNull
    @Override
    public MessageAdaptorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_message, parent, false);
        return new MessageAdaptorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdaptorViewHolder messageAdaptorViewHolder, int position) {
        Message message = messages.get(position);

        if(message.getName().equals(AllMethods.name)){
            messageAdaptorViewHolder.title.setText("You: "+message.getMessage());
            messageAdaptorViewHolder.title.setGravity(Gravity.START);
            messageAdaptorViewHolder.linearLayout.setBackgroundColor(Color.parseColor("#63B8FF"));
        }else{
            messageAdaptorViewHolder.title.setText(message.getName()+ ": "+ message.getMessage());
            messageAdaptorViewHolder.buttonDelete.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class MessageAdaptorViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        ImageButton buttonDelete;
        LinearLayout linearLayout;

        public MessageAdaptorViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvTitle);
            buttonDelete = itemView.findViewById(R.id.btnDeleteMessage);
            linearLayout = itemView.findViewById(R.id.l1Message);

            buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    databaseReference.child(messages.get(getAdapterPosition()).getKey()).removeValue();

                }
            });
        }
    }
}
