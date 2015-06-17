package com.xan.TestUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.util.Log;
import android.widget.Toast;

public class NfcControl {
    private static final String TAG = MainActivity.MAIN_TAG + NfcControl.class.getSimpleName();

    private Context mContext;
    private NfcAdapter mNfcAdapter;

    public NfcControl(Context ctx) {
        mContext = ctx;

        NfcManager mNfcManager = (NfcManager) mContext.getSystemService(Context.NFC_SERVICE);
        mNfcAdapter = mNfcManager.getDefaultAdapter();
    }

    protected void getDefaultAdapter() {
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(mContext);
        nfcAdapter.isEnabled();
    }

    protected boolean isNfcSupported() {
        PackageManager pm = mContext.getPackageManager();
        if (pm != null && pm.hasSystemFeature(PackageManager.FEATURE_NFC)) {
            return true;
        }
        // Device not compatible for NFC support.
        return false;
    }

    // Use Google SDK, no Java reflection is needed.
    protected boolean isNfcEnabled() {
        if (mNfcAdapter != null) {
            return mNfcAdapter.isEnabled();
        }
        Log.d(TAG, "isNfcEnabled: mNfcAdapter is null");
        return false;
    }

    // Require "WRITE_SECURE_SETTING" permission.
    protected boolean turnOnNfc(boolean enable) {
        Object result = SystemApiHelper.runManagerAPI(mNfcAdapter, "android.nfc.NfcAdapter", (enable ? "enable" : "disable"), (Class[]) null, (Object[]) null);
        return checkResult("turnOnNfc", result);
    }

    protected boolean isNfcLocked() {
        Object result = SystemApiHelper.runServiceAPI(Context.NFC_SERVICE, "android.nfc.INfcAdapter", "isNfcLocked", (Class[]) null, (Object[]) null);
        return checkResult("isNfcLocked", result);
    }

    protected boolean lockNfc(boolean lock) {
        Class<?>[] paramTypes = { boolean.class };
        Object[] arguments = { lock };
        Object result = SystemApiHelper.runServiceAPI(Context.NFC_SERVICE, "android.nfc.INfcAdapter", "lockNfc", paramTypes, arguments);
        return checkResult("lockNfc", result);
    }

    protected boolean isMDMsupported() {
        Boolean nRet = Boolean.valueOf(false);
        DevicePolicyManager devicePolicyMgr = (DevicePolicyManager) mContext.getSystemService(Context.DEVICE_POLICY_SERVICE);
        Class<?> DevicePolicyManagerClass = DevicePolicyManager.class;
        Method is_mdmapi_supported = null;
        try {
            is_mdmapi_supported = DevicePolicyManagerClass.getDeclaredMethod("isMDMAPIPhase2Supported", (Class[]) null);
        } catch (NoSuchMethodException e) {
            Log.d(TAG, "Method DevicePolicyManager.isMDMAPIPhase2Supported() does not exist.", e);
        }

        try {
            nRet = (Boolean) is_mdmapi_supported.invoke(devicePolicyMgr,  (Object[]) null);
        } catch (IllegalAccessException e) {
            Log.d(TAG, "IllegalAccesssException by invoking Method.", e);
        } catch (InvocationTargetException e) {
            Log.d(TAG, "InvocationTargetException by invoking Method.", e);
        } catch (NullPointerException e) {
            Log.d(TAG, "NullPointerException by invoking Method.", e);
        } catch (IllegalArgumentException e) {
            Log.d(TAG, "IllegalArgumentException by invoking Method.", e);
        } catch (Exception e) {
            Log.d(TAG, "failed to invoke isMDMAPIPhase2Supported()", e);
        }
        return nRet;
    }

    public String[] getAvailableSecureElementList() {
        try {
            // NfcAdapter mNfcAdapter = NfcAdapter.getDefaultAdapter(mContext);
            Class<?> nfcAdapterClass = Class.forName("android.nfc.NfcAdapter");
            Method m = nfcAdapterClass.getDeclaredMethod("getDefaultAdapter", Context.class);
            Object nfcAdapter = m.invoke(nfcAdapterClass, mContext);

            // NxpNfcAdapter mNxpNfcAdapter = NxpNfcAdapter.getNxpNfcAdapter(mNfcAdapter);
            Class<?> nxpNfcAdapterClass = Class.forName("com.nxp.nfc.NxpNfcAdapter");
            m = nxpNfcAdapterClass.getDeclaredMethod("getNxpNfcAdapter", nfcAdapterClass);
            Object nxpNfcAdapter = m.invoke(nxpNfcAdapterClass, nfcAdapter);

            // String[] list = mNxpNfcAdapter.getAvailableSecureElementList("com.xan.TestUtils");
            Class<?>[] paramType = { String.class };
            Object[] argument = { "com.xan.TestUtils" };
            m = nxpNfcAdapterClass.getDeclaredMethod("getAvailableSecureElementList", paramType);
            return (String[]) m.invoke(nxpNfcAdapter, argument);
        } catch (Exception e) {
            Log.d(TAG, "getAvailableSecureElementList: exception.", e);
        }
        return null;
    }

    protected boolean checkResult(String apiName, Object result) {
        if (result == null) {
            Log.d(TAG, apiName + " failed.");
            Toast.makeText(mContext, apiName + " failed. Check permission.", Toast.LENGTH_LONG).show();
        }
        if (result instanceof Boolean) {
            return (Boolean) result;
        }
        return false;
    }
}
