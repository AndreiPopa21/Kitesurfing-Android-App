package com.example.stefanpopa.kitesurfingandroidproject.api_spot_favorites_add_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Favorites_Add_Result {

    @SerializedName("result")
    @Expose
    private String result;


    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public SentBodyParameters getSentBodyParameters() {
        return sentBodyParameters;
    }

    public void setSentBodyParameters(SentBodyParameters sentBodyParameters) {
        this.sentBodyParameters = sentBodyParameters;
    }

    public SentHeaders getSentHeaders() {
        return sentHeaders;
    }

    public void setSentHeaders(SentHeaders sentHeaders) {
        this.sentHeaders = sentHeaders;
    }

    @SerializedName("error")
    @Expose
    private Error error;

    @SerializedName("sentBodyParameters")
    @Expose
    private SentBodyParameters sentBodyParameters;

    @SerializedName("sentHeaders")
    @Expose
    private SentHeaders sentHeaders;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public class Error{
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("code")
        @Expose
        private String code;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }
    public class SentBodyParameters{
        @SerializedName("spotId")
        @Expose
        private String spotId;

        public String getSpotId() {
            return spotId;
        }

        public void setSpotId(String spotId) {
            this.spotId = spotId;
        }
    }

    public class SentHeaders{
        @SerializedName("content-type")
        @Expose
        private String content_type;
        @SerializedName("token")
        @Expose
        private String token;

        public String getContent_type() {
            return content_type;
        }

        public void setContent_type(String content_type) {
            this.content_type = content_type;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
