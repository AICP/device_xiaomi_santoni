/*
* Copyright (C) 2020 The Android Ice Cold Project
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 2 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>.
*
*/
package com.aicp.device;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Vibrator;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceViewHolder;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.Log;

public class HeadphoneGainPreference extends CustomSeekBarPreference {

    private static int mMinVal = -40;
    private static int mMaxVal = 20;
    private static int mDefVal = 0;

    private static final boolean DEBUG = false;
    private static final String TAG = "HeadphoneGainPreference";
    private static final String FILE_LEVEL = "/sys/kernel/sound_control/headphone_gain";
    public static final String SETTINGS_KEY = DeviceSettings.KEY_SETTINGS_PREFIX + DeviceSettings.KEY_HEADPHONE_GAIN;

    public HeadphoneGainPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        // from sound/soc/codecs/msm8x16-wcd.c
        mInterval = 1;
        mShowSign = false;
        mUnits = "";
        mContinuousUpdates = false;
        mMinValue = mMinVal;
        mMaxValue = mMaxVal;
        mDefaultValueExists = true;
        mDefaultValue = mDefVal;
        mValue = Integer.parseInt(loadValue(context));
    }

    public static boolean isSupported() {
        return Utils.fileWritable(FILE_LEVEL);
    }

    public static String loadValue(Context context) {
        Log.i(TAG,"reading sysfs file: "+FILE_LEVEL);
        String val = Utils.getFileValueDual(FILE_LEVEL, String.valueOf(mDefVal));
        return val;
    }

    private void setValue(String newValue) {
        Utils.writeValueDual(FILE_LEVEL, newValue);
        Settings.System.putString(getContext().getContentResolver(), SETTINGS_KEY, newValue);
    }

    public static void restore(Context context) {
        if (!isSupported()) {
            return;
        }
        String storedValue = Settings.System.getString(context.getContentResolver(), SETTINGS_KEY);
        if (DEBUG) Log.d(TAG,"restore value:"+storedValue);
        if (storedValue == null) {
            storedValue = String.valueOf(mDefVal);
        }
        if (DEBUG) Log.d(TAG,"restore file:"+FILE_LEVEL);
        Utils.writeValueDual(FILE_LEVEL, storedValue);
    }

    @Override
    protected void changeValue(int newValue) {
        setValue(String.valueOf(newValue));
    }
}

