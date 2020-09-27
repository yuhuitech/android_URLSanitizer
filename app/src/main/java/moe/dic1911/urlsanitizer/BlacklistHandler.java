package moe.dic1911.urlsanitizer;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Collections;

@SuppressWarnings("SpellCheckingInspection")
public class BlacklistHandler {
    private static SharedPreferences prefs;
    private static ArrayList<String> blacklist;
    private static BlacklistHandler blh;

    public static BlacklistHandler getInstance() {
        return blh;
    }

    public BlacklistHandler(Context c) {
        prefs = c.getSharedPreferences("main", Context.MODE_PRIVATE);
        blacklist = new ArrayList<>();
        if (prefs.contains("blacklist")) {
            Collections.addAll(blacklist, prefs.getString("blacklist", "").split(","));
        } else {
            initialize();
        }
        if (blh == null) blh = this;
    }

    public void initialize() {
        // default blacklisted shit here
        // facebook
        blacklist.add("igshid");
        blacklist.add("fbclid");

        // twitter
        blacklist.add("s");

        // bilibili
        blacklist.add("spm_id_from");

        // Google analytics?
        blacklist.add("utm_source");
        blacklist.add("utm_medium");
        blacklist.add("utm_campaign");
        blacklist.add("utm_term");
        blacklist.add("utm_content");

        String result = buildPrefs();
        prefs.edit().putString("blacklist", result).apply();
    }

    public Boolean isBlacklisted(String query) {
        return blacklist.contains(query);
    }

    public Boolean addEntry(String query) {
        if (blacklist.contains(query))
            return false;
        blacklist.add(query);
        prefs.edit()
            .putString("blacklist", prefs.getString("blacklist", "") + "," + query)
            .apply();
        return true;
    }

    public Boolean removeEntry(int index) {
        try {
            blacklist.remove(index);
            prefs.edit().putString("blacklist", buildPrefs()).apply();
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
        return true;
    }

    public Boolean removeEntry(String value) {
        return removeEntry(blacklist.indexOf(value));
    }

    public String getEntry(int index) {
        return blacklist.get(index);
    }

    public int getBlacklistSize() {
        return blacklist.size();
    }

    private String buildPrefs() {
        StringBuilder sb = new StringBuilder();
        for (String entry : blacklist)
            sb.append(entry).append(",");

        return sb.subSequence(0, sb.length()-1).toString();
    }
}