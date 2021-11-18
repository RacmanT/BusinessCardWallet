package it.units.businesscardwallet.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.print.PrintHelper;

import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import it.units.businesscardwallet.R;
import it.units.businesscardwallet.entities.Contact;
import it.units.businesscardwallet.utils.AESHelper;


public class BusinessCard extends Fragment {


    private static final String ARG_PARAM_CONTACT = "ARG_PARAM_CONTACT";

    private Contact contact;
    private Bitmap bitmap;
    private final Gson gson = new Gson();

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

        View view = inflater.inflate(R.layout.fragment_business_card, container, false);
        ((TextView) view.findViewById(R.id.first_name)).setText(this.contact.getName());
        ((TextView) view.findViewById(R.id.last_name)).setText(this.contact.getLastName());
        ((TextView) view.findViewById(R.id.profession)).setText(this.contact.getProfession());
        ((TextView) view.findViewById(R.id.phone_number)).setText(String.valueOf(this.contact.getPhoneNumber()));
        ((TextView) view.findViewById(R.id.email_address)).setText(this.contact.getEmail());
        ((TextView) view.findViewById(R.id.address)).setText(this.contact.getAddress());


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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_print) {
            printQrCode();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }


    // https://developer.android.com/training/printing/photos
    private void printQrCode() {
        PrintHelper photoPrinter = new PrintHelper(getActivity()); //requireActivity()
        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
        photoPrinter.printBitmap("qrCode.jpg - test print", bitmap);
    }

}