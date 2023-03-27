// Generated by view binder compiler. Do not edit!
package com.kidsupervisor.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kidsupervisor.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentHomeBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button addSchedule;

  @NonNull
  public final TextView lengthText;

  @NonNull
  public final ListView list;

  @NonNull
  public final FloatingActionButton logoPic;

  @NonNull
  public final Button modifyBtn;

  @NonNull
  public final Button rmBtn;

  @NonNull
  public final TextView scheduleText;

  @NonNull
  public final Spinner spinner;

  @NonNull
  public final TextView weightText;

  @NonNull
  public final TextView weightText2;

  private FragmentHomeBinding(@NonNull ConstraintLayout rootView, @NonNull Button addSchedule,
      @NonNull TextView lengthText, @NonNull ListView list, @NonNull FloatingActionButton logoPic,
      @NonNull Button modifyBtn, @NonNull Button rmBtn, @NonNull TextView scheduleText,
      @NonNull Spinner spinner, @NonNull TextView weightText, @NonNull TextView weightText2) {
    this.rootView = rootView;
    this.addSchedule = addSchedule;
    this.lengthText = lengthText;
    this.list = list;
    this.logoPic = logoPic;
    this.modifyBtn = modifyBtn;
    this.rmBtn = rmBtn;
    this.scheduleText = scheduleText;
    this.spinner = spinner;
    this.weightText = weightText;
    this.weightText2 = weightText2;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentHomeBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentHomeBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_home, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentHomeBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.addSchedule;
      Button addSchedule = ViewBindings.findChildViewById(rootView, id);
      if (addSchedule == null) {
        break missingId;
      }

      id = R.id.lengthText;
      TextView lengthText = ViewBindings.findChildViewById(rootView, id);
      if (lengthText == null) {
        break missingId;
      }

      id = R.id.list;
      ListView list = ViewBindings.findChildViewById(rootView, id);
      if (list == null) {
        break missingId;
      }

      id = R.id.logoPic;
      FloatingActionButton logoPic = ViewBindings.findChildViewById(rootView, id);
      if (logoPic == null) {
        break missingId;
      }

      id = R.id.modifyBtn;
      Button modifyBtn = ViewBindings.findChildViewById(rootView, id);
      if (modifyBtn == null) {
        break missingId;
      }

      id = R.id.rmBtn;
      Button rmBtn = ViewBindings.findChildViewById(rootView, id);
      if (rmBtn == null) {
        break missingId;
      }

      id = R.id.scheduleText;
      TextView scheduleText = ViewBindings.findChildViewById(rootView, id);
      if (scheduleText == null) {
        break missingId;
      }

      id = R.id.spinner;
      Spinner spinner = ViewBindings.findChildViewById(rootView, id);
      if (spinner == null) {
        break missingId;
      }

      id = R.id.weightText;
      TextView weightText = ViewBindings.findChildViewById(rootView, id);
      if (weightText == null) {
        break missingId;
      }

      id = R.id.weightText2;
      TextView weightText2 = ViewBindings.findChildViewById(rootView, id);
      if (weightText2 == null) {
        break missingId;
      }

      return new FragmentHomeBinding((ConstraintLayout) rootView, addSchedule, lengthText, list,
          logoPic, modifyBtn, rmBtn, scheduleText, spinner, weightText, weightText2);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
