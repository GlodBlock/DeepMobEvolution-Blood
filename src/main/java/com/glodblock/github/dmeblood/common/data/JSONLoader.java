package com.glodblock.github.dmeblood.common.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.annotation.Nullable;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class JSONLoader {

    private static File rootDir;

    @Nullable
    private static String readJsonFile(URL json) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(json.openStream(), StandardCharsets.UTF_8));
            String s;
            StringBuilder sb = new StringBuilder();
            while ((s = in.readLine()) != null) {
                sb.append(s);
                sb.append("\n");
            }
            in.close();
            return sb.toString();
        } catch (IOException e) {
            return null;
        }
    }

    @Nullable
    private static String readJsonFile(File json) {
        try {
            if (json.exists()) {
                return readJsonFile(json.toURI().toURL());
            }
        } catch (MalformedURLException e) {
            return null;
        }
        return null;
    }

    private static void copyDefaultJsonFile(File json, URL defaultFile) {
        String context = readJsonFile(defaultFile);
        if (context == null) {
            return;
        }
        try {
            if (!json.createNewFile()) {
                return;
            }
            FileWriter out = new FileWriter(json);
            out.write(context);
            out.close();
        } catch (IOException ignore) {
        }
    }

    public static void deserialize(IJSONSerializable instance, String id) {
        String json = readJsonFile(getSubJson(id));
        if (json != null) {
            instance.loadJSON(json);
        } else {
            instance.loadDefault();
            copyDefaultJsonFile(getSubJson(id), getDefaultFile(id));
        }
    }

    public static File getSubJson(String name) {
        return new File(rootDir, name);
    }

    public static void setRoot(File root) {
        rootDir = root;
    }

    public static URL getDefaultFile(String name) {
        return Thread.currentThread().getContextClassLoader().getResource("assets/deepmoblearningbm/default/" + name);
    }

    public static JsonArray getJsonArray(String json) {
        JsonParser jsonParser = new JsonParser();
        return (JsonArray) jsonParser.parse(json);
    }

}
