package heroiceraser.mulatschak.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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
        void onMultiPlayerSettingsChanged(int code);
    }

    MultiPlayerSettingsFragment.Listener mListener = null;

    private final int NOT_SET = -1;
    private int player_lives = NOT_SET;
    private int enemies = NOT_SET;
    private int difficulty = NOT_SET;

    private RadioButton enemies_0_radioButton;
    private RadioButton enemies_1_radioButton;
    private RadioButton enemies_2_radioButton;
    private RadioButton difficulty_easy_radioButton;
    private RadioButton difficulty_normal_radioButton;
    private RadioButton difficulty_hard_radioButton;
    private SeekBar player_lives_seekBar;
    private TextView enemiesText;
    private TextView difficultyText;
    private TextView player_lives_textView;
    private Button startGameButton;
    private TextView notHostText;
    private boolean notCreatedYet = false;
    private boolean received = false;
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
                        mListener.onMultiPlayerSettingsChanged(enemies0);
                        break;
                    case R.id.multi_player_settings_enemies_1_radioButton:
                        mListener.onMultiPlayerSettingsChanged(enemies1);
                        break;
                    case R.id.multi_player_settings_enemies_2_radioButton:
                        mListener.onMultiPlayerSettingsChanged(enemies2);
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
                        mListener.onMultiPlayerSettingsChanged(difficulty0);
                        break;
                    case R.id.multi_player_settings_difficulty_normal_radioButton:
                        mListener.onMultiPlayerSettingsChanged(difficulty1);
                        break;
                    case R.id.multi_player_settings_difficulty_hard_radioButton:
                        mListener.onMultiPlayerSettingsChanged(difficulty2);
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
                mListener.onMultiPlayerSettingsChanged(progress_value);
            }
        });

        notHostText = v.findViewById(R.id.multi_player_settings_no_host);

        if (notCreatedYet) {
            prepareMultiPlayerSettingsRequested(players, host);
        }
        if (received) {
            handleMessageCode();
        }

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
        enemies_0_radioButton.setEnabled(true);
        enemies_1_radioButton.setEnabled(true);
        enemies_2_radioButton.setEnabled(true);
        difficulty_easy_radioButton.setEnabled(true);
        difficulty_normal_radioButton.setEnabled(true);
        difficulty_hard_radioButton.setEnabled(true);
        player_lives_seekBar.setEnabled(true);
        startGameButton.setEnabled(true);
        enemies_0_radioButton.setVisibility(View.VISIBLE);
        enemies_1_radioButton.setVisibility(View.VISIBLE);
        enemies_2_radioButton.setVisibility(View.VISIBLE);
        difficulty_easy_radioButton.setVisibility(View.VISIBLE);
        difficulty_normal_radioButton.setVisibility(View.VISIBLE);
        difficulty_hard_radioButton.setVisibility(View.VISIBLE);
        player_lives_seekBar.setVisibility(View.VISIBLE);
        startGameButton.setVisibility(View.VISIBLE);
    }


    public void receiveMessage(int newMessageCode) {
        messageQueue.add(newMessageCode);
        handleMessageCode();
    }

    private ArrayList<Integer> messageQueue = new ArrayList<>();


    private void handleMessageCode() {
        try {
            int messageCode = messageQueue.get(0);
            switch (messageCode) {
                case MultiPlayerSettingsFragment.backButton:

                    break;
                case MultiPlayerSettingsFragment.enemies0:
                    enemies_0_radioButton.setChecked(true);
                    break;
                case MultiPlayerSettingsFragment.enemies1:
                    enemies_1_radioButton.setChecked(true);
                    break;
                case MultiPlayerSettingsFragment.enemies2:
                    enemies_2_radioButton.setChecked(true);
                    break;
                case MultiPlayerSettingsFragment.difficulty0:
                    difficulty_easy_radioButton.setChecked(true);
                    break;
                case MultiPlayerSettingsFragment.difficulty1:
                    difficulty_normal_radioButton.setChecked(true);
                    break;
                case MultiPlayerSettingsFragment.difficulty2:
                    difficulty_hard_radioButton.setChecked(true);
                    break;
                default:
                    String text = getString(R.string.single_player_settings_player_lives_text) +
                            "   "  + messageCode;
                    player_lives_textView.setText(text);
                    player_lives_seekBar.setProgress(messageCode - 1);
                    break;
            }
            messageQueue.remove(0);
            if (!messageQueue.isEmpty()) {
                handleMessageCode();
            }
        }
        catch (Exception e) {
            received = true;
        }
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

    public void setValues(int enemies, int difficulty, int player_lives) {
        switch (enemies) {
            case 0:
                enemies_0_radioButton.isChecked();
                break;

            case 1:
                enemies_1_radioButton.isChecked();
                break;

            case 2:
                enemies_2_radioButton.isChecked();
                break;
        }
        switch (difficulty) {
            case 0:
                difficulty_easy_radioButton.isChecked();
                break;

            case 1:
                difficulty_normal_radioButton.isChecked();
                break;

            case 2:
                difficulty_hard_radioButton.isChecked();
                break;
        }
        player_lives_seekBar.setProgress(player_lives - 1);
    }
}

