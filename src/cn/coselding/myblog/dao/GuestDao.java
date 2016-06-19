package cn.coselding.myblog.dao;

import cn.coselding.myblog.domain.Guest;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by 宇强 on 2016/3/12 0012.
 */
public interface GuestDao {
    //增
    long saveGuest(Guest guest) throws SQLException;

    //删
    void deleteGuest(int gid) throws SQLException;

    //改
    int updateGuest(Guest guest) throws SQLException;

    //查询单个
    Guest queryGuest(int gid) throws SQLException;

    //查询单个
    Guest queryGuestByEmail(String email) throws SQLException;

    //查询分页
    List<Guest> getPageData(int startindex, int pagesize) throws SQLException;

    //查总数
    long queryCount() throws SQLException;

    List<Guest> queryRssGuests() throws SQLException;
}
