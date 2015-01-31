package com.app.mymeal;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class RecipeSendActivity extends Activity {

	private static final int REQUEST_ENABLE_BT = 1;
	ToggleButton tButton;
	TextView tvStateofToggleButton;
	private List<String> mArrayAdapter;
	ListView availableDevicesListView;
	ListView pairedDevicesListView;
	List<String> availableDevices;
	List<String> pairedDevices;
	BluetoothAdapter mBluetoothAdapter;
	BluetoothDevice mPairedDevice;
	String recipeName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recipe_send);

		Log.e("error", "onCreate");

		tButton = (ToggleButton) findViewById(R.id.toggleButton1);
		tvStateofToggleButton = (TextView) findViewById(R.id.tvstate);
		tvStateofToggleButton.setText("OFF");
		tButton.setOnCheckedChangeListener(listener);

		availableDevicesListView = (ListView) findViewById(R.id.listViewAvailableDevices);
		pairedDevicesListView = (ListView) findViewById(R.id.listViewPairedDevices);

	}

	protected void onNewIntent(Intent intent) {

		recipeName = intent.getStringExtra("recipeName");

	};

	OnCheckedChangeListener listener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {

			if (isChecked) {

				Log.e("error", "isChecked");

				tvStateofToggleButton.setText("ON");

				Log.e("error", "Getting default adapter");

				BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
						.getDefaultAdapter();

				if (mBluetoothAdapter == null) {
					// Device does not support Bluetooth
				}

				if (!mBluetoothAdapter.isEnabled()) {
					// If bluetooth is not enabled, send a request to enable.

					Log.e("error", "If bluetooth adapter is not enabled");

					Intent enableBtIntent = new Intent(
							BluetoothAdapter.ACTION_REQUEST_ENABLE);
					startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
				}

				Log.e("error", "Registering broadcast receiver");

				// Register the BroadcastReceiver
				IntentFilter filter = new IntentFilter(
						BluetoothDevice.ACTION_FOUND);
				registerReceiver(mReceiver, filter); // Don't forget to
														// unregister
														// during onDestroy
				// Start discovery

				Log.e("error", "Starting discovery");

				Intent discoverableIntent = new Intent(
						BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
				discoverableIntent.putExtra(
						BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
				startActivity(discoverableIntent);

				if (mArrayAdapter != null) {

					for (String str : mArrayAdapter) {
						availableDevices.add(str);
					}

					ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
							getApplicationContext(),
							android.R.layout.simple_list_item_1, availableDevices);
					
					availableDevicesListView.setAdapter(arrayAdapter);
					
					availableDevicesListView
					.setOnClickListener(availableDevicesOnClickListener);
				}

			} else {
				tvStateofToggleButton.setText("OFF");

				unregisterReceiver(mReceiver); // Don't forget to
				// unregister
				// during onDestroy

			}

		}
	};

	private final OnClickListener availableDevicesOnClickListener = new OnClickListener() {

		public void onClick(View v) {

			Set<BluetoothDevice> pairedDevices = mBluetoothAdapter
					.getBondedDevices();

			// @SuppressWarnings("rawtypes")
			// ArrayList<String> list = new ArrayList<String>();
			// for (BluetoothDevice bt : pairedDevices)
			// list.add(bt.getName());

			Log.e("BlueToothPaired Device: ", pairedDevices.iterator().next()
					.getName());

			mPairedDevice = pairedDevices.iterator().next();

			BluetoothDevice device = mBluetoothAdapter
					.getRemoteDevice(mPairedDevice.getAddress());
			Method m = null;

			try {
				m = device.getClass().getMethod("createRfcommSocket",
						new Class[] { int.class });
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			BluetoothSocket bs = null;
			try {
				bs = (BluetoothSocket) m.invoke(device, Integer.valueOf(1));
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				bs.connect();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			OutputStream tmpOut = null;

			try {
				tmpOut = bs.getOutputStream();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			// File f = new File("/mnt/sdcard/test.jpg");

			File f = Recipe.getRecipeXml(recipeName, getApplicationContext());

			byte b[] = new byte[(int) f.length()];
			try {
				FileInputStream fileInputStream = new FileInputStream(f);
				fileInputStream.read(b);
			} catch (IOException e) {
				Log.e("error", "Error converting file");
				Log.e("error", e.getMessage());
			}

			try {
				tmpOut.write(b);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// String uri = "/mnt/sdcard/test.jpg";
			// Uri.fromFile(new File(uri));
			//
			// OutputStream os =

			try {
				bs.getOutputStream();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// mPairedDevice.getAddress();

			// Toast.makeText(getApplicationContext(), "Showing Paired Devices",
			// Toast.LENGTH_SHORT).show();
			// @SuppressWarnings("rawtypes")
			// final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
			// getApplicationContext(),
			// android.R.layout.simple_list_item_1, list);
			// listDevicesFound.setAdapter(adapter);
		};
	};

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Check if the enabling request is complete.
		if (requestCode == 1) {

			if (resultCode == RESULT_OK) {
				Log.e("error", "result ok");
				String result = data.getStringExtra("result");
			}
			if (resultCode == RESULT_CANCELED) {
				// Write your code if there's no result
				Log.e("error", "result cancelled");
			}
		}
	}// onActivityResult

	// Create a BroadcastReceiver for ACTION_FOUND
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

		public void onReceive(Context context, Intent intent) {
			Log.e("error", "onReceive");
			String action = intent.getAction();
			// When discovery finds a device
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {

				Log.e("error", "ACTION_FOUND");

				// Get the BluetoothDevice object from the Intent
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				// Add the name and address to an array adapter to show in a
				// ListView
				mArrayAdapter
						.add(device.getName() + "\n" + device.getAddress());

			}
		}
	};

}
