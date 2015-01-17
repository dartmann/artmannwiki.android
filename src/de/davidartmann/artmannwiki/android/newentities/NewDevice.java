package de.davidartmann.artmannwiki.android.newentities;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import de.artmann.artmannwiki.R;
import de.davidartmann.artmannwiki.android.Choice;
import de.davidartmann.artmannwiki.android.backend.BackendConstants;
import de.davidartmann.artmannwiki.android.backend.SyncManager;
import de.davidartmann.artmannwiki.android.backend.VolleyRequestQueue;
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
        deviceEditText = (EditText) findViewById(R.id.new_device_edittext_device);
        numberEditText = (EditText) findViewById(R.id.new_device_edittext_number);
        pinEditText = (EditText) findViewById(R.id.new_device_edittext_pin);
        pukEditText = (EditText) findViewById(R.id.new_device_edittext_puk);
        saveButton = (Button) findViewById(R.id.new_device_button_save);
        pleaseFillField = "Bitte ausfüllen";

        checkIfUpdate();
        
        saveButton.setOnClickListener(new View.OnClickListener() {
            
            public void onClick(View view) {
            	if (getIntent().getBooleanExtra("update", false)) {
            		updateDevice(deviceEditText, numberEditText, pinEditText, pukEditText);
				} else {
					createDevice(deviceEditText, numberEditText, pinEditText, pukEditText);
				}
            }
        });
    }
    
    /**
	 * Method to send the created device to the backend.
	 * Via a Volley {@link JsonObjectRequest}
	 * @param d ({@link Device})
	 * @param url ({@link String})
	 */
	private void createInBackend(final Device d, String url) {
		JSONObject jDevice = new JSONObject();
		try {
			jDevice.put("active", d.isActive());
			jDevice.put("name", d.getName());
			jDevice.put("number", d.getNumber());
			jDevice.put("pin", d.getPin());
			jDevice.put("puk", d.getPuk());
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jDevice, 
			new Response.Listener<JSONObject>() {
				public void onResponse(JSONObject response) {
					deviceManager = new DeviceManager(NewDevice.this);
					deviceManager.openWritable(NewDevice.this);
					Device tempDev = deviceManager.addDevice(d);
					try {
						deviceManager.addBackendId(tempDev.getId(), response.getLong("id"));
					} catch (JSONException e1) {
						e1.printStackTrace();
					}
					deviceManager.close();
					new SyncManager().setLocalSyncTimeWithBackendResponse(NewDevice.this);
					Toast.makeText(NewDevice.this, "Gerät erfolgreich abgespeichert", Toast.LENGTH_SHORT).show();
				}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					VolleyLog.e("Error: ", error.getMessage());
					Toast.makeText(NewDevice.this, "Konnte Gerät nicht speichern, Fehler bei Übertragung zum Server", Toast.LENGTH_SHORT).show();
				}
			}) 	{
		       	public Map<String, String> getHeaders() throws AuthFailureError {
		           	HashMap<String, String> headers = new HashMap<String, String>();
		           	headers.put(BackendConstants.HEADER_KEY, BackendConstants.HEADER_VALUE);
		       		headers.put(BackendConstants.CONTENT_TYPE, BackendConstants.APPLICATION_JSON);
		       		return headers;
		       	}
		};
		VolleyRequestQueue.getInstance(this).addToRequestQueue(jsonObjectRequest);
	}
	
	/**
	 * Method to send the updated device to the backend.
	 * Via a Volley {@link JsonObjectRequest}
	 * @param d ({@link Device})
	 * @param url ({@link String})
	 */
	private void updateInBackend(final Device d, String url) {
		JSONObject jDevice = new JSONObject();
		try {
			jDevice.put("active", d.isActive());
			jDevice.put("name", d.getName());
			jDevice.put("number", d.getNumber());
			jDevice.put("pin", d.getPin());
			jDevice.put("puk", d.getPuk());
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jDevice, 
			new Response.Listener<JSONObject>() {
				public void onResponse(JSONObject response) {
					deviceManager = new DeviceManager(NewDevice.this);
					deviceManager.openWritable(NewDevice.this);
					deviceManager.updateDeviceById(d);
					deviceManager.close();
					new SyncManager().setLocalSyncTimeWithBackendResponse(NewDevice.this);
					Toast.makeText(NewDevice.this, "Gerät erfolgreich aktualisiert", Toast.LENGTH_SHORT).show();
				}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					VolleyLog.e("Error: ", error.getMessage());
					Toast.makeText(NewDevice.this, "Konnte Gerät nicht aktualisieren, Fehler bei Übertragung zum Server", Toast.LENGTH_SHORT).show();
				}
			}) 	{
		       	public Map<String, String> getHeaders() throws AuthFailureError {
		           	HashMap<String, String> headers = new HashMap<String, String>();
		           	headers.put(BackendConstants.HEADER_KEY, BackendConstants.HEADER_VALUE);
		       		headers.put(BackendConstants.CONTENT_TYPE, BackendConstants.APPLICATION_JSON);
		       		return headers;
		       	}
		};
		VolleyRequestQueue.getInstance(this).addToRequestQueue(jsonObjectRequest);
	}


    /**
     * Method to update an existing {@link Device}
     * @param deviceEditText2
     * @param numberEditText2
     * @param pinEditText2
     * @param pukEditText2
     */
    private void updateDevice(EditText deviceEditText2, EditText numberEditText2, EditText pinEditText2, EditText pukEditText2) {
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
	    	Device d = (Device) getIntent().getSerializableExtra("device");
			d.setName(device);
			d.setNumber(number);
			d.setPin(pin);
			d.setPuk(puk);
//			deviceManager = new DeviceManager(this);
//			deviceManager.openWritable(this);
//			d = deviceManager.updateDevice(d);
//			deviceManager.close();
			updateInBackend(d, BackendConstants.ARTMANNWIKI_ROOT+BackendConstants.UPDATE_DEVICE+d.getBackendId());
			goBackToMain();
        }
	}

    /**
	 * Method to fill the EditText fields with data. If editing was chosen before.
	 */
	private void checkIfUpdate() {
    	if (getIntent().getSerializableExtra("device") != null) {
			setTitle("Gerät aktualisieren");
    		Device d = (Device) getIntent().getSerializableExtra("device");
			deviceEditText.setText(d.getName());
			numberEditText.setText(d.getNumber());
			pinEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
			pinEditText.setText(d.getPin());
			pukEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
			pukEditText.setText(d.getPuk());
		} else {
			setTitle("Neues Gerät");
		}
	}

	/**
	 * Method to create a new {@link Device}
	 * @param deviceEditText2
	 * @param numberEditText2
	 * @param pinEditText2
	 * @param pukEditText2
	 */
	private void createDevice(EditText deviceEditText2, EditText numberEditText2,	EditText pinEditText2, EditText pukEditText2) {
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
//			deviceManager = new DeviceManager(this);
//			deviceManager.openWritable(this);
//			d = deviceManager.addDevice(d);
//			deviceManager.close();
			createInBackend(d, BackendConstants.ARTMANNWIKI_ROOT+BackendConstants.ADD_DEVICE);
			goBackToMain();
		}
	}

	private void goBackToMain() {
		Intent intent = new Intent(getBaseContext(), Choice.class);
		finish();
		startActivity(intent);
	}
	
	protected void onPause() {
		super.onPause();
	}

	protected void onResume() {
		super.onResume();
	}
}
