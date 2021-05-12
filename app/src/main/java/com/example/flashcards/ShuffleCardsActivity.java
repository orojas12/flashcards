package com.example.flashcards;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class ShuffleCardsActivity extends AppCompatActivity {

    private static ArrayList<Card> cards = new ArrayList<>();
    private static Card currentCard;
    private static boolean showingBack = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shuffle_cards);

        Intent viewCard = getIntent();
        cards = (ArrayList<Card>) viewCard.getSerializableExtra("CARDS");

        FrameLayout cardContainer = findViewById(R.id.frameLayout);
        ImageButton btnShuffle = findViewById(R.id.btnShuffle);

        // Shuffle button click handler
        btnShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRandomCard();
            }
        });

        // Flip card click handler
        cardContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipCard();
            }
        });

        // Display a random initial card
        getRandomCard();
    }

    private void getRandomCard() {
        int position = getRandomNumber(0, cards.size() - 1);
        currentCard = cards.get(position);

        // Replace current card with new one
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, new CardFrontFragment())
                .commit();

        showingBack = false;
    }

    private int getRandomNumber(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    private void flipCard() {
        if (showingBack) {
            // Flip to the front.
            getSupportFragmentManager().popBackStack();
            showingBack = false;
            return;
        }

        // Flip to the back.

        showingBack = true;

        // Create and commit a new fragment transaction that adds the fragment for
        // the back of the card, uses custom animations, and is part of the fragment
        // manager's back stack.

        getSupportFragmentManager()
                .beginTransaction()

                // Replace the default fragment animations with animator resources
                // representing rotations when switching to the back of the card, as
                // well as animator resources representing rotations when flipping
                // back to the front (e.g. when the system Back button is pressed).
                .setCustomAnimations(
                        R.animator.card_flip_right_in,
                        R.animator.card_flip_right_out,
                        R.animator.card_flip_left_in,
                        R.animator.card_flip_left_out)

                // Replace any fragments currently in the container view with a
                // fragment representing the next page (indicated by the
                // just-incremented currentPage variable).
                .replace(R.id.frameLayout, new CardBackFragment())

                // Add this transaction to the back stack, allowing users to press
                // Back to get to the front of the card.
                .addToBackStack(null)

                // Commit the transaction.
                .commit();
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                onBackPressed();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    /**
     * A fragment representing the front of the card.
     */
    public static class CardFrontFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View inflation = inflater.inflate(R.layout.card_front, container, false);
            TextView txtFront = inflation.findViewById(R.id.txtFront);
            txtFront.setText(currentCard.getFrontText());
            return inflation;
        }
    }

    /**
     * A fragment representing the back of the card.
     */
    public static class CardBackFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View inflation = inflater.inflate(R.layout.card_back, container, false);
            TextView txtBack = inflation.findViewById(R.id.txtBack);
            txtBack.setText(currentCard.getBackText());
            return inflation;
        }
    }
}