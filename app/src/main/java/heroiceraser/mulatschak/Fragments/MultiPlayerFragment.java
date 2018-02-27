package heroiceraser.mulatschak.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import at.heroiceraser.mulatschak.R;


//--------------------------------------------------------------------------------------------------
//  Multi Player Screen Fragment
//
public class MultiPlayerFragment extends Fragment implements View.OnClickListener {

    public interface Listener {
        void onMultiPlayerQuickGameRequested();
        void onMultiPlayerInvitePlayersRequested();
        void onMultiPlayerSeeInvitationsRequested();
        void onStartMenuRequested();
    }

    heroiceraser.mulatschak.Fragments.MultiPlayerFragment.Listener mListener = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.multiplayer_options, container, false);
        final int[] CLICKABLES = new int[]{
                R.id.multi_player_settings_back_button,
                R.id.multi_player_settings_quick_game_button,
                R.id.multi_player_settings_invite_button,
                R.id.multi_player_settings_invitations_button
        };
        for (int i : CLICKABLES) {
            v.findViewById(i).setOnClickListener(this);
        }
        return v;
    }

    public void setListener(heroiceraser.mulatschak.Fragments.MultiPlayerFragment.Listener l) {
        mListener = l;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.multi_player_settings_quick_game_button:
                mListener.onMultiPlayerQuickGameRequested();
                break;
            case R.id.multi_player_settings_invite_button:
                mListener.onMultiPlayerInvitePlayersRequested();
                break;
            case R.id.multi_player_settings_invitations_button:
                mListener.onMultiPlayerSeeInvitationsRequested();
                break;
            case R.id.multi_player_settings_back_button:
                mListener.onStartMenuRequested();
                break;
        }
    }
}
