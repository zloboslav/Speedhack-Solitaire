/*
 * Copyright (C) 2016  Tobias Bielefeld
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * If you want to contact me, send me an e-mail at tobias.bielefeld@gmail.com
 */


package de.tobiasbielefeld.solitaire.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.util.List;
import java.util.Locale;

import de.tobiasbielefeld.solitaire.R;
import de.tobiasbielefeld.solitaire.classes.Card;

import static de.tobiasbielefeld.solitaire.SharedData.CARD_BACKGROUND;
import static de.tobiasbielefeld.solitaire.SharedData.CARD_DRAWABLES;
import static de.tobiasbielefeld.solitaire.SharedData.gameLogic;
import static de.tobiasbielefeld.solitaire.SharedData.getSharedBoolean;
import static de.tobiasbielefeld.solitaire.SharedData.getSharedInt;
import static de.tobiasbielefeld.solitaire.SharedData.getSharedString;
import static de.tobiasbielefeld.solitaire.SharedData.savedSharedData;

/*
 *
 */

public class Settings extends AppCompatPreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private Preference preferenceCards, preferenceCardsBackground;

    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((ViewGroup) getListView().getParent()).setPadding(0, 0, 0, 0);                             //remove huge padding in landscape


         /* set a nice back arrow in the actionBar */
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //only item is the back arrow
        finish();
        return true;
    }

    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key) {
            case CARD_DRAWABLES:
                Card.updateCardDrawableChoice();
                setPreferenceCardsSummary();
                break;
            case CARD_BACKGROUND:
                Card.updateCardBackgroundChoice();
                setPreferenceCardsBackgroundSummary();
                break;
            case "pref_key_hide_status_bar":
                showOrHideStatusBar();
                break;
            case "pref_key_orientation":
                setOrientation();
                break;
            case "pref_key_left_handed_mode":
                gameLogic.mirrorStacks();
                break;
        }
    }

    public void onResume() {
        super.onResume();

        savedSharedData.registerOnSharedPreferenceChangeListener(this);
        showOrHideStatusBar();
        setOrientation();
    }

    public void onPause() {
        super.onPause();
        savedSharedData.unregisterOnSharedPreferenceChangeListener(this);
    }

    private void setPreferenceCardsBackgroundSummary() {
        preferenceCardsBackground.setSummary(String.format(Locale.getDefault(), "%s %s",
                getString(R.string.settings_background), getSharedInt(CARD_BACKGROUND, 1)));

        switch (getSharedInt(CARD_BACKGROUND, 1)) {
            case 1:
                preferenceCardsBackground.setIcon(R.drawable.background_1);
                break;
            case 2:
                preferenceCardsBackground.setIcon(R.drawable.background_2);
                break;
            case 3:
                preferenceCardsBackground.setIcon(R.drawable.background_3);
                break;
            case 4:
                preferenceCardsBackground.setIcon(R.drawable.background_4);
                break;
            case 5:
                preferenceCardsBackground.setIcon(R.drawable.background_5);
                break;
            case 6:
                preferenceCardsBackground.setIcon(R.drawable.background_6);
                break;
            case 7:
                preferenceCardsBackground.setIcon(R.drawable.background_7);
                break;
            case 8:
                preferenceCardsBackground.setIcon(R.drawable.background_8);
                break;
            case 9:
                preferenceCardsBackground.setIcon(R.drawable.background_9);
                break;
            case 10:
                preferenceCardsBackground.setIcon(R.drawable.background_10);
                break;
            case 11:
                preferenceCardsBackground.setIcon(R.drawable.background_11);
                break;
            case 12:
                preferenceCardsBackground.setIcon(R.drawable.background_12);
                break;
        }
    }

    private void setPreferenceCardsSummary() {
        String text = "";

        switch (getSharedInt(CARD_DRAWABLES, 1)) {
            case 1:
                text = getString(R.string.settings_classic);
                preferenceCards.setIcon(R.drawable.classic_diamonds_13);
                break;
            case 2:
                text = getString(R.string.settings_abstract);
                preferenceCards.setIcon(R.drawable.abstract_diamonds_13);
                break;
            case 3:
                text = getString(R.string.settings_simple);
                preferenceCards.setIcon(R.drawable.simple_diamonds_13);
                break;
            case 4:
                text = getString(R.string.settings_modern);
                preferenceCards.setIcon(R.drawable.modern_diamonds_13);
                break;
            case 5:
                text = getString(R.string.settings_dark);
                preferenceCards.setIcon(R.drawable.dark_diamonds_13);
                break;
        }

        preferenceCards.setSummary(text);
    }

    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || CustomizationPreferenceFragment.class.getName().equals(fragmentName)
                || OtherPreferenceFragment.class.getName().equals(fragmentName)
                || GamesPreferenceFragment.class.getName().equals(fragmentName);
    }

    private void setOrientation() {
        switch (getSharedString("pref_key_orientation", "1")) {
            case "1": //follow system settings
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
                break;
            case "2": //portrait
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                break;
            case "3": //landscape
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                break;
            case "4": //landscape upside down
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
                break;
        }
    }

    private void showOrHideStatusBar() {
        if (getSharedBoolean(getString(R.string.pref_key_hide_status_bar), false))
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        else
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public static class CustomizationPreferenceFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_customize);
            setHasOptionsMenu(true);

            Settings settings = (Settings) getActivity();

            settings.preferenceCards = findPreference(getString(R.string.pref_key_cards));
            settings.preferenceCardsBackground = findPreference(getString(R.string.pref_key_cards_background));

            settings.setPreferenceCardsSummary();
            settings.setPreferenceCardsBackgroundSummary();
        }
    }

    public static class OtherPreferenceFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_other);
            setHasOptionsMenu(true);
        }
    }

    public static class GamesPreferenceFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_games);
            setHasOptionsMenu(true);
        }
    }
}
