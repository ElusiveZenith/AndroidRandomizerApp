package com.example.d20;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

/**
 * Fragment used for creating dice of words.
 */
public class StringListFragment extends Fragment {

    ArrayList<String> items;
    static StringListAdapter adapter;
    RecyclerView list;

    public StringListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_string_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        items = new ArrayList<>();
        list = (RecyclerView) view.findViewById(R.id.list);
        adapter = new StringListAdapter(items);
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));

        //Setting up the FloatingActionButton with its onClick method.
        FloatingActionButton floatingActionButton
                = (FloatingActionButton) getActivity().findViewById(R.id.add_list_item);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.addItem("");
            }
        });
    }

    /**
     * Saves list when fragment is closed.
     */
    @Override
    public void onStop(){
        super.onStop();
        SharedPreferences pref = getActivity().getSharedPreferences("stringListFragPrefId",
                                                                    Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        //Gets all items in the list.
        ArrayList<String> stringList = new ArrayList<>();
        for (int i = 0; i < adapter.getItemCount(); i++) {
            EditText editText = list.getChildAt(i).findViewById(R.id.string_item);
            String text = editText.getText().toString();
            if (!text.equals("")) {
                stringList.add(text);
            }
        }

        //Converts list to json. To allow it to be saved to SharedPreferences.
        Gson gson = new Gson();
        String json = gson.toJson(stringList);
        editor.putString("stringListFragmentList", json);
        editor.apply();
    }

    /**
     * Reloads saved list when fragment is started.
     */
    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences pref = getActivity().getSharedPreferences("stringListFragPrefId",
                                                                    Context.MODE_PRIVATE);

        String json = pref.getString("stringListFragmentList", "");
        if (!json.equals("[]")) {
            Gson gson = new Gson();
            items = gson.fromJson(json, new TypeToken<ArrayList<String>>(){}.getType());
        } else {
            items = new ArrayList<>();
        }
        adapter.loadList(items);
    }

    /**
     * Builds the custom dice and saves it to the extras field of the intent. Sets defaults for
     * blank fields.
     * @param intent Intent to save the dice to.
     */
    protected void buildResults(Intent intent) {
        //Gets all items in the list.
        ArrayList<String> stringList = new ArrayList<>();
        for (int i = 0; i < adapter.getItemCount(); i++) {
            EditText editText = list.getChildAt(i).findViewById(R.id.string_item);
            String text = editText.getText().toString();
            if (!text.equals("")) {
                stringList.add(text);
            }
        }

        //Converts list to array.
        String[] stringArray = new String[stringList.size()];
        stringArray = stringList.toArray(stringArray);

        //Saves dice to intent.
        intent.putExtra(MoreOptions.listReturn, stringArray);
        intent.putExtra(MoreOptions.numDiceReturn, 1);
    }
}
