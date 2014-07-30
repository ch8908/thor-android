package com.osolve.thor.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * Created by liq on 4/16/14.
 */
public abstract class AbstractPref {

  public class BooleanPref {

    private final String key;
    private final boolean defaultValue;

    public BooleanPref(final String key) {
      this(key, false);
    }

    public BooleanPref(final String key, final boolean defaultValue) {
      this.key = key;
      this.defaultValue = defaultValue;
    }

    public boolean get() {
      final SharedPreferences prefs = context.getSharedPreferences(PREFERENCE_FILE_NAME,
          Context.MODE_PRIVATE);
      return prefs.getBoolean(key, defaultValue);
    }

    public void set(final boolean value) {
      final SharedPreferences prefs = context.getSharedPreferences(PREFERENCE_FILE_NAME,
          Context.MODE_PRIVATE);
      final SharedPreferences.Editor editor = prefs.edit();
      editor.putBoolean(key, value);
      editor.commit();
    }
  }

  public class IntPref {

    private final String key;
    private final int defaultValue;

    public IntPref(final String key) {
      this(key, 0);
    }

    public IntPref(final String key, final int defaultValue) {
      this.key = key;
      this.defaultValue = defaultValue;
    }

    public int get() {
      final SharedPreferences prefs = context.getSharedPreferences(PREFERENCE_FILE_NAME,
          Context.MODE_PRIVATE);
      return prefs.getInt(key, defaultValue);
    }

    public void set(final int value) {
      final SharedPreferences prefs = context.getSharedPreferences(PREFERENCE_FILE_NAME,
          Context.MODE_PRIVATE);
      final SharedPreferences.Editor editor = prefs.edit();
      editor.putInt(key, value);
      editor.commit();
    }
  }

  public class LongPref {

    private final String key;

    public LongPref(final String key) {
      this.key = key;
    }

    public long get() {
      final SharedPreferences prefs = context.getSharedPreferences(PREFERENCE_FILE_NAME,
          Context.MODE_PRIVATE);
      return prefs.getLong(key, 0);
    }

    public void set(final long value) {
      final SharedPreferences prefs = context.getSharedPreferences(PREFERENCE_FILE_NAME,
          Context.MODE_PRIVATE);
      final SharedPreferences.Editor editor = prefs.edit();
      editor.putLong(key, value);
      editor.commit();
    }
  }

  public class SingleShotFlag extends BooleanPref {

    private final boolean defaultValue;

    public SingleShotFlag(final String key) {
      super(key, false);
      defaultValue = false;
    }

    public SingleShotFlag(final String key, final boolean init) {
      super(key, init);
      defaultValue = init;
    }

    public boolean getAndSet() {
      final boolean value = get();
      set(!defaultValue);
      return value;
    }

    public void setToDefaultValue() {
      set(defaultValue);
    }
  }

  public class StringPref {

    private final String key;
    private final String defaultValue;

    public StringPref(final String key) {
      this(key, "");
    }

    public StringPref(final String key, final String defaultValue) {
      this.key = key;
      this.defaultValue = defaultValue;
    }

    public String get() {
      final SharedPreferences prefs = context.getSharedPreferences(PREFERENCE_FILE_NAME,
          Context.MODE_PRIVATE);
      return prefs.getString(key, defaultValue);
    }

    public boolean isDefined() {
      return !TextUtils.isEmpty(get());
    }

    public void set(final String value) {
      final SharedPreferences prefs = context.getSharedPreferences(PREFERENCE_FILE_NAME,
          Context.MODE_PRIVATE);
      final SharedPreferences.Editor editor = prefs.edit();
      editor.putString(key, value);
      editor.commit();
    }

    public void remove() {
        final SharedPreferences prefs = context.getSharedPreferences(PREFERENCE_FILE_NAME,
                Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.remove(key);
        editor.commit();
    }
  }

  private static final String PREFERENCE_FILE_NAME = "THOR_PREF";

  protected final Context context;

  protected AbstractPref(final Application application) {
    this.context = application;
  }
}
