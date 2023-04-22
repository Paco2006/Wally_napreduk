package com.prilojenie.wally;

import android.app.Activity;
import android.app.AppComponentFactory;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.slider.RangeSlider;

import java.io.OutputStream;

import petrov.kristiyan.colorpicker.ColorPicker;

public class Paint extends Fragment
{

    private DrawView paint;
    private ImageButton save, color, stroke, undo;
    private RangeSlider rangeSlider;

    Activity activity;

    View parentHolder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        activity = getActivity();
        parentHolder = inflater.inflate(R.layout.paint, container, false);
        super.onCreate(savedInstanceState);
//        activity.setContentView(R.layout.paint);

        paint = (DrawView) parentHolder.findViewById(R.id.draw_view);
        rangeSlider = (RangeSlider) parentHolder.findViewById(R.id.rangebar);
        undo = (ImageButton) parentHolder.findViewById(R.id.btn_undo);
        save = (ImageButton) parentHolder.findViewById(R.id.btn_save);
        color = (ImageButton) parentHolder.findViewById(R.id.btn_color);
        stroke = (ImageButton) parentHolder.findViewById(R.id.btn_stroke);

        undo.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view)
            {

                paint.undo();

            }

        });

        save.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view)
            {

                Bitmap bmp = paint.save();
                OutputStream imageOutStream = null;
                ContentValues cv = new ContentValues();

                cv.put(MediaStore.Images.Media.DISPLAY_NAME, "drawing.png");
                cv.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
                cv.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);

                Uri uri = activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);

                try {

                    imageOutStream = activity.getContentResolver().openOutputStream(uri);
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, imageOutStream);
                    imageOutStream.close();

                } catch (Exception e)
                {

                    e.printStackTrace();

                }

            }

        });

        color.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view)
            {

                final ColorPicker colorPicker = new ColorPicker(activity);
                colorPicker.setOnFastChooseColorListener(new ColorPicker.OnFastChooseColorListener()
                        {

                            @Override
                            public void setOnFastChooseColorListener(int position, int color)
                            {

                                paint.setColor(color);

                            }

                            @Override
                            public void onCancel()
                            {

                                colorPicker.dismissDialog();

                            }

                        })

                        .setColumns(5)
                        .setDefaultColorButton(Color.parseColor("#000000"))
                        .show();

            }

        });

        stroke.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view)
            {

                if (rangeSlider.getVisibility() == View.VISIBLE)
                    rangeSlider.setVisibility(View.GONE);
                else
                    rangeSlider.setVisibility(View.VISIBLE);

            }

        });

        rangeSlider.setValueFrom(0.0f);
        rangeSlider.setValueTo(100.0f);
        rangeSlider.addOnChangeListener(new RangeSlider.OnChangeListener()
        {

            @Override
            public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser)
            {

                paint.setStrokeWidth((int) value);

            }

        });

        ViewTreeObserver vto = paint.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
        {

            @Override
            public void onGlobalLayout()
            {

                paint.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int width = paint.getMeasuredWidth();
                int height = paint.getMeasuredHeight();
                paint.init(height, width);

            }

        });
        return parentHolder;
    }

}
