package org.latefire.deals.business.home;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.view.View;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Calendar;
import org.latefire.deals.R;
import org.latefire.deals.auth.AuthManager;
import org.latefire.deals.base.BaseActivity;
import org.latefire.deals.database.DatabaseManager;
import org.latefire.deals.database.Deal;
import org.latefire.deals.databinding.ActivityCreateDealBinding;
import org.latefire.deals.utils.AlertDialogUtils;
import org.latefire.deals.utils.Constant;
import org.latefire.deals.utils.DateUtils;
import org.latefire.deals.utils.DeviceUtils;
import org.latefire.deals.utils.ProcessBitmap;
import org.latefire.deals.utils.debug.ShowLog;

public class CreateDealActivity extends BaseActivity {

  public static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 10;
  private static final String LOG_TAG = CreateDealActivity.class.getSimpleName();

  ActivityCreateDealBinding b;
  CreateDealActivity mActivity;
  DatabaseManager mDatabaseManager;
  private File mImageFile;
  private String mCurrentPhotoPath;
  private Deal mDeal;
  private Bitmap mBitmapImageDeal;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    b = DataBindingUtil.setContentView(this, R.layout.activity_create_deal);
    ButterKnife.bind(this);
    setSupportActionBar(b.toolbarInclude.toolbar);
    getSupportActionBar().setTitle("Create Deal");
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    mActivity = this;
    mDatabaseManager = DatabaseManager.getInstance();
    mDeal = new Deal();
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == Constant.FROM_GALLERY && resultCode == Activity.RESULT_OK) {
      Uri selectedImage = data.getData();
      mBitmapImageDeal = ProcessBitmap.decodeFile(this, selectedImage, 800, 800);
      b.imgDeal.setImageBitmap(mBitmapImageDeal);
    }
    if (requestCode == Constant.TAKE_A_PHOTO && resultCode == Activity.RESULT_OK) {
      File file = new File(mCurrentPhotoPath);
      mBitmapImageDeal = ProcessBitmap.decodeFile(file, 800, 800);
      b.imgDeal.setImageBitmap(mBitmapImageDeal);
    }
    if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
      if (resultCode == RESULT_OK) {
        Place place = PlaceAutocomplete.getPlace(this, data);
        ShowLog.debug("Place: " + place.getName());
        b.etLocation.setText(place.getName());
        mDeal.setLocationName(place.getName().toString());
        mDeal.setLatitude(place.getLatLng().latitude);
        mDeal.setLongitude(place.getLatLng().longitude);
      } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
        Status status = PlaceAutocomplete.getStatus(this, data);
        // TODO: Handle the error.
        ShowLog.debug(status.getStatusMessage());
      } else if (resultCode == RESULT_CANCELED) {
        // The user canceled the operation.
      }
    }
  }

  @Override public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
      @NonNull int[] grantResults) {
    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
      switch (requestCode) {
        case Constant.PERMISSION_REQUEST_CAMERA:
          DeviceUtils.takePicture(this, mImageFile);
          break;
        case Constant.PERMISSION_READ_WRITE_STORAGE:
          DeviceUtils.pickFromGallery(this);
          break;
        case Constant.PERMISSION_CREATE_FILE:
          mImageFile = DeviceUtils.getFileForCaptureImage(this);
          mCurrentPhotoPath = mImageFile != null ? mImageFile.getAbsolutePath() : "";
          break;
      }
    }
  }

  @OnClick(R.id.etLocation) public void pickPlace(View view) {
    try {
      Intent intent =
          new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(this);
      startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
    } catch (GooglePlayServicesRepairableException e) {
      e.printStackTrace();
    } catch (GooglePlayServicesNotAvailableException e) {
      e.printStackTrace();
    }
  }

  @OnClick(R.id.etDateBegin) public void pickDateBegin(View view) {
    Calendar calendar = Calendar.getInstance();
    DatePickerDialog datePickerDialog =
        new DatePickerDialog(this, (view1, year, month, dayOfMonth) -> {
          Calendar calendar1 = Calendar.getInstance();
          calendar1.set(Calendar.YEAR, year);
          calendar1.set(Calendar.MONTH, month);
          calendar1.set(Calendar.DAY_OF_MONTH, dayOfMonth);
          b.etDateBegin.setText(DateUtils.formatDayMonthYear(calendar1.getTime()));
          mDeal.setBeginValidity(calendar1.getTimeInMillis());
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH));
    datePickerDialog.show();
  }

  @OnClick(R.id.etDateEnd) public void pickDateEnd(View view) {
    Calendar calendar = Calendar.getInstance();
    DatePickerDialog datePickerDialog =
        new DatePickerDialog(this, (view1, year, month, dayOfMonth) -> {
          Calendar calendar1 = Calendar.getInstance();
          calendar1.set(Calendar.YEAR, year);
          calendar1.set(Calendar.MONTH, month);
          calendar1.set(Calendar.DAY_OF_MONTH, dayOfMonth);
          b.etDateEnd.setText(DateUtils.formatDayMonthYear(calendar1.getTime()));
          mDeal.setEndValidity(calendar1.getTimeInMillis());
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH));

    datePickerDialog.show();
  }

  @OnClick(R.id.imgDeal) public void pickImage(View view) {
    new AlertDialog.Builder(this).setTitle("Upload deal image")
        .setItems(R.array.upload_deal_image, (dialog, which) -> {
          dialog.dismiss();
          if (which == 0) {
            // take a picture
            mImageFile = DeviceUtils.getFileForCaptureImage(this);
            mCurrentPhotoPath = mImageFile != null ? mImageFile.getAbsolutePath() : "";
            //deal.setLocalPhotoPath(mCurrentPhotoPath);
          } else {
            // choose from gallery
            DeviceUtils.pickFromGallery(this);
          }
        })
        .create()
        .show();
  }

  @OnClick(R.id.btnSave) public void uploadImage(View view) {
    if (!validateInput()) {
      return;
    }

    showProgress();

    // Storage reference to deal image
    StorageReference rootRef = FirebaseStorage.getInstance().getReference();
    StorageReference imageRef = rootRef.child(System.currentTimeMillis() + ".jpg");

    // Transform the image to a byte array
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    mBitmapImageDeal.compress(Bitmap.CompressFormat.JPEG, 100, baos);
    byte[] data = baos.toByteArray();

    // Upload the data to Firebase Storage
    imageRef.putBytes(data)
      .addOnFailureListener(exception -> dismissProgress())
      .addOnSuccessListener(taskSnapshot -> createDeal(taskSnapshot.getDownloadUrl()));
  }

  private void createDeal(Uri imageUrl) {
    mDeal.setPhoto(imageUrl.toString());
    mDeal.setTitle(b.etTitle.getText().toString());
    mDeal.setDescription(b.etDescription.getText().toString());
    mDeal.setRegularPrice(Double.parseDouble(b.etPrice.getText().toString()));
    mDeal.setDealPrice(Double.parseDouble(b.etDealPrice.getText().toString()));
    mDeal.setLocation(b.etLocation.getText().toString());
    mDeal.setBusinessId(AuthManager.getInstance().getCurrentUserId());
    mDatabaseManager.createDeal(mDeal, () -> {
      dismissProgress();
      startActivity(new Intent(mActivity, HomeActivityBusiness.class));
      finish();
    });
  }

  private boolean validateInput() {
    if (mBitmapImageDeal == null) {
      AlertDialogUtils.showError(this, getString(R.string.error_upload_image));
      return false;
    }
    if (b.etTitle.getText().toString().isEmpty()) {
      AlertDialogUtils.showError(this, getString(R.string.erro_input_title));
      return false;
    }
    if (b.etDescription.getText().toString().isEmpty()) {
      AlertDialogUtils.showError(this, getString(R.string.error_input_description));
      return false;
    }
    if (b.etLocation.getText().toString().isEmpty()) {
      AlertDialogUtils.showError(this, getString(R.string.error_input_location));
      return false;
    }
    if (b.etPrice.getText().toString().isEmpty()) {
      AlertDialogUtils.showError(this, getString(R.string.error_input_price));
      return false;
    }
    if (b.etDealPrice.getText().toString().isEmpty()) {
      AlertDialogUtils.showError(this, getString(R.string.error_input_deal_price));
      return false;
    }
    if (b.etDateBegin.getText().toString().isEmpty()) {
      AlertDialogUtils.showError(this, getString(R.string.error_input_date_begin));
      return false;
    }
    if (b.etDateEnd.getText().toString().isEmpty()) {
      AlertDialogUtils.showError(this, getString(R.string.error_input_date_end));
      return false;
    }
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        finish();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }
}
