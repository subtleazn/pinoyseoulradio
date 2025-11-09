package com.pinoyseoul.radio.dialog;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.pinoyseoul.radio.R;
import com.pinoyseoul.radio.databinding.BottomSheetBinding;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

public class SearchPanel extends BottomSheetDialogFragment {

    private BottomSheetBinding binding;
    String uri;
    String song_title;

    public static SearchPanel newInstance() {
        return new SearchPanel();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = BottomSheetBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        binding.btnInstall.setVisibility(View.GONE);

        Bundle mArgs = getArguments();

        String t = mArgs.getString("song").toUpperCase();
        String r1 = t.replace("/", " ");
        String r2 = r1.replace("\n", " ");
        song_title = r2;


        String txt = "find : \n" + t + "\n"+"in popular music streaming services.";

        int art = (r2.replace(" ","").length());
        int all = txt.length();
        int start =  song_title.length()+8;


        Spannable wordtoSpan = new SpannableString(txt);
        wordtoSpan.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordtoSpan.setSpan(new ForegroundColorSpan(Color.BLACK), start, all, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        binding.topTxt.setText(wordtoSpan);


        System.out.println("WWW - - " + art + " " + all);

        binding.btnDeezer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ifApprInstalled(getActivity(),"deezer.android.app")) {

                    try {

                        String decoded = URLDecoder.decode(song_title, "UTF-8");

                        uri = "deezer://www.deezer.com/search//" + decoded + "/";

                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        requireActivity().startActivity(intent);
                        dismiss();

                    } catch (UnsupportedEncodingException ee) {

                    }

                } else {

                    showAppAlert("Deezer", "https://play.google.com/store/apps/details?id=deezer.android.app&hl=us", requireActivity());

                }
            }
        });

        binding.btnApple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ifApprInstalled( requireActivity(),"com.apple.android.music")) {
                    // Network request removed
                } else {

                    showAppAlert("Apple music", "https://play.google.com/store/apps/details?id=com.apple.android.music&hl=en", requireActivity());

                }

            }
        });

        binding.btnSpotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ifApprInstalled(requireActivity(),"com.spotify.music")) {

                    try {

                        String decoded = URLDecoder.decode(song_title, "UTF-8");
                        uri = "spotify:search:" + decoded ;


                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        requireActivity().startActivity(intent);
                        dismiss();

                    } catch (UnsupportedEncodingException ee) {
                        Log.d("XXX", ee.getLocalizedMessage());

                    }

                } else {

                    showAppAlert("Spotify", "https://play.google.com/store/apps/details?id=com.spotify.music&hl=en", requireActivity());

                }

            }
        });

        binding.btnVk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ifApprInstalled(requireActivity(),"com.vkontakte.android") || ifApprInstalled(requireActivity(),"com.vtosters.android")) {

                    try {

                        String decoded = URLDecoder.decode(song_title, "UTF-8"); // https://vk.com/audio?q=Vanotek,%20Eneli%20-%20Back%20to%20Me
                        uri = "vk://vk.com/audio?q=" + decoded;
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        requireActivity().startActivity(intent);
                        dismiss();

                    } catch (UnsupportedEncodingException ee) {
                        Log.d("XXX", ee.getLocalizedMessage());

                    }

                } else {

                    showAppAlert("Vk", "https://play.google.com/store/apps/details?id=com.vkontakte.android&hl=en", requireActivity());

                }

            }
        });
        binding.btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    String decoded = URLDecoder.decode(song_title, "UTF-8"); // https://vk.com/audio?q=Vanotek,%20Eneli%20-%20Back%20to%20Me
                    uri = "googleplaymusic://play.google.com//music/search/" + decoded;
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    requireActivity().startActivity(intent);
                    dismiss();

                } catch (UnsupportedEncodingException ee) {
                    Log.d("XXX", ee.getLocalizedMessage());

                }
            }
        });
        binding.btnYandex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    String decoded = URLDecoder.decode(song_title, "UTF-8"); // https://vk.com/audio?q=Vanotek,%20Eneli%20-%20Back%20to%20Me
                    uri = "yandexmusic://search/?text=" + decoded;
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    requireActivity().startActivity(intent);
                    dismiss();

                } catch (UnsupportedEncodingException ee) {
                    Log.d("XXX", ee.getLocalizedMessage());

                }

            }
        });

        return rootView;

    }

    private void showAppAlert(String app, String url, Activity activity) {

        binding.btnInstall.setVisibility(View.VISIBLE);
        binding.btnInstall.setText(getString(R.string.please_install) + app);
        binding.btnInstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = uri;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                activity.startActivity(i);

            }
        });

        binding.topTxt.setText(R.string.not_app);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    public static boolean ifApprInstalled(Activity activity, String pacaje) {
        PackageManager manager = activity.getPackageManager();
        List<ApplicationInfo> infoList = manager.getInstalledApplications(PackageManager.GET_META_DATA);


        for (ApplicationInfo info : infoList) {

            System.out.println("AAAPPP = =  " + info.packageName + "  " + pacaje);

            if (info.packageName.equals(pacaje)) {
                return true;
            }
        }
        return false;
    }
}
