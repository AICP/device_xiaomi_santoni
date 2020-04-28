/*
* Copyright (C) 2016 The OmniROM Project
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

public class VibratorStrengthPreference extends CustomSeekBarPreference {

    private static int mMinVal = 116;
    private static int mMaxVal = 3596;
    private static int mDefVal = mMaxVal - (mMaxVal - mMinVal) / 4;
    private Vibrator mVibrator;

    private static final boolean DEBUG = false;
    private static final String TAG = "VibratorStrengthPreference";
    private static final String FILE_LEVEL = "/sys/class/timed_output/vibrator/vtg_level";
    private static final long testVibrationPattern[] = {0,250};
    public static final String SETTINGS_KEY = DeviceSettings.KEY_SETTINGS_PREFIX + DeviceSettings.KEY_VIBSTRENGTH;

    public VibratorStrengthPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        mInterval = 10;
        mShowSign = false;
        mUnits = "";
        mContinuousUpdates = false;
        mMinValue = mMinVal;
        mMaxValue = mMaxVal;
        mDefaultValueExists = true;
        mDefaultValue = mDefVal;
        mValue = Integer.parseInt(loadValue(context));

        setPersistent(false);

        mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public static boolean isSupported() {
        return Utils.fileWritable(FILE_LEVEL);
    }

    public static String loadValue(Context context) {
        Log.i(TAG,"reading sysfs file: "+FILE_LEVEL);
        String val = Utils.getFileValue(FILE_LEVEL, String.valueOf(mDefVal));
        return val;
    }

    private void setValue(String newValue, boolean withFeedback) {
        Utils.writeValue(FILE_LEVEL, newValue);
        Settings.System.putString(getContext().getContentResolver(), SETTINGS_KEY, newValue);
        if (withFeedback) {
            mVibrator.vibrate(testVibrationPattern, -1);
        }
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
        Utils.writeValue(FILE_LEVEL, storedValue);
    }

    @Override
    protected void changeValue(int newValue) {
        setValue(String.valueOf(newValue), true);
    }
}

