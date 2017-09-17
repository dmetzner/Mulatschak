package heroiceraser.mulatschak.titleScreen;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;
import com.google.example.games.basegameutils.BaseGameUtils;

import java.util.List;

import heroiceraser.mulatschak.R;
import heroiceraser.mulatschak.game.GameActivity;
import heroiceraser.mulatschak.game.GameView;

public class MainActivity extends AppCompatActivity implements
        StartScreenFragment.Listener,
        SinglePlayerFragment.Listener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    // Fragments
    StartScreenFragment mStartScreenFragment;
    SinglePlayerFragment mSinglePlayerFragment;
    LoadingScreenFragment mLoadingScreenFragment;

    // Client used to interact with Google APIs
    private GoogleApiClient mGoogleApiClient;

    // Are we currently resolving a connection failure?
    private boolean mResolvingConnectionFailure = false;

    // Has the user clicked the sign-in button?
    private boolean mSignInClicked = false;

    // Automatically start the sign-in flow when the Activity starts
    private boolean mAutoStartSignInFlow = true;

    // request codes we use when invoking an external activity
    private static final int RC_RESOLVE = 5000;
    private static final int RC_UNUSED = 5001;
    private static final int RC_SIGN_IN = 9001;

    // tag for debug logging
    final boolean ENABLE_DEBUG = true;
    final String TAG = "MainActivity";

    // achievements and scores we're pending to push to the cloud
    // (waiting for the user to sign in, for instance)
    AccomplishmentsOutbox mOutbox = new AccomplishmentsOutbox();

    //----------------------------------------------------------------------------------------------
    //  onCreate
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_activity);

        // Create the Google Api Client with access to the Play Game and Drive services.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .build();

        // create fragments
        mStartScreenFragment = new StartScreenFragment();
        mSinglePlayerFragment = new SinglePlayerFragment();
        mLoadingScreenFragment = new LoadingScreenFragment();

        // listen to fragment events
        mStartScreenFragment.setListener(this);
        mSinglePlayerFragment.setListener(this);

        // add initial fragment (welcome fragment)
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,
                mStartScreenFragment).commit();

        // load outbox from file
        mOutbox.loadLocal(this);
    }

    //----------------------------------------------------------------------------------------------
    // Switch UI to the given fragment
    //
    void switchToFragment(Fragment newFrag, String id) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, newFrag, id)
                .commit();
    }

    //----------------------------------------------------------------------------------------------
    // isSignedIn
    //
    private boolean isSignedIn() {
        return (mGoogleApiClient != null && mGoogleApiClient.isConnected());
    }

    //----------------------------------------------------------------------------------------------
    // onStart
    //
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart(): connecting");
        mGoogleApiClient.connect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Fragment frag = getVisibleFragment();
        if (frag != null && frag.equals(mLoadingScreenFragment)) {
            onStartMenuRequested();
        }
    }

    //----------------------------------------------------------------------------------------------
    // onStop
    //
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop(): disconnecting");
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    //----------------------------------------------------------------------------------------------
    // start SinglePlayer Game Mode
    //
    @Override
    public void onSinglePlayerSettingsRequested() {
        switchToFragment(mSinglePlayerFragment, "mSinglePlayerFragment");
    }

    //----------------------------------------------------------------------------------------------
    // start MultiPlayer Game Mode
    //
    @Override
    public void onMultiPlayerRequested() {
        // ToDO Multiplayer
    }

    //----------------------------------------------------------------------------------------------
    // start Show Achievements
    //
    @Override
    public void onShowAchievementsRequested() {
        if (isSignedIn()) {
            startActivityForResult(Games.Achievements.getAchievementsIntent(mGoogleApiClient),
                    RC_UNUSED);
        } else {
            BaseGameUtils.makeSimpleDialog(this, getString(R.string.achievements_not_available)).show();
        }
    }

    //----------------------------------------------------------------------------------------------
    // start Show LeaderBoards
    //
    @Override
    public void onShowLeaderboardsRequested() {
        if (isSignedIn()) {
            startActivityForResult(Games.Leaderboards.getAllLeaderboardsIntent(mGoogleApiClient),
                    RC_UNUSED);
        } else {
            BaseGameUtils.makeSimpleDialog(this, getString(R.string.leaderboards_not_available)).show();
        }
    }

    //----------------------------------------------------------------------------------------------
    // check for Achievements  ToDO
    //
    void checkForAchievements(int requestedScore, int finalScore) {
        // Check if each condition is met; if so, unlock the corresponding
        // achievement.
        /*if (requestedScore == 9999) {
            mOutbox.mArrogantAchievement = true;
            achievementToast(getString(R.string.achievement_arrogant_toast_text));
        }*/
        mOutbox.mBoredSteps++;
    }

    void unlockAchievement(int achievementId, String fallbackString) {
        if (isSignedIn()) {
            Games.Achievements.unlock(mGoogleApiClient, getString(achievementId));
        } else {
            Toast.makeText(this, getString(R.string.achievement) + ": " + fallbackString,
                    Toast.LENGTH_LONG).show();
        }
    }

    void achievementToast(String achievement) {
        // Only show toast if not signed in. If signed in, the standard Google Play
        // toasts will appear, so we don't need to show our own.
        if (!isSignedIn()) {
            Toast.makeText(this, getString(R.string.achievement) + ": " + achievement,
                    Toast.LENGTH_LONG).show();
        }
    }

    void pushAccomplishments() {
        if (!isSignedIn()) {
            // can't push to the cloud, so save locally
            mOutbox.saveLocal(this);
            return;
        }
        //if (mOutbox.mPrimeAchievement) {
          //  Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_prime));
            //mOutbox.mPrimeAchievement = false;
        //}
        mOutbox.saveLocal(this);
    }

    /**
     * Update leaderboards with the user's score.
     *
     * @param finalScore The score the user got.
     */
    void updateLeaderboards(int finalScore) { // ToDO
        if (mOutbox.mHardModeScore < finalScore) {
            mOutbox.mHardModeScore = finalScore;
        }
    }

    //----------------------------------------------------------------------------------------------
    //  onActivityResult
    //
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == RC_SIGN_IN) {
            mSignInClicked = false;
            mResolvingConnectionFailure = false;
            if (resultCode == RESULT_OK) {
                mGoogleApiClient.connect();
            } else {
                BaseGameUtils.showActivityResultError(this, requestCode, resultCode, R.string.signin_other_error);
            }
        }
    }

    //----------------------------------------------------------------------------------------------
    //  onConnected
    //
    @Override
    public void onConnected(Bundle connectionHint) {
        Log.d(TAG, "onConnected(): connected to Google APIs");
        // Show sign-out button on main menu
        mStartScreenFragment.setShowSignInButton(false);

        // Set the greeting appropriately on main menu
        Player p = Games.Players.getCurrentPlayer(mGoogleApiClient);
        String displayName;
        if (p == null) {
            Log.w(TAG, "mGamesClient.getCurrentPlayer() is NULL!");
            displayName = "???";
        } else {
            displayName = p.getDisplayName();
        }
        mStartScreenFragment.setGreeting("Hello, " + displayName);

        // if we have accomplishments to push, push them
        if (!mOutbox.isEmpty()) {
            pushAccomplishments();
            Toast.makeText(this, getString(R.string.your_progress_will_be_uploaded),
                    Toast.LENGTH_LONG).show();
        }
    }

    //----------------------------------------------------------------------------------------------
    //  onConnectionSuspended
    //
    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended(): attempting to connect");
        mGoogleApiClient.connect();
    }

    //----------------------------------------------------------------------------------------------
    //  onConnectionFailed
    //
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed(): attempting to resolve");
        if (mResolvingConnectionFailure) {
            Log.d(TAG, "onConnectionFailed(): already resolving");
            return;
        }

        // if the sign-in button was clicked or if auto sign-in is enabled,
        // launch the sign-in flow
        if (mSignInClicked || mAutoStartSignInFlow) {
            mAutoStartSignInFlow = false;
            mSignInClicked = false;
            mResolvingConnectionFailure = true;

            // Attempt to resolve the connection failure using BaseGameUtils.
            // The R.string.signin_other_error value should reference a generic
            // error string in your strings.xml file, such as "There was
            // an issue with sign-in, please try again later."
            if (!BaseGameUtils.resolveConnectionFailure(this,
                    mGoogleApiClient, connectionResult,
                    RC_SIGN_IN, getString(R.string.signin_other_error))) {
                mResolvingConnectionFailure = false;
            }
        }

        mStartScreenFragment.setGreeting(getString(R.string.signed_out_greeting));
        mStartScreenFragment.setShowSignInButton(true);
    }


    //----------------------------------------------------------------------------------------------
    //  onSignInButtonClicked
    //
    @Override
    public void onSignInButtonClicked() {
        // start the sign-in flow
        mSignInClicked = true;
        mGoogleApiClient.connect();
    }

    //----------------------------------------------------------------------------------------------
    //  onSignOutButtonClicked
    //
    @Override
    public void onSignOutButtonClicked() {
        mSignInClicked = false;
        Games.signOut(mGoogleApiClient);
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }

        mStartScreenFragment.setGreeting(getString(R.string.signed_out_greeting));
        mStartScreenFragment.setShowSignInButton(true);
    }

    public Fragment getVisibleFragment(){
        FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if(fragments != null){
            for(Fragment fragment : fragments){
                if(fragment != null && fragment.isVisible())
                    return fragment;
            }
        }
        return null;
    }

    @Override
    public void onBackPressed() {
        Fragment active_frag = getVisibleFragment();
        if (active_frag != null && active_frag.getTag().equals("mSinglePlayerFragment")) {
            onStartMenuRequested();
        }
        else {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(getString(R.string.app_name) + " schließen?")
                    .setCancelable(true)
                    .setMessage("Bist du sicher dass du " + getString(R.string.app_name) + " beenden möchtest?")
                    .setPositiveButton("Ja", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton("Nein", null)
                    .show();
        }

    }

    //----------------------------------------------------------------------------------------------
    // start SinglePlayer Game Mode
    //
    @Override
    public void onSinglePlayerRequested() {
        Intent gameIntent = new Intent(this, GameActivity.class);
        gameIntent.putExtra("enemies", mSinglePlayerFragment.getEnemies());
        gameIntent.putExtra("difficulty", mSinglePlayerFragment.getDifficulty());
        gameIntent.putExtra("player_lives", mSinglePlayerFragment.getPlayerLives());
        this.startActivity(gameIntent);
        switchToFragment(mLoadingScreenFragment, "mLoadingScreenFragment");
    }

    @Override
    public void onStartMenuRequested() {
        switchToFragment(mStartScreenFragment, "mStartScreenFragment");
    }

    class AccomplishmentsOutbox {
        boolean mPrimeAchievement = false;
        boolean mHumbleAchievement = false;
        boolean mLeetAchievement = false;
        boolean mArrogantAchievement = false;
        int mBoredSteps = 0;
        int mEasyModeScore = -1;
        int mHardModeScore = -1;

        boolean isEmpty() {
            return !mPrimeAchievement && !mHumbleAchievement && !mLeetAchievement &&
                    !mArrogantAchievement && mBoredSteps == 0 && mEasyModeScore < 0 &&
                    mHardModeScore < 0;
        }


        public void saveLocal(Context ctx) {
            /* TODO: This is left as an exercise. To make it more difficult to cheat,
             * this data should be stored in an encrypted file! And remember not to
             * expose your encryption key (obfuscate it by building it from bits and
             * pieces and/or XORing with another string, for instance). */
        }

        public void loadLocal(Context ctx) {
            /* TODO: This is left as an exercise. Write code here that loads data
             * from the file you wrote in saveLocal(). */
        }

    }

}
