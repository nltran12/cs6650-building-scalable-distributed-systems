/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dal;

import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class HikariDS {
  private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
  private static final String CLOUD_SQL_CONNECTION_NAME = System.getenv(
      "CLOUD_SQL_CONNECTION_NAME");
  private static final String DB_USER = System.getenv("DB_USER");
  private static final String DB_PASS = System.getenv("DB_PASS");
  private static final String DB_NAME = System.getenv("DB_NAME");

  private static HikariDataSource dataSource = new HikariDataSource();
  static {

    // Configure which instance and what database user to connect with.
    dataSource.setJdbcUrl(String.format("jdbc:mysql:///%s", DB_NAME));
    dataSource.setUsername(DB_USER); // e.g. "root", "postgres"
    dataSource.setPassword(DB_PASS); // e.g. "my-password"
    dataSource.setDriverClassName(JDBC_DRIVER);

    dataSource.addDataSourceProperty("socketFactory", "com.google.cloud.sql.mysql.SocketFactory");
    dataSource.addDataSourceProperty("cloudSqlInstance", CLOUD_SQL_CONNECTION_NAME);
    dataSource.addDataSourceProperty("useSSL", "false");
    dataSource.setMaximumPoolSize(5);
    dataSource.setMinimumIdle(5);

  }

  public static Connection getConnection() throws SQLException {
    return dataSource.getConnection();
  }
}
