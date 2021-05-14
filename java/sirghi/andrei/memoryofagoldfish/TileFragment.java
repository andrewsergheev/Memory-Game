package sirghi.andrei.memoryofagoldfish;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.view.GestureDetectorCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import org.json.JSONException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import static sirghi.andrei.memoryofagoldfish.AcceptSSLCerts.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TileFragment extends Fragment implements GestureDetector.OnGestureListener{

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_INDEX = "index";

    private int mIndex;
    private GameViewModel mGameViewModel;
    private View mInflatedView;
    private Animator mFlipInAnimator;
    private Animator mFlipOutAnimator;
    private Animation mSpinAnimator;
    //private int mAnimationCompleteCount;
    private GestureDetectorCompat mDetector;
    private Random random = new Random();
    LiveData<Tile> tempTile;
    private ImageView frontImageView;
    private ImageView backImageView;
    public int getShownIndex() {
        return mIndex;
    }

    //HighScoreTime variables
    String userName ="";
    String time ="";
    String puzzleName = "";
    String start_Date = "";
    String end_Date = "";
    String dateOfScore = "";

    int sumOfTurns = 0;
    static int mLongestSequence;
    static int sumOfSequence;
    ArrayList<HighScore> mHighScoreArray;

    int counter_Time = 0;
    Date start_Time;
    Date end_Time;

    public TileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TileFragment.
     */

    public static TileFragment newInstance(int index) {
        TileFragment fragment = new TileFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_INDEX, index);
        fragment.setArguments(args);
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("ResourceType")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        getTime();
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mIndex = getArguments().getInt(ARG_INDEX);
        }
        mGameViewModel = new ViewModelProvider(getActivity()).get(GameViewModel.class);
        mFlipInAnimator = AnimatorInflater.loadAnimator(getActivity(), R.anim.flip_in);
        mFlipOutAnimator = AnimatorInflater.loadAnimator(getActivity(), R.anim.flip_out);
        mSpinAnimator = AnimationUtils.loadAnimation(getActivity(), R.anim.spin_image_animation_complete);
        mDetector = new GestureDetectorCompat(getContext(),this);
        addAnimationListeners();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mInflatedView = inflater.inflate(R.layout.fragment_tile, container, false);

        FrameLayout frameLayout = mInflatedView.findViewById(R.id.container);

        // this is where images are generated
        frontImageView = mInflatedView.findViewById(R.id.front);
        backImageView = mInflatedView.findViewById(R.id.back);

        frontImageView.setImageBitmap(mGameViewModel.gameLiveData().getValue().getTileArrayList().get(mIndex).getFront());
        backImageView.setImageBitmap(mGameViewModel.gameLiveData().getValue().getTileArrayList().get(mIndex).getBack());

        frontImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        frontImageView.setLayoutParams(new FrameLayout.LayoutParams(220, 198));

        backImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        backImageView.setLayoutParams(new FrameLayout.LayoutParams(220, 198));


        View.OnTouchListener touchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // pass the events to the gesture detector
                // a return value of true means the detector is handling it
                // false means the detector didn't recognise the event
                //return mDetector.onTouchEvent(event);
                //mGameViewModel.flipTile(mIndex);
                return mDetector.onTouchEvent(event);
            }
        };
        frameLayout.setOnTouchListener(touchListener);
        final Observer<Tile> tileObserver = new Observer<Tile>() {
            @Override
            public void onChanged(@Nullable final Tile tile) {

                Log.i(this.getClass().getSimpleName() + "Observer", "Generating Tile: " + mIndex);

                // check to see the game state.
                if (tile.isFound()) {
                    frontImageView.startAnimation(mSpinAnimator);
                    backImageView.startAnimation(mSpinAnimator);
                } else if (!tile.isFound()){
                    flipAnimation(tile);
                }
            }

        };
        LiveData<Tile> tileData = mGameViewModel.tileIndex(mIndex);
        tileData.observe(getViewLifecycleOwner(), tileObserver);
        return mInflatedView;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void animationComplete() {
        if(mGameViewModel.mGame.getValue().getAnimationCompleteCount() == 2)
        {
            mGameViewModel.mGame.getValue().resetAnimationCompleteCount();

            ArrayList<Tile> tiles =  new ArrayList<>();
            tiles = mGameViewModel.mGame.getValue().getSecondTileArray();
            Tile tile1 = tiles.get(0);
            Tile tile2 = tiles.get(1);

            if(tile1.getId() != tile2.getId())
            {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tile1.setHidden(false);
                        tile2.setHidden(false);
                        mGameViewModel.mGame.getValue().resetTileToArrayList();
                    }
                }, 500);

                if(mLongestSequence > sumOfSequence) {
                    sumOfSequence = mLongestSequence;
                }
                mLongestSequence = 0;
            }

            if(tile1.getId() == tile2.getId())
            {
                tile1.setHidden(true);
                tile2.setHidden(true);
                tile1.setIsFound(true);
                tile2.setIsFound(true);
                mGameViewModel.mGame.getValue().setTilePairsFound();
                mGameViewModel.mGame.getValue().resetTileToArrayList();

                mLongestSequence++;
                sumOfSequence = mLongestSequence;
            }

            if(mGameViewModel.mGame.getValue().getTilePairsFound() == 20)
            {
                mGameViewModel.mGame.getValue().setIsGameFinished(true);
                gameWon();
            }
        }
    }

    private void flipAnimation(Tile tile) {
        if(tile.isHidden())
        {
           mFlipInAnimator.setTarget(frontImageView);
           mFlipOutAnimator.setTarget(backImageView);
        } else {
            mFlipInAnimator.setTarget(backImageView);
            mFlipOutAnimator.setTarget(frontImageView);
        }
        mFlipInAnimator.start();
        mFlipOutAnimator.start();
    }

    private void addAnimationListeners() {
        mFlipInAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onAnimationEnd(Animator animation) {
                if(mGameViewModel.mGame.getValue().getAnimationCompleteCount() == 2) {
                    mGameViewModel.mGame.getValue().setTurnsCount();
                    animationComplete();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mFlipOutAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    @Override
    public boolean onDown(MotionEvent e) {

        if(mGameViewModel.tileIndex(mIndex).getValue().isHidden() == true) //might need to change newGame() to gameLiveData()
        {
            return false;
        }
        else {
            mGameViewModel.getGame().getValue().setAnimationCompleteCount();
            mGameViewModel.getGame().getValue().addTileToArrayList(mGameViewModel.tileIndex(mIndex).getValue());
            mGameViewModel.flipTile(mIndex);
            return true;
        }
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    //For HighScoreTime to get Time
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getTime(){
        counter_Time++;
        if (counter_Time == 1){
            start_Time = Calendar.getInstance().getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("hh.mm.ss");
            start_Date = formatter.format(start_Time);
        }
        if (counter_Time == 2){
            end_Time = Calendar.getInstance().getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("hh.mm.ss");
            end_Date = formatter.format(end_Time);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateOfScore = dateFormat.format(end_Time);

            try {
                Date startDate = formatter.parse(start_Date);
                Date endDate = formatter.parse(end_Date);
                long temp = (endDate.getTime() - startDate.getTime()) / 1000;
                time += temp;
            }
            catch (Exception e) {
                throw new IllegalArgumentException();
            }
            counter_Time = 0;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private boolean gameWon() {
        getTime();
        sumOfTurns = mGameViewModel.mGame.getValue().getTurnsCount();
        String totalTurns = Integer.toString(sumOfTurns);
        String totalSequence = Integer.toString(sumOfSequence);

        boolean gameWon = mGameViewModel.getGame().getValue().getIsGameFinished();
        if(gameWon == true) {
            Log.i(TAG, "You Won!");
            puzzleName = mGameViewModel.getGame().getValue().getPuzzleName();

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("You Won! Your Time is - " + time + " Seconds " + "\nPlease Type in Your Name");

            View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.text_name,(ViewGroup) getView(), false);
            final EditText nameView = (EditText) viewInflated.findViewById(R.id.nameView);
            builder.setView(viewInflated);

            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    userName = nameView.getText().toString();

                    try {
                        mGameViewModel.setHighScore(userName, puzzleName, time, dateOfScore, totalTurns, totalSequence);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    Intent main = new Intent(getActivity(), MainActivity.class);
                    main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(main);

                    dialog.dismiss();

                }
            });
            builder.show();
        }
        return gameWon;
    }
}