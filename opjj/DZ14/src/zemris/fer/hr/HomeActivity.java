package zemris.fer.hr;

import java.io.InputStream;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import zemris.fer.hr.adapters.ContactAdapter;
import zemris.fer.hr.models.Contact;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;

/**
 * The home screen of this application. It displays a short
 * info about each contact in a list. Each item (contact) of this
 * list may be clicked on to be taken to its profile page where
 * a more detailed information about that contact is displayed.
 * Users may deletc contatcs by <i>long-clicking</i> them on
 * this screen.
 * @author karlo
 *
 */
public class HomeActivity extends Activity {
	public static ArrayList<Contact> kontakti;
	public static int BUF_SIZE = 4096;
	public static String CONTACT_ID = "cid";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*
		 * Contacts' list is initialized from JSON on
		 * activity creation. The list is then used by
		 * other activities, including this one in its
		 * 'onStart' method.
		 */
		if(kontakti == null) {
			kontakti = new ArrayList<Contact>();
			byte[] buf = new byte[BUF_SIZE];
			JSONObject jsonObj;
			JSONArray people = null;
			try {
				InputStream in = this.getAssets().open("people.json");
				in.read(buf);
				jsonObj = new JSONObject(new String(buf));
				people = jsonObj.getJSONArray("people");
				in.close();
			} catch (Exception e) {
				Log.e("ERR", "Error during contact initialization: " + e.getMessage());
			}
			
			for(int i = 0; i < people.length(); i++) {
				try {
					JSONObject person = people.getJSONObject(i).getJSONObject("person");
					kontakti.add(
							new Contact(
									person.getString("name"),
									person.getString("phone"),
									person.getString("email"),
									person.getString("image"),
									person.getString("note"),
									person.getString("facebook_profile")
									)
							);
				} catch (JSONException e) {
					Log.e("ERR", e.getMessage());
				}
			}
		}
		
		setContentView(R.layout.activity_home);
		
		// "add" button
		
		Button btnDodaj = (Button) findViewById(R.id.btnDodaj);
		btnDodaj.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(HomeActivity.this, AddContactActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
		ListView lw = (ListView) findViewById(R.id.listKontakt);
		lw.setAdapter(new ContactAdapter(this, kontakti));
		lw.setClickable(true);
		
		// short click - going to that contact's page
		
		lw.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
				intent.putExtra(CONTACT_ID, position);
				startActivity(intent);
			}
		});
		
		// long click - deleting a contact, including a dialog to make sure
		
		lw.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				final int pos = position;
				AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
				builder.setMessage(getText(R.string.deleteContact))
						.setCancelable(false)
						.setPositiveButton(getText(R.string.yes), 
								new DialogInterface.OnClickListener() {
							
							public void onClick(DialogInterface dialog, int which) {
								kontakti.remove(pos);
								Intent intent = getIntent();
								finish();
								startActivity(intent);
							}
						})
						.setNegativeButton(getText(R.string.no), 
								new DialogInterface.OnClickListener() {
							
							public void onClick(DialogInterface dialog, int which) {
								dialog.cancel();
							}
						});
				builder.create().show();
				return true;
			}
			
		});
			
	}
}
