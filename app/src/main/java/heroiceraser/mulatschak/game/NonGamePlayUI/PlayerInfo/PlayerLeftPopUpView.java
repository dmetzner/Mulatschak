package heroiceraser.mulatschak.game.NonGamePlayUI.PlayerInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import at.heroiceraser.mulatschak.R;


//--------------------------------------------------------------------------------------------------
//  PlayerInfo Pop up view class
//
public class PlayerLeftPopUpView extends RelativeLayout {


    //----------------------------------------------------------------------------------------------
    //  back button listener
    //
    public interface Listener {
        void onOkButtonRequested();
    }

    PlayerLeftPopUpView.Listener mListener = null;

    public void setListener(PlayerLeftPopUpView.Listener l) {
        mListener = l;
    }


    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public PlayerLeftPopUpView(Context context) {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.player_left_popup, null, false);
        addView(v);
    }


    //----------------------------------------------------------------------------------------------
    //  init
    //
    public void init(String displayName) {
        findViewById(R.id.player_left_pop_up_ok)
                .setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                mListener.onOkButtonRequested();
            }
        });


        ((TextView) (findViewById(R.id.player_left_pop_up_info))).setText(getResources()
                .getString(R.string.last_player_left_info, displayName));
    }
}

