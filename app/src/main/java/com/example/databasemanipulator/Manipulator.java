package com.example.databasemanipulator;

import android.util.Log;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

public class Manipulator {
    public static String DatabaseUserName;
    public static String DatabasePassword;
    public static String DatabaseUrl;
    public static final String DriverName = "com.mysql.jdbc.Driver";
    public static final String LOG_TAG = "Manipulator LOG";
    public static String filter, tableName;


    public static void update(String filter, String tableName) {
        Manipulator.filter = filter;
        Manipulator.tableName = tableName;
    }

    public void close(AutoCloseable resource) {
        try {
            resource.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Connection connect() {
        try {
            Class.forName(DriverName);
            Connection connection = DriverManager.getConnection(DatabaseUrl, DatabaseUserName, DatabasePassword);
            Log.e(LOG_TAG, "Connection Success");
            return connection;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        Log.e(LOG_TAG, "Connection Failed");
        return null;
    }

    public ArrayList<ArrayList<String>> query(String customSql,String filter, String tableName, String condition) {

        ArrayList<ArrayList<String>> ans = new ArrayList<>();
        Connection connection = connect();
        if (connection == null)
            return null;
        String sql;
        if (condition == null || condition.equals(""))
            sql = "select " + filter + " from " + tableName;
        else {
            sql = "select " + filter + " from " + tableName + " where " + condition;
        }
        if (customSql.equals("")==false)
            sql=customSql;

        try {
            ResultSet resultSet = connection.createStatement().executeQuery(sql);
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

            //获得表头
            int columnCount = resultSetMetaData.getColumnCount();
            ArrayList<String> columnName = new ArrayList<>();
            for (int i = 1; i <= columnCount; i++) {
                columnName.add(resultSetMetaData.getColumnName(i));
            }

            //添加值
            while (resultSet.next()) {
                ArrayList<String> temp = new ArrayList<>();
                for (int i = 1; i <= columnCount; i++) {
                    temp.add(columnName.get(i - 1)+":    " + resultSet.getString(i));
                }
                ans.add(temp);
            }
            close(resultSet);
            close(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (ans.size() > 0)
            return ans;
        else
            return null;
    }

    public Boolean insert(String TableName,ArrayList<String> parameter, ArrayList<ArrayList<String>> values){
        String sql = "insert into " + TableName + " (";
        for (int i = 0; i < parameter.size() - 1; i++)
            sql += parameter.get(i) + ",";
        sql += parameter.get(parameter.size() - 1);
        sql +=")VALUES";
        for (int i = 0; i < values.size(); i++) {
            sql += "(";
            for (int j = 0; j < values.get(0).size(); j++) {
                sql += "'" + values.get(i).get(j) + "'";
                if (j == values.get(0).size() - 1)
                    sql += ")";
                else
                    sql += ",";
            }
            if (i == values.size() - 1)
                sql += ";";
            else
                sql += ",";
        }
        //sql="insert into Class_10 (ID,Name,Statu)VALUES (0008,8888,1);";
        Log.e("Sql=",sql);
        Boolean flag=false;
        Connection connection=connect();
        if (connection==null)
            return false;
        try {
            connection.createStatement().executeUpdate(sql);
            flag=true;
            close(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }

    public Boolean execute(String sql){
        Connection connection=connect();
        if (connection==null)
            return false;
        Boolean flag=false;
        try {
            connection.createStatement().execute(sql);
            flag=true;
            close(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }
}
