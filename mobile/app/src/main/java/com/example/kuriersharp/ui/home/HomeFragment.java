package com.example.kuriersharp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kuriersharp.R;
import com.example.kuriersharp.adapter.PackageAdapter;
import com.example.kuriersharp.databinding.FragmentHomeBinding;
import com.example.kuriersharp.model.Address;
import com.example.kuriersharp.model.Person;
import com.example.kuriersharp.model.Package;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

//public class HomeFragment extends Fragment {
//
//    private FragmentHomeBinding binding;
//    private HomeViewModel homeViewModel; // Dodaj deklarację HomeViewModel
//
//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             ViewGroup container, Bundle savedInstanceState) {
//
//        binding = FragmentHomeBinding.inflate(inflater, container, false);
//        View root = binding.getRoot();
//
//        final TextView textView = binding.textHome;
//
//        // Pobierz HomeViewModel
//        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
//
//        String localizedText = getString(R.string.home_fragment_text);
//        homeViewModel.setText(localizedText);
//
//        // Obserwuj LiveData z ViewModel
//        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
//
//        return root;
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        binding = null;
//    }
//}
public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private RecyclerView recyclerView;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false); // Użyj inflatera do inflate layoutu
        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Map<Integer, String> statusMap = new HashMap<>();
        statusMap.put(1, "Utworzony");
        statusMap.put(2, "Odebrany");
        statusMap.put(3, "W transporcie");
        statusMap.put(4, "Dostarczony");
        statusMap.put(5, "Zwrócony");

        // Przykładowe dane - ZASTĄP TYMI Z BACKENDU
        List<Package> packages = createExamplePackages();

        PackageAdapter adapter = new PackageAdapter(packages, statusMap);
        recyclerView.setAdapter(adapter);

        return root;
    }



    private List<Package> createExamplePackages() {
        List<Package> packages = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        Date createdAt, deliveredAt;
        try {
            // Paczka 1 - Utworzony
            createdAt = format.parse("2023-10-26T10:00:00Z");
            deliveredAt = null; // Nie dostarczona
            packages.add(new Package(1, "1Z999AA10123456785", 2.5, 1, createdAt, deliveredAt,
                    new Address(1, "ul. Kwiatowa 12", "Warszawa", "Mazowieckie", "02-001", "Polska"), 1,
                    new Person(1, "Jan", "Kowalski", "+48123456789"), 2,
                    new Person(2, "Anna", "Nowak", "+48987654321")));


            // Paczka 2 - Odebrany
            createdAt = format.parse("2023-10-27T10:00:00Z");
            deliveredAt = null;
            packages.add(new Package(2, "1Z999BB10123456786", 1.0, 2, createdAt, deliveredAt,
                    new Address(2, "ul. Lipowa 20", "Kraków", "Małopolskie", "30-000", "Polska"), 3,
                    new Person(3, "Adam", "Nowak", "+48500111222"), 4,
                    new Person(4, "Barbara", "Kowalska", "+48600333444")));

            // Paczka 3 - W transporcie
            createdAt = format.parse("2023-10-28T10:00:00Z");
            deliveredAt = null;
            packages.add(new Package(3, "1Z999CC10123456787", 0.5, 3, createdAt, deliveredAt,
                    new Address(3, "ul. Dębowa 5", "Gdańsk", "Pomorskie", "80-000", "Polska"), 5,
                    new Person(5, "Ewa", "Wiśniewska", "+48700555666"), 6,
                    new Person(6, "Cezary", "Wiśniewski", "+48800777888")));

            // Paczka 4 - Dostarczony
            createdAt = format.parse("2023-10-29T10:00:00Z");
            deliveredAt = format.parse("2023-10-30T12:00:00Z");
            packages.add(new Package(4, "1Z999DD10123456788", 3.2, 4, createdAt, deliveredAt,
                    new Address(4, "ul. Sosnowa 10", "Wrocław", "Dolnośląskie", "50-000", "Polska"), 7,
                    new Person(7, "Filip", "Lewandowski", "+48900999000"), 8,
                    new Person(8, "Zofia", "Lewandowska", "+48100111222")));

            // Paczka 5 - Zwrócony
            createdAt = format.parse("2023-10-30T10:00:00Z");
            deliveredAt = null;
            packages.add(new Package(5, "1Z999EE10123456789", 1.8, 5, createdAt, deliveredAt,
                    new Address(5, "ul. Brzozowa 3", "Poznań", "Wielkopolskie", "60-000", "Polska"), 9,
                    new Person(9, "Grzegorz", "Zieliński", "+48200333444"), 10,
                    new Person(10, "Maria", "Zielińska", "+48300555666")));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return packages;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}