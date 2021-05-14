package sirghi.andrei.memoryofagoldfish;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;



public class MainActivity extends AppCompatActivity {

    //static final int REQUEST_DIALOG_RESPONSE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AcceptSSLCerts.accept();
        //Loads the images
        PuzzleRepository.getInstance(this.getApplicationContext()).getPuzzles();
        //PuzzleRepository.getInstance(this.getApplicationContext()).getPuzzle(0);


    }

    //This method is called when button "Play" is pressed
    public void puzzleActivity (View view) {
        Intent openListActivity = new Intent(getApplicationContext(), List.class);
        startActivity(openListActivity);
    }

    //This method is called when button "HighScoreTime" is pressed
    public void openHighScore(View view) {
        Intent openHighScoreIntent = new Intent(getApplicationContext(), HighScoreActivity.class);
        startActivity(openHighScoreIntent);
    }

    //This method is called when button "About" is pressed
    public void openAboutActivity(View view) {
        Intent openAboutIntent = new Intent(getApplicationContext(), About.class);
        startActivity(openAboutIntent);
    }




}