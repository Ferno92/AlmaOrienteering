package com.almaorient.ferno92.almaorienteering.firebaseDB;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by ale96 on 25/03/2017.
 */

public class Mappe implements ClusterItem {
        private LatLng mPosition;
        private String mTitle;
        private String mSnippet;

        public Mappe(double lat, double lng) {
            mPosition = new LatLng(lat, lng);
        }

        public Mappe(double lat, double lng, String title, String snippet) {
            this.mPosition = new LatLng(lat, lng);
            this.mTitle = title;
            this.mSnippet = snippet;
        }
        @Override
        public LatLng getPosition() {
            return this.mPosition;
        }

        @Override
        public String getTitle() {
            return this.mTitle;
        }

        @Override
        public String getSnippet() {
            return this.mSnippet;
        }
}

