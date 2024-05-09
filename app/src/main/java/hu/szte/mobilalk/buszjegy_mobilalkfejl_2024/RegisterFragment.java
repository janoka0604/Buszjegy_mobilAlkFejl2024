package hu.szte.mobilalk.buszjegy_mobilalkfejl_2024;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;

public class RegisterFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseFirestore mDb;
    private EditText foreNameEditText;
    private EditText surNameEditText;
    private EditText emailEditText;
    private EditText birthDateDatePicker;
    private EditText passwordEditText;
    private EditText passwordConfirmEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        mDb = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        foreNameEditText = view.findViewById(R.id.foreNameEditText);
        surNameEditText = view.findViewById(R.id.surNameEditText);
        emailEditText = view.findViewById(R.id.registerEmailEditText);
        birthDateDatePicker = view.findViewById(R.id.registerDateText);
        passwordEditText = view.findViewById(R.id.registerPasswordEditText);
        passwordConfirmEditText = view.findViewById(R.id.registerPasswordConfirmEditText);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.registerPageRegisterButton).setOnClickListener(v -> {
            String foreName = foreNameEditText.getText().toString();
            String surName = surNameEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String birthDate = birthDateDatePicker.getText().toString();
            String password = passwordEditText.getText().toString();
            String confirmPassword = passwordConfirmEditText.getText().toString();


            registerUser(foreName, surName, email, birthDate, password, confirmPassword);
        });

        view.findViewById(R.id.registerDateText).setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                openDateDialog();
            }
            return true;
        });
    }

    private void openDateDialog(){
        Date dateOfToday = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateOfToday);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);

        DatePickerDialog dialog = new DatePickerDialog(getContext(),
                (view, year1, month1, dayOfMonth) -> birthDateDatePicker
                        .setText(year1 +"/"+ (month1+1) +
                "/"+ dayOfMonth), year, month, day);

        dialog.show();
    }

    private boolean isEmailvalid(String s) {
        return s.matches("^[-0-9a-zA-Z.+_]+@[-0-9a-zA-Z.+_]+\\.[a-zA-Z]{2,4}");
    }

    private boolean validateDatas(String foreName, String surName, String email, String bthD,
                                  String pw, String pwConfirm){
        Toast errorToast = new Toast(getContext());
        errorToast.setDuration(Toast.LENGTH_SHORT);
        if(foreName.isEmpty() || surName.isEmpty() || email.isEmpty() || bthD.isEmpty()
                || pw.isEmpty() || pwConfirm.isEmpty()){
            errorToast.setText("Nincs minden adat kitöltve!");
            errorToast.show();
            return false;
        }

        if(!isEmailvalid(email)){
            errorToast.setText("Hibás email formátum!");
            errorToast.show();
            return false;
        }

        if(!pw.equals(pwConfirm)){
            errorToast.setText("A két jelszó nem egyezik meg!");
            errorToast.show();
            return false;
        }

        if(pw.length() < 6){
            errorToast.setText("A jelszó legyen hosszabb, mint 6 karakter!");
            errorToast.show();
            return false;
        }

        return true;
    }

    private void registerUser(String foreName, String surName, String email, String bthD,
                              String pw, String pwConfirm){
        if(!validateDatas(foreName, surName, email, bthD, pw, pwConfirm)) return;

        mAuth.createUserWithEmailAndPassword(email, pw)
                .addOnCompleteListener(task -> {
                    Toast completeToast = new Toast(getContext());
                    completeToast.setDuration(Toast.LENGTH_SHORT);
                    if(task.isSuccessful()){
                        completeToast.setText("Sikeres regisztráció!");
                        completeToast.show();
                        Log.i("register.page.succes","Sikeres regisztráció "+mAuth.getCurrentUser().getEmail());
                        registerUserDatas(foreName, surName, email, bthD);
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                        transaction.replace(R.id.menuFrameLayout, new LoggedInMenuFragment());
                        transaction.commit();
                    }
                    else{
                        Log.e("register.page.succes",task.getException().getMessage());
                        completeToast.setText(task.getException().getMessage());
                        completeToast.show();
                    }
                });
    }

    private void registerUserDatas(String foreName, String surName, String email, String bthD){
        User user = new User(foreName, surName, email,bthD);

        mDb.collection("user").add(user);
    }
}