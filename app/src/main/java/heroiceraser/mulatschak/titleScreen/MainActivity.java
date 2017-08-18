package heroiceraser.mulatschak.titleScreen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TitleView titleView = new TitleView(this);
        titleView.setKeepScreenOn(true);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(titleView);
    }
}
