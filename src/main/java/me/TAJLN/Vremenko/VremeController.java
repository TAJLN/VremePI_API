package me.TAJLN.Vremenko;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;

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