package sirghi.andrei.memoryofagoldfish;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {
    GameViewModel mGameViewModel;
    //TileFragment mTileFragment;
    private LiveData<Game> mGame;
    ArrayList<Tile> mTiles;
    //private int mIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //Access to the puzzle though view model
        //loop through all tiles in puzzle, for each tile use fragment transaction to add a tile fragment to a gridlayout( create gridlayout in
        //activity game2)
        mGameViewModel = new ViewModelProvider(this).get(GameViewModel.class); //mTileFragment.getActivity() been used previous instead of "this"
        mGame =  mGameViewModel.newGame();
        gridView();

    }
    private void gridView(){
            mTiles = new ArrayList<>();
            mTiles = mGame.getValue().getTileArrayList();

        for (int i = 0; i < mTiles.size(); i++) {
            TileFragment content = TileFragment.newInstance(i);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.gameGridlayout, content).commit();

        }

    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

}