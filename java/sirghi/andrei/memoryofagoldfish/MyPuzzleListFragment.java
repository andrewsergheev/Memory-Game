package sirghi.andrei.memoryofagoldfish;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.ListFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

public class MyPuzzleListFragment extends ListFragment {
    int mCurCheckPosition = 0;
    boolean mSingleActivity;
    PuzzleViewModel mViewModel;
    //View mInflatedView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(getActivity()).get(PuzzleViewModel.class);

        //Create observer with updates the UI
        final Observer<List<Puzzle>> puzzleObserver = new Observer<List<Puzzle>>() {
            @Override
            public void onChanged(@Nullable final List<Puzzle> items) {
                PuzzleAdapter puzzleAdapter =  new PuzzleAdapter(getActivity(), mViewModel.getPuzzles().getValue());
                setListAdapter(puzzleAdapter);
            }
        };
        mViewModel.getPuzzles().observe(this,puzzleObserver);
    }

    @Override
    public void onActivityCreated(Bundle saveInstanceState) {
        super.onActivityCreated(saveInstanceState);

        /*setListAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_activated_1,
                DummyData.DATA_HEADINGS));*/

        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        View contentFrame = getActivity().findViewById(R.id.content);
        mSingleActivity = contentFrame != null
                && contentFrame.getVisibility() == View.VISIBLE;
        if (saveInstanceState != null) {
            //restore last state for checked position
            mCurCheckPosition = saveInstanceState.getInt("curChoice", 0);
        }

        if (mSingleActivity) {
            showContent(mCurCheckPosition);
        } else {
            getListView().setItemChecked(mCurCheckPosition, true);
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        mViewModel.selectPuzzle(position);
        showContent(position);
    }

    void showContent (int index) {
        mCurCheckPosition = index;

        if (mSingleActivity) {
            getListView().setItemChecked(index, true);

            //Check what fragment is currently shown, replace if needed
            ListPuzzleFragment content = (ListPuzzleFragment) getFragmentManager()
                    .findFragmentById(R.id.content);
            if (content == null || content.getShownIndex() != index){
                //make new fragment to show this selection

                content = ListPuzzleFragment.newInstance(index);

                //execute a transaction replacing any existing fragment
                //with this one inside the frame
                FragmentTransaction ft = getFragmentManager()
                        .beginTransaction();
                ft.replace(R.id.content, content);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }
        } else {
            //create an intent for starting the DetailsActivity
            Intent intent = new Intent();

            //explicitly set activity context and class
            //associated with the intent (context, class)
            intent.setClass(getActivity(), PuzzleActivity.class);

            //pass the current position
            intent.putExtra("index", index);

            startActivity(intent);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("curChoice", mCurCheckPosition);
    }
}
