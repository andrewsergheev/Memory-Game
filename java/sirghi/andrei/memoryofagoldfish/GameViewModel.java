package sirghi.andrei.memoryofagoldfish;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.json.JSONException;

public class GameViewModel extends AndroidViewModel {
    private PuzzleRepository mPuzzleRepository;
    private LiveData<Puzzle> mSelectedPuzzle;
    LiveData<Game> mGame;

    public GameViewModel(@NonNull Application application) {
        super(application);
        mPuzzleRepository = PuzzleRepository.getInstance(getApplication());
        mSelectedPuzzle = mPuzzleRepository.getSelectedPuzzle();
        mSelectedPuzzle.getValue().newGame();
        mGame = mSelectedPuzzle.getValue().getGeneratedGame().gameLiveData();

    }

    public LiveData<Game> gameLiveData()
    {
        return mGame.getValue().gameLiveData();
    }

    public  LiveData<Game> newGame(){
        mSelectedPuzzle.getValue().newGame();
        mGame= mSelectedPuzzle.getValue().getGeneratedGame().gameLiveData();
        return mGame;
    }

    public LiveData<Game> getGame() {
        return mGame;
    }


    public LiveData<Tile> tileIndex(int pIndex)
    {
        return mGame.getValue().tileIndex(pIndex);
    }

    public void flipTile(int pIndex) {
        tileIndex(pIndex).getValue().flip();
    }

    public void setHighScore(String userName, String puzzleName, String time, String dateOdScore, String turns, String sequence) throws JSONException {
        mPuzzleRepository.setHighScore(userName, puzzleName, time, dateOdScore, turns, sequence);
    }

}
