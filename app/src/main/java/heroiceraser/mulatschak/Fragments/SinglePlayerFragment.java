package heroiceraser.mulatschak.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

import at.heroiceraser.mulatschak.R;


//--------------------------------------------------------------------------------------------------
//  Singe Player Screen Fragment
//
public class SinglePlayerFragment extends Fragment implements View.OnClickListener {

    public interface Listener {
        void onSinglePlayerRequested();
        void onStartMenuRequested();
    }

    heroiceraser.mulatschak.Fragments.SinglePlayerFragment.Listener mListener = null;

    private final int NOT_SET = -1;
    private int player_lives = NOT_SET;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.singleplayer_settings, container, false);
        final int[] CLICKABLES = new int[] {
                R.id.single_player_settings_back_button,
                R.id.single_player_settings_start_button
        };
        for (int i : CLICKABLES) {
            v.findViewById(i).setOnClickListener(this);
        }

        enemies_1_radioButton = (RadioButton)
                v.findViewById(R.id.single_player_settings_enemies_1_radioButton);
        enemies_2_radioButton = (RadioButton)
                v.findViewById(R.id.single_player_settings_enemies_2_radioButton);
        enemies_3_radioButton = (RadioButton)
                v.findViewById(R.id.single_player_settings_enemies_3_radioButton);

        difficulty_easy_radioButton   = (RadioButton)
                v.findViewById(R.id.single_player_settings_difficulty_easy_radioButton);
        difficulty_normal_radioButton = (RadioButton)
                v.findViewById(R.id.single_player_settings_difficulty_normal_radioButton);
        difficulty_hard_radioButton   = (RadioButton)
                v.findViewById(R.id.single_player_settings_difficulty_hard_radioButton);

        player_lives_textView = (TextView)
                v.findViewById(R.id.single_player_settings_player_lives_text);
        player_lives_seekBar = (SeekBar)
                v.findViewById(R.id.single_player_settings_player_lives_seekBar);

        player_lives_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress_value = NOT_SET;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress_value = progress + 1;
                String text = getString(R.string.single_player_settings_player_lives_text) +
                        "   "  + progress_value;
                player_lives_textView.setText(text);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                String text = getString(R.string.single_player_settings_player_lives_text) +
                        "   "  + progress_value;
                player_lives_textView.setText(text);
            }
        });

        return v;
    }

    public void setListener(heroiceraser.mulatschak.Fragments.SinglePlayerFragment.Listener l) {
        mListener = l;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.single_player_settings_start_button:
                mListener.onSinglePlayerRequested();
                break;
            case R.id.single_player_settings_back_button:
                mListener.onStartMenuRequested();
                break;
        }
    }

    public void prepareSinglePlayerRequested() {
        if (enemies_1_radioButton.isChecked()) {
            enemies = 1;
        }
        else if (enemies_2_radioButton.isChecked()) {
            enemies = 2;
        }
        else if (enemies_3_radioButton.isChecked()) {
            enemies = 3;
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

        player_lives = player_lives_seekBar.getProgress() + 1;
    }

    public int getPlayerLives() {
        return player_lives;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public int getEnemies() {
        return enemies;
    }
}

