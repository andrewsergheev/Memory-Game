package sirghi.andrei.memoryofagoldfish;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HighScoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);
    }

    //This method is called when button "HighScoreTime" is pressed
    public void timeHighScore (View view) {
        Intent openTimeHighScoreActivity = new Intent(getApplicationContext(), HighScoreTime.class);
        startActivity(openTimeHighScoreActivity);
    }

    //This method is called when button "HighScoreTurns" is pressed
    public void turnsHighScore(View view) {
        Intent openHighScoreTurnsIntent = new Intent(getApplicationContext(), HighScoreTurns.class);
        startActivity(openHighScoreTurnsIntent);
    }

    //This method is called when button "About" is pressed
    public void sequenceHighScore(View view) {
        Intent openHighScoreSequenceIntent = new Intent(getApplicationContext(), HighScoreSequence.class);
        startActivity(openHighScoreSequenceIntent);
    }
}