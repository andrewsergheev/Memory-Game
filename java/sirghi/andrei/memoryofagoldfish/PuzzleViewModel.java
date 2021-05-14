package sirghi.andrei.memoryofagoldfish;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;

public class PuzzleViewModel extends AndroidViewModel {
    private LiveData<ArrayList<Puzzle>> mPuzzle;
    private LiveData<Puzzle> mSelectedPuzzle;
    private PuzzleRepository mPuzzleRepository;

    private int mSelectedIndex;

    public PuzzleViewModel(@NonNull Application pApplication) {
        super(pApplication);
        mPuzzleRepository = PuzzleRepository.getInstance(getApplication());
        getPuzzles();

    }

    public LiveData<ArrayList<Puzzle>> getPuzzles() {
        if (mPuzzle == null) {
            mPuzzle = mPuzzleRepository.getPuzzles();
        }
        return mPuzzle;
    }

    public LiveData<Puzzle> getPuzzle (int pPuzzleIndex) {
        return mPuzzleRepository.getPuzzle(pPuzzleIndex);
    }

    public void selectPuzzle(int pIndex) {
        if (pIndex != mSelectedIndex ||  mSelectedPuzzle == null) { //might be problem in "||" symbol
            mSelectedIndex = pIndex;
            mSelectedPuzzle = getPuzzle(mSelectedIndex);
        }
    }

    public LiveData<Puzzle> getSelectedPuzzle() {
        selectPuzzle(mSelectedIndex);
        return mSelectedPuzzle;
    }
/*
    public void setHighScoreObject() {
        mPuzzleRepository.mHighScore = new HighScore();
    }
 */
}
