package ren.qinc.markdowneditors.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.transition.Explode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import ren.qinc.markdowneditors.R;
import ren.qinc.markdowneditors.model.User;

public class Main2Activity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;
    private TextView loginwithQQ;
    private Button btGo;
    private CardView cv;
    private FloatingActionButton fab;
    //QQ登录的接入控件。
    private static final String TAG = "Main2Activity";
    private static final String APP_ID = "1106671771";//官方获取的APPID
    private Tencent mTencent;
    private BaseUiListener mIUiListener;
    private UserInfo mUserInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initView();
        setListener();
    }


    private void initView() {

        etUsername = (EditText) findViewById(R.id.et_username);
        etPassword = (EditText) findViewById(R.id.et_password);
        btGo = (Button) findViewById(R.id.bt_go);
        cv = (CardView) findViewById(R.id.cv);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        loginwithQQ= (TextView) findViewById(R.id.loginwithQQ);
        //传入参数APPID和全局Context上下文
        mTencent = Tencent.createInstance(APP_ID, Main2Activity.this.getApplicationContext());

    }


    private void setListener() {
        btGo.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                Toast.makeText(Main2Activity.this, "请输入密码和账号登录", Toast.LENGTH_SHORT).show();
//                Explode explode = new Explode();
//                explode.setDuration(500);
//
//                getWindow().setExitTransition(explode);
//                getWindow().setEnterTransition(explode);
//
////                登录的逻辑在这里验证
////                ActivityOptionsCompat oc2 = ActivityOptionsCompat.makeSceneTransitionAnimation(Main2Activity.this);
////                Intent i2 = new Intent(Main2Activity.this,HomeActivity.class);
////                startActivity(i2, oc2.toBundle());
///**通过这句代码，SDK实现了QQ的登录，这个方法有三个参数，第一个参数是context上下文，
// * 第二个参数SCOPO 是一个String类型的字符串，表示一些权限
// *
// 官方文档中的说明：应用需要获得哪些API的权限，由“，”分隔。例如：SCOPE = “get_user_info,add_t”；所有权限用“all”
// 第三个参数，是一个事件监听器，IUiListener接口的实例，这里用的是该接口的实现类 */
//                mIUiListener = new BaseUiListener();
//                //all表示获取所有权限
//                mTencent.login(Main2Activity.this, "all", mIUiListener);

            }
        });
        loginwithQQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Explode explode = new Explode();
                explode.setDuration(500);

                getWindow().setExitTransition(explode);
                getWindow().setEnterTransition(explode);
                mIUiListener = new BaseUiListener();
                mTencent.login(Main2Activity.this, "all", mIUiListener);
            }
        });

//        打开另外一个页面
        fab.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                getWindow().setExitTransition(null);
                getWindow().setEnterTransition(null);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Main2Activity.this, fab, fab.getTransitionName());
                startActivity(new Intent(Main2Activity.this, RegisterActivity.class), options.toBundle());
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        fab.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fab.setVisibility(View.VISIBLE);
    }

    private class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object response) {
            JSONObject obj = (JSONObject) response;
            try {
                String openID = obj.getString("openid");
                String accessToken = obj.getString("access_token");
                String expires = obj.getString("expires_in");
                mTencent.setOpenId(openID);
                mTencent.setAccessToken(accessToken, expires);
                QQToken qqToken = mTencent.getQQToken();
                mUserInfo = new UserInfo(getApplicationContext(), qqToken);
                mUserInfo.getUserInfo(new IUiListener() {
                    @Override
                    public void onComplete(Object response) {
                        JSONObject info = (JSONObject) response;
                        try {
                            String nickName = info.getString("nickname");//获取用户昵称
                            String iconUrl = info.getString("figureurl_qq_2");//获取用户头像的url
                            User user=new User(nickName,iconUrl);
                            ActivityOptionsCompat oc2 = ActivityOptionsCompat.makeSceneTransitionAnimation(Main2Activity.this);
                            Intent i2 = new Intent(Main2Activity.this, HomeActivity.class);
//                     这里吧用户的数据传入给HomeActivity。
//                     传递好之后settingFragemet跟新视图。
                            i2.putExtra("person_data", user);
                            startActivity(i2,oc2.toBundle());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(UiError uiError) {
                        Toast.makeText(Main2Activity.this, "登录失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(Main2Activity.this, "登录取消", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(UiError uiError) {
            Toast.makeText(Main2Activity.this, "授权失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(Main2Activity.this, "授权取消", Toast.LENGTH_SHORT).show();

        }

    }


    public void getUserInfoInThread(Object response)
    {
        new Thread(){
            @Override
            public void run() {
//                在线程中实现获取用户的数据。




            }
        }.start();
    }

    /**
     * 在调用Login的Activity或者Fragment中重写onActivityResult方法
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_LOGIN) {
            Tencent.onActivityResultData(requestCode, resultCode, data, mIUiListener);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
