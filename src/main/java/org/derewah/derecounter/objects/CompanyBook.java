package org.derewah.derecounter.objects;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.Setter;
import org.derewah.derecounter.DereCounter;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;



@DatabaseTable(tableName = "company_book")
public class CompanyBook{


    @Getter @Setter
    @DatabaseField(canBeNull = false, defaultValue = "0")
    private double balance;

    @Getter @Setter
    @DatabaseField(id = true, canBeNull = false)
    private String name;

    @Getter @Setter
    @ForeignCollectionField(eager = false)
    private ForeignCollection<RegistryAction> register;


    public CompanyBook(){
    }


    public void addAction(RegistryAction action) throws SQLException {
        register.add(action);
        if(action.getType() == ActionType.SALE || action.getType() == ActionType.DEPOSIT){
            balance += action.getAmount();
        }else if(action.getType() == ActionType.WITHDRAW){
            if(balance >= action.getAmount()) {
                balance -= action.getAmount();
            }
        }
        DereCounter.getInstance().getDatabase().updateCompanyBook(this);
    }
}
