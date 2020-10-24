package project.mapobed.webhut.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.textfield.TextInputLayout;

import es.dmoral.toasty.Toasty;
import project.mapobed.webhut.R;

public class BookmarkDialog extends AppCompatDialogFragment {
    String link,name;
    public BookmarkDialog(String link,String name){
        this.link=link;
        this.name=name;
    }

    private TextInputLayout book_name;
    private TextInputLayout book_link;
    private ExampleDialogListener listener;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.bookmark_dialogue, null);
        builder.setTitle(getString(R.string.bookmark));
        builder.setIcon(R.drawable.ic_launcher);
        builder.setMessage(R.string.add_url_as_bookmark);


        builder.setView(view)
                .setTitle(getString(R.string.bookmark))
                .setNeutralButton(
                        R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                Toasty.error(getContext(), R.string.url_wasnt_saved_in_bookmark, Toast.LENGTH_SHORT).show();
                            }
                        })
                .setPositiveButton(getString(R.string.add), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String name = book_name.getEditText().getText().toString();
                        String link = book_link.getEditText().getText().toString();
                        listener.applyTexts(name,link);
                    }
                })
        .setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Toasty.error(getContext(), R.string.url_wasnt_saved_in_bookmark, Toast.LENGTH_SHORT).show();

            }
        });
        book_name = view.findViewById(R.id.book_name);
        book_link = view.findViewById(R.id.book_link);
        book_link.getEditText().setText(link);
        book_name.getEditText().setText(name);
        builder.setCancelable(true);
        return builder.create();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (ExampleDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ExampleDialogListener");
        }
    }
    public interface ExampleDialogListener {
        void applyTexts(String username, String password);
    }
}
