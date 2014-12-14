package de.davidartmann.artmannwiki.android.adapter;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import de.artmann.artmannwiki.R;

public class CategoryListSearchArrayAdapter extends ArrayAdapter<String> {
	
	private final Activity context;
	private final List<String> names;
	private LayoutInflater inflater;
	
	static class ViewHolder {
	    TextView textView;
    }

	public CategoryListSearchArrayAdapter(Activity context, List<String> names) {
		super(context, R.layout.activity_categorylist_save, names);
		this.context = context;
		this.names = names;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			inflater = context.getLayoutInflater();
			convertView = inflater.inflate(R.layout.activity_categorylist_search, null);
			viewHolder = new ViewHolder();
			viewHolder.textView = (TextView) convertView.findViewById(R.id.activity_categorylist_search_listview);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		return convertView;
	}
	
	
}
