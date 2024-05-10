package hu.szte.mobilalk.buszjegy_mobilalkfejl_2024;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity {
    private FirebaseFirestore mFirestore;
    private final int PERMISSION = 1;

    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;
    SearchFragment search = new SearchFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mFirestore = FirebaseFirestore.getInstance();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Keresés");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.ticketFragmentContainer, search);
        fragmentTransaction.commit();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnApplyWindowInsetsListener(null);
        bottomNavigationView.setPadding(0, 0, 0, 0);
        bottomNavigationView.setSelectedItemId(R.id.naviPurchase);

        bottomNavigationView.setOnItemSelectedListener(menuItem -> {
            Intent intent;
            if (menuItem.getItemId() == R.id.naviMenu) {
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
        });

    }
    @Override
    protected void onStart() {
        super.onStart();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            Log.i("test.noti","start");
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                    PERMISSION);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void search(String city, String type){
        Query query = mFirestore.collection("tickets");

        if(!city.equalsIgnoreCase("összes")){
            Log.i("search.debug",city);
            query = query.whereEqualTo("city",city);
        }
        if(!type.equalsIgnoreCase("összes")){
            Log.i("search.debug",type);
            query = query.whereEqualTo("type",type);
        }

        Log.i("search.debug", query.toString());
        switchToBuyFragment(query);
    }

    private void switchToBuyFragment(Query query){
        ShowTicketsFragment fragment = new ShowTicketsFragment(query);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.anim_slide_in_left,
                R.anim.anim_slide_out_left,
                R.anim.anim_slide_out_right, R.anim.anim_slide_in_right);
        fragmentTransaction.replace(R.id.ticketFragmentContainer, fragment);
        fragmentTransaction.addToBackStack(null); // Add fragment transaction to back stack
        fragmentTransaction.commit();

        getSupportActionBar().setTitle("Vásárlás");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        search.reset();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            Log.i("back.debug","pressed");

            getSupportActionBar().setTitle("Keresés");
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
        } else {
            Log.i("back.debug","super");
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}