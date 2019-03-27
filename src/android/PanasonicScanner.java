package PanasonicScanner;

import android.os.AsyncTask;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.apache.cordova.CordovaInterface;

import android.util.Log;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.panasonic.toughpad.android.api.ToughpadApi;
import com.panasonic.toughpad.android.api.ToughpadApiListener;
import com.panasonic.toughpad.android.api.barcode.BarcodeData;
import com.panasonic.toughpad.android.api.barcode.BarcodeException;
import com.panasonic.toughpad.android.api.barcode.BarcodeListener;
import com.panasonic.toughpad.android.api.barcode.BarcodeReader;
import com.panasonic.toughpad.android.api.barcode.BarcodeReaderManager;
import java.util.concurrent.TimeoutException;

/**
 * This class echoes a string called from JavaScript.
 */
public class PanasonicScanner extends CordovaPlugin  implements BarcodeListener, ToughpadApiListener{

    public static final String ARG_ITEM_ID = "item_id";
    public static final String ACTION_ITEM_ID = ARG_ITEM_ID;
    public static final String TAG = "SCANNER.PANASONIC";
    private CallbackContext callbackContextReference;
    private String message = "";

    private class EnableReaderTask extends AsyncTask<BarcodeReader, Void, Boolean> {

        @Override
        protected void onPreExecute(){
        }
        @Override
        protected Boolean doInBackground(BarcodeReader... params) {
            try {
				Log.e(TAG, "Steven in doBackground 1");
                params[0].enable(1);
                params[0].addBarcodeListener(PanasonicScanner.this);
				Log.e(TAG, "Steven in doBackground 2");
                return true;
            } catch (BarcodeException ex) {
                Log.e(TAG, ex.toString());
                return false;
            } catch (TimeoutException ex) {
                Log.e(TAG, ex.toString());
                return false;
            }
        }
        @Override
        protected void onPostExecute(Boolean result) {
        }
    }
	@Override
	public void initialize(CordovaInterface cordova, CordovaWebView webView) {
		try{
			ToughpadApi.initialize(cordova.getActivity(), this);
		}catch(Exception ex){
			Log.e(TAG, ex.toString());
		}
	}

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("activate")){
			this.activateReader(callbackContext);
			return true;
		} else if (action.equals("deactivate")) {
            this.deactivateReader(callbackContext);
            return true;
        } else if (action.equals("startScanning")) {
            this.startScanning();
            return true;
        } else if (action.equals("stopScanning")) {
            this.stopScanning();
            return true;
        }

        return false;
    }

	private void activateReader(CallbackContext callbackContext){
		try {
			Log.e(TAG, "Steven in activateReader 1");
			List<BarcodeReader> readers = BarcodeReaderManager.getBarcodeReaders();
			BarcodeReader selectedReader = readers.get(0);
            selectedReader.enable(1);
			selectedReader.addBarcodeListener(this);
			callbackContextReference = callbackContext;
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}

	}

    private void deactivateReader(CallbackContext callbackContext) {
        try {
			Log.e(TAG, "Steven in deactivateReader 1");
			List<BarcodeReader> readers = BarcodeReaderManager.getBarcodeReaders();
			BarcodeReader selectedReader = readers.get(0);
            selectedReader.disable();
            selectedReader.clearBarcodeListener();
			callbackContextReference = callbackContext;
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
    }
	
		private void startScanning(){
		try {
			Log.e(TAG, "Steven in startScanning 1");
			List<BarcodeReader> readers = BarcodeReaderManager.getBarcodeReaders();
			BarcodeReader selectedReader = readers.get(0);
            selectedReader.pressSoftwareTrigger(true);
			Log.e(TAG, "Steven in startScanning 2");
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}

	}

    private void stopScanning() {
        try {
			Log.e(TAG, "Steven in stopScanning 1");
			List<BarcodeReader> readers = BarcodeReaderManager.getBarcodeReaders();
			BarcodeReader selectedReader = readers.get(0);
            selectedReader.pressSoftwareTrigger(false);
			Log.e(TAG, "Steven in stopScanning 2");
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
    }

    @Override
    public void onRead(BarcodeReader paramBarcodeReader, BarcodeData paramBarcodeData)
    {
		  Log.e(TAG, "Steven in onRead");
		  String strBarcodeData =  paramBarcodeData.getTextData();
          String strSymbologyId = paramBarcodeData.getSymbology();
          message = strBarcodeData;
          cordova.getActivity().runOnUiThread(new Runnable() {
              public void run() {
    			PluginResult result = new PluginResult(PluginResult.Status.OK, message);
				result.setKeepCallback(true);
				callbackContextReference.sendPluginResult(result);
              }
          });
     }

    public void onApiConnected(int version) { }

    public void onApiDisconnected() { }
}
