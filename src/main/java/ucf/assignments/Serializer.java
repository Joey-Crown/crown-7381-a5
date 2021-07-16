package ucf.assignments;

import com.google.gson.annotations.SerializedName;

public class Serializer {

    @SerializedName("serialNumber")
    String serialNumber;
    @SerializedName("name")
    String name;
    @SerializedName("value")
    double value;

    public Serializer(String serialNumber, String name, double value) {
        this.serialNumber = serialNumber;
        this.name = name;
        this.value = value;
    }

    Serializer serializeItem(Item item) {
        return new Serializer(item.getSerialNumber(), item.getName(), item.getValue());
    }
}
