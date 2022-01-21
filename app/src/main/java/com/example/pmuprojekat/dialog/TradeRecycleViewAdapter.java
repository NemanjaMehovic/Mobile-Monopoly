package com.example.pmuprojekat.dialog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pmuprojekat.databinding.ViewTradeHolderBinding;
import com.example.pmuprojekat.monopoly.Fields.BuyableField;

import java.util.List;

public class TradeRecycleViewAdapter extends RecyclerView.Adapter<TradeRecycleViewAdapter.TradeViewHolder> {

    private List<BuyableField> allFields;
    private List<BuyableField> tradeFields;

    public TradeRecycleViewAdapter(List<BuyableField> allFields) {
        this.allFields = allFields;
    }

    public List<BuyableField> getTradeFields() {
        return tradeFields;
    }

    public void setTradeFields(List<BuyableField> tradeFields) {
        this.tradeFields = tradeFields;
    }

    public void addTrade(BuyableField field) {
        tradeFields.add(field);
    }

    public void removeTrade(BuyableField field) {
        tradeFields.remove(field);
    }

    @NonNull
    @Override
    public TradeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewTradeHolderBinding viewHolderRouteBinding = ViewTradeHolderBinding.inflate(
                layoutInflater,
                parent,
                false);
        return new TradeViewHolder(viewHolderRouteBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull TradeViewHolder holder, int position) {
        holder.setField(allFields.get(position));
        String tmp = holder.field.getName() + " - " + holder.field.getType();
        holder.binding.checkboxField.setText(tmp);
    }

    @Override
    public int getItemCount() {
        return allFields.size();
    }

    public class TradeViewHolder extends RecyclerView.ViewHolder {

        private ViewTradeHolderBinding binding;
        private BuyableField field;

        public void setField(BuyableField field) {
            this.field = field;
        }

        public TradeViewHolder(@NonNull ViewTradeHolderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.checkboxField.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked)
                    addTrade(field);
                else
                    removeTrade(field);
            });
        }
    }
}
