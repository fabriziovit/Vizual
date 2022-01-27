package com.app.vizual;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.app.vizual.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    public static ActivityMainBinding binding;
    private String[] immagini_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        immagini_name = getResources().getStringArray(R.array.immagini_name);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.dropdown_item, immagini_name);
        binding.categorieAuto.setAdapter(arrayAdapter);

        bottone_premuto(binding);
    }

    public void bottone_premuto(ActivityMainBinding binding){
        binding.mainActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*intent
                richiesta api che displayano l'immagine compressa a schermo
                 */
            }
        });
    }
}