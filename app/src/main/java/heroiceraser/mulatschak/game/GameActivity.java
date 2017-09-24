package heroiceraser.mulatschak.game;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.google.android.gms.games.*;
import com.google.android.gms.games.multiplayer.Participant;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean multiplayer = getIntent().getBooleanExtra("multiplayer", false);

        // SinglePlayer Settings
        int enemies = getIntent().getIntExtra("enemies", 3);
        int difficulty = getIntent().getIntExtra("difficulty", 2);
        int player_lives = getIntent().getIntExtra("player_lives", 21);

        // + signed in
        String myName = getIntent().getStringExtra("myName");

        // multiplayer Extras
        String my_id = getIntent().getStringExtra("myId");

        GameView gameView = new GameView(this);
        gameView.setKeepScreenOn(true);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(gameView);
        gameView.getController().start(player_lives, enemies, multiplayer,
                myName, my_id);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Beende das Spiel?")
                .setCancelable(true)
                .setMessage("Willst du wirklich das laufende Spiel beenden und ins Hauptmenu zurückkehren?")
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
