package com.example.d20;

import java.util.ArrayList;
import java.util.Random;


/**
 * Implements a dice that can be rolled. Each side is a string.
 */
class Dice {
    String[] stringList;
    private int numDice;

    /**
     * Constructs a Dice
     * @param stringList An array with each item representing a side of the dice.
     * @param numDice The number of dice of this type. If less than 1, defaults to 1.
     */
    public Dice(String[] stringList, int numDice) {
        this.stringList = stringList;
        if (numDice > 0) {
            this.numDice = numDice;
        } else {
            this.numDice = 1;
        }
    }

    /**
     * Constructs a Dice. numDice defaults to 1.
     * @param stringList An array with each item representing a side of the dice.
     */
    public Dice(String[] stringList) {
        this.stringList = stringList;
        this.numDice = 1;
    }

    /**
     * Chooses a random item in from the list of sides. (stringList)
     * @return Returns the random item that is chosen from stringList.
     */
    String roll() {
        int listLength = stringList.length;
        if (listLength == 0) {
            return null;
        }
        int rollIndex = new Random().nextInt(listLength);
        return stringList[rollIndex];
    }

    /**
     * Calls roll numDice number of times.
     * @return Returns single string of all rolls separated be ", ".
     */
    String rollAll() {
        //rollAll with numDice == 1 is the same as roll()
        if (numDice == 1) {
            return roll();
        }

        //If numDice > 1 it needs to loop.
        ArrayList<String> rolls = new ArrayList<>();
        for (int i = 0; i < getNumDice(); i++) {
            String roll = roll();
            if (roll != null) {
                rolls.add(roll());
            }
        }

        //Converts list to a single string.
        StringBuilder result = new StringBuilder();
        for (String roll : rolls) {
            if (result.length() == 0) {
                result.append(roll);
            } else {
                result.append(", ")
                        .append(roll);
            }
        }

        return result.toString();
    }

    int getNumDice() {
        return numDice;
    }

    /**
     * Sets number of dice. If less than 1, sets it to 1.
     * @param numDice Number of dice.
     */
    void setNumDice(int numDice) {
        if (numDice > 0) {
            this.numDice = numDice;
        } else {
            this.numDice = 1;
        }
    }
}
