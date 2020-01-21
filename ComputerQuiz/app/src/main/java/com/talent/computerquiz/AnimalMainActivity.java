package com.talent.computerquiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import java.util.Set;

public class AnimalMainActivity extends AppCompatActivity {
    public final  static String GUESSES="settings_numberOfGuesses";
    public final  static String TECHNOLOGY_TYPE ="settings_technologyType";
    public final  static String QUIZ_BACKGROUND_COLOR="settings_quiz_background_color";
    public final  static String QUIZ_FONT="settings_quiz_font";

    private  boolean isSettingsChanged=true;
    static Typeface chunkfive;
    static  Typeface fontleroyBrown;
    static  Typeface wonderBarDemo;

        AnimalMainActivityFragment myAnimalQuizFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        chunkfive=Typeface.createFromAsset(getAssets(),"fonts/Chunkfive.otf");
        fontleroyBrown=Typeface.createFromAsset(getAssets(),"fonts/FontleroyBrown.ttf");
        wonderBarDemo=Typeface.createFromAsset(getAssets(),"fonts/Wonderbar Demo.otf");
        PreferenceManager.setDefaultValues(AnimalMainActivity.this,R.xml.quiz_preferences,false);
        PreferenceManager.getDefaultSharedPreferences(AnimalMainActivity.this)
                .registerOnSharedPreferenceChangeListener(settingsChangeListener);

        myAnimalQuizFragment= (AnimalMainActivityFragment) getSupportFragmentManager().findFragmentById(R.id.animalQuizFragment);

        myAnimalQuizFragment.modifyAnimalGuessRows(PreferenceManager.getDefaultSharedPreferences(AnimalMainActivity.this));
        myAnimalQuizFragment.modifyTypeOfAnimalsInQuiz(PreferenceManager.getDefaultSharedPreferences(AnimalMainActivity.this));
        myAnimalQuizFragment.modifyQuizFont(PreferenceManager.getDefaultSharedPreferences(AnimalMainActivity.this));
        myAnimalQuizFragment.modifyBGColor(PreferenceManager.getDefaultSharedPreferences(AnimalMainActivity.this));
        myAnimalQuizFragment.resetAnimalQuiz();
        isSettingsChanged = false;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //infate the menu ,this adds items to the items bar if present
        getMenuInflater().inflate(R.menu.menu_animal_main,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent  preferencesIntent = new Intent(AnimalMainActivity.this,SettingsActivity.class);
        startActivity(preferencesIntent);
        return  super.onOptionsItemSelected(item);

    }

    private SharedPreferences.OnSharedPreferenceChangeListener settingsChangeListener=
            new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                    isSettingsChanged = true;

                    if (key.equals(GUESSES)) {

                        myAnimalQuizFragment.modifyAnimalGuessRows(sharedPreferences);
                        myAnimalQuizFragment.resetAnimalQuiz();

                    } else if (key.equals(TECHNOLOGY_TYPE)) {

                        Set<String> animalTypes = sharedPreferences.getStringSet(TECHNOLOGY_TYPE, null);

                        if (animalTypes != null && animalTypes.size() > 0) {

                            myAnimalQuizFragment.modifyTypeOfAnimalsInQuiz(sharedPreferences);
                            myAnimalQuizFragment.resetAnimalQuiz();

                        } else {

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            animalTypes.add(getString(R.string.default_technology_type));
                            editor.putStringSet(TECHNOLOGY_TYPE, animalTypes);
                            editor.apply();

                            Toast.makeText(AnimalMainActivity.this,
                                    R.string.toast_message, Toast.LENGTH_SHORT).show();

                        }

                    } else if (key.equals(QUIZ_FONT)) {

                        myAnimalQuizFragment.modifyQuizFont(sharedPreferences);
                        myAnimalQuizFragment.resetAnimalQuiz();
                    } else if (key.equals(QUIZ_BACKGROUND_COLOR)) {

                        myAnimalQuizFragment.modifyBGColor(sharedPreferences);
                        myAnimalQuizFragment.resetAnimalQuiz();

                    }

                    Toast.makeText(AnimalMainActivity.this, R.string.change_message, Toast.LENGTH_SHORT).show();



                }
            };
}
