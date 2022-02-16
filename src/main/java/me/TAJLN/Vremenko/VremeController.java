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

    //@RequestParam(name="vlaga") float vlaga
    //            , @RequestParam(name="temperatura") float temperatura,@RequestParam(name="svetloba") float svetloba, @RequestParam(name="oxid") float oxid,@RequestParam(name="redu") float redu, @RequestParam(name="nh3") float nh3, @RequestParam(name="postaja") int postaja

    @PostMapping(path="/addVreme") // Map ONLY POST Requests
    public @ResponseBody String addVreme (@RequestBody String body) {
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request

        JSONObject obj;
        float vlaga;
        float pritisk;
        float temperatura;
        float svetloba;
        float oxid;
        float redu;
        float nh3;
        int postaja;

        try {
            obj = new JSONObject(body);
            vlaga = BigDecimal.valueOf(obj.getDouble("vlaga")).floatValue();
            pritisk = BigDecimal.valueOf(obj.getDouble("pritisk")).floatValue();
            temperatura = BigDecimal.valueOf(obj.getDouble("temperatura")).floatValue();
            svetloba = BigDecimal.valueOf(obj.getDouble("svetloba")).floatValue();
            oxid = BigDecimal.valueOf(obj.getDouble("oxid")).floatValue();
            redu = BigDecimal.valueOf(obj.getDouble("redu")).floatValue();
            nh3 = BigDecimal.valueOf(obj.getDouble("nh3")).floatValue();
            postaja = obj.getInt("postaja");
        } catch (JSONException e) {
            return "Failed to parse JSON body";
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

    @GetMapping(path="/all")
    public @ResponseBody
    ArrayList<Vreme> getAllPodatki() {
        System.out.println("Sent all from podatki");
        return VremeRepo.findAll();
    }

    @GetMapping(path="/latest")
    public @ResponseBody
    Vreme getLatestpodatki() {
        System.out.println("Sent latest from podatki");
        return VremeRepo.latest();
    }

    @GetMapping(path="/last30")
    public @ResponseBody
    ArrayList<Vreme> last30() {
        System.out.println("Sent last 30 from podatki");
        return VremeRepo.last30();
    }
}