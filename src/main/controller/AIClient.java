package main.controller;

import main.model.Choice;
import main.model.ProCon;
import java.util.ArrayList;
import java.util.List;

public class AIClient {
    public List<ProCon> generateProsAndCons(String choiceTitle) {
        List<ProCon> prosAndCons = new ArrayList<>();
        
        if (choiceTitle.toLowerCase().contains("iphone")) {
            prosAndCons.add(new ProCon(-1, ProCon.Type.PRO, "48MP main camera with 4x optical zoom"));
            prosAndCons.add(new ProCon(-1, ProCon.Type.PRO, "A16 chip - 40% faster than previous gen"));
            prosAndCons.add(new ProCon(-1, ProCon.Type.PRO, "Up to 29 hours video playback"));
            prosAndCons.add(new ProCon(-1, ProCon.Type.PRO, "IP68 water resistance (6m for 30 mins)"));
            prosAndCons.add(new ProCon(-1, ProCon.Type.CON, "Starts at $999 - 25% more expensive than competitors"));
            prosAndCons.add(new ProCon(-1, ProCon.Type.CON, "Limited to iOS ecosystem"));
            prosAndCons.add(new ProCon(-1, ProCon.Type.CON, "No USB-C port until iPhone 15"));
        } else if (choiceTitle.toLowerCase().contains("android")) {
            prosAndCons.add(new ProCon(-1, ProCon.Type.PRO, "Customizable UI with 1000+ themes"));
            prosAndCons.add(new ProCon(-1, ProCon.Type.PRO, "30-50% lower price point than iPhone"));
            prosAndCons.add(new ProCon(-1, ProCon.Type.PRO, "Universal USB-C charging"));
            prosAndCons.add(new ProCon(-1, ProCon.Type.PRO, "Supports external storage up to 1TB"));
            prosAndCons.add(new ProCon(-1, ProCon.Type.CON, "2-3 years of updates vs iPhone's 5-6 years"));
            prosAndCons.add(new ProCon(-1, ProCon.Type.CON, "3-6 months delay in security patches"));
            prosAndCons.add(new ProCon(-1, ProCon.Type.CON, "Performance varies by manufacturer"));
        } else {
            prosAndCons.add(new ProCon(-1, ProCon.Type.PRO, choiceTitle + " - Market leader with 45% share"));
            prosAndCons.add(new ProCon(-1, ProCon.Type.PRO, "4.5/5 average customer rating"));
            prosAndCons.add(new ProCon(-1, ProCon.Type.PRO, "Available at 1000+ retailers"));
            prosAndCons.add(new ProCon(-1, ProCon.Type.CON, "15-20% more expensive than alternatives"));
            prosAndCons.add(new ProCon(-1, ProCon.Type.CON, "Limited availability in some regions"));
        }
        
        return prosAndCons;
    }

    public List<ProCon> compareChoices(Choice choice1, Choice choice2) {
        List<ProCon> comparison = new ArrayList<>();
        int pros1 = (int) choice1.getProsAndCons().stream().filter(p -> p.getType() == ProCon.Type.PRO).count();
        int cons1 = (int) choice1.getProsAndCons().stream().filter(p -> p.getType() == ProCon.Type.CON).count();
        int pros2 = (int) choice2.getProsAndCons().stream().filter(p -> p.getType() == ProCon.Type.PRO).count();
        int cons2 = (int) choice2.getProsAndCons().stream().filter(p -> p.getType() == ProCon.Type.CON).count();
        
        comparison.add(new ProCon(-1, ProCon.Type.PRO, 
            choice1.getTitle() + " has " + pros1 + " advantages vs " + pros2 + " for " + choice2.getTitle()));
        comparison.add(new ProCon(-1, ProCon.Type.CON, 
            choice1.getTitle() + " has " + cons1 + " disadvantages vs " + cons2 + " for " + choice2.getTitle()));
        
        return comparison;
    }
} 