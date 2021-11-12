package it.units.businesscardwallet.fragments;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONObject;

import java.util.Collections;
import java.util.stream.IntStream;

import it.units.businesscardwallet.R;
import it.units.businesscardwallet.entities.Contact;


public class BusinessCard extends Fragment {


    private static final String ARG_PARAM_CONTACT = "ARG_PARAM_CONTACT";

    private Contact contact; //= new Contact("Patrick", "Bateman", "Vice President", "patrick.bateman@company.com", 343988666, "55 West 81st Street, Upper West Side");

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
            // TODO check why width is zero
            int width = ((ImageView)view.findViewById(R.id.qr_code)).getMeasuredWidth();
            int height = view.findViewById(R.id.qr_code).getHeight();

            String json = new Gson().toJson(contact);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.encodeBitmap(json, BarcodeFormat.QR_CODE, 300, 300);

           // Bitmap bitmap = encodeAsBitmap(this.contact, 300, 300);

            ((ImageView) view.findViewById(R.id.qr_code)).setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        return view;
    }

    // https://stackoverflow.com/questions/28232116/android-using-zxing-generate-qr-code
   /* @Nullable
    private Bitmap encodeAsBitmap(Contact contact, int width, int height) throws WriterException {
        String json = new Gson().toJson(contact);

        final BitMatrix bitMatrix = new MultiFormatWriter().encode(json, BarcodeFormat.QR_CODE, width, height, Collections.singletonMap(EncodeHintType.CHARACTER_SET, "utf-8"));
        return Bitmap.createBitmap(IntStream.range(0, width)
                        .flatMap(h -> IntStream.range(0, height)
                                .map(w -> bitMatrix.get(w, h) ? Color.BLACK : Color.WHITE)).toArray(),
                width, height, Bitmap.Config.ARGB_8888);

        *//*
        BitMatrix result;
        result = new MultiFormatWriter().encode(json, BarcodeFormat.QR_CODE, 300, 300, null); //500 500

        int[] pixels = new int[result.getWidth() * result.getHeight()];
        for (int y = 0; y < result.getHeight(); y++) {
            int offset = y * result.getWidth();
            for (int x = 0; x < result.getWidth(); x++) {
                pixels[offset + x] = result.get(x, y) ? ContextCompat.getColor(getActivity(), R.color.black) : ContextCompat.getColor(getActivity(), R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(result.getWidth(), result.getHeight(), Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, result.getWidth(), 0, 0, result.getWidth(), result.getHeight());
        return bitmap;*//*

    }*/
}