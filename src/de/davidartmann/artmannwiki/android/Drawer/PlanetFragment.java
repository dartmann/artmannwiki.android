package de.davidartmann.artmannwiki.android.Drawer;

import java.util.Locale;

import android.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class PlanetFragment extends Fragment {

	public static final String ARG_PLANET_NUMBER = "planet_number";
	
	public PlanetFragment() {
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.test_list_item, container, false);
		int i = getArguments().getInt(ARG_PLANET_NUMBER);
		String planet = getResources().getStringArray(R.array.emailAddressTypes)[i];
		
		int imageId = getResources().getIdentifier(planet.toLowerCase(Locale.getDefault()),
				"drawable", getActivity().getPackageName());
		((ImageView) rootView.findViewById(R.id.icon)).setImageResource(imageId);
		getActivity().setTitle(planet);
		
		return rootView;
	}
	
	
}
