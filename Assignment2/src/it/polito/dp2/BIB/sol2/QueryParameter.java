package it.polito.dp2.BIB.sol2;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class QueryParameter {
    private Integer itemNumber;
    private List<String> keywordList;




    public QueryParameter(String[] args) {

        itemNumber = Integer.parseInt(args[0]);
        keywordList = new LinkedList<>(Arrays.asList(args));
        keywordList.remove(0);
    }

    public Integer getItemNumber() {
        return itemNumber;
    }

    public String getGoogleKeyword(){
        return String.join(" ", keywordList);
    }


    public String getCrossrefKeyword(){
        return String.join("+", keywordList);
    }

}
