package sample.model.constraint;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.alfresco.repo.dictionary.constraint.ListOfValuesConstraint;
import org.apache.log4j.Logger;

public class ListOfSitesConstraint extends ListOfValuesConstraint implements Serializable {

    private static final long serialVersionUID = 1;
    private static Logger log = Logger.getLogger(ListOfSitesConstraint.class);

    private String sourceUrl;

    public void setCaseSensitive(boolean caseSensitive) {
    }

    public void initialize() {
        super.setCaseSensitive(false);
        this.loadList();
    }

    public List<SelectItem> getSelectItemList() {
        List<SelectItem> result = new ArrayList<SelectItem>(this.getAllowedValues().size());
        for (int i = 0; i < this.getAllowedValues().size(); i++) {
            result.add(new SelectItem((Object) this.getAllowedValues().get(i), (String) this.getAllowedValues().get(i)));
        }
        return result;
    }

    protected void loadList() {

        List<String> av = new ArrayList<String>();
        try {
            URL url = new URL(this.getSourceUrl());
            URLConnection urlConnection = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if(log.isDebugEnabled()) log.debug(inputLine);
                av.add(inputLine);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.setAllowedValues(av);
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }
}