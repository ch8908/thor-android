package com.osolve.thor.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.osolve.thor.fragment.event.SignInSuccessEvent;
import com.squareup.otto.Subscribe;

/**
 * Created by Kros on 7/24/14.
 */
public class LeftMenuListFragment extends BaseListFragment {

    enum MenuOptions {
        ADD_SHOP(R.string.leftMenu_add_shop),
        SIGN_IN(R.string.leftMenu_sign_in),
        SIGN_OUT(R.string.leftMenu_sign_out),
        ABOUT_APP(R.string.leftMenu_about_app);

        private final int resId;

        MenuOptions(int resId) {

            this.resId = resId;
        }

        public int getResId() {
            return resId;
        }
    }

    private SampleAdapter adapter;

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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new SampleAdapter(getActivity());
        adapter.add(MenuOptions.ADD_SHOP);
        adapter.add(MenuOptions.SIGN_IN);
        adapter.add(MenuOptions.ABOUT_APP);
        setListAdapter(adapter);
        adapter.notifyDataSetChanged();

        bean().accountDaemon.askIsLoggedIn();
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {

        MenuOptions item = adapter.getItem(position);

        switch (item) {
            case ADD_SHOP:
                addShop();
                break;
            case SIGN_IN:
                bean().postBusEvent(new SignInEvent());
                break;
            case SIGN_OUT:
                signOut();
                break;
            case ABOUT_APP:
                break;
            default:
                break;
        }
    }

    private void addShop() {
        int found = adapter.findOption(MenuOptions.SIGN_IN);

        if (found > -1) {
            showSignInRequiredDialog();
            return;
        }

        bean().postBusEvent(new AddShopEvent());
    }

    private void showSignInRequiredDialog() {
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.leftMenu_sign_in)
                .setMessage(R.string.leftMenu_sign_in_required)
                .setPositiveButton(R.string.leftMenu_sign_in, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        bean().postBusEvent(new SignInEvent());
                    }
                })
                .setNegativeButton(R.string.leftMenu_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void signOut() {

        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.leftMenu_sign_out)
                .setMessage(R.string.leftMenu_sign_out_confirm)
                .setPositiveButton(R.string.leftMenu_sign_out, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        bean().accountDaemon.signOut();
                        adapter.replaceOptions(MenuOptions.SIGN_OUT, MenuOptions.SIGN_IN);
                    }
                })
                .setNegativeButton(R.string.leftMenu_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Subscribe
    public void onSignInSuccessEvent(SignInSuccessEvent event) {
        adapter.replaceOptions(MenuOptions.SIGN_IN, MenuOptions.SIGN_OUT);
        adapter.notifyDataSetChanged();
    }

    public class SampleAdapter extends ArrayAdapter<MenuOptions> {

        public SampleAdapter(Context context) {
            super(context, 0);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_left_menu, null);
            }

            TextView title = (TextView) convertView.findViewById(R.id.titleTextView);
            MenuOptions item = getItem(position);
            title.setText(getContext().getResources().getText(item.getResId()));

            return convertView;
        }

        public void replaceOptions(MenuOptions oldOptions, MenuOptions newOptions) {
            int position = getPosition(oldOptions);
            if (position == -1) {
                return;
            }
            remove(oldOptions);
            insert(newOptions, position);
            notifyDataSetChanged();
        }

        public int findOption(MenuOptions option) {
            return getPosition(option);
        }
    }
}
