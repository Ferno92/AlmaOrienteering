package com.almaorient.ferno92.almaorienteering.recensioni;

import android.view.View;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.almaorient.ferno92.almaorienteering.R;

/**
 * Created by lucas on 26/03/2017.
 */

class RecensioniBinder implements SimpleAdapter.ViewBinder {
    @Override
    public boolean setViewValue(View view, Object data, String textRepresentation) {
        if(view.getId() == R.id.rating_recensione){
            String stringval = (String) data;
            float ratingValue = Float.parseFloat(stringval);
            RatingBar ratingBar = (RatingBar) view;
            ratingBar.setRating(ratingValue);
            return true;
        }else if(view.getId() == R.id.testo_recensione){
            String stringval = (String) data;
            TextView recensioneText = (TextView) view;
            recensioneText.setText(stringval);
            return true;
        }
        return false;
    }
}
