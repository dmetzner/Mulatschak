package heroiceraser.mulatschak.titleScreen;

/**
 * Created by Daniel Metzner on 15.09.2017.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import heroiceraser.mulatschak.R;

/**
 * Created by Daniel Metzner on 16.09.2017.
 */

public class SinglePlayerFragment extends Fragment implements View.OnClickListener {

    public interface Listener {
        void onSinglePlayerRequested();
        void onStartMenuRequested();
    }

    heroiceraser.mulatschak.titleScreen.SinglePlayerFragment.Listener mListener = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.singleplayer_settings, container, false);
        final int[] CLICKABLES = new int[] {
                R.id.single_player_settings_back_button,
                R.id.single_player_settings_enemies_1_radioButton,
                R.id.single_player_settings_enemies_2_radioButton,
                R.id.single_player_settings_enemies_3_radioButton,
                R.id.single_player_settings_difficulty_easy_radioButton,
                R.id.single_player_settings_difficulty_normal_radioButton,
                R.id.single_player_settings_difficulty_hard_radioButton,
                R.id.single_player_settings_player_lives_seekBar,
                R.id.single_player_settings_start_button
        };
        for (int i : CLICKABLES) {
            v.findViewById(i).setOnClickListener(this);
        }
        return v;
    }

    public void setListener(heroiceraser.mulatschak.titleScreen.SinglePlayerFragment.Listener l) {
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
}
