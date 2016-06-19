package cn.coselding.myblog.dao.impl;


import cn.coselding.myblog.domain.Comment;
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
public class CommentDaoImpl implements cn.coselding.myblog.dao.CommentDao {


    //增
    @Override
    public void saveComment(Comment comment) throws SQLException {
        QueryRunner runner = new QueryRunner();
        String sql = "insert into comment(comid,comcontent,gid,artid,comtime) values(?,?,?,?,?);";
        Object[] params = {comment.getComid(),comment.getComcontent(),comment.getGid(),comment.getArtid(),comment.getComtime()};
        int result = runner.update(JdbcUtils.getConnection(), sql,params);
    }

    //删
    @Override
    public void deleteComnent(int comid) throws SQLException {
        QueryRunner runner = new QueryRunner();
        String sql = "delete from comment where comid=?;";
        Object[] params = {comid};
        int result = runner.update(JdbcUtils.getConnection(), sql,params);
    }

    //改
    @Override
    public int updateComment(Comment comment) throws SQLException {
        QueryRunner runner = new QueryRunner();
        String sql = "update comment set comcontent=?,artid=?,comtime=? where comid=?;";
        Object[] params = {comment.getComcontent(),comment.getArtid(),comment.getComtime(),comment.getComid()};
        int result = runner.update(JdbcUtils.getConnection(), sql,params);
        return result;
    }

    //查询单个
    @Override
    public Comment queryComment(int comid) throws SQLException {
        QueryRunner runner = new QueryRunner();
        String sql = "select * from comment where comid=?;";
        Comment  comment = runner.query(JdbcUtils.getConnection(),sql,new BeanHandler<Comment>(Comment.class),comid);
        return comment;
    }

    //查询总数
    @Override
    public long queryCount() throws SQLException {
        QueryRunner runner = new QueryRunner();
        String sql = "select count(*) from comment;";
        long result = runner.query(JdbcUtils.getConnection(),sql,new ScalarHandler<Long>());
        return result;
    }

    //查分页
    @Override
    public List<Comment> getPageData(int startindex, int pagesize) throws SQLException {
        QueryRunner runner = new QueryRunner();
        String sql = "select * from comment com,guest g where com.gid=g.gid order by com.comtime desc limit ?,?;";
        List<Comment>  list = runner.query(JdbcUtils.getConnection(),sql,new BeanListHandler<Comment>(Comment.class),startindex,pagesize);
        return list;
    }
    //查询客户的留言总数
    @Override
    public long queryGuestCount(int gid) throws SQLException {
        QueryRunner runner = new QueryRunner();
        String sql = "select count(*) from comment where gid=?;";
        long result = runner.query(JdbcUtils.getConnection(),sql,new ScalarHandler<Long>(),new Object[]{gid});
        return result;
    }

    //查客户的留言分页
    @Override
    public List<Comment> getGuestPageData(int gid,int startindex, int pagesize) throws SQLException {
        QueryRunner runner = new QueryRunner();
        String sql = "select * from comment com,guest g where com.gid=g.gid and com.gid=? order by com.comtime desc limit ?,?;";
        List<Comment>  list = runner.query(JdbcUtils.getConnection(),sql,new BeanListHandler<Comment>(Comment.class),gid,startindex,pagesize);
        return list;
    }
}
