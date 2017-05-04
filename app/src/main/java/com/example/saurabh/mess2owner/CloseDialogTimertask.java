package com.example.saurabh.mess2owner;

import android.support.v7.app.AlertDialog;

import java.util.TimerTask;

/**
 * Created by saurabh on 12/4/17.
 */
public class CloseDialogTimertask extends TimerTask {

    private AlertDialog dialog;

    public CloseDialogTimertask(AlertDialog dialog) {

        this.dialog=dialog;

    }


    @Override
    public void run() {
        if(dialog.isShowing())
        {
            dialog.dismiss();
        }
    }
}
