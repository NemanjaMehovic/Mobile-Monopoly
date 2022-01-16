package com.example.pmuprojekat.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.pmuprojekat.MainActivity;
import com.example.pmuprojekat.databinding.DialogFieldInfoBinding;
import com.example.pmuprojekat.monopoly.Fields.BuyableField;
import com.example.pmuprojekat.monopoly.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class fieldInfoDialog extends DialogFragment {

    private DialogFieldInfoBinding binding;
    private MainActivity mainActivity;

    public fieldInfoDialog(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogFieldInfoBinding.inflate(inflater, container, false);

        List<String> list = new ArrayList<>();
        for (BuyableField f : Game.getInstance().getBuyableFields())
            list.add(f.getName());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(mainActivity, android.R.layout.simple_spinner_item, list);
        binding.infoSpinner.setAdapter(adapter);
        binding.infoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                BuyableField field = Game.getInstance().getBuyableFields().get(position);
                String tmp = "Location: " + field.getName();
                binding.infoLocation.setText(tmp);
                tmp = "Owner: " + (field.getOwner() != null ? field.getOwner().getPlayerName() : "None");
                binding.infoOwner.setText(tmp);
                tmp = "Price: " + field.getPrice();
                binding.infoPrice.setText(tmp);
                tmp = "House price: " + field.getHouseHotelPrice() + System.lineSeparator();
                tmp += "Number of houses: " + (field.getHousesOwned() < 5 ? field.getHousesOwned() : 0) + System.lineSeparator();
                tmp += "Has hotel: " + (field.getHousesOwned() == 5 ? "Yes" : "No");
                binding.infoHouses.setText(tmp);
                StringBuilder rent = new StringBuilder("Rent ");
                List<Integer> rentPrices = field.getRentPrices();
                if (rentPrices.size() > 5)
                    for (int i = 0; i < rentPrices.size(); i++) {
                        if ((i > 0) && i < (rentPrices.size() - 1)) {
                            String format = String.format("%04d", rentPrices.get(i));
                            rent.append(i).append(":").append(format).append((i % 2 == 0) ? System.lineSeparator() : " ");
                        } else if (i == (rentPrices.size() - 1))
                            rent.append("Hotel ").append(rentPrices.get(i));
                        else
                            rent.append(rentPrices.get(i)).append(System.lineSeparator()).append("With houses").append(System.lineSeparator());
                    }
                else
                    for (int i = 0; i < rentPrices.size(); i++) {
                        rent.append(rentPrices.get(i));
                        if (i != (rentPrices.size() - 1))
                            rent.append(", ");
                    }
                binding.infoRent.setText(rent.toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.infoFieldButton.setOnClickListener(v -> {
            getDialog().dismiss();
        });

        return binding.getRoot();
    }
}
