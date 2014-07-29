package com.osolve.thor.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.osolve.thor.R;
import com.osolve.thor.app.BaseListFragment;
import com.osolve.thor.fragment.event.AddShopEvent;
import com.osolve.thor.fragment.event.SignInEvent;

/**
 * Created by Kros on 7/24/14.
 */
public class LeftMenuListFragment extends BaseListFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_left_menu, null);
        if (savedInstanceState != null) {
            // currently ArticleListFragment is stateless (it can rebuild its own state without
            // arguments)
        }
        return rootView;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        switch (position) {
            case 0:
                bean().postBusEvent(new AddShopEvent());
                break;
            case 1:
                bean().postBusEvent(new SignInEvent());
                break;
            case 2:
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SampleAdapter adapter = new SampleAdapter(getActivity());
        adapter.add("Add Shop");
        adapter.add("Sign In");
        adapter.add("Title3");
        setListAdapter(adapter);
    }

    public class SampleAdapter extends ArrayAdapter<String> {

        public SampleAdapter(Context context) {
            super(context, 0);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_left_menu, null);
            }

            TextView title = (TextView) convertView.findViewById(R.id.titleTextView);
            title.setText(getItem(position));

            return convertView;
        }

    }
}
