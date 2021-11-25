package it.units.businesscardwallet.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.print.PrintHelper;

import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;

import it.units.businesscardwallet.BuildConfig;
import it.units.businesscardwallet.R;
import it.units.businesscardwallet.activities.MainActivity;
import it.units.businesscardwallet.entities.Contact;
import it.units.businesscardwallet.utils.AESHelper;


public class BusinessCard extends Fragment {


    private static final String ARG_PARAM_CONTACT = "ARG_PARAM_CONTACT";

    private Contact contact;
    private Bitmap bitmap;
    private final Gson gson = new Gson();

    private View view;
    private TextView name, lastName, profession, phone, emailAddress, address;
    private ImageView phoneIcon, addressIcon, emailIcon;

    public BusinessCard() {
    }

    public static BusinessCard newInstance(Contact contact) {
        BusinessCard fragment = new BusinessCard();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM_CONTACT, contact);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true); // TODO if you want fragment to have menu
        // https://stackoverflow.com/questions/8308695/how-to-add-options-menu-to-fragment-in-android
        contact = (Contact) getArguments().getSerializable(ARG_PARAM_CONTACT); //https://stackoverflow.com/questions/17443081/fragment-initialization-with-complex-object
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // TODO https://stackoverflow.com/questions/25279715/android-how-to-add-icon-at-the-left-side-of-the-textview/25279726
        view = inflater.inflate(R.layout.fragment_business_card, container, false);

        name = view.findViewById(R.id.first_name);
        name.setText(this.contact.getName());

        lastName = view.findViewById(R.id.last_name);
        lastName.setText(this.contact.getLastName());

        profession = view.findViewById(R.id.profession);
        profession.setText(this.contact.getProfession());

        phone = view.findViewById(R.id.phone_number);
        phoneIcon = view.findViewById(R.id.phone_icon);

        if (this.contact.getPhoneNumber().isEmpty()) {
            phone.setVisibility(View.GONE);
            phoneIcon.setVisibility(View.GONE);
        } else {
            phone.setVisibility(View.VISIBLE);
            phoneIcon.setVisibility(View.VISIBLE);
            phone.setText(this.contact.getPhoneNumber());
        }

        emailAddress = view.findViewById(R.id.email_address);
        emailIcon = view.findViewById(R.id.email_icon);


        if (this.contact.getEmail().isEmpty()) {
            emailAddress.setVisibility(View.GONE);
            emailIcon.setVisibility(View.GONE);
        } else {
            emailAddress.setVisibility(View.VISIBLE);
            emailIcon.setVisibility(View.VISIBLE);
            emailAddress.setText(this.contact.getEmail());
        }


        address = view.findViewById(R.id.address);
        addressIcon = view.findViewById(R.id.location_icon);

        if (this.contact.getAddress().isEmpty()) {
            address.setVisibility(View.GONE);
            addressIcon.setVisibility(View.GONE);
        } else {
            address.setVisibility(View.VISIBLE);
            addressIcon.setVisibility(View.VISIBLE);
            address.setText(this.contact.getAddress());
        }


        try {
            String json = gson.toJson(this.contact);
            String encryptedJson = AESHelper.encrypt(json);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();

            int width = (int) getResources().getDimension(R.dimen.qr_code_width);
            int height = (int) getResources().getDimension(R.dimen.qr_code_height);
            bitmap = barcodeEncoder.encodeBitmap(encryptedJson, BarcodeFormat.QR_CODE, width, height); // 300, 300
            ImageView qrCode = view.findViewById(R.id.qr_code);
            qrCode.setImageBitmap(bitmap);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.business_card_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_print:
                printQrCode();
                break;
            case R.id.action_share:
                shareQrCode();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void shareQrCode() {
        // https://stackoverflow.com/questions/48117511/exposed-beyond-app-through-clipdata-item-geturi
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());


        File file = new File(getContext().getCacheDir(), contact.getName() + contact.getLastName() + "QrCode.png");
        file.setReadable(true, false);
        try (FileOutputStream fOut = new FileOutputStream(file)) {

            //Uri uri = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID+".provider", file);

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            Uri fileUri = FileProvider.getUriForFile(getContext(), "it.units.businesscardwallet.provider", file);
            final Intent intent = new Intent(android.content.Intent.ACTION_SEND)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    .putExtra(Intent.EXTRA_STREAM, fileUri)
                    .setType("image/png");

            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        file.deleteOnExit();
    }


    // https://developer.android.com/training/printing/photos
    private void printQrCode() {
        PrintHelper photoPrinter = new PrintHelper(getActivity()); //requireActivity()
        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
        photoPrinter.printBitmap("qrCode.jpg - test print", bitmap);
    }


}