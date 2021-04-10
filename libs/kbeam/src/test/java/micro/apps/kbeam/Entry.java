package micro.apps.kbeam;

import org.apache.beam.sdk.coders.AvroCoder;
import org.apache.beam.sdk.coders.DefaultCoder;

@DefaultCoder(AvroCoder.class)
public class Entry {
    private String name;
    private String countryCode;
    private Double doubleValue;
    private String countryName;

    private Entry() {
    }

    Entry(String name, String countryCode, Double doubleValue) {
        this(name, countryCode, doubleValue, "Unknown");
    }

    Entry(String name, String countryCode, Double doubleValue, String countryName) {
        this.name = name;
        this.countryCode = countryCode;
        this.doubleValue = doubleValue;
        this.countryName = countryName;
    }

    String getName() {
        return name;
    }

    String getCountryCode() {
        return countryCode;
    }

    Double getDoubleValue() {
        return doubleValue;
    }

    public String getCountryName() {
        return countryName;
    }

    @Override
    public String toString() {
        return "KEntry{" +
            "name='" + name + '\'' +
            ", countryCode='" + countryCode + '\'' +
            ", doubleValue=" + doubleValue +
            ", countryName='" + countryName + '\'' +
            '}';
    }
}
