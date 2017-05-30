package kr.study.chatapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mInputMessage;
    private Button mSendMEssage;
    private LinearLayout mMessageLog;
//    private TextView mCpuMessage;
//    private TextView mUserMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mInputMessage = (EditText) findViewById(R.id.input_message);
        mSendMEssage = (Button) findViewById(R.id.send_message);
        mMessageLog = (LinearLayout) findViewById(R.id.message_log);
//        mCpuMessage = (TextView) findViewById(R.id.cpu_message);
//        mUserMessage = (TextView) findViewById(R.id.user_message);

        mInputMessage.setText("hoge");

        mSendMEssage.setOnClickListener(this);

//        mSendMEssage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(mSendMEssage)) {
            // SEND 버튼 이 눌렸을 떄의 처리
            String inputText = mInputMessage.getText().toString();
            String answer;
//            mUserMessage.setText(inputText);
            TextView userMessage = new TextView(this);
            userMessage.setText(inputText);
            userMessage.setGravity(Gravity.END);
            mMessageLog.addView(userMessage, 0);

            if (inputText.contains("안녕") || inputText.contains("hello")) {
                answer = "안녕하세요";
            } else if (inputText.contains("피곤") || inputText.contains("tired")) {
                answer = "고생헀어요";
            } else if (inputText.contains("운세") || inputText.contains("luck")) {
                double random = Math.random() * 5.1d;
                if (random < 1d) {
                    answer = "몹시 나쁨";
                } else if (random < 2d) {
                    answer = "나쁨";
                } else if (random < 3d) {
                    answer = "보통";
                } else if (random < 4d) {
                    answer = "행운";
                } else if (random < 5d) {
                    answer = "대박";
                } else {
                    answer = "운수대통";
                }
            } else if (inputText.contains("시간") || inputText.contains("time")) {
                Calendar cal = Calendar.getInstance();
                int hour = cal.get(Calendar.HOUR);
                int minute = cal.get(Calendar.MINUTE);
                int second = cal.get(Calendar.SECOND);
                answer = String.format("현재 시각은 %1$d시 %2$d분 %3$d초입니다", hour, minute, second);
            } else {
                answer = "그거 괜찮네요";
            }
//            mCpuMessage.setText(answer);
            final TextView cpuMessage = new TextView(this);
            cpuMessage.setText(answer);
            cpuMessage.setGravity(Gravity.START);


            mInputMessage.setText("");
            TranslateAnimation userMessageAnimation = new TranslateAnimation(mMessageLog.getWidth(), 0, 0, 0);
            userMessageAnimation.setDuration(1000);
            userMessageAnimation.setAnimationListener(new Animation.AnimationListener(){
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mMessageLog.addView(cpuMessage, 0);
                    TranslateAnimation cpuMessageAnimation = new TranslateAnimation(-mMessageLog.getWidth(), 0, 0, 0);
                    cpuMessageAnimation.setDuration(1000);
                    cpuMessage.setAnimation(cpuMessageAnimation);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            userMessage.startAnimation(userMessageAnimation);
        }
    }
}
