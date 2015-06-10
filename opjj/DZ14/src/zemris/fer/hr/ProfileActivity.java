package zemris.fer.hr;

import java.io.IOException;
import java.io.InputStream;

import zemris.fer.hr.models.Contact;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Every known detail about a particular contact may be examined
 * with this activity. Some additional actions include: clicking on
 * contact's phone number will prompt the user whether to text or call
 * said contact, clicking the e-mail address will trigger the e-mail client
 * chooser. If the contact has a Facebook profile, an additional button will be
 * displayed which may open it in the phone's web browser.
 * @author karlo
 *
 */
public class ProfileActivity extends Activity {
	// positions in the array
	private final int SMS_POS = 0;
	private final int CALL_POS = 1;
	private CharSequence[] phoneMenuItems = new CharSequence[2];	// call/sms supported
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_profile);
		int id = getIntent().getExtras().getInt(HomeActivity.CONTACT_ID);
		final Contact c = HomeActivity.kontakti.get(id);
		
		// if no image was provided, the default one will be used
		
		ImageView iw = (ImageView) findViewById(R.id.prPic);
		iw.setAdjustViewBounds(true);
		if(!c.getImgFile().isEmpty()) {
			try {
				iw.setImageBitmap(getBitmapFromAsset(c.getImgFile()));
			} catch (IOException e) {
				Log.e("ERR", "Cannot open image: " + c.getImgFile());
			}
		} else {
			iw.setImageResource(R.drawable.ic_launcher);
		}
		
		TextView name = (TextView) findViewById(R.id.prName);
		name.setText(c.getName().isEmpty() ?
				getText(R.string.notEntered) : c.getName());
		
		TextView email = (TextView) findViewById(R.id.prEMail);
		email.setText(c.getEmail().isEmpty() ? 
				getText(R.string.notEntered) : c.getEmail());
		// if an email was provided, enable sending to with an external app
		if(!c.getEmail().isEmpty()) {
			email.setOnClickListener(new OnClickListener() {
			
				public void onClick(View v) {
					Intent intent = new Intent(Intent.ACTION_SENDTO, 
							Uri.parse("mailto:" + c.getEmail()));
					startActivity(Intent.createChooser(intent, 
							getText(R.string.sendMailWith)));
				}
			});
		}
		
		TextView note = (TextView) findViewById(R.id.prNote);
		note.setText(c.getNote().isEmpty() ? 
				getText(R.string.notEntered) : c.getNote());
		
		TextView fb = (TextView) findViewById(R.id.prFB);
		fb.setText(c.getFbProfile().isEmpty() ? 
				getText(R.string.notEntered) : c.getFbProfile());
		
		// click on the phone number to text or call the contact
		
		TextView phone = (TextView) findViewById(R.id.prPhone);
		phone.setText(c.getPhoneNum());
		phone.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
				phoneMenuItems[SMS_POS] = getText(R.string.sms);
				phoneMenuItems[CALL_POS] = getText(R.string.call);
				builder.setTitle(getText(R.string.messageType))
						.setItems(phoneMenuItems, new DialogInterface.OnClickListener() {
							
							public void onClick(DialogInterface dialog, int which) {
								Intent intent = null;
								switch(which) {
								case SMS_POS:
									intent = new Intent(Intent.ACTION_VIEW);
									intent.setData(Uri.parse("sms:" + c.getPhoneNum()));
									break;
								case CALL_POS:
									intent = new Intent(Intent.ACTION_SEND);
									intent.setData(Uri.parse("tel:" + c.getPhoneNum()));
									break;
								default:
									break;
								}
								startActivity(intent);
							}
						}); 	
				builder.create().show();
			}
		});
		
		// 'call this contact' button
		
		Button btnCall = (Button) findViewById(R.id.btnCall);
		btnCall.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_CALL);
				intent.setData(Uri.parse("tel:" + c.getPhoneNum()));
				startActivity(intent);
			}
		});
		
		// go to contacts facebook; displayed only if it was provided
		
		Button btnFB = (Button) findViewById(R.id.btnFB);
		if(c.getFbProfile().isEmpty()) {
			btnFB.setClickable(false);
			btnFB.setVisibility(Button.INVISIBLE);
		} else {
			btnFB.setOnClickListener(new OnClickListener() {
			
				public void onClick(View v) {
					Intent intent = new Intent(Intent.ACTION_VIEW, 
							Uri.parse(c.getFbProfile()));
					startActivity(intent);
				}
			});
		}
	}
	
	/**
	 * This method is used to transform the image found in assets
	 * into a bitmap which may be displayed in ImageView.
	 * @param filename of the image
	 * @return Bitmap object representing the image.
	 * @throws IOException if something goes wrong during the I/O
	 */
	private Bitmap getBitmapFromAsset(String strName) throws IOException {
	    AssetManager assetManager = getAssets();

	    InputStream istr = assetManager.open(strName);
	    Bitmap bitmap = BitmapFactory.decodeStream(istr);

	    return bitmap;
	}
}
