/*
* Copyright (C) 2013 The OmniROM Project
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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

import com.aicp.device.dirac.DiracUtils;
import com.aicp.device.kcal.KcalUtils;
import com.aicp.device.kcal.FileUtils;

public class Startup extends BroadcastReceiver implements KcalUtils {

    private final FileUtils mFileUtils = new FileUtils();

    private static void restore(String file, boolean enabled) {
        if (file == null) {
            return;
        }
        Utils.writeValueSimple(file, enabled ? "1" : "0");
    }

    @Override
    public void onReceive(final Context context, final Intent bootintent) {
        restoreAfterUserSwitch(context);
        new DiracUtils(context).onBootCompleted();

        if (Settings.Secure.getInt(context.getContentResolver(), PREF_ENABLED, 0) == 1) {
            mFileUtils.setValue(KCAL_ENABLE, Settings.Secure.getInt(context.getContentResolver(),
                    PREF_ENABLED, 0));

            String rgbValue = Settings.Secure.getInt(context.getContentResolver(),
                    PREF_RED, RED_DEFAULT) + " " +
                    Settings.Secure.getInt(context.getContentResolver(), PREF_GREEN,
                            GREEN_DEFAULT) + " " +
                    Settings.Secure.getInt(context.getContentResolver(), PREF_BLUE,
                            BLUE_DEFAULT);

            mFileUtils.setValue(KCAL_RGB, rgbValue);
            mFileUtils.setValue(KCAL_MIN, Settings.Secure.getInt(context.getContentResolver(),
                    PREF_MINIMUM, MINIMUM_DEFAULT));
            mFileUtils.setValue(KCAL_SAT, Settings.Secure.getInt(context.getContentResolver(),
                    PREF_GRAYSCALE, 0) == 1 ? 128 :
                    Settings.Secure.getInt(context.getContentResolver(),
                            PREF_SATURATION, SATURATION_DEFAULT) + SATURATION_OFFSET);
            mFileUtils.setValue(KCAL_VAL, Settings.Secure.getInt(context.getContentResolver(),
                    PREF_VALUE, VALUE_DEFAULT) + VALUE_OFFSET);
            mFileUtils.setValue(KCAL_CONT, Settings.Secure.getInt(context.getContentResolver(),
                    PREF_CONTRAST, CONTRAST_DEFAULT) + CONTRAST_OFFSET);
            mFileUtils.setValue(KCAL_HUE, Settings.Secure.getInt(context.getContentResolver(),
                    PREF_HUE, HUE_DEFAULT));
        }
    }

    public static void restoreAfterUserSwitch(Context context) {

        boolean enabled = Settings.System.getInt(context.getContentResolver(), FastChargeSwitch.SETTINGS_KEY, 0) != 0;
        restore(FastChargeSwitch.getFile(), enabled);

        VibratorStrengthPreference.restore(context);
        HeadphoneGainPreference.restore(context);
        MicGainPreference.restore(context);
        SpeakerGainPreference.restore(context);
    }
}
