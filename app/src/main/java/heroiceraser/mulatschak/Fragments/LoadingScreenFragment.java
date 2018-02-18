package heroiceraser.mulatschak.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import at.heroiceraser.mulatschak.R;

/**
 * Created by Daniel Metzner on 17.09.2017.
 */

public class LoadingScreenFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.loading, container, false);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
