package com.example.qrlector;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Aplicar el tema antes de crear la vista
        applyTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle("Configuraciones");

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_container, new SettingsFragment())
                .commit();
    }

    private void applyTheme() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String theme = sharedPreferences.getString("theme_preference", "light");

        if (theme.equals("dark")) {
            setTheme(R.style.Theme_QRLector);
        } else {
            setTheme(R.style.Theme_QRLector);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.preferences, rootKey);

            // Configurar la opción de "Ayuda y Comentarios"
            Preference helpPreference = findPreference("help_feedback"); // Cambia la clave a la que usaste en preferences.xml
            if (helpPreference != null) {
                helpPreference.setOnPreferenceClickListener(preference -> {
                    openHelpForm(preference);
                    return true;
                });
            }
        }

        private void openHelpForm(Preference preference) {
            String url = "https://docs.google.com/forms/d/e/1FAIpQLSdUR3WtTBIlIVKCb3WpaC1_LXtvG3QvRZQ_-eDCqcC6Svu79Q/viewform?usp=sf_link";
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
        }
    }
}
