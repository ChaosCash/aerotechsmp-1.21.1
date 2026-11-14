package de.chaos_cash.aerotechsmp.datagen.Language;


public enum MenuNames implements ITranslation {
    ShipProtector
    ;

    public final String key;

    MenuNames() {
        this.key = getTranslationKey();
    }


    String getTranslationKey() {
        return ITranslation.getTranslationKey(this.name(), "menu_names");
    }


    @Override
    public String getKey() {
        return key;
    }
}
