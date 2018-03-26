package heroiceraser.mulatschak.Fragments;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import at.heroiceraser.mulatschak.R;


//--------------------------------------------------------------------------------------------------
//  Singe Player Screen Fragment
//
public class PrivacyPolicyFragment extends Fragment implements View.OnClickListener {

    public interface Listener {
        void onStartMenuRequested();
    }

    heroiceraser.mulatschak.Fragments.SinglePlayerFragment.Listener mListener = null;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.privacy_policy, container, false);
       final int[] CLICKABLES = new int[]{
                R.id.privacy_policy_back_button,
        };
        for (int i : CLICKABLES) {
            v.findViewById(i).setOnClickListener(this);
        }

        TextView tv = v.findViewById(R.id.privacy_policy_text);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tv.setText(Html.fromHtml(getString(R.string.policy_text), Html.FROM_HTML_MODE_COMPACT));
        }
        else {
            tv.setText(Html.fromHtml(getString(R.string.policy_text)));
        }

        return v;
    }

    public void setListener(heroiceraser.mulatschak.Fragments.SinglePlayerFragment.Listener l) {
        mListener = l;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.privacy_policy_back_button:
                mListener.onStartMenuRequested();
                break;
        }
    }
}

