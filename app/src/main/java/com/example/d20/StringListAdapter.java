package com.example.d20;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Adapter for managing list.
 */
public class StringListAdapter extends RecyclerView.Adapter<StringListAdapter.ViewHolder>  {

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private ImageButton deleteButton;

        private ViewHolder(View itemView) {
            super(itemView);

            textView = (TextView) itemView.findViewById(R.id.string_item);
            deleteButton = (ImageButton) itemView.findViewById(R.id.delete_button);
        }
    }

    private ArrayList<String> items;

    public StringListAdapter(ArrayList<String> items) {
        this.items = items;
    }

    /**
     * Replaces current list with new list.
     * @param items Full list of new Items.
     */
    public void loadList(ArrayList<String> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    /**
     * Adds an item to the list and updates it.
     * @param item Item to add to the list.
     */
    public void addItem(String item) {
        int index;
        try {
            index = items.size();
            items.add(item);
            notifyItemInserted(index);
        } catch (NullPointerException e) {
            ArrayList<String> newItems = new ArrayList<>();
            newItems.add(item);
            loadList(newItems);
        }
    }

    @Override
    public StringListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View stringListView = inflater.inflate(R.layout.list_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(stringListView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final StringListAdapter.ViewHolder viewHolder, int position) {
        String text = items.get(position);
        TextView textView = viewHolder.textView;
        textView.setText(text);

        viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                int index = viewHolder.getAdapterPosition();
                items.remove(index);
                notifyItemRemoved(index);
            }
        });
    }

    @Override
    public int getItemCount() {
        try {return items.size();} catch (NullPointerException e) {return 0;}
    }
}
