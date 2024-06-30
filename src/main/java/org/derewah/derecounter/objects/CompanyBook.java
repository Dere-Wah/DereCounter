package org.derewah.derecounter.objects;

import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;

public class CompanyBook implements Serializable {


    @Getter
    private double balance;

    @Getter
    private String name;

    @Getter
    private ArrayList<RegistryAction> register;

    public CompanyBook(String borsaName){
        this.name = borsaName;
        this.balance = 0;

        register = new ArrayList<>();
    }


    public void addAction(RegistryAction action){
        register.add(action);
        if(action.getType() == ActionType.SALE || action.getType() == ActionType.DEPOSIT){
            balance += action.getAmount();
        }else if(action.getType() == ActionType.WITHDRAW){
            if(balance >= action.getAmount()) {
                balance -= action.getAmount();
            }
        }
    }
}
