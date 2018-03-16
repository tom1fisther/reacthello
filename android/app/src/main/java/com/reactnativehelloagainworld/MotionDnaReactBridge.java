package com.reactnativehelloagainworld;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.navisens.motiondnaapi.MotionDna;
import com.navisens.motiondnaapi.MotionDnaApplication;
import com.navisens.motiondnaapi.MotionDnaInterface;

import java.util.Map;

import static com.navisens.motiondnaapi.MotionDna.MotionType.FIDGETING;
import static com.navisens.motiondnaapi.MotionDna.MotionType.FORWARD;
import static com.navisens.motiondnaapi.MotionDna.MotionType.STATIONARY;

/**
 * Created by jamesgrantham on 3/15/18.
 */

public class MotionDnaReactBridge extends ReactContextBaseJavaModule implements MotionDnaInterface {

    MotionDnaApplication motionDnaApplication;

    public MotionDnaReactBridge(ReactApplicationContext reactContext) {
        super(reactContext);
        Looper.prepare();

    }

    @ReactMethod
    public void start()
    {
        motionDnaApplication = new MotionDnaApplication(this);
        motionDnaApplication.runMotionDna("4e7485cfe0c552a50112f33c573dca8c4e174786a59a6e407a589aa6d1d71d7a");
        motionDnaApplication.setExternalPositioningState(MotionDna.ExternalPositioningState.LOW_ACCURACY);
//        motionDnaApplication.setLocationAndHeadingGPSMag();
        motionDnaApplication.setLocationLatitudeLongitudeAndHeadingInDegrees(37.787582, -122.396627, 0);

        motionDnaApplication.setPowerMode(MotionDna.PowerConsumptionMode.PERFORMANCE);
//        motionDnaApplication.startUDPHostAndPort("45.79.101.164", "6512");
        motionDnaApplication.setBinaryFileLoggingEnabled(true);
        motionDnaApplication.setCallbackUpdateRateInMs(100);

//        motionDnaApplication.setARModeEnabled(true);
//        motionDnaApplication.setARModeEnabled(false);
//
//        motionDnaApplication.setContinuousService(true);
//        motionDnaApplication.setContinuousService(false);
        Log.v(this.getClass().getSimpleName(),"Setup MotionDNAApplication");
        if (MainApplication.context == null) {
            Log.v(this.getClass().getSimpleName(),"Context is null");

        } else {
            Log.v(this.getClass().getSimpleName(),"Context is valid");
        }
    }

    // name used to access bridge from javascript side
    @Override
    public String getName() {
        return "MotionDnaReactBridge";
    }

    private void sendEvent(ReactContext reactContext,
                           String eventName,
                           @Nullable WritableMap params) {
        reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }

    @Override
    public void receiveMotionDna(MotionDna motionDna)
    {
        String str = "Lat: " + motionDna.getLocation().globalLocation.latitude + "/Lon: " + motionDna.getLocation().globalLocation.longitude + "\n";
        str +="X:" + motionDna.getLocation().localLocation.x + "/Y:" + motionDna.getLocation().localLocation.y + "/Z:" + motionDna.getLocation().localLocation.z + "\n";
        str += "Hdg: " + motionDna.getLocation().heading +  " \n";
//            str += motionDna.getMotion().primaryMotion + "\n";
        str += MotionDnaApplication.checkSDKVersion();
        WritableMap motionDnaParameters = Arguments.createMap();
        motionDnaParameters.putString("MotionDnaString",str);
        motionDnaParameters.putDouble("location_localLocation_x",motionDna.getLocation().localLocation.x);
        motionDnaParameters.putDouble("location_localLocation_y",motionDna.getLocation().localLocation.y);
        motionDnaParameters.putDouble("location_localLocation_z",motionDna.getLocation().localLocation.z);
        motionDnaParameters.putDouble("location_localHeading",motionDna.getLocation().localHeading);
        motionDnaParameters.putString("motion_motionType",StringFromMotionType(motionDna.getMotion().motionType));

        motionDnaParameters.putString("MotionDnaString",str);
        Log.v("REACT","MotionDNA" + str);
        sendEvent(getReactApplicationContext(),"MotionDnaEvent",motionDnaParameters);

    }

    private String StringFromMotionType(MotionDna.MotionType mt) {
        switch (mt) {
            case STATIONARY:
                return "STATIONARY";
            case FIDGETING:
                return "FIDGETING";
            case FORWARD:
                return "FORWARD";
            default:
                break;
        }
        return "";
    }

    @Override
    public Context getAppContext() {
        return getReactApplicationContext();
    }

    @Override
    public PackageManager getPkgManager() {
        return MainApplication.context.getPackageManager();
    }

    @Override
    public void reportError(MotionDna.ErrorCode errorCode, String s) {

    }

    @Override
    public void receiveNetworkData(MotionDna motionDna) {
        System.out.println(motionDna.getDeviceName());
    }

    @Override
    public void receiveNetworkData(MotionDna.NetworkCode networkCode, Map<String, ? extends Object> map) {

    }
}
