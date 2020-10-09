package heroiceraser.mulatschak.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import at.heroiceraser.mulatschak.R;
import heroiceraser.mulatschak.MainActivity;
import heroiceraser.mulatschak.utils.MyViewUtils;


public class GlobalSettingsFragment extends Fragment {

    public interface Listener {
        void switchToStartScreen();
    }

    GlobalSettingsFragment.Listener mListener = null;

    public void setListener(GlobalSettingsFragment.Listener l) {
        mListener = l;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.settings_back_button:
                    mListener.switchToStartScreen();
                    break;
            }
        }
    };


    /**
     * Different design sets of cards
     */
    private final String CARD_DESIGN_KEY = "CARD_DESIGN_KEY";
    private final String CARD_DESIGN_DEFAULT = "CARD_DESIGN_DEFAULT";
    private final String CARD_DESIGN_2 = "CARD_DESIGN_2";
    private String card_design_value = CARD_DESIGN_DEFAULT;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.settings, container, false);

        MyViewUtils.addOnClickListenerToElements(view, onClickListener, new int[]{
                R.id.settings_back_button
        });

        final MainActivity mainActivity = (MainActivity) getActivity();
        assert null != mainActivity;


        card_design_value = mainActivity.mySharedPreference.getString(CARD_DESIGN_KEY, CARD_DESIGN_DEFAULT);
        initCardDesignButtons(view);
        addCardDesignOnChangeListener(view, mainActivity.mySharedPreference);

        return view;
    }

    private void initCardDesignButtons(@NonNull View view) {
        RadioButton card_design_1_radioButton = view.findViewById(R.id.settings_card_design_1_radioButton);
        RadioButton card_design_2_radioButton = view.findViewById(R.id.settings_card_design_2_radioButton);

        switch (card_design_value) {
            case CARD_DESIGN_DEFAULT:
                card_design_1_radioButton.setChecked(true);
                card_design_2_radioButton.setChecked(false);
                break;
            case CARD_DESIGN_2:
                card_design_2_radioButton.setChecked(true);
                card_design_1_radioButton.setChecked(false);
                break;
        }
    }

    private void addCardDesignOnChangeListener(@NonNull View view, @NonNull final SharedPreferences sharedPreferences) {
        RadioGroup radioGroup = view.findViewById(R.id.settings_card_design_radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.settings_card_design_1_radioButton:
                        sharedPreferences.edit().putString(CARD_DESIGN_KEY, CARD_DESIGN_DEFAULT).apply();
                        break;
                    case R.id.settings_card_design_2_radioButton:
                        sharedPreferences.edit().putString(CARD_DESIGN_KEY, CARD_DESIGN_2).apply();
                        break;
                }
            }
        });
    }


    public String getCardDesign() {
        return card_design_value;
    }
}