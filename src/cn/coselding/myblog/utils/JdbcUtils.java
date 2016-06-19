package cn.coselding.myblog.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 使用dbcp连接池的jdbcUtils
 *
 * @author 宇强
 *
 */
public class JdbcUtils {

	/**
	 * 五种事务隔离级别
	 */
	public static final int TRANSACTION_READ_COMMITTED = Connection.TRANSACTION_READ_COMMITTED;
	public static final int TRANSACTION_READ_UNCOMMITTED = Connection.TRANSACTION_READ_UNCOMMITTED;
	public static final int TRANSACTION_REPEATABLE_READ = Connection.TRANSACTION_REPEATABLE_READ;
	public static final int TRANSACTION_SERIALIZABLE = Connection.TRANSACTION_SERIALIZABLE;
	public static final int TRANSACTION_NONE = Connection.TRANSACTION_NONE;

	private static DataSource ds = null;
	private static ThreadLocal<Connection> threadLocal = new ThreadLocal<Connection>();

	static {
		try {
			//JNDI获取数据库连接池
			//Context initCtx = new InitialContext();
			//Context envCtx = (Context) initCtx.lookup("java:comp/env");
			//ds = (DataSource)
			//		envCtx.lookup("jdbc/MyDataSource");
			// 加载配置文件
			ds = new ComboPooledDataSource("myConfig");
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 获取数据源连接池
	 *
	 * @return
	 */
	public static DataSource getDataSource() {
		return ds;
	}

	/**
	 * 开启事务
	 */
	public static void startTransaction() {
		Connection conn = null;
		try {
			conn = threadLocal.get();
			if (conn == null) {
				conn = getConnection();
				threadLocal.set(conn);
			}
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 回滚事务
	 */
	public static void rollback() {
		Connection connection = null;
		try {
			connection = threadLocal.get();
			if (connection != null)
				connection.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 提交事务
	 */
	public static void commit() {
		Connection connection = null;
		try {
			connection = threadLocal.get();
			if (connection != null)
				connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 测试
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		try {
			connection = JdbcUtils.getConnection();
			statement = connection
					.prepareStatement("insert into account(name,money) values(?,?);");

			statement.setString(1, "jack");
			statement.setFloat(2, 1000);
			statement.addBatch();

			statement.setString(1, "sam");
			statement.setFloat(2, 1000);
			statement.addBatch();

			statement.setString(1, "john");
			statement.setFloat(2, 1000);
			statement.addBatch();

			statement.executeBatch();
			statement.clearBatch();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JdbcUtils.release();
		}
	}

	/**
	 * 获取当前线程连接
	 *
	 * @return
	 */
	public static Connection getConnection() {
		Connection connection = null;
		try {
			connection = threadLocal.get();
			if (connection == null) {
				connection = ds.getConnection();
				threadLocal.set(connection);
			}
			return connection;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 设置只读状态
	 */
	public static void setReadOnly() {
		Connection conn = null;
		try {
			conn = threadLocal.get();
			if (conn == null) {
				conn = getConnection();
				threadLocal.set(conn);
			}
			conn.setReadOnly(true);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 设置事务隔离级别
	 */
	public static void setTransactionIsolation(int level) {
		Connection conn = null;
		try {
			conn = threadLocal.get();
			if (conn == null) {
				conn = getConnection();
				threadLocal.set(conn);
			}
			conn.setTransactionIsolation(level);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 释放资源
	 *
	 * @param con
	 * @param st
	 * @param rs
	 */
	public static void release() {
		Connection con = threadLocal.get();
		if (con != null) {
			try {
				con.close();
				// 解除当前线程的绑定
				threadLocal.remove();
				con = null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
