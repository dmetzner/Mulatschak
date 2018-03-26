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
public class RulesFragment extends Fragment implements View.OnClickListener {

    public interface Listener {
        void onStartMenuRequested();
    }

    SinglePlayerFragment.Listener mListener = null;

    private TextView textView;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.rules, container, false);
        final int[] CLICKABLES = new int[]{
                R.id.rules_back_button,
        };
        for (int i : CLICKABLES) {
            v.findViewById(i).setOnClickListener(this);
        }

        TextView tv = v.findViewById(R.id.rules_text);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tv.setText(Html.fromHtml(getString(R.string.rules_text), Html.FROM_HTML_MODE_COMPACT));
        }
        else {
            tv.setText(Html.fromHtml(getString(R.string.rules_text)));
        }

        return v;
    }

    public void setListener(SinglePlayerFragment.Listener l) {
        mListener = l;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rules_back_button:
                mListener.onStartMenuRequested();
                break;
        }
    }
}

