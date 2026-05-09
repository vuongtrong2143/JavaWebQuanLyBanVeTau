/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vetau.dto;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class DashboardStatsDTO {

    private BigDecimal revenueToday = BigDecimal.ZERO;
    private BigDecimal revenueThisMonth = BigDecimal.ZERO;
    private BigDecimal refundThisMonth = BigDecimal.ZERO;
    private BigDecimal netRevenueThisMonth = BigDecimal.ZERO;

    private int ticketsThisMonth;
    private int ordersWaitingPayment;
    private int paidOrdersThisMonth;
    private int newCustomersThisMonth;
    private int pendingRefunds;

    public BigDecimal getRevenueToday() {
        return revenueToday;
    }

    public void setRevenueToday(BigDecimal revenueToday) {
        this.revenueToday = safe(revenueToday);
    }

    public BigDecimal getRevenueThisMonth() {
        return revenueThisMonth;
    }

    public void setRevenueThisMonth(BigDecimal revenueThisMonth) {
        this.revenueThisMonth = safe(revenueThisMonth);
    }

    public BigDecimal getRefundThisMonth() {
        return refundThisMonth;
    }

    public void setRefundThisMonth(BigDecimal refundThisMonth) {
        this.refundThisMonth = safe(refundThisMonth);
    }

    public BigDecimal getNetRevenueThisMonth() {
        return netRevenueThisMonth;
    }

    public void setNetRevenueThisMonth(BigDecimal netRevenueThisMonth) {
        this.netRevenueThisMonth = safe(netRevenueThisMonth);
    }

    public int getTicketsThisMonth() {
        return ticketsThisMonth;
    }

    public void setTicketsThisMonth(int ticketsThisMonth) {
        this.ticketsThisMonth = ticketsThisMonth;
    }

    public int getOrdersWaitingPayment() {
        return ordersWaitingPayment;
    }

    public void setOrdersWaitingPayment(int ordersWaitingPayment) {
        this.ordersWaitingPayment = ordersWaitingPayment;
    }

    public int getPaidOrdersThisMonth() {
        return paidOrdersThisMonth;
    }

    public void setPaidOrdersThisMonth(int paidOrdersThisMonth) {
        this.paidOrdersThisMonth = paidOrdersThisMonth;
    }

    public int getNewCustomersThisMonth() {
        return newCustomersThisMonth;
    }

    public void setNewCustomersThisMonth(int newCustomersThisMonth) {
        this.newCustomersThisMonth = newCustomersThisMonth;
    }

    public int getPendingRefunds() {
        return pendingRefunds;
    }

    public void setPendingRefunds(int pendingRefunds) {
        this.pendingRefunds = pendingRefunds;
    }

    public String getRevenueTodayText() {
        return formatMoney(revenueToday);
    }

    public String getRevenueThisMonthText() {
        return formatMoney(revenueThisMonth);
    }

    public String getRefundThisMonthText() {
        return formatMoney(refundThisMonth);
    }

    public String getNetRevenueThisMonthText() {
        return formatMoney(netRevenueThisMonth);
    }

    private BigDecimal safe(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    private String formatMoney(BigDecimal value) {
        NumberFormat nf = NumberFormat.getInstance(new Locale("vi", "VN"));
        return nf.format(safe(value)) + " đ";
    }
}