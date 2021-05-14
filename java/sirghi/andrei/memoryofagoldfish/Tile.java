package sirghi.andrei.memoryofagoldfish;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class  Tile {
    MutableLiveData<Tile> mTileData;
    private Bitmap mFront;
    private Bitmap mBack;
    private int mId;
    private boolean mHidden;
    private boolean isFound;

    public Tile() {
        mTileData = new MutableLiveData<>();
        mTileData.setValue(this);
        mHidden = false;
    }

    public Tile(Bitmap pFront, Bitmap pBack, int pId) {
        setFront(pFront);
        setBack(pBack);
        setId(pId);

        mTileData = new MutableLiveData<>();
        isFound = false;
        mHidden = false;
        mTileData.setValue(this);
    }

    public void setFront(Bitmap mFront) { this.mFront = mFront; }
    public Bitmap getFront() { return mFront; }

    public void setBack(Bitmap mBack) { this.mBack = mBack; }
    public Bitmap getBack() { return mBack; }

    public void setId(int mId) { this.mId = mId; }
    public int getId() { return mId; }

    public boolean isHidden() {
        return mHidden;
    }
    public void setHidden(boolean pHidden) {
        mHidden = pHidden;
        mTileData.setValue(this);
    }

    public boolean isFound() {
        return isFound;
    }
    public void setIsFound(boolean pIsFound) {
        isFound = pIsFound;
        mTileData.setValue(this);
    }

    public void flip() {
        mHidden = !mHidden;
        mTileData.setValue(this);
    }

    public LiveData<Tile> getTileData() {
        return mTileData;
    }

}
