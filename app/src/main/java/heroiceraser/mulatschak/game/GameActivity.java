package heroiceraser.mulatschak.game;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int enemies = getIntent().getIntExtra("enemies", 4);
        int difficulty = getIntent().getIntExtra("difficulty", 2);
        int player_lives = getIntent().getIntExtra("player_lives", 21);
        GameView gameView = new GameView(this, enemies, difficulty, player_lives);
        gameView.setKeepScreenOn(true);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(gameView);
        gameView.getController().start();
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
