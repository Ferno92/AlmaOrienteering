package com.almaorient.ferno92.almaorienteering.homepage;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.almaorient.ferno92.almaorienteering.CalendarActivity;
import com.almaorient.ferno92.almaorienteering.FilterCorsoActivity;
import com.almaorient.ferno92.almaorienteering.InfoAppActivity;
import com.almaorient.ferno92.almaorienteering.InfoGeneraliActivity;
import com.almaorient.ferno92.almaorienteering.MapsActivity;
import com.almaorient.ferno92.almaorienteering.ModusActivity;
import com.almaorient.ferno92.almaorienteering.NewMainActivity;
import com.almaorient.ferno92.almaorienteering.R;
import com.almaorient.ferno92.almaorienteering.recensioni.RecensioniActivity;
import com.almaorient.ferno92.almaorienteering.versus.VersusSelectorActivity;

/**
 * Created by lucas on 28/03/2017.
 */

public class HomeElementFragment extends Fragment {
    private View mRootView;
    private int mPosition;
    private HomeElementModel[] mElementList = new HomeElementModel[]{
            new HomeElementModel(0, "L'unibo si presenta", "Breve descrizione", "ic_unibo"),
            new HomeElementModel(1, "Offerta formativa", "Breve descrizione", "ic_elenco_scuole"),
            new HomeElementModel(2, "Mappa", "Breve descrizione", "ic_mappa"),
            new HomeElementModel(3, "Modus", "Breve descrizione", "ic_icona_chat"),
            new HomeElementModel(4, "Statistiche", "Breve descrizione", "ic_statistica"),
            new HomeElementModel(5, "Calendario", "Breve descrizione", "ic_calendario_icona"),
            new HomeElementModel(6, "Guida applicazione", "Breve descrizione", "ic_guida"),
            new HomeElementModel(7, "Recensioni", "Breve descrizione", "ic_recensioni")
    };


    public HomeElementFragment(){

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mRootView = inflater.inflate(R.layout.homepage_element, container, false);
        this.mPosition = getArguments().getInt("pos");
        TextView titoloView = (TextView)mRootView.findViewById(R.id.titolo_elemento);
        titoloView.setText(mElementList[mPosition].getTitle());
        TextView descrizioneView = (TextView)mRootView.findViewById(R.id.descrizione);
        descrizioneView.setText(mElementList[mPosition].getDescription());
        ImageView imageView = (ImageView)mRootView.findViewById(R.id.image_elemento);
        int resourceId = getResources().getIdentifier(mElementList[mPosition].getImgSource() , "drawable", getContext().getPackageName());
        imageView.setImageResource(resourceId);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i;
                switch(mPosition){
                    case 0:
                        i = new Intent(getContext(), InfoGeneraliActivity.class);
                        startActivity(i);
                        break;
                    case 1:
                        i = new Intent(getContext(), FilterCorsoActivity.class);
                        startActivity(i);
                        break;
                    case 2:
                        i = new Intent(getContext(), MapsActivity.class);
                        i.putExtra("CallingActivity","main");
                        startActivity(i);
                        break;
                    case 3:
                        i = new Intent(getContext(), ModusActivity.class);
                        startActivity(i);
                        break;
                    case 4:
                        i = new Intent(getContext(), VersusSelectorActivity.class);
                        startActivity(i);
                        break;
                    case 5:
                        i = new Intent(getContext(), CalendarActivity.class);
                        startActivity(i);
                        break;
                    case 6:
                        i = new Intent(getContext(), InfoAppActivity.class);
                        startActivity(i);
                        break;
                    case 7:
                        i = new Intent(getContext(), RecensioniActivity.class);
                        startActivity(i);
                        break;
                    default:
                        break;
                }
            }
        });

        return this.mRootView;

    }

    public  static HomeElementFragment newInstance(int position){
        HomeElementFragment homeElementFragment = new HomeElementFragment();
        Bundle args = new Bundle();
        args.putInt("pos", position);
        homeElementFragment.setArguments(args);

        return homeElementFragment;
    }
}
