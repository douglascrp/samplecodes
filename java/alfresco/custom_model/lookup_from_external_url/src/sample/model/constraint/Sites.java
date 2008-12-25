package sample.model.constraint;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.alfresco.repo.dictionary.constraint.ListOfValuesConstraint;
import org.apache.log4j.Logger;

public class Sites extends ListOfValuesConstraint implements Serializable {

    private static final long serialVersionUID = 1;
    private static Logger log = Logger.getLogger(Sites.class);

    private String sourceUrl;

    /** TODO: if you remove this method, type dpesn't show up !! */
    public void initialize() {
    }

    @Override
    public List<String> getAllowedValues() {
        List<String> allowedValues = new ArrayList<String>();
        try {
            URL url = new URL(this.getSourceUrl());
            URLConnection urlConnection = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null && inputLine.trim().length() != 0) {
                if (log.isDebugEnabled()) log.debug(inputLine);
                allowedValues.add(inputLine);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // you need to set the super class, apparently in super class they used
        // the vairiable directly
        if (allowedValues.size() == 0) allowedValues.add("");
        super.setAllowedValues(allowedValues);
        return allowedValues;
    }

    /**
     * Set the values that are allowed by the constraint.
     * 
     * @param values
     *            a list of allowed values
     */
    @SuppressWarnings("unchecked")
    public void setAllowedValues(List allowedValues) {
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }
}