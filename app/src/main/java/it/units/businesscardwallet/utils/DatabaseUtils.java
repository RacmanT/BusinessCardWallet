package it.units.businesscardwallet.utils;

import android.annotation.SuppressLint;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

@SuppressWarnings("ConstantConditions")
@SuppressLint("StaticFieldLeak")
public abstract class DatabaseUtils {

    private static final FirebaseAuth AUTH = FirebaseAuth.getInstance();
    private static final FirebaseFirestore DATABASE = FirebaseFirestore.getInstance();

    public static DocumentReference userRef;
    public static CollectionReference contactsRef;

    public static void init() {
        if (!userIsNotLogged()) {
            userRef = DATABASE.collection("users").document(AUTH.getCurrentUser().getUid());
            contactsRef = userRef.collection("contacts");
        }
    }

    public static boolean userIsNotLogged() {
        return AUTH.getCurrentUser() == null;
    }

    public static void signOut(){
        AUTH.signOut();
    }

    public static FirebaseUser getUser() {
        return AUTH.getCurrentUser();
    }



    public static void deleteAccount(){
        AUTH.signOut();
        //AUTH.getCurrentUser().delete().isComplete();

    }

}
