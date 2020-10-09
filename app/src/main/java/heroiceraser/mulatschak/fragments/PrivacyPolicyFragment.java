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


public class PrivacyPolicyFragment extends Fragment {

    public interface Listener {
        void switchToStartScreen();
    }

    PrivacyPolicyFragment.Listener mListener = null;

    public void setListener(PrivacyPolicyFragment.Listener l) {
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.privacy_policy, container, false);

        MyViewUtils.addOnClickListenerToElements(view, onClickListener, new int[]{
                R.id.privacy_policy_back_button,
        });

        TextView policyTextView = view.findViewById(R.id.privacy_policy_text);
        MyViewUtils.addHTML(policyTextView, getString(R.string.policy_text));

        return view;
    }
}

