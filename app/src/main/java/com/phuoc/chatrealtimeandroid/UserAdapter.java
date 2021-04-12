package com.phuoc.chatrealtimeandroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserHolder> {

    private ArrayList<User> listUser;
    private Context context;
    private onUserClickListener onUserClickListener;

    public UserAdapter(ArrayList<User> listUser, Context context, UserAdapter.onUserClickListener onUserClickListener) {
        this.listUser = listUser;
        this.context = context;
        this.onUserClickListener = onUserClickListener;
    }


    public interface onUserClickListener {
        void onUserClick(int position);
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_holder, parent, false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        holder.txtUsername.setText(listUser.get(position).getUserName());
        Glide.with(context).load(listUser.get(position).getImgProfile()).error(R.drawable.ic_account).placeholder(R.drawable.ic_account).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }

    class UserHolder extends RecyclerView.ViewHolder {
        TextView txtUsername;
        ImageView imageView;
        public UserHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onUserClickListener.onUserClick(getAdapterPosition());
                }
            });
            txtUsername = itemView.findViewById(R.id.txtUserName);
            imageView = itemView.findViewById(R.id.img_pro);
        }
    }
}
