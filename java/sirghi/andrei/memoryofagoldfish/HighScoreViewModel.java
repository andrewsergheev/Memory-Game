package sirghi.andrei.memoryofagoldfish;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class HighScoreViewModel extends AndroidViewModel {

    private PuzzleRepository mPuzzleRepository;


    public HighScoreViewModel(@NonNull Application application) {
        super(application);

        mPuzzleRepository = PuzzleRepository.getInstance(getApplication());
    }

    public void  loadHighScoreFromRepository() {
        mPuzzleRepository.loadHighScore();
    }

    public HighScore getHighScoreObject() {
        return mPuzzleRepository.getHighScoreObject();
    }
}
