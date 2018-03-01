package heroiceraser.mulatschak.game.NonGamePlayUI.PlayerInfo;

import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import at.heroiceraser.mulatschak.R;


//--------------------------------------------------------------------------------------------------
//  PlayerInfo Pop up view class
//
public class LastPlayerLeftPopUpView extends RelativeLayout {


    //----------------------------------------------------------------------------------------------
    //  back button listener
    //
    public interface Listener {
        void onYesButtonRequested();
        void onNoButtonRequested();
    }

    LastPlayerLeftPopUpView.Listener mListener = null;

    public void setListener(LastPlayerLeftPopUpView.Listener l) {
        mListener = l;
    }


    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public LastPlayerLeftPopUpView(Context context) {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.last_player_left_popup, null, false);
        addView(v);
    }


    //----------------------------------------------------------------------------------------------
    //  init
    //
    public void init(String displayName) {
        findViewById(R.id.last_player_left_pop_up_yes)
                .setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                mListener.onYesButtonRequested();
            }
        });

        findViewById(R.id.last_player_left_pop_up_no)
                 .setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                    mListener.onNoButtonRequested();
            }
        });

        ((TextView) (findViewById(R.id.last_player_left_pop_up_info))).setText(getResources()
                .getString(R.string.last_player_left_info, displayName));
    }
}

