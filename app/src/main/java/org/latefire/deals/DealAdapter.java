package org.latefire.deals;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.latefire.deals.databinding.ItemDealBinding;
import org.latefire.deals.models.Deal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dw on 19/03/17.
 */
public class DealAdapter extends RecyclerView.Adapter<DealAdapter.ViewHolder> {

    private static final String LOG_TAG = DealAdapter.class.getSimpleName();

    ArrayList<Deal> mData;

    public DealAdapter(ArrayList<Deal> data) {
        mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemDealBinding binding = ItemDealBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Deal deal = mData.get(position);
        ItemDealBinding binding = holder.getBinding();
        binding.setDeal(deal);
        binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    // Remove all items from the adapter
    public void clear() {
        int oldSize = mData.size();
        mData.clear();
        notifyItemRangeRemoved(0, oldSize);
    }

    // Append a list of items to the end of the adapter
    public void append(List<Deal> tweets) {
        int oldSize = mData.size();
        mData.addAll(tweets);
        notifyItemRangeInserted(oldSize, tweets.size());
    }

    // ViewHolder
    class ViewHolder extends RecyclerView.ViewHolder {
        private ItemDealBinding b;
        ViewHolder(ItemDealBinding binding) {
            super(binding.getRoot());
            b = binding;
        }
        ItemDealBinding getBinding() {
            return b;
        }
    }
}
