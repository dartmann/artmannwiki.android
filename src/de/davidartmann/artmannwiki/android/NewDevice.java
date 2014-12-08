package de.davidartmann.artmannwiki.android;

import de.artmann.artmannwiki.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class NewDevice extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_device);
        final EditText deviceEdittext = (EditText) findViewById(R.id.new_device_edittext_device);
        final EditText numberEdittext = (EditText) findViewById(R.id.new_device_edittext_number);
        final EditText pinEdittext = (EditText) findViewById(R.id.new_device_edittext_pin);
        final EditText pukEdittext = (EditText) findViewById(R.id.new_device_edittext_puk);
        Button saveButton = (Button) findViewById(R.id.new_device_button_save);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String device = deviceEdittext.getText().toString().trim();
                String number = numberEdittext.getText().toString().trim();
                String pin = pinEdittext.getText().toString().trim();
                String puk = pukEdittext.getText().toString().trim();
                if (!(device.isEmpty()&&(number.isEmpty())&&(pin.isEmpty())&&(puk.isEmpty()))) {
                    DBManager dbManager = new DBManager(NewDevice.this);
                    dbManager.addDevice(new Entity(device, number, pin, puk));
                    Toast.makeText(getApplicationContext(), R.string.prompt_saved_succesfully, Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), R.string.prompt_all_fields_must_be_filled, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_device, menu);
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
