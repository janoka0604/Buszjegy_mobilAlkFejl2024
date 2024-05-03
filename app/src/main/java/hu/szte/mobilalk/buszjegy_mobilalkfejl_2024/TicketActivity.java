package hu.szte.mobilalk.buszjegy_mobilalkfejl_2024;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class TicketActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private PurchasedTicketAdapter mPurchasedTicketAdapter;

    private ArrayList<PurchasedTicket> mPurchasedTickets;

    private CollectionReference mQuerydData;

    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ticket);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        mPurchasedTickets = new ArrayList<>();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Jegyeim");

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnApplyWindowInsetsListener(null);
        bottomNavigationView.setPadding(0,0,0,0);
        bottomNavigationView.setSelectedItemId(R.id.naviTicket);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Intent intent = new Intent();
                if(menuItem.getItemId() == R.id.naviMenu){
                    intent = new Intent(getApplicationContext(), MenuActivity.class);
                    Log.i("bottomNavigationView.testing.v1", "onNavigationItemSelected: Menu");
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    return true;
                } else if (menuItem.getItemId() == R.id.naviTicket) {
                    intent = new Intent(getApplicationContext(), TicketActivity.class);
                    Log.i("bottomNavigationView.testing.v1", "onNavigationItemSelected: Ticket");
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    return true;
                } else if (menuItem.getItemId() == R.id.naviPurchase) {
                    intent = new Intent(getApplicationContext(), MainActivity.class);
                    Log.i("bottomNavigationView.testing.v1", "onNavigationItemSelected: Purchase");
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    return true;
                }
                return false;
            }
        });

        createTicketView();
        querryData();

    }

    private void createTicketView(){
        mRecyclerView = findViewById(R.id.purchaseTicketRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mPurchasedTicketAdapter = new PurchasedTicketAdapter(this, mPurchasedTickets);
        mRecyclerView.setAdapter(mPurchasedTicketAdapter);
    }

    private void querryData() {
        if(mAuth.getCurrentUser()!=null){
            mFirestore
                    .collection("purchasedTickets")
                    .whereEqualTo("userEmail", mAuth.getCurrentUser().getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        for(QueryDocumentSnapshot document : task.getResult()){
                            Log.i("getting.id.test", document.getId());
                            Map<String,Object> ticketData = document.getData();

                            mPurchasedTickets.add(new PurchasedTicket(
                                    (String)ticketData.get("userEmail"),
                                    (String)ticketData.get("city"),
                                    (String)ticketData.get("type"),
                                    (String)ticketData.get("validDate"),
                                    document.getId()));
                            Log.i("getting.data",ticketData.toString());
                            mPurchasedTicketAdapter.notifyDataSetChanged();
                        }
                    }
                }
            });
        }
    }
}