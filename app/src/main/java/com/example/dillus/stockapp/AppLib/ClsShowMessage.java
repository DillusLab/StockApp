package com.example.dillus.stockapp.AppLib;

import static com.example.dillus.stockapp.AppLib.ClsMessage.msgEmailExist;
import static com.example.dillus.stockapp.AppLib.ClsMessage.msgNetworkError;
import static com.example.dillus.stockapp.AppLib.ClsMessage.msgNoPasswordExits;
import static com.example.dillus.stockapp.AppLib.ClsMessage.msgNoUserExits;
import static com.example.dillus.stockapp.AppLib.ClsMessageErrorFirebase.msgErrorFirebase_EmailExists;
import static com.example.dillus.stockapp.AppLib.ClsMessageErrorFirebase.msgErrorFirebase_NetworkError;
import static com.example.dillus.stockapp.AppLib.ClsMessageErrorFirebase.msgErrorFirebase_NoPasswordExits;
import static com.example.dillus.stockapp.AppLib.ClsMessageErrorFirebase.msgErrorFirebase_NoUserExits;

import android.content.Context;
import android.widget.Toast;

public class ClsShowMessage {

    public ClsShowMessage() {
    }

    public void showMessageError(Context context, String msg) {
        if (msg.equals(msgErrorFirebase_NoPasswordExits))
            Toast.makeText(context, msgNoPasswordExits, Toast.LENGTH_SHORT).show();

        else if (msg.equals(msgErrorFirebase_NoUserExits))
            Toast.makeText(context, msgNoUserExits, Toast.LENGTH_SHORT).show();

        else if (msg.equals(msgErrorFirebase_NetworkError))
            Toast.makeText(context, msgNetworkError, Toast.LENGTH_SHORT).show();

        else if (msg.equals(msgErrorFirebase_EmailExists))
            Toast.makeText(context, msgEmailExist, Toast.LENGTH_SHORT).show();

        else
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
