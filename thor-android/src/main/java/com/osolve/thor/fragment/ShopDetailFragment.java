package com.osolve.thor.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.osolve.thor.R;
import com.osolve.thor.app.BaseFragment;
import com.osolve.thor.model.ShopDto;

import bolts.Continuation;
import bolts.Task;

/**
 * Created by Kros on 7/25/14.
 */
public class ShopDetailFragment extends BaseFragment {

    private static final String SHOP_ID_BUNDLE_KEY = "SHOP_ID_BUNDLE_KEY";
    private static final String TAG = ShopDetailFragment.class.getSimpleName();
    private ListView infoListView;
    private ShopDetailAdapter adapter;
    private ViewPager pictureViewPager;


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
        View rootView = inflater.inflate(R.layout.fragment_shop_detail, container, false);
        String shopId = getArguments().getString(SHOP_ID_BUNDLE_KEY);


        adapter = new ShopDetailAdapter(getActivity());
        infoListView = (ListView) rootView.findViewById(R.id.informationListView);

        infoListView.setAdapter(adapter);

        pictureViewPager = (ViewPager) rootView.findViewById(R.id.pictureViewPager);

        bean().coffeeShopDaemon.shopDetailWithId(shopId).continueWith(new Continuation<ShopDto, Object>() {
            @Override
            public Object then(Task<ShopDto> task) throws Exception {
                if (task.isFaulted()) {
                    Log.e(TAG, "shopDetailWithId error", (VolleyError) task.getError());
                    return null;
                }
                ShopDto shopDto = task.getResult();
                Log.d(TAG, "shoptDetail:" + shopDto);
                adapter.reset(shopDto);
                return null;
            }
        });

        return rootView;
    }


}
