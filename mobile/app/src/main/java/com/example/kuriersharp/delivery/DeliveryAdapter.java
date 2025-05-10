package com.example.kuriersharp.delivery;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kuriersharp.R;
import com.example.kuriersharp.delivery.model.Delivery;
import com.example.kuriersharp.delivery.model.DeliveryStatus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DeliveryAdapter extends RecyclerView.Adapter<DeliveryAdapter.DeliveryViewHolder> {

    private List<Delivery> deliveryList;
    private final OnDeliveryClickListener listener;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    public interface OnDeliveryClickListener {
        void onDeliveryClick(Delivery delivery);
    }

    public DeliveryAdapter(List<Delivery> deliveryList, OnDeliveryClickListener listener) {
        this.deliveryList = deliveryList;
        this.listener = listener;
    }

    public void updateList(List<Delivery> newList) {
        this.deliveryList = new ArrayList<>(newList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DeliveryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_delivery, parent, false);
        return new DeliveryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeliveryViewHolder holder, int position) {
        Delivery delivery = deliveryList.get(position);
        holder.bind(delivery, listener);
    }

    @Override
    public int getItemCount() {
        return deliveryList.size();
    }

    static class DeliveryViewHolder extends RecyclerView.ViewHolder {
        private final TextView trackingNumberTextView;
        private final TextView recipientNameTextView;
        private final TextView recipientAddressTextView;
        private final TextView statusTextView;
        private final TextView dateTextView;

        public DeliveryViewHolder(@NonNull View itemView) {
            super(itemView);
            trackingNumberTextView = itemView.findViewById(R.id.trackingNumberTextView);
            recipientNameTextView = itemView.findViewById(R.id.recipientNameTextView);
            recipientAddressTextView = itemView.findViewById(R.id.recipientAddressTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
        }

        public void bind(final Delivery delivery, final OnDeliveryClickListener listener) {
            trackingNumberTextView.setText(delivery.getTrackingNumber());
            recipientNameTextView.setText(delivery.getRecipientName());
            recipientAddressTextView.setText(delivery.getRecipientAddress());
            statusTextView.setText(delivery.getStatus().getDisplayName());

            // Ustaw kolor statusu
            int statusColor;
            switch (delivery.getStatus()) {
                case DELIVERED:
                    statusColor = R.color.status_delivered;
                    break;
                case IN_TRANSIT:
                    statusColor = R.color.status_in_transit;
                    break;
                case FAILED:
                    statusColor = R.color.status_failed;
                    break;
                default:
                    statusColor = R.color.status_new;
                    break;
            }

            statusTextView.setTextColor(ContextCompat.getColor(itemView.getContext(), statusColor));

            // Format daty
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
            if (delivery.getDeliveryDate() != null) {
                dateTextView.setText(dateFormat.format(delivery.getDeliveryDate()));
            } else {
                dateTextView.setText("--.--.----");
            }

            itemView.setOnClickListener(v -> listener.onDeliveryClick(delivery));
        }
    }
}