package heroiceraser.mulatschak.Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;


import at.heroiceraser.mulatschak.R;
import heroiceraser.mulatschak.MainActivity;
import heroiceraser.mulatschak.game.GameController;


//--------------------------------------------------------------------------------------------------
//  Singe Player Screen Fragment
//
public class SettingsFragment extends Fragment implements View.OnClickListener {

    public interface Listener {
        void onStartMenuRequested();
    }

    SettingsFragment.Listener mListener = null;

    private final int NOT_SET = -1;
    private int card_design = NOT_SET;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.settings, container, false);
        final int[] CLICKABLES = new int[] {
                R.id.settings_back_button,
        };
        for (int i : CLICKABLES) {
            v.findViewById(i).setOnClickListener(this);
        }

        RadioButton card_design_1_radioButton =
                v.findViewById(R.id.settings_card_design_1_radioButton);
        RadioButton card_design_2_radioButton =
                v.findViewById(R.id.settings_card_design_2_radioButton);

        final MainActivity ma = (MainActivity) getActivity();
        if (ma == null) {
            return v;
        }
        card_design = ma.mySharedPreference.getInt(ma.cardDesignKey, 1);

        switch (card_design) {
            case 1:
                card_design_1_radioButton.setChecked(true);
                card_design_2_radioButton.setChecked(false);
                break;
            case 2:
                card_design_2_radioButton.setChecked(true);
                card_design_1_radioButton.setChecked(false);
                break;
        }

        RadioGroup radioGroup = v.findViewById(R.id.settings_card_design_radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.settings_card_design_1_radioButton:
                        card_design = 1;
                        ma.mySharedPreference.edit().putInt(ma.cardDesignKey, 1).apply();
                        break;
                    case R.id.settings_card_design_2_radioButton:
                        card_design = 2;
                        ma.mySharedPreference.edit().putInt(ma.cardDesignKey, 2).apply();
                        break;
                }
            }
        });

        return v;
    }

    public void setListener(SettingsFragment.Listener l) {
        mListener = l;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.settings_back_button:
                mListener.onStartMenuRequested();
                break;
        }
    }

    public int getCardDesign(MainActivity ma) {
        try {
            if (ma == null) {
                return 1;
            }
            card_design = ma.mySharedPreference.getInt(ma.cardDesignKey, 1);
        }
        catch (Exception e) {
            return 1;
        }
        return card_design;
    }

}

