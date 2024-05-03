package hu.szte.mobilalk.buszjegy_mobilalkfejl_2024;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.ViewHolder> {
    private ArrayList<Ticket> ticketData;
    private Context mContext;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private  NotificationHelper mNotification;

    public TicketAdapter(Context mContext, ArrayList<Ticket> ticketData){
        this.mContext = mContext;
        this.ticketData = ticketData;
        this.mAuth= FirebaseAuth.getInstance();
        this.mFirestore=FirebaseFirestore.getInstance();
        this.mNotification = new NotificationHelper(mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(mContext)
               .inflate(R.layout.list_ticket, parent, false);

       return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ticket actualData = ticketData.get(position);

        holder.setData(actualData);

    }

    @Override
    public int getItemCount() {
        return ticketData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView ticketPlaceName;
        private TextView ticketDurationTime;
        private TextView ticketDurationText;
        private TextView ticketPrice;
        private Button ticketBuyButton;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ticketPlaceName = itemView.findViewById(R.id.ticketPlaceName);
            ticketDurationTime = itemView.findViewById(R.id.ticketDurationTimeText);
            ticketDurationText = itemView.findViewById(R.id.ticketDurationText);
            ticketPrice = itemView.findViewById(R.id.ticketPrice);
        }

        public void setData(Ticket data){
            ticketPlaceName.setText(data.getCity());
            ticketDurationTime.setText(data.getType() + " jegy " + data.getCity());
            ticketDurationText.setText("Várható lejárati idő: " + calculateDate(data));
            ticketPrice.setText(data.getPrice() + " Ft");
            itemView.findViewById(R.id.ticketBuyButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mAuth!=null){

                       showDialog(data);
                    }
                }
            });

        }
        public void showDialog(Ticket data) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle(R.string.purchase_confirmation_message_title);
            builder.setMessage(R.string.purchase_confirmation_message);
            builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    PurchasedTicket purchased = new PurchasedTicket(mAuth.getCurrentUser().getEmail(), data.getCity() ,data.getType(), calculateDate(data),"");
                    mFirestore.collection("purchasedTickets").add(purchased);
                    mNotification.purchase();
                }
            });
            builder.setNegativeButton(R.string.decline, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
    private String calculateDate(Ticket ticket){
        Calendar calendar = Calendar.getInstance();

        if(ticket.getType().toLowerCase().contains("havi")){
            calendar.add(Calendar.MONTH, 1);
        }
        else if (ticket.getType().toLowerCase().contains("napi")){
            calendar.add(Calendar.DATE, 1);
        }
        else if (ticket.getType().toLowerCase().contains("féléves")){
            calendar.add(Calendar.MONTH, 6);
        }
        else{
            calendar.add(Calendar.YEAR, 1);
        }

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // Calendar.MONTH is zero-based
        int day = calendar.get(Calendar.DATE);

        return year + "/" + month + "/" + day;
    }

}
