package org.nuuskapoeka.logic;

import org.nuuskapoeka.domain.Boss;
import org.nuuskapoeka.domain.DailyBoss;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DailyBossManager {

    private String loadUrl;
    private List<DailyBoss> bosses;

    public DailyBossManager(){
        this.loadUrl = "https://docs.google.com/spreadsheets/d/e/2PACX-1vQACdbvpCIg7Uri2UZ_ZpoPLqEQzB0tWtnf8J8awM7s7DwvZQkoet1V-8TYyEKYPPo_CtU4QdtQDHxo/pub?gid=1222530453&single=true&output=csv";
        this.bosses = new ArrayList<>();
    }
    public void load(){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try{
            URL url = new URL(loadUrl);
            Scanner r = new Scanner(url.openStream());
            r.nextLine();
            while(r.hasNextLine()){
                String[] b = r.nextLine().split(",");
                LocalDate d = parseDate(b[0]);
                bosses.add(new DailyBoss(d,b[1],b[2],b[3]));
            }
            System.out.println("successfully loaded " + bosses.size() + " bosses");
        }catch(IOException e){
            System.out.println("file not found");
        }
    }
    private LocalDate parseDate(String d){
        String[] dates = d.split("/");
        return LocalDate.of(Integer.parseInt(dates[2]), Integer.parseInt(dates[1]), Integer.parseInt(dates[0]));
    }
    public DailyBoss getToday(){
        for(DailyBoss db : bosses){
            if(db.getDate().isEqual(LocalDate.now())){
                return db;
            }
        }
        return null;
    }
    public DailyBoss getBoss(LocalDate d){
        for(DailyBoss db : bosses){
            if(db.getDate().isEqual(d)){
                return db;
            }
        }
        return null;
    }
}