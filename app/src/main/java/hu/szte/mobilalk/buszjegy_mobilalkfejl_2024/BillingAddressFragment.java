package hu.szte.mobilalk.buszjegy_mobilalkfejl_2024;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class BillingAddressFragment extends Fragment {
private EditText forName;
private EditText surName;
private EditText postalCode;
private EditText city;
private EditText street;
private EditText houseNumber;
private Button modify;
private FirebaseAuth mAuth;
private FirebaseFirestore mFirestore;
private String documentID;
private User beforeUpdateUser;
private BillingAddress beforeUpdateAddress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_billing_address, container, false);

        forName = view.findViewById(R.id.billingAdressForName);
        surName = view.findViewById(R.id.billingAdressSurName);
        postalCode = view.findViewById(R.id.billingAdressPostalCode);
        city = view.findViewById(R.id.billingAdressCity);
        street = view.findViewById(R.id.billingAdressStreet);
        houseNumber = view.findViewById(R.id.billingAdressHouseNumber);
        modify = view.findViewById(R.id.billingAdressModifyButton);

        querryData();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        modify.setOnClickListener(v -> {
            if(checkRequiredDatas()){
                updateData();
            }
            else{
                Toast errorToast = new Toast(getContext());
                errorToast.setText("Nincs minden adat kitöltve!");
                errorToast.setDuration(Toast.LENGTH_SHORT);
                errorToast.show();
            }
        });
    }

    public void querryData(){
        if(mAuth.getCurrentUser()!=null){
            mFirestore.collection("user")
                    .whereEqualTo("email",mAuth.getCurrentUser().getEmail())
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                           for(QueryDocumentSnapshot document : task.getResult()){
                               documentID = document.getId();
                               beforeUpdateUser = document.toObject(User.class);
                               beforeUpdateAddress = beforeUpdateUser.getBillingAddress();
                               Log.i("address.error",beforeUpdateAddress.toString());

                               forName.setText(beforeUpdateUser.getForeName());
                               surName.setText(beforeUpdateUser.getSurName());
                               if(beforeUpdateAddress.getCity() != null){
                                   postalCode.setText(Integer.toString(beforeUpdateAddress.getPostalCode()));
                                   city.setText(beforeUpdateAddress.getCity());
                                   street.setText(beforeUpdateAddress.getStreet());
                                   houseNumber.setText(Integer.toString(beforeUpdateAddress.getHouseNumber()));
                               }
                           }
                        }
                    });
        }
    }

    private boolean checkRequiredDatas(){
        return !forName.getText().toString().isEmpty() && !surName.getText().toString().isEmpty()
                && !postalCode.getText().toString().isEmpty()
                && !city.getText().toString().isEmpty()
                && !street.getText().toString().isEmpty()
                && !houseNumber.getText().toString().isEmpty();
    }

    public void updateData(){
        if(mAuth.getCurrentUser()!=null){
            DocumentReference document = mFirestore.collection("user")
                    .document(documentID);

            String fName = forName.getText().toString();
            String sName = surName.getText().toString();
            int pCode = Integer.parseInt(postalCode.getText().toString());
            String sCity = city.getText().toString();
            String sStreet = street.getText().toString();
            int hNumber = Integer.parseInt(houseNumber.getText().toString());

            if(!fName.equals(beforeUpdateUser.getForeName())){
                document.update("foreName", fName);
            }
            if(!sName.equals(beforeUpdateUser.getSurName())){
                document.update("surName", sName);
            }
            if(pCode != beforeUpdateAddress.getPostalCode()){
                document.update("billingAddress.postalCode", pCode);
            }
            if(!sCity.equals(beforeUpdateAddress.getCity())){
                document.update("billingAddress.city", sCity);
            }
            if(!sStreet.equals(beforeUpdateAddress.getStreet())){
                document.update("billingAddress.street", sStreet);
            }
            if(hNumber != beforeUpdateAddress.getHouseNumber()){
                document.update("billingAddress.houseNumber", hNumber);
            }

        }
    }

}