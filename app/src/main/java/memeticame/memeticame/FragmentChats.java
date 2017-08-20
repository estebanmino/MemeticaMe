package memeticame.memeticame;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by efmino on 18-08-17.
 */

public class FragmentChats extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //TextView textView = rootView.findViewById(R.id.section_label);
        //textView.setText("In Chats fragment");

        return inflater.inflate(R.layout.fragment_chats, container, false);
    }
}
