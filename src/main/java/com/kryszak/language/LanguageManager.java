package com.kryszak.language;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Observable;
import java.util.ResourceBundle;

import static com.kryszak.config.ApplicationConfiguration.getProperty;

public class LanguageManager extends Observable {

    public static final String PL = "pl";

    public static final String EN = "en";

    private static final String BUNDLE_NAME = getProperty("application.resource.bundle.name");

    private static LanguageManager instance;

    private ResourceBundle resources;

    private Map<String, Locale> locales;

    private Locale selectedLocale;

    private LanguageManager() {
        initializeLocales();
        this.resources = ResourceBundle.getBundle(BUNDLE_NAME, this.getSelectedLocale());
    }
    private void initializeLocales() {
        this.locales = new HashMap<>();
        this.locales.put(PL, new Locale(PL));
        this.locales.put(EN, new Locale(EN));
        this.selectedLocale = this.locales.get(EN);
    }

    public static LanguageManager getInstance() {
        if (instance == null) {
            instance = new LanguageManager();
        }
        return instance;
    }

    public ResourceBundle getResources() {
        return resources;
    }


    public Locale getSelectedLocale() {
        return selectedLocale;
    }
    public void setSelectedLocale(String selectedLocale) {
        this.selectedLocale = this.locales.get(selectedLocale);
        this.updateResources();
    }
    private synchronized void updateResources() {
        this.resources = ResourceBundle.getBundle(BUNDLE_NAME, this.getSelectedLocale());
        this.setChanged();
        this.notifyObservers();
    }
}
