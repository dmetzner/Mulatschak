package heroiceraser.mulatschak;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.games.AnnotatedData;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesActivityResultCodes;
import com.google.android.gms.games.GamesCallbackStatusCodes;
import com.google.android.gms.games.GamesClient;
import com.google.android.gms.games.GamesClientStatusCodes;
import com.google.android.gms.games.InvitationsClient;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.PlayersClient;
import com.google.android.gms.games.RealTimeMultiplayerClient;
import com.google.android.gms.games.event.Event;
import com.google.android.gms.games.event.EventBuffer;
import com.google.android.gms.games.multiplayer.Invitation;
import com.google.android.gms.games.multiplayer.InvitationCallback;
import com.google.android.gms.games.multiplayer.Multiplayer;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.realtime.OnRealTimeMessageReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.realtime.RoomStatusUpdateCallback;
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import at.heroiceraser.mulatschak.R;
import heroiceraser.mulatschak.Fragments.MultiPlayerSettingsFragment;
import heroiceraser.mulatschak.game.GameView;
import heroiceraser.mulatschak.Fragments.GameScreenFragment;
import heroiceraser.mulatschak.Fragments.LoadingScreenFragment;
import heroiceraser.mulatschak.Fragments.SinglePlayerFragment;
import heroiceraser.mulatschak.Fragments.StartScreenFragment;
import heroiceraser.mulatschak.helpers.LocaleHelper;


public class MainActivity extends FragmentActivity implements
        // StartScreen  -> Single/Multi MyPlayer, Sign in/out, Achievements, Leaderboards
        StartScreenFragment.Listener,
        // SinglePlayer Settings
        SinglePlayerFragment.Listener,
        // MultiPlayer Settings
         MultiPlayerSettingsFragment.Listener {

    // Fragments
    List<Fragment> fragList = new ArrayList<>();
    StartScreenFragment mStartScreenFragment;
    SinglePlayerFragment mSinglePlayerFragment;
    MultiPlayerSettingsFragment mMultiPlayerSettingsFragment;
    LoadingScreenFragment mLoadingScreenFragment;
    GameScreenFragment mGameScreenFragment;
    GameView mGameView;

    // tag for debug logging
    final String TAG = "MainActivity";

    // bool if an actual game is active
    private boolean gameRunning;
    private int waitForOnlineInteraction;
    private boolean waitForNewGame;

    // request codes we use when invoking an external activity
    private static final int RC_SIGN_IN = 9001;
    private static final int RC_ACHIEVEMENT_UI = 9003;
    private static final int RC_LEADERBOARD_UI = 9004;

    // Request codes for the UIs that we show with startActivityForResult:
    final static int RC_SELECT_PLAYERS = 10000;
    final static int RC_INVITATION_INBOX = 10001;
    final static int RC_WAITING_ROOM = 10002;

    // Client used to sign in with Google APIs
    private GoogleSignInClient mGoogleSignInClient = null;

    // The currently signed in account, used to check the account has changed outside of this activity when resuming.
    GoogleSignInAccount mSignedInAccount = null;

    private PlayersClient mPlayersClient;
    private String mPlayerId;
    public String  mDisplayName;

    // Client used to interact with the real time multiplayer system.
    private RealTimeMultiplayerClient mRealTimeMultiplayerClient = null;

    // Client used to interact with the Invitation system.
    private InvitationsClient mInvitationsClient = null;

    // Room ID where the currently active game is taking place; null if we're
    // not playing.
    String mRoomId = null;

    // Holds the configuration of the current room.
    RoomConfig mRoomConfig;

    // Are we playing in multiplayer mode?
    public boolean mMultiplayer = false;

    // The participants in the currently active game
    ArrayList<Participant> mParticipants = null;

    // My participant ID in the currently active game
    String myParticipantId = null;

    // If non-null, this is the id of the invitation we received via the
    // invitation listener
    String mIncomingInvitationId = null;

    // Message buffer for sending messages
    //byte[] mMsgBuf = new byte[2];

    // to save data local
    SharedPreferences mySharedPreference;



    //----------------------------------------------------------------------------------------------
    //----------------------- Handle Fragments -----------------------------------------------------
    //----------------------------------------------------------------------------------------------

    @Override
    public void onAttachFragment (Fragment fragment) {
        fragList.add(fragment);
    }

    //----------------------------------------------------------------------------------------------
    // getVisibleFragment
    //
    public Fragment getVisibleFragment(){
        try {
            List<Fragment> fragments = new ArrayList<>();
            fragments.addAll(fragList);
            for(Fragment fragment : fragments){
                if(fragment != null && fragment.isVisible())
                    return fragment;
            }
        }
        catch (Exception e) {
            Log.e(TAG, "getVisibleFragment" + e);
        }
        return null;
    }

    //----------------------------------------------------------------------------------------------
    // Switch UI to the given fragment
    //
    public void switchToFragment(Fragment newFrag, String id) {
        try {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, newFrag, id)
                    .commitAllowingStateLoss();
        }
        catch (Exception e) {
            Log.e(TAG, "switchToFragment" + e);
        }
    }

    @Override
    public void onStartMenuRequested() {
        switchToFragment(mStartScreenFragment, "mStartScreenFragment");
    }

    public void requestLoadingScreen() {
        switchToFragment(mLoadingScreenFragment, "mLoadingScreenFragment");
    }


    //----------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------

    //----------------------------------------------------------------------------------------------
    //  onCreate
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "**** got onCreate");

        gameRunning = false;
        waitForOnlineInteraction = 0;
        waitForNewGame = false;
        
        // supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_activity);

        // set language
        LocaleHelper.setLocale(this, LocaleHelper.getLanguage(this));

        // create fragments
        mStartScreenFragment =          new StartScreenFragment();
        mSinglePlayerFragment =         new SinglePlayerFragment();
        mMultiPlayerSettingsFragment =  new MultiPlayerSettingsFragment();
        mLoadingScreenFragment =        new LoadingScreenFragment();
        mGameScreenFragment =           new GameScreenFragment();

        mStartScreenFragment.setListener(this);
        mSinglePlayerFragment.setListener(this);
        mMultiPlayerSettingsFragment.setListener(this);

        // add initial fragment (welcome fragment)
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,
                mStartScreenFragment).commit();

        mDisplayName = "";
        // load sign in preference
        // Create the Google Api Client with access to the Play Game and Drive services.
        mGoogleSignInClient = GoogleSignIn.getClient(this, new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
                .requestScopes(Drive.SCOPE_APPFOLDER)
                .build());


        mySharedPreference = getSharedPreferences("MyMuliData", 0);
    }


    //----------------------------------------------------------------------------------------------
    // onStart()
    //
    @Override
    public void onStart() {
        Log.d(TAG, "**** got onSart");
        super.onStart();
    }

    //----------------------------------------------------------------------------------------------
    // onResume()
    //
    @Override
    protected void onResume() {

        super.onResume();
        Log.d(TAG, "**** got onResume");

        if (gameRunning) {
            keepScreenOn();
        }

        Fragment frag = getVisibleFragment();
        if (frag != null && frag.equals(mGameScreenFragment) && !gameRunning) {
            onStartMenuRequested();
        }

        // Since the state of the signed in user can change when the activity is not active
        // it is recommended to try and sign in silently from when the app resumes.
        signInSilently();
    }

    //----------------------------------------------------------------------------------------------
    // onResume()
    //
    @Override
    protected void onPause() {
        super.onPause();

        // unregister our listeners.  They will be re-registered via onResume->signInSilently->onConnected.
        if (mInvitationsClient != null) {
            mInvitationsClient.unregisterInvitationCallback(mInvitationCallback);
        }
    }


    //----------------------------------------------------------------------------------------------
    // onStop()
    //
    // Activity is going to the background. We have to leave the current room.
    @Override
    public void onStop() {
        Log.d(TAG, "**** got onStop");

        // if we're in a room, leave it.        // not happy with the api -> prefer resource leaks
        // leaveRoom();                        // ToDo find better idea

        // stop trying to keep the screen on
        stopKeepingScreenOn();

        super.onStop();
    }

    //----------------------------------------------------------------------------------------------
    // onDestroy()
    //
    @Override
    public void onDestroy() {
        Log.d(TAG, "**** got onDestroy");

        leaveRoom();// not happy with the api -> prefer resource leaks // ToDo find better idea
        super.onDestroy();
    }



    //----------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------

    public boolean isSignedIn() {
        return GoogleSignIn.getLastSignedInAccount(this) != null;
    }

    //
    // Start a sign in activity.  To properly handle the result, call tryHandleSignInResult from
    // your Activity's onActivityResult function
    //
    public void startSignInIntent() {
        try {
            startActivityForResult(mGoogleSignInClient.getSignInIntent(), RC_SIGN_IN);
        }
        catch (Exception e) {
            Log.e(TAG, "startSignInIntent " + e);
        }
    }

    //
    // Try to sign in without displaying dialogs to the user.
    // <p>
    // If the user has already signed in previously, it will not show dialog.
    //
    public void signInSilently() {

        try {
            Log.d(TAG, "signInSilently()");

            mGoogleSignInClient.silentSignIn().addOnCompleteListener(this,
                    new OnCompleteListener<GoogleSignInAccount>() {
                        @Override
                        public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "signInSilently(): success");
                                onConnected(task.getResult());
                            } else {
                                Log.d(TAG, "signInSilently(): failure", task.getException());
                                onDisconnected();
                            }
                        }
                    });
        } catch (Exception e) {
            Log.e(TAG, "signInSilently " + e);
        }
    }

    public void signOut() {
        try {
            Log.d(TAG, "signOut()");

            if (!isSignedIn()) {
                Log.w(TAG, "signOut() called, but was not signed in!");
                return;
            }

            mGoogleSignInClient.signOut().addOnCompleteListener(this,
                    new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                Log.d(TAG, "signOut(): success");
                            } else {
                                handleException(task.getException(), "signOut() failed!");
                            }

                            onDisconnected();
                        }
                    });
        } catch (Exception e) {
            Log.e(TAG, "signOut " + e);
        }
    }


    //----------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------

    /**
     * Since a lot of the operations use tasks, we can use a common handler for whenever one fails.
     *
     * @param exception The exception to evaluate.  Will try to display a more descriptive reason for the exception.
     * @param details   Will display alongside the exception if you wish to provide more details for why the exception
     *                  happened
     */
    private void handleException(Exception exception, String details) {
        int status = 0;

        if (exception instanceof ApiException) {
            ApiException apiException = (ApiException) exception;
            status = apiException.getStatusCode();
        }

        String errorString = null;
        switch (status) {
            case GamesCallbackStatusCodes.OK:
                break;
            case GamesClientStatusCodes.MULTIPLAYER_ERROR_NOT_TRUSTED_TESTER:
                errorString = "getString(R.string.status_multiplayer_error_not_trusted_tester)";
                break;
            case GamesClientStatusCodes.MATCH_ERROR_ALREADY_REMATCHED:
                errorString = "getString(R.string.match_error_already_rematched)";
                break;
            case GamesClientStatusCodes.NETWORK_ERROR_OPERATION_FAILED:
                errorString = "getString(R.string.network_error_operation_failed)";
                break;
            case GamesClientStatusCodes.INTERNAL_ERROR:
                errorString = "getString(R.string.internal_error)";
                break;
            case GamesClientStatusCodes.MATCH_ERROR_INACTIVE_MATCH:
                errorString = "getString(R.string.match_error_inactive_match)";
                break;
            case GamesClientStatusCodes.MATCH_ERROR_LOCALLY_MODIFIED:
                errorString = "getString(R.string.match_error_locally_modified)";
                break;
            default:
                errorString = "getString(R.string.unexpected_status, GamesClientStatusCodes.getStatusCodeString(status))";
                break;
        }

        if (errorString == null) {
            return;
        }

        String message = "getString(R.string.status_exception_error, details, status, exception)";

        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Error")
                .setMessage(message + "\n" + errorString)
                .setNeutralButton(android.R.string.ok, null)
                .show();
    }

    void startQuickGame() {
        try {
            // quick-start a game with 1 randomly selected opponent
            final int MIN_OPPONENTS = 1, MAX_OPPONENTS = 3;
            Bundle autoMatchCriteria = RoomConfig.createAutoMatchCriteria(MIN_OPPONENTS,
                    MAX_OPPONENTS, 0);

            keepScreenOn();
            messageQueue.clear();
            correctLeftPeers.clear();

            mRoomConfig = RoomConfig.builder(mRoomUpdateCallback)
                    .setOnMessageReceivedListener(mOnRealTimeMessageReceivedListener)
                    .setRoomStatusUpdateCallback(mRoomStatusUpdateCallback)
                    .setAutoMatchCriteria(autoMatchCriteria)
                    .build();
            mRealTimeMultiplayerClient.create(mRoomConfig);
        }
        catch (Exception e) {
            Log.e(TAG, "startQuickGame exception " + e);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        try {
            if (requestCode == RC_SIGN_IN) {

                Task<GoogleSignInAccount> task =
                        GoogleSignIn.getSignedInAccountFromIntent(intent);

                try {
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    onConnected(account);
                } catch (ApiException apiException) {
                    String message = apiException.getMessage();
                    if (message == null || message.isEmpty()) {
                        message = getString(R.string.signin_other_error);
                    }

                    onDisconnected();

                    new AlertDialog.Builder(this)
                            .setMessage(message)
                            .setNeutralButton(android.R.string.ok, null)
                            .show();
                }
            } else if (requestCode == RC_SELECT_PLAYERS) {
                // we got the result from the "select players" UI -- ready to create the room
                handleSelectPlayersResult(resultCode, intent);

            } else if (requestCode == RC_INVITATION_INBOX) {
                // we got the result from the "select invitation" UI (invitation inbox). We're
                // ready to accept the selected invitation:
                handleInvitationInboxResult(resultCode, intent);

            } else if (requestCode == RC_WAITING_ROOM) {
                // we got the result from the "waiting room" UI.
                if (resultCode == Activity.RESULT_OK) {
                    // ready to start playing
                    Log.d(TAG, "Starting game (waiting room returned OK).");
                    startGamePreparation(true);
                } else if (resultCode == GamesActivityResultCodes.RESULT_LEFT_ROOM) {
                    // player indicated that they want to leave the room
                    leaveRoom();
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    // Dialog was cancelled (user pressed back key, for instance). In our game,
                    // this means leaving the room too. In more elaborate games, this could mean
                    // something else (like minimizing the waiting room UI).
                    leaveRoom();
                }
            }
            else if (requestCode == RC_ACHIEVEMENT_UI) {

            }
            super.onActivityResult(requestCode, resultCode, intent);
        }
        catch (Exception e) {
            Log.e(TAG, "onActivityResult exception " + e);
        }
    }

    // Handle the result of the "Select players UI" we launched when the user clicked the
    // "Invite friends" button. We react by creating a room with those players.

    private void handleSelectPlayersResult(int response, Intent data) {
        try {
            if (response != Activity.RESULT_OK) {
                Log.w(TAG, "*** select players UI cancelled, " + response);
                onStartMenuRequested();
                return;
            }

            Log.d(TAG, "Select players UI succeeded.");

            // get the invitee list
            final ArrayList<String> invitees = data.getStringArrayListExtra(Games.EXTRA_PLAYER_IDS);
            Log.d(TAG, "Invitee count: " + invitees.size());

            // get the automatch criteria
            Bundle autoMatchCriteria = null;
            int minAutoMatchPlayers = data.getIntExtra(Multiplayer.EXTRA_MIN_AUTOMATCH_PLAYERS, 0);
            int maxAutoMatchPlayers = data.getIntExtra(Multiplayer.EXTRA_MAX_AUTOMATCH_PLAYERS, 0);
            if (minAutoMatchPlayers > 0 || maxAutoMatchPlayers > 0) {
                autoMatchCriteria = RoomConfig.createAutoMatchCriteria(
                        minAutoMatchPlayers, maxAutoMatchPlayers, 0);
                Log.d(TAG, "Automatch criteria: " + autoMatchCriteria);
            }

            // create the room
            Log.d(TAG, "Creating room...");
            requestLoadingScreen();
            messageQueue.clear();
            correctLeftPeers.clear();
            keepScreenOn();
            playedWithAFriend = true;

            mRoomConfig = RoomConfig.builder(mRoomUpdateCallback)
                    .addPlayersToInvite(invitees)
                    .setOnMessageReceivedListener(mOnRealTimeMessageReceivedListener)
                    .setRoomStatusUpdateCallback(mRoomStatusUpdateCallback)
                    .setAutoMatchCriteria(autoMatchCriteria).build();
            mRealTimeMultiplayerClient.create(mRoomConfig);
            Log.d(TAG, "Room created, waiting for it to be ready...");
        }
        catch (Exception e) {
            Log.e(TAG, "handleSelectPlayersResult exception: " + e);
        }
    }

    // Handle the result of the invitation inbox UI, where the player can pick an invitation
    // to accept. We react by accepting the selected invitation, if any.
    private void handleInvitationInboxResult(int response, Intent data) {
        try {
            if (response != Activity.RESULT_OK) {
                Log.w(TAG, "*** invitation inbox UI cancelled, " + response);
                onStartMenuRequested();
                return;
            }

            Log.d(TAG, "Invitation inbox UI succeeded.");
            Invitation invitation = null;
            if (data.getExtras() != null) {
                invitation = data.getExtras().getParcelable(Multiplayer.EXTRA_INVITATION);
            }

            // accept invitation
            if (invitation != null) {
                acceptInviteToRoom(invitation.getInvitationId());
            }
        }
        catch (Exception e) {
            Log.e(TAG, "handleInvitationInboxResult exception: " + e);
        }
    }

    // Accept the given invitation.
    void acceptInviteToRoom(String invitationId) {
        try {
            // accept the invitation
            Log.d(TAG, "Accepting invitation: " + invitationId);

            mRoomConfig = RoomConfig.builder(mRoomUpdateCallback)
                    .setInvitationIdToAccept(invitationId)
                    .setOnMessageReceivedListener(mOnRealTimeMessageReceivedListener)
                    .setRoomStatusUpdateCallback(mRoomStatusUpdateCallback)
                    .build();

            requestLoadingScreen();
            messageQueue.clear();
            correctLeftPeers.clear();
            keepScreenOn();

            playedWithAFriend = true;

            mRealTimeMultiplayerClient.join(mRoomConfig)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "Room Joined Successfully!");
                        }
                    });
        }  catch (Exception e) {
            Log.e(TAG, "handleInvitationInboxResult exception: " + e);
        }
    }


    // Leave the room.
    void leaveRoom() {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mMultiplayer = false;
                    Log.d(TAG, "Leaving room.");
                    messageQueue.clear();
                    onStartMenuRequested();
                    if (mRoomId != null) {
                        mRealTimeMultiplayerClient.leave(mRoomConfig, mRoomId)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        mRoomId = null;
                                        mRoomConfig = null;
                                    }
                                });
                        onStartMenuRequested();
                    } else {
                        Log.d(TAG, "Leaving room.... but we are in no room");
                        onStartMenuRequested();
                    }
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "leave room exceptio " + e);
        }
    }

    void onlyLeaveRoom() {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mMultiplayer = false;
                    Log.d(TAG, "Leaving room.");
                    messageQueue.clear();
                    if (mRoomId != null) {
                        mRealTimeMultiplayerClient.leave(mRoomConfig, mRoomId)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        mRoomId = null;
                                        mRoomConfig = null;
                                    }
                                });
                    } else {
                        Log.d(TAG, "Leaving room.... but we are in no room");
                    }
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "leave room exceptio " + e);
        }
    }

    // Show the waiting room UI to track the progress of other players as they enter the
    // room and get connected.
    void showWaitingRoom(Room room) {
        try {
            if (room == null) {
                return;
            }
            // minimum number of players required for our game
            final int MIN_PLAYERS = Integer.MAX_VALUE;
            mRealTimeMultiplayerClient.getWaitingRoomIntent(room, MIN_PLAYERS)
                    .addOnSuccessListener(new OnSuccessListener<Intent>() {
                        @Override
                        public void onSuccess(Intent intent) {
                            // show waiting room UI
                            startActivityForResult(intent, RC_WAITING_ROOM);
                        }
                    })
                    .addOnFailureListener(createFailureListener("There was a problem getting the waiting room!"));
        }
        catch (Exception e) {
            Log.e(TAG, "showWaitingRoom exception: " + e);
        }

    }

    private InvitationCallback mInvitationCallback = new InvitationCallback() {
        // Called when we get an invitation to play a game. We react by showing that to the user.
        @Override
        public void onInvitationReceived(@NonNull Invitation invitation) {
            // We got an invitation to play a game! So, store it in
            // mIncomingInvitationId
            // and show the popup on the screen.
            mIncomingInvitationId = invitation.getInvitationId();
            mStartScreenFragment.setInvitationPopUpVisibility(View.VISIBLE,
                    invitation.getInviter().getDisplayName() + " " +
                    getString(R.string.is_inviting_you));
        }

        @Override
        public void onInvitationRemoved(@NonNull String invitationId) {

            if (mIncomingInvitationId != null && mIncomingInvitationId.equals(invitationId)) {
                mIncomingInvitationId = null;
                Log.d("Tag", "nice");
                mStartScreenFragment.setInvitationPopUpVisibility(View.GONE, "");
            }
        }
    };

    @Override
    public void onInvitationPopUpAccepted() {
        // user wants to accept the invitation shown on the invitation popup
        // (the one we got through the OnInvitationReceivedListener).
        if (mIncomingInvitationId != null) {
            acceptInviteToRoom(mIncomingInvitationId);
            mIncomingInvitationId = null;
        }
    }

    @Override
    public void onInvitationPopUpDeclined() {
        // user wants to accept the invitation shown on the invitation popup
        // (the one we got through the OnInvitationReceivedListener).
        mIncomingInvitationId = null;
        mStartScreenFragment.setInvitationPopUpVisibility(View.GONE, "");
        // can still be found in all invitations
    }

    /*
     * CALLBACKS SECTION. This section shows how we implement the several games
     * API callbacks.
     */

    private void onConnected(GoogleSignInAccount googleSignInAccount) {
        try {
            Log.d(TAG, "onConnected(): connected to Google APIs");

            // Show sign-out button on main menu
            mStartScreenFragment.handleSignIn(false);
            mStartScreenFragment.setGreeting(getString(R.string.signed_in_greeting, mDisplayName));

            if (mSignedInAccount != googleSignInAccount) {

                mSignedInAccount = googleSignInAccount;

                // update the clients
                mRealTimeMultiplayerClient = Games.getRealTimeMultiplayerClient(this, googleSignInAccount);
                mInvitationsClient = Games.getInvitationsClient(MainActivity.this, googleSignInAccount);

                // get the playerId from the PlayersClient
                mPlayersClient = Games.getPlayersClient(this, googleSignInAccount);
                mPlayersClient.getCurrentPlayer()
                        .addOnSuccessListener(new OnSuccessListener<Player>() {
                            @Override
                            public void onSuccess(Player player) {
                                mPlayerId = player.getPlayerId();
                            }
                        })
                        .addOnFailureListener(createFailureListener("There was a problem getting the player id!"));
                // Set the greeting appropriately on main menu
                mPlayersClient.getCurrentPlayer()
                        .addOnCompleteListener(new OnCompleteListener<Player>() {
                            @Override
                            public void onComplete(@NonNull Task<Player> task) {
                                if (task.isSuccessful()) {
                                    mDisplayName = task.getResult().getDisplayName();
                                } else {
                                    Exception e = task.getException();
                                    handleException(e, "getString(R.string.players_exception)");
                                    mDisplayName = "???";
                                }
                                mStartScreenFragment.setGreeting(getString(R.string.signed_in_greeting, mDisplayName));
                            }
                        });
            }

            // register listener so we are notified if we receive an invitation to play
            // while we are in the game
            mInvitationsClient.registerInvitationCallback(mInvitationCallback);

            // get the invitation from the connection hint
            // Retrieve the TurnBasedMatch from the connectionHint
            GamesClient gamesClient = Games.getGamesClient(MainActivity.this, googleSignInAccount);
            gamesClient.getActivationHint()
                    .addOnSuccessListener(new OnSuccessListener<Bundle>() {
                        @Override
                        public void onSuccess(Bundle hint) {
                            if (hint != null) {
                                Invitation invitation =
                                        hint.getParcelable(Multiplayer.EXTRA_INVITATION);

                                if (invitation != null && invitation.getInvitationId() != null) {
                                    // retrieve and cache the invitation ID
                                    Log.d(TAG, "onConnected: connection hint has a room invite!");
                                    acceptInviteToRoom(invitation.getInvitationId());
                                }
                            }
                        }
                    })
                    .addOnFailureListener(createFailureListener("There was a problem getting the activation hint!"));
        }
        catch (Exception e) {
            Log.e(TAG, "onConnected exception: " + e);
        }

    }

    private OnFailureListener createFailureListener(final String string) {
        return new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                handleException(e, string);
            }
        };
    }

    public void onDisconnected() {
        try {
            Log.d(TAG, "onDisconnected()");

            mRealTimeMultiplayerClient = null;
            mInvitationsClient = null;

            mStartScreenFragment.handleSignIn(true);
            mStartScreenFragment.setGreeting(getString(R.string.signed_out_greeting));
        }
        catch (Exception e) {
            Log.e(TAG, "onDisconnected exception: " + e);
        }
    }


    private RoomStatusUpdateCallback mRoomStatusUpdateCallback = new RoomStatusUpdateCallback() {
        // Called when we are connected to the room. We're not ready to play yet! (maybe not everybody
        // is connected yet).
        @Override
        public void onConnectedToRoom(Room room) {
            try {
                Log.d(TAG, "onConnectedToRoom.");

                //get participants and my ID:
                mParticipants = room.getParticipants();
                myParticipantId = room.getParticipantId(mPlayerId);

                // save room ID if its not initialized in onRoomCreated() so we can leave cleanly before the game starts.
                if (mRoomId == null) {
                    mRoomId = room.getRoomId();
                }

                // print out the list of participants (for debug purposes)
                Log.d(TAG, "Room ID: " + mRoomId);
                Log.d(TAG, "My ID " + myParticipantId);
                Log.d(TAG, "<< CONNECTED TO ROOM>>");
            }
            catch (Exception e) {
                Log.e(TAG, "onConnected exception: " + e);
            }
        }

        // Called when we get disconnected from the room. We return to the main screen.
        @Override
        public void onDisconnectedFromRoom(Room room) {
            try {
                Log.d(TAG, "onDisconnectedToRoom.");
                onlyLeaveRoom();
                ArrayList<String> leftPeers = new ArrayList<>();
                for (Participant p : mParticipants) {
                    leftPeers.add(p.getParticipantId());
                }

                updatePlayer(leftPeers);
                // showGameError(); -> we can stay in the game and play against bots ;)
            }
            catch (Exception e) {
                Log.e(TAG, "onDisconnected exception: " + e);
            }

        }


        // We treat most of the room update callbacks in the same way: we update our list of
        // participants and update the display. In a real game we would also have to check if that
        // change requires some action like removing the corresponding player avatar from the screen,
        // etc.
        @Override
        public void onPeerDeclined(Room room, @NonNull List<String> arg1) {
            updateRoom(room);
        }

        @Override
        public void onPeerInvitedToRoom(Room room, @NonNull List<String> arg1) {
            updateRoom(room);
        }

        @Override
        public void onP2PDisconnected(@NonNull String participant) { }

        @Override
        public void onP2PConnected(@NonNull String participant) {
        }

        @Override
        public void onPeerJoined(Room room, @NonNull List<String> arg1) {
            updateRoom(room);
        }

        @Override
        public void onPeerLeft(Room room, @NonNull List<String> peersWhoLeft) {
            updateRoom(room, peersWhoLeft);
        }

        @Override
        public void onRoomAutoMatching(Room room) {
            updateRoom(room);
        }

        @Override
        public void onRoomConnecting(Room room) {
            updateRoom(room);
        }

        @Override
        public void onPeersConnected(Room room, @NonNull List<String> peers) {
            updateRoom(room);
        }

        @Override
        public void onPeersDisconnected(Room room, @NonNull List<String> peers) {
            updateRoom(room);
        }
    };

    // Show error message about game being cancelled and return to main screen.
    void showGameError() {
        new AlertDialog.Builder(this)
                .setMessage("getString(R.string.game_problem)")
                .setNeutralButton(android.R.string.ok, null).create();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                endGame();
                onStartMenuRequested();
            }
        });
    }

    private RoomUpdateCallback mRoomUpdateCallback = new RoomUpdateCallback() {

        // Called when room has been created
        @Override
        public void onRoomCreated(int statusCode, Room room) {
            try {
                Log.d(TAG, "onRoomCreated(" + statusCode + ", " + room + ")");
                if (statusCode != GamesCallbackStatusCodes.OK) {
                    Log.e(TAG, "*** Error: onRoomCreated, status " + statusCode);
                    showGameError();
                    return;
                }

                // save room ID so we can leave cleanly before the game starts.
                mRoomId = room.getRoomId();

                // show the waiting room UI
                showWaitingRoom(room);
            }
            catch (Exception e) {
                Log.e(TAG, "onRoomCreated exception: " + e);
            }

        }

        // Called when room is fully connected.
        @Override
        public void onRoomConnected(int statusCode, Room room) {
            try {
                Log.d(TAG, "onRoomConnected(" + statusCode + ", " + room + ")");
                if (statusCode != GamesCallbackStatusCodes.OK) {
                    Log.e(TAG, "*** Error: onRoomConnected, status " + statusCode);
                    showGameError();
                    return;
                }
                // save room ID so we can leave cleanly before the game starts.
                mRoomId = room.getRoomId();
                updateRoom(room);
            }
            catch (Exception e) {
                Log.e(TAG, "onRoomConnected exception: " + e);
            }

        }

        @Override
        public void onJoinedRoom(int statusCode, Room room) {
            try {
                Log.d(TAG, "onJoinedRoom(" + statusCode + ", " + room + ")");
                if (statusCode != GamesCallbackStatusCodes.OK) {
                    Log.e(TAG, "*** Error: onRoomConnected, status " + statusCode);
                    showGameError();
                    return;
                }
                // save room ID so we can leave cleanly before the game starts.
                mRoomId = room.getRoomId();
                // show the waiting room UI
                showWaitingRoom(room);
            }
            catch (Exception e) {
                Log.e(TAG, "onJoinedRoom exception: " + e);
            }

        }

        // Called when we've successfully left the room (this happens a result of voluntarily leaving
        // via a call to leaveRoom(). If we get disconnected, we get onDisconnectedFromRoom()).
        @Override
        public void onLeftRoom(int statusCode, @NonNull String roomId) {
            try {
                Log.d(TAG, "onLeftRoom, code " + statusCode);
                if (!gameRunning) {
                    onStartMenuRequested();
                }
            }
            catch (Exception e) {
                Log.e(TAG, "onLeftRoom exception: " + e);
            }

        }
    };

    void updateRoom(Room room) {
        if (room != null) {
            mParticipants = room.getParticipants();
        }
    }

    private ArrayList<String> correctLeftPeers = new ArrayList<>();

    void updateRoom(Room room, @NonNull List<String> peersWhoLeft) {
        updateRoom(room);
        updatePlayer(peersWhoLeft);
    }

    private void updatePlayer(@NonNull List<String> peersWhoLeft) {
        try {
            // if (peersWhoLeft.contains(myParticipantId)) {
               // return; // this player left the room too -> so there is nothing to do!
            // }

            if (mParticipants.size() - peersWhoLeft.size() - correctLeftPeers.size() < 2) {
                for (final String leftPeer : peersWhoLeft) {

                    if (leftPeer.equals(myParticipantId)) {
                        continue;
                    }

                    if (correctLeftPeers.contains(leftPeer)) {
                        continue;
                    }

                    // double secure -> check if game is over...
                    if (mGameView != null && mGameView.getController() != null &&
                            mGameView.getController().getGameOver() != null &&
                            mGameView.getController().getGameOver().isVisible()) {
                        return;
                    }

                    // left in the middle of the game, but still leaves the game correct
                    correctLeftPeers.add(leftPeer);

                    // so a player left in the middle od a game? :(
                    // tell the player
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (gameRunning && mGameView != null && mGameView.getController() != null
                                    && mGameView.getController().isGameStarted()) {
                                Log.d(TAG, "Bye bye " + leftPeer);
                                mGameView.getController().handleLeftPeer(leftPeer);
                            }
                            else if (gamePreparationRunning || gameRunning) {
                                Log.d(TAG, "Bye bye pre " + leftPeer);
                                // ToDo Show message Error
                                endGame();
                            }
                        }
                    });

                }
            }

            if (mParticipants.size() - peersWhoLeft.size() - correctLeftPeers.size() >= 2) {
                for (final String leftPeer: peersWhoLeft) {

                    if (leftPeer.equals(myParticipantId)) {
                        continue;
                    }

                    if (correctLeftPeers.contains(leftPeer)) {
                        continue;
                    }
                    // double secure -> check if game is over...
                    if (mGameView != null && mGameView.getController() != null &&
                            mGameView.getController().getGameOver() != null &&
                            mGameView.getController().getGameOver().isVisible()) {
                        return;
                    }

                    // left in the middle of the game, but still leaves the game correct
                    correctLeftPeers.add(leftPeer);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // so a player left in the middle od a game? :(
                            // tell the player
                            if (gameRunning && mGameView != null && mGameView.getController() != null
                                    && mGameView.getController().isGameStarted()) {
                                Log.d(TAG, "Bye bye " + leftPeer);
                                mGameView.getController().handleLeftPeer(leftPeer);
                            }
                            else if (gamePreparationRunning || gameRunning) {
                                Log.d(TAG, "Bye bye pre " + leftPeer);
                                // ToDo Show message Error
                                endGame();
                            }
                        }
                    });

                }
            }
        }
        catch (Exception e) {
            Log.e(TAG, "updatePlayers exception: " + e);
        }

    }


    /*
     * COMMUNICATIONS SECTION. Methods that implement the game's network
     * protocol.
     */
    
    //  data structure to save incoming messages, till they were processed
    public volatile List<Message> messageQueue = new ArrayList<>();
   
    // used to (un)marshall messages
    private Gson gson = new Gson();

    //
    // Called when we receive a real-time message from the network.
    //          -> converts the byte message into an Message object and
    //              calls workOnMessageQueue 
    //
    OnRealTimeMessageReceivedListener mOnRealTimeMessageReceivedListener = new OnRealTimeMessageReceivedListener() {
        @Override
        public void onRealTimeMessageReceived(@NonNull RealTimeMessage realTimeMessage) {
            try {
                // don't need messages for singleplayer
                if (!mMultiplayer) {
                    return;
                }
                byte[] buf = realTimeMessage.getMessageData();
                String data = new String(buf);
                Message message = gson.fromJson(data, Message.class);
                // Log.d(TAG, "---------------------------------------");
                // Log.d(TAG, "Message received: type " + message.type);
                // Log.d(TAG, "GameState " + gameState);
                if (correctLeftPeers.contains(message.senderId)) {
                    return;
                }
                messageQueue.add(message);
                if (messageQueue.size() > 2 && messageQueue.get(messageQueue.size() - 1).type ==
                        messageQueue.get(messageQueue.size() - 2).type) {
                    messageQueue.remove(messageQueue.size() - 1);
                    Log.d(TAG, "Message removed");
                }
                else {
                    workOnMessageQueue();
                }

            }
            catch (Exception e) {
                Log.e(TAG, "message received exception " + e);
            }

        }
    };



    //
    //  starts workOnMessageQueue again after a short delay
    //
    private void tryWorkOnMessageAgainLater() {
        /*Handler h = new Handler();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                workOnMessageQueue();
            }
        };
        h.postDelayed(r, 50);
        */
        messageQueue.remove(0);
    }

    //
    // Broadcast message (uses reliable)
    //
    public void broadcastMessage(int type, String data) {

        try {
            // playing single-player mode -> nothing to send
            if (!mMultiplayer) {
                return;
            }

            // create message
            Message message = new Message();
            message.type = type;
            message.senderId = myParticipantId;
            message.data = data;
            byte[] msgBuf =  null;
            try {
                msgBuf = gson.toJson(message).getBytes("UTF-8");
            }
            catch (Exception e) {
                Log.d(TAG, "send message -> charset");
            }
            if (msgBuf == null) {
                return;
            }

            broadcastUnreliable(msgBuf, message);

        }
        catch (Exception e) {
            Log.e(TAG, "broadcast message exception " + e);
        }
    }


    //
    // Broadcast reliable
    //
    private void broadcastReliable(byte[] msgBuf) {
        try {
            for (Participant p : mParticipants) {
                if (p.getParticipantId().equals(myParticipantId)) {
                    continue;
                }
                if (p.getStatus() != Participant.STATUS_JOINED) {
                    continue;
                }
                if (correctLeftPeers.contains(p.getParticipantId())) {
                    continue;
                }
                // final score notification must be sent via reliable message
                mRealTimeMultiplayerClient.sendReliableMessage(msgBuf,
                        mRoomId, p.getParticipantId(), new RealTimeMultiplayerClient.ReliableMessageSentCallback() {
                            @Override
                            public void onRealTimeMessageSent(int statusCode, int tokenId, String recipientParticipantId) {
                                Log.d(TAG, "RealTime message sent");
                                Log.d(TAG, "  statusCode: " + statusCode);
                                Log.d(TAG, "  tokenId: " + tokenId);
                                Log.d(TAG, "  recipientParticipantId: " + recipientParticipantId);
                            }
                        })
                        .addOnSuccessListener(new OnSuccessListener<Integer>() {
                            @Override
                            public void onSuccess(Integer tokenId) {
                                Log.d(TAG, "Created a reliable message with tokenId: " + tokenId);
                            }
                        });
            }
        }
        catch (Exception e) {
            Log.e(TAG, "broadCastReliable exception: " + e);
        }
    }

    public void sendUnReliable(String sendToId, int type, String data) {

        try {
            // playing single-player mode -> nothing to send
            if (!mMultiplayer) {
                return;
            }

            if (sendToId.equals("")) {
                return;
            }

            // create message
            Message message = new Message();
            message.type = type;
            message.senderId = myParticipantId;
            message.data = data;
            byte[] msgBuf = null;
            try {
                msgBuf = gson.toJson(message).getBytes("UTF-8");
            }
            catch (Exception e) {
                Log.d(TAG, "send message -> charset");
            }
            if (msgBuf == null) {
                return;
            }

            if (sendToId.equals(myParticipantId)) {
                return;
            }
            for (Participant p : mParticipants) {
                if (p.getParticipantId().equals(sendToId) &&
                        p.getStatus() != Participant.STATUS_JOINED) {
                    return;
                }
            }

            if (correctLeftPeers.contains(sendToId)) {
                return;
            }
            // it's an interim score notification, so we can use unreliable
            mRealTimeMultiplayerClient.sendUnreliableMessage(msgBuf, mRoomId,
                    sendToId);

           // Log.d(TAG, "Message sent: " + message.type + "-- to: " + sendToId);
        }
        catch (Exception e) {
            Log.e(TAG, "broadCastUnReliable exception: " + e);
        }
    }


    private void broadcastUnreliable(byte[] msgBuf, Message message) {
        try {
            for (Participant p : mParticipants) {
                if (p.getParticipantId().equals(myParticipantId)) {
                    continue;
                }
                if (p.getStatus() != Participant.STATUS_JOINED) {
                    continue;
                }
                if (correctLeftPeers.contains(p.getParticipantId())) {
                    continue;
                }
                // it's an interim score notification, so we can use unreliable
                mRealTimeMultiplayerClient.sendUnreliableMessage(msgBuf, mRoomId,
                        p.getParticipantId());

                // Log.d(TAG, "Message sent: " + message.type);
            }
        }
        catch (Exception e) {
            Log.e(TAG, "onbroadcastUnreliable exception: " + e);
        }

    }



    //----------------------------------------------------------------------------------------------
    //  change language
    //
    @Override
    public void onChangeLanguage() {
        try {
            if (LocaleHelper.getLanguage(this).equals("de")) {
                LocaleHelper.setLocale(this, "");
                mStartScreenFragment.updateLanguageIcon("");
            }
            else {
                LocaleHelper.setLocale(this, "de");
                mStartScreenFragment.updateLanguageIcon("de");
            }

            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
        catch (Exception e) {
            Log.e(TAG, "onChangeLanguage: " + e);
        }

    }

    //----------------------------------------------------------------------------------------------
    // start SinglePlayer Settings
    //
    @Override
    public void onSinglePlayerSettingsRequested() {
        switchToFragment(mSinglePlayerFragment, "mSinglePlayerFragment");
    }


    //----------------------------------------------------------------------------------------------
    // start Show Achievements
    //
    @Override
    public void onShowAchievementsRequested() {
        if (isSignedIn()) {
            showAchievements();
        }
    }

    private void showAchievements() {
        Games.getAchievementsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                .getAchievementsIntent()
                .addOnSuccessListener(new OnSuccessListener<Intent>() {
                    @Override
                    public void onSuccess(Intent intent) {
                        startActivityForResult(intent, RC_ACHIEVEMENT_UI);
                    }
                });
    }

    //----------------------------------------------------------------------------------------------
    // start Show LeaderBoards
    //
    @Override
    public void onShowLeaderboardsRequested() {
        if (isSignedIn()) {
            showLeaderboard();
        }
    }

    private void showLeaderboard() {
        Games.getLeaderboardsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                .getAllLeaderboardsIntent()
                .addOnSuccessListener(new OnSuccessListener<Intent>() {
                    @Override
                    public void onSuccess(Intent intent) {
                        startActivityForResult(intent, RC_LEADERBOARD_UI);
                    }
                });
    }


    //----------------------------------------------------------------------------------------------
    //  onSignInButtonClicked
    //
    @Override
    public void onSignInButtonClicked() {
        // start the sign-in flow
        Log.d(TAG, "Sign-in button clicked");
        startSignInIntent();
    }

    //----------------------------------------------------------------------------------------------
    //  onSignOutButtonClicked
    //
    @Override
    public void onSignOutButtonClicked() {
        // user wants to sign out
        // sign out.
        Log.d(TAG, "Sign-out button clicked");
        signOut();
    }

    @Override
    public void onMultiPlayerSettingsBackButtonRequested() {
        onBackPressed();
    }


    @Override
    public void onBackPressed() {

        Log.d(TAG, "-----------------------------------------------------------------------------");

        Fragment active_frag = getVisibleFragment();
        if (!gameRunning && active_frag != null && active_frag.getTag() != null &&
                (active_frag.getTag().equals("mGameScreenFragment"))) {
            onStartMenuRequested();
        }

        else if (active_frag != null && active_frag.getTag() != null &&
                (active_frag.getTag().equals("mGameScreenFragment") ||
                (active_frag.getTag().equals("mMultiPlayerSettingsFragment")))) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(getString(R.string.app_name) + " " + getString(R.string.close) + "?")
                    .setCancelable(true)
                    .setMessage(getString(R.string.are_you_sure_to_close_message, getString(R.string.this_game)))
                    .setPositiveButton(getString(R.string.yes_button), new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            endGame();
                        }
                    })
                    .setNegativeButton(getString(R.string.no_button), null)
                    .show();
        }
        else if (active_frag != null && active_frag.getTag() != null &&
                !active_frag.getTag().equals("mStartScreenFragment")) {
            onStartMenuRequested();
        }
        else {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(getString(R.string.app_name)  + " " + getString(R.string.close) + "?")
                    .setCancelable(true)
                    .setMessage(getString(R.string.are_you_sure_to_close_message, getString(R.string.app_name)))
                    .setPositiveButton(getString(R.string.yes_button), new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton(getString(R.string.no_button), null)
                    .show();
        }

    }

    //----------------------------------------------------------------------------------------------
    // start SinglePlayer Game Mode
    //
    @Override
    public void onSinglePlayerRequested() {
        try {
            requestLoadingScreen();
            Handler mHandler = new Handler();
            playedWithAFriend = false;
            Runnable showLoadingScreenThenContinue = new Runnable() {
                @Override
                public void run() {
                    mSinglePlayerFragment.prepareSinglePlayerRequested();
                    startGamePreparation(false);
                }
            };
            mHandler.postDelayed(showLoadingScreenThenContinue, 100);
        }
        catch (Exception e) {
            onStartMenuRequested();
            Log.e(TAG, "onSinglePlayerRequested: " + e);
        }

    }


    //----------------------------------------------------------------------------------------------
    // start Qick Game Mode
    //
    @Override
    public void onMultiPlayerQuickGameRequested() {
        try {
            // user wants to play against a random opponent right now
            requestLoadingScreen();
            playedWithAFriend = false;
            Handler mHandler = new Handler();
            Runnable showLoadingScreenThenContinue = new Runnable() {
                @Override
                public void run() {
                    startQuickGame();
                }
            };
            mHandler.postDelayed(showLoadingScreenThenContinue, 10);
        }
        catch (Exception e) {
            onStartMenuRequested();
            Log.e(TAG, "onMultiPlayerQuickGameRequested exception: " + e);
        }
    }

    @Override
    public void onMultiPlayerInvitePlayersRequested() {
        try {
            requestLoadingScreen();
            // show list of invitable players
            mRealTimeMultiplayerClient.getSelectOpponentsIntent(1, 3).addOnSuccessListener(
                    new OnSuccessListener<Intent>() {
                        @Override
                        public void onSuccess(Intent intent) {
                            startActivityForResult(intent, RC_SELECT_PLAYERS);
                        }
                    }
            ).addOnFailureListener(createFailureListener("There was a problem selecting opponents."));
        }
        catch (Exception e) {
            onStartMenuRequested();
            Log.e(TAG, "onMultiPlayerInvitePlayerRequested exception: " + e);
        }

    }

    @Override
    public void onMultiPlayerSeeInvitationsRequested() {
        try {
            // show list of pending invitations
            switchToFragment(mLoadingScreenFragment, "mLoadingScreenFragment");
            // show list of pending invitations
            mInvitationsClient.getInvitationInboxIntent().addOnSuccessListener(
                    new OnSuccessListener<Intent>() {
                        @Override
                        public void onSuccess(Intent intent) {
                            startActivityForResult(intent, RC_INVITATION_INBOX);
                        }
                    }
            ).addOnFailureListener(createFailureListener("There was a problem getting the inbox."));
        }
        catch (Exception e) {
            onStartMenuRequested();
            Log.e(TAG, "onMultiPlayerSeeInvitationsRequested exception: " + e);
        }
    }



    /*
     * GAME LOGIC SECTION. Methods that implement the game's rules.
     */

    private boolean gamePreparationRunning = false;
    void startGamePreparation(final boolean multiplayer) {
        try {
            gamePreparationRunning = true;
            requestLoadingScreen();
            gameState = 0;
            waitForOnlineInteraction = 0;
            correctLeftPeers.clear();

            //  create the Game View
            if (waitForNewGame) {
                Handler handler = new Handler();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        startGamePreparation(multiplayer);
                    }
                };
                handler.postDelayed(runnable, 200);
            }
            else {
                Handler myHandler = new Handler();
                Runnable myRunnable = new Runnable() {
                    @Override
                    public void run() {
                        prepareStartedGame(multiplayer);
                    }
                };
                myHandler.postDelayed(myRunnable, 10);
            }
        }
        catch (Exception e) {
            Log.e(TAG, "startGamePreparation exception: " + e);
        }
    }


    private volatile Integer[] missedHeartBeats = new Integer[4];
    private volatile boolean heartBeating = false;
    ScheduledExecutorService executor;

    private void startHeartBeat() {
        try {
            heartBeating = true;
            missedHeartBeats = new Integer[4];
            missedHeartBeats[0] = 0;
            missedHeartBeats[1] = 0;
            missedHeartBeats[2] = 0;
            missedHeartBeats[3] = 0;

            Runnable r = new Runnable() {
                @Override
                public void run() {
                    handleHeartBeats();
                }
            };
            executor = Executors.newScheduledThreadPool(7);
            executor.scheduleAtFixedRate(r, 0, 5, TimeUnit.SECONDS);

        }
        catch (Exception e) {
            Log.e(TAG, "startHeartBeat: " + e);
            leaveRoom();
        }

    }


    private void handleHeartBeats() {
        try {
            if (!heartBeating || !mMultiplayer) {
                stopHeartBeat();
                return;
            }
            missedHeartBeats[0]++;
            missedHeartBeats[1]++;
            missedHeartBeats[2]++;
            missedHeartBeats[3]++;
            sendHeartBeat();
            checkHeartBeat();
        }
        catch (Exception e) {
            Log.e(TAG, "handleHeartBeat exception: " + e);
        }

    }


    private void sendHeartBeat() {
        broadcastMessage(Message.heartBeat, "");
    }

    private void checkHeartBeat() {
        try {
            boolean connectionProblem = false;
            for (int i = 0; i < mParticipants.size(); i++) {
                if (mParticipants.get(i).getParticipantId().equals(myParticipantId) ||
                        correctLeftPeers.contains(mParticipants.get(i).getParticipantId())) {
                    continue;
                }

                if (missedHeartBeats[i] > 3 && mGameView != null && mGameView.getController() != null
                        && mGameView.getController().getNonGamePlayUIContainer() != null &&
                        mGameView.getController().getNonGamePlayUIContainer().getConnectionProblem() != null) {
                    connectionProblem = true;
                }

                //  -> 18*5 = 90 -> 1m30sec
                if (missedHeartBeats[i] > 18) {
                    Log.d(TAG, i + "  -> " + mParticipants.get(i).getDisplayName() + "lost connection");
                    final ArrayList<String> leftPlayers = new ArrayList<>();
                    leftPlayers.add(mParticipants.get(i).getParticipantId());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updatePlayer(leftPlayers);
                        }
                    });
                    return;
                }
            }
            if (connectionProblem) {
                mGameView.getController().getNonGamePlayUIContainer().getConnectionProblem().setVisible(true);
            }
        }
        catch (Exception e) {
            Log.e(TAG, "checkHeartbeat exception: " + e);
        }
    }

    private void receiveHeartBeat(String senderId) {
        for (int i = 0; i < mParticipants.size(); i++) {
            if (mParticipants.get(i).getParticipantId().equals(senderId)) {
                missedHeartBeats[i] = 0;
            }
        }
    }

    private void stopHeartBeat() {
        heartBeating = false;
        try {
            executor.shutdown();
        }
        catch (Exception e) {
            Log.w(TAG, "HeartBeat couldn't be stopped");
        }
    }


    public volatile int gameState = 0;

    //
    //  tries to execute received messaged
    //          -> if the activity is not ready it tries again after a short delay
    //
    private void workOnMessageQueue() {
        try {
            // just in case something went wrong
            if (messageQueue == null || messageQueue.isEmpty()) {
                return;
            }

            // heartBeat will be always received!
            if (messageQueue.get(0).type == Message.heartBeat) {
                String sId = messageQueue.get(0).senderId;
                messageQueue.remove(0);
                receiveHeartBeat(sId);
                return;
            }

            // players that leave are always received too.
            if (messageQueue.get(0).type == Message.leftGameAtEnd) {
                correctLeftPeers.add(messageQueue.get(0).senderId);
                messageQueue.remove(0);
                return;
            }

            //
            // ----- setHost
            //
            // game State 0 -> set Host
            if (messageQueue.get(0).type == Message.setHost) {
                // already in another state?
                if (gameState != Message.gameStateWaitForSetHost) {
                    messageQueue.remove(0);
                    return;
                }
                // ui still working?
                if (waitForOnlineInteraction != Message.setHost) {
                    tryWorkOnMessageAgainLater();
                    return;
                }
                waitForOnlineInteraction = 0;
                String hostId = messageQueue.get(0).data;
                messageQueue.remove(0);
                setHostAndContinue(hostId);
                return;
            }

            // requested repeated messages
            if (messageQueue.get(0).type == Message.requestHost) {
                if (gameState <= Message.gameStateWaitForSetHost) {
                    tryWorkOnMessageAgainLater();
                    return;
                }
                String sendTo = messageQueue.get(0).senderId;
                messageQueue.remove(0);
                sendUnReliable(sendTo, Message.setHost, gameHostId);
                return;
            }

            //--------------------

            //
            // ----- prepare & start Game
            //
            // game State 1.0 -> prepare Game   (can be skipped)
            if (messageQueue.get(0).type == Message.prepareGame) {
                // already in another state?
                if (gameState != Message.gameStateWaitForStartGame) {
                    messageQueue.remove(0);
                    return;
                }
                // ui still working?
                if (waitForOnlineInteraction != Message.prepareGame) {
                    tryWorkOnMessageAgainLater();
                    return;
                }
                int msgCode = Integer.parseInt(messageQueue.get(0).data);
                messageQueue.remove(0);
                mMultiPlayerSettingsFragment.receiveMessage(msgCode);
                return;
            }

            // game State 1.1 -> start Game
            if (messageQueue.get(0).type == Message.startGame) {
                // already in another state?
                if (gameState != Message.gameStateWaitForStartGame) {
                    messageQueue.remove(0);
                    return;
                }
                // ui still working?
                if (waitForOnlineInteraction != Message.prepareGame) {
                    tryWorkOnMessageAgainLater();
                    return;
                }
                waitForOnlineInteraction = 0;
                paras = gson.fromJson(messageQueue.get(0).data, startGameParas.class);
                messageQueue.remove(0);
                mMultiPlayerSettingsFragment.setValues(paras.enemies, paras.difficulty, paras.playerLives);
                startGame(paras, mDisplayName);
                return;
            }

            // requested repeated messages
            if (messageQueue.get(0).type == Message.requestStartGame) {
                if (gameState <= 1) {
                    tryWorkOnMessageAgainLater();
                    return;
                }
                String sendTo = messageQueue.get(0).senderId;
                messageQueue.remove(0);
                sendUnReliable(sendTo, Message.startGame, gson.toJson(paras));
                return;
            }


            //--------------------
            // game State 2 -> the Game
            if (gameRunning) {
                // push forward to the game controller
                if (mGameView != null && mGameView.getController() != null) {
                    Message message = messageQueue.get(0);
                    messageQueue.remove(0);
                    mGameView.getController().handleReceivedMessage(message);
                    return;
                }
                // game controller needs more time to get initialized
                else {
                    tryWorkOnMessageAgainLater();
                    return;
                }
            }

            messageQueue.remove(0);
            Log.w(TAG, "couldn't proceed a message");
        }
        catch (Exception e) {
            Log.e(TAG, "workOnMessageQueue exception: " + e);
        }
    }



    ScheduledExecutorService rMMexecutor = null;
    public void requestMissedMessage(final String requestFromId, final int oldState, final int msgCode, final String data) {

        if (rMMexecutor != null) {
            rMMexecutor.shutdown();
            rMMexecutor = null;
        }

        Runnable r = new Runnable() {
            @Override
            public void run() {
                if (gameState != oldState) {
                    Log.d("rMM", gameState + " --- " + oldState);
                    rMMexecutor.shutdown();
                    return;
                }
                sendUnReliable(requestFromId, msgCode, data);
            }
        };
        rMMexecutor = Executors.newScheduledThreadPool(15);
        rMMexecutor.scheduleAtFixedRate(r, 1000, 1000, TimeUnit.MILLISECONDS);
    }

    ScheduledExecutorService rMMexecutor2 = null;
    public void requestMissedMessageFromAll(final int oldState, final int msgCode, final String data) {

        if (rMMexecutor2 != null) {
            rMMexecutor2.shutdown();
            rMMexecutor2 = null;
        }

        Runnable r = new Runnable() {
            @Override
            public void run() {
                if (gameState != oldState) {
                    Log.d("rMM", gameState + " --- " + oldState);
                    rMMexecutor2.shutdown();
                    return;
                }
                broadcastMessage(msgCode, data);
            }
        };
        rMMexecutor2 = Executors.newScheduledThreadPool(15);
        rMMexecutor2.scheduleAtFixedRate(r, 1000, 1000, TimeUnit.MILLISECONDS);
    }

    // nr 3 is controller

    private String gameHostId;

    void prepareStartedGame(final boolean multiplayer) {

        try {
            mMultiplayer = multiplayer;
            startHeartBeat();

            //---- get/set all game starting parameters
            if (!isSignedIn()) {
                mDisplayName = "Du";
            }

            if (multiplayer) {
                sortParticipants();
                if (mParticipants.get(0).getParticipantId().equals(myParticipantId)) {
                    Log.d(TAG, "I choose the host");
                    int randomNum = ThreadLocalRandom.current().nextInt(0, mParticipants.size());
                    gameHostId = mParticipants.get(randomNum).getParticipantId();
                    broadcastMessage(Message.setHost, gameHostId);
                    setHostAndContinue(gameHostId);
                }
                else {
                    waitForOnlineInteraction = Message.setHost;
                    requestMissedMessageFromAll(gameState, Message.requestHost, "");
                }
            }

            else { // singleplayer
                paras = new startGameParas();
                paras.enemies = mSinglePlayerFragment.getEnemies();
                paras.difficulty = mSinglePlayerFragment.getDifficulty();
                paras.playerLives = mSinglePlayerFragment.getPlayerLives();
                startGame(paras, mDisplayName);
            }
        }
        catch (Exception e) {
            Log.e(TAG, "prepareStartedGame exception: " + e);
        }
    }


    private void setHostAndContinue(String hostParticipantId) {
        try {
            gameHostId = hostParticipantId;
            gameState = Message.gameStateWaitForStartGame;
            Log.d("----------", "host id " + gameHostId);
            mMultiPlayerSettingsFragment.prepareMultiPlayerSettingsRequested(mParticipants.size(),
                    gameHostId.equals(myParticipantId));
            switchToFragment(mMultiPlayerSettingsFragment, "mMultiPlayerSettingsFragment");
            if (!gameHostId.equals(myParticipantId)) {
                waitForOnlineInteraction = Message.prepareGame;
                requestMissedMessage(gameHostId, gameState, Message.requestStartGame, "");
            }
        }
        catch (Exception e) {
            Log.e(TAG, "setHostAndContinue exception: " + e);
        }

    }

    private startGameParas paras;

    //----------------------------------------------------------------------------------------------
    // start SinglePlayer Game Mode
    //
    @Override
    public void onMultiPlayerSettingsStartGameRequested() {
        try {
            mMultiPlayerSettingsFragment.setMultiPlayerSettingsRequested();
            int player_lives = mMultiPlayerSettingsFragment.getPlayerLives();
            int difficulty = mMultiPlayerSettingsFragment.getDifficulty();
            int enemies = mMultiPlayerSettingsFragment.getEnemies();
            paras = new startGameParas();
            paras.enemies = enemies;
            paras.difficulty = difficulty;
            paras.playerLives = player_lives;
            if (myParticipantId.equals(gameHostId)) {
                paras.playerPositions = new ArrayList<>();
                for (Participant p : mParticipants) {
                    paras.playerPositions.add(p.getParticipantId());
                }
                for (int i = 0; i < enemies; i++) {
                    paras.playerPositions.add("");
                }
                Collections.shuffle(paras.playerPositions);
                broadcastMessage(Message.startGame, gson.toJson(paras));
            }
            else {        // else can't be called without a message
                return;
            }
            startGame(paras, mDisplayName);
        }
        catch (Exception e) {
            Log.e(TAG, "onMultiPlayerSettingsStartGameRequested exception: " + e);
        }
    }

    @Override
    public void onMultiPlayerSettingsChanged(int code) {
        if (myParticipantId.equals(gameHostId)) {
            broadcastMessage(Message.prepareGame, "" + code);
        }
    }

    class startGameParas
    {
        int playerLives;
        int enemies;
        int difficulty;
        ArrayList<String> playerPositions;
    }

    private void startGame(final startGameParas paras, final String my_name_final) {
        // start the game via the game controller of our game view

        gameState = Message.gameStateWaitForGameReady;
        mGameView = new GameView(this);
        mGameView.setKeepScreenOn(true);

        Handler mHandler = new Handler();
        Runnable showGameScreenThenContinue = new Runnable() {
            @Override
            public void run() {
                gameRunning = true;
                keepGameScreen();
                try {
                    switchToFragment(mGameScreenFragment, "mGameScreenFragment");
                    mGameView.getController().init(
                            paras.playerLives,
                            paras.enemies,
                            paras.difficulty,
                            mMultiplayer,
                            my_name_final,
                            myParticipantId,
                            mParticipants,
                            gameHostId,
                            paras.playerPositions);
                }
                catch (Exception e) {
                    Log.e(TAG, "game error: " + e);
                    endGame();
                }
            }
        };
        addContentView(mGameView,  new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT));
        mHandler.postDelayed(showGameScreenThenContinue, 10);
    }

    private void keepGameScreen() {
        Handler mHandler = new Handler();
        Runnable kr = new Runnable() {
            @Override
            public void run() {
                try {
                    if (getVisibleFragment() == null ||
                            !getVisibleFragment().getTag().equals("mGameScreenFragment")) {
                        switchToFragment(mGameScreenFragment, "mGameScreenFragment");
                    }

                } catch (Exception e) {
                    Log.e(TAG, "keep game screen error " + e);
                    endGame();
                }
            }
        };
        mHandler.postDelayed(kr, 2500);
    }


    public void submitEvent(String eventId) {
        Games.getEventsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                .increment(eventId, 1);
    }

    boolean playedWithAFriend = false;
    boolean gameWon = false;
    long gamesPlayed = 0;
    long gameWins = 0;

    public void loadEvents() {
        Games.getEventsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                .load(true)
                .addOnCompleteListener(new OnCompleteListener<AnnotatedData<EventBuffer>>() {
                    @Override
                    public void onComplete(@NonNull Task<AnnotatedData<EventBuffer>> task) {
                        if (task.isSuccessful()) {
                            // Process all the events.
                            for (Event event : task.getResult().get()) {
                                Log.d(TAG, "loaded event " + event.getName());
                                if (event.getEventId().equals(getString(R.string.event_games_played))) {
                                    gamesPlayed = event.getValue();
                                }
                                if (event.getEventId().equals(getString(R.string.event_wins))) {
                                    gameWins = event.getValue();
                                }
                            }

                            setLeaderBoardsAndAchievements();
                        } else {
                            // Handle Error
                            Exception exception = task.getException();
                            int statusCode = CommonStatusCodes.DEVELOPER_ERROR;
                            if (exception instanceof ApiException) {
                                ApiException apiException = (ApiException) exception;
                                statusCode = apiException.getStatusCode();
                            }
                            Log.e(TAG, "receive events exception");
                            // showError(statusCode);
                        }
                    }
                });
    }

    public void endGame(boolean game_won) {

        try {
            submitEvent(getResources().getString(R.string.event_games_played));
            if (game_won) {
                gameWon = true;
                submitEvent(getResources().getString(R.string.event_wins));
            }
            else {
                gameWon = false;
            }
            loadEvents();
            // gets continued with setting leaderboards
        }
        catch (Exception e) {
            Log.e(TAG,"leaderboarding failed");
        }

        endGame();
    }

    private void setLeaderBoardsAndAchievements() {

        if (GoogleSignIn.getLastSignedInAccount(this) == null) {
            return;
        }

        // LeaderBoards
        Games.getLeaderboardsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                .submitScore(getString(R.string.leaderboard_games_played), gamesPlayed);

        Games.getLeaderboardsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                    .submitScore(getString(R.string.leaderboard_wins), gameWins);

        if (gamesPlayed > 0) {
            double winRate = (gameWins * 100.0) / gamesPlayed;
            Games.getLeaderboardsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                    .submitScore(getString(R.string.leaderboard_win_rate), (long) winRate * 100);
        }

        // Achievements
        if (gamesPlayed == 1) {
            Games.getAchievementsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                    .unlock(getString(R.string.achievement_first_game));
        }

        Games.getAchievementsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                .increment(getString(R.string.achievement_play_all_time), 1);

        if (gameWon) {
            if (gameWins == 1) {
                Games.getAchievementsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                        .unlock(getString(R.string.achievement_first_win));
            }

            Games.getAchievementsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                    .increment(getString(R.string.achievement_winning_is_everything), 1);

            Games.getAchievementsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                    .increment(getString(R.string.achievement_pro_gamer), 1);
        }

        if (!gameWon) {
            Games.getAchievementsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                    .increment(getString(R.string.achievement_never_give_up), 1);
        }

        if (playedWithAFriend) {
            Games.getAchievementsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                    .unlock(getString(R.string.achievement_friendship));
        }

        // Muli achievements can be found in all cards played // succeded or failed muli

    }

    public void unlockAchievement(int id) {

        if (GoogleSignIn.getLastSignedInAccount(this) == null) {
            return;
        }

        Games.getAchievementsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                .unlock(getString(id));
    }





    public void endGame() {

        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    requestLoadingScreen();
                    Handler h = new Handler();
                    Runnable r = new Runnable() {
                        @Override
                        public void run() {
                            if (mMultiplayer) {
                                leaveRoom();
                            }

                            stopKeepingScreenOn();
                            if (gamePreparationRunning) {
                                onStartMenuRequested();
                            }

                            if (gameRunning) {
                                onStartMenuRequested();
                                ((ViewGroup) mGameView.getParent()).removeView(mGameView);
                                mGameView.stopAll = true;
                                gameRunning = false;
                                waitForNewGame = true;
                                Handler handler = new Handler();
                                Runnable runnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        if (mGameView != null) {
                                            mGameView.clear();
                                            mGameView = null;
                                        }
                                        waitForNewGame = false;
                                        System.gc();
                                    }
                                };
                                handler.postDelayed(runnable, 2000);
                            }
                        }
                    };
                    h.postDelayed(r, 20);
                }
            });
        }
        catch (Exception e) {
            Log.e(TAG, "endGame: " + e);
        }
    }


    /*
 * MISC SECTION. Miscellaneous methods.
 */
    // Sets the flag to keep this screen on. It's recommended to do that during
    // the
    // handshake when setting up a game, because if the screen turns off, the
    // game will be
    // cancelled.
    void keepScreenOn() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    // Clears the flag that keeps the screen on.
    void stopKeepingScreenOn() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void sortParticipants() {
        Collections.sort(mParticipants, new Comparator<Participant>() {
            @Override
            public int compare(Participant p1, Participant p2) {
                String s1 = p1.getParticipantId();
                String s2 = p2.getParticipantId();
                return s1.compareToIgnoreCase(s2);
            }
        });
    }
}





