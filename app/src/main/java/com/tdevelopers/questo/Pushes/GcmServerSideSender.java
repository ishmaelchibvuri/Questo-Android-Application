/*
Copyright 2015 Google Inc. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package com.tdevelopers.questo.Pushes;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import static com.tdevelopers.questo.Pushes.HttpRequest.CONTENT_TYPE_JSON;
import static com.tdevelopers.questo.Pushes.HttpRequest.HEADER_AUTHORIZATION;
import static com.tdevelopers.questo.Pushes.HttpRequest.HEADER_CONTENT_TYPE;


/**
 * This class is used to send GCM downstream messages in the same way a server would.
 */
public class GcmServerSideSender {

    private static final String GCM_SEND_ENDPOINT = "https://gcm-http.googleapis.com/gcm/send";


    private static final String PARAM_TO = "to";
    private static final String PARAM_COLLAPSE_KEY = "collapse_key";
    private static final String PARAM_DELAY_WHILE_IDLE = "delay_while_idle";
    private static final String PARAM_TIME_TO_LIVE = "time_to_live";
    private static final String PARAM_DRY_RUN = "dry_run";
    private static final String PARAM_RESTRICTED_PACKAGE_NAME = "com.tdevelopers.questo";


    private static final String PARAM_JSON_PAYLOAD = "data";
    private static final String PARAM_JSON_NOTIFICATION_PARAMS = "notification";

    private final String key;
    private final LoggingService.Logger logger;


    public GcmServerSideSender(String key, LoggingService.Logger logger) {
        this.key = key;
        this.logger = logger;
    }


    public String sendHttpJsonDownstreamMessage(String destination,
                                                Message message) throws IOException {

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put(PARAM_TO, destination);
            jsonBody.putOpt(PARAM_COLLAPSE_KEY, message.getCollapseKey());
            jsonBody.putOpt(PARAM_RESTRICTED_PACKAGE_NAME, message.getRestrictedPackageName());
            jsonBody.putOpt(PARAM_TIME_TO_LIVE, message.getTimeToLive());
            jsonBody.putOpt(PARAM_DELAY_WHILE_IDLE, message.isDelayWhileIdle());
            jsonBody.putOpt(PARAM_DRY_RUN, message.isDryRun());
            if (message.getData().size() > 0) {
                JSONObject jsonPayload = new JSONObject(message.getData());
                jsonBody.put(PARAM_JSON_PAYLOAD, jsonPayload);
            }
            if (message.getNotificationParams().size() > 0) {
                JSONObject jsonNotificationParams = new JSONObject(message.getNotificationParams());
                jsonBody.put(PARAM_JSON_NOTIFICATION_PARAMS, jsonNotificationParams);
            }
        } catch (JSONException e) {
            logger.log(Log.ERROR, "Failed to build JSON body");
            throw new IOException("Failed to build JSON body");
        }

        HttpRequest httpRequest = new HttpRequest();
        httpRequest.setHeader(HEADER_CONTENT_TYPE, CONTENT_TYPE_JSON);
        httpRequest.setHeader(HEADER_AUTHORIZATION, "key=" + key);
        httpRequest.doPost(GCM_SEND_ENDPOINT, jsonBody.toString());

        if (httpRequest.getResponseCode() != 200) {
            throw new IOException("Invalid request."
                    + " status: " + httpRequest.getResponseCode()
                    + " response: " + httpRequest.getResponseBody());
        }

        JSONObject jsonResponse;
        try {
            jsonResponse = new JSONObject(httpRequest.getResponseBody());
            logger.log(Log.INFO, "Send message:\n" + jsonResponse.toString(2));
        } catch (JSONException e) {
            logger.log(Log.ERROR, "Failed to parse server response:\n"
                    + httpRequest.getResponseBody());
        }
        return httpRequest.getResponseBody();
    }

}