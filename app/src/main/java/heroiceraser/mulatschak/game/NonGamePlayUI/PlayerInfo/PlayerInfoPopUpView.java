package heroiceraser.mulatschak.game.NonGamePlayUI.PlayerInfo;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import at.heroiceraser.mulatschak.R;


//--------------------------------------------------------------------------------------------------
//  PlayerInfo Pop up view class
//
public class PlayerInfoPopUpView extends LinearLayout {

    //----------------------------------------------------------------------------------------------
    //  back button listener
    //
    public interface Listener {
        void onBackButtonRequested();
    }

    PlayerInfoPopUpView.Listener mListener = null;

    public void setListener(PlayerInfoPopUpView.Listener l) {
        mListener = l;
    }


    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public PlayerInfoPopUpView(Context context) {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.player_info_popup, null, false);
        addView(v);
    }


    //----------------------------------------------------------------------------------------------
    //  init
    //
    public void init(String display_name, String text, Bitmap bitmap) {
        findViewById(R.id.player_info_pop_up_back_button)
                .setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mListener.onBackButtonRequested();
            }
        });

        ((ImageView) findViewById(R.id.player_info_pop_up_image)).setImageBitmap(bitmap);

        ((TextView) findViewById(R.id.player_info_pop_up_display_name)).setText(display_name);

        ((TextView) findViewById(R.id.player_info_pop_up_text)).setText(text);
    }
}
