/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vetau.dto;

public class SystemCheckItemDTO {

    private String groupName;
    private String checkName;
    private String status;
    private String message;
    private int countValue;

    public SystemCheckItemDTO() {
    }

    public SystemCheckItemDTO(String groupName, String checkName, String status, String message, int countValue) {
        this.groupName = groupName;
        this.checkName = checkName;
        this.status = status;
        this.message = message;
        this.countValue = countValue;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getCheckName() {
        return checkName;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public int getCountValue() {
        return countValue;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setCheckName(String checkName) {
        this.checkName = checkName;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCountValue(int countValue) {
        this.countValue = countValue;
    }

    public boolean isOk() {
        return "OK".equalsIgnoreCase(status);
    }

    public boolean isWarning() {
        return "WARNING".equalsIgnoreCase(status);
    }

    public boolean isError() {
        return "ERROR".equalsIgnoreCase(status);
    }
}