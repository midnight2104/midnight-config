package com.midnight.config.server.lock;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 基于数据库实现的分布式锁
 */
@Slf4j
@Component
public class DistributedLocks {

    @Autowired
    private DataSource dataSource;
    private Connection connection;

    @Getter
    private final AtomicBoolean locked = new AtomicBoolean(false);

    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    @PostConstruct
    public void init() {
        try {
            // 单独创建连接，不影响其他地方的数据库连接使用
            this.connection = dataSource.getConnection();
        } catch (Exception e) {
            log.error("获取数据库连接出错了", e);
        }

        executor.scheduleWithFixedDelay(this::tryLock, 1000, 5000, TimeUnit.MILLISECONDS);
    }

    public void tryLock() {
        try {
            lock();
            locked.set(true);
        } catch (Exception e) {
            log.error("数据库操作出错了", e);

            locked.set(false);
        }
    }

    public boolean lock() throws SQLException {

        connection.setAutoCommit(false);
        connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

        // 锁5秒
        connection.createStatement().execute("set innodb_lock_wait_timeout=5");
        // 同一个链接请求， for update 锁的可重入
        connection.createStatement().execute("select app from locks where id=1 for update");

        // 事务不要commit或rollback，否则锁就释放了

        if (locked.get()) {
            log.info("===>>> reenter this dist lock");
        } else {
            log.info("===>>> get a dist lock");
        }

        return true;
    }

    @PreDestroy
    public void closed() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.rollback();
                connection.close();
            }
        } catch (Exception e) {
            log.error("关闭连接出错了", e);
        }
    }
}
