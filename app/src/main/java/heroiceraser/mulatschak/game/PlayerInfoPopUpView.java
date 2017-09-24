package heroiceraser.mulatschak.game;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import heroiceraser.mulatschak.R;
import heroiceraser.mulatschak.game.DrawableObjects.PlayerInfo;

/**
 * Created by Daniel Metzner on 22.09.2017.
 */

public class PlayerInfoPopUpView extends LinearLayout {

    public interface Listener {
        void onBackButtonRequested();
    }

    heroiceraser.mulatschak.game.PlayerInfoPopUpView.Listener mListener = null;

    private TextView player_name_;

    public PlayerInfoPopUpView(Context context) {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.player_info_popup, null, false);
        addView(v);
    }

    public void init(String display_name) {
        ((Button) findViewById(R.id.player_info_pop_up_back_button))
                .setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mListener.onBackButtonRequested();
            }
        });

        ((TextView) findViewById(R.id.player_info_pop_up_display_name)).setText(display_name);

    }



    public void setListener(heroiceraser.mulatschak.game.PlayerInfoPopUpView.Listener l) {
        mListener = l;
    }

}
