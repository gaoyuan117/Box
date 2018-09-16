package com.xzwzz.orange.ui;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.xzwzz.orange.AppConfig;
import com.xzwzz.orange.AppContext;
import com.xzwzz.orange.R;
import com.xzwzz.orange.api.http.BaseListObserver;
import com.xzwzz.orange.api.http.RetrofitClient;
import com.xzwzz.orange.api.http.RxUtils;
import com.xzwzz.orange.base.BaseActivity;
import com.xzwzz.orange.bean.BaseBean;
import com.xzwzz.orange.bean.UpdatePhotoBean;
import com.xzwzz.orange.bean.UserBean;
import com.xzwzz.orange.glide.GlideApp;
import com.xzwzz.orange.glide.GlideCircleTransform;
import com.xzwzz.orange.utils.DialogHelp;
import com.xzwzz.orange.utils.ImageUtils;
import com.xzwzz.orange.utils.LoginUtils;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UserInfoDetailActivity extends BaseActivity implements View.OnClickListener {

    private com.xzwzz.orange.widget.LineControlView mCvAvatar;
    private com.xzwzz.orange.widget.LineControlView mCvUsername;
    private com.xzwzz.orange.widget.LineControlView mCvSignature;
    private com.xzwzz.orange.widget.LineControlView mCvBirthday;
    private com.xzwzz.orange.widget.LineControlView mCvSex;
    private android.widget.Button mBtnExitLogin;
    private static final int BIRTHDAY = 2, USERNAME = 0, SIGNATURE = 1, SEX = 3;

    @Override
    protected boolean hasActionBar() {
        return true;
    }

    @Override
    protected Object getIdOrView() {
        return R.layout.activity_user_info_detail;
    }

    @Override
    protected void initView() {
        setToolbar("个人资料", true);
        mCvAvatar = findViewById(R.id.cv_avatar);
        mCvUsername = findViewById(R.id.cv_username);
        mCvSignature = findViewById(R.id.cv_signature);
        mCvBirthday = findViewById(R.id.cv_birthday);
        mCvSex = findViewById(R.id.cv_sex);
        mBtnExitLogin = findViewById(R.id.btn_exitLogin);
    }

    @Override
    protected void initData() {
        super.initData();
        UserBean loginUser = AppContext.getInstance().getLoginUser();
        mCvUsername.setContent(loginUser.user_nicename);
        if (loginUser.signature.isEmpty()) {
            mCvSignature.setContent("暂无签名");
        } else {
            mCvSignature.setContent(loginUser.signature);
        }

        GlideApp.with(this).load(loginUser.avatar)
                .transform(new GlideCircleTransform(mContext))
                .transition(new DrawableTransitionOptions().crossFade())
                .into(mCvAvatar.getContentIcon());

        mCvBirthday.setContent(loginUser.birthday);
        if ("1".equals(loginUser.sex)) {
            mCvSex.setContent("男");
        } else if ("2".equals(loginUser.sex)) {
            mCvSex.setContent("女");
        }

    }

    @Override
    protected void setListener() {
        mCvAvatar.setOnClickListener(this);
        mCvUsername.setOnClickListener(this);
        mCvSignature.setOnClickListener(this);
        mCvBirthday.setOnClickListener(this);
        mCvSex.setOnClickListener(this);
        mBtnExitLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cv_avatar:
                DialogHelp.getSelectDialog(this, new String[]{"从相机", "从本地"}, (dialog, which) -> {
                    if (which == 0) {
                        startTakePhoto();
                    } else if (which == 1) {
                        startImagePick();
                    }
                }).show();
                break;
            case R.id.cv_username:
                DialogHelp.showInputContentDialog(this, "修改昵称", "请输入新昵称", new DialogHelp.DialogInterface() {
                    @Override
                    public void cancelDialog(View v, Dialog d) {
                        d.dismiss();
                    }

                    @Override
                    public void determineDialog(View v, Dialog d) {
                        EditText content = d.findViewById(R.id.et_input);
                        String result = content.getText().toString();
                        if (StringUtils.isEmpty(result)) {
                            ToastUtils.showShort("请输入新昵称");
                            return;
                        }
                        requestSaveBirthday(USERNAME, result);
                        d.dismiss();
                    }
                });
                break;
            case R.id.cv_signature:
                DialogHelp.showInputContentDialog(this, "修改签名", "请输入新签名", new DialogHelp.DialogInterface() {
                    @Override
                    public void cancelDialog(View v, Dialog d) {
                        d.dismiss();
                    }

                    @Override
                    public void determineDialog(View v, Dialog d) {
                        EditText content = d.findViewById(R.id.et_input);
                        String result = content.getText().toString();
                        if (StringUtils.isEmpty(result)) {
                            ToastUtils.showShort("请输入新签名");
                            return;
                        }
                        requestSaveBirthday(SIGNATURE, result);
                        d.dismiss();
                    }
                });
                break;
            case R.id.cv_birthday:
                showSelectBirthday(Calendar.getInstance());
                break;
            case R.id.cv_sex:
                DialogHelp.getSelectDialog(this, mStrings, (dialog, which) -> {
                    requestSaveBirthday(SEX, mStrings[which]);
                }).show();
                break;
            case R.id.btn_exitLogin:
                LoginUtils.ExitLoginStatus();
                break;
            default:
                break;
        }
    }

    private String[] mStrings = new String[]{"男", "女"};

    //生日选择
    private void showSelectBirthday(final Calendar c) {
        DatePickerDialog dialog = new DatePickerDialog(UserInfoDetailActivity.this, (view, year, monthOfYear, dayOfMonth) -> {
            c.set(year, monthOfYear, dayOfMonth);
            if (c.getTime().getTime() > System.currentTimeMillis()) {
                ToastUtils.showShort("请选择正确的日期");
                return;
            }
            final String birthday = DateFormat.format("yyy-MM-dd", c).toString();
            requestSaveBirthday(2, birthday);

        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

        try {
            dialog.getDatePicker().setMinDate(new SimpleDateFormat("yyyy-MM-dd").parse("1950-01-01").getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        dialog.show();
    }

    private void requestSaveBirthday(int type, String fields) {
        final String fieldType = fields;
        if (type == BIRTHDAY) {
            fields = getFiledJson("birthday", fields);
        } else if (type == USERNAME) {
            fields = getFiledJson("user_nicename", fields);
        } else if (type == SIGNATURE) {
            fields = getFiledJson("signature", fields);
        } else if (type == SEX) {
            if ("男".equals(fields)) {
                fields = "1";
            } else {
                fields = "2";
            }
            fields = getFiledJson("sex", fields);
        } else {
            return;
        }
        RetrofitClient.getInstance().createApi().ModifyUserInfo("User.updateFields", fields, AppContext.getInstance().getToken(), AppContext.getInstance().getLoginUid())
                .compose(RxUtils.io_main())
                .subscribe(new Observer<BaseBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseBean baseBean) {
                        if (baseBean.data.info.size() > 0) {
                            ToastUtils.showShort("修改成功");
                            UserBean u = AppContext.getInstance().getLoginUser();
                            if (type == BIRTHDAY) {
                                u.birthday = fieldType;
                                mCvBirthday.setContent(fieldType);
                            } else if (type == USERNAME) {
                                u.user_nicename = fieldType;
                                mCvUsername.setContent(fieldType);
                            } else if (type == SIGNATURE) {
                                u.signature = fieldType;
                                mCvSignature.setContent(fieldType);
                            } else if (type == SEX) {
                                if ("男".equals(fieldType)) {
                                    u.sex = "1";
                                } else {
                                    u.sex = "2";
                                }
                                mCvSex.setContent(fieldType);
                            }
                            AppContext.getInstance().updateUserInfo(u);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public String getFiledJson(String key, String val) {
        return "{\"" + key + "\":\"" + val + "\"}";
    }

    /**
     * 选择图片裁剪
     */
    private void startImagePick() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "选择图片"),
                    ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP);
        } else {
            intent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "选择图片"),
                    ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP);
        }
    }

    private Uri resultUri;
    private Uri cropUri;
    private File protraitFile;

    private void startTakePhoto() {
        Intent intent;
        // 判断是否挂载了SD卡
        String savePath = "";
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            savePath = AppConfig.DEFAULT_SAVE_IMAGE_PATH + "Camera/";
            File savedir = new File(savePath);
            if (!savedir.exists()) {
                savedir.mkdirs();
            }
        }

        // 没有挂载SD卡，无法保存文件
        if (savePath.isEmpty()) {
            ToastUtils.showShort("无法保存照片，请检查SD卡是否挂载");
            return;
        }

        String fileName = "phonelive_" + UUID.randomUUID() + ".jpg";// 照片命名
        File out = new File(savePath, fileName);
        Uri uri = Uri.fromFile(out);
        resultUri = uri;
        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA);
    }

    /**
     * 拍照后裁剪
     *
     * @param data 原始图片
     */
    private void startActionCrop(Uri data) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(data, "image/*");
        intent.putExtra("output", this.getUploadTempFile(data));
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);// 裁剪框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 200);// 输出图片大小
        intent.putExtra("outputY", 200);
        intent.putExtra("scale", true);// 去黑边
        intent.putExtra("scaleUpIfNeeded", true);// 去黑边
        startActivityForResult(intent, ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD);
    }

    // 裁剪头像的绝对路径
    private Uri getUploadTempFile(Uri uri) {
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            File savedir = new File(AppConfig.DEFAULT_SAVE_IMAGE_PATH);
            if (!savedir.exists()) {
                savedir.mkdirs();
            }
        } else {
            ToastUtils.showShort("无法保存上传的头像，请检查SD卡是否挂载");
            return null;
        }
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss")
                .format(new Date());
        String thePath = ImageUtils.getAbsolutePathFromNoStandardUri(uri);

        // 如果是标准Uri
        if (StringUtils.isEmpty(thePath)) {
            thePath = ImageUtils.getAbsoluteImagePath(this, uri);
        }
        String ext = thePath.substring(thePath.lastIndexOf('.') + 1);
        ext = StringUtils.isEmpty(ext) ? "jpg" : ext;
        // 照片命名
        String cropFileName = "phonelive_crop_" + timeStamp + "." + ext;
        // 裁剪头像的绝对路径
        protraitFile = new File(AppConfig.DEFAULT_SAVE_IMAGE_PATH + cropFileName);
        cropUri = Uri.fromFile(protraitFile);
        return this.cropUri;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA:
                startActionCrop(resultUri);// 选图后裁剪
                break;
            case ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP:
                startActionCrop(intent.getData());// 选图后裁剪
                break;
            case ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD:
                updatePhotoToServer();
                break;
            default:
                break;
        }

    }

    private void updatePhotoToServer() {
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), protraitFile);
        ArrayList<MultipartBody.Part> list = new ArrayList<>();
        list.add(MultipartBody.Part.createFormData("file", "wp.png", requestBody));
        list.add(MultipartBody.Part.createFormData("service", "User.updateAvatar"));
        list.add(MultipartBody.Part.createFormData("uid", AppContext.getInstance().getLoginUid()));
        list.add(MultipartBody.Part.createFormData("token", AppContext.getInstance().getToken()));
        ProgressDialog loading = ProgressDialog.show(this, "", "正在上传图片中", false, false);
        RetrofitClient.getInstance().createApi().UpdateAvatar(list).compose(RxUtils.io_main())
                .subscribe(new BaseListObserver<UpdatePhotoBean>() {
                    @Override
                    protected void onHandleSuccess(List<UpdatePhotoBean> list) {
                        if (list.size() > 0) {
                            ToastUtils.showShort("头像修改成功");
                            UserBean u = AppContext.getInstance().getLoginUser();
                            u.avatar = list.get(0).avatar;
                            u.avatar_thumb = list.get(0).avatar_thumb;
                            AppContext.getInstance().updateUserInfo(u);
                            loading.dismiss();
                        }
                    }
                });
    }

}
