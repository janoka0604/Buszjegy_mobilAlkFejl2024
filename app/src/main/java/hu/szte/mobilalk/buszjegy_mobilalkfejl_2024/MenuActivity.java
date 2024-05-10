package hu.szte.mobilalk.buszjegy_mobilalkfejl_2024;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    BottomNavigationView bottomNavigationView;
    NotLoggedInFragment notLoggedInFragment = new NotLoggedInFragment();
    LoggedInMenuFragment loggedInMenuFragment = new LoggedInMenuFragment();
    Toolbar toolbar;
    public List<String> transactionBefore = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        toolbar = findViewById(R.id.toolbar);

        defaultFragment();
        setToolbar("Menü");

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnApplyWindowInsetsListener(null);
        bottomNavigationView.setPadding(0,0,0,0);
        bottomNavigationView.setSelectedItemId(R.id.naviMenu);

        bottomNavigationView.setOnItemSelectedListener(menuItem -> {
            Intent intent;
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
        });
    }

    public void redirectToLogin(View view){
        getSupportActionBar().setTitle("Bejelentkezés");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left, R.anim.anim_slide_out_right, R.anim.anim_slide_in_right);
        transaction.replace(R.id.menuFrameLayout, new LoginFragment());
        transaction.addToBackStack(null);
        transaction.commit();

        Log.i("button.test.menu","login");
    }

    public void redirectToRegister(View view){
        getSupportActionBar().setTitle("Regisztrálás");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left, R.anim.anim_slide_out_right, R.anim.anim_slide_in_right);
        transaction.replace(R.id.menuFrameLayout, new RegisterFragment());
        transaction.addToBackStack(null);
        transaction.commit();

        Log.i("button.test.menu","register");
    }



    public void redirectToRegisterFromLogin(View view){
        redirectToRegister(view);
        transactionBefore.add("Bejelentkezés");
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();

            if(!transactionBefore.isEmpty()){
                getSupportActionBar().
                        setTitle(transactionBefore.remove(transactionBefore.size() -1 ));
                return;
            }

            getSupportActionBar().setTitle("Menü");
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
        } else {
            super.onBackPressed();
        }
    }

    private void defaultFragment(){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if(mAuth.getCurrentUser() == null){
            fragmentTransaction.replace(R.id.menuFrameLayout, notLoggedInFragment);
            fragmentTransaction.commit();
        }
        else{
            fragmentTransaction.replace(R.id.menuFrameLayout, loggedInMenuFragment);
            fragmentTransaction.commit();
        }
    }

    public void setToolbar(String s){
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(s);
    }
}