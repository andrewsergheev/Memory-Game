package sirghi.andrei.memoryofagoldfish;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListPuzzleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListPuzzleFragment extends Fragment {

    private static final String ARG_INDEX = "index";

    private int mIndex;
    PuzzleViewModel mViewModel;
    View mInflatedView;
    private PuzzleRepository mPuzzleRepository;

    public int getShownIndex() {

        return mIndex;
    }
    public ListPuzzleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     */

    public static ListPuzzleFragment newInstance(int index) {
        ListPuzzleFragment fragment = new ListPuzzleFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_INDEX, index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mIndex = getArguments().getInt(ARG_INDEX);
        }
        mViewModel = new ViewModelProvider(getActivity()).get(PuzzleViewModel.class);
        mViewModel.selectPuzzle(mIndex);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i(this.getClass().getSimpleName() + " Observer", "onCreateView");
        mInflatedView = inflater.inflate(R.layout.fragment_list_puzzle, container, false);

        Button button = (Button) mInflatedView.findViewById(R.id.startButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GameActivity.class);
                startActivity(intent);
                //mViewModel.setHighScoreObject();
            }
        });

        //Create the observer which updates the UI
        final Observer<Puzzle> puzzleObserver = new Observer<Puzzle>() {
            @Override
            public void onChanged(@Nullable final Puzzle puzzle) {
                GridLayout gridLayout = (GridLayout) mInflatedView.findViewById(R.id.gridlayout);
                gridLayout.removeAllViews();
                for (int i = 0; i < puzzle.imageCount(); i++) {
                    ImageView imageView = new ImageView(gridLayout.getContext());
                    imageView.setImageBitmap(puzzle.getImage(i));
                    imageView.setLayoutParams(new LinearLayout.LayoutParams(220, 220));
                    gridLayout.addView(imageView);

                }
/*
                ImageView image = (ImageView) mInflatedView.findViewById(R.id.imageView_image);
                image.setImageBitmap(puzzle.getTileBackImage());
                image.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
*/
                TextView text = (TextView) mInflatedView.findViewById(R.id.listPuzzleTextView);
                text.setText(puzzle.getName());
            }
        };

        //Observer the LiveData, passing in this activity as the LifecycleOwner and the observer
        mViewModel.getSelectedPuzzle().observe(getViewLifecycleOwner(), puzzleObserver);
        return mInflatedView;
    }
}