package heroiceraser.mulatschak.multi;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.google.android.gms.games.multiplayer.Participant;

import java.util.ArrayList;

import heroiceraser.mulatschak.game.GameView;

public class GameActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // boolean multiplayer = getIntent().getBooleanExtra("multiplayer", false);
        // int enemies = getIntent().getIntExtra("enemies", 3);
        // int difficulty = getIntent().getIntExtra("difficulty", 2);
        // int player_lives = getIntent().getIntExtra("player_lives", 21);

        String my_id = getIntent().getStringExtra("myId");
        ArrayList<Participant> participants =
                (ArrayList) getIntent().getSerializableExtra("participants");
        GameView2 gameView = new GameView2(this);
        gameView.setKeepScreenOn(true);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(gameView);
        gameView.getController().start(participants, my_id, 21);
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
