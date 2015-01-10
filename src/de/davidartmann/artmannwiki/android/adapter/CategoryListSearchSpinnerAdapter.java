package de.davidartmann.artmannwiki.android.adapter;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import de.artmann.artmannwiki.R;

public class CategoryListSearchSpinnerAdapter extends ArrayAdapter<String> {

	private final Activity context;
	private final List<String> names;
	
	static class ViewHolder {
	    TextView textView;
	    ImageView imageView;
    }
	
	public CategoryListSearchSpinnerAdapter(Activity context, List<String> names) {
		super(context, R.layout.custom_spinner, names);
		this.context = context;
		this.names = names;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return getCustomView(position, convertView, parent);
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return getCustomView(position, convertView, parent);
	}
	
	private View getCustomView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
        View row = inflater.inflate(R.layout.custom_spinner, parent, false);
        TextView label = (TextView) row.findViewById(R.id.customspinner_text);

        ImageView icon = (ImageView) row.findViewById(R.id.customspinner_image);
        // String based on the position
     	String s = names.get(position);
 		if (s != null) {
 			// Text for each row
 			label.setText(names.get(position));
 		    if (s.equals("Bankkonto")) {
 		      icon.setImageResource(R.drawable.account);
 		    } else if(s.equals("Gerät")) {
 		    	icon.setImageResource(R.drawable.device);
 		    } else if(s.equals("E-Mail")) {
 		    	icon.setImageResource(R.drawable.email);
 		    } else if(s.equals("Versicherung")) {
 		    	icon.setImageResource(R.drawable.insurance);
 		    } else if(s.equals("Login")) {
 		    	icon.setImageResource(R.drawable.login);
 		    } else if(s.equals("Diverse/Notizen")) {
 		    	icon.setImageResource(R.drawable.miscellaneous);
 		    } else {
 		    	icon.setImageResource(R.drawable.question);
 		    }
 		}
 	    return row;
	}
}
