package com.kunminx.architecture.lang;

import android.text.TextUtils;

import androidx.annotation.NonNull;
/**
 * Create by KunMinX at 2022/8/30
 */
public class When<T> {
  private String id;
  private T t;

  public When<T> when(@NonNull String id, @NonNull T t) {
    this.id = id;
    this.t = t;
    return this;
  }
  public When<T> is(@NonNull String id, @NonNull DispatchAction<T> action) {
    if (!TextUtils.isEmpty(this.id) && id.equals(this.id)) {
      this.id = "";
      action.onAction(t);
    }
    return this;
  }
}
