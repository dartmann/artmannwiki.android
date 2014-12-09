package de.davidartmann.artmannwiki.android.Drawer;

import android.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class DrawerItemClickListener implements OnItemClickListener {

	@Override
	public void onItemClick(AdapterView parent, View view, int position, long id) {
		selectItem(position);

	}

	private void selectItem(int position) {
		//Fragment fragment = new PlanetFragment();
		Bundle args = new Bundle();
		//args.putInt(PlanetFragment.ARG_PLANET_NUMBER, value);
	}

}
