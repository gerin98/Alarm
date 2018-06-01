package com.example.gerin.alarm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

//this activity is entered from an intent upon clicking the gridview button
public class alarmChooserActivity extends AppCompatActivity {

    GridView grid;
    String[] web = {
            "Bridge",
            "New Dawn",
            "Night Sky",
            "Stream",
            "Sunset",
            "Waterfall",
            "Mountains",
            "Beach"
    } ;
    int[] imageId = {
            R.drawable.bridge_image,
            R.drawable.new_dawn_image,
            R.drawable.night_sky_image,
            R.drawable.stream_image,
            R.drawable.sunset_image,
            R.drawable.waterfall_image,
            R.drawable.mountain_image,
            R.drawable.beach_image

    };
    AlarmSound sound = new AlarmSound(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_chooser);

        //Intent callee = getIntent();
        GridAdapter adapter = new GridAdapter(alarmChooserActivity.this, web, imageId);
        grid = (GridView) findViewById(R.id.grid);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //ImageView clickedImage = (ImageView) findViewById(imageId[position]);
                //clickedImage.setImageAlpha(30);

                SharedPreferences preferences = getSharedPreferences("alarm_tune", Context.MODE_PRIVATE);
                final SharedPreferences.Editor editor = preferences.edit();

                switch (position){
                    case 0:
                        editor.putInt("tune", R.raw.down_stream);
                        editor.apply();
                        break;
                    case 1:
                        editor.putInt("tune", R.raw.new_dawn);
                        editor.apply();
                        break;
                    case 2:
                        editor.putInt("tune", R.raw.bittersweet);
                        editor.apply();
                        break;
                    case 3:
                        editor.putInt("tune", R.raw.silver_flame);
                        editor.apply();
                        break;
                    case 4:
                        editor.putInt("tune", R.raw.river_fire);
                        editor.apply();
                        break;
                    case 5:
                        editor.putInt("tune", R.raw.soaring);
                        editor.apply();
                        break;
                    case 6:
                        editor.putInt("tune", R.raw.dreamer);
                        editor.apply();
                        break;
                    case 7:
                        editor.putInt("tune", R.raw.fireflies_and_stardust);
                        editor.apply();
                        break;
                }

//                if(position == 0) {
//                    editor.putInt("tune", R.raw.down_stream);
//                    editor.apply();
//                }
//                else if(position == 1){
//                    editor.putInt("tune", R.raw.new_dawn);
//                    editor.apply();
//                }

                sound.stopTune();
                sound.chooseTrack(preferences.getInt("tune",0));
                sound.playTune();

                Toast.makeText(alarmChooserActivity.this, web[+ position]+" alarm set", Toast.LENGTH_SHORT).show();
            }

        });

    }

    @Override
    protected  void onDestroy() {

        super.onDestroy();
        sound.stopTune();
    }
}
