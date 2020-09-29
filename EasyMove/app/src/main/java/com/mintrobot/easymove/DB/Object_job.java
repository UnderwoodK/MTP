package com.mintrobot.easymove.DB;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "jobs")
public class Object_job {

    @PrimaryKey
    int jobNo;
    String jobName;
    byte moveMethod;
    double j1, j2, j3, j4, j5, j6,
            x, y, z, Rx, Ry, Rz, vel, acc1, acc2, io;

    public Object_job(int jobNo, String jobName, byte moveMethod,
                      double j1, double j2, double j3, double j4, double j5, double j6,
                      double x, double y, double z, double Rx, double Ry, double Rz, double vel, double acc1, double acc2, double io) {

        this.jobNo = jobNo;
        this.jobName = jobName;
        this.moveMethod = moveMethod;
        this.j1 = j1;
        this.j2 = j2;
        this.j3 = j3;
        this.j4 = j4;
        this.j5 = j5;
        this.j6 = j6;
        this.x = x;
        this.y = y;
        this.z = z;
        this.Rx = Rx;
        this.Ry = Ry;
        this.Rz = Rz;
        this.vel = vel;
        this.acc1 = acc1;
        this.acc2 = acc2;
        this.io = io;
    }

    public int getJobNo() {
        return jobNo;
    }

    public void setJobNo(int jobNo) {
        this.jobNo = jobNo;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public byte getMoveMethod() {
        return moveMethod;
    }

    public void setMoveMethod(byte moveMethod) {
        this.moveMethod = moveMethod;
    }

    public double getJ1() {
        return j1;
    }

    public void setJ1(double j1) {
        this.j1 = j1;
    }

    public double getJ2() {
        return j2;
    }

    public void setJ2(double j2) {
        this.j2 = j2;
    }

    public double getJ3() {
        return j3;
    }

    public void setJ3(double j3) {
        this.j3 = j3;
    }

    public double getJ4() {
        return j4;
    }

    public void setJ4(double j4) {
        this.j4 = j4;
    }

    public double getJ5() {
        return j5;
    }

    public void setJ5(double j5) {
        this.j5 = j5;
    }

    public double getJ6() {
        return j6;
    }

    public void setJ6(double j6) {
        this.j6 = j6;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public double getRx() {
        return Rx;
    }

    public void setRx(double rx) {
        Rx = rx;
    }

    public double getRy() {
        return Ry;
    }

    public void setRy(double ry) {
        Ry = ry;
    }

    public double getRz() {
        return Rz;
    }

    public void setRz(double rz) {
        Rz = rz;
    }

    public double getVel() {
        return vel;
    }

    public void setVel(double vel) {
        this.vel = vel;
    }

    public double getAcc1() {
        return acc1;
    }

    public void setAcc1(double acc1) {
        this.acc1 = acc1;
    }

    public double getAcc2() {
        return acc2;
    }

    public void setAcc2(double acc2) {
        this.acc2 = acc2;
    }

    public double getIo() {
        return io;
    }

    public void setIo(double io) {
        this.io = io;
    }
}
