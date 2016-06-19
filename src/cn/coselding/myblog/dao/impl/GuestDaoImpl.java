package cn.coselding.myblog.dao.impl;

import cn.coselding.myblog.dao.GuestDao;
import cn.coselding.myblog.domain.Guest;
import cn.coselding.myblog.utils.JdbcUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by 宇强 on 2016/3/11 0011.
 */
public class GuestDaoImpl implements GuestDao {

    //增
    @Override
    public long saveGuest(Guest guest) throws SQLException {
        QueryRunner runner = new QueryRunner();
        String sql = "insert into guest(gname,gemail,rss) values(?,?,?);";
        Object[] params = {guest.getGname(), guest.getGemail(), guest.getRss()};
        long result = runner.insert(JdbcUtils.getConnection(),sql,new ScalarHandler<Long>(),params);
        return result;
    }

    //删
    @Override
    public void deleteGuest(int gid) throws SQLException {
        QueryRunner runner = new QueryRunner();
        String sql = "delete from guest where gid=?;";
        int result = runner.update(JdbcUtils.getConnection(), sql, gid);
    }

    //改
    @Override
    public int updateGuest(Guest guest) throws SQLException {
        QueryRunner runner = new QueryRunner();
        String sql = "update guest set gname=?,gemail=?,rss=? where gid=?;";
        Object[] params = {guest.getGname(), guest.getGemail(), guest.getRss(), guest.getGid()};
        int result = runner.update(JdbcUtils.getConnection(), sql, params);
        return result;
    }

    //查询单个
    @Override
    public Guest queryGuest(int gid) throws SQLException {
        QueryRunner runner = new QueryRunner();
        String sql = "select * from guest where gid=?;";
        Guest guest = runner.query(JdbcUtils.getConnection(), sql, new BeanHandler<Guest>(Guest.class), gid);
        return guest;
    }

    //查询单个
    @Override
    public Guest queryGuestByEmail(String email) throws SQLException {
        QueryRunner runner = new QueryRunner();
        String sql = "select * from guest where gemail=?;";
        Guest guest = runner.query(JdbcUtils.getConnection(), sql, new BeanHandler<Guest>(Guest.class), email);
        return guest;
    }

    //查询分页
    @Override
    public List<Guest> getPageData(int startindex, int pagesize) throws SQLException {
        QueryRunner runner = new QueryRunner();
        String sql = "select * from guest limit ?,?;";
        Object[] params = {startindex,pagesize};
        List<Guest> list = runner.query(JdbcUtils.getConnection(), sql, new BeanListHandler<Guest>(Guest.class), params);
        return list;
    }

    //查总数
    @Override
    public long queryCount() throws SQLException {
        QueryRunner runner = new QueryRunner();
        String sql = "select count(*) from guest;";
        long count = runner.query(JdbcUtils.getConnection(), sql, new ScalarHandler<Long>());
        return count;
    }

    @Override
    public List<Guest> queryRssGuests() throws SQLException {
        QueryRunner runner = new QueryRunner();
        String sql = "select * from guest where rss=?;";
        List<Guest> list = runner.query(JdbcUtils.getConnection(), sql, new BeanListHandler<Guest>(Guest.class), 1);
        return list;
    }
}
