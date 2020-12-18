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
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
class good_upload{
    String name;
    String info;
    int good_id;
    int price;
    int quantity;
    String img;
}

public class Upload_good extends AppCompatActivity {
    private String hash;
    Button back;
    RelativeLayout upload_image;
    private ImageView img;
    private EditText upload_name;
    private EditText upload_price;
    EditText upload_quantity;
    EditText upload_info;
    Button upload;
    private String image_hash;
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
                                    .url("http://49.232.214.94/api/upload/good")
                                    .addHeader("Accept","application/json")
                                    .post(body)
                                    .build();
                            Response response = null;
                            response = httpClient.newCall(request).execute();
                            String responseData = response.body().string();
                            Gson gson =new Gson();
                            good_img goodImg=gson.fromJson(responseData,good_img.class);
                            good_img.DataBean dataBean=goodImg.getData();
                            String hash=dataBean.getHash();
                            Log.v("tag",hash);
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.putString("upload","http://49.232.214.94/api/img/"+hash);
                            editor.commit();



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
        setContentView(R.layout.activity_upload_good);
        upload_image=findViewById(R.id.upload_img);
        upload_name=findViewById(R.id.uploadGet_name);
        upload_price=findViewById(R.id.uploadGet_price);
        upload_quantity=findViewById(R.id.uploadGet_number);
        upload_info=findViewById(R.id.uploadGet_info);
        upload=findViewById(R.id.upload);
        img=findViewById(R.id.image);
        back=findViewById(R.id.upload_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        final SharedPreferences sharedPreferences=getSharedPreferences("user",MODE_PRIVATE);
        Intent it2=getIntent();
        final Bundle bd=it2.getExtras();
        final String id=bd.getString("id");

        Thread thread =new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient okHttpClient =new OkHttpClient();
                    Gson gson =new Gson();
                    Request request = new Request.Builder()
                            .url("http://49.232.214.94/api/goods/"+id)
                            .build();
                    Response response = okHttpClient.newCall(request).execute();
                    String responseData = response.body().string();
                    final detail detail =gson.fromJson(responseData, com.example.lego.detail.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            com.example.lego.detail.DataBean dataBean = detail.getData();
                            com.example.lego.detail.DataBean.GoodBean goodBean=dataBean.getGood();
                            String good_name=goodBean.getName();
                            int good_price=goodBean.getPrice();
                            String good_info =goodBean.getInfo();
                            String good_url =goodBean.getImg();
                            int good_quantity=goodBean.getQuantity();
                            upload_name.setText(good_name);
                            upload_price.setText(Integer.valueOf(good_price).toString());
                            upload_info.setText(good_info);
                            upload_quantity.setText(Integer.valueOf(good_quantity).toString());
                            Log.v("tag",good_url);
                            Glide.with(Upload_good.this).load(good_url).into(img);
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.putString("upload",good_url);
                            editor.commit();
                        }
                    });

                } catch (Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Upload_good.this,"网络连接已断开",Toast.LENGTH_SHORT).show();
                        }
                    });
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        upload_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String sex[]=new String[]{"        相册","        取消"};
                AlertDialog.Builder builder1 =new AlertDialog.Builder(Upload_good.this);
                builder1.setIcon(R.drawable.ic_launcher_foreground);
                builder1.setTitle("选择:");
                builder1.setItems(sex, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which==1)
                        {



                        }
                        else
                        {
                            selectPhoto();
                            final String hash=sharedPreferences.getString("upload",null);
                            final Thread thread =new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try{
                                        do {
                                            Thread.sleep(1000);
                                            final SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
                                            image_hash =sharedPreferences.getString("upload",null);
                                            Log.v("tag",image_hash);
                                        }
                                        while (image_hash.equals(hash));
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Glide.with(Upload_good.this).load(image_hash).into(img);

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
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name=upload_name.getText().toString();
                final String info=upload_info.getText().toString();
                String jiage=upload_price.getText().toString();
                String shuliang=upload_quantity.getText().toString();

                if(name==null || name.length()==0)
                {
                    Toast.makeText(Upload_good.this,"商品未命名",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(jiage==null ||jiage.length()==0)
                {
                    Toast.makeText(Upload_good.this,"价格不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(shuliang==null ||shuliang.length()==0)
                {
                    Toast.makeText(Upload_good.this,"数量不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(Integer.parseInt(shuliang)>99)
                {
                    Toast.makeText(Upload_good.this,"商品数量不能超过99",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (Integer.parseInt(jiage)>10000000)
                {
                    Toast.makeText(Upload_good.this,"商品价格不合理",Toast.LENGTH_SHORT).show();
                    return;
                }
                else {

                Thread thread1 =new Thread(new Runnable() {
                    @Override
                    public void run() {
                    try {
                        final int price=Integer.parseInt(upload_price.getText().toString());
                        final int quantity =Integer.parseInt(upload_quantity.getText().toString());
                        OkHttpClient client = new OkHttpClient();
                        Gson gson = new Gson();
                        good_upload good=new good_upload();
                        good.name=name;
                        good.good_id=Integer.parseInt(id);
                        good.info=info;
                        good.price=price;
                        good.quantity=quantity;
                        good.img=sharedPreferences.getString("upload",null);
                        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                        String token=sharedPreferences.getString("token",null);
                        RequestBody requestBody =RequestBody.create(JSON,gson.toJson(good));
                        Request request = new Request.Builder()
                                .addHeader("Authorization",token)
                                .url("http://49.232.214.94/api/good")
                                .put(requestBody)
                                .build();
                        Response response = null;
                        response = client.newCall(request).execute();
                        String responseData = response.body().string();
                        good_creat good_creat=gson.fromJson(responseData, com.example.lego.good_creat.class);
                        String msg=good_creat.getMsg();
                        Looper.prepare();
                        Toast.makeText(Upload_good.this,msg,Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    } catch (Exception e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Upload_good.this,"网络连接已断开",Toast.LENGTH_SHORT).show();
                            }
                        });
                        e.printStackTrace();
                    }
                    }
                });
                thread1.start();}
            }
        });
    }
}