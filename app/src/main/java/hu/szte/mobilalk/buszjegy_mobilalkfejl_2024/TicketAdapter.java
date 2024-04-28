package hu.szte.mobilalk.buszjegy_mobilalkfejl_2024;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.ViewHolder> {
    private ArrayList<Ticket> ticketData;

    public TicketAdapter(ArrayList<Ticket> ticketData){
        this.ticketData = ticketData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext())
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
            ticketDurationText.setText("In construction");
            ticketPrice.setText(data.getPrice() + " Ft");
        }
    }
}
