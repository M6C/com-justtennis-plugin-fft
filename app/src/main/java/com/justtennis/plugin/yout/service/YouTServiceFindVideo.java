package com.justtennis.plugin.yout.service;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.justtennis.plugin.generic.converter.GenericFormResponseConverter;
import com.justtennis.plugin.generic.parser.GenericFormParser;
import com.justtennis.plugin.generic.query.response.GenericFormResponse;
import com.justtennis.plugin.shared.exception.NotConnectedException;
import com.justtennis.plugin.shared.network.model.ResponseHttp;
import com.justtennis.plugin.shared.query.response.FormElement;
import com.justtennis.plugin.shared.service.AbstractService;
import com.justtennis.plugin.shared.tool.FileUtil;
import com.justtennis.plugin.yout.query.request.YouTFindVideoFormRequest;
import com.justtennis.plugin.yout.query.response.YoutFindVideoResponse;

import org.jsoup.helper.StringUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YouTServiceFindVideo extends AbstractYouTService {

    private static final String TAG = YouTServiceFindVideo.class.getName();

    private YouTServiceFindVideo(Context context) {
        super(context);
    }

    public static YouTServiceFindVideo newInstance(Context context) {
        return new YouTServiceFindVideo(context);
    }

    public GenericFormResponse getFindForm(ResponseHttp responseHttp, String search) {
        AbstractService.logMethod("getFindForm");
        GenericFormResponse ret = null;

        if (!StringUtil.isBlank(responseHttp.body)) {
            ret = GenericFormParser.getInstance().parseForm(responseHttp.body, new YouTFindVideoFormRequest());
            Map<String, FormElement> fieldValue = ret.fieldValue;
            fieldValue.get(YouTFindVideoFormRequest.KEY_FIELD_SEARCH).value = search;

            for(String key : fieldValue.keySet()) {
                FormElement val = fieldValue.get(key);
                System.out.println("==============> new form element key:" + key + "  name:" + val.name + " value:" + val.value);
            }
        }

        return ret;
    }

    public ResponseHttp submitFindForm(ResponseHttp form, GenericFormResponse response) throws NotConnectedException {
        Map<String, String> data = GenericFormResponseConverter.toDataMap(response);
        return doPostConnected(URL_ROOT, "/results", data, form);
    }

    public YoutFindVideoResponse getFind(ResponseHttp findResponseHttp) {
        YoutFindVideoResponse ret = new YoutFindVideoResponse();
        String jsonString = parseRegexData(findResponseHttp.body);
        JsonElement root = new JsonParser().parse(jsonString);

        Object[] pathNode = new Object[] {
            "contents",new String[]{"twoColumnSearchResultsRenderer","twoColumnBrowseResultsRenderer"},"primaryContents","sectionListRenderer","contents",
            0,
            "itemSectionRenderer", "contents"
        };

        String[] contentRenderer = new String[]{"videoRenderer","channelRenderer","playlistRenderer"};
        JsonElement elem = parseJsonNode(root, pathNode);
        if (elem != null) {
            for (JsonElement e : elem.getAsJsonArray()) {
                YoutFindVideoResponse.VideoItem video = new YoutFindVideoResponse.VideoItem();
                for(String renderer : contentRenderer) {
                    JsonElement videoRenderer = e.getAsJsonObject().get(renderer);
                    if (videoRenderer != null) {
                        System.out.println("---------->renderer:"+renderer);
                        parseVideo(video, videoRenderer);
                        parseChannel(video, videoRenderer);
                        parsePlaylist(video, videoRenderer);
                    }
                }
                ret.videoList.add(video);
            }
        }
        return ret;
    }

    private void parseVideo(YoutFindVideoResponse.VideoItem video, JsonElement videoRenderer) {
        video.videoId = parseJsonNodeToString(videoRenderer, new String[]{"videoId"});
        if (video.videoId == null) {
            return;
        }
        video.url = parseJsonNodeToString(videoRenderer, new String[]{"navigationEndpoint","commandMetadata","webCommandMetadata","url"});
        video.title = parseJsonNodeToString(videoRenderer, new String[]{"title","simpleText"});
        video.subTitle = parseJsonNodeToString(videoRenderer, new String[]{"shortViewCountText","simpleText"});
        video.length = parseJsonNodeToString(videoRenderer, new String[]{"lengthText","simpleText"});
        video.publishedTime = parseJsonNodeToString(videoRenderer, new String[]{"publishedTimeText","simpleText"});
        parseJsonNodeToArray(videoRenderer, new String[]{"thumbnail", "thumbnails"}, video.thumbnails);
    }

    private void parseChannel(YoutFindVideoResponse.VideoItem video, JsonElement videoRenderer) {
        video.channelId = parseJsonNodeToString(videoRenderer, new String[]{"channelId"});
        if (video.channelId == null) {
            return;
        }
        video.url = parseJsonNodeToString(videoRenderer, new String[]{"navigationEndpoint","commandMetadata","webCommandMetadata","url"});
        video.title = parseJsonNodeToString(videoRenderer, new String[]{"title","simpleText"});
        video.subTitle = parseJsonNodeToString(videoRenderer, new String[]{"subscriberCountText","simpleText"});
        video.length = parseJsonNodeToString(videoRenderer, new String[]{"videoCountText","simpleText"});
        parseJsonNodeToArray(videoRenderer, new String[]{"thumbnail", "thumbnails"}, video.thumbnails);
    }

    private void parsePlaylist(YoutFindVideoResponse.VideoItem video, JsonElement videoRenderer) {
        video.playlistId = parseJsonNodeToString(videoRenderer, new String[]{"playlistId"});
        if (video.playlistId == null) {
            return;
        }
        video.url = parseJsonNodeToString(videoRenderer, new String[]{"navigationEndpoint","commandMetadata","webCommandMetadata","url"});
        video.title = parseJsonNodeToString(videoRenderer, new String[]{"title","simpleText"});
        video.length = parseJsonNodeToString(videoRenderer, new String[]{"videoCountText","simpleText"});
        parseJsonNodeToArray(videoRenderer, new String[]{"thumbnail", "thumbnails"}, video.thumbnails);
    }

    private void parseJsonNodeToArray(JsonElement videoRenderer, Object[] pathNode, List<String> list) {
        JsonElement thumbnail = parseJsonNode(videoRenderer, pathNode);
        if (thumbnail != null && thumbnail.getAsJsonArray().size() > 0) {
            for(JsonElement t : thumbnail.getAsJsonArray()) {
                list.add(t.getAsJsonObject().get("url").getAsString());
                System.out.println("url:"+list.get(list.size()-1));
            }
        }
    }

    private String parseJsonNodeToString(JsonElement videoRenderer, Object[] pathNode) {
        JsonElement node = parseJsonNode(videoRenderer, pathNode);
        if (node != null && !node.getAsString().isEmpty()) {
            System.out.println(pathNode.toString()+":"+node.getAsString());
            return node.getAsString();
        }
        return null;
    }

    private JsonElement parseJsonNode(JsonElement root, Object[] pathNode) {
        if (root == null || pathNode == null || pathNode.length == 0) {
            return root;
        }
        JsonElement ret = root;
        for(Object node : pathNode) {
            if (node instanceof Integer) {
                ret = ret.getAsJsonArray().get((Integer) node);
            } else if (node instanceof String[]) {
                JsonElement r = ret;
                for(String n : (String[])node) {
                    ret = r.getAsJsonObject().get(n);
                    if (ret != null) {
                        break;
                    }
                }
            } else {
                ret = ret.getAsJsonObject().get(node.toString());
            }
            if (ret == null) {
                System.out.println("parseJsonNode node:'"+node+"' not found.");
                break;
            }
        }
        return ret;
    }

    private String parseRegexData(String body) {
        String ret = null;
        Pattern pattern = Pattern.compile("(window\\[\\\"ytInitialData\\\"\\]\\s*=\\s*)(.*)(;)");
        Matcher matcher = pattern.matcher(body);
        int groupIdx = 2;

        if (matcher.find() && matcher.groupCount() >= groupIdx) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                System.out.println(i + ":" + matcher.group(i));
            }
            ret = matcher.group(groupIdx);
        }
        return ret;
    }
}