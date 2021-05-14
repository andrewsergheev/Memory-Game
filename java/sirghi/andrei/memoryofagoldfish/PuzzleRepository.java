package sirghi.andrei.memoryofagoldfish;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class PuzzleRepository {

    private static PuzzleRepository sPuzzleRepository;
    private Context mApplicationContext;

    private MediatorLiveData<ArrayList<Puzzle>> mPuzzles;
    private  LiveData<Puzzle> mSelectedPuzzle;
    private MutableLiveData<Puzzle> mSelectedPuzzleMutableLiveData;

    private VolleyPuzzleListRetriever mRemotePuzzleList;
    private String url;
    private  ArrayList<Puzzle> puzzlesArrayList = new ArrayList<>();

    private LiveData<Tile> mTileData;

    private JSONObject mHighScoreJSONObject;
    HighScore mHighScore;
    private HighScore highScore = new HighScore();

    private PuzzleRepository(Context pApplicationContext) {
        this.mApplicationContext = pApplicationContext;
        //The URL passed for object
        url = "https://www.goparker.com/600096/moag/index.json";
        mRemotePuzzleList = new VolleyPuzzleListRetriever(url, pApplicationContext);
        //For the Tile class
        Tile tile = new Tile();
        mTileData = tile.getTileData();
    }

    public static PuzzleRepository getInstance(Context pApplicationContext) {
        if (sPuzzleRepository == null) {
            sPuzzleRepository = new PuzzleRepository(pApplicationContext);
        }
        return sPuzzleRepository;
    }



    public LiveData<ArrayList<Puzzle>> getPuzzles() {
        if (mPuzzles == null) {
            mPuzzles = new MediatorLiveData<>();
            ContextWrapper contextWrapper = new ContextWrapper(mApplicationContext);
            File file = contextWrapper.getFilesDir();
            File[] puzzleFiles = file.listFiles();
            LiveData<ArrayList<Puzzle>> remoteData = mRemotePuzzleList.getPuzzles();
            LiveData<ArrayList<Puzzle>> localData = new LiveData<ArrayList<Puzzle>>() {};

            for(int i = 0; i < puzzleFiles.length; i++)
            {
                localData = loadIndexLocally(puzzleFiles[i].getName());
            }
            mPuzzles.addSource(remoteData, value-> mPuzzles.setValue(value));
            mPuzzles.addSource(localData, value-> mPuzzles.setValue(value));
        }


        return mPuzzles;
    }

    public LiveData<Puzzle> getPuzzle(final int pPuzzleIndex) {
        LiveData<Puzzle> transformedPuzzle = Transformations.switchMap(mPuzzles, puzzles -> {
            if (mSelectedPuzzleMutableLiveData ==  null) {
                mSelectedPuzzleMutableLiveData = new MutableLiveData<>();
            }
            if (puzzles.size() > 0) {
                Puzzle puzzle = puzzles.get(pPuzzleIndex);
                mSelectedPuzzleMutableLiveData.setValue(puzzle);
            }

            return mSelectedPuzzleMutableLiveData;
        });
        mSelectedPuzzle =  transformedPuzzle;


        return  mSelectedPuzzle;
    }

    public LiveData<Puzzle> getSelectedPuzzle() {
        return mSelectedPuzzle;
    }

    public LiveData<Tile> getTile() {
        return mTileData;
    }

    //Saving index locally
    public void saveIndexLocally(JSONObject pIndexObject, String pFilename) {
        ContextWrapper contextWrapper = new ContextWrapper(mApplicationContext);
        OutputStreamWriter outputStreamWriter = null;
        try {
            outputStreamWriter = new OutputStreamWriter(
                    contextWrapper.openFileOutput(pFilename, Context.MODE_PRIVATE));
            outputStreamWriter.write(pIndexObject.toString());
            outputStreamWriter.flush();
            outputStreamWriter.close();
        }
        catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    //Load Index
    private LiveData<ArrayList<Puzzle>> loadIndexLocally(String pFilename) {
        JSONObject indexObject = null;
        MutableLiveData<ArrayList<Puzzle>> mutablePuzzles = new MutableLiveData<ArrayList<Puzzle>>();
        try {
            InputStream inputStream = mApplicationContext.openFileInput(pFilename);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder =  new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                String builtString = stringBuilder.toString();
                indexObject = new JSONObject(builtString);
            }
        }
        catch (FileNotFoundException e) {
            Log.e("JSONLoading", "File not found: " + e.toString());
        }
        catch (IOException e) {
            Log.e("JSONLoading", "Can not read file: " + e.toString());
        }
        catch(JSONException e) {
            Log.e("JSONLoading", "json error: " + e.toString());
        }
        if (indexObject != null) {
            puzzlesArrayList.add(mRemotePuzzleList.parseJSONResponse(indexObject));
            mutablePuzzles.setValue(puzzlesArrayList);
        }
        return mutablePuzzles;
    }

    //Saving images
    public void saveImageLocally(Bitmap pBitmap, String pFilename) {
        ContextWrapper contextWrapper = new ContextWrapper(mApplicationContext);
        File directory = contextWrapper.getDir("puzzleImages", Context.MODE_PRIVATE);
        File file =  new File(directory, pFilename);
        if (!file.exists()) {
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(file);
                pBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
            }
            catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
    }

    //Save High score
    public void setHighScore(String userName, String puzzleName, String time, String dateOdScore, String turns, String sequence) throws JSONException {
        JSONObject highScoreJSONObject = new JSONObject();
        highScoreJSONObject.put("userName", userName);
        highScoreJSONObject.put("puzzleName", puzzleName);
        highScoreJSONObject.put("time", time);
        highScoreJSONObject.put("date", dateOdScore);
        highScoreJSONObject.put("turns", turns);
        highScoreJSONObject.put("sequence", sequence);

        setHighScoreJSONObject(highScoreJSONObject);
        savingHighScore(userName);

    }

    public JSONObject getHighScoreJSONObject() {
        return mHighScoreJSONObject;
    }
    public void setHighScoreJSONObject(JSONObject jsonObject) {
        mHighScoreJSONObject = jsonObject;
    }

    private void savingHighScore(String name) {
        ContextWrapper contextWrapper = new ContextWrapper(mApplicationContext);
        File directory = contextWrapper.getDir("highScore", Context.MODE_PRIVATE);
        File file =  new File(directory, name + ".json");

        OutputStreamWriter outputStreamWriter = null;
        try {
            outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file));
            JSONObject jsonObject = getHighScoreJSONObject();
            outputStreamWriter.write(jsonObject.toString());
            outputStreamWriter.flush();
            outputStreamWriter.close();
        }
        catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    public void loadHighScore() {
        //Clearing the array lists to show the updated content
        highScore.mHighScoreArrayList.clear();
        highScore.mTurnsHighScoreArrayList.clear();
        highScore.mSequenceArrayList.clear();

        JSONObject indexObject = null;

        try {
            ContextWrapper contextWrapper = new ContextWrapper(mApplicationContext);
            File directory = contextWrapper.getDir("highScore", Context.MODE_PRIVATE);
            File[] highScoreFile = directory.listFiles();
            String fileName = "";
            for(int i = 0; i < highScoreFile.length; i++)
            {
                fileName = highScoreFile[i].getName();
                File file = new File(directory, fileName);

                FileInputStream fileInputStream = null;
                fileInputStream = new FileInputStream(file);
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder =  new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }
                fileInputStream.close();
                String builtString = stringBuilder.toString();
                indexObject = new JSONObject(builtString);

                if (indexObject != null) {
                    parseHighScoreJSONResponse(indexObject);
                }

            }

        }
        catch (FileNotFoundException e) {
            Log.e("JSONLoading", "File not found: " + e.toString());
        }
        catch (IOException e) {
            Log.e("JSONLoading", "Can not read file: " + e.toString());
        }
        catch(JSONException e) {
            Log.e("JSONLoading", "json error: " + e.toString());
        }
    }

    public void parseHighScoreJSONResponse(JSONObject pResponse) {

        try {
            String userName = pResponse.getString("userName");
            highScore.setUserName(userName);

            String puzzleName = pResponse.getString("puzzleName");
            highScore.setPuzzleName(puzzleName);

            String time = pResponse.getString("time");
            highScore.setTime(time);

            String date = pResponse.getString("date");
            highScore.setDate(date);

            String turns = pResponse.getString("turns");
            highScore.setTurns(turns);

            String sequence = pResponse.getString("sequence");
            highScore.setSequence(sequence);

            String timeResult = userName + ": " + time + " seconds " + date + " in " + puzzleName;
            highScore.addStringToArrayList(timeResult);

            String turnsResult = userName + ": " + turns + " turns " + date + " in " + puzzleName;
            highScore.addStringToTurnsArrayList(turnsResult);

            String sequenceResult = userName + ": " + sequence + " matches " + date + " in " + puzzleName;
            highScore.setSequenceArrayList(sequenceResult);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        setHighScoreObject(highScore);
    }

    public void setHighScoreObject(HighScore pHighScore)
    {
        mHighScore = pHighScore;
    }

    public  HighScore getHighScoreObject()
    {
        return mHighScore;
    }

}
