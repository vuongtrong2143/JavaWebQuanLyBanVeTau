/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vetau.dto;

import java.math.BigDecimal;

public class MonthlyRevenueDTO {

    private String label;
    private BigDecimal revenue;
    private BigDecimal refund;
    private BigDecimal netRevenue;
    private int ticketCount;

    public MonthlyRevenueDTO() {
    }

    public MonthlyRevenueDTO(String label, BigDecimal revenue, BigDecimal refund, BigDecimal netRevenue, int ticketCount) {
        this.label = label;
        this.revenue = revenue;
        this.refund = refund;
        this.netRevenue = netRevenue;
        this.ticketCount = ticketCount;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public BigDecimal getRevenue() {
        return revenue;
    }

    public void setRevenue(BigDecimal revenue) {
        this.revenue = revenue;
    }

    public BigDecimal getRefund() {
        return refund;
    }

    public void setRefund(BigDecimal refund) {
        this.refund = refund;
    }

    public BigDecimal getNetRevenue() {
        return netRevenue;
    }

    public void setNetRevenue(BigDecimal netRevenue) {
        this.netRevenue = netRevenue;
    }

    public int getTicketCount() {
        return ticketCount;
    }

    public void setTicketCount(int ticketCount) {
        this.ticketCount = ticketCount;
    }
}