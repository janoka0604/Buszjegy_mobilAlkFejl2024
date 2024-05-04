package hu.szte.mobilalk.buszjegy_mobilalkfejl_2024;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class ShowTicketsFragment extends Fragment {

    private FirebaseFirestore mFirestore;

    private RecyclerView mRecyclerView;
    private ArrayList<Ticket> mTickets;
    private TicketAdapter mTicketAdapter;
    private Query queryFromMain;

    public ShowTicketsFragment(Query queryFromMain) {
        this.queryFromMain = queryFromMain;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirestore = FirebaseFirestore.getInstance();
        mTickets = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_show_tickets, container, false);

        mRecyclerView = view.findViewById(R.id.purchaseTicketRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext() ));

        mTicketAdapter = new TicketAdapter(this.getContext(), mTickets);
        mRecyclerView.setAdapter(mTicketAdapter);

        querryData();

        return view;
    }

    private void querryData(){
        Log.i("search.debug", queryFromMain.toString());
        queryFromMain.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        Map<String,Object> ticketData = document.getData();

                        mTickets.add(new Ticket((String)ticketData.get("city"),
                                (String)ticketData.get("type"), (Long) ticketData.get("price")));
                        Log.i("getting.data",ticketData.toString());
                        mTicketAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

    }

}