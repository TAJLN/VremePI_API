package me.TAJLN.Vremenko;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;

@Controller
@RequestMapping(path="/podatki")
public class VremeController {
    @PostMapping(path="/addVreme")
    public @ResponseBody String addVreme (@RequestBody String body) throws JSONException {
        JSONObject obj;
        float vlaga;
        float pritisk;
        float temperatura;
        float svetloba;
        float oxid;
        float redu;
        float nh3;
        String postajakey;

        try {
            obj = new JSONObject(body);
            vlaga = BigDecimal.valueOf(obj.getDouble("vlaga")).floatValue();
            pritisk = BigDecimal.valueOf(obj.getDouble("pritisk")).floatValue();
            temperatura = BigDecimal.valueOf(obj.getDouble("temperatura")).floatValue();
            svetloba = BigDecimal.valueOf(obj.getDouble("svetloba")).floatValue();
            oxid = BigDecimal.valueOf(obj.getDouble("oxid")).floatValue();
            redu = BigDecimal.valueOf(obj.getDouble("redu")).floatValue();
            nh3 = BigDecimal.valueOf(obj.getDouble("nh3")).floatValue();
            postajakey = obj.getString("kljuc");

        } catch (JSONException e) {
            return "Failed to parse JSON body";
        }

        int postaja = VremeRepo.getPostaja(postajakey).getId();

        if (postaja == 0) {
            JSONObject resp = new JSONObject();
            resp.put("Status", "failed");
            resp.put("Error", "Wrong key");
            return resp.toString();
        }

        Vreme v = new Vreme();
        v.setVlaga(vlaga);
        v.setPritisk(pritisk);
        v.setTemperatura(temperatura);
        v.setSvetloba(svetloba);

        v.setOxid(oxid);
        v.setRedu(redu);
        v.setNh3(nh3);

        v.setPostaja(postaja);
        VremeRepo.addData(v);


        String output = "Saved entry: " +
                "Vlaga: " + vlaga + ", " +
                "Pritisk: " + pritisk + ", " +
                "Temperatura: " + temperatura + ", " +
                "Svetloba: " + svetloba + ", " +
                "Oxid: " + oxid + ", " +
                "Redu: " + redu + ", " +
                "Nh3: " + nh3 + ", " +
                "Postaja: " + postaja;
        System.out.println(output);
        return output;
    }

    @PostMapping(path="/all")
    public @ResponseBody
    Object getAllPodatki(@RequestBody String body) throws JSONException {
        JSONObject obj = new JSONObject(body);
        String kljuc = obj.getString("kljuc");

        int id = VremeRepo.getPostaja(kljuc).getId();
        if(id == 0) {
            JSONObject resp = new JSONObject();
            resp.put("Status", "failed");
            resp.put("Error", "Wrong key");
            return resp.toString();
        }

        System.out.println("Sent all from podatki");
        return VremeRepo.findAll(id);
    }

    @PostMapping(path="/latest")
    public @ResponseBody
    Object getLatestpodatki(@RequestBody String body) throws JSONException {
        JSONObject obj = new JSONObject(body);
        String kljuc = obj.getString("kljuc");

        int id = VremeRepo.getPostaja(kljuc).getId();
        if(id == 0) {
            JSONObject resp = new JSONObject();
            resp.put("Status", "failed");
            resp.put("Error", "Wrong key");
            return resp.toString();
        }

        System.out.println("Sent latest from podatki");
        return VremeRepo.latest(id);
    }

    @PostMapping(path = "/addPostaja")
    public @ResponseBody
    Object addPostaja(@RequestBody String body) throws JSONException {
        JSONObject obj = new JSONObject(body);
        String ime = obj.getString("ime");
        String owner_token = obj.getString("token");

        try{
            VremeRepo.addPostaja(ime, owner_token);

            System.out.println("Postaja added");
            return "Postaja added";
        } catch(Exception e){
            e.printStackTrace();
            System.out.println("Failed to add postaja");
            return "Failed to add postaja";
        }


    }

    @PostMapping(path = "/getPostaja")
    public @ResponseBody
    Object getPostaja(@RequestBody String body) throws JSONException {
        JSONObject obj = new JSONObject(body);
        String kljuc = obj.getString("kljuc");
        try{
             Postaja postaja = VremeRepo.getPostaja(kljuc);
             System.out.println("Sent postaja");
             return postaja;
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("Failed to get postaja");
            return "Failed to get postaja";
        }
    }

    @PostMapping(path = "/deletePostaja")
    public @ResponseBody
    Object deletePostaja(@RequestBody String body) throws JSONException {
        JSONObject obj = new JSONObject(body);
        String kljuc = obj.getString("kljuc");
        String owner_oneaccountid = obj.getString("token");

        try{
            VremeRepo.deletePostaja(kljuc, owner_oneaccountid);

            System.out.println("Postaja deleted");
            return "Postaja deleted";
        } catch(Exception e){
            e.printStackTrace();
            System.out.println("Failed to delete postaja");
            return "Failed to delete postaja";
        }
    }


    @PostMapping(path="/addUser")
    public @ResponseBody
    String addUser(@RequestBody String body) throws JSONException, IOException {
        JSONObject obj = new JSONObject(body);
        String token = obj.getString("token");

        String onaccount_id = Utils.oneaccount_id(token);

        try {
            VremeRepo.createUser(onaccount_id);
            System.out.println("User added");
            return "User added";
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("Failed to add user");
            return "Failed to add user";
        }
    }

    @PostMapping(path="/getUserPostaje")
    public @ResponseBody
    Object getUserPostaje(@RequestBody String body) throws JSONException {
        JSONObject obj = new JSONObject(body);
        String token = obj.getString("token");

        try {
            System.out.println("Sent postaje for token: " + token);
            return VremeRepo.getUserPostaje(token).toString();
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("Failed to get postaje");
            return "Failed to get postaje";
        }
    }

    @PostMapping(path="/last30")
    public @ResponseBody
    Object last30(@RequestBody String body) throws JSONException {
        JSONObject obj = new JSONObject(body);
        String kljuc = obj.getString("kljuc");

        int id = VremeRepo.getPostaja(kljuc).getId();
        if(id == 0) {
            JSONObject resp = new JSONObject();
            resp.put("Status", "failed");
            resp.put("Error", "Wrong key");
            return resp.toString();
        }

        System.out.println("Sent last 30 from podatki");
        return VremeRepo.last30(id);
    }

    @PostMapping(path="/login")
    public @ResponseBody String login (@RequestBody String body) throws JSONException {

        JSONObject resp = new JSONObject();

        JSONObject obj = new JSONObject(body);
        String kljuc = obj.getString("kljuc");
        Postaja p = VremeRepo.getPostaja(kljuc);
        if (p.getId() == 0) {
            resp.put("status", "failed");
            resp.put("Error","Wrong key");
        }
        else {
            resp.put("status", "success");
            resp.put("id", p.getId());
            resp.put("ime", p.getIme());
            resp.put("kljuc", p.getKljuc());
        }

        return resp.toString();
    }
}