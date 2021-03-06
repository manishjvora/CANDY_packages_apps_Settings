/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.development;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.os.SystemProperties;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.preference.PreferenceScreen;
import android.view.View;

import com.android.settings.testutils.SettingsRobolectricTestRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RuntimeEnvironment;

@RunWith(SettingsRobolectricTestRunner.class)
public class ShowLayoutBoundsPreferenceControllerTest {

    @Mock
    private SwitchPreference mPreference;
    @Mock
    private PreferenceScreen mPreferenceScreen;

    private ShowLayoutBoundsPreferenceController mController;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mController = new ShowLayoutBoundsPreferenceController(RuntimeEnvironment.application);
        when(mPreferenceScreen.findPreference(mController.getPreferenceKey()))
            .thenReturn(mPreference);
        mController.displayPreference(mPreferenceScreen);
    }

    @Test
    public void onPreferenceChanged_settingEnabled_turnOnShowLayoutBounds() {
        mController.onPreferenceChange(mPreference, true /* new value */);

        final boolean mode =
            SystemProperties.getBoolean(View.DEBUG_LAYOUT_PROPERTY, false /* default */);

        assertThat(mode).isTrue();
    }

    @Test
    public void onPreferenceChanged_settingDisabled_turnOffShowLayoutBounds() {
        mController.onPreferenceChange(mPreference, false /* new value */);

        final boolean mode =
            SystemProperties.getBoolean(View.DEBUG_LAYOUT_PROPERTY, false /* default */);

        assertThat(mode).isFalse();
    }

    @Test
    public void updateState_settingEnabled_preferenceShouldBeChecked() {
        SystemProperties.set(View.DEBUG_LAYOUT_PROPERTY, Boolean.toString(true));
        mController.updateState(mPreference);

        verify(mPreference).setChecked(true);
    }

    @Test
    public void updateState_settingDisabled_preferenceShouldNotBeChecked() {
        SystemProperties.set(View.DEBUG_LAYOUT_PROPERTY, Boolean.toString(false));
        mController.updateState(mPreference);

        verify(mPreference).setChecked(false);
    }

    @Test
    public void onDeveloperOptionsDisabled_shouldDisablePreference() {
        mController.onDeveloperOptionsDisabled();

        final boolean mode =
            SystemProperties.getBoolean(View.DEBUG_LAYOUT_PROPERTY, false /* default */);

        assertThat(mode).isFalse();
        verify(mPreference).setEnabled(false);
        verify(mPreference).setChecked(false);
    }
}
