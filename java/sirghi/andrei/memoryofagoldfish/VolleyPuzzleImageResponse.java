package sirghi.andrei.memoryofagoldfish;

import android.graphics.Bitmap;

import com.android.volley.VolleyError;

public interface VolleyPuzzleImageResponse {

    void onResponse(Bitmap pImage, Puzzle pPuzzle, int pIndex);

    void onError(VolleyError error, String tag);

}
