package com.example.d20;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import com.google.gson.*;


/**
 * Main activity.
 */
public class MainActivity extends AppCompatActivity {

    private ArrayList<Dice> dice = new ArrayList<>();
    private int index;
    private static final int CUSTOM_LIST_REQUEST_CODE = 1;

    /**
     * Constructor. Populates dice with the following dice, d2 d4 d6 d8 d10 d12 d16 d20 and
     * {"red","green","blue"}. Sets index to the index of d20, which is 7.
     */
    public MainActivity() {
        int[] diceToMake = {2,4,6,8,10,12,16,20};
        for (int d : diceToMake) {
            String[] diceString = new String[d];
            for (int x = 1; x <= d; x++) {
                diceString[x-1] = String.valueOf(x);
            }
            Dice newDice = new Dice(diceString);
            dice.add(newDice);
        }
        index = dice.size()-1;

        //Adds Default custom dice.
        String[] customString = {"red","green","blue"};
        Dice newDice = new Dice(customString);
        dice.add(newDice);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * When activity is stopped, saves the custom dice to be reloaded.
     */
    @Override
    protected void onStop(){
        super.onStop();
        SharedPreferences pref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        int customIndex = dice.size() - 1;
        int numDice = dice.get(customIndex).getNumDice();

        //Converts list to json. To allow it to be saved to SharedPreferences.
        String[] customList = dice.get(customIndex).stringList;
        Gson gson = new Gson();
        String json = gson.toJson(customList);

        editor.putString("customDiceString", json);
        editor.putInt("customDiceNumDice", numDice);
        editor.apply();
    }

    /**
     * When activity is started, loads the saved custom dice and saves it to the last index of dice.
     */
    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences pref = getPreferences(Context.MODE_PRIVATE);

        int numDice = pref.getInt("customDiceNumDice", 1);
        String json = pref.getString("customDiceString", "");
        if (!json.equals("")) {
            Gson gson = new Gson();
            String[] customList = gson.fromJson(json, String[].class);
            Dice customDice = new Dice(customList, numDice);

            int customIndex = dice.size() - 1;
            dice.set(customIndex, customDice);
        }
    }

    /**
     * Rolls the dice at the index in dice and sets the resulting string from rollAll() to the
     * textView that shows the roll.
     * @param view Not used, but provided for easy implementation with a button.
     */
    public void roll(View view) {
        TextView rollNumber = findViewById(R.id.textView);
        rollNumber.setText(dice.get(index).rollAll());
    }

    /**
     * Call whenever index is changed. Changes the textView of the roll and the selected dice to
     * the string of the last item of the selected dice. If index is the last index of dice, then
     * the string that is displayed is the string at R.string.custom_dice_name.
     */
    public void changeDice() {
        TextView rollNumber = findViewById(R.id.textView);
        TextView diceSelection = findViewById(R.id.textView2);

        if (index != dice.size() - 1) {
            String message = dice.get(index).stringList[dice.get(index).stringList.length - 1];
            rollNumber.setText(message);
            diceSelection.setText(String.format("d%s", message));
        } else {
            String message = getString(R.string.custom_dice_name);
            rollNumber.setText(message);
            diceSelection.setText(message);
        }
    }

    /**
     * Advances index by one and calls changeDice(). Loops back to beginning if index is already
     * on the last index of dice.
     * @param view Not used, but provided for easy implementation with a button.
     */
    public void rightButton(View view) {
        if (index < dice.size()-1) {
            index += 1;
        } else {
            index = 0;
        }
        changeDice();
    }

    /**
     * Retreats index by one and calls changeDice(). Loops back to end if index is already
     * on the first index of dice.
     * @param view Not used, but provided for easy implementation with a button.
     */
    public void leftArrow(View view) {
        if (index > 0) {
            index -= 1;
        } else {
            index = dice.size() - 1;
        }
        changeDice();
    }

    /**
     * Starts MoreOptions activity of results.
     * @param view Not used, but provided for easy implementation with a button.
     */
    public void optionButton(View view) {
        Intent intent = new Intent(this, MoreOptions.class);
        startActivityForResult(intent, CUSTOM_LIST_REQUEST_CODE);
    }

    /**
     * Callback for result from MoreOptions activity. Replaces the custom dice that is saved in
     * SharedPreferences with the custom dice that is returned from MoreOptions activity. onStart()
     * loads the dice into the ArrayList dice. Also sets the index to that of the custom dice
     * and calls changeDice().
     * @param requestCode Request code that was provided when startActivityForResults was called.
     * @param resultCode Status code. Will change dice when == RESULT_OK
     * @param data Intent containing data as extras.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CUSTOM_LIST_REQUEST_CODE) {
            if (resultCode == RESULT_OK && data != null) {
                String[] customList = data.getStringArrayExtra(MoreOptions.listReturn);
                int numDice = data.getIntExtra(MoreOptions.numDiceReturn, 1);
                if (customList == null) {
                    return;
                }

                SharedPreferences pref = getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();

                //Converts list to json. To allow it to be saved to SharedPreferences.
                Gson gson = new Gson();
                String json = gson.toJson(customList);
                editor.putString("customDiceString", json);
                editor.putInt("customDiceNumDice", numDice);
                editor.apply();

                index = dice.size() - 1;
                changeDice();
            }
        }
    }
}
