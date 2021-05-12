package com.example.flashcards;

/*
1. Splash screen
2. Home Screen (list all cards)
3. Home Screen > Shuffle and practice all cards
4. Home Screen > View single card
5. Home Screen > Create flashcard
6. Link to github
 */

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static final int NEW_CARD_REQUEST = 0;
    static final int DELETE_CARD_REQUEST = 1;

    public ArrayList<Card> cardList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_AppCompat_Light);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean firstRun = sharedPref.getBoolean("first_run", true);

        if (firstRun) {
            SharedPreferences.Editor editor = sharedPref.edit();
            createExampleCards();
            editor.putBoolean("first_run", false);
            editor.commit();
        }

        getCardList();

        ListView listview = findViewById(R.id.list);
        listview.setAdapter(new CardListAdapter(this, cardList));
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Card card = cardList.get(position);
                Intent viewCardIntent = new Intent(MainActivity.this, ViewCardActivity.class);
                viewCardIntent.putExtra("CARD", card);
                viewCardIntent.putExtra("CARD_POSITION", position);

                // If card is not deleted in ViewCardActivity, request will be cancelled by default.
                startActivityForResult(viewCardIntent, DELETE_CARD_REQUEST);
            }
        });
    }

    private void getCardList() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        Type cardListType = new TypeToken<ArrayList<Card>>(){}.getType();

        String json = sharedPref.getString("cards", null);

        if (json == null) {
            return;
        }

        cardList = gson.fromJson(json, cardListType);
    }

    private void createExampleCards() {
        // Create example cards
        Card card1 = new Card("Activity",
                "An Android component that represents a single screen with a user interface.");
        Card card2 = new Card("GridView",
                "A View container that displays a grid of objects with rows and columns.");
        Card card3 = new Card("Linear Layout",
                "A layout that arranges components in a vertical column or horizontal row.");
        cardList.add(card1);
        cardList.add(card2);
        cardList.add(card3);

        saveCards();
    }

    private void saveCards() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();

        String json = gson.toJson(cardList);
        editor.putString("cards", json);
        editor.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menuCreateCard:
                Intent newCardIntent = new Intent(this, CreateCardActivity.class);
                startActivityForResult(newCardIntent, NEW_CARD_REQUEST);
                return true;
            case R.id.menuShuffle:
                Intent shuffleCardsIntent = new Intent(this, ShuffleCardsActivity.class);
                shuffleCardsIntent.putExtra("CARDS", cardList);
                startActivity(shuffleCardsIntent);
                return true;
            case R.id.menuViewSource:
                Intent viewSourceIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://github.com/orojas12/flashcards"));
                startActivity(viewSourceIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ListView listview = findViewById(R.id.list);

        if (requestCode == NEW_CARD_REQUEST) {
            if (resultCode == RESULT_OK) {
                Card newCard = data.getParcelableExtra("NEW_CARD");
                cardList.add(newCard);

                saveCards();

                // Tell the ListView to update its list items
                ((BaseAdapter) listview.getAdapter()).notifyDataSetChanged();
            }
        } else if (requestCode == DELETE_CARD_REQUEST) {
            if (resultCode == RESULT_OK) {
                int position = data.getIntExtra("POSITION", -1);
                cardList.remove(position);
                saveCards();

                // Tell the ListView to update its list items
                ((BaseAdapter) listview.getAdapter()).notifyDataSetChanged();
            }
        }
    }
}