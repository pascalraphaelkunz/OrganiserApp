package com.example.organisator.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.organisator.Helpers.Database.DatabaseHelperRechnung;
import com.example.organisator.Helpers.Rechnung.RechnungsDetails;
import com.example.organisator.R;


public class RechnungListView extends AppCompatActivity {

    DatabaseHelperRechnung cursor = new DatabaseHelperRechnung(RechnungListView.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rechnung_list_view);

        // Set up the toolbar as the app bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enable the back arrow in the toolbar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        LinearLayout invoiceListLayout = findViewById(R.id.invoice_list);
        for (RechnungsDetails rechnungsDetails : cursor.getAllRechnungsDetails()) {
            // Inflate the list_item.xml layout
            View listItem = getLayoutInflater().inflate(R.layout.invoice_list, null);

            // Find the TextViews in the inflated layout
            TextView name = listItem.findViewById(R.id.textViewName);
            TextView date = listItem.findViewById(R.id.textViewDueDate);
            TextView amount = listItem.findViewById(R.id.textViewAmount);

            amount.setText(rechnungsDetails.getAmount().toString());
            date.setText(rechnungsDetails.getDate().toString());
            name.setText(rechnungsDetails.getName());
            listItem.setOnClickListener(setTileClickListener(name.getText().toString()));
            invoiceListLayout.addView(listItem);
        }
    }


    private View.OnClickListener setTileClickListener(final String tileTitle) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tileTitle.equals("Rechnungen")) {
                    Intent intent = new Intent(v.getContext(), Rechnung.class); // Replace NewActivity.class with the name of your new activity
                    v.getContext().startActivity(intent);
                }
            }
        };
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Handle the back arrow click event
            finish(); // Finish the current activity to go back
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
