/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vetau.dto;

import java.math.BigDecimal;

public class LabelValueDTO {

    private String label;
    private int count;
    private BigDecimal value;

    public LabelValueDTO() {
    }

    public LabelValueDTO(String label, int count, BigDecimal value) {
        this.label = label;
        this.count = count;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public int getCount() {
        return count;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}