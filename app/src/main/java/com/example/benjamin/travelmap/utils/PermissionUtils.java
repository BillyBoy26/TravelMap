package com.example.benjamin.travelmap.utils;

import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

public class PermissionUtils {

    public static void requestPermission(AppCompatActivity activity, int requestId,
                                         String permission, boolean finishActivity) {
//        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
//            // Display a dialog with rationale.
//            PermissionUtils.RationaleDialog.newInstance(requestId, finishActivity)
//                    .show(activity.getSupportFragmentManager(), "dialog");
//        } else {
//            // Location permission has not been granted yet, request it.
            ActivityCompat.requestPermissions(activity, new String[]{permission}, requestId);

//        }
    }

    /**
     * Checks if the result contains a  result for a
     * permission from a runtime permissions request.
     *
     * @see android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback
     */
    public static boolean isPermissionGranted(String[] grantPermissions, int[] grantResults,
                                              String permission) {
        for (int i = 0; i < grantPermissions.length; i++) {
            if (permission.equals(grantPermissions[i])) {
                return grantResults[i] == PackageManager.PERMISSION_GRANTED;
            }
        }
        return false;
    }
}
