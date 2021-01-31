package com.example.flasktestver3;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.flasktestver3.FileUploadUtils.NumOfClass;
import static com.example.flasktestver3.FileUploadUtils.result1;
import static com.example.flasktestver3.FileUploadUtils.result2;
import static com.example.flasktestver3.FileUploadUtils.result3;
import static com.example.flasktestver3.FileUploadUtils.result4;
import static com.example.flasktestver3.FileUploadUtils.result5;
import static com.example.flasktestver3.FileUploadUtils.result6;
import static com.example.flasktestver3.FileUploadUtils.result7;
import static com.example.flasktestver3.FileUploadUtils.result8;


public class MainActivity extends AppCompatActivity {

    ImageView imgVwSelected;
    Button btnImageSend, btnImageSelection;
    File tempSelectFile;
    String filePath = "";
    TextView tv_result;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnImageSend = findViewById(R.id.btnImageSend);
        tv_result = findViewById(R.id.tv_result);

        btnImageSend.setEnabled(false);

        btnImageSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FileUploadUtils.send2Server(tempSelectFile);

                Handler mHandler = new Handler();
                mHandler.postDelayed(new Runnable()  {
                    public void run() {
                        // 시간 지난 후 실행할 코딩
                        String result="";

                        result += "인식된 라벨 개수 : " + NumOfClass + "\n";

                        result+="result1 : ";

                        for(int i=0;i<5;i++){
                            result += result1[i] + " ";
                        }

                        result+="\nresult2 : ";

                        for(int i=0;i<5;i++){
                            result += result2[i] + " ";
                        }

                        result+="\nresult3 : ";

                        for(int i=0;i<5;i++){
                            result += result3[i] + " ";
                        }

                        result+="\nresult4 : ";

                        for(int i=0;i<5;i++){
                            result += result4[i] + " ";
                        }

                        result+="\nresult5 : ";

                        for(int i=0;i<5;i++){
                            result += result5[i] + " ";
                        }

                        result+="\nresult6 : ";

                        for(int i=0;i<5;i++){
                            result += result6[i] + " ";
                        }

                        result+="\nresult7 : ";

                        for(int i=0;i<5;i++){
                            result += result7[i] + " ";
                        }

                        result+="\nresult8 : ";

                        for(int i=0;i<5;i++){
                            result += result8[i] + " ";
                        }


                        tv_result.setText(result);
                    }
                }, 1000);



            }
        });

        btnImageSelection = findViewById(R.id.btnImageSelection);
        btnImageSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
            }
        });

        imgVwSelected = findViewById(R.id.imgVwSelected);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != 1 || resultCode != RESULT_OK) {
            return;
        }

        Uri dataUri = data.getData();
        imgVwSelected.setImageURI(dataUri);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1001);
            }
        }


        /*try {

            InputStream in = getContentResolver().openInputStream(dataUri);
            Bitmap image = BitmapFactory.decodeStream(in);
            imgVwSelected.setImageBitmap(image);
            in.close();

            String date = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss").format(new Date());

            tempSelectFile = new File(Environment.getExternalStorageDirectory() + "/Pictures", "temp_" + date + ".jpeg");
            OutputStream out = new FileOutputStream(tempSelectFile);
            image.compress(Bitmap.CompressFormat.JPEG, 100, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        btnImageSend.setEnabled(true);*/


        Bitmap imgBitmap = null;

        try {
            imgBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), dataUri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Bitmap resized = Bitmap.createScaledBitmap(imgBitmap,(int)(imgBitmap.getWidth()*0.7), (int)(imgBitmap.getHeight()*0.7), true);

        filePath = getRealPathFromURI(getImageUri(getApplicationContext(), resized));
        //filePath = getRealPathFromURI(uri);

        tempSelectFile = new File(filePath);

        btnImageSend.setEnabled(true);

    }

    private String getRealPathFromURI(Uri contentURI) {

        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);

        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }

        return result;

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

}