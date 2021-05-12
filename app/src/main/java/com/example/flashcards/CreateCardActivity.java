package com.example.flashcards;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CreateCardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_card);

        EditText editFront = (EditText) findViewById(R.id.editFront);
        EditText editBack = (EditText) findViewById(R.id.editBack);
        Button btnCreate = (Button) findViewById(R.id.btnCreate);
        Button btnCancel = (Button) findViewById(R.id.btnCancel);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Card newCard = new Card(editFront.getText().toString(),
                        editBack.getText().toString());
                Intent newCardIntent = new Intent();
                newCardIntent.putExtra("NEW_CARD", newCard);
                setResult(RESULT_OK, newCardIntent);
                finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });


    }
}