package sirghi.andrei.memoryofagoldfish;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class HighScoreTime extends AppCompatActivity {

    HighScoreViewModel mHighScoreViewModel;
    //ArrayList<String> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score_time);

        mHighScoreViewModel = new ViewModelProvider(this).get(HighScoreViewModel.class);

        mHighScoreViewModel.loadHighScoreFromRepository();
        //PuzzleRepository.getInstance(this.getApplicationContext()).loadHighScore();
        HighScore highscore =  mHighScoreViewModel.getHighScoreObject();

        LinearLayout layout = (LinearLayout) findViewById(R.id.listOfScoresLayout);

        //Checking if high score folder is not empty
        ContextWrapper contextWrapper = new ContextWrapper(this);
        File file = contextWrapper.getDir("highScore", Context.MODE_PRIVATE);
        if (file.isDirectory()) {
            String[] files = file.list();
            if (files != null && files.length > 0) {

                ArrayList<String> arrayList = highscore.getHighScoreArrayList();

                //LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                for (int i = 0; i < arrayList.size(); i++)
                {
                    TextView tv = new TextView(this);
                    //tv.setLayoutParams(lparams);
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