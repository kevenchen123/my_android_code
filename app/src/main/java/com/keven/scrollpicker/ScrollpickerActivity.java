package com.keven.scrollpicker;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

import com.keven.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public class ScrollpickerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_picker);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        testScroll();
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private BitmapScrollPicker mPickerHorizontalImage;
    private StringScrollPicker mPickerHorizontalString;

    private void testScroll() {
        List<String> list = Arrays.asList(new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"});
        List<CharSequence> newList = new ArrayList<>();
        for (String s : list) {
            s = "No." + "<br/>" + "<font color='#ff0000'>" + s + "</font>";
            newList.add(Html.fromHtml(s));
        }

        final CopyOnWriteArrayList<Bitmap> bitmaps = new CopyOnWriteArrayList<Bitmap>();
        bitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.slot_01));
        bitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.slot_02));
        bitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.slot_03));
        bitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.slot_04));
        bitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.slot_05));
        bitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.slot_06));
        bitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.slot_07));

        //--------------------------

        mPickerHorizontalString = (StringScrollPicker) findViewById(R.id.picker_01_horizontal);
        mPickerHorizontalString.setData(newList);
        mPickerHorizontalString.setOnSelectedListener(new ScrollPickerView.OnSelectedListener() {
            @Override
            public void onSelected(ScrollPickerView scrollPickerView, int position) {
                Toast.makeText(ScrollpickerActivity.this, "" + (position + 1), Toast.LENGTH_SHORT).show();
            }
        });

        mPickerHorizontalImage = (BitmapScrollPicker) findViewById(R.id.picker_02_horizontal);
        mPickerHorizontalImage.setData(bitmaps);

        //--------------------------

        StringScrollPicker2 stringScrollPicker = (StringScrollPicker2) findViewById(R.id.picker_03_horizontal);
        stringScrollPicker.setHorizontal(true);
        stringScrollPicker.setVisibleItemCount(5);//可见5个  第3个  （3-1）个位中间
        stringScrollPicker.setCenterPosition(2);
        stringScrollPicker.setIsCirculation(false);
        stringScrollPicker.setCanTap(true);
        stringScrollPicker.setDisallowInterceptTouch(true);
        stringScrollPicker.setData(newList);
        stringScrollPicker.setOnSelectedListener(new ScrollPickerView2.OnSelectedListener() {
            @Override
            public void onSelected(ScrollPickerView2 scrollPickerView, final int position) {
                Log.e("TAG", ">>>>>>>1>>position=" + position);
            }
        });
        stringScrollPicker.setOnSelectedListener(new StringScrollPicker2.OnDataSelectedListener() {
            @Override
            public void DataSelected(CharSequence data) {
                Log.e("TAG", ">>>>>>>2>>position=" + data);
            }
        });

        BitmapScrollPicker2 bitmapScrollPicker = (BitmapScrollPicker2) findViewById(R.id.picker_04_horizontal);
        bitmapScrollPicker.setHorizontal(true);
        bitmapScrollPicker.setVisibleItemCount(5);
        bitmapScrollPicker.setCenterPosition(2);
        bitmapScrollPicker.setIsCirculation(false);
        bitmapScrollPicker.setCanTap(true);
        bitmapScrollPicker.setDisallowInterceptTouch(true);
        bitmapScrollPicker.setData(bitmaps);
        bitmapScrollPicker.setOnSelectedListener(new ScrollPickerView2.OnSelectedListener() {
            @Override
            public void onSelected(ScrollPickerView2 scrollPickerView, final int position) {
            }
        });
    }
}