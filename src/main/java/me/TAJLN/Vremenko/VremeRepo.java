package me.TAJLN.Vremenko;

import org.apache.commons.dbcp2.BasicDataSource;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.dao.DataIntegrityViolationException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;

public class VremeRepo {

    static String GET_ALL = "select * from podatki where PID = ?;";
    static String GET_LAST_30 = "SELECT * FROM `podatki` where PID = ? order by id desc LIMIT 30;";
    static String GET_LATEST = "SELECT * FROM `podatki` where PID = ? order by cas DESC LIMIT 1;";
    static String NEW_POSTAJA = "insert into podatki(vlaga, pritisk, temperatura, svetloba, oxid, redu, nh3, PID) values(?, ?, ?, ?, ?, ?, ?, ?);";
    static String NEW_USER = "insert into uporabniki(oneaccount_id) values(?);";
    static String GET_USER_ID = "select id from uporabniki where oneaccount_id = ?";
    static String GET_POSTAJA = "select * from postaje where kljuc = ?";
    static String GET_USER_POSTAJE = "select * from postaje where lastnik = ?";
    static String ADD_POSTAJA = "insert into postaje(ime, kljuc, lastnik) values(?,?,?);";
    static String DELETE_POSTAJA = "delete from postaje where kljuc=?;";
    static DataSource source;

    static ArrayList<Vreme> findAll(int id) {

        ArrayList<Vreme> vremelist = new ArrayList<>();

        try
        {

            Connection con = connect().getConnection();
            PreparedStatement p = con.prepareStatement(GET_ALL);
            p.setInt(1, id);
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
                vreme.setSvetloba(resultSet.getFloat("svetloba"));
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



    //User
    static void createUser(String oneaccount_id) {

        try {
            Connection con = connect().getConnection();
            PreparedStatement p = con.prepareStatement(NEW_USER);
            p.setString(1, oneaccount_id);
            p.executeUpdate();
            con.close();
        }
            catch(SQLIntegrityConstraintViolationException ignored){

            } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    static int getUserId(String oneaccount_id) throws SQLException {
        Connection con = connect().getConnection();
        PreparedStatement p = con.prepareStatement(GET_USER_ID);
        p.setString(1, oneaccount_id);
        p.executeQuery();

        ResultSet resultSet = p.getResultSet();

        while (resultSet.next())
        {
            return resultSet.getInt("id");
        }


        con.close();
        return -1;
    }

    static JSONArray getUserPostaje(String oneaccount_id) throws SQLException, JSONException {
        JSONArray arr = new JSONArray();

        Connection con = connect().getConnection();
        PreparedStatement p = con.prepareStatement(GET_USER_POSTAJE);
        p.setInt(1, getUserId(oneaccount_id));
        p.executeQuery();

        ResultSet resultSet = p.getResultSet();

        while (resultSet.next()) {
            Postaja postaja = getPostaja(resultSet.getString("kljuc"));

            JSONObject obj = new JSONObject();
            obj.put("id", postaja.getId());
            obj.put("ime", postaja.getIme());
            obj.put("kljuc", postaja.getKljuc());
            obj.put("lastnik", postaja.getLastnik());

            arr.put(obj);
        }

        con.close();

        return arr;

    }

    //Postaja

    static void addPostaja(String ime, String owner_oneaccountid) throws SQLException {
        Connection con = connect().getConnection();
        PreparedStatement p = con.prepareStatement(ADD_POSTAJA);

        p.setString(1, ime);
        p.setString(2, String.valueOf(new RandomString().nextString()));
        p.setInt(3, getUserId(owner_oneaccountid));
        p.executeUpdate();
        con.close();
    }

    static void deletePostaja(String kljuc, String owner_oneaccountid) throws SQLException {
        Postaja postaja = getPostaja(kljuc);
        if(postaja.getLastnik() != getUserId(owner_oneaccountid))
            return;

        Connection con = connect().getConnection();
        PreparedStatement p = con.prepareStatement(DELETE_POSTAJA);
        p.setString(1, kljuc);
        p.executeUpdate();
        con.close();
    }

    static Postaja getPostaja(String kljuc){
        Postaja po = new Postaja();
        try{
            Connection con = connect().getConnection();
            PreparedStatement p = con.prepareStatement(GET_POSTAJA);
            p.setString(1, kljuc);
            p.executeQuery();

            ResultSet resultSet = p.getResultSet();

            if (resultSet!=null){
                while(resultSet.next()) {
                    po.setId(resultSet.getInt("id"));
                    po.setIme(resultSet.getString("ime"));
                    po.setKljuc(resultSet.getString("kljuc"));
                    po.setLastnik(resultSet.getInt("lastnik"));
                }
            }
            else{
                po = null;
            }
            con.close();
        } catch(SQLException e){
            e.printStackTrace();
        }
        return po;
    }

    static ArrayList<Vreme> last30(int id) {

        ArrayList<Vreme> vremelist = new ArrayList<>();

        try
        {

            Connection con = connect().getConnection();
            PreparedStatement p = con.prepareStatement(GET_LAST_30);
            p.setInt(1, id);
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
                vreme.setSvetloba(resultSet.getFloat("svetloba"));
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

        Collections.reverse(vremelist);

        return vremelist;
    }

    static Vreme latest(int id) {

        Vreme vreme = new Vreme();

        try
        {

            Connection con = connect().getConnection();
            PreparedStatement p = con.prepareStatement(GET_LATEST);
            p.setInt(1, id);
            p.executeQuery();

            ResultSet resultSet = p.getResultSet();

            while (resultSet.next()) {
                vreme.setId(resultSet.getInt("id"));
                vreme.setTime(resultSet.getTimestamp("cas"));
                vreme.setVlaga(resultSet.getFloat("vlaga"));
                vreme.setPritisk(resultSet.getFloat("pritisk"));
                vreme.setTemperatura(resultSet.getFloat("temperatura"));
                vreme.setSvetloba(resultSet.getFloat("svetloba"));
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
            PreparedStatement p = con.prepareStatement(NEW_POSTAJA);

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