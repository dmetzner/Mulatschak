package heroiceraser.mulatschak.game;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GameView gameView = new GameView(this);
        gameView.setKeepScreenOn(true);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(gameView);
        gameView.getController().start();
    }

}
