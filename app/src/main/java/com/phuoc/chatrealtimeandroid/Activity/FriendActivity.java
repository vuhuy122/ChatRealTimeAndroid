package com.phuoc.chatrealtimeandroid.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.phuoc.chatrealtimeandroid.Profile;
import com.phuoc.chatrealtimeandroid.R;
import com.phuoc.chatrealtimeandroid.User;
import com.phuoc.chatrealtimeandroid.UserAdapter;

import java.util.ArrayList;

public class FriendActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<User> list;
    private ProgressBar progressBar;
    private UserAdapter userAdapter;
    UserAdapter.onUserClickListener onUserClickListener;
    private SwipeRefreshLayout refresh;
    String myImageUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.rcvFriend);
        refresh = findViewById(R.id.refresh);
        list = new ArrayList<>();

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getUser();
                refresh.setRefreshing(false);
            }
        });

        onUserClickListener = new UserAdapter.onUserClickListener() {
            @Override
            public void onUserClick(int position) {
                startActivity(new Intent(FriendActivity.this, MessageActivity.class)
                .putExtra("username_of_roommate",list.get(position).getUserName())
                .putExtra("email_of_roommate", list.get(position).getEmail())
                .putExtra("image_of_roommate", list.get(position).getImgProfile())
                .putExtra("my_image", myImageUrl));
                ;
            }
        };
        getUser();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_item_profile) {
            Intent i = new Intent(FriendActivity.this, Profile.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    private void getUser() {
        list.clear();
        FirebaseDatabase.getInstance().getReference("user").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    list.add(dataSnapshot.getValue(User.class));
                }
                userAdapter = new UserAdapter(list, FriendActivity.this, onUserClickListener);
                recyclerView.setLayoutManager(new LinearLayoutManager(FriendActivity.this));
                recyclerView.setAdapter(userAdapter);
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

                for (User user : list){
                    if (user.getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
                        myImageUrl = user.getImgProfile();
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}