package com.example.volunter.MainActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.volunter.R;
import com.example.volunter.activityBackground.ActivityBackground;
import com.example.volunter.db.User;
import com.example.volunter.griefGrocery.GriefGrocery;
import com.example.volunter.login.LoginActivity;
import com.example.volunter.personalMessage.ProfileActivity;
import com.example.volunter.volunteerCommunity.VolunteerCommunity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    //当前用户
    private User user;

    //圆形头像
    private CircleImageView circleImageView;
    private static final int TAKE_PHOTO = 0;
    private static final int SELECT_FROM_ABLUM = 1;
    private File file;
    private Uri imageUri;

    //侧拉菜单
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    //初始化主界面的两级导航菜单栏
    private FragmentTabHost fragmentTabHost;
    private LayoutInflater layoutInflater;
    private List<String> titleList;
    private List<Fragment> fragmentList;
    private int[] tabIcons = {
            R.drawable.activity_background,
            R.drawable.grief_grocery,
            R.drawable.volunteer_community
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initValue();
    }

    private void initView(){
        //侧拉菜单
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.menu);
        }
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        //这里是底部导航菜单栏
        layoutInflater = LayoutInflater.from(this);

        fragmentTabHost = (FragmentTabHost) findViewById(R.id.tabHost);
        fragmentTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        //初始化圆形头像
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //View headLayout = navigationView.inflateHeaderView(R.layout.nav_header);
        View headLayout = navigationView.getHeaderView(0);
        circleImageView = (CircleImageView) headLayout.findViewById(R.id.nav_icon);

        //初始化用户
        user = BmobUser.getCurrentUser(User.class);
    }

    private void initValue(){
        //侧拉菜单
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_profile:
                        Intent intentP = new Intent(MainActivity.this, ProfileActivity.class);
                        startActivity(intentP);
                        break;
                    case R.id.nav_call:
                        Toast.makeText(MainActivity.this, "Call", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_friends:
                        Toast.makeText(MainActivity.this, "Friends", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_location:
                        Toast.makeText(MainActivity.this, "Location", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_task:
                        Toast.makeText(MainActivity.this, "Task", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_cancel:
                        Bundle bundle = new Bundle();
                        bundle.putString("account", BmobUser.getCurrentUser().getUsername());
                        Intent intentC = new Intent(MainActivity.this, LoginActivity.class);
                        intentC.putExtras(bundle);
                        BmobUser.logOut();
                        startActivity(intentC);
                        finish();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        //设置初始项
//        navigationView.setCheckedItem(R.id.nav_call);
//        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                drawerLayout.closeDrawers();
//                return true;
//            }
//        });

        //底部导航菜单栏
        fragmentList = new ArrayList<>();
        fragmentList.add(new ActivityBackground());
        fragmentList.add(new GriefGrocery());
        fragmentList.add(new VolunteerCommunity());
        titleList = new ArrayList<>();
        titleList.add("活动广场");
        titleList.add("解忧杂货铺");
        titleList.add("志愿社区");
        for (int i = 0; i < fragmentList.size(); i++){
            TabHost.TabSpec tabSpec = fragmentTabHost.newTabSpec(titleList.get(i)).
                    setIndicator(getTabItemView(i));
            fragmentTabHost.addTab(tabSpec, fragmentList.get(i).getClass(), null);
        }

        //圆形头像
        final BmobFile bmobfile = user.getHead();
        file = new File(getCacheDir(), bmobfile.getFilename());
        new Thread(new Runnable() {
            @Override
            public void run() {
                bmobfile.download(file, new DownloadFileListener() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null){
                            try {
                                imageUri = Uri.fromFile(file);
                                Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                                circleImageView.setImageBitmap(bitmap);
                            }catch (FileNotFoundException ex){
                                ex.printStackTrace();
                            }
                        }
                    }
                    @Override
                    public void onProgress(Integer integer, long l) {

                    }
                });
            }
        }).start();

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] area = {"拍照", "从相册里面选择"};
                new AlertDialog.Builder(MainActivity.this)
                        .setItems(area, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case 0:
                                        takePhoto();
                                        break;
                                    case 1:
                                        selectFromAblum();
                                        break;
                                    default:
                                        break;
                                }
                            }
                        })
                        .show();
            }
        });
    }

    private View getTabItemView(int index){
        View view = layoutInflater.inflate(R.layout.tab_content, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.tab_imageView);
        imageView.setImageResource(tabIcons[index]);

        TextView textView = (TextView) view.findViewById(R.id.tab_textView);
        textView.setText(titleList.get(index));

        return view;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                //设定menu的点击事件为出现侧拉菜单
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.search_activity:
                Intent intentSearch = new Intent(this, SearchActivity.class);
                startActivity(intentSearch);
                break;
            case R.id.issue_activity:
                Intent intentIssue = new Intent(this, IssueActivity.class);
                startActivity(intentIssue);
                break;
            default:
                break;
        }
        return true;
    }

    //拍摄照片
    private void takePhoto(){
        //将文件放于应用关联的缓存目录下
        file = new File(getExternalCacheDir(), "head_image.jpg");
        try {
            if (file.exists()){
                file.delete();
            }
            file.createNewFile();
        }catch (IOException e){
            e.printStackTrace();
        }
        //不安全？？？？？
        imageUri = Uri.fromFile(file);

        //启动照相机
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PHOTO);
    }

    //从相册里面选取
    private void selectFromAblum(){
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_FROM_ABLUM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK){
                    try {
                        final Bitmap[] bitmap = {BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri))};
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                bitmap[0] = compressImage(bitmap[0]);
                                //存储图片
                                updateHead(bitmap[0]);
                            }
                        }).start();
                        circleImageView.setImageBitmap(bitmap[0]);
                    }catch (FileNotFoundException e){
                        e.printStackTrace();
                    }
                }
                break;
            case SELECT_FROM_ABLUM:
                if (resultCode == RESULT_OK){
                    try {
                        imageUri = data.getData();
                        final Bitmap[] bitmap = {BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri))};
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                bitmap[0] = compressImage(bitmap[0]);
                                //存储图片
                                updateHead(bitmap[0]);
                            }
                        }).start();
                        circleImageView.setImageBitmap(bitmap[0]);
                    }catch (FileNotFoundException e){
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }
    //压缩图片
    private Bitmap compressImage(Bitmap image){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 90;

        while (baos.toByteArray().length / 1024 > 800){
            baos.reset();
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
            options -= 10;
        }

        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);

        return bitmap;
    }

    private void updateHead(Bitmap bitmap){
        //先写在临时缓存里面,再上传更新
        FileOutputStream fileOutputStream = null;
        try {
            file = new File(getCacheDir(), user.getUsername() + "_head_image.jpg");
            fileOutputStream = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)){
                fileOutputStream.flush();
            }
        }catch (FileNotFoundException e){
            file.delete();
            e.printStackTrace();
        }catch (IOException e){
            file.delete();
            e.printStackTrace();
        }finally {
            try {
                fileOutputStream.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        
        //删除原来的头像
        BmobFile deleteFile = new BmobFile();
        deleteFile.setUrl(user.getHead().getUrl());
        deleteFile.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
            }
        });
        //更新头像
        final BmobFile bmobFile = new BmobFile(file);
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    user.setHead(bmobFile);
                    user.update(user.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null){
                                Toast.makeText(MainActivity.this, "头像更新成功", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(MainActivity.this, "头像更新失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(MainActivity.this, "头像上传失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

//    private String getImagePath(Uri uri){
////        String res = null;
////        String[] proj = { MediaStore.Images.Media.DATA };
////        Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
////        if(cursor.moveToFirst()){
////            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
////            res = cursor.getString(column_index);
////        }
////        cursor.close();
////        return res;
//        String path = null;
//        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
//        if (cursor != null){
//            if (cursor.moveToFirst()){
//                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
//            }
//            cursor.close();
//        }else{
//            path = uri.getPath();
//        }
//        return path;
//    }

//    private void handleImageOnKitKat(Intent data){
//        String imagePath = null;
//        Uri uri = data.getData();
//        if (DocumentsContract.isDocumentUri(this, uri)){
//            String docId = DocumentsContract.getDocumentId(uri);
//            if ("com.android.providers.media.documents".equals(uri.getAuthority())){
//                String id = docId.split(":")[1];
//                String selection = MediaStore.Images.Media._ID + "=" + id;
//                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
//            }else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())){
//                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
//                imagePath = getImagePath(contentUri, null);
//            }
//        }else if ("content".equalsIgnoreCase(uri.getScheme())){
//            imagePath = getImagePath(uri, null);
//        }else if ("file".equalsIgnoreCase(uri.getScheme())){
//            imagePath = uri.getPath();
//        }
//        displayImage(imagePath);
//    }
//
//    private void handleImageBeforeKitKat(Intent data){
//        Uri uri = data.getData();
//        String imagePath = getImagePath(uri, null);
//        displayImage(imagePath);
//    }
//
//    private String getImagePath(Uri uri, String selection){
//        String path = null;
//        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
//        if (cursor != null){
//            if (cursor.moveToFirst()){
//                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
//            }
//            cursor.close();
//        }
//        return  path;
//    }
//
//    private void displayImage(String imagePath){
//        if (imagePath != null){
//            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
//            circleImageView.setImageBitmap(bitmap);
//        }else{
//            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
//        }
//    }
}
