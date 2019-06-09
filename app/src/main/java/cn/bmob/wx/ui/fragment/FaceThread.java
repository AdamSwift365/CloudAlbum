package cn.bmob.wx.ui.fragment;

import android.util.Log;

import com.megvii.cloud.http.CommonOperate;
import com.megvii.cloud.http.Response;

import org.json.JSONObject;

import u.aly.S;

import static java.lang.Double.valueOf;

public class FaceThread extends Thread {
    private static final String TAG = "FaceThread";
    private byte[] fileByte1;
    private byte[] fileByte2;
    private double confidence;

    private static String apiKey = "F8f5JClgy6gGSa0CsHu2AVFORveGJwlb";//api_key
    private static String apiSecret = "WbnPImebUcgZZIcKBCgG4_MB-TfyPjHR";//api_secret

    public FaceThread(byte[] fileByte1, byte[] fileByte2) {
        this.fileByte1 = fileByte1;
        this.fileByte2 = fileByte2;
        this.confidence = 0;
    }

    public boolean getRes() {
        while(confidence == 0) {

        }
        if (confidence > 80)
            return true;
        return false;
    }

    public void run() {
        Log.i(TAG, "run() called");
        CommonOperate commonOperate = new CommonOperate(apiKey, apiSecret, false);
        try{
            Response compare = commonOperate.compare(null, null, fileByte1, null,
                    null, null, fileByte2, null);
            String result = new String(compare.getContent());
            // Log.i(TAG, "res: " + result);
            JSONObject jsonObject = new JSONObject(result);
            confidence = valueOf(jsonObject.getString("confidence"));
            Log.i(TAG, "confidence: " + confidence);


        }catch (Exception e){
            Log.i(TAG, "startCompare: " +e.toString());
        }
    }
}
