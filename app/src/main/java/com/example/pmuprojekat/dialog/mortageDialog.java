package com.example.pmuprojekat.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.pmuprojekat.MainActivity;
import com.example.pmuprojekat.databinding.DialogFieldInfoBinding;
import com.example.pmuprojekat.databinding.DialogMortageBinding;
import com.example.pmuprojekat.fragments.gameFragment;
import com.example.pmuprojekat.monopoly.Fields.BuyableField;
import com.example.pmuprojekat.monopoly.Game;

import java.util.ArrayList;
import java.util.List;

public class mortageDialog extends DialogFragment {

    public enum dialogType {
        MORTAGE,
        MORTAGE_PAY,
        HOUSE_BUY,
        HOUSE_SELL
    }

    private List<BuyableField> list;
    private dialogType type;
    private DialogMortageBinding binding;
    private MainActivity mainActivity;
    private BuyableField selected;

    public mortageDialog(List<BuyableField> list, dialogType type, MainActivity mainActivity) {
        this.list = list;
        this.type = type;
        this.mainActivity = mainActivity;
        this.selected = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogMortageBinding.inflate(inflater, container, false);

        List<String> nameList = new ArrayList<>();
        for (BuyableField f : list)
            nameList.add(f.getName() + " - " + f.getType());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(mainActivity, android.R.layout.simple_spinner_item, nameList);
        binding.fieldSpinnerMortage.setAdapter(adapter);
        binding.fieldSpinnerMortage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected = list.get(position);
                String tmp = "";
                switch (type)
                {
                    case MORTAGE:
                        tmp = "You would receive " + selected.getPrice()/2;
                        binding.fieldMortageText.setText(tmp);
                        break;
                    case MORTAGE_PAY:
                        tmp = "You would have to pay " + (int)(selected.getPrice()/2 * 1.1);
                        binding.fieldMortageText.setText(tmp);
                        break;
                    case HOUSE_BUY:
                        tmp = "You would have to pay  " + selected.getHouseHotelPrice() + System.lineSeparator();
                        tmp += "Currently have " + selected.getHousesOwned() + " houses";
                        binding.fieldMortageText.setText(tmp);
                        break;
                    case HOUSE_SELL:
                        tmp = "You would receive " + selected.getHouseHotelPrice()/2 + System.lineSeparator();
                        if(selected.getHousesOwned() == 5)
                            tmp += "Currently have 1 hotel";
                        else
                            tmp +=  "Currently have " + selected.getHousesOwned() + " houses";
                        binding.fieldMortageText.setText(tmp);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        binding.cancelButtonMortage.setOnClickListener(v -> {
            getDialog().dismiss();
        });

        binding.acceptButtonMortage.setOnClickListener(v -> {
            if(selected == null)
                return;
            switch (type)
            {
                case MORTAGE:
                    Game.getInstance().mortageField(selected);
                    getDialog().dismiss();
                    break;
                case MORTAGE_PAY:
                    if(selected.getOwner().getCurrMoney() > (int)(selected.getMortgage()*1.1))
                    {
                        Game.getInstance().payOffMortage(selected);
                        getDialog().dismiss();
                    }
                    else
                        Toast.makeText(mainActivity, "Not enough money to pay off mortage", Toast.LENGTH_SHORT).show();
                    break;
                case HOUSE_BUY:
                    if (selected.getHouseHotelPrice() > selected.getOwner().getCurrMoney()) {
                        Toast.makeText(mainActivity, "Not enough money to buy a house/hotel", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (selected.getHousesOwned() == 4 && Game.getInstance().getNumOfHotelsLeft() == 0) {
                        Toast.makeText(mainActivity, "No hotels left", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(selected.getHousesOwned() < 4 && Game.getInstance().getNumOfHousesLeft() == 0){
                        Toast.makeText(mainActivity, "No houses left", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Game.getInstance().buyHouse(selected);
                    getDialog().dismiss();
                    break;
                case HOUSE_SELL:
                    if (selected.getHousesOwned() == 5 && Game.getInstance().getNumOfHousesLeft() < 4) {
                        Toast.makeText(mainActivity, "Can't sell hotel no houses left to replace it", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Game.getInstance().sellHouse(selected);
                    getDialog().dismiss();
                    break;
            }
        });

        return binding.getRoot();
    }
}
