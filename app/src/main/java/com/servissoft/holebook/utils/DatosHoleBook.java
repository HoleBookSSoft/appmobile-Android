package com.servissoft.holebook.utils;

import java.util.Arrays;

/**
 * Created by raulandrez on 10/11/15.
 */
public class DatosHoleBook {

    double[] data;
    int size;

    public DatosHoleBook(double data,int size )
    {
        this.data=new double[size];

        this.data[0] = data;
        this.size =size;
    }

    public void setDato(double dato, int i){
        this.data[i%size]=dato;
    }

    double getMean()
    {

        double sum = 0.0;
        for(double a : data)
            sum += a;
        return sum/size;
    }

    double getVariance()
    {
        double mean = getMean();
        double temp = 0;
        for(double a :data)
            temp += (mean-a)*(mean-a);
        return temp/size;
    }


    public double getPromedio()
    {
        Arrays.sort(data);

        if (data.length % 2 == 0)
        {
            return (data[(data.length / 2) - 1] + data[data.length / 2]) / 2.0;
        }
        else
        {
            return data[data.length / 2];
        }
    }
}
