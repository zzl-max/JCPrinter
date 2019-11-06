package cordova.plugin.JCPrinter;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.gengcon.www.jcapi.api.JCAPI;


/**
 * This class echoes a string called from JavaScript.
 */
public class JCPrinter extends CordovaPlugin {

    	/**
	 * B3S系列打印机打印接口
	 */
    JCAPI mJCAPI = null;
    	/**
	 * B3S系列打印机回调接口
	 */
    JCAPI.CallBack mJCAPICallback = null;
    	/**
	 * B3S系列
	 */
    private static final String B3S = "B3S";
    	/**
	 * 连接设备类型
	 */
	private static final int DISCONNECTED_TYPE = -1;
	private static final int B11_CONNECTED_TYPE = 1;
    private static final int B3S_CONNECTED_TYPE = 2;
    	/**
	 * -1 未连接打印机 .
	 */
	private static int mConnectPrinterType = DISCONNECTED_TYPE;
    
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("coolMethod")) {
            String message = args.getString(0);
            this.coolMethod(message, callbackContext);
            return true;
        }
        else if(action.equals("getInstance")){//开启打印任务
            String  printerdata = args.getJSONObject(0).getString("printData"); 
            String  type = args.getJSONObject(0).getString("type"); 
            String  Id=args.getJSONObject(0).getString("address"); 
            if (printerdata != null && printerdata.length() > 0) { 
                try{
                    mJCAPICallback = new JCAPI.CallBack() {
                        @Override
                        public void onConnectSuccess() {
            
                        }
            
                        @Override
                        public void onConnectFail() {
            
                        }
            
                        @Override
                        public void disConnect() {
            
                        }
            
                        @Override
                        public void onAbnormalResponse(int i) {
            
                        }
            
                        @Override
                        public void electricityChange(int i) {
            
                        }
                    };
                 //   Intent intent=new Intent().setClass(cordova.getActivity(),CordovaActivity.class);
                 Context context=this.cordova.getActivity().getApplicationContext();
                 //连接打印机
                 mJCAPI = JCAPI.getInstance(context, mJCAPICallback);
                 boolean ifOpen= mJCAPI.openPrinterByAddress(Id);
                    mJCAPI.startJob(30, 40, 0, 1);
                     mJCAPI.startPage();
                     boolean a= false ;
                   //  mJCAPI.drawQrCode(printerdata, 4, 4, 15, 0);
                     if(type.equals("text")){
                        mJCAPI.drawText(printerdata, 2, 10, 40, 10, 2.5, 0, 1F, 0, 0, 0, true, "");
                        a=true;
                    }
                     else if(type.equals("barcode")){
                        mJCAPI.drawBarCode(printerdata, 20 , 2, 2, 46, 20, 2.5, 0, 0); 
                     }
                     else if(type.equals("qrcode")){
                        mJCAPI.drawQrCode(printerdata, 4, 4, 15, 0);
                     }
                     else if(type.equals("bitmap")){
                        a=true;
                        String str="data:image/png;base64,"; 
                        printerdata=printerdata.replace(str, "");
                        //JSONObject dataobj=JSONObject.fromObject(printerdata);
                       // printerdata= dataobj.getString("data");
                        Bitmap bitmap = null;
                        byte[] bitmapArray;
                        bitmapArray = Base64.decode(printerdata.getBytes(), Base64.DEFAULT);
                        bitmap =BitmapFactory.decodeByteArray(bitmapArray,0,bitmapArray.length);
                        mJCAPI.drawBitmap(bitmap, 0, 0,39,40,0);
                     }        
                    mJCAPI.endPage();
                boolean isPrintSuccess = mJCAPI.commitJob(1);
               boolean isEnd=mJCAPI.endJob();
               int electricity=mJCAPI.getElectricity();
              // var  DeviceSn=mJCAPI.getDeviceSn();
               callbackContext.success("连接:"+ifOpen+"提交:"+isPrintSuccess+"结束打印:"+isEnd+"当前电量:"+electricity+"printerdata:"+printerdata+"type:"+type+"a:"+a);//+"序列号:"+DeviceSn);
              //callbackContext.success("printerdata:"+printerdata);
            }
            catch (Exception e)
            {
                  callbackContext.error(printerdata);
            }
        }

          //  var mApi= jcapi.getInstance(boothactivity,callbackContext);  
            else {
                callbackContext.error("Expected one non-empty string argument.null");
            }
             return true;
        } 
    //    else if(action.equals("drawText")){//打印文本
    //     string message=args.getString(0);
    //     double x=args.getString(1);
    //     double Y=args.getString(2);
    //     double width=args.getString(3);
    //     double height=args.getString(4);
    //     double fontSize=args.getString(5);
    //     double letterSpacing=args.getString(6);
    //     float lineSpacing=args.getString(7);
    //     int fontStyle=args.getString(8);
    //     int align=args.getString(9);
    //     int rotate=args.getString(10);
    //     boolean isWrap=args.getString(11);
    //     String fontFamilyPath=args.getString(12);
    //     jcapi.drawText(message,x,Y,width,height,fontSize,letterSpacing,lineSpacing,fontStyle,align,rotate,isWrap,fontFamilyPath);
    //     callbackContext.success("Successful");
    //     return true;
    //     }
    // }
    //     }
        return false;
    }

    private void coolMethod(String message, CallbackContext callbackContext) {
        if (message != null && message.length() > 0) {
            callbackContext.success(message);
        } else {
            callbackContext.error("Expected one non-empty string argument.null");
        }
    }
}
