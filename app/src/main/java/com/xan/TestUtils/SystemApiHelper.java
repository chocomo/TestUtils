package com.xan.TestUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class SystemApiHelper {
    private static final String TAG = MainActivity.MAIN_TAG + SystemApiHelper.class.getSimpleName();

    // No need to initialize.  Access statically.
    private SystemApiHelper() {
        // Do nothing for now.
    }

    /**
     * Use Java reflection to call hidden framework API.
     *
     * Usage example:
     *         boolean result = (Boolean) runManagerAPI(mNfcAdapter, "android.nfc.NfcAdapter", (enable ? "enable" : "disable"), (Class[]) null, (Object[]) null);
     *
     * @param managerName
     * @param apiName
     * @param paramTypes
     * @param arguments
     * @return
     */
    protected static Object runManagerAPI(Object manager, String managerName, String apiName, Class<?>[] paramTypes, Object[] arguments) {
        try {
            Class<?> managerClass = Class.forName(managerName);
            Method m = managerClass.getDeclaredMethod(apiName, paramTypes);
            return m.invoke(manager, (Object[]) null);
        } catch (Exception e) {
            Log.d(TAG, "runManagerAPI: exception.", e);
        }
        return null;
    }

    /**
     * Use Java reflection to call service API without using manager.
     * Please be aware of the permission check inside Android framework.
     *
     * Usage example:
     *     boolean result = (Boolean) runServiceAPI(Context.NFC_SERVICE, "android.nfc.INfcAdapter", "isNfcLocked", (Class[]) null, (Object[]) null);
     *
     * Equivalent system call:
     *     INfcAdapter adapter = (INfcAdapter) INfcAdapter.Stub.asInterface(ServiceManager.getService(Context.NFC_SERVICE));
     *     boolean result = (Boolean) adapter.isNfcLocked();
     *
     * @param serviceName This is used as arguments when calling SystemManager.getService(serviceName);
     * @param interfaceName This is used to cast the returned object from getService(String) function call to corresponding type.
     * @param apiName The target API function.
     * @param paramTypes The parameter types of target API function.
     * @param arguments The values for calling target API function.
     * @return The return value of target API function in Object type.
     */
    protected static Object runServiceAPI(String serviceName, String interfaceName, String apiName, Class<?>[] paramTypes, Object[] arguments) {
        try {
            Class<?> serviceManagerClass = Class.forName("android.os.ServiceManager");
            Class<?> serviceManagerNativeClass = Class.forName("android.os.ServiceManagerNative");
            Method getService = serviceManagerClass.getMethod("getService", String.class);
            Method tempInterfaceMethod = serviceManagerNativeClass.getMethod("asInterface", IBinder.class);
            Binder tmpBinder = new Binder();
            tmpBinder.attachInterface(null, "fake");
            Object serviceManagerObject = tempInterfaceMethod.invoke(null, tmpBinder);

            Class<?> nfcClass = Class.forName(interfaceName);
            Class<?> nfcStubClass = nfcClass.getClasses()[0];
            IBinder retbinder = (IBinder) getService.invoke(serviceManagerObject, serviceName);
            Method serviceMethod = nfcStubClass.getMethod("asInterface", IBinder.class);
            Object nfcObject = serviceMethod.invoke(null, retbinder);

            Method nfcMethod = nfcClass.getDeclaredMethod(apiName, paramTypes);
            return nfcMethod.invoke(nfcObject, arguments);
        } catch (IllegalAccessException e) {
            Log.d(TAG, "IllegalAccesssException by invoking API " + interfaceName + "." + apiName, e);
        } catch (InvocationTargetException e) {
            Log.d(TAG, "InvocationTargetException by invoking API " + interfaceName + "." + apiName, e);
        } catch (NullPointerException e) {
            Log.d(TAG, "NullPointerException by invoking Method.", e);
        } catch (IllegalArgumentException e) {
            Log.d(TAG, "IllegalArgumentException by invoking Method.", e);
        } catch (Exception e) {
            Log.d(TAG, "runINfcAdapterMethod failed.", e);
        }
        return null;
    }
}
