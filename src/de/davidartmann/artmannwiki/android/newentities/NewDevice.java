package de.davidartmann.artmannwiki.android.newentities;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import de.artmann.artmannwiki.R;
import de.davidartmann.artmannwiki.android.Choice;
import de.davidartmann.artmannwiki.android.database.DeviceManager;
import de.davidartmann.artmannwiki.android.model.Device;


public class NewDevice extends Activity {
	
	private EditText deviceEditText;
	private EditText numberEditText;
	private EditText pinEditText;
	private EditText pukEditText;
	private Button saveButton;
	private DeviceManager deviceManager;
	private String pleaseFillField;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_device);
        // find components
        deviceEditText = (EditText) findViewById(R.id.new_device_edittext_device);
        numberEditText = (EditText) findViewById(R.id.new_device_edittext_number);
        pinEditText = (EditText) findViewById(R.id.new_device_edittext_pin);
        pukEditText = (EditText) findViewById(R.id.new_device_edittext_puk);
        saveButton = (Button) findViewById(R.id.new_device_button_save);
        pleaseFillField = "Bitte ausfüllen";

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate(deviceEditText, numberEditText, pinEditText, pukEditText);
            }
        });
    }


    protected void validate(EditText deviceEditText2, EditText numberEditText2,	EditText pinEditText2, EditText pukEditText2) {
    	String device = deviceEditText.getText().toString().trim();
        String number = numberEditText.getText().toString().trim();
        String pin = pinEditText.getText().toString().trim();
        String puk = pukEditText.getText().toString().trim();
        
        if (device.isEmpty()) {
			deviceEditText2.setError(pleaseFillField);
		}
        if (number.isEmpty()) {
			numberEditText2.setError(pleaseFillField);
		}
        if (pin.isEmpty()) {
			pinEditText2.setError(pleaseFillField);
		}
        if (puk.isEmpty()) {
			pukEditText2.setError(pleaseFillField);
		}
        if (!pin.isEmpty() && !number.isEmpty() && !pin.isEmpty() && !puk.isEmpty()) {
			Device d = new Device(device, number, pin, puk);
			d.setActive(true);
			d.setCreateTime(new Date());
			deviceManager = new DeviceManager(this);
			deviceManager.openWritable();
			deviceManager.addDevice(d);
			deviceManager.close();
			Toast.makeText(this, "Gerät erfolgreich abgespeichert", Toast.LENGTH_SHORT).show();
			goBackToMain();
		}
	}


	private void goBackToMain() {
		Intent intent = new Intent(getBaseContext(), Choice.class);
		startActivity(intent);
	}
	
	protected void onPause() {
		super.onPause();
	}

	protected void onResume() {
		super.onResume();
	}
}
