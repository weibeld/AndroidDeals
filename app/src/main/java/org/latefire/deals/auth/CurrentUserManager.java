package org.latefire.deals.auth;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import org.latefire.deals.database.AbsUser;
import org.latefire.deals.database.Business;
import org.latefire.deals.database.Customer;
import org.latefire.deals.database.DatabaseManager;
import org.latefire.deals.utils.Constant;

/**
 * Created by dw on 27/03/17.
 */

public class CurrentUserManager {

  private static CurrentUserManager instance;

  private FirebaseAuth mFirebaseAuth;
  private DatabaseManager mDatabaseManager;

  private AbsUser mCurrentUser;
  private DatabaseReference mUserRef;
  private ValueEventListener mUserListener;

  private CurrentUserManager() {
    mFirebaseAuth = FirebaseAuth.getInstance();
    mDatabaseManager = DatabaseManager.getInstance();
  }

  public static synchronized  CurrentUserManager getInstance() {
    if (instance == null) instance = new CurrentUserManager();
    return instance;
  }

  public void getCurrentUser(CurrentUserCallback callback) {
    if (mFirebaseAuth.getCurrentUser() == null)
      callback.result(null);
    else if (mCurrentUser == null)
      updateCurrentUser(callback);
    else if (mCurrentUser.getId() == mFirebaseAuth.getCurrentUser().getUid())
      callback.result(mCurrentUser);
    else
      updateCurrentUser(callback);
  }

  private void updateCurrentUser(CurrentUserCallback callback) {
    if (mUserRef != null && mUserListener != null) {
      mUserRef.removeEventListener(mUserListener);
      mUserRef = null;
      mUserListener = null;
    }
    String userId = mFirebaseAuth.getCurrentUser().getUid();
    mDatabaseManager.getUserType(userId, userType -> {
      if (userType == Constant.USER_TYPE_CUSTOMER) {
        mUserRef = mDatabaseManager.getCustomerRef(userId);
        mUserListener = new CurrentCustomerListener();
        mUserRef.addValueEventListener(mUserListener);
        mUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
          @Override public void onDataChange(DataSnapshot dataSnapshot) {
            callback.result(dataSnapshot.getValue(Customer.class));
          }
          @Override public void onCancelled(DatabaseError databaseError) {}
        });
      }
      else if (userType == Constant.USER_TYPE_BUSINESS) {
        mUserRef = mDatabaseManager.getBusinessRef(userId);
        mUserListener = new CurrentBusinessListener();
        mUserRef.addValueEventListener(mUserListener);
        mUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
          @Override public void onDataChange(DataSnapshot dataSnapshot) {
            callback.result(dataSnapshot.getValue(Business.class));
          }
          @Override public void onCancelled(DatabaseError databaseError) {}
        });
      }
      else callback.result(null);
    });
  }

  private class CurrentCustomerListener implements ValueEventListener {
    @Override public void onDataChange(DataSnapshot dataSnapshot) {
      mCurrentUser = dataSnapshot.getValue(Customer.class);
    }
    @Override public void onCancelled(DatabaseError databaseError) {}
  }

  private class CurrentBusinessListener implements ValueEventListener {
    @Override public void onDataChange(DataSnapshot dataSnapshot) {
      mCurrentUser = dataSnapshot.getValue(Business.class);
    }
    @Override public void onCancelled(DatabaseError databaseError) {}
  }

  public interface CurrentUserCallback {
    void result(AbsUser user);
  }

}
