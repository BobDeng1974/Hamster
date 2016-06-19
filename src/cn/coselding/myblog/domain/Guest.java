package cn.coselding.myblog.domain;

/**
 * Created by 宇强 on 2016/3/11 0011.
 */
public class Guest {
    private int gid ;
    private String gname ;
    private String gemail;
    private int rss;

    @Override
    public String toString() {
        return "Guest{" +
                "gid=" + gid +
                ", gname='" + gname + '\'' +
                ", gemail='" + gemail + '\'' +
                ", rss=" + rss +
                '}';
    }

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public String getGname() {
        return gname;
    }

    public void setGname(String gname) {
        this.gname = gname;
    }

    public String getGemail() {
        return gemail;
    }

    public void setGemail(String gemail) {
        this.gemail = gemail;
    }

    public int getRss() {
        return rss;
    }

    public void setRss(int rss) {
        this.rss = rss;
    }
}
