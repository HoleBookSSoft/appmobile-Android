package com.servissoft.holebook.entidades;

import java.util.Date;

/**
 * Created by iproject on 26/02/15.
 */
public class Punto {


    private Integer puntoId;
    private Integer rutaId;
    private Double x;
    private Double y;
    private Date t;
    private Double ax;
    private Double ay;
    private int  hueco;
    private Double az;
    private Date fechaRegistro;


    public Punto() {
    }

    public Integer getPuntoId() {
        return puntoId;
    }

    public void setPuntoId(Integer puntoId) {
        this.puntoId = puntoId;
    }

    public Integer getRutaId() {
        return rutaId;
    }

    public void setRutaId(Integer rutaId) {
        this.rutaId = rutaId;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public Date getT() {
        return t;
    }

    public void setT(Date t) {
        this.t = t;
    }

    public Double getAx() {
        return ax;
    }

    public void setAx(Double ax) {
        this.ax = ax;
    }

    public Double getAy() {
        return ay;
    }

    public void setHueco(int hueco) {
        this.hueco = hueco;
    }
    public int getHueco() {
        return hueco;
    }

    public void setAy(Double ay) {
        this.ay = ay;
    }

    public Double getAz() {
        return az;
    }

    public void setAz(Double az) {
        this.az = az;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}

