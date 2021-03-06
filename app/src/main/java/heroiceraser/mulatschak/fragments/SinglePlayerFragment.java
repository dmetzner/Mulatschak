package heroiceraser.mulatschak.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import at.heroiceraser.mulatschak.R;


public class SinglePlayerFragment extends Fragment implements View.OnClickListener {

    public interface Listener {
        void onSinglePlayerRequested();

        void switchToStartScreen();
    }

    heroiceraser.mulatschak.fragments.SinglePlayerFragment.Listener mListener = null;

    private final int NOT_SET = -1;
    private int player_lives = NOT_SET;
    private int max_lives = NOT_SET;
    private int enemies = NOT_SET;
    private int difficulty = NOT_SET;

    private RadioButton enemies_1_radioButton;
    private RadioButton enemies_2_radioButton;
    private RadioButton enemies_3_radioButton;
    private RadioButton difficulty_easy_radioButton;
    private RadioButton difficulty_normal_radioButton;
    private RadioButton difficulty_hard_radioButton;
    private SeekBar player_lives_seekBar;
    private TextView player_lives_textView;
    private SeekBar max_player_lives_seekBar;
    private TextView max_player_lives_textView;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.singleplayer_settings, container, false);
        final int[] CLICKABLES = new int[]{
                R.id.single_player_settings_back_button,
                R.id.single_player_settings_start_button
        };
        for (int i : CLICKABLES) {
            v.findViewById(i).setOnClickListener(this);
        }

        enemies_1_radioButton =
                v.findViewById(R.id.single_player_settings_enemies_1_radioButton);
        enemies_2_radioButton =
                v.findViewById(R.id.single_player_settings_enemies_2_radioButton);
        enemies_3_radioButton =
                v.findViewById(R.id.single_player_settings_enemies_3_radioButton);

        difficulty_easy_radioButton =
                v.findViewById(R.id.single_player_settings_difficulty_easy_radioButton);
        difficulty_normal_radioButton =
                v.findViewById(R.id.single_player_settings_difficulty_normal_radioButton);
        difficulty_hard_radioButton =
                v.findViewById(R.id.single_player_settings_difficulty_hard_radioButton);

        player_lives_textView =
                v.findViewById(R.id.single_player_settings_player_lives_text);
        player_lives_seekBar =
                v.findViewById(R.id.single_player_settings_player_lives_seekBar);

        player_lives_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress_value = NOT_SET;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress_value = progress;
                String text = getString(R.string.single_player_settings_player_lives_text) +
                        "   " + (progress_value + 1);
                player_lives_textView.setText(text);

                setPlayerLives();
                setMaxLives();
                if (player_lives >= max_lives) {
                    max_player_lives_seekBar.setProgress(player_lives);
                    setMaxLives();
                    max_player_lives_textView.setText(getString(R.string.single_player_settings_player_max_lives_text) +
                            "   " + (max_lives + 2));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                String text = getString(R.string.single_player_settings_player_lives_text) +
                        "   " + (progress_value + 1);
                player_lives_textView.setText(text);

                setPlayerLives();
                setMaxLives();
                if (player_lives >= max_lives) {
                    max_player_lives_seekBar.setProgress(player_lives);
                    setMaxLives();
                    max_player_lives_textView.setText(getString(R.string.single_player_settings_player_max_lives_text) +
                            "   " + (max_lives + 2));
                }
            }
        });

        max_player_lives_textView =
                v.findViewById(R.id.single_player_settings_player_max_lives_text);
        max_player_lives_seekBar =
                v.findViewById(R.id.single_player_settings_player_max_lives_seekBar);

        max_player_lives_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress_value = NOT_SET;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress_value = progress;
                String text = getString(R.string.single_player_settings_player_max_lives_text) +
                        "   " + (max_lives + 2);
                max_player_lives_textView.setText(text);

                setPlayerLives();
                setMaxLives();
                if (max_lives <= player_lives) {
                    player_lives_seekBar.setProgress(max_lives);
                    setPlayerLives();
                    player_lives_textView.setText(getString(R.string.single_player_settings_player_lives_text) +
                            "   " + (player_lives + 1));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                String text = getString(R.string.single_player_settings_player_max_lives_text) +
                        "   " + (max_lives + 2);
                max_player_lives_textView.setText(text);

                setPlayerLives();
                setMaxLives();
                if (max_lives <= player_lives) {
                    player_lives_seekBar.setProgress(max_lives);
                    setPlayerLives();
                    player_lives_textView.setText(getString(R.string.single_player_settings_player_lives_text) +
                            "   " + (player_lives + 1));
                }
            }
        });

        receiveGameSettings();

        return v;
    }

    public void setListener(heroiceraser.mulatschak.fragments.SinglePlayerFragment.Listener l) {
        mListener = l;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.single_player_settings_start_button:
                mListener.onSinglePlayerRequested();
                break;
            case R.id.single_player_settings_back_button:
                mListener.switchToStartScreen();
                break;
        }
    }

    public void receiveGameSettings() {
        setEnemies();
        setDifficulty();
        setPlayerLives();
        setMaxLives();
    }

    private void setEnemies() {
        if (enemies_1_radioButton.isChecked()) {
            enemies = 1;
        } else if (enemies_2_radioButton.isChecked()) {
            enemies = 2;
        } else if (enemies_3_radioButton.isChecked()) {
            enemies = 3;
        }
    }

    private void setDifficulty() {
        if (difficulty_easy_radioButton.isChecked()) {
            difficulty = 1;
        } else if (difficulty_normal_radioButton.isChecked()) {
            difficulty = 2;
        } else if (difficulty_hard_radioButton.isChecked()) {
            difficulty = 3;
        }
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
}

