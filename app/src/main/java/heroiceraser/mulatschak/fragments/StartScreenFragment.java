package heroiceraser.mulatschak.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.snackbar.Snackbar;

import at.heroiceraser.mulatschak.R;
import heroiceraser.mulatschak.MainActivity;
import heroiceraser.mulatschak.utils.LocaleHelper;
import heroiceraser.mulatschak.utils.MyViewUtils;


public class StartScreenFragment extends Fragment {

    private String language;
    private GoogleSignInAccount googleSignInAccount;

    public interface Listener {
        void onSinglePlayerSettingsRequested();

        void onPolicyRequested();

        void onRulesRequested();

        void onShowAchievementsRequested();

        void onShowLeaderboardsRequested();

        void onSignInButtonClicked();

        void onSignOutButtonClicked();

        void onChangeLanguage();
    }

    private Listener mListener = null;


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.single_player_button:
                    mListener.onSinglePlayerSettingsRequested();
                    break;
                case R.id.achievements_button:
                    mListener.onShowAchievementsRequested();
                    break;
                case R.id.leaderboards_button:
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
                case R.id.policy_button:
                    mListener.onPolicyRequested();
                    break;
                case R.id.rules_button:
                    mListener.onRulesRequested();
                    break;
            }
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.start_screen, container, false);

        MyViewUtils.addOnClickListenerToElements(view, onClickListener, new int[]{
                R.id.change_language_button,
                R.id.single_player_button,
                R.id.sign_in_button,
                R.id.sign_out_button,
                R.id.policy_button,
                R.id.rules_button,
                R.id.leaderboards_button,
                R.id.achievements_button
        });

        language = LocaleHelper.getLanguage(getContext());
        return view;
    }

    public void setListener(Listener l) {
        mListener = l;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateLanguageIcon(language);
        updateSignInUI(googleSignInAccount);
    }

    public void showSnackbarError(String text) {
        View view = getView();
        if (null == view) {
            return;
        }
        Snackbar.make(getView(), text, Snackbar.LENGTH_LONG).show();
    }

    public void updateLanguageIcon(String languageCode) {
        if (getActivity() == null) return;
        ImageButton ib = getActivity().findViewById(R.id.change_language_button);
        switch (languageCode) {
            case "de":
                ib.setImageResource(R.drawable.language_de);
                break;
            case "en":
            default:
                ib.setImageResource(R.drawable.language_en);
        }
    }


    public void updateSignInUI(GoogleSignInAccount account) {

        googleSignInAccount = account;

        MainActivity mainActivity = (MainActivity) getActivity();
        if (null == mainActivity) {
            return;
        }

        mainActivity.findViewById(R.id.sign_in_bar)
                .setVisibility(null == account ? View.VISIBLE : View.GONE);

        mainActivity.findViewById(R.id.sign_out_bar)
                .setVisibility(null != account ? View.VISIBLE : View.GONE);
    }
}

