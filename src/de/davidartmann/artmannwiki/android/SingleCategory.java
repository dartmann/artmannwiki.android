package de.davidartmann.artmannwiki.android;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import de.artmann.artmannwiki.R;


public class SingleCategory extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_category);
        fillWithData();
    }

    private void fillWithData() {
        Intent intent = getIntent();
        String category = intent.getStringExtra("category");
        setTitle(category);
        /*
        DBManager dbManager = new DBManager(SingleCategory.this);
        TableLayout tableLayout = (TableLayout) findViewById(R.id.single_category_tablelayout);
        ArrayList<Entity> allEntitiesArrayList = dbManager.getAllEntities();
        fillFirstRowWithData(category);
        try {
            if (category.equals(dbManager.getKategorieLogin())) {
                for (Entity entity : allEntitiesArrayList) {
                    TableRow tableRow = new TableRow(this);
                    TableRow.LayoutParams rowLayoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                    tableRow.setLayoutParams(rowLayoutParams);
                    tableRow.setGravity(Gravity.LEFT);
                    tableRow.setPadding(0,0,0,2);
                    //UserView erstellen:
                    TextView userTextView = new TextView(this);
                    userTextView.setPadding(0,0,10,0);
                    userTextView.setLayoutParams(rowLayoutParams);
                    userTextView.setText(entity.get_benutzer());
                    userTextView.setTextSize(16);
                    tableRow.addView(userTextView);
                    //PasswordView erstellen:
                    TextView passwordTextView = new TextView(this);
                    passwordTextView.setPadding(0,0,10,0);
                    passwordTextView.setLayoutParams(rowLayoutParams);
                    passwordTextView.setText(entity.get_password());
                    passwordTextView.setTextSize(16);
                    tableRow.addView(passwordTextView);
                    //
                    tableLayout.addView(tableRow);
                }
            }
            else if (category.equals(dbManager.getKategorieDevice())) {
                for (Entity entity : allEntitiesArrayList) {

                }
            }
            else {
                for (Entity entity : allEntitiesArrayList) {

                }
            }
            
        }
        catch (Exception e) {}
        */
    }

    private void fillFirstRowWithData(String category) {
        TableLayout tableLayout = (TableLayout) findViewById(R.id.single_category_tablelayout);
        //DBManager dbManager = new DBManager(this);
        TableRow tableRow = new TableRow(this);
        TableRow.LayoutParams rowLayoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        tableRow.setLayoutParams(rowLayoutParams);
        tableRow.setGravity(Gravity.LEFT);
        tableRow.setBackgroundColor(Color.LTGRAY);
        tableRow.setPadding(0,0,0,2);

        /*
        if (category.equals(dbManager.getKategorieLogin())) {
            TextView textView = new TextView(this);
            textView.setPadding(0,0,10,0);
            textView.setLayoutParams(rowLayoutParams);
            textView.setText("Benutzer");   textView.setTextSize(18);   tableRow.addView(textView);
            //
            TextView textView2 = new TextView(this);
            textView2.setLayoutParams(rowLayoutParams);
            textView2.setText("Passwort");   textView2.setTextSize(18);   tableRow.addView(textView2);
        }
        else if (category.equals(dbManager.getKategorieDevice())) {
            TextView textView = new TextView(this);
            textView.setPadding(0,0,10,0);
            textView.setLayoutParams(rowLayoutParams);
            textView.setText("Handy/Tablet");   textView.setTextSize(18);   tableRow.addView(textView);
            //
            TextView textView2 = new TextView(this);
            textView2.setPadding(0,0,10,0);
            textView2.setLayoutParams(rowLayoutParams);
            textView2.setText("Nummer");   textView2.setTextSize(18);   tableRow.addView(textView2);
            //
            TextView textView3 = new TextView(this);
            textView3.setPadding(0,0,10,0);
            textView3.setLayoutParams(rowLayoutParams);
            textView3.setText("PIN");   textView3.setTextSize(18);   tableRow.addView(textView3);
            //
            TextView textView4 = new TextView(this);
            textView4.setLayoutParams(rowLayoutParams);
            textView4.setText("PUK");   textView4.setTextSize(18);   tableRow.addView(textView4);
        }
        else {
            TextView textView = new TextView(this);
            textView.setPadding(0,0,10,0);
            textView.setLayoutParams(rowLayoutParams);
            textView.setText("E-Mail");   textView.setTextSize(18);   tableRow.addView(textView);
            //
            TextView textView2 = new TextView(this);
            textView2.setLayoutParams(rowLayoutParams);
            textView2.setText("Passwort");   textView2.setTextSize(18);   tableRow.addView(textView2);
        }
        tableLayout.addView(tableRow);
        */
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.single_category, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
