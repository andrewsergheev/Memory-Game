package sirghi.andrei.memoryofagoldfish;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

public interface VolleyJSONObjectResponse {

    void onResponse(JSONObject pObject, String pTag) throws JSONException;

    void onError(VolleyError pError, String pTag);
}
