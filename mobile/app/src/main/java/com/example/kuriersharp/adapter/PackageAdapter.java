package com.example.kuriersharp.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kuriersharp.AddPhotoActivity;
import com.example.kuriersharp.R;
import com.example.kuriersharp.model.Address;
import com.example.kuriersharp.model.Package;

import java.util.List;
import java.util.Map;

public class PackageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private List<Package> packages;
    private Map<Integer, String> statusMap;
    private PackageAdapter adapter;

    public PackageAdapter(List<Package> packages, Map<Integer, String> statusMap) {
        this.packages = packages;
        this.statusMap = statusMap;
        this.adapter = this;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.package_status_header, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.package_item, parent, false);
            return new PackageViewHolder(view, this);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            int status = findStatusForPosition(position);
            int count = countPackagesWithStatus(status);
            ((HeaderViewHolder) holder).statusTextView.setText(statusMap.get(status) + " (" + count + ")");
        } else {
            int itemPosition = getItemPosition(position);
            Package packageItem = packages.get(itemPosition);
            ((PackageViewHolder) holder).trackingNumber.setText(packageItem.trackingNumber);
            ((PackageViewHolder) holder).weight.setText(String.valueOf(packageItem.weightKg) + " kg");
            ((PackageViewHolder) holder).status.setText("Status: " + statusMap.get(packageItem.status));
            ((PackageViewHolder) holder).createdAt.setText("Utworzony: " + packageItem.createdAt);
            ((PackageViewHolder) holder).deliveredAt.setText("Dostarczony: " + packageItem.deliveredAt);
            String address = packageItem.address.street + ", " + packageItem.address.city + ", " +
                    packageItem.address.postalCode + " " + packageItem.address.country;
            ((PackageViewHolder) holder).address.setText(address);
            ((PackageViewHolder) holder).sender.setText("Nadawca: " + packageItem.sender.name + " " + packageItem.sender.surname + ", " + packageItem.sender.phoneNumber);
            ((PackageViewHolder) holder).recipient.setText("Odbiorca: " + packageItem.recipient.name + " " + packageItem.recipient.surname + ", " + packageItem.recipient.phoneNumber);
        }
    }

    @Override
    public int getItemCount() {
        int count = 0;
        int statusCounter = 0;
        if (packages != null) {
            for (int i = 1; i <= 5; i++) {
                boolean found = false;
                for (Package p : packages) {
                    if (p.status == i) {
                        found = true;
                        break;
                    }
                }
                if (found) statusCounter++;
            }
        }
        return packages.size() + statusCounter;
    }

    @Override
    public int getItemViewType(int position) {
        if (packages == null) return TYPE_ITEM;
        int count = 0;
        for (int i = 1; i <= 5; i++) {
            boolean found = false;
            for (Package p : packages) {
                if (p.status == i) {
                    found = true;
                    break;
                }
            }
            if (found && position == count) {
                return TYPE_HEADER;
            }
            if (found) {
                count += countPackagesWithStatus(i) + 1;
            }
        }
        return TYPE_ITEM;
    }

    private int countPackagesWithStatus(int status) {
        int count = 0;
        for (Package p : packages) {
            if (p.status == status) {
                count++;
            }
        }
        return count;
    }

    private int findStatusForPosition(int position) {
        int count = 0;
        for (int i = 1; i <= 5; i++) {
            boolean found = false;
            for (Package p : packages) {
                if (p.status == i) {
                    found = true;
                    break;
                }
            }
            if (found) {
                if (position == count) return i;
                count += countPackagesWithStatus(i) + 1;
            }
        }
        return 0;
    }

    private int getItemPosition(int position) {
        int count = 0;
        for (int i = 1; i <= 5; i++) {
            boolean found = false;
            for (Package p : packages) {
                if (p.status == i) {
                    found = true;
                    break;
                }
            }
            if (found) {
                if (position > count && position <= count + countPackagesWithStatus(i)) {
                    return position - count - 1;
                }
                count += countPackagesWithStatus(i) + 1;
            }
        }
        return 0;
    }

    public static class PackageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView trackingNumber;
        TextView weight;
        TextView status;
        TextView createdAt;
        TextView deliveredAt;
        TextView address;
        TextView sender;
        TextView recipient;
        Button changeStatusButton;
        private PackageAdapter adapter;
        Button addPhotoButton;


        public PackageViewHolder(@NonNull View itemView, PackageAdapter adapter) {
            super(itemView);
            this.adapter = adapter;
            trackingNumber = itemView.findViewById(R.id.trackingNumber);
            weight = itemView.findViewById(R.id.weight);
            status = itemView.findViewById(R.id.status);
            createdAt = itemView.findViewById(R.id.createdAt);
            deliveredAt = itemView.findViewById(R.id.deliveredAt);
            address = itemView.findViewById(R.id.address);
            sender = itemView.findViewById(R.id.sender);
            recipient = itemView.findViewById(R.id.recipient);
            changeStatusButton = itemView.findViewById(R.id.changeStatusButton);
            changeStatusButton.setOnClickListener(this);

            addPhotoButton = itemView.findViewById(R.id.addPhoto);
            addPhotoButton.setOnClickListener(this);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if (v.getId() == R.id.changeStatusButton) {
                //Obsługa przycisku zmiany statusu (bez zmian)
            } else if (v.getId() == R.id.addPhoto) {
                //Obsługa przycisku dodania zdjęcia
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Package packageItem = adapter.packages.get(adapter.getItemPosition(position));
                    adapter.openAddPhotoActivity(v.getContext(), packageItem);
                }
            } else {
                //Obsługa kliknięcia na cały element listy (mapa)
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Package packageItem = adapter.packages.get(adapter.getItemPosition(position));
                    adapter.openMap(v.getContext(), packageItem.address);
                }}}
    }

    public void openMap(Context context, Address address) {
        String addressString = String.format("%s, %s, %s %s", address.street, address.city, address.postalCode, address.country);
        String uri = "geo:0,0?q=" + Uri.encode(addressString);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(mapIntent);
        } else {
            //Obsługa braku Google Maps
            Log.i("Google Maps","Nie ma google maps ");
//            Toast.makeText("Brak google Maps")

        }
    }
    public void openAddPhotoActivity(Context context, Package packageItem) {
        Intent intent = new Intent(context, AddPhotoActivity.class);
        intent.putExtra("package", packageItem);
        context.startActivity(intent);
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView statusTextView;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            statusTextView = itemView.findViewById(R.id.status_header);
        }
    }
}