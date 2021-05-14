package sirghi.andrei.memoryofagoldfish;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class PuzzleAdapter extends ArrayAdapter<Puzzle> {
    private Context mContext;
    private List<Puzzle> mPuzzleList;

    public PuzzleAdapter(@NonNull Context pContext, ArrayList<Puzzle> pList) {
        super (pContext, 0, pList);
        mContext = pContext;
        mPuzzleList = pList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listPuzzle = convertView;
        if (listPuzzle == null) {
            listPuzzle = LayoutInflater.from(mContext).inflate(R.layout.puzzle_layout, parent, false);
        }else {
            listPuzzle = (View) convertView;
        }
        Puzzle currentPuzzle = mPuzzleList.get(position);

        ImageView image = (ImageView) listPuzzle.findViewById(R.id.imageView_tileBackImage);
        image.setImageBitmap(currentPuzzle.getTileBackImage());
        image.setLayoutParams(new LinearLayout.LayoutParams(200, 200));

        TextView name = (TextView) listPuzzle.findViewById(R.id.textView_PuzzleName);
        name.setText(currentPuzzle.getName());
        name.setTextColor(Color.WHITE);
        
        return listPuzzle;
    }

}

