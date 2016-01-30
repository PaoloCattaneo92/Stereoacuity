package utils;

import android.app.Activity;
import android.app.DialogFragment;

/**
 * Created by paolo on 23/11/2015.
 * Auxiliary class for showing Settings dialogs
 */
public class NoticeDialogFragment extends DialogFragment {

    public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClicK(DialogFragment dialog);
    }

    NoticeDialogListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (NoticeDialogListener) activity;
        } catch(ClassCastException e) {
            throw new ClassCastException(activity.toString()+" must implement NoticeDialogListener");
        }
    }
}
