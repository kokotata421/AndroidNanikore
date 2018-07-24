package com.kotakawanishi.nanikore;

/**
 * Created by kota on 2017/05/11.
 */

public class Scale {
    double xScale;
    double yScale;
    double widthScale;
    double heightScale;

    Scale(double xScale, double yScale, double widthScale, double heightScale){
        this.xScale = xScale;
        this.yScale = yScale;
        this.widthScale = widthScale;
        this.heightScale = heightScale;
    }


    Scale(double widthScale, double heightScale){
        this.xScale = 0.0;
        this.yScale = 0.0;
        this.widthScale = widthScale;
        this.heightScale = heightScale;
    }
}
