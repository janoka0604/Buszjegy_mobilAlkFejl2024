package hu.szte.mobilalk.buszjegy_mobilalkfejl_2024;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;

public class LoggedInMenuFragment extends Fragment {
    private FirebaseAuth mAuth;
    private Button logOutButton;
    private CardView billingAddress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_logged_in_menu, container, false);

        logOutButton = view.findViewById(R.id.logutButton);
        TextView email = view.findViewById(R.id.cardProfileEmailText);
        email.setText(mAuth.getCurrentUser().getEmail());
        billingAddress = view.findViewById(R.id.billingAdressCard);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        logOutButton.setOnClickListener(v -> logOut());

        billingAddress.setOnClickListener(v -> {
            FragmentTransaction transaction = getActivity()
                    .getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left, R.anim.anim_slide_out_right, R.anim.anim_slide_in_right);
            transaction.replace(R.id.menuFrameLayout, new BillingAddressFragment());
            transaction.addToBackStack(null);
            transaction.commit();
            ( (MenuActivity) getActivity()).setToolbar("Számlázási cím");
            ( (MenuActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ( (MenuActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        });

    }

    private void logOut(){
        mAuth.signOut();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        transaction.replace(R.id.menuFrameLayout, new NotLoggedInFragment());
        transaction.commit();
    }
}