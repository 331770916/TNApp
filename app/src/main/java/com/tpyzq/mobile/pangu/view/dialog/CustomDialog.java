package com.tpyzq.mobile.pangu.view.dialog;


import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;


public class CustomDialog extends Dialog {

    public CustomDialog(Context context) {
        super(context);
    }

    public CustomDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;
        private String title;
        public CustomDialog dialog;
        private TextView mText;

        public Builder(Context context) {
            this.context = context;
        }


        /**
         * Set the Dialog title from String
         *
         * @param title
         * @return
         */
        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public CustomDialog create() {
            if (dialog == null) {
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                // instantiate the dialog with the custom Theme
                dialog = new CustomDialog(context, R.style.loading_dialog);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(true);
                View layout = inflater.inflate(R.layout.loading_dialog_view, null);

                dialog.addContentView(layout, new LayoutParams(
                        LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
                // set the dialog title
                mText = (TextView) layout.findViewById(R.id.loading_text);

            }
            if (mText!=null){
                mText.setText(title);
            }

            return dialog;
        }
    }
}
