package com.example.projeto_gradesync;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.AlphaAnimation;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.Calendar;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Força o aplicativo a rodar sempre no Modo Claro (Light Mode)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Esconde a ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // 1. Vínculo do TextView de Copyright e atualização do ano dinâmico
        TextView tvCopyright = findViewById(R.id.tv_copyright);
        if (tvCopyright != null) {
            int anoAtual = Calendar.getInstance().get(Calendar.YEAR);
            tvCopyright.setText("Copyright © " + anoAtual);
        }

        // Configura animação de fade-in para o container do logo
        View logoContainer = findViewById(R.id.logo_container);
        AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setDuration(1500);
        logoContainer.startAnimation(fadeIn);

        // Handler para transição após 3 segundos
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Fecha a Splash para não voltar ao clicar em 'Back'
        }, 3000);
    }
}
