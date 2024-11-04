package com.example.akasztofa;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private ImageView akasztofaImageView;
    private TextView kitalalandoSzoTextView, aktualisBetuTextView;
    private Button elozoBetuButton, tippButton, kovetkezoBetuButton;

    private String[] szavak = {"ALMA", "BANAN", "CITROM", "DIKTAFON", "ELEFANT", "FAHO", "GOMB", "HALO", "ISKOLA", "JAVASLAT"};
    private String kitalalandoSzo;
    private Set<Character> tippeltBetuk = new HashSet<>();
    private int rosszTippekSzama = 0;
    private char aktualisBetu = 'A';

    private int[] akasztofaImages = {
            R.drawable.akasztofa00, R.drawable.akasztofa01, R.drawable.akasztofa02,
            R.drawable.akasztofa03, R.drawable.akasztofa04, R.drawable.akasztofa05,
            R.drawable.akasztofa06, R.drawable.akasztofa07, R.drawable.akasztofa08,
            R.drawable.akasztofa09, R.drawable.akasztofa10, R.drawable.akasztofa11,
            R.drawable.akasztofa12, R.drawable.akasztofa13
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        akasztofaImageView = findViewById(R.id.akasztofaImageView);
        kitalalandoSzoTextView = findViewById(R.id.kitalalandoSzoTextView);
        aktualisBetuTextView = findViewById(R.id.aktualisBetuTextView);
        elozoBetuButton = findViewById(R.id.elozoBetuButton);
        tippButton = findViewById(R.id.tippButton);
        kovetkezoBetuButton = findViewById(R.id.kovetkezoBetuButton);

        ujJatek();

        elozoBetuButton.setOnClickListener(view -> lapozBetu(false));
        kovetkezoBetuButton.setOnClickListener(view -> lapozBetu(true));
        tippButton.setOnClickListener(view -> tippel());
    }

    private void ujJatek() {
        Random random = new Random();
        kitalalandoSzo = szavak[random.nextInt(szavak.length)];
        kitalalandoSzoTextView.setText(ujRejtettSzo(kitalalandoSzo));
        rosszTippekSzama = 0;
        tippeltBetuk.clear();
        aktualisBetu = 'A';
        aktualisBetuTextView.setText(String.valueOf(aktualisBetu));
        akasztofaImageView.setImageResource(akasztofaImages[0]);
    }

    private String ujRejtettSzo(String szo) {
        StringBuilder rejtett = new StringBuilder();
        for (int i = 0; i < szo.length(); i++) {
            rejtett.append('_');
        }
        return rejtett.toString();
    }

    private void lapozBetu(boolean kovetkezo) {
        aktualisBetu = (char) ((kovetkezo ? aktualisBetu + 1 : aktualisBetu - 1) % 26 + 'A');
        aktualisBetuTextView.setText(String.valueOf(aktualisBetu));
    }

    private void tippel() {
        if (tippeltBetuk.contains(aktualisBetu)) {
            Toast.makeText(this, "Már tippelted ezt a betűt!", Toast.LENGTH_SHORT).show();
            return;
        }
        tippeltBetuk.add(aktualisBetu);
        if (kitalalandoSzo.contains(String.valueOf(aktualisBetu))) {
            Toast.makeText(this, "Helyes tipp!", Toast.LENGTH_SHORT).show();
            frissitRejtettSzo();
        } else {
            Toast.makeText(this, "Rossz tipp!", Toast.LENGTH_SHORT).show();
            rosszTippekSzama++;
            if (rosszTippekSzama < akasztofaImages.length) {
                akasztofaImageView.setImageResource(akasztofaImages[rosszTippekSzama]);
            } else {
                vege(false);
            }
        }
    }

    private void frissitRejtettSzo() {
        StringBuilder aktualisSzoveg = new StringBuilder(kitalalandoSzoTextView.getText());
        for (int i = 0; i < kitalalandoSzo.length(); i++) {
            if (kitalalandoSzo.charAt(i) == aktualisBetu) {
                aktualisSzoveg.setCharAt(i, aktualisBetu);
            }
        }
        kitalalandoSzoTextView.setText(aktualisSzoveg);
        if (!aktualisSzoveg.toString().contains("_")) {
            vege(true);
        }
    }

    private void vege(boolean nyert) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(nyert ? "Gratulálok, nyertél!" : "Vesztettél!")
                .setMessage(nyert ? "Sikerült kitalálnod a szót!" : "A szó: " + kitalalandoSzo)
                .setPositiveButton("Új játék", (dialog, id) -> ujJatek())
                .setNegativeButton("Kilépés", (dialog, id) -> finish())
                .show();
    }
}