package heroiceraser.mulatschak.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

import at.heroiceraser.mulatschak.R;
import heroiceraser.mulatschak.helpers.startGameParas;


//--------------------------------------------------------------------------------------------------
//  Singe Player Screen Fragment
//
public class MultiPlayerSettingsFragment extends Fragment implements View.OnClickListener {

    public static final int backButton = 0;
    // public static final int startButton = -1;
    public static final int enemies0 = -2;
    public static final int enemies1 = -3;
    public static final int enemies2 = -4;
    public static final int difficulty0 = -5;
    public static final int difficulty1 = -6;
    public static final int difficulty2 = -7;


    public interface Listener {
        void onMultiPlayerSettingsStartGameRequested();
        void onMultiPlayerSettingsBackButtonRequested();
        void onMultiPlayerSettingsChanged();
    }

    MultiPlayerSettingsFragment.Listener mListener = null;

    private final int NOT_SET = -1;
    private int player_lives = NOT_SET;
    private int enemies = NOT_SET;
    private int difficulty = NOT_SET;
    private int max_lives = NOT_SET;

    private RadioButton enemies_0_radioButton;
    private RadioButton enemies_1_radioButton;
    private RadioButton enemies_2_radioButton;
    private RadioButton difficulty_easy_radioButton;
    private RadioButton difficulty_normal_radioButton;
    private RadioButton difficulty_hard_radioButton;
    private SeekBar player_lives_seekBar;
    private TextView player_lives_textView;
    private SeekBar max_player_lives_seekBar;
    private TextView max_player_lives_textView;
    private TextView enemiesText;
    private TextView difficultyText;
    private Button startGameButton;
    private TextView notHostText;
    private boolean notCreatedYet = false;
    int players;
    boolean host;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.multiplayer_settings, container, false);
        final int[] CLICKABLES = new int[] {
                R.id.multi_player_settings_back_button,
                R.id.multi_player_settings_start_button
        };
        for (int i : CLICKABLES) {
            v.findViewById(i).setOnClickListener(this);
        }

        startGameButton = v.findViewById(R.id.multi_player_settings_start_button);

        enemiesText = v.findViewById(R.id.multi_player_settings_player_amount_text);
        enemies_0_radioButton = v.findViewById(R.id.multi_player_settings_enemies_0_radioButton);
        enemies_1_radioButton = v.findViewById(R.id.multi_player_settings_enemies_1_radioButton);
        enemies_2_radioButton = v.findViewById(R.id.multi_player_settings_enemies_2_radioButton);

        RadioGroup radioGroup = v.findViewById(R.id.multi_player_settings_enemies_radio_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.multi_player_settings_enemies_0_radioButton:
                        mListener.onMultiPlayerSettingsChanged();
                        break;
                    case R.id.multi_player_settings_enemies_1_radioButton:
                        mListener.onMultiPlayerSettingsChanged();
                        break;
                    case R.id.multi_player_settings_enemies_2_radioButton:
                        mListener.onMultiPlayerSettingsChanged();
                        break;
                }
            }
        });

        difficultyText = v.findViewById(R.id.multi_player_settings_difficulty_text);
        difficulty_easy_radioButton   =
                v.findViewById(R.id.multi_player_settings_difficulty_easy_radioButton);
        difficulty_normal_radioButton =
                v.findViewById(R.id.multi_player_settings_difficulty_normal_radioButton);
        difficulty_hard_radioButton   =
                v.findViewById(R.id.multi_player_settings_difficulty_hard_radioButton);

        RadioGroup radioGroup2 =  v.findViewById(R.id.multi_player_settings_difficulty_radio_group);
        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.multi_player_settings_difficulty_easy_radioButton:
                        mListener.onMultiPlayerSettingsChanged();
                        break;
                    case R.id.multi_player_settings_difficulty_normal_radioButton:
                        mListener.onMultiPlayerSettingsChanged();
                        break;
                    case R.id.multi_player_settings_difficulty_hard_radioButton:
                        mListener.onMultiPlayerSettingsChanged();
                        break;
                }
            }
        });


        player_lives_textView = v.findViewById(R.id.multi_player_settings_player_lives_text);
        player_lives_seekBar = v.findViewById(R.id.multi_player_settings_player_lives_seekBar);

        player_lives_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress_value = NOT_SET;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress_value = progress;
                String text = getString(R.string.single_player_settings_player_lives_text) +
                        "   "  + (progress_value + 1);
                player_lives_textView.setText(text);

                setPlayerLives();
                setMaxLives();
                if (player_lives >= max_lives) {
                    max_player_lives_seekBar.setProgress(player_lives);
                    setMaxLives();
                    max_player_lives_textView.setText(getString(R.string.single_player_settings_player_max_lives_text) +
                            "   "  + (max_lives + 2));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                String text = getString(R.string.single_player_settings_player_lives_text) +
                        "   "  + (progress_value + 1);
                player_lives_textView.setText(text);

                setPlayerLives();
                setMaxLives();
                if (player_lives >= max_lives) {
                    max_player_lives_seekBar.setProgress(player_lives);
                    setMaxLives();
                    max_player_lives_textView.setText(getString(R.string.single_player_settings_player_max_lives_text) +
                            "   "  + (max_lives + 2));
                }
                mListener.onMultiPlayerSettingsChanged();
            }
        });

        max_player_lives_textView = v.findViewById(R.id.multi_player_settings_player_max_lives_text);
        max_player_lives_seekBar = v.findViewById(R.id.multi_player_settings_player_max_lives_seekBar);

        max_player_lives_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress_value = NOT_SET;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress_value = progress;
                String text = getString(R.string.single_player_settings_player_max_lives_text) +
                        "   "  + (max_lives + 2);
                max_player_lives_textView.setText(text);

                setPlayerLives();
                setMaxLives();
                if (max_lives <= player_lives) {
                    player_lives_seekBar.setProgress(max_lives);
                    setPlayerLives();
                    player_lives_textView.setText(getString(R.string.single_player_settings_player_lives_text) +
                            "   "  + (player_lives + 1));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                String text = getString(R.string.single_player_settings_player_max_lives_text) +
                        "   "  + (max_lives + 2);
                max_player_lives_textView.setText(text);

                setPlayerLives();
                setMaxLives();
                if (max_lives <= player_lives) {
                    player_lives_seekBar.setProgress(max_lives);
                    setPlayerLives();
                    player_lives_textView.setText(getString(R.string.single_player_settings_player_lives_text) +
                            "   "  + (player_lives + 1));
                }
                mListener.onMultiPlayerSettingsChanged();
            }
        });

        notHostText = v.findViewById(R.id.multi_player_settings_no_host);

        if (notCreatedYet) {
            prepareMultiPlayerSettingsRequested(players, host);
        }

        setMultiPlayerSettingsRequested();

        return v;
    }

    public void setListener(MultiPlayerSettingsFragment.Listener l) {
        mListener = l;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.multi_player_settings_start_button:
                mListener.onMultiPlayerSettingsStartGameRequested();
                break;
            case R.id.multi_player_settings_back_button:
                mListener.onMultiPlayerSettingsBackButtonRequested();
                break;
        }
    }

    public void prepareMultiPlayerSettingsRequested(int players, boolean host) {

        try {
            resetMultiPlayerSettingsRequested();

            if (players >= 4) {
                enemies_2_radioButton.setVisibility(View.GONE);
                enemies_1_radioButton.setVisibility(View.GONE);
                difficulty_easy_radioButton.setVisibility(View.GONE);
                difficulty_normal_radioButton.setVisibility(View.GONE);
                difficulty_hard_radioButton.setVisibility(View.GONE);
                enemies_2_radioButton.setEnabled(false);
                enemies_1_radioButton.setEnabled(false);
                difficulty_easy_radioButton.setEnabled(false);
                difficulty_normal_radioButton.setEnabled(false);
                difficulty_hard_radioButton.setEnabled(false);
                difficultyText.setVisibility(View.GONE);
            }
            else if (players == 3) {
                enemies_2_radioButton.setEnabled(false);
                enemies_2_radioButton.setVisibility(View.GONE);
            }
            if (host) {
                difficultyText.setText(getString(R.string.multi_player_settings_difficulty_text));
                enemiesText.setText(getString(R.string.multi_player_settings_player_amount_text));
                startGameButton.setVisibility(View.VISIBLE);
                notHostText.setVisibility(View.GONE);
                notCreatedYet = false;
            }
            // no host
            else {
                enemies_2_radioButton.setEnabled(false);
                enemies_1_radioButton.setEnabled(false);
                enemies_0_radioButton.setEnabled(false);
                difficulty_easy_radioButton.setEnabled(false);
                difficulty_normal_radioButton.setEnabled(false);
                difficulty_hard_radioButton.setEnabled(false);
                player_lives_seekBar.setEnabled(false);
                max_player_lives_seekBar.setEnabled(false);
                difficultyText.setText(getString(R.string.multi_player_settings_difficulty_text_no_host));
                enemiesText.setText(getString(R.string.multi_player_settings_player_amount_text_no_host));
                startGameButton.setVisibility(View.GONE);
                notHostText.setVisibility(View.VISIBLE);
            }
        }
        catch (Exception e) {
            notCreatedYet = true;
            this.players = players;
            this.host = host;
        }
    }

    private void resetMultiPlayerSettingsRequested() {
        difficultyText.setVisibility(View.VISIBLE);
        enemies_0_radioButton.setEnabled(true);
        enemies_1_radioButton.setEnabled(true);
        enemies_2_radioButton.setEnabled(true);
        difficulty_easy_radioButton.setEnabled(true);
        difficulty_normal_radioButton.setEnabled(true);
        difficulty_hard_radioButton.setEnabled(true);
        player_lives_seekBar.setEnabled(true);
        max_player_lives_seekBar.setEnabled(true);
        startGameButton.setEnabled(true);
        enemies_0_radioButton.setVisibility(View.VISIBLE);
        enemies_1_radioButton.setVisibility(View.VISIBLE);
        enemies_2_radioButton.setVisibility(View.VISIBLE);
        difficulty_easy_radioButton.setVisibility(View.VISIBLE);
        difficulty_normal_radioButton.setVisibility(View.VISIBLE);
        difficulty_hard_radioButton.setVisibility(View.VISIBLE);
        player_lives_seekBar.setVisibility(View.VISIBLE);
        max_player_lives_seekBar.setVisibility(View.VISIBLE);
        startGameButton.setVisibility(View.VISIBLE);
        player_lives_textView.setText( getString(R.string.single_player_settings_player_lives_text_default));
        max_player_lives_textView.setText( getString(R.string.single_player_settings_player_max_lives_text_default));
    }


    public void setMultiPlayerSettingsRequested() {
        if (enemies_0_radioButton.isChecked()) {
            enemies = 0;
        }
        else if (enemies_1_radioButton.isChecked()) {
            enemies = 1;
        }
        else if (enemies_2_radioButton.isChecked()) {
            enemies = 2;
        }

        if (difficulty_easy_radioButton.isChecked()) {
            difficulty = 1;
        }
        else if (difficulty_normal_radioButton.isChecked()) {
            difficulty = 2;
        }
        else if (difficulty_hard_radioButton.isChecked()) {
            difficulty = 3;
        }

        setPlayerLives();

        setMaxLives();
    }

    private void setPlayerLives() {
        player_lives = player_lives_seekBar.getProgress();
    }

    public int getDifficulty() {
        return difficulty;
    }

    public int getEnemies() {
        return enemies;
    }

    private void setMaxLives() {
        max_lives = max_player_lives_seekBar.getProgress();
    }

    public int getPlayerLives() {
        return player_lives + 1;
    }

    public int getMaxLives() {
        return max_lives + 2;
    }

    public synchronized void setValues(int enemies, int difficulty, int player_lives, int max_lives) {

        try {
            switch (enemies) {
                case 0:
                    enemies_0_radioButton.setChecked(true);
                    break;

                case 1:
                    enemies_1_radioButton.setChecked(true);
                    break;

                case 2:
                    enemies_2_radioButton.setChecked(true);
                    break;
            }
            switch (difficulty) {
                case 1:
                    difficulty_easy_radioButton.setChecked(true);
                    break;

                case 2:
                    difficulty_normal_radioButton.setChecked(true);
                    break;

                case 3:
                    difficulty_hard_radioButton.setChecked(true);
                    break;
            }

            this.player_lives = player_lives - 1;
            player_lives_seekBar.setProgress(this.player_lives);
            player_lives_textView.setText(getString(R.string.single_player_settings_player_lives_text) + "" + player_lives);

            this.max_lives = max_lives - 2;
            max_player_lives_seekBar.setProgress(this.max_lives);
            max_player_lives_textView.setText(getString(R.string.single_player_settings_player_max_lives_text) + "" + max_lives);
        }
        catch (Exception e) {
            // not created yet
            setValues(enemies, difficulty, player_lives, max_lives);
        }

    }
}

