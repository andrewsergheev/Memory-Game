package sirghi.andrei.memoryofagoldfish;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Collections;

public class Game {
    ArrayList<Tile> mTileArrayList;
    ArrayList<Tile> mSecondTileArray;
        private MutableLiveData<Game> mGameMutableLiveData;
    private int mAnimationCompleteCount;
    private boolean mIsGameFinished;
    private int mTilePairsFound;
    private int turnsMade;
    private String puzzleName;

    public Game(Puzzle pPuzzle){
        mAnimationCompleteCount = 0;
        mIsGameFinished = false;
        mTilePairsFound = 0;
        mSecondTileArray = new ArrayList<>();
        mGameMutableLiveData = new MutableLiveData<>();
        mTileArrayList = new ArrayList<>();
        generateTileBitmapImages(pPuzzle);
        setPuzzleName(pPuzzle);
        mGameMutableLiveData.setValue(this);
    }

    private void generateTileBitmapImages(Puzzle pPuzzle) {
        mTileArrayList = new ArrayList<>();
        ArrayList<Bitmap> images = pPuzzle.imageArray;
        Bitmap tileBack = pPuzzle.getTileBackImage();

        for (int i = 0; i < images.size(); i++) {
            Tile firstTile = new Tile(images.get(i), tileBack, i);
            Tile secondTile = new Tile(images.get(i), tileBack, i);
            mTileArrayList.add(firstTile);
            mTileArrayList.add(secondTile);
        }
        //Randomly allocated tiles
        Collections.shuffle(mTileArrayList);
        mGameMutableLiveData.setValue(this);

    }

    public  ArrayList<Tile> getTileArrayList(){return mTileArrayList;}

    public LiveData<Game> gameLiveData(){return mGameMutableLiveData;}

    public LiveData<Tile> tileIndex(int pIndex) {
        return mTileArrayList.get(pIndex).getTileData();
    }

    public void setAnimationCompleteCount() {
        mAnimationCompleteCount++;
        mGameMutableLiveData.setValue(this);
    }

    public void resetAnimationCompleteCount() {
        mAnimationCompleteCount = 0;
        mGameMutableLiveData.setValue(this);
    }

    public int getAnimationCompleteCount() {
        return mAnimationCompleteCount;
    }

    public int getTilePairsFound() {
        return mTilePairsFound;
    }

    public void setTilePairsFound() {
        mTilePairsFound++;
        mGameMutableLiveData.setValue(this);
    }

    public void setIsGameFinished(boolean pIsGameFinished) {
        mIsGameFinished = pIsGameFinished;
        mGameMutableLiveData.setValue(this);
    }

    public boolean getIsGameFinished() {
        return mIsGameFinished;
    }

    public ArrayList<Tile> getSecondTileArray() {
        return mSecondTileArray;
    }

    public void addTileToArrayList(Tile pTile) {
        mSecondTileArray.add(pTile);
        mGameMutableLiveData.setValue(this);
    }

    public void resetTileToArrayList() {
        mSecondTileArray.clear();
        mGameMutableLiveData.setValue(this);
    }

    public void setTurnsCount() {
        turnsMade++;
    }

    public int getTurnsCount() {
        return turnsMade;
    }

    private void setPuzzleName(Puzzle pPuzzle) {
        puzzleName = pPuzzle.getName();
    }
    public String getPuzzleName() {
        return  puzzleName;
    }


}