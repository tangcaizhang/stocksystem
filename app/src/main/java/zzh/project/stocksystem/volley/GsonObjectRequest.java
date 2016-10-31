package zzh.project.stocksystem.volley;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * 体会Response.Listener的返回类型，避免多次转换
 */
public class GsonObjectRequest extends JsonRequest<JsonObject> {

    public GsonObjectRequest(int method, String url, String requestBody,
                             Response.Listener<JsonObject> listener, Response.ErrorListener errorListener) {
        super(method, url, requestBody, listener,
                errorListener);
    }


    public GsonObjectRequest(String url, Response.Listener<JsonObject> listener, Response.ErrorListener errorListener) {
        super(Method.GET, url, null, listener, errorListener);
    }


    public GsonObjectRequest(int method, String url, Response.Listener<JsonObject> listener, Response.ErrorListener errorListener) {
        super(method, url, null, listener, errorListener);
    }


    public GsonObjectRequest(int method, String url, JSONObject jsonRequest,
                             Response.Listener<JsonObject> listener, Response.ErrorListener errorListener) {
        super(method, url, (jsonRequest == null) ? null : jsonRequest.toString(), listener,
                errorListener);
    }


    public GsonObjectRequest(String url, JSONObject jsonRequest, Response.Listener<JsonObject> listener,
                             Response.ErrorListener errorListener) {
        this(jsonRequest == null ? Method.GET : Method.POST, url, jsonRequest,
                listener, errorListener);
    }

    @Override
    protected Response<JsonObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
            return Response.success(new JsonParser().parse(jsonString).getAsJsonObject(),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }
}
