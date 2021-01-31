package com.example.flasktestver3;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;




import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.os.Looper.getMainLooper;

public class FileUploadUtils {

    static String[] result1 = {"", "", "", "", ""};   // result1[0]에는 라벨의 이름, 나머지 4개는 좌표 (x, y, w, h)
    static String[] result2 = {"", "", "", "", ""};
    static String[] result3 = {"", "", "", "", ""};
    static String[] result4 = {"", "", "", "", ""};
    static String[] result5 = {"", "", "", "", ""};
    static String[] result6 = {"", "", "", "", ""};
    static String[] result7 = {"", "", "", "", ""};
    static String[] result8 = {"", "", "", "", ""};
    static int NumOfClass = 0;  // 추출할 라벨&좌표의 개수 ( = 리사이클러뷰의 아이템개수 )



    public static void send2Server(File file){
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", file.getName(), RequestBody.create(MediaType.parse("image/*"), file))
                .build();

        Request request = new Request.Builder()
                .url("http://1.229.131.101:5000/detections")
                .addHeader("Content-Type", "multipart/form-data")
                .post(requestBody)
                .build();

        OkHttpClient client = new OkHttpClient();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("요청", "onFailure()");
                Log.e("error", String.valueOf(e));
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                //Log.e("onResponse", "Response Body is " + response.body().string());
                Log.e("onResponse()", "실행");

                new Handler(getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            JSONObject jsonObject = new JSONObject(response.body().string());
                            String response = jsonObject.getString("response");
                            JSONArray jsonArray1 = new JSONArray(response);
                            JSONObject jsonObject1 = jsonArray1.getJSONObject(0);
                            String detections = jsonObject1.getString("detections");
                            JSONArray jsonArray = new JSONArray(detections);

                            for (int i=0;i<jsonArray.length();i++){

                                NumOfClass = jsonArray.length();

                                JSONObject subJSONObject =jsonArray.getJSONObject(i);
                                String boxes = subJSONObject.getString("boxes");
                                String Class = subJSONObject.getString("class");

                                //Log.e("boxes", boxes);
                                //Log.e("class", Class);

                                // 배열로 만들기

                                StringBuffer boxesBuffer = new StringBuffer(boxes);
                                boxesBuffer.deleteCharAt(boxes.length()-1);
                                boxesBuffer.deleteCharAt(0);

                                String boxesString = boxesBuffer.toString();

                                Log.e("boxesString", boxesString);

                                String[] array = boxesString.split("\\s+");

                                switch (i){
                                    case 0:
                                        makeResultArray(result1, array, Class);
                                        break;
                                    case 1:
                                        makeResultArray(result2, array, Class);
                                        break;
                                    case 2:
                                        makeResultArray(result3, array, Class);
                                        break;
                                    case 3:
                                        makeResultArray(result4, array, Class);
                                        break;
                                    case 4:
                                        makeResultArray(result5, array, Class);
                                        break;
                                    case 5:
                                        makeResultArray(result6, array, Class);
                                        break;
                                    case 6:
                                        makeResultArray(result7, array, Class);
                                        break;
                                    case 7:
                                        makeResultArray(result8, array, Class);
                                        break;
                                    default:
                                        Log.e("탐지", "개인정보가 인식되지 않았습니다");
                                        break;

                                }

                            }

                            // result 1~8 배열 출력
                            for(int k=0; k<result1.length;k++)
                                Log.e("result1", result1[k]);

                            for(int k=0; k<result2.length;k++)
                                Log.e("result2", result2[k]);

                            for(int k=0; k<result3.length;k++)
                                Log.e("result3", result3[k]);

                            for(int k=0; k<result4.length;k++)
                                Log.e("result4", result4[k]);

                            for(int k=0; k<result5.length;k++)
                                Log.e("result5", result5[k]);

                            for(int k=0; k<result6.length;k++)
                                Log.e("result6", result6[k]);

                            for(int k=0; k<result7.length;k++)
                                Log.e("result7", result7[k]);

                            for(int k=0; k<result8.length;k++)
                                Log.e("result8", result8[k]);



                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    private void makeResultArray(String[] result, String[] array, String Class) {

                        result[0] = Class;
                        for(int j=0;j<4;j++) {
                            result[j+1]=array[j];
                            //Log.e("[" + j + "]" ,array[j]);
                        }

                    }

                });

            }
        });
    }
}

