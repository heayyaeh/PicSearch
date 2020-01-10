package com.tp1.picsearch;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class MessageSender extends Activity {

    EditText mobileno, message, email;
    Button sendsms, sendemail, savePicture;
    ImageView picture, picture1, picture2, picture3;
    Bitmap bitmap;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_picture);

        //extraction de l'intent
        Intent i = getIntent();
        name = i.getStringExtra("site");

        Integer drawable1 = i.getIntExtra("Picture1",0);
        Integer drawable2 = i.getIntExtra("Picture2",0);
        Integer drawable3 = i.getIntExtra("Picture3",0);

        picture1 = findViewById(R.id.pictureTosend);
        picture1.setImageResource(drawable1);
        picture2 = findViewById(R.id.pictureTosend2);
        picture2.setImageResource(drawable2);
        picture3 = findViewById(R.id.pictureTosend3);
        picture3.setImageResource(drawable3);

        picture = picture1; //Par défaut afin que l'enregistrement n'échoue pas

        //appel des objets
        mobileno =  findViewById(R.id.editText1);
        message =  findViewById(R.id.editText2);
        email =  findViewById(R.id.editText3);
        sendsms = findViewById(R.id.button1);
        sendemail = findViewById(R.id.button2);
        savePicture = findViewById(R.id.Store);

        //Choix de la photo à envoyer


        picture1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                picture = picture1;
                Toast.makeText(MessageSender.this, "Picture 1 selected", Toast.LENGTH_SHORT).show();
            }
        });

        picture2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                picture = picture2;
                Toast.makeText(MessageSender.this, "Picture 2 selected", Toast.LENGTH_SHORT).show();
            }
        });

        picture3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                picture = picture3;
                Toast.makeText(MessageSender.this, "Picture 3 selected", Toast.LENGTH_SHORT).show();
            }
        });

        //enregistrement de la photo. Elle permet de l'enregistrer dans la gallerie.

        savePicture.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View blop){
                String filename= SavePic(picture);

                Toast.makeText(MessageSender.this, "Image Saved Successfully", Toast.LENGTH_LONG).show();
                /*
                //actualise la gallerie
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                intent.setData(Uri.fromFile(outFile));
                sendBroadcast(intent);
                */
            }
        });





        //envoie par sms
        sendsms.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String fileName= SavePic(picture);
                File storageLoc = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

                String no = mobileno.getText().toString();
                String msg = message.getText().toString();

                SmsManager sms = SmsManager.getDefault();
                //sms.sendTextMessage(no, null, msg, null, null);

                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.setClassName("com.android.mms", "com.android.mms.ui.ComposeMessageActivity");
                sendIntent.putExtra("sms_body", msg);
                sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(storageLoc + fileName));
                sendIntent.putExtra("address", no);;
                sendIntent.setType("image/png");


                startActivity(sendIntent);
                startActivity(Intent.createChooser(sendIntent, "Send"));

                Toast.makeText(getApplicationContext(), "Message Sent successfully!",
                        Toast.LENGTH_LONG).show();
            }
        });



        //envoie par email
        sendemail.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //enregistre la photo avec méthode savepic
                String filename= SavePic(picture);


                String adresse = email.getText().toString();
                String objet = "A Pic from Pic Search! just arrived !";
                String msg = message.getText().toString();

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_EMAIL,new String[]{adresse});
                intent.putExtra(Intent.EXTRA_SUBJECT, objet);
                intent.putExtra(Intent.EXTRA_TEXT, msg);


                intent.setType("message/rfc822");
                startActivity(Intent.createChooser(intent, "Select Email App"));
            }
        });

    }

    //méthode pour enregistrer une photo, réutilisée dans envoie par sms, email, et store. elle renvoie le nom du fichier enregistrée
    public String SavePic(ImageView Pic){
        Pic.buildDrawingCache();
        bitmap = Pic.getDrawingCache();
        FileOutputStream outStream = null;
        File storageLoc = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String uniqueName = name + System.currentTimeMillis() + ".jpg";
        //String fileName = String.format("%d.png",uniqueName);
        File outFile = new File(storageLoc, uniqueName);

        try {
            outStream = new FileOutputStream(outFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);

        try {
            outStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, name, "test");
        return uniqueName;

    }
}



