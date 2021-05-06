package org.paul.lib.mgr;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class SharedManager {

    private Context context;
    private static final String SPF_LABLE = "dns_spf";

    private static class Holder {
        @SuppressLint("StaticFieldLeak")
        private static final SharedManager INSTANCE = new SharedManager();
    }

    private SharedManager() {
    }

    public static SharedManager getInstance(Context context) {
        SharedManager instance = Holder.INSTANCE;
        if (null == instance.context) {
            if (null == context) {
                throw new IllegalArgumentException("context = null");
            }
            instance.context = context;
        }
        return instance;
    }

    public void write(String key, Number value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SPF_LABLE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        if (value instanceof Integer) {
            edit.putInt(key, (Integer) value);
        }
        if (value instanceof Long) {
            edit.putLong(key, (Long) value);
        }
        if (value instanceof Float) {
            edit.putFloat(key, (Float) value);
        }
        edit.apply();
    }

    public void write(String key, boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SPF_LABLE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putBoolean(key, value);
        edit.apply();
    }

    public void write(String key, String... arg) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SPF_LABLE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        int length = arg.length;
        if (length == 1) {
            edit.putString(key, arg[0]);
        }
        if (length > 1) {
            edit.putStringSet(key, new HashSet<String>(Arrays.asList(arg)));
        }
        edit.apply();
    }

    public Number read(String key, Number defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SPF_LABLE, Context.MODE_PRIVATE);
        Number result = null;
        if (defaultValue instanceof Integer) {
            result = sharedPreferences.getInt(key, (Integer) defaultValue);
        }
        if (defaultValue instanceof Long) {
            result = sharedPreferences.getLong(key, (Long) defaultValue);
        }
        if (defaultValue instanceof Float) {
            result = sharedPreferences.getFloat(key, (Float) defaultValue);
        }
        return result;
    }

    public boolean read(String key, boolean defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SPF_LABLE, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    String read(String key, String defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SPF_LABLE, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, defaultValue);
    }

    public Set<String> read(String key, Set<String> defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SPF_LABLE, Context.MODE_PRIVATE);
        return sharedPreferences.getStringSet(key, defaultValue);
    }

}
