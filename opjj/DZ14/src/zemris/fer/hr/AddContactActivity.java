package zemris.fer.hr;

import zemris.fer.hr.models.Contact;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * An activity used to add a new contact to the application's
 * temporary storage. When adding a contact, the only mandatory field
 * is the phone number. Leaving the other fields empty may disable
 * some functionality of the Contact profile screen, such as Facebook
 * lookup and E-mail sending.
 * @author karlo
 *
 */
public class AddContactActivity extends Activity {
	/*
	 * Only 3 images are available to pick from when choosing a profile
	 * picture. They must be available in the application's assets.
	 */
	private final String[] images = { "image1.jpeg", "image2.jpeg", "image3.jpeg" };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_contact);
		
		// press this button to return to the main activity
		Button btnCancel = (Button) findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				finish();
			}
		});
		
		final TextView editName = (TextView) findViewById(R.id.editName);
		final TextView editPhone = (TextView) findViewById(R.id.editPhone);
		final TextView editEMail = (TextView) findViewById(R.id.editEMail);
		final TextView editFB = (TextView) findViewById(R.id.editFB);
		final TextView editPic = (TextView) findViewById(R.id.editPic);
		final TextView editNote = (TextView) findViewById(R.id.editNote);
		
		// this button is used to save the entered data, if it's provided
		Button btnSave = (Button) findViewById(R.id.btnSave);
		btnSave.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				if(editPhone.getText().toString().isEmpty()) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							AddContactActivity.this);
					builder.setMessage(getText(R.string.noPhone))
							.setCancelable(false)
							.setPositiveButton(getText(R.string.ok), 
								new DialogInterface.OnClickListener() {
								
									public void onClick(DialogInterface dialog, int which) {
										dialog.cancel();
									}
							});
					builder.create().show();
				} else {
					String pic = editPic.getText().toString();
					HomeActivity.kontakti.add(new Contact(
							editName.getText().toString(),
							editPhone.getText().toString(),
							editEMail.getText().toString(),
							(pic.equals(getText(R.string.noPic)) ? "" : pic),
							editNote.getText().toString(),
							editFB.getText().toString()
							));
					finish();
				}
			}
		});
		
		// the profile picture may be selected by clicking on this button
		Button btnPic = (Button) findViewById(R.id.getPicture);
		btnPic.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						AddContactActivity.this);
				builder.setTitle(getText(R.string.pickPic))
						.setItems(images, new DialogInterface.OnClickListener() {
							
							public void onClick(DialogInterface dialog, int which) {
								editPic.setText(images[which]);
							}
						});
				builder.create().show();
			}
		});
	}
	
}