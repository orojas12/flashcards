package com.example.flashcards;

import android.os.Parcel;
import android.os.Parcelable;

public class Card implements Parcelable {
    private String frontText;
    private String backText;

    public Card(String frontText, String backText) {
        this.frontText = frontText;
        this.backText = backText;
    }

    public void setFrontText(String frontText) {
        this.frontText = frontText;
    }

    public void setBackText(String backText) {
        this.backText = backText;
    }

    public String getFrontText() {
        return frontText;
    }

    public String getBackText() {
        return backText;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Write the Card's data to the passed-in Parcel
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(frontText);
        dest.writeString(backText);
    }

    // This is used to regenerate the Card object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Card> CREATOR = new Parcelable.Creator<Card>() {
        public Card createFromParcel(Parcel in) {
            return new Card(in);
        }

        public Card[] newArray(int size) {
            return new Card[size];
        }
    };

    // Constructor that takes a Parcel and returns a Card object populated with its values
    // Values are retrieved in the same order that they were written (in a FIFO approach)
    private Card(Parcel in) {
        frontText = in.readString();
        backText = in.readString();
    }
}
