package heroiceraser.mulatschak.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import at.heroiceraser.mulatschak.R;


//--------------------------------------------------------------------------------------------------
//  Singe Player Screen Fragment
//
public class RulesFragment extends Fragment implements View.OnClickListener {

    public interface Listener {
        void onStartMenuRequested();
    }

    SinglePlayerFragment.Listener mListener = null;

    private TextView textView;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.singleplayer_settings, container, false);
        final int[] CLICKABLES = new int[]{
                R.id.single_player_settings_back_button,
        };
        for (int i : CLICKABLES) {
            v.findViewById(i).setOnClickListener(this);
        }

        return v;
    }

    public void setListener(SinglePlayerFragment.Listener l) {
        mListener = l;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.single_player_settings_back_button:
                mListener.onStartMenuRequested();
                break;
        }
    }
}

