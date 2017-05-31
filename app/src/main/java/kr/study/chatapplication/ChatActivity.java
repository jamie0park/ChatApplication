package kr.study.chatapplication;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
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

import java.util.ArrayList;
import java.util.Calendar;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mInputMessage;
    private Button mSendMEssage;
    private LinearLayout mMessageLog;
    private TextView mCpuMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Locale locale = Locale.US;
//        Locale.setDefault(locale);
//        Configuration config = new Configuration();
//        config.locale = locale;
//        getResources().updateConfiguration(config, null);

        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // XML의 View를 가져온다
        mInputMessage = (EditText) findViewById(R.id.input_message);
        // 사용자 입력필드
        mSendMEssage = (Button) findViewById(R.id.send_message); // SEND 버튼
        // 입력 이력을 표시할 레이아웃
        mMessageLog = (LinearLayout) findViewById(R.id.message_log);
        mCpuMessage = (TextView) findViewById(R.id.cpu_message); // 버튼이 눌렸을 때 메서드를 실행한다

        mSendMEssage.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);

        if (getPackageManager().queryIntentActivities(
                new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0).size() == 0) {
            menu.removeItem(R.id.action_voice_input);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically hadle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml
        int id = item.getItemId();

        // noinspection SimplifiableIfStatement
        if (id == R.id.action_voice_input) {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            startActivityForResult(intent, 0);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK
                && data.hasExtra(RecognizerIntent.EXTRA_RESULTS)) {
            ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (results.size() > 0) {
                mInputMessage.setText(results.get(0));
                send();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.equals(mSendMEssage)) {
            send();
        }
    }

    private void send () {
        // SEND 버튼 이 눌렸을 떄의 처리
        String inputText = mInputMessage.getText().toString();
        String lowerInputText = inputText.toLowerCase();
        String answer;
        TextView userMessage = new TextView(this); // 새 TextView를 생성한다
        int messageColor = getResources().getColor(R.color.message_color);
        userMessage.setTextColor(messageColor);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            userMessage.setBackground(getResources().getDrawable(R.drawable.user_message, getTheme()));
        } else {
            userMessage.setBackground(getResources().getDrawable(R.drawable.user_message));
        }
        userMessage.setText(inputText); // TextView에 입력한 텍스트를 설정한다

        // 메시지 크기에 맞춤
        LinearLayout.LayoutParams userMessageLayout = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        userMessageLayout.gravity = Gravity.END; // 오른쪽 맞춤
        // 단말에 따라 최적의 간격을 띄운다
        final int marginSize = getResources().getDimensionPixelSize(R.dimen.message_margin);
        // 간격을 설정한다
        userMessageLayout.setMargins(0, marginSize, 0, marginSize);
        mMessageLog.addView(userMessage, 0, userMessageLayout);

        // TextView를 View의 맨 위에 설정한다
        if (lowerInputText.contains(getString(R.string.how_are_you))) {
            answer = getString(R.string.fine);
        } else if (lowerInputText.contains(getString(R.string.tire))) {
            answer = getString(R.string.bless_you);
        } else if (lowerInputText.contains(getString(R.string.fortune))) {
            double random = Math.random() * 5.1d;
            if (random < 1d) {
                answer = getString(R.string.worst_luck);
            } else if (random < 2d) {
                answer = getString(R.string.bad_luck);
            } else if (random < 3d) {
                answer = getString(R.string.good_luck);
            } else if (random < 4d) {
                answer = getString(R.string.nice_luck);
            } else if (random < 5d) {
                answer = getString(R.string.best_luck);
            } else {
                answer = getString(R.string.amazing_best_luck);
            }
        } else if (lowerInputText.contains(getString(R.string.time))) {
            Calendar cal = Calendar.getInstance();
            int hour = cal.get(Calendar.HOUR);     // 시
            int minute = cal.get(Calendar.MINUTE); // 분
            int second = cal.get(Calendar.SECOND); // 초
            answer = getString(R.string.time_format, hour, minute, second);
        } else {
            answer = getString(R.string.good);
        }
        final TextView cpuMessage = new TextView(this);
        // 내부 클래스에서 참조하기 위해 final 선언
        cpuMessage.setTextColor(messageColor);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cpuMessage.setBackground(getResources().getDrawable(R.drawable.cpu_message, getTheme()));
        } else {
            cpuMessage.setBackground(getResources().getDrawable(R.drawable.cpu_message));
        }
        cpuMessage.setText(answer);
        cpuMessage.setGravity(Gravity.START);
        mInputMessage.setText("");
        TranslateAnimation userMessageAnimation = new TranslateAnimation(mMessageLog.getWidth(), 0, 0, 0);
        userMessageAnimation.setDuration(1000);
        userMessageAnimation.setAnimationListener(new Animation.AnimationListener(){
            // 클래스 이름이 선언되지 않은 내부 클래스를 '익명 내부 클래스 라고 한다
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // 메시지 크기에 맞춤
                LinearLayout.LayoutParams cpuMessageLayout = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                cpuMessageLayout.gravity = Gravity.START;
                // 간격을 설정한다
                cpuMessageLayout.setMargins(marginSize, marginSize, marginSize, marginSize);
                mMessageLog.addView(cpuMessage, 0, cpuMessageLayout);
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
