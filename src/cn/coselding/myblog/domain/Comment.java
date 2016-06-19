package cn.coselding.myblog.domain;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 宇强 on 2016/3/11 0011.
 */
public class Comment {
    private int comid ;
    private String comcontent ;
    private int gid ;
    private int artid ;
    private Date comtime ;
    private String gname;
    private String comtimeshow;

    public String getGemail() {
        return gemail;
    }

    public void setGemail(String gemail) {
        this.gemail = gemail;
    }

    private String gemail;

    public String getComtimeshow() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        this.comtimeshow = format.format(this.comtime).replace(" ","T");
        return this.comtimeshow;
    }

    public void setComtimeshow(String comtimeshow) {
        this.comtimeshow = comtimeshow;
    }

    public String getGname() {
        return gname;
    }

    public void setGname(String gname) {
        this.gname = gname;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "comid=" + comid +
                ", comcontent='" + comcontent + '\'' +
                ", gid=" + gid +
                ", artid=" + artid +
                ", comtime=" + comtime +
                ", gname='" + gname + '\'' +
                '}';
    }

    public int getComid() {
        return comid;
    }

    public void setComid(int comid) {
        this.comid = comid;
    }

    public String getComcontent() {
        return comcontent;
    }

    public void setComcontent(String comcontent) {
        this.comcontent = comcontent;
    }

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public int getArtid() {
        return artid;
    }

    public void setArtid(int artid) {
        this.artid = artid;
    }

    public Date getComtime() {
        return comtime;
    }

    public void setComtime(Date comtime) {
        this.comtime = comtime;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        this.comtimeshow = format.format(this.comtime).replace(" ","T");
    }
}
