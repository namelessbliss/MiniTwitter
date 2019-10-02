package com.app.nb.minitwitter.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import com.app.nb.minitwitter.R;
import com.app.nb.minitwitter.common.Constants;
import com.app.nb.minitwitter.common.SharedPreferencesManager;
import com.app.nb.minitwitter.data.TweetViewModel;
import com.bumptech.glide.Glide;

public class NuevoTweetDialogFragment extends DialogFragment implements View.OnClickListener {

    private ImageView ivClose, ivAvatar;
    private Button btnEnviar;
    private EditText etMensaje;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Aplica estilo para dialogo de pantalla completa
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.nevo_tweet_dialog, container, false);

        ivAvatar = view.findViewById(R.id.imageViewAvatar);
        ivClose = view.findViewById(R.id.imageViewClose);
        btnEnviar = view.findViewById(R.id.buttonEnviar);
        etMensaje = view.findViewById(R.id.editTextMensaje);

        //Eventos
        btnEnviar.setOnClickListener(this);
        ivClose.setOnClickListener(this);

        //Set imagen del usuario
        String photoUrl = SharedPreferencesManager.getStringValue(Constants.PREF_PHOTO_URL);

        if (!photoUrl.isEmpty()) {
            Glide.with(getActivity())
                    .load(Constants.API_MINITWIITER_FILES_URL + photoUrl)
                    .into(ivAvatar);
        }
        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        String mensaje = etMensaje.getText().toString();

        if (id == R.id.buttonEnviar) {
            if (mensaje.isEmpty()) {
                Toast.makeText(getActivity(), "El Tweet no puede estar vacío", Toast.LENGTH_SHORT).show();
            } else {
                TweetViewModel tweetViewModel = ViewModelProviders.of(getActivity()).get(TweetViewModel.class);
                tweetViewModel.createTweet(mensaje);
                getDialog().dismiss();
            }
        } else if (id == R.id.imageViewClose) {
            //Cerra dialogo
            if (!mensaje.isEmpty())
                showDialogConfirm();
            else
                getDialog().dismiss();
        }
    }

    private void showDialogConfirm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage("¿Desea eliminar el tweet? El mensaje será eliminado")
                .setTitle("Salir");

        builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                getDialog().dismiss();

            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog dialog = builder.create();

        dialog.show();
    }
}
