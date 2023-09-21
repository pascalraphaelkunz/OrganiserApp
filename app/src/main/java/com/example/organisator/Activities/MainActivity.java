package com.example.organisator.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.organisator.Activities.Rechnung.RechnungBase;
import com.example.organisator.Helpers.Tiles.TileClickListener;
import com.example.organisator.Helpers.Tiles.TileHelper;
import com.example.organisator.R;

public class MainActivity extends AppCompatActivity implements TileClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout parentLayout = findViewById(R.id.parent_layout);

        // Initialize the TileHelper with this activity instance and TileClickListener
        TileHelper tileHelper = new TileHelper(this, this);

        // Call setTilesFromJSON after setContentView to ensure the context is initialized
        tileHelper.setTilesFromJSON("tiles", parentLayout, R.raw.tiles);
    }


    @Override
    public View.OnClickListener onTileClick(String title) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (title.equals("Rechnungen")){
                    Intent intent = new Intent(v.getContext(), RechnungBase.class); // Replace NewActivity.class with the name of your new activity
                    v.getContext().startActivity(intent);
                }
            }
        };
    }
}
