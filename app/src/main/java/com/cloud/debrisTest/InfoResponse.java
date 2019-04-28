package com.cloud.debrisTest;


public class InfoResponse {
    private Info Result;

    public Info getResult() {
        if (Result == null) {
            return new Info();
        }
        return Result;
    }

    public void setResult(Info result) {
        Result = result;
    }
}
