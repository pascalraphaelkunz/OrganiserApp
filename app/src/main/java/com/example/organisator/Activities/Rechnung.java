package com.example.organisator.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.organisator.Helpers.Database.DatabaseHelperRechnung;
import com.example.organisator.Helpers.Rechnung.RechnungsDetails;
import com.example.organisator.Helpers.Tiles.TileClickListener;
import com.example.organisator.Helpers.Tiles.TileHelper;
import com.example.organisator.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Date;


public class Rechnung extends AppCompatActivity implements TileClickListener {
    DatabaseHelperRechnung cursor = new DatabaseHelperRechnung(Rechnung.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rechnung);

        // Set up the toolbar as the app bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // Enable the back arrow in the toolbar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setTilesFromJSON("tilesrechnungen");

        LinearLayout parentLayout = findViewById(R.id.parent_layout);

        // Initialize the TileHelper with this activity instance and TileClickListener
        TileHelper tileHelper = new TileHelper(this, this);

        // Call setTilesFromJSON after setContentView to ensure the context is initialized
        tileHelper.setTilesFromJSON("tiles", parentLayout, R.raw.tilesrechnungen);
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

    public void setTilesFromJSON(String fileName){
        LinearLayout parentLayout = findViewById(R.id.parent_layout); // Replace with your parent layout's ID
        try {
            // Read the JSON file from the resources
            Resources resources = getResources();
            InputStream inputStream = resources.openRawResource(R.raw.tilesrechnungen);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            String json = new String(buffer, StandardCharsets.UTF_8);

            // Parse the JSON data
            JSONObject jsonObject = new JSONObject(json);
            JSONArray tilesArray = jsonObject.getJSONArray(fileName);

            // Create and add tiles based on the JSON data
            for (int i = 0; i < tilesArray.length(); i++) {
                View tileLayout = getLayoutInflater().inflate(R.layout.tile_layout, null);
                JSONObject tileObject = tilesArray.getJSONObject(i);
                String title = tileObject.getString("title");
                String description = tileObject.getString("description");

                TextView titleTextView = tileLayout.findViewById(R.id.tile_title);
                TextView descriptionTextView = tileLayout.findViewById(R.id.tile_description);

                titleTextView.setText(title); // Set the title
                descriptionTextView.setText(description); // Set the description


                tileLayout.setOnClickListener(setTileClickListener(title));

                // Add the tile to the parent layout
                parentLayout.addView(tileLayout);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private View.OnClickListener setTileClickListener(final String tileTitle) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tileTitle.equals("Rechnung erfassen")){
                    openInputDialogErfassen();
                }
                if (tileTitle.equals("Ausstehende Zahlungen anschauen")){
                    openInputDialogAnschauen(v);
                }
            }
        };
    }

    private void openInputDialogAnschauen(View v){
        Intent intent = new Intent(v.getContext(), RechnungListView.class); // Replace NewActivity.class with the name of your new activity
        v.getContext().startActivity(intent);
    }

    private void openInputDialogErfassen() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Set the dialog title
        builder.setTitle("Rechnung erfassen");

        // Set the layout for the dialog
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_rechnung_erfassen, null);
        builder.setView(dialogView);

        // Initialize views in the dialog
        final EditText nameEditText = dialogView.findViewById(R.id.nameEditText);
        final DatePicker dateEditText = dialogView.findViewById(R.id.datePicker);
        final EditText amountEditText = dialogView.findViewById(R.id.invoiceAmount);

        // Create an EditText for user input
        final EditText input = new EditText(this);

        // Add OK and Cancel buttons to the dialog
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = nameEditText.getText().toString();
                String amountStr = amountEditText.getText().toString();
                String dateStr = dateEditText.getYear() + "-" + String.format("%02d", dateEditText.getMonth() + 1) + "-" + String.format("%02d", dateEditText.getDayOfMonth());

                if (name.isEmpty() || amountStr.isEmpty()) {
                    nameEditText.setError("Name is required");
                    amountEditText.setError("Amount is required");
                } else {
                    try {
                        Date dueDate = Date.valueOf(dateStr);
                        float amount = Float.parseFloat(amountStr);

                        // Insert data into the database if all fields are valid.
                        cursor.insertRechnung(new RechnungsDetails(name, dueDate, amount));
                    } catch (NumberFormatException e) {
                        // Handle the case where amount cannot be parsed as a float.
                        // Display an error message or toast indicating that the amount is invalid.
                        // Example:
                        // amountEditText.setError("Invalid amount format");
                    }
                }
            }
        });


        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel(); // Close the dialog if Cancel is clicked
            }
        });

        // Show the dialog
        builder.show();
    }


    @Override
    public View.OnClickListener onTileClick(String title) {
        return null;
    }
}
