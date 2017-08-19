package memeticame.memeticame;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by efmino on 18-08-17.
 */

public class ContactsAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Contact> arrayList;

    public ContactsAdapter(Context context, ArrayList<Contact> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.contact_view,null);
        }

        TextView contact_name = convertView.findViewById(R.id.contact_name);
        //TextView contact_phone = (TextView)convertView.findViewById(R.id.contact_phone);

        contact_name.setText(arrayList.get(position).getContact_name());
        //contact_phone.setText(arrayList.get(position).getContact_phone());

        return convertView;
    }
}
