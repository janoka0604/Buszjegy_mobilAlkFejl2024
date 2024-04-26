package hu.szte.mobilalk.buszjegy_mobilalkfejl_2024;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class TicketActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;

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
    }
}