package org.latefire.deals.auth;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import org.latefire.deals.database.AbsUser;
import org.latefire.deals.database.DatabaseManager;

/**
 * Created by dw on 25/03/17.
 */

public class AuthManager {

  private static final String LOG_TAG = AuthManager.class.getSimpleName();

  private static AuthManager instance;
  private FirebaseAuth mFirebaseAuth;
  private AbsUser mCurrentUser;
  private DatabaseManager mDatabaseManager;

  private AuthManager() {
    mFirebaseAuth = FirebaseAuth.getInstance();
    mDatabaseManager = DatabaseManager.getInstance();
  }

  public static synchronized  AuthManager getInstance() {
    if (instance == null) instance = new AuthManager();
    return instance;
  }

  public boolean isSignedIn() {
    return mFirebaseAuth.getCurrentUser() != null;
  }

  public String getCurrentUserId() {
    FirebaseUser user = mFirebaseAuth.getCurrentUser();
    if (user == null) return null;
    return user.getUid();
  }

  public void signInWithEmailAndPassword(String email, String password, FirebaseAuthListener listener) {
    mFirebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
      if (task.isSuccessful())
        listener.onAuthSuccess(task);
      else
        listener.onAuthFailure(task);
    });
  }

  public void createUserWithEmailAndPassword(String email, String password, FirebaseAuthListener listener) {
    mFirebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
      if (task.isSuccessful())
        listener.onAuthSuccess(task);
      else
        listener.onAuthFailure(task);
    });
  }

  public void signInWithGoogleAccount(GoogleSignInAccount account, FirebaseAuthListener listener) {
    AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
    mFirebaseAuth.signInWithCredential(credential).addOnCompleteListener(task -> {
      if (task.isSuccessful())
        listener.onAuthSuccess(task);
      else
        listener.onAuthFailure(task);
    });
  }

  public void signOut() {
    mFirebaseAuth.signOut();
    mCurrentUser = null;
  }

  public interface FirebaseAuthListener {
    void onAuthSuccess(Task<AuthResult> task);
    void onAuthFailure(Task<AuthResult> task);
  }

}
