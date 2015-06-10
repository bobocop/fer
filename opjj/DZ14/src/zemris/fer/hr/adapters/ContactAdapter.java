package zemris.fer.hr.adapters;

import java.util.List;

import zemris.fer.hr.HomeActivity;
import zemris.fer.hr.models.Contact;
import zemris.fer.hr.R;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Used to adapt the array of contacts for use by
 * <code>ListView</code>.
 * @author karlo
 *
 */
public class ContactAdapter extends ArrayAdapter<Contact> {
	private Context context;
	private List<Contact> contacts;
	
	/**
	 * @param context The context in which the list is displayed.
	 * @param contacts A list which contains the contacts.
	 */
	public ContactAdapter(Context context, List<Contact> contacts) {
		super(context, 0);
		this.context = context;
		this.contacts = contacts;
	}

	/**
	 * @return The number of elements in the list.
	 */
	@Override
	public int getCount() {
		return HomeActivity.kontakti.size();
	}

	/**
	 * @return The item at the given position in the list.
	 */
	@Override
	public Contact getItem(int position) {
		return HomeActivity.kontakti.get(position);
	}

	/**
	 * @return The view of elements of the list.
	 */
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		if(view == null) {
			view = ((Activity) context).getLayoutInflater().inflate(
					R.layout.list_item, null); 
		}
		
		Contact c = contacts.get(position);
		
		TextView name = (TextView) view.findViewById(R.id.name);
		TextView phoneAndEmail = (TextView) view.findViewById(R.id.phonemail);
		
		name.setText(c.getName());
		phoneAndEmail.setText(c.getPhoneNum() + ", " + c.getEmail());
		
		return view;
	}
}
