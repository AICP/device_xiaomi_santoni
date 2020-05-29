/*
* Copyright (C) 2016 The OmniROM Project
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

import android.content.Intent;
import android.os.Bundle;
import androidx.preference.PreferenceFragment;
import androidx.preference.Preference;
import androidx.preference.TwoStatePreference;

import com.aicp.device.kcal.KCalSettingsActivity;
import com.aicp.device.dirac.DiracActivity;

public class DeviceSettings extends PreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    public static final String KEY_VIBSTRENGTH = "vib_strength";
    public static final String KEY_KCAL = "device_kcal";
    public static final String KEY_DIRAC = "device_dirac";
    public static final String KEY_FASTCHARGE = "fastcharge";
    public static final String KEY_HEADPHONE_GAIN = "headphone_gain";
    public static final String KEY_MIC_GAIN = "mic_gain";
    public static final String KEY_SPEAKER_GAIN = "speaker_gain";
    public static final String KEY_SETTINGS_PREFIX = "device_setting_";

    private VibratorStrengthPreference mVibratorStrength;
    private Preference mKcal;
    private Preference mDirac;

    private HeadphoneGainPreference mHeadphoneGainPref;
    private MicGainPreference mMicGainPref;
    private SpeakerGainPreference mSpeakerGainPref;

    private static TwoStatePreference mFastChargeSwitch;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.main, rootKey);

        mKcal = findPreference(KEY_KCAL);
        mKcal.setOnPreferenceClickListener(preference -> {
            Intent intent = new Intent(getActivity().getApplicationContext(), KCalSettingsActivity.class);
            startActivity(intent);
            return true;
        });

        mDirac = findPreference(KEY_DIRAC);
        mDirac.setOnPreferenceClickListener(preference -> {
            Intent intent = new Intent(getActivity().getApplicationContext(), DiracActivity.class);
            startActivity(intent);
            return true;
        });

        mVibratorStrength = (VibratorStrengthPreference) findPreference(KEY_VIBSTRENGTH);
        if (mVibratorStrength != null) {
            mVibratorStrength.setEnabled(VibratorStrengthPreference.isSupported());
        }

        mFastChargeSwitch = (TwoStatePreference) findPreference(KEY_FASTCHARGE);
        if (mFastChargeSwitch != null) {
            mFastChargeSwitch.setEnabled(true);
            mFastChargeSwitch.setChecked(FastChargeSwitch.isCurrentlyEnabled(this.getContext()));
            mFastChargeSwitch.setOnPreferenceChangeListener(new FastChargeSwitch(getContext()));
        }

        mHeadphoneGainPref = (HeadphoneGainPreference) findPreference(KEY_HEADPHONE_GAIN);
        mMicGainPref = (MicGainPreference) findPreference(KEY_MIC_GAIN);
        mSpeakerGainPref = (SpeakerGainPreference) findPreference(KEY_SPEAKER_GAIN);

        if (mHeadphoneGainPref != null) {
            mHeadphoneGainPref.setEnabled(true);
        }

        if (mMicGainPref != null) {
            mMicGainPref.setEnabled(true);
        }

        if (mSpeakerGainPref != null) {
            mSpeakerGainPref.setEnabled(true);
        }
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        return super.onPreferenceTreeClick(preference);
    }
    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return true;
    }
}
