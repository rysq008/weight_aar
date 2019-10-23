package com.innovationai.pigweight.net.bean;

import java.io.Serializable;

/**
 * @Author: Lucas.Cui
 * 时   间：2019/5/23
 * 简   述：模型识别bean
 */
public class RecognitionBean implements Serializable {

    private static final long serialVersionUID = 5243381200486933728L;

    /**
     * status : 1
     * errorcode : 1
     * recognitionResult  : ee
     */

    private int status;
    private int errorcode;
    private RecognitionResultBean recognitionResult;


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(int errorcode) {
        this.errorcode = errorcode;
    }

    public RecognitionResultBean getRecognitionResult() {
        return recognitionResult;
    }

    public void setRecognitionResult(RecognitionResultBean recognitionResult) {
        this.recognitionResult = recognitionResult;
    }

    @Override
    public String toString() {
        return "RecognitionBean{" +
                "status=" + status +
                ", errorcode=" + errorcode +
                ", recognitionResult=" + recognitionResult +
                '}';
    }

    /**
     * measureArea : 1930.6856293123124
     * weight : 37.555484771728516
     * bodyLength : 88.09072600477207
     */
    public static class RecognitionResultBean implements Serializable {

        private static final long serialVersionUID = 6909975026582713760L;
        private String measureArea;
        private String weight;
        private String bodyLength;//耳根到尾长度
        private String wlength;//全长

        public String getWlength() {
            return wlength;
        }

        public void setWlength(String wlength) {
            this.wlength = wlength;
        }

        public String getMeasureArea() {
            return measureArea;
        }

        public void setMeasureArea(String measureArea) {
            this.measureArea = measureArea;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public String getBodyLength() {
            return bodyLength;
        }

        public void setBodyLength(String bodyLength) {
            this.bodyLength = bodyLength;
        }

        @Override
        public String toString() {
            return "RecognitionResultBean{" +
                    "measureArea='" + measureArea + '\'' +
                    ", weight='" + weight + '\'' +
                    ", bodyLength='" + bodyLength + '\'' +
                    '}';
        }
    }
}
