package heroiceraser.mulatschak.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import at.heroiceraser.mulatschak.R;


//--------------------------------------------------------------------------------------------------
//  Loading Screen Fragment
//
public class LoadingScreenFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.loading, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
