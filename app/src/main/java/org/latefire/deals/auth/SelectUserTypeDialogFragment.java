package org.latefire.deals.auth;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;
import org.latefire.deals.R;
import org.latefire.deals.databinding.DialogSelectUserTypeBinding;

/**
 * Created by dw on 27/03/17.
 */
public class SelectUserTypeDialogFragment extends DialogFragment {

  DialogSelectUserTypeBinding b;
  OnUserTypeSelectedListener mListener;

  @Override public Dialog onCreateDialog(Bundle savedInstanceState) {

    b = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.dialog_select_user_type, null, false);

    b.rgUserType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
      @Override public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.rbBusiness) b.etBusinessName.setVisibility(View.VISIBLE);
        else if (checkedId == R.id.rbCustomer) b.etBusinessName.setVisibility(View.GONE);
      }
    });

    AlertDialog dialog = new AlertDialog.Builder(getActivity()).setTitle("Select Account Type")
        .setView(b.getRoot())
        .setPositiveButton("Create Account", (dialog1, which) -> {
          switch (b.rgUserType.getCheckedRadioButtonId()) {
            case (R.id.rbCustomer):
              mListener.onCustomerSelected();
              break;
            case (R.id.rbBusiness):
              mListener.onBusinessSelected(b.etBusinessName.getText().toString());
              break;
            default:
              throw new RuntimeException("Invalid radio button ID: " + b.rgUserType.getCheckedRadioButtonId());
          }
        })
        .create();
    return dialog;
  }

  @Override public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof GoogleSignInFragment.OnGoogleSignInListener) {
      mListener = (OnUserTypeSelectedListener) context;
    } else {
      throw new RuntimeException(context.toString() + " must implement " + OnUserTypeSelectedListener.class.getCanonicalName());
    }
  }

  @Override public void onDetach() {
    super.onDetach();
    mListener = null;
  }

  public interface OnUserTypeSelectedListener {
    void onCustomerSelected();
    void onBusinessSelected(String businessName);
  }
}
