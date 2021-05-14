package sirghi.andrei.memoryofagoldfish;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class HighScoreSequence extends AppCompatActivity {

    HighScoreViewModel mHighScoreViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score_sequence);

        mHighScoreViewModel = new ViewModelProvider(this).get(HighScoreViewModel.class);

        mHighScoreViewModel.loadHighScoreFromRepository();
        HighScore highscore =  mHighScoreViewModel.getHighScoreObject();

        LinearLayout layout = (LinearLayout) findViewById(R.id.listOfScoresLayout);

        //Checking if high score folder is not empty
        ContextWrapper contextWrapper = new ContextWrapper(this);
        File file = contextWrapper.getDir("highScore", Context.MODE_PRIVATE);
        if (file.isDirectory()) {
            String[] files = file.list();
            if (files != null && files.length > 0) {
                ArrayList<String> arrayList = highscore.getSequenceArrayList();

                for (int i = 0; i < arrayList.size(); i++)
                {
                    TextView tv = new TextView(this);
                    tv.setTextSize(20);
                    tv.setTextColor(Color.WHITE);
                    tv.setPadding(0, 0, 0, 20);
                    tv.setText((i + 1) + ". " + arrayList.get(i));
                    layout.addView(tv);
                }
            }
        }


        
    }
}