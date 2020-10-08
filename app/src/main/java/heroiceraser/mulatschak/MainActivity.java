package heroiceraser.mulatschak;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.games.AnnotatedData;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.event.Event;
import com.google.android.gms.games.event.EventBuffer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.List;

import at.heroiceraser.mulatschak.R;
import heroiceraser.mulatschak.fragments.GameScreenFragment;
import heroiceraser.mulatschak.fragments.GlobalSettingsFragment;
import heroiceraser.mulatschak.fragments.LoadingScreenFragment;
import heroiceraser.mulatschak.fragments.PrivacyPolicyFragment;
import heroiceraser.mulatschak.fragments.RulesFragment;
import heroiceraser.mulatschak.fragments.SinglePlayerFragment;
import heroiceraser.mulatschak.fragments.StartScreenFragment;
import heroiceraser.mulatschak.fragments.WebViewActivityPrivacy;
import heroiceraser.mulatschak.fragments.WebViewActivityRules;
import heroiceraser.mulatschak.game.GameView;
import heroiceraser.mulatschak.gameSettings.GameSettings;
import heroiceraser.mulatschak.utils.DetectConnection;
import heroiceraser.mulatschak.utils.LocaleHelper;


public class MainActivity extends FragmentActivity implements
        StartScreenFragment.Listener,
        SinglePlayerFragment.Listener,
        PrivacyPolicyFragment.Listener,
        RulesFragment.Listener,
        GlobalSettingsFragment.Listener {

    final String TAG = "MainActivity";

    public final Boolean DEBUG = false;


    // Fragments
    List<Fragment> fragList = new ArrayList<>();
    StartScreenFragment mStartScreenFragment;
    SinglePlayerFragment mSinglePlayerFragment;
    LoadingScreenFragment mLoadingScreenFragment;
    GameScreenFragment mGameScreenFragment;
    PrivacyPolicyFragment mPrivacyPolicyFragment;
    RulesFragment mRulesFragment;
    GlobalSettingsFragment mSettingsFragment;

    // Views
    GameView mGameView = null;


    // request codes we use when invoking an external activity
    private static final int RC_SIGN_IN = 9001;
    private static final int RC_ACHIEVEMENT_UI = 9003;
    private static final int RC_LEADERBOARD_UI = 9004;


    // Client used to sign in with Google APIs
    private GoogleSignInClient mGoogleSignInClient = null;

    // to save data local
    public SharedPreferences mySharedPreference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);
        createFragments();
        setLocale();
        setStartScreenAsInitialFragment();
        initGoogleSignIn();

        mySharedPreference = getSharedPreferences("MY_MULATSCHAK_DATA", MODE_PRIVATE);
    }

    @Override
    protected void onStart() {
        // Check for existing Google Sign In account,
        // if the user is already signed in the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        mStartScreenFragment.updateSignInUI(account);
        super.onStart();
    }


    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        fragList.add(fragment);
    }

    private Fragment getVisibleFragment() {
        List<Fragment> fragments = new ArrayList<>(fragList);
        for (Fragment fragment : fragments) {
            if (fragment != null && fragment.isVisible())
                return fragment;
        }

        return null;
    }

    private void switchToFragment(Fragment newFrag, String id) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, newFrag, id)
                .commitAllowingStateLoss();
    }

    private void createFragments() {
        mStartScreenFragment = new StartScreenFragment();
        mSinglePlayerFragment = new SinglePlayerFragment();
        mLoadingScreenFragment = new LoadingScreenFragment();
        mGameScreenFragment = new GameScreenFragment();
        mPrivacyPolicyFragment = new PrivacyPolicyFragment();
        mRulesFragment = new RulesFragment();
        mSettingsFragment = new GlobalSettingsFragment();

        mStartScreenFragment.setListener(this);
        mSinglePlayerFragment.setListener(this);
        mPrivacyPolicyFragment.setListener(this);
        mRulesFragment.setListener(this);
        mSettingsFragment.setListener(this);
    }

    private void setStartScreenAsInitialFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, mStartScreenFragment)
                .commit();
    }


    @Override
    public void switchToStartScreen() {
        switchToFragment(mStartScreenFragment, "mStartScreenFragment");
    }

    public void switchToLoadingScreen() {
        switchToFragment(mLoadingScreenFragment, "mLoadingScreenFragment");
    }


    private void initGoogleSignIn() {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
    }

    private void signInToGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            mStartScreenFragment.updateSignInUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            mStartScreenFragment.updateSignInUI(null);
        }
    }

    public void signOutFromGoogle() {
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mStartScreenFragment.updateSignInUI(null);
                        } else if (null != task.getException()) {
                            mStartScreenFragment.showSnackbarError(
                                    task.getException().getMessage()
                            );
                        }
                    }
                });

    }

    public boolean isSignedInToGoogle() {
        return null != getSignedInGoogleAccount();
    }

    private GoogleSignInAccount getSignedInGoogleAccount() {
        return GoogleSignIn.getLastSignedInAccount(this);
    }

    private void setLocale() {
        LocaleHelper.setLocale(this, LocaleHelper.getLanguage(this));
    }

    @Override
    public void onChangeLanguage() {
        if (LocaleHelper.getLanguage(this).equals("de")) {
            LocaleHelper.setLocale(this, "en");
            mStartScreenFragment.updateLanguageIcon("en");
        } else {
            LocaleHelper.setLocale(this, "de");
            mStartScreenFragment.updateLanguageIcon("de");
        }

        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    @Override
    public void onPolicyRequested() {
        boolean internetConnection = DetectConnection.checkInternetConnection(getApplicationContext());
        if (internetConnection) {
            Intent intent = new Intent(getApplicationContext(), WebViewActivityPrivacy.class);
            startActivity(intent);
        } else {
            mStartScreenFragment.showSnackbarError(getString(R.string.no_internet_error));
            switchToFragment(mPrivacyPolicyFragment, "mPrivacyPolicyFragment");
        }
    }

    @Override
    public void onRulesRequested() {
        boolean internetConnection = DetectConnection.checkInternetConnection(getApplicationContext());
        if (internetConnection) {
            Intent intent = new Intent(getApplicationContext(), WebViewActivityRules.class);
            startActivity(intent);
        } else {
            mStartScreenFragment.showSnackbarError(getString(R.string.no_internet_error));
            switchToFragment(mRulesFragment, "mRulesFragment");
        }
    }

    @Override
    public void onSinglePlayerSettingsRequested() {
        switchToFragment(mSinglePlayerFragment, "mSinglePlayerFragment");
    }

    @Override
    public void onShowAchievementsRequested() {
        if (isSignedInToGoogle()) {
            Games.getAchievementsClient(this, getSignedInGoogleAccount())
                    .getAchievementsIntent()
                    .addOnSuccessListener(new OnSuccessListener<Intent>() {
                        @Override
                        public void onSuccess(Intent intent) {
                            startActivityForResult(intent, RC_ACHIEVEMENT_UI);
                        }
                    });
        } else {
            mStartScreenFragment.showSnackbarError(getString(R.string.signin_first));
        }
    }

    @Override
    public void onShowLeaderboardsRequested() {
        if (isSignedInToGoogle()) {
            Games.getLeaderboardsClient(this, getSignedInGoogleAccount()
            ).getAllLeaderboardsIntent()
                    .addOnSuccessListener(new OnSuccessListener<Intent>() {
                        @Override
                        public void onSuccess(Intent intent) {
                            startActivityForResult(intent, RC_LEADERBOARD_UI);
                        }
                    });
        } else {
            mStartScreenFragment.showSnackbarError(getString(R.string.signin_first));
        }
    }

    @Override
    public void onSignInButtonClicked() {
        signInToGoogle();
    }

    @Override
    public void onSignOutButtonClicked() {
        signOutFromGoogle();
    }


    @Override
    public void onBackPressed() {

        if (isGameRunning()) {
            new MaterialAlertDialogBuilder(this)
                    .setTitle(getString(R.string.close, getString(R.string.this_game)))
                    .setMessage(getString(R.string.are_you_sure_to_close_message, getString(R.string.this_game)))
                    .setPositiveButton(getString(R.string.yes_button), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cancelGame();
                        }
                    })
                    .setNegativeButton(getString(R.string.no_button), null)
                    .show();
        } else if (isStartScreenActive()) {
            new MaterialAlertDialogBuilder(this)
                    .setTitle(getString(R.string.close, getString(R.string.app_name)))
                    .setMessage(getString(R.string.are_you_sure_to_close_message, getString(R.string.app_name)))
                    .setPositiveButton(getString(R.string.yes_button), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton(getString(R.string.no_button), null)
                    .show();
        } else {
            switchToStartScreen();
        }
    }

    private boolean isStartScreenActive() {
        Fragment visibleFragment = getVisibleFragment();
        return visibleFragment != null
                && visibleFragment.getTag() != null
                && visibleFragment.getTag().equals("mStartScreenFragment");
    }

    @Contract(pure = true)
    private boolean isGameRunning() {
        return null != mGameView;
    }


    @Override
    public void onSinglePlayerRequested() {
        try {
            switchToLoadingScreen();
            Handler mHandler = new Handler();
            Runnable showLoadingScreenThenContinue = new Runnable() {
                @Override
                public void run() {
                    GameSettings gameSettings = getGameSettings();
                    startGame(gameSettings);
                }
            };
            mHandler.postDelayed(showLoadingScreenThenContinue, 10);
        } catch (Exception e) {
            switchToStartScreen();
            mStartScreenFragment.showSnackbarError(getString(R.string.error_text));
        }
    }

    @NonNull
    private GameSettings getGameSettings() {
        GameSettings gameSettings = new GameSettings();

        // global settings
        gameSettings.cardDesign = mSettingsFragment.getCardDesign();

        // local settings
        mSinglePlayerFragment.receiveGameSettings();
        gameSettings.enemies = mSinglePlayerFragment.getEnemies();
        gameSettings.difficulty = mSinglePlayerFragment.getDifficulty();
        gameSettings.playerLives = mSinglePlayerFragment.getPlayerLives();
        gameSettings.maxLives = mSinglePlayerFragment.getMaxLives();

        return gameSettings;
    }

    private void startGame(final GameSettings gameSettings) {

        mGameView = new GameView(this);
        mGameView.setKeepScreenOn(true);

        addContentView(
                mGameView,
                new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT
                )
        );

        Runnable showGameScreenThenContinue = new Runnable() {
            @Override
            public void run() {
                try {
                    switchToFragment(mGameScreenFragment, "mGameScreenFragment");
                    mGameView.getController().init(gameSettings);
                } catch (Exception e) {
                    mStartScreenFragment.showSnackbarError(getString(R.string.error_game_canceled));
                    cancelGame();
                }
            }
        };

        new Handler().postDelayed(showGameScreenThenContinue, 10);
    }

    public void finishGame(boolean gameWon) {
        switchToLoadingScreen();
        if (isSignedInToGoogle()) {
            updateLeaderboardsAndAchievements(gameWon);
        }
        clearGameData();
        switchToStartScreen();
    }

    public void cancelGame() {
        switchToLoadingScreen();
        clearGameData();
        switchToStartScreen();
    }

    private void clearGameData() {
        ((ViewGroup) mGameView.getParent()).removeView(mGameView);
        mGameView.clear();
        mGameView = null;
        System.gc();
    }


    private void updateLeaderboardsAndAchievements(final boolean gameWon) {

        incrementEvent_TotalGamesPlayed();
        incrementEvent_TotalWins(gameWon);

        Games.getEventsClient(this, getSignedInGoogleAccount())
                .load(true)
                .addOnCompleteListener(new OnCompleteListener<AnnotatedData<EventBuffer>>() {
                    @Override
                    public void onComplete(@NonNull Task<AnnotatedData<EventBuffer>> task) {

                        if (task.isSuccessful() && null != task.getResult()) {

                            EventBuffer eventBuffer = task.getResult().get();
                            if (null == eventBuffer) {
                                return;
                            }

                            int totalGamesPlayedTmp = 0;
                            int totalWinsTmp = 0;

                            for (Event event : eventBuffer) {
                                if (event.getEventId().equals(getString(R.string.event_games_played))) {
                                    totalGamesPlayedTmp = (int) event.getValue();
                                }
                                if (event.getEventId().equals(getString(R.string.event_wins))) {
                                    totalWinsTmp = (int) event.getValue();
                                }
                            }

                            eventBuffer.release();

                            final int totalGamesPlayed = totalGamesPlayedTmp;
                            final int totalWins = totalWinsTmp;

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    setLeaderBoardsAndAchievements(gameWon, totalGamesPlayed, totalWins);
                                }
                            });

                        } else {
                            mStartScreenFragment.showSnackbarError("Event exception");
                        }
                        releaseInstance();
                    }
                });
    }


    private void incrementEvent_TotalGamesPlayed() {
        Games.getEventsClient(this, getSignedInGoogleAccount())
                .increment(getResources().getString(R.string.event_games_played), 1);
    }

    private void incrementEvent_TotalWins(boolean gameWon) {
        if (gameWon) {
            Games.getEventsClient(this, getSignedInGoogleAccount())
                    .increment(getResources().getString(R.string.event_wins), 1);
        }
    }


    private void setLeaderBoardsAndAchievements(boolean gameWon, int totalGamesPlayed, int totalWins) {

        if (!isSignedInToGoogle()) {
            return;
        }

        // LeaderBoards
        updateLeaderboards_TotalGamesPlayed(totalGamesPlayed);
        updateLeaderboards_TotalWins(totalWins);
        updateLeaderboards_WinRate(totalGamesPlayed, totalWins);

        // Achievements
        unlockAchievement_FirstGamePlayed(totalGamesPlayed);
        incrementAchievement_AllTimePlayedGames();
        unlockAchievement_FirstGameWon(gameWon, totalWins);
        incrementAchievement_WinningIsEverything(gameWon);
        incrementAchievement_ProGamer(gameWon);
        incrementAchievement_NeverGiveUp(!gameWon);
    }

    private void updateLeaderboards_TotalGamesPlayed(int totalGamesPlayed) {
        Games.getLeaderboardsClient(this, getSignedInGoogleAccount())
                .submitScore(getString(R.string.leaderboard_games_played), totalGamesPlayed);
    }

    private void updateLeaderboards_TotalWins(int totalWins) {
        Games.getLeaderboardsClient(this, getSignedInGoogleAccount())
                .submitScore(getString(R.string.leaderboard_wins), totalWins);
    }

    private void updateLeaderboards_WinRate(int totalGamesPlayed, int totalWins) {
        if (totalGamesPlayed > 0) {
            double winRate = (totalWins * 100.0) / totalGamesPlayed;
            Games.getLeaderboardsClient(this, getSignedInGoogleAccount())
                    .submitScore(getString(R.string.leaderboard_win_rate), (long) winRate * 100);
        }
    }


    private void unlockAchievement_FirstGamePlayed(int totalGamesPlayed) {
        if (totalGamesPlayed == 1) {
            Games.getAchievementsClient(this, getSignedInGoogleAccount())
                    .unlock(getString(R.string.achievement_first_game));
        }
    }

    private void incrementAchievement_AllTimePlayedGames() {
        Games.getAchievementsClient(this, getSignedInGoogleAccount())
                .increment(getString(R.string.achievement_play_all_time), 1);
    }

    private void unlockAchievement_FirstGameWon(boolean gameWon, int totalWins) {
        if (gameWon && totalWins == 1) {
            Games.getAchievementsClient(this, getSignedInGoogleAccount())
                    .unlock(getString(R.string.achievement_first_win));
        }
    }

    private void incrementAchievement_WinningIsEverything(boolean gameWon) {
        if (gameWon) {
            Games.getAchievementsClient(this, getSignedInGoogleAccount())
                    .increment(getString(R.string.achievement_winning_is_everything), 1);
        }
    }

    private void incrementAchievement_ProGamer(boolean gameWon) {
        if (gameWon) {
            Games.getAchievementsClient(this, getSignedInGoogleAccount())
                    .increment(getString(R.string.achievement_winning_is_everything), 1);
        }
    }

    private void incrementAchievement_NeverGiveUp(boolean gameLost) {
        if (gameLost) {
            Games.getAchievementsClient(this, getSignedInGoogleAccount())
                    .increment(getString(R.string.achievement_never_give_up), 1);
        }
    }


    public void unlockAchievement(final int id) {

        final Activity activity = this;
        final GoogleSignInAccount googleSignInAccount = getSignedInGoogleAccount();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isSignedInToGoogle()) {
                    Games.getAchievementsClient(activity, googleSignInAccount)
                            .unlock(getString(id));
                }
            }
        });
    }
}
