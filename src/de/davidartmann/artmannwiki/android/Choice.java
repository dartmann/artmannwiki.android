package de.davidartmann.artmannwiki.android;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import de.artmann.artmannwiki.R;


public class Choice extends Activity {
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_choice_landscape);
        }
        else {
            setContentView(R.layout.activity_choice_portrait);
        }
        
        Button wikiSearchButton = (Button) findViewById(R.id.choice_wiki_search);
        Button wikiNewEntityButton = (Button) findViewById(R.id.choice_wiki_new_entity);

        wikiSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	Intent intent = new Intent(getBaseContext(), CategoryListSearch.class);
                startActivity(intent);
            }
        });
        wikiNewEntityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	Intent intent = new Intent(getBaseContext(), CategoryListSave.class);
                startActivity(intent);
            }
        });
        
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // not necessary, so commented
    	getMenuInflater().inflate(R.menu.choice, menu);
        return super.onCreateOptionsMenu(menu);
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
		case R.id.menu_choice_action_sync:
			return true;
		case R.id.menu_choice_action_exit:
			finish();
	        Runtime.getRuntime().addShutdownHook(new Thread() {
	        	@Override
	        	public void run() {
	        		Intent intent = new Intent(Intent.ACTION_MAIN);
	        		intent.addCategory(Intent.CATEGORY_HOME);
	        		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        		startActivity(intent);
	        	}
	        });;
			return true;
		default:
			break;
		}
        return super.onOptionsItemSelected(item);
    }
}
