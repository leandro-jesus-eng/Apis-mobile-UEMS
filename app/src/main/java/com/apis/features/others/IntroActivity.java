package com.apis.features.others;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.apis.R;
import com.apis.features.lote_list.MainActivity;
import com.github.appintro.AppIntro;
import com.github.appintro.AppIntroFragment;

public class IntroActivity extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(AppIntroFragment.createInstance("Introdução", "O Ápis é um aplicativo Android para o monitoramento do comportamento de bovinos.", R.drawable.smart_cow, R.color.appintro_background_color));
        addSlide(AppIntroFragment.createInstance("Comportamentos", "Adicione comportamentos personalizados em suas observações. Eles podem ser alterados no menu do app.", R.drawable.intro_01, R.color.appintro_background_color));
        addSlide(AppIntroFragment.createInstance("Notificações", "Ative as notificações no menu Configurações. Elas irão te lembrar de atualizar o comportamento dos animais.", R.drawable.intro_02, R.color.appintro_background_color));
        addSlide(AppIntroFragment.createInstance("Dados", "Os dados referentes aos registros de comportamento permanecerão na pasta /apis/, na raiz do armazenamento.", R.drawable.intro_04, R.color.appintro_background_color));
        addSlide(AppIntroFragment.createInstance("Dados", "Você também pode exportar os dados individualmente nas telas \"Lista de Lotes\" e \"Lista de Animais\".", R.drawable.intro_03, R.color.appintro_background_color));
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);

        Intent intent = new Intent(
                IntroActivity.this, MainActivity.class
        );
        startActivity(intent);
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);

        Intent intent = new Intent(
                IntroActivity.this,MainActivity.class
        );
        startActivity(intent);
        finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }
}
