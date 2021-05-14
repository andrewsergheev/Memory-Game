package sirghi.andrei.memoryofagoldfish;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Collections;

public class Puzzle {
    private String mName;
    private  Bitmap mTileBackImage;
    ArrayList<Bitmap> imageArray = new ArrayList<>();
    private ArrayList<Tile> tileArray = new ArrayList<>();
    private Game mGame;

    public void newGame()
    {
        mGame = new Game(this);
    }
    public Game getGeneratedGame(){return mGame;}

    public Puzzle() {

    }

    public String getName() {return mName;}
    public void setName(String pName) {mName = pName;}

    public Bitmap getImage(int pIndex) {
        return imageArray.get(pIndex);
    }
    public  void setImage(Bitmap pImage, int pIndex) {
        if (pIndex == -1) {
            setTileBackImage(pImage);
        }else {
            imageArray.add(pImage);
        }
    }

    public Bitmap getTileBackImage() {return mTileBackImage;}
    public  void setTileBackImage(Bitmap pTileBackImage) {mTileBackImage = pTileBackImage;}

    public int imageCount() {
        return imageArray.size();
    }
    public int tileCount() {
        return tileArray.size();
    }
}
