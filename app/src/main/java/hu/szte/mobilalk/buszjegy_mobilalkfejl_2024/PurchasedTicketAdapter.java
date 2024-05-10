package hu.szte.mobilalk.buszjegy_mobilalkfejl_2024;

import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class PurchasedTicketAdapter extends RecyclerView.Adapter<PurchasedTicketAdapter.ViewHolder>{

    private ArrayList<PurchasedTicket> ticketData;
    private Context mContext;
    private FirebaseFirestore mFirestore;

    public PurchasedTicketAdapter(Context mContext, ArrayList<PurchasedTicket> ticketData){
        this.mContext = mContext;
        this.ticketData = ticketData;
        this.mFirestore = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public PurchasedTicketAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.list_purchased_ticket, parent, false);

        return new PurchasedTicketAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PurchasedTicketAdapter.ViewHolder holder, int position) {
        PurchasedTicket actualData = ticketData.get(position);

        holder.setData(actualData);
    }

    @Override
    public int getItemCount() {
        return ticketData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView ticketPlaceName;
        private TextView ticketValidationLength;
        private TextView ticketDurationTimeText;
        private TextView ticketValid;
        private ImageButton deletePurchasedTicket;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ticketPlaceName = itemView.findViewById(R.id.ticketPlaceName);
            ticketValidationLength = itemView.findViewById(R.id.ticketValidationLengthText);
            ticketDurationTimeText = itemView.findViewById(R.id.ticketDurationTimeText);
            ticketValid = itemView.findViewById(R.id.ticketValid);
            deletePurchasedTicket = itemView.findViewById(R.id.deletePurchasedTicket);
        }

        public void setData(PurchasedTicket data){
            ticketPlaceName.setText(data.getCity());
            ticketDurationTimeText.setText(data.getType() + " jegy " + data.getCity());
            ticketValidationLength.setText("Érvényesség: " + data.getValidDate());
            ticketValid.setText("Érvényes");

            deletePurchasedTicket.setOnClickListener(v -> showDialog(data._getDocumentID(), getAdapterPosition()));
        }

        public void showDialog(String documentId, final int position) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle(R.string.purchase_ticket_delete_message_title);
            builder.setMessage(R.string.purchase_ticket_delete_message);
            builder.setPositiveButton(R.string.confirm, (dialog, id) -> {
                mFirestore.collection("purchasedTickets").document(documentId).delete();
                ticketData.remove(position);
                notifyItemRemoved(position);
                Toast toast = Toast.makeText(mContext, "Sikeres törlés!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();

            });
            builder.setNegativeButton(R.string.decline, (dialog, id) -> {
                Toast toast = Toast.makeText(mContext, "Törlés félbeszakítva!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}