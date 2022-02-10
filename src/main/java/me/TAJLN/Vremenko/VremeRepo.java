package me.TAJLN.Vremenko;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class VremeRepo {

    static String GET_ALL = "select * from podatki;";
    static String GET_LAST_30 = "SELECT * FROM `podatki` order by id asc LIMIT 30;";
    static String GET_LATEST = "SELECT * FROM `podatki` order by cas DESC LIMIT 1;";
    static String NEW = "insert into podatki(vlaga, pritisk, temperatura, svetloba, oxid, redu, nh3, PID) values(?, ?, ?, ?, ?, ?, ?, ?);";
    static DataSource source;

    static ArrayList<Vreme> findAll() {

        ArrayList<Vreme> vremelist = new ArrayList<>();

        try
        {

            Connection con = connect().getConnection();
            PreparedStatement p = con.prepareStatement(GET_ALL);
            p.executeQuery();

            ResultSet resultSet = p.getResultSet();

            while (resultSet.next())
            {
                Vreme vreme = new Vreme();
                vreme.setId(resultSet.getInt("id"));
                vreme.setTime(resultSet.getTimestamp("cas"));
                vreme.setVlaga(resultSet.getFloat("vlaga"));
                vreme.setPritisk(resultSet.getFloat("pritisk"));
                vreme.setTemperatura(resultSet.getFloat("temperatura"));
                vreme.setVlaga(resultSet.getFloat("svetloba"));
                vreme.setOxid(resultSet.getFloat("oxid"));
                vreme.setRedu(resultSet.getFloat("redu"));
                vreme.setNh3(resultSet.getFloat("nh3"));
                vreme.setPostaja(resultSet.getInt("pid"));
                vremelist.add(vreme);
            }
            con.close();

        } catch (SQLException e){
            e.printStackTrace();
        }

        return vremelist;
    }

    static ArrayList<Vreme> last30() {

        ArrayList<Vreme> vremelist = new ArrayList<>();

        try
        {

            Connection con = connect().getConnection();
            PreparedStatement p = con.prepareStatement(GET_LAST_30);
            p.executeQuery();

            ResultSet resultSet = p.getResultSet();

            while (resultSet.next())
            {
                Vreme vreme = new Vreme();
                vreme.setId(resultSet.getInt("id"));
                vreme.setTime(resultSet.getTimestamp("cas"));
                vreme.setVlaga(resultSet.getFloat("vlaga"));
                vreme.setPritisk(resultSet.getFloat("pritisk"));
                vreme.setTemperatura(resultSet.getFloat("temperatura"));
                vreme.setVlaga(resultSet.getFloat("svetloba"));
                vreme.setOxid(resultSet.getFloat("oxid"));
                vreme.setRedu(resultSet.getFloat("redu"));
                vreme.setNh3(resultSet.getFloat("nh3"));
                vreme.setPostaja(resultSet.getInt("pid"));
                vremelist.add(vreme);
            }
            con.close();

        } catch (SQLException e){
            e.printStackTrace();
        }

        return vremelist;
    }

    static Vreme latest() {

        Vreme vreme = new Vreme();

        try
        {

            Connection con = connect().getConnection();
            PreparedStatement p = con.prepareStatement(GET_LATEST);
            p.executeQuery();

            ResultSet resultSet = p.getResultSet();

            while (resultSet.next()) {
                vreme.setId(resultSet.getInt("id"));
                vreme.setTime(resultSet.getTimestamp("cas"));
                vreme.setVlaga(resultSet.getFloat("vlaga"));
                vreme.setPritisk(resultSet.getFloat("pritisk"));
                vreme.setTemperatura(resultSet.getFloat("temperatura"));
                vreme.setVlaga(resultSet.getFloat("svetloba"));
                vreme.setOxid(resultSet.getFloat("oxid"));
                vreme.setRedu(resultSet.getFloat("redu"));
                vreme.setNh3(resultSet.getFloat("nh3"));
                vreme.setPostaja(resultSet.getInt("pid"));
            }
            con.close();

        } catch (SQLException e){
            e.printStackTrace();
        }

        return vreme;
    }

    static void addData(Vreme vreme){
        try {
            Connection con = connect().getConnection();
            PreparedStatement p = con.prepareStatement(NEW);

            p.setFloat(1, vreme.getVlaga());
            p.setFloat(2, vreme.getPritisk());
            p.setFloat(3, vreme.getTemperatura());
            p.setFloat(4, vreme.getSvetloba());
            p.setFloat(5, vreme.getOxid());
            p.setFloat(6, vreme.getRedu());
            p.setFloat(7, vreme.getNh3());
            p.setInt(8, vreme.getPostaja());

            p.executeUpdate();
            con.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    static DataSource connect() {
        BasicDataSource source = new BasicDataSource();
        source.addConnectionProperty("autoReconnect", "true");
        source.addConnectionProperty("allowMultiQueries", "true");
        source.setDefaultTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        source.setUrl("jdbc:mysql://localhost/vremenar");
        source.setUsername("root");
        source.setPassword("whateverpasswordyouwant");
        source.setMaxTotal(4);
        source.setMaxIdle(4);
        source.setTimeBetweenEvictionRunsMillis(180 * 1000);
        source.setSoftMinEvictableIdleTimeMillis(180 * 1000);

        VremeRepo.source = source;
        return source;
    }
}