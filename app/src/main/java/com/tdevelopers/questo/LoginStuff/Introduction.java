package com.tdevelopers.questo.LoginStuff;

import android.os.Bundle;

import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;
import com.tdevelopers.questo.R;

public class Introduction extends IntroActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(new SimpleSlide.Builder()
                .title(R.string.title_material_metaphor)
                .description(R.string.description_material_metaphor)
                .image(R.drawable.question)
                .background(R.color.color_material_shadow)
                .backgroundDark(R.color.color_dark_material_shadow)
                .build());

        addSlide(new SimpleSlide.Builder()
                .title(R.string.title_material_bold)
                .description(R.string.description_material_bold)
                .image(R.drawable.badge)
                .background(R.color.color_material_metaphor)
                .backgroundDark(R.color.color_dark_material_metaphor)
                .build());

        addSlide(new SimpleSlide.Builder()
                .title(R.string.title_material_motion)
                .description(R.string.description_material_motion)
                .image(R.drawable.articlenew)
                .background(R.color.color_material_motion)
                .backgroundDark(R.color.color_dark_material_motion)
                .build());

        addSlide(new SimpleSlide.Builder()
                .title(R.string.title_material_shadow)
                .description(R.string.description_material_shadow)
                .image(R.drawable.chatillu)
                .background(R.color.greenintrolite)
                .backgroundDark(R.color.greenintrodark)
                .build());

        addSlide(new SimpleSlide.Builder()
                .title("SaiTej Dandge")
                .description("Developer and Designer of Questo\nGoogle Facilitator, Student at CBIT, Hyderabad")
                .image(R.drawable.intropic)
                .background(R.color.color_material_motion)
                .backgroundDark(R.color.color_dark_material_motion)
                .build());


    }
}
