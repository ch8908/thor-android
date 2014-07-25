package com.osolve.thor.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.osolve.thor.R;
import com.osolve.thor.app.BaseFragment;

/**
 * Created by Kros on 7/25/14.
 */
public class ShopDetailFragment extends BaseFragment {

    private static final String SHOP_ID_BUNDLE_KEY = "SHOP_ID_BUNDLE_KEY";

    public static ShopDetailFragment newInstance(final String shopId) {
        Bundle args = new Bundle();
        ShopDetailFragment shopDetailFragment = new ShopDetailFragment();
        args.putString(SHOP_ID_BUNDLE_KEY, shopId);
        shopDetailFragment.setArguments(args);
        return shopDetailFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shop_detail, container, false);
    }
}
