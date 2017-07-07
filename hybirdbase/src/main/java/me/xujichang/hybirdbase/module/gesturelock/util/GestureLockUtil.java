package me.xujichang.hybirdbase.module.gesturelock.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

/**
 * Created by xjc on 2017/6/28.
 */
public class GestureLockUtil {

    public static boolean isPassWordSet(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("gesture", Context.MODE_PRIVATE);
        String passStr = preferences.getString("lock_password", "");
        return !TextUtils.isEmpty(passStr);
    }

    public static void clearLockInfo(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("gesture", Context.MODE_PRIVATE);
        preferences.edit().clear().apply();
    }

    public static boolean comparePassword(ArrayList<Integer> results, ArrayList<Integer> password) {
        if (!results.containsAll(password) || !password.containsAll(results)) {
            return false;
        }
        //互相包含 说明 size相同
        int size = results.size();
        for (int i = 0; i < size; i++) {
            if (!results.get(i).equals(password.get(i))) {
                return false;
            }
        }
        return true;
    }

    public static int getLockNum(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("gesture", Context.MODE_PRIVATE);
        return preferences.getInt("lock_num", 3);
    }

    public static ArrayList<Integer> getLockPassword(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("gesture", Context.MODE_PRIVATE);
        String str = preferences.getString("lock_password", "");
        if (TextUtils.isEmpty(str)) {
            return new ArrayList<Integer>();
        }
        return new Gson().fromJson(str, new TypeToken<ArrayList<Integer>>() {
        }.getType());
    }

    public static void saveLockInfo(Context context, ArrayList<Integer> result, int lockNum) {
        SharedPreferences preferences = context.getSharedPreferences("gesture", Context.MODE_PRIVATE);
        String numArray = new Gson().toJson(result);
        preferences.edit().putInt("lock_num", lockNum)
                .putString("lock_password", numArray).apply();
    }
}
