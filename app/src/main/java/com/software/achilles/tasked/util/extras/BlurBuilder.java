package com.software.achilles.tasked.util.extras;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.view.View;

// On create

//    FloatingActionMenu fam = (FloatingActionMenu) findViewById(R.id.menuFAB);
//
//    fam.setOnMenuButtonClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            blurContent();
//    //      fam.onclick();
//        }
//    });

// Method

//private void blurContent(){
//    final View content = this.findViewById(R.id.coordinatorDashboard);
//    final FloatingActionMenu fam = (FloatingActionMenu) findViewById(R.id.menuFAB);
//
//    if (content.getWidth() > 0) {
//        Bitmap image = BlurBuilder.blur(content);
//        fam.setBackground(new BitmapDrawable(this.getResources(), image));
//    } else {
//        content.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                    Bitmap image = BlurBuilder.blur(content);
//                    fam.setBackground(new BitmapDrawable(getResources(), image));
//                }
//        });
//    }
//}

// REVIEW Implement?
public class BlurBuilder {
    private static final float BITMAP_SCALE = 0.4f;
    private static final float BLUR_RADIUS = 7.5f;

    public static Bitmap blur(View v) {
        return blur(v.getContext(), getScreenshot(v));
    }

    public static Bitmap blur(Context ctx, Bitmap image) {
        int width = Math.round(image.getWidth() * BITMAP_SCALE);
        int height = Math.round(image.getHeight() * BITMAP_SCALE);

        Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

        RenderScript rs = RenderScript.create(ctx);
        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
        theIntrinsic.setRadius(BLUR_RADIUS);
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);

        return outputBitmap;
    }

    private static Bitmap getScreenshot(View v) {
        Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);
        return b;
    }
}
