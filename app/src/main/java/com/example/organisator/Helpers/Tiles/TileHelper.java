package com.example.organisator.Helpers.Tiles;

import android.app.Activity;
import android.content.res.Resources;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.organisator.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class TileHelper extends AppCompatActivity {

    private final TileClickListener tileClickListener;
    private final Activity activity;

    public TileHelper(Activity activity, TileClickListener listener) {
        this.activity = activity;
        this.tileClickListener = listener;
    }

    public void setTilesFromJSON(String fileName, LinearLayout parentLayout, int tiles) {
        try {
            Resources resources = activity.getResources();
            InputStream inputStream = resources.openRawResource(tiles);
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
                View tileLayout = activity.getLayoutInflater().inflate(R.layout.tile_layout, null);
                JSONObject tileObject = tilesArray.getJSONObject(i);
                String title = tileObject.getString("title");
                String description = tileObject.getString("description");

                TextView titleTextView = tileLayout.findViewById(R.id.tile_title);
                TextView descriptionTextView = tileLayout.findViewById(R.id.tile_description);

                titleTextView.setText(title); // Set the title
                descriptionTextView.setText(description); // Set the description
                tileLayout.setOnClickListener(this.tileClickListener.onTileClick(title));

                parentLayout.addView(tileLayout);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
