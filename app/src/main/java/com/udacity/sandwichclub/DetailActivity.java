package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView ingredientsIv = findViewById(R.id.image_iv);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI(sandwich);
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(ingredientsIv);

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {
        TextView name = findViewById(R.id.myImageViewText);
        TextView alsoKnownAs = findViewById(R.id.also_known_tv);
        TextView origin = findViewById(R.id.origin_tv);
        TextView description = findViewById(R.id.description_tv);
        TextView ingredients = findViewById(R.id.ingredients_tv);

        name.setText(missingInfo(sandwich.getMainName()));
        origin.setText(missingInfo(sandwich.getPlaceOfOrigin()));
        description.setText(missingInfo(sandwich.getDescription()));

        List<String> sandwichAlsoKnownAs = sandwich.getAlsoKnownAs();
        String other_name = "";
        for (String s: sandwichAlsoKnownAs) {other_name += s + ", ";}
        if (other_name.length() != 0){
            other_name = other_name.substring(0, other_name.length() - 2);
        }
        alsoKnownAs.setText(missingInfo(other_name));
        other_name = "";


        List<String> sandwichIngredients = sandwich.getIngredients();
        for (String s: sandwichIngredients) {
            other_name += " * " + s + "\n";
        }
        ingredients.setText(missingInfo(other_name));
    }

    private String missingInfo(String info){
        if (info.equals("")){
            return getString(R.string.no_info);
        } else {
            return info;
        }
    }
}
