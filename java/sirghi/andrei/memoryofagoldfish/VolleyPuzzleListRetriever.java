package sirghi.andrei.memoryofagoldfish;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

public class VolleyPuzzleListRetriever implements VolleyJSONObjectResponse, VolleyPuzzleImageResponse{
    private String mUrl;
    //private String imageUrl;
    private MutableLiveData<ArrayList<Puzzle>> mPuzzlesData;
    private ArrayList<Puzzle> mPuzzles = new ArrayList<>();
    private RequestQueue mQueue;
    private Context mAppContext;
    private PuzzleRepository mPuzzleRepository;
    private MutableLiveData<Puzzle> mMutableLiveLocalData;

    public VolleyPuzzleListRetriever(String pUrl, Context pContext) {
        mUrl = pUrl;
        mAppContext = pContext;
        mQueue = Volley.newRequestQueue(pContext);

    }

    public LiveData<ArrayList<Puzzle>> getPuzzles() {
        mPuzzlesData = new MutableLiveData<ArrayList<Puzzle>>();
        CustomJSONObjectRequest request = new CustomJSONObjectRequest(Request.Method.GET, mUrl,
                null, "PuzzleIndex", this);
        mQueue.add(request.getJsonObjectRequest());
        return mPuzzlesData;
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onResponse(JSONObject pObject, String pTag) throws JSONException {
        if (pTag.equals("PuzzleIndex")) {
            mPuzzleRepository = PuzzleRepository.getInstance(mAppContext);
            ArrayList<Puzzle> puzzles = new ArrayList<>();
            try {
                JSONArray puzzleArray = pObject.getJSONArray("PuzzleIndex");
                for (int i=0; i < puzzleArray.length(); i++) {
                    String puzzleString = puzzleArray.getString(i);
                    String puzzleUrl = "https://www.goparker.com/600096/moag/puzzles/" + puzzleString;
                    CustomJSONObjectRequest request = new CustomJSONObjectRequest(Request.Method.GET, puzzleUrl,
                            null, "PuzzleRetriever", this);
                    //Check line below
                   // mPuzzleRepository.saveIndexLocally(pObject, "puzzleIndex.json"); //Saving Index locally, might be wrong
                    mQueue.add(request.getJsonObjectRequest());
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }else {
            Log.i("VolleyPuzzleListRetriever", pTag);
            //mPuzzleRepository.saveIndexLocally(pObject, "puzzleIndex.json");
            mPuzzleRepository.saveIndexLocally(pObject, pObject.getString("name") + ".json");
            mPuzzles.add(parseJSONResponse(pObject));
            mPuzzlesData.setValue(mPuzzles);
        }
    }

    @Override
    public void onResponse(Bitmap pImage, Puzzle pPuzzle, int pIndex) {
        Log.i("VolleyItemListRetriever", "Image retrieved for:" + pPuzzle.getName());
        if (pIndex == -1) {
            pPuzzle.setTileBackImage(pImage);
        }
        else {
            pPuzzle.setImage(pImage, pIndex);
        }
        mPuzzlesData.setValue(mPuzzles);
    }

    @Override
    public void onError(VolleyError pError, String pTag) {
        Log.e("VolleyItemListRetriever", pTag);
    }

    public Puzzle parseJSONResponse(JSONObject pResponse) {
        Puzzle puzzle = new Puzzle();
        try{
            String puzzleName = pResponse.getString("name");
            puzzle.setName(puzzleName);
            JSONArray puzzleArray = pResponse.getJSONArray("PictureSet");

            for (int i=0; i < puzzleArray.length(); i++) {
                String imageName = puzzleArray.getString(i);
                mMutableLiveLocalData = new MutableLiveData<>();
                String fullUrl = "https://www.goparker.com/600096/moag/images/" + imageName + ".png";
                if (!loadImageLocally(Uri.parse(fullUrl).getLastPathSegment(),  mMutableLiveLocalData, i , puzzle)) {
                    CustomPuzzleImageRequest puzzleImageRequest = new CustomPuzzleImageRequest(fullUrl, puzzle, i, this);
                    mQueue.add(puzzleImageRequest.getImageRequest());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            int pIndex = -1;
            String tileBack = pResponse.getString("TileBack");
            String fullUrl = "https://www.goparker.com/600096/moag/images/" + tileBack + ".png";
            if (!loadImageLocally(Uri.parse(fullUrl).getLastPathSegment(), mMutableLiveLocalData, pIndex, puzzle)) {
                CustomPuzzleImageRequest puzzleImageRequest = new CustomPuzzleImageRequest(fullUrl, puzzle, pIndex, this);
                mQueue.add(puzzleImageRequest.getImageRequest());
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return puzzle;
    }


    //Load image
    public boolean loadImageLocally(String pFilename, MutableLiveData<Puzzle> pPuzzleData, int pIndex, Puzzle  pPuzzle) {
        boolean loaded = false;
        ContextWrapper contextWrapper = new ContextWrapper(mAppContext);
        File directory = contextWrapper.getDir("puzzleImages", Context.MODE_PRIVATE);
        File file = new File(directory, pFilename);
        if (file.exists()) {
            FileInputStream fileInputStream = null;
            try {
                fileInputStream = new FileInputStream(file);
                Bitmap bitmap = BitmapFactory.decodeStream(fileInputStream);
                pPuzzle.setImage(bitmap, pIndex);
                pPuzzleData.setValue(pPuzzle);

                fileInputStream.close();
                loaded = true;
            }
            catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
        return loaded;
    }


}
