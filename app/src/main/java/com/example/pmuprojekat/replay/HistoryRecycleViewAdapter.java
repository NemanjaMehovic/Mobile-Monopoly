package com.example.pmuprojekat.replay;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pmuprojekat.MainActivity;
import com.example.pmuprojekat.database.GameEntity;
import com.example.pmuprojekat.database.PlayerEntity;
import com.example.pmuprojekat.databinding.ViewHistoryHolderBinding;
import com.example.pmuprojekat.fragments.historyReplayFragment;

import java.util.ArrayList;
import java.util.List;

public class HistoryRecycleViewAdapter extends RecyclerView.Adapter<HistoryRecycleViewAdapter.HistoryViewHolder> {

    private List<GameEntity> games;
    private MainActivity mainActivity;

    public HistoryRecycleViewAdapter(MainActivity mainActivity) {
        this.games = new ArrayList<>();
        this.mainActivity = mainActivity;
    }

    public void setGames(List<GameEntity> gameEntities) {
        games = gameEntities;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewHistoryHolderBinding viewHistoryHolderBinding = ViewHistoryHolderBinding.inflate(
                layoutInflater,
                parent,
                false);
        return new HistoryViewHolder(viewHistoryHolderBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        GameEntity gameEntity = games.get(position);
        holder.setEntity(gameEntity);
        PlayerEntity playerEntity = MainActivity.repository.getWinner(gameEntity.getId());
        holder.setData(playerEntity);
    }

    @Override
    public int getItemCount() {
        return games.size();
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder {

        private ViewHistoryHolderBinding binding;
        private GameEntity entity;

        public HistoryViewHolder(@NonNull ViewHistoryHolderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.playReplayButton.setOnClickListener(v -> {
                historyReplayFragment fragment = new historyReplayFragment();
                fragment.setGameEntity(entity);
                mainActivity.switchFragment(fragment);
            });
        }

        public void setEntity(GameEntity gameEntity) {
            entity = gameEntity;
        }

        public void setData(PlayerEntity playerEntity) {
            binding.gameWinnerTextView.setText("Winner is: " + playerEntity.getName());
            long mili = entity.getGameLength();
            long s = (mili / 1000) % 60;
            long min = (mili / 1000) / 60;
            binding.gameLengthTextView.setText("Game length: " + String.format("%02d:%02d", min, s));
        }
    }
}
