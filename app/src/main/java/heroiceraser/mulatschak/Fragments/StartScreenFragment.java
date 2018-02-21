package heroiceraser.mulatschak.Fragments;

import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import at.heroiceraser.mulatschak.R;



//--------------------------------------------------------------------------------------------------
//  Start Screen Fragment
//
public class StartScreenFragment extends Fragment implements OnClickListener {
    String mGreeting = "Hello, anonymous user (not signed in)";

    public interface Listener {
        void onSinglePlayerSettingsRequested();
        void onMultiPlayerRequested();
        void onShowAchievementsRequested();
        void onShowLeaderboardsRequested();
        void onSignInButtonClicked();
        void onSignOutButtonClicked();
    }

    Listener mListener = null;
    boolean mShowSignIn = true;

    private Button mSinglePlayerButton;
    private Button mMultiplayerButton;
    private Button mAchievementsButton;
    private Button mLeaderboardsButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.start_screen, container, false);
        final int[] CLICKABLES = new int[] {
                R.id.single_player_button, R.id.multi_player_button,
                R.id.show_achievements_button, R.id.show_leaderboards_button,
                R.id.sign_in_button, R.id.sign_out_button
        };
        for (int i : CLICKABLES) {
            v.findViewById(i).setOnClickListener(this);
        }

        return v;
    }

    public void setListener(Listener l) {
        mListener = l;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateUi();
    }

    public void setGreeting(String greeting) {
        mGreeting = greeting;
        updateUi();
    }

    void updateUi() {
        if (getActivity() == null) return;
        TextView tv = (TextView) getActivity().findViewById(R.id.hello);
        if (tv != null) tv.setText(mGreeting);

        getActivity().findViewById(R.id.sign_in_bar).setVisibility(mShowSignIn ?
                View.VISIBLE : View.GONE);
        getActivity().findViewById(R.id.sign_out_bar).setVisibility(mShowSignIn ?
                View.GONE : View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.single_player_button:
                mListener.onSinglePlayerSettingsRequested();
                break;
            case R.id.multi_player_button:
                mListener.onMultiPlayerRequested();
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
        }
    }

    public void handleSignIn(boolean showSignIn) {
        mShowSignIn = showSignIn;
        handleButtons(!showSignIn);
        updateUi();
    }

    private void handleButtons(boolean signedIn) {
        if (getActivity() != null) {
            getActivity().findViewById(R.id.multi_player_button).setEnabled(signedIn);
            getActivity().findViewById(R.id.show_achievements_button).setEnabled(signedIn);
            getActivity().findViewById(R.id.show_leaderboards_button).setEnabled(signedIn);
        }
    }
}

