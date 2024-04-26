package hu.szte.mobilalk.buszjegy_mobilalkfejl_2024;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment extends Fragment {
    private FirebaseAuth mAuth;
    EditText loginPasswordEditText;
    EditText loginEmailEditText;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        loginPasswordEditText = view.findViewById(R.id.loginPasswordEditText);
        loginEmailEditText = view.findViewById(R.id.loginEmailEditText);

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button loginButton = view.findViewById(R.id.loginOnLoginButton);
        loginButton.setOnClickListener(v -> {
            String email = loginEmailEditText.getText().toString();
            String password = loginPasswordEditText.getText().toString();
            logIn(email, password);
        });
    }

    private void logIn(String email, String password){
        if(!validateDatas(email, password)) return;

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.i("login.test","Succesful login");
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                    transaction.replace(R.id.menuFrameLayout, new LoggedInMenuFragment());
                    transaction.commit();
                }
                else{
                    Log.e("login.test",task.getException().getMessage());
                }
            }
        });

    }

    private boolean validateEmail(String email){
        return email.matches("^[-0-9a-zA-Z.+_]+@[-0-9a-zA-Z.+_]+\\.[a-zA-Z]{2,4}");
    }

    private boolean validateDatas(String email, String password){
        if(email.isEmpty() || password.isEmpty()){
            Log.e("login.test","Empty data");
            return false;
        }
        if(!validateEmail(email)){
            Log.e("login.test","Wrong email");
            return false;
        }
        if(password.length() < 6){
            Log.e("login.test","Short pw");
            return false;
        }
        return true;
    }
}