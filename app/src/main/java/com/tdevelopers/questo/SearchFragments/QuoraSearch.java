package com.tdevelopers.questo.SearchFragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tdevelopers.questo.R;

import java.util.StringTokenizer;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuoraSearch extends Fragment {

    public static String tobesearch = "";
    public static WebView webView;
    public static Context context;

    public QuoraSearch() {
        // Required empty public constructor
    }

    public static void setQuery(String q) {
        tobesearch = q;

        search();
    }

    public static void search() {

        if (webView != null && tobesearch != null && tobesearch.trim().length() != 0) {
            webView.getSettings().setJavaScriptEnabled(true);
            String url = "https://www.quora.com/search?q=";
            StringTokenizer breaker = new StringTokenizer(tobesearch, " ");
            while (breaker.hasMoreTokens())
                url += breaker.nextToken() + "+";
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    return super.shouldOverrideUrlLoading(view, url);
                }
            });
            webView.loadUrl(url);

        }
    }

    public static QuoraSearch newInstance() {

        Bundle args = new Bundle();

        QuoraSearch fragment = new QuoraSearch();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quora_search, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        webView = (WebView) view.findViewById(R.id.quora);
        context = view.getContext();
        if (tobesearch != null && tobesearch.trim().length() != 0)
            setQuery(tobesearch);
    }
}
