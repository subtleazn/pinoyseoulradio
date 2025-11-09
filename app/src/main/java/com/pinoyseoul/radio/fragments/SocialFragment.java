package com.pinoyseoul.radio.fragments;

import android.annotation.TargetApi;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.pinoyseoul.radio.databinding.FragmentSocialBinding;
import com.pinoyseoul.radio.R;
import com.pinoyseoul.radio.settings.Configs;
import com.pinoyseoul.radio.utils.JavaScriptInterface;

public class SocialFragment extends Fragment implements Configs {
    private FragmentSocialBinding binding;
    private JavaScriptInterface javaScriptInterface;

    public SocialFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSocialBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        runWebview(W_FB);

        return rootView;
    }

    private void runWebview(String url) {

        String INTERFACE_NAME = "Android";

        binding.webView.setWebViewClient(new WebViewClient() {
            @SuppressWarnings("deprecation")
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(getActivity(), description, Toast.LENGTH_SHORT).show();
            }

            @TargetApi(android.os.Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
                onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
            }
        });

        binding.webView.loadUrl(url);
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.setWebViewClient(new WebViewClient());

        javaScriptInterface = new JavaScriptInterface(getActivity());
        binding.webView.addJavascriptInterface(javaScriptInterface, INTERFACE_NAME);

    }

}
