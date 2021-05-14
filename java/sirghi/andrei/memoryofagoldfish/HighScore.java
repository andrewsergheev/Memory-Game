package sirghi.andrei.memoryofagoldfish;

import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class HighScore {

    private String mUserName;
    private String mPuzzleName;
    private String mTime;
    private String mDate;
    private String mTurns;
    private String mSequence;
    ArrayList<String> mHighScoreArrayList = new ArrayList<>();
    ArrayList<String> mTurnsHighScoreArrayList = new ArrayList<>();
    ArrayList<String> mSequenceArrayList = new ArrayList<>();

    public HighScore() {

    }
/*
    public HighScore(String pName, String pPuzzleName, String pTime, String pDate, String pTurns, String pSequence) {
        setUserName(pName);
        setPuzzleName(pPuzzleName);
        setTime(pTime);
        setDate(pDate);
        setTurns(pTurns);
        setSequence(pSequence);

        mHighScoreData = new ArrayList<>();
        mHighScoreData.add(this);
    }
*/

    public void addStringToArrayList(String score)
    {
        mHighScoreArrayList.add(score);
    }

    public ArrayList<String> getHighScoreArrayList() {
        return mHighScoreArrayList;
    }

    public void addStringToTurnsArrayList(String score)
    {
        mTurnsHighScoreArrayList.add(score);
    }

    public ArrayList<String> getHighScoreTurnsArrayList() {
        return mTurnsHighScoreArrayList;
    }

    public void setSequenceArrayList(String score)
    {
        mSequenceArrayList.add(score);
    }

    public ArrayList<String> getSequenceArrayList() {
        return mSequenceArrayList;
    }

    public String getUserName() {
        return mUserName;
    }
    public void setUserName(String pUserName) {
        mUserName = pUserName;
    }

    public String getPuzzleName() {
        return mPuzzleName;
    }
    public void setPuzzleName(String pPuzzleName) {
        mPuzzleName = pPuzzleName;
    }

    public String getTime() {
        return mTime;
    }
    public void setTime(String pTime) {
        mTime = pTime;
    }

    public String getDate() {
        return mDate;
    }
    public void setDate(String pDate) {
        mDate = pDate;
    }

    public String getTurns() {
        return mTurns;
    }
    public void setTurns(String pTurns) {
        mTurns = pTurns;
    }

    public String getSequence() {
        return mSequence;
    }
    public void setSequence(String pSequence) {
        mSequence = pSequence;
    }


}
