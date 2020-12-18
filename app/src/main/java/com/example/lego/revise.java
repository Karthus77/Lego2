package com.example.lego;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
class  user_upload{
    String name;
    String info;
    Boolean sex;
}

public class revise extends AppCompatActivity {
    private static int sdkVersion=android.os.Build.VERSION.SDK_INT;
    private RelativeLayout head;
    private RelativeLayout name;
    private RelativeLayout sex;
    private RelativeLayout info;
    private TextView revise_name;
    private TextView revise_sex;
    private TextView revise_info;
    private ImageView back;
    private ImageView head_image;
    String myName ;
    boolean mySex;
    String myInfo;
    String image_hash;
    private Uri photoUri;   //相机拍照返回图片路径
    private File outputImage;
    private static final int GET_BACKGROUND_FROM_CAPTURE_RESOULT = 1;
    private static final int RESULT_REQUEST_CODE = 2;
    private static final int TAKE_PHOTO = 3;



    private void selectCamera() {

        //创建file对象，用于存储拍照后的图片，这也是拍照成功后的照片路径
        outputImage = new File(this.getExternalCacheDir(), "camera_photos.jpg");
        try {
            //判断文件是否存在，存在删除，不存在创建
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        photoUri = Uri.fromFile(outputImage);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(intent, TAKE_PHOTO);

    }
    public static final String STR_IMAGE = "image/*";
    //选择相册
    private void selectPhoto(){
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, STR_IMAGE);
        startActivityForResult(intent, GET_BACKGROUND_FROM_CAPTURE_RESOULT);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;

        switch (requestCode) {

            case RESULT_REQUEST_CODE:   //相册返回
                final SharedPreferences sharedPreferences=getSharedPreferences("user",MODE_PRIVATE);
                final String token =sharedPreferences.getString("token",null);
                final String selectPhoto = getRealPathFromUri(this,cropImgUri);
                //创建file文件，用于存储剪裁后的照片
                Thread thread1 =new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
//                            OkHttpClient httpClient = new OkHttpClient();
//                            MediaType mediaType = MediaType.parse("application/octet-stream");//设置类型，类型为八位字节流
//                            RequestBody requestBody = RequestBody.create(mediaType, );//把文件与类型放入请求体
//
//                            MultipartBody multipartBody = new MultipartBody.Builder()
//                                    .setType(MultipartBody.FORM)
//                                    .addFormDataPart("image", file.getName(), requestBody)//文件名,请求体里的文件
//                                    .build();
//                            Request request = new Request.Builder()
//                                    .url(UpUser_Img)
//                                    .post(multipartBody)
//                                    .build();
                            OkHttpClient httpClient = new OkHttpClient().newBuilder().build();
                           RequestBody body=new MultipartBody.Builder().setType(MultipartBody.FORM)
                                   .addFormDataPart("img",selectPhoto,RequestBody.create(MediaType.parse("application/octet-stream"),
                                           new File(selectPhoto)))
                                   .build();
                            Request request = new Request.Builder()
                                    .addHeader("Authorization",token)
                                    .url("http://49.232.214.94/api/upload/head")
                                    .addHeader("Accept","application/json")
                                    .post(body)
                                    .build();
                            Response response = null;
                            response = httpClient.newCall(request).execute();
                            String responseData = response.body().string();
                            Log.v("tag",responseData);
                            Gson gson=new Gson();
                            user user =gson.fromJson(responseData, com.example.lego.user.class);
                            String msg=user.getMsg();
                            final SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
                            String token = sharedPreferences.getString("token", null);
                            OkHttpClient client = new OkHttpClient();
                            Gson gson1 = new Gson();
                            Request request1 = new Request.Builder()
                                    .addHeader("Authorization", token)
                                    .url("http://49.232.214.94/api/user")
                                    .build();
                            Response response1 = client.newCall(request1).execute();
                            String responseData1 = response1.body().string();
                            information information = gson1.fromJson(responseData1, com.example.lego.information.class);
                            com.example.lego.information.DataBean data = information.getData();
                            com.example.lego.information.DataBean.UserBean userData = data.getUser();
                            image_hash=userData.getHead();
                            myName = userData.getName();
                            mySex = userData.isSex();
                            myInfo = userData.getInfo();
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("head",image_hash);
                            editor.putString("name", myName);
                            editor.putBoolean("sex", mySex);
                            editor.putString("info", myInfo);
                            editor.commit();
                            Log.v("tag",image_hash);
                            Looper.prepare();
                            Toast.makeText(revise.this,msg,Toast.LENGTH_SHORT).show();
                            Looper.loop();



                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread1.start();

                break;

            case TAKE_PHOTO://   拍照返回
                cropRawPhoto(photoUri);

                break;
            case GET_BACKGROUND_FROM_CAPTURE_RESOULT:
                photoUri = data.getData();
                cropRawPhoto(photoUri);


        }





    }
    private Uri cropImgUri;
    public void cropRawPhoto(Uri uri) {
        //创建file文件，用于存储剪裁后的照片
        File cropImage = new File(Environment.getExternalStorageDirectory(), "crop_image.jpg");
        String path = cropImage.getAbsolutePath();
        try {
            if (cropImage.exists()) {
                cropImage.delete();
            }
            cropImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        cropImgUri = Uri.fromFile(cropImage);
        Intent intent = new Intent("com.android.camera.action.CROP");
//设置源地址uri
        intent.setDataAndType(photoUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("scale", true);
//设置目的地址uri
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cropImgUri);
//设置图片格式
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("return-data", false);
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, RESULT_REQUEST_CODE);
    }
    public static String getRealPathFromUri(Context context, Uri uri) {
        if(context == null || uri == null) {
            return null;
        }
        if("file".equalsIgnoreCase(uri.getScheme())) {
            return getRealPathFromUri_Byfile(context,uri);
        } else if("content".equalsIgnoreCase(uri.getScheme())) {
            return getRealPathFromUri_Api11To18(context,uri);
        }
//        int sdkVersion = Build.VERSION.SDK_INT;
//        if (sdkVersion < 11) {
//            // SDK < Api11
//            return getRealPathFromUri_BelowApi11(context, uri);
//        }

//        // SDK > 19
        return getRealPathFromUri_AboveApi19(context, uri);//没用到
    }
    private static String getRealPathFromUri_Byfile(Context context,Uri uri){
        String uri2Str = uri.toString();
        String filePath = uri2Str.substring(uri2Str.indexOf(":") + 3);
        return filePath;
    }
    @SuppressLint("NewApi")
    private static String getRealPathFromUri_AboveApi19(Context context, Uri uri) {
        String filePath = null;
        String wholeID = null;

        wholeID = DocumentsContract.getDocumentId(uri);

        // 使用':'分割
        String id = wholeID.split(":")[1];

        String[] projection = { MediaStore.Images.Media.DATA };
        String selection = MediaStore.Images.Media._ID + "=?";
        String[] selectionArgs = { id };

        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
                selection, selectionArgs, null);
        int columnIndex = cursor.getColumnIndex(projection[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }

    /**
     * //适配api11-api18,根据uri获取图片的绝对路径。
     * 针对图片URI格式为Uri:: content://media/external/images/media/1028
     */
    private static String getRealPathFromUri_Api11To18(Context context, Uri uri) {
        String filePath = null;
        String[] projection = { MediaStore.Images.Media.DATA };

        CursorLoader loader = new CursorLoader(context, uri, projection, null,
                null, null);
        Cursor cursor = loader.loadInBackground();

        if (cursor != null) {
            cursor.moveToFirst();
            filePath = cursor.getString(cursor.getColumnIndex(projection[0]));
            cursor.close();
        }
        return filePath;
    }

    /**
     * 适配api11以下(不包括api11),根据uri获取图片的绝对路径
     */
    private static String getRealPathFromUri_BelowApi11(Context context, Uri uri) {
        String filePath = null;
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(uri, projection,
                null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            filePath = cursor.getString(cursor.getColumnIndex(projection[0]));
            cursor.close();
        }
        return filePath;
    }







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
        if (Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_revise);
        final SharedPreferences sharedPreferences=getSharedPreferences("user",MODE_PRIVATE);
         revise_name=findViewById(R.id.revise_name);
         revise_sex=findViewById(R.id.revise_sex);
         revise_info=findViewById(R.id.revise_info);
         head_image=findViewById(R.id.head_image);
        head=findViewById(R.id.change_head);
        name=findViewById(R.id.change_name);
        sex=findViewById(R.id.change_sex);
        info=findViewById(R.id.change_info);
        back=findViewById(R.id.revise_back);
        Thread thread =new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient okHttpClient =new OkHttpClient();
                    String token =sharedPreferences.getString("token",null);
                    Gson gson=new Gson();
                    Request request = new Request.Builder()
                            .addHeader("Authorization",token)
                            .url("http://49.232.214.94/api/user")
                            .build();
                    Response response = okHttpClient.newCall(request).execute();
                    String responseData = response.body().string();
                    information information =gson.fromJson(responseData, com.example.lego.information.class);
                    com.example.lego.information.DataBean data=information.getData();
                    com.example.lego.information.DataBean.UserBean userData=data.getUser();
                    image_hash=userData.getHead();
                    myName=userData.getName();
                    mySex=userData.isSex();
                    myInfo=userData.getInfo();
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("head",image_hash);
                    editor.putString("name",myName);
                    editor.putBoolean("sex",mySex);
                    editor.putString("info",myInfo);
                    editor.commit();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String sex[]=new String[]{"        相机","        相册"};
                AlertDialog.Builder builder1 =new AlertDialog.Builder(revise.this);
                builder1.setIcon(R.drawable.ic_launcher_foreground);
                builder1.setTitle("选择上传方式:");
                builder1.setItems(sex, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which==0)
                        {
                            selectCamera();
                            final String head=sharedPreferences.getString("head",null);
                            final Thread thread =new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try{
                                        do {
                                            Thread.sleep(800);
                                            final SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
                                            String token = sharedPreferences.getString("token", null);
                                            OkHttpClient client = new OkHttpClient();
                                            Gson gson = new Gson();
                                            Request request = new Request.Builder()
                                                    .addHeader("Authorization", token)
                                                    .url("http://49.232.214.94/api/user")
                                                    .build();
                                            Response response = client.newCall(request).execute();
                                            String responseData = response.body().string();
                                            information information = gson.fromJson(responseData, com.example.lego.information.class);
                                            com.example.lego.information.DataBean data = information.getData();
                                            com.example.lego.information.DataBean.UserBean userData = data.getUser();
                                            image_hash = userData.getHead();
                                            Log.v("tag", image_hash + 666);
                                        }
                                        while (image_hash.equals(head));
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Glide.with(revise.this).load("http://49.232.214.94/api/img/"+image_hash).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(head_image);

                                            }
                                        });
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            thread.start();


                        }
                        else
                        {
                                    selectPhoto();
                                    final String head=sharedPreferences.getString("head",null);
                            final Thread thread =new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try{
                                        do {
                                            Thread.sleep(800);
                                            final SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
                                            String token = sharedPreferences.getString("token", null);
                                            OkHttpClient client = new OkHttpClient();
                                            Gson gson = new Gson();
                                            Request request = new Request.Builder()
                                                    .addHeader("Authorization", token)
                                                    .url("http://49.232.214.94/api/user")
                                                    .build();
                                            Response response = client.newCall(request).execute();
                                            String responseData = response.body().string();
                                            information information = gson.fromJson(responseData, com.example.lego.information.class);
                                            com.example.lego.information.DataBean data = information.getData();
                                            com.example.lego.information.DataBean.UserBean userData = data.getUser();
                                            image_hash = userData.getHead();
                                            Log.v("tag", image_hash + 666);
                                        }
                                        while (image_hash.equals(head));
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Glide.with(revise.this).load("http://49.232.214.94/api/img/"+image_hash).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(head_image);

                                            }
                                        });
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            thread.start();

                        }
                    }
                });
                builder1.create().show();
            }

        });
        sex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String sex[]=new String[]{"        男","        女"};
                AlertDialog.Builder builder =new AlertDialog.Builder(revise.this);
                builder.setIcon(R.drawable.ic_launcher_foreground);
                builder.setTitle("选择你的性别:");
                builder.setItems(sex, new DialogInterface.OnClickListener() {    //设置监听
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final SharedPreferences sharedPreferences=getSharedPreferences("user",MODE_PRIVATE);
                        final SharedPreferences.Editor editor=sharedPreferences.edit();
                        if(which==0)
                        {
                            Thread thread1 =new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try{
                                        OkHttpClient okHttpClient =new OkHttpClient();
                                        String token =sharedPreferences.getString("token",null);
                                        Gson gson=new Gson();
                                        user_upload user_upload =new user_upload();
                                        user_upload.name=myName;
                                        user_upload.info=myInfo;
                                        user_upload.sex=true;
                                        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                                        RequestBody requestBody =RequestBody.create(JSON,gson.toJson(user_upload));
                                        Request request = new Request.Builder()
                                                .addHeader("Authorization",token)
                                                .url("http://49.232.214.94/api/user")
                                                .put(requestBody)
                                                .build();
                                        Response response = okHttpClient.newCall(request).execute();
                                        editor.putBoolean("sex",true);
                                        editor.commit();
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                revise_sex.setText("男");
                                            }
                                        });

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            thread1.start();
                        }
                        else
                        {
                            Thread thread1 =new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try{
                                        OkHttpClient okHttpClient =new OkHttpClient();
                                        String token =sharedPreferences.getString("token",null);
                                        Gson gson=new Gson();
                                        user_upload user_upload =new user_upload();
                                        user_upload.name=myName;
                                        user_upload.info=myInfo;
                                        user_upload.sex=false;
                                        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                                        RequestBody requestBody =RequestBody.create(JSON,gson.toJson(user_upload));
                                        Request request = new Request.Builder()
                                                .addHeader("Authorization",token)
                                                .url("http://49.232.214.94/api/user")
                                                .put(requestBody)
                                                .build();
                                        Response response = okHttpClient.newCall(request).execute();
                                        editor.putBoolean("sex",false);
                                        editor.commit();
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                revise_sex.setText("女");
                                            }
                                        });


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            thread1.start();
                        }
                    }
                });
                builder.create().show();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(revise.this,nameresive.class);
                startActivity(intent);
            }
        });
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(revise.this,info_revise.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
                    String token = sharedPreferences.getString("token", null);
                    OkHttpClient client = new OkHttpClient();
                    Gson gson = new Gson();
                    Request request = new Request.Builder()
                            .addHeader("Authorization", token)
                            .url("http://49.232.214.94/api/user")
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    information information = gson.fromJson(responseData, com.example.lego.information.class);
                    com.example.lego.information.DataBean data = information.getData();
                    com.example.lego.information.DataBean.UserBean userData = data.getUser();
                    image_hash=userData.getHead();
                    Log.v("tag",image_hash);
                    myName = userData.getName();
                    mySex = userData.isSex();
                    myInfo = userData.getInfo();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("head",image_hash);
                    editor.putString("name", myName);
                    editor.putBoolean("sex", mySex);
                    editor.putString("info", myInfo);
                    editor.commit();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            revise_name.setText(myName);
                            revise_info.setText(myInfo);
                             if(image_hash == null || image_hash.length() == 0)
                             {
                             }
                             else
                             {
                                 Glide.with(revise.this).load("http://49.232.214.94/api/img/"+image_hash).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(head_image);
                             }
                            if (mySex) {
                                revise_sex.setText("男");
                            } else {
                                revise_sex.setText("女");
                            }
                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();


    }
}