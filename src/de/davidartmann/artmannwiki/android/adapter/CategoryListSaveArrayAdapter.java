package de.davidartmann.artmannwiki.android.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import de.artmann.artmannwiki.R;

public class CategoryListSaveArrayAdapter extends ArrayAdapter<String> {
	
	private final Activity context;
	private final List<String> names;
	private LayoutInflater inflater;
	
	static class ViewHolder {
	    TextView textView;
	    ImageView imageView;
    }
	
	public CategoryListSaveArrayAdapter(Activity context, List<String> names) {
		super(context, R.layout.activity_categorylist_save, names);
		this.context = context;
		this.names = names;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		/*
		 * The convertView argument is essentially a "ScrapView".
		 * It will have a non-null value when ListView is asking you to recycle the row layout.
		 * So, when convertView is not null, you should simply update its contents instead of inflating a new row layout.
		 */
		if (convertView == null) {
			//Inflate the layout
			inflater = context.getLayoutInflater();
			convertView = inflater.inflate(R.layout.activity_categorylist_save, null);
			viewHolder = new ViewHolder();
			viewHolder.textView = (TextView) convertView.findViewById(R.id.label);
			viewHolder.imageView = (ImageView) convertView.findViewById(R.id.icon);
			// store the holder with the view
			convertView.setTag(viewHolder);
		} else {
			// we've just avoided calling findViewById() on resource
			// just use the viewHolder
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		
		// String based on the position
		String s = names.get(position);
		if (s != null) {
			// Text for each row
			viewHolder.textView.setText(names.get(position));
		    if (s.equals("Bankkonto")) {
		      viewHolder.imageView.setImageResource(R.drawable.account);
		    } else if(s.equals("Gerät")) {
		    	viewHolder.imageView.setImageResource(R.drawable.device);
		    } else if(s.equals("E-Mail")) {
		    	viewHolder.imageView.setImageResource(R.drawable.email);
		    } else if(s.equals("Versicherung")) {
		    	viewHolder.imageView.setImageResource(R.drawable.insurance);
		    } else if(s.equals("Login")) {
		    	viewHolder.imageView.setImageResource(R.drawable.login);
		    } else if(s.equals("Diverse/Notizen")) {
		    	viewHolder.imageView.setImageResource(R.drawable.miscellaneous);
		    } else {
		    	viewHolder.imageView.setImageResource(R.drawable.question);
		    }
		}
	    return convertView;
	}
	
	

}
