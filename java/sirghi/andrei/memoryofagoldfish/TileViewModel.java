package sirghi.andrei.memoryofagoldfish;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class TileViewModel extends AndroidViewModel {
    private LiveData<Tile> mTile;
    private PuzzleRepository mPuzzleRepository;

    public TileViewModel(@NonNull Application pApplication) {
        super(pApplication);
        mPuzzleRepository = mPuzzleRepository.getInstance(getApplication());
        getTile();
    }

    public LiveData<Tile> getTile() {
        if(mTile == null) {
            mTile = mPuzzleRepository.getTile(); //How to use getSelectedPuzzle if it has Puzzle instead of Tile
        }
        return mTile;
    }

    public void flipTile() {
        mTile.getValue().flip();
    }

}
