package heroiceraser.mulatschak.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import at.heroiceraser.mulatschak.R;


//--------------------------------------------------------------------------------------------------
//  GameScreen Fragment
//
public class GameScreenFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.game_screen, container, false);
    }
}
