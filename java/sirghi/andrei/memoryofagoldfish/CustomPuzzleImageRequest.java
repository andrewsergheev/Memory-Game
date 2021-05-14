package sirghi.andrei.memoryofagoldfish;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

public class CustomPuzzleImageRequest implements Response.Listener<Bitmap>, Response.ErrorListener {

    private VolleyPuzzleImageResponse mVolleyPuzzleImageResponse;
    private Puzzle mPuzzle;
    private ImageRequest mImageRequest;
    private int mIndex;
    private String mUrl;
    private Context mContext;

    private PuzzleRepository mPuzzleRepository;

    public ImageRequest getImageRequest() {return mImageRequest;}

    public CustomPuzzleImageRequest(String pUrl, Puzzle pPuzzle, int pIndex,
                                    VolleyPuzzleImageResponse pVolleyPuzzleImageResponse) {
        mVolleyPuzzleImageResponse = pVolleyPuzzleImageResponse;
        mPuzzle = pPuzzle;
        mIndex = pIndex;
        mUrl = pUrl;
        mImageRequest = new ImageRequest(
                pUrl,
                this,
                0,
                0,
                ImageView.ScaleType.CENTER_CROP,
                Bitmap.Config.RGB_565,
                this);
    }

    @Override
    public void onResponse(Bitmap pResponse) {
        mPuzzleRepository = PuzzleRepository.getInstance(mContext);
        mPuzzleRepository.saveImageLocally(pResponse, Uri.parse(mUrl).getLastPathSegment());
        mVolleyPuzzleImageResponse.onResponse(pResponse, mPuzzle, mIndex);
    }

    @Override
    public void onErrorResponse(VolleyError pError) {
        mVolleyPuzzleImageResponse.onError(pError, mPuzzle.getName());
    }



}
