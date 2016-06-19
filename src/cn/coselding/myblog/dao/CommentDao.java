package cn.coselding.myblog.dao;

import cn.coselding.myblog.domain.Comment;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by 宇强 on 2016/3/12 0012.
 */
public interface CommentDao {
    //增
    void saveComment(Comment comment) throws SQLException;

    //删
    void deleteComnent(int comid) throws SQLException;

    //改
    int updateComment(Comment comment) throws SQLException;

    //查询单个
    Comment queryComment(int comid) throws SQLException;

    //查询总数
    long queryCount() throws SQLException;

    //查分页
    List<Comment> getPageData(int startindex, int pagesize) throws SQLException;

    //查询总数
    long queryGuestCount(int gid) throws SQLException;

    //查分页
    List<Comment> getGuestPageData(int gid, int startindex, int pagesize) throws SQLException;
}
