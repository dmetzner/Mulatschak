package heroiceraser.mulatschak.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import at.heroiceraser.mulatschak.R;
import heroiceraser.mulatschak.utils.MyViewUtils;


public class RulesFragment extends Fragment {

    public interface Listener {
        void switchToStartScreen();
    }

    RulesFragment.Listener mListener = null;

    public void setListener(RulesFragment.Listener l) {
        mListener = l;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.rules_back_button:
                    mListener.switchToStartScreen();
                    break;
            }
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.rules, container, false);

        MyViewUtils.addOnClickListenerToElements(view, onClickListener, new int[]{
                R.id.rules_back_button,
        });

        TextView rulesTextView = view.findViewById(R.id.rules_text);
        MyViewUtils.addHTML(rulesTextView, getString(R.string.rules_text));

        return view;
    }

}

