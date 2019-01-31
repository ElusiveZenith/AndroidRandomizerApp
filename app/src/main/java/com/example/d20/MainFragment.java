package com.example.d20;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.ArrayList;

/**
 * Fragments for created number based dice.
 */
public class MainFragment extends Fragment {

    private EditText minText;
    private EditText maxText;
    private EditText numDiceText;
    private EditText multipleText;
    private CheckBox evens;
    private CheckBox odds;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        minText = (EditText) view.findViewById(R.id.min);
        maxText = (EditText) view.findViewById(R.id.max);
        numDiceText = (EditText) view.findViewById(R.id.numDice);
        multipleText = (EditText) view.findViewById(R.id.multiple);
        evens = (CheckBox) view.findViewById(R.id.evens);
        odds = (CheckBox) view.findViewById(R.id.odds);
    }

    /**
     * When fragment is stopped, saves data or state of each field so they can be set to the same
     * values when re-started.
     */
    @Override
    public void onStop(){
        super.onStop();
        SharedPreferences pref = getActivity().getSharedPreferences("mainFragPrefId",
                                                                    Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        //Gets integers of the EditText fields and skips if empty.
        try {
            editor.putInt("min", Integer.valueOf(minText.getText().toString()));
        } catch (NumberFormatException e) {
            //There is no integer to get. That's fine, continue.
        }
        try {
            editor.putInt("max", Integer.valueOf(maxText.getText().toString()));
        } catch (NumberFormatException e) {
            //There is no integer to get. That's fine, continue.
        }
        try {
            editor.putInt("numDice", Integer.valueOf(numDiceText.getText().toString()));
        } catch (NumberFormatException e) {
            //There is no integer to get. That's fine, continue.
        }
        try {
            editor.putInt("multiple", Integer.valueOf(multipleText.getText().toString()));
        } catch (NumberFormatException e) {
            //There is no integer to get. That's fine, continue.
        }

        editor.putBoolean("evens", evens.isChecked());
        editor.putBoolean("odds", odds.isChecked());
        editor.apply();
    }

    /**
     * Sets fields and states to saved data when re-stared so that it looks the same as when it
     * was closed.
     */
    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences pref = getActivity().getSharedPreferences("mainFragPrefId",
                                                                    Context.MODE_PRIVATE);
        //Gets values
        int min = pref.getInt("min", 1);
        int max = pref.getInt("max", 20);
        int numDice = pref.getInt("numDice", 1);
        int multiple = pref.getInt("multiple", 1);
        boolean hasEvens = pref.getBoolean("evens", true);
        boolean hasOdds = pref.getBoolean("odds", true);
        //Sets Values
        minText.setText(String.valueOf(min));
        maxText.setText(String.valueOf(max));
        numDiceText.setText(String.valueOf(numDice));
        multipleText.setText(String.valueOf(multiple));
        evens.setChecked(hasEvens);
        odds.setChecked(hasOdds);
    }

    /**
     * Builds the custom dice and saves it to the extras field of the intent. Sets defaults for
     * blank fields.
     * @param intent Intent to save the dice to.
     */
    void buildResults(Intent intent) {
        int min;
        int max;
        int numDice;
        int multiple;
        boolean hasEvens;
        boolean hasOdds;

        //Gets integers of the EditText fields and sets it to default if empty.
        try {
            min = Integer.valueOf(minText.getText().toString());
        } catch (NumberFormatException e) {
            min = 1;
        }
        try {
            max = Integer.valueOf(maxText.getText().toString());
        } catch (NumberFormatException e) {
            max = 20;
        }
        try {
            numDice = Integer.valueOf(numDiceText.getText().toString());
        } catch (NumberFormatException e) {
            numDice = 1;
        }
        try {
            multiple = Integer.valueOf(multipleText.getText().toString());
        } catch (NumberFormatException e) {
            multiple = 1;
        }

        //Corrects invalid entries.
        if (multiple == 0) { multiple = 1; }
        if (numDice == 0) { numDice = 1; }

        //Gets boolean fields.
        hasEvens = evens.isChecked();
        hasOdds = odds.isChecked();

        //Constructs list of the sides of the dice.
        ArrayList<String> list = new ArrayList<>();
        for (int x = min; x <= max; x += 1) {
            if (x % 2 == 0) {
                if (!hasEvens) { continue; }
            } else {
                if (!hasOdds) { continue; }
            }
            if (x % multiple == 0) {
                list.add(String.valueOf(x));
            }
        }

        //Converts list to array.
        String[] stringList = new String[list.size()];
        stringList = list.toArray(stringList);

        //Puts data in intent.
        intent.putExtra(MoreOptions.listReturn, stringList);
        intent.putExtra(MoreOptions.numDiceReturn, numDice);
    }
}
