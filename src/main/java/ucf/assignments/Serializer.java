/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Joseph Crown
 */
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

    // converts a serialized item to an html table row
    String serializeToHTML() {
        String formattedRow = "  \t\t<tr>\n" +
                "  \t\t\t<td>" + this.serialNumber + "</td>\n" +
                "  \t\t\t<td>" + this.name + "</td>\n" +
                "  \t\t\t<td>" + String.format("%.2f",this.value) + "</td>\n" +
                "  \t\t</tr>\n";
        return formattedRow;
    }

}
