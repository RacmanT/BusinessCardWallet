package it.units.businesscardwallet.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class DatabaseUtils {

    public static FirebaseAuth AUTH = FirebaseAuth.getInstance();
    public static FirebaseFirestore DATABASE = FirebaseFirestore.getInstance();

    public static  DocumentReference userRef;
    public static  CollectionReference contactsRef;

    public static void init() {
        if(AUTH.getCurrentUser() != null){
             userRef = DATABASE.collection("users").document(AUTH.getCurrentUser().getUid());
             contactsRef = userRef.collection("contacts");
        }
    }

    public static boolean userIsNotLogged(){
        return AUTH.getCurrentUser() == null;
    }

}
