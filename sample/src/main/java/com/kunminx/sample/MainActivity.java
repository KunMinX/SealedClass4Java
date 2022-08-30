package com.kunminx.sample;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.kunminx.sample.bean.User;
import com.kunminx.sample.config.TestEvent_;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by KunMinX at 2022/7/19
 */
public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    User u = new User();
    u.title = "title1";
    u.content = "content1";
    List<User> users = new ArrayList<>();
    users.add(u);

    findViewById(R.id.btn_write).setOnClickListener(v -> {
//      configs.user().set(u);
//      configs.users().set(users);
    });
    findViewById(R.id.btn_read).setOnClickListener(v -> {
//      Log.d("---title", configs.user().get().title);
//      Log.d("---users", configs.users().get().toString());
//      Log.d("---content", configs.users().get().get(0).content);
    });

    TestEvent_.ResultTest1 resultTest1 = new TestEvent_.ResultTest1("x", 1);

    TestEvent_ event = TestEvent_.ResultTest1("textx");

    switch (event.id) {
      case TestEvent_.ResultTest1.ID:
        break;
      case TestEvent_.ResultTest2.ID:
        break;
    }
  }
}