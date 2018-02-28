package heroiceraser.mulatschak.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import at.heroiceraser.mulatschak.R;
import heroiceraser.mulatschak.helpers.LocaleHelper;


//--------------------------------------------------------------------------------------------------
//  Start Screen Fragment
//
public class StartScreenFragment extends Fragment implements OnClickListener {
    String mGreeting;
    String language;

    public interface Listener {
        void onSinglePlayerSettingsRequested();
        void onShowAchievementsRequested();
        void onShowLeaderboardsRequested();
        void onSignInButtonClicked();
        void onSignOutButtonClicked();
        void onMultiPlayerQuickGameRequested();
        void onMultiPlayerInvitePlayersRequested();
        void onMultiPlayerSeeInvitationsRequested();
        void onChangeLanguage();
    }

    Listener mListener = null;
    boolean mShowSignIn = true;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.start_screen, container, false);
        final int[] CLICKABLES = new int[] {
                R.id.change_language_button,
                R.id.single_player_button,
                R.id.show_achievements_button, R.id.show_leaderboards_button,
                R.id.sign_in_button, R.id.sign_out_button,
                R.id.multi_player_quick_game_button,
                R.id.multi_player_invite_button,
                R.id.multi_player_invitations_button
        };
        for (int i : CLICKABLES) {
            v.findViewById(i).setOnClickListener(this);
        }

        mGreeting = v.getResources().getString(R.string.signed_out_greeting);
        language = LocaleHelper.getLanguage(getContext());
        return v;
    }

    public void setListener(Listener l) {
        mListener = l;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateUi();
        updateLanguageIcon(language);
    }

    public void setGreeting(String greeting) {
        mGreeting = greeting;
        updateUi();
    }

    void updateUi() {
        if (getActivity() == null) return;

        TextView tv =  getActivity().findViewById(R.id.hello);
        if (tv != null) tv.setText(mGreeting);

        getActivity().findViewById(R.id.sign_in_bar).setVisibility(mShowSignIn ?
                View.VISIBLE : View.GONE);
        getActivity().findViewById(R.id.sign_out_bar).setVisibility(mShowSignIn ?
                View.GONE : View.VISIBLE);
    }

    public void updateLanguageIcon(String languageCode) {
        if (getActivity() == null) return;
        ImageButton ib = getActivity().findViewById(R.id.change_language_button);
        switch (languageCode) {
            case "de":
                ib.setImageResource(R.drawable.language_de);
                break;
            default:
                ib.setImageResource(R.drawable.language_en);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.single_player_button:
                mListener.onSinglePlayerSettingsRequested();
                break;
            case R.id.show_achievements_button:
                mListener.onShowAchievementsRequested();
                break;
            case R.id.show_leaderboards_button:
                mListener.onShowLeaderboardsRequested();
                break;
            case R.id.sign_in_button:
                mListener.onSignInButtonClicked();
                break;
            case R.id.sign_out_button:
                mListener.onSignOutButtonClicked();
                break;
            case R.id.change_language_button:
                mListener.onChangeLanguage();
                break;
            case R.id.multi_player_quick_game_button:
                mListener.onMultiPlayerQuickGameRequested();
                break;
            case R.id.multi_player_invite_button:
                mListener.onMultiPlayerInvitePlayersRequested();
                break;
            case R.id.multi_player_invitations_button:
                mListener.onMultiPlayerSeeInvitationsRequested();
                break;
        }
    }

    public void handleSignIn(boolean showSignIn) {
        mShowSignIn = showSignIn;
        handleButtons(!showSignIn);
        updateUi();
    }

    private void handleButtons(boolean signedIn) {
        if (getActivity() != null) {
            getActivity().findViewById(R.id.multi_player_quick_game_button).setEnabled(signedIn);
            getActivity().findViewById(R.id.multi_player_invite_button).setEnabled(signedIn);
            getActivity().findViewById(R.id.multi_player_invitations_button).setEnabled(signedIn);
            getActivity().findViewById(R.id.show_achievements_button).setEnabled(signedIn);
            getActivity().findViewById(R.id.show_leaderboards_button).setEnabled(signedIn);
        }
    }
}

