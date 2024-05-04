package hu.szte.mobilalk.buszjegy_mobilalkfejl_2024;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class SearchFragment extends Fragment {

    private String[] citys;
    private String[] ticketTypes;
    private AutoCompleteTextView autoCompleteCity;
    private AutoCompleteTextView autoCompleteTicketType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        citys = new String[]{"Szeged", "Debrecen", "Budapest", "Győr", "Pécs", "Összes"};
        ticketTypes = new String[]{"Napi", "Kedvezményes havi", "Havi", "Féléves", "Éves", "Összes"};
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        autoCompleteCity = view.findViewById(R.id.autoCompleteCity);
        autoCompleteTicketType = view.findViewById(R.id.autoCompleteTicketType);
        Button searchButton = view.findViewById(R.id.searchButton);

        ArrayAdapter cityAdapter = new ArrayAdapter(getActivity(),
                android.R.layout.simple_list_item_1,citys);
        autoCompleteCity.setAdapter(cityAdapter);
        autoCompleteCity.setThreshold(1);
        autoCompleteCity.setOnTouchListener((v, event) -> {
            if(event.getAction() == MotionEvent.ACTION_DOWN){
                autoCompleteCity.showDropDown();
                return true;
            }
            return false;
        });

        ArrayAdapter typeAdapter = new ArrayAdapter(getActivity(),
                android.R.layout.simple_list_item_1,ticketTypes);
        autoCompleteTicketType.setAdapter(typeAdapter);
        autoCompleteTicketType.setThreshold(1);
        autoCompleteTicketType.setOnTouchListener((v, event) -> {
            if(event.getAction() == MotionEvent.ACTION_DOWN){
                autoCompleteTicketType.showDropDown();
                return true;
            }
            return false;
        });

        searchButton.setOnClickListener(v -> {
            String city = autoCompleteCity.getText().toString().isEmpty()
                    ? "Összes" : autoCompleteCity.getText().toString();
            String type = autoCompleteTicketType.getText().toString().isEmpty()
                    ? "Összes" : autoCompleteTicketType.getText().toString();
            Log.i("search.debug",city+" "+type);
            ((MainActivity)getActivity()).search(city, type);
        });

        return view;
    }
    public void reset(){
        autoCompleteCity.setText("");
        autoCompleteTicketType.setText("");
    }
}