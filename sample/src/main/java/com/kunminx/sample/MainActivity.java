package com.kunminx.sample;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.kunminx.sample.config.TestEvent;

/**
 * Create by KunMinX at 2022/7/19
 */
public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    TestEvent event = TestEvent.ResultTest1("textx");

    switch (event.id) {
      case TestEvent.ResultTest1.ID:
        TestEvent.ResultTest1 event1 = (TestEvent.ResultTest1) event;
        event1.copy(1);
        Log.d("---", event1.paramA);
        Log.d("---", String.valueOf(event1.resultB));
        break;
      case TestEvent.ResultTest2.ID:
        break;
    }
  }
}